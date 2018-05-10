package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException        ;
import io.dvlopt.linux.epoll.Epoll           ;
import io.dvlopt.linux.epoll.EpollEvent      ;
import io.dvlopt.linux.epoll.EpollEventFlag  ;
import io.dvlopt.linux.epoll.EpollEventFlags ;
import io.dvlopt.linux.gpio.GpioEventHandle  ;




public class GpioEventWatcher implements AutoCloseable {


    private Epoll      epoll      ;
    private EpollEvent epollEvent ;


    private static final EpollEventFlags eventFlags = new EpollEventFlags() ;


    

    static {
    
        eventFlags
            .set( EpollEventFlag.EPOLLIN  )
            .set( EpollEventFlag.EPOLLPRI ) ;
    }




    public GpioEventWatcher() throws LinuxException {
    
        this.epoll      = new Epoll()      ;
        this.epollEvent = new EpollEvent() ;
    }




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




    public GpioEventWatcher removeHandle( GpioEventHandle handle ) throws LinuxException {
    
        this.epoll.remove( handle.getFD() ) ;

        return this ;
    }




    public boolean waitForEvent( GpioEventData data ) throws LinuxException {
    
        return this.waitForEvent( data ,
                                  -1   ) ;
    }




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




    public void close() throws LinuxException {

        this.epoll.close() ;
    }
}
