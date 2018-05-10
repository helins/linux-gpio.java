package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException        ;
import io.dvlopt.linux.epoll.Epoll           ;
import io.dvlopt.linux.epoll.EpollEvent      ;
import io.dvlopt.linux.epoll.EpollEventFlag  ;
import io.dvlopt.linux.epoll.EpollEventFlags ;
import io.dvlopt.linux.gpio.GpioEventHandle  ;




/**
 * Class for efficiently monitoring several GPIO events at once.
 */
public class GpioEventWatcher implements AutoCloseable {


    private Epoll      epoll      ;
    private EpollEvent epollEvent ;


    private static final EpollEventFlags eventFlags = new EpollEventFlags() ;


    

    static {
    
        eventFlags
            .set( EpollEventFlag.EPOLLIN  )
            .set( EpollEventFlag.EPOLLPRI ) ;
    }




    /**
     * Basic constructor allocating the needed resources.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEventWatcher() throws LinuxException {
    
        this.epoll      = new Epoll()      ;
        this.epollEvent = new EpollEvent() ;
    }




    /**
     * Adds a GPIO event to monitor.
     *
     * @param handle  The GPIO event handle to monitor.
     *
     * @return  This GpioEventWatcher.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEventWatcher addHandle( GpioEventHandle handle ) throws LinuxException {

        int fd = handle.getFD() ;

        EpollEvent epollEvent = new EpollEvent() ;

        epollEvent
            .setEventFlags( eventFlags )
            .setUserData(   fd 
                          | (long)( handle.getLine() ) << 32 ) ;
    
        this.epoll.add( fd         ,
                        epollEvent ) ;

        return this ;
    }




    /**
     * Removes a GPIO event from being monitored.
     *
     * @param handle  The GPIO event handle to remove.
     *
     * @return  This GpioEventWatcher.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEventWatcher removeHandle( GpioEventHandle handle ) throws LinuxException {
    
        this.epoll.remove( handle.getFD() ) ;

        return this ;
    }




    /**
     * Waits forever until a GPIO event occurs.
     *
     * @param data  Where information about the event will be written.
     *
     * @return  A boolean whether something happened or not.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public boolean waitForEvent( GpioEventData data ) throws LinuxException {
    
        return this.waitForEvent( data ,
                                  -1   ) ;
    }




    /** Waits `<code>timeout</code>` milliseconds at most until a GPIO event occurs.
     *
     * @param data  Where information about the event will be written.
     *
     * @param timeout  A timeout in milliseconds where -1 means forever.
     *
     * @return  A boolean whether something happend or not within the given timeout.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public boolean waitForEvent( GpioEventData data    ,
                                 int           timeout ) throws LinuxException {

        if ( this.epoll.wait( this.epollEvent ,
                              timeout         ) > 0 ) {

            long longValue = this.epollEvent.getUserData() ;

            int fd   = (int)longValue            ;
            int line = (int)( longValue >>> 32 ) ;

            data.read( fd   ,
                       line ) ;

            return true ;
        }

        return false ;
    }




    /**
     * Closes this instance and release associated resources.
     * <p>
     * The previously registered GPIO event handles WILL NOT be closed.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public void close() throws LinuxException {

        this.epoll.close() ;
    }
}
