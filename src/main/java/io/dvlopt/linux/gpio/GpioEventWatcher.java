package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException       ;
import io.dvlopt.linux.epoll.Epoll          ;
import io.dvlopt.linux.epoll.EpollDataType  ;
import io.dvlopt.linux.epoll.EpollEvent     ;
import io.dvlopt.linux.epoll.EpollEvents    ;
import io.dvlopt.linux.gpio.GpioEventHandle ;




public class GpioEventWatcher implements AutoCloseable {


    private Epoll      epoll ;
    private EpollEvent event ;



    public GpioEventWatcher() throws LinuxException {
    
        this.epoll = new Epoll()      ;
        this.event = new EpollEvent() ;
                
        event.selectDataType( EpollDataType.LONG ) ;
    }


    public GpioEventWatcher add( GpioEventHandle eventHandle ) throws LinuxException {

        int fd = eventHandle.getFD() ;

        EpollEvent event = new EpollEvent() ;

        event
            .setEvents(   Epoll.EPOLLIN
                        | Epoll.EPOLLPRI )
            .selectDataType( EpollDataType.LONG )
            .setDataLong(   fd 
                          | (long)( eventHandle.getLine() ) << 32 ) ;
    
        this.epoll.add( fd    ,
                        event ) ;

        return this ;
    }


    public GpioEventWatcher remove( GpioEventHandle eventHandle ) throws LinuxException {
    
        this.epoll.remove( eventHandle.getFD() ) ;

        return this ;
    }


    public GpioEventData wait( GpioEventData eventData ,
                               int           timeout   ) throws LinuxException {

        if ( this.epoll.wait( this.event ,
                              timeout    ) > 0 ) {

            long longValue = this.event.getDataLong() ;

            int fd   = (int)longValue            ;
            int line = (int)( longValue >>> 32 ) ;

            return eventData.readEvent( fd   ,
                                        line ) ;
        }

        return null ;
    }


    public void close() throws LinuxException {

        this.epoll.close() ;
    }
}
