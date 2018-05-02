package io.dvlopt.linux.gpio ;


import com.sun.jna.NativeLong               ;
import io.dvlopt.linux.LinuxException       ;
import io.dvlopt.linux.gpio.GpioChipInfo    ;
import io.dvlopt.linux.gpio.GpioLineInfo    ;
import io.dvlopt.linux.gpio.GpioEventHandle ;
import io.dvlopt.linux.gpio.GpioHandle      ;
import io.dvlopt.linux.io.LinuxIO           ;




public class GpioDevice implements AutoCloseable {


    public static final NativeLong GPIO_GET_CHIPINFO_IOCTL   = new NativeLong( 2151986177L ,
                                                                               true        ) ; 

    public static final NativeLong GPIO_GET_LINEINFO_IOCTL   = new NativeLong( 3225990146L ,
                                                                               true        ) ;

    public static final NativeLong GPIO_GET_LINEHANDLE_IOCTL = new NativeLong( 3245126659L ,
                                                                               true        ) ;

    public static final NativeLong GPIO_GET_LINEEVENT_IOCTL  = new NativeLong( 3224417284L ,
                                                                               true        ) ;




    private int fd ;




    private GpioDevice( int fd ) {
    
        this.fd = fd ;
    }




    public GpioDevice( String path ) throws LinuxException {

        int fd = LinuxIO.open64( path           ,
                                 LinuxIO.O_RDWR ) ;

        if ( fd < 0 ) throw new LinuxException( "Unable to open GPIO device" ) ;

        this.fd = fd ;
    }




    public void close() throws LinuxException {

        if ( LinuxIO.close( this.fd ) != 0 ) {
        
            throw new LinuxException( "Unable to close GPIO device" ) ;
        }
    }




    public GpioChipInfo requestChipInfo() throws LinuxException {
    
        return this.requestChipInfo( new GpioChipInfo() ) ;
    }




    public GpioChipInfo requestChipInfo( GpioChipInfo info ) throws LinuxException {
    
       if ( LinuxIO.ioctl( this.fd                        ,
                           GPIO_GET_CHIPINFO_IOCTL        ,
                           info.nativeStruct.getPointer() ) < 0 ) {
           
           throw new LinuxException( "Unable to retrieve informations about GPIO device" ) ;
       }

       info.nativeStruct.read() ;

       return info ;
    }




    public GpioLineInfo requestLineInfo( int line ) throws LinuxException {
    
        return this.requestLineInfo( new GpioLineInfo( line ) ) ;
    }




    public GpioLineInfo requestLineInfo( GpioLineInfo info ) throws LinuxException {

        info.nativeStruct.writeField( "lineOffset" ) ;  // TODO check performance
    
        if ( LinuxIO.ioctl( this.fd                        ,
                            GPIO_GET_LINEINFO_IOCTL        ,
                            info.nativeStruct.getPointer() ) < 0 ) {
            
            throw new LinuxException( "Unable to retrieve information about the request GPIO line" ) ;
        }

        info.nativeStruct.read() ;  // TODO select which field to read ? for performance ?

        return info ;
    }




    public GpioHandle requestHandle( GpioHandleRequest request ) throws LinuxException {
    
        request.nativeStruct.write() ;

        if ( LinuxIO.ioctl( this.fd                           ,
                            GPIO_GET_LINEHANDLE_IOCTL         ,
                            request.nativeStruct.getPointer() ) < 0 ) {
        
            throw new LinuxException( "Unable to provide a GPIO handle" ) ;
        }

        request.nativeStruct.read() ;  // TODO read only fd

        return new GpioHandle( request.nativeStruct.fd    ,
                               request.nativeStruct.lines ) ;
    }




    public GpioEventHandle requestEvent( GpioEventRequest request ) throws LinuxException {
    
        request.nativeStruct.write() ;

        if ( LinuxIO.ioctl( this.fd                           ,
                            GPIO_GET_LINEEVENT_IOCTL          ,
                            request.nativeStruct.getPointer() ) < 0 ) {
        
            throw new LinuxException( "Unable to provide a GPIO event handle" ) ;
        }

        request.nativeStruct.read() ; // TODO read only FD

        return new GpioEventHandle( request.nativeStruct.fd ,
                                    request.getLine()       ) ;
    }
}
