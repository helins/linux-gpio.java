package io.dvlopt.linux.gpio ;


import com.sun.jna.NativeLong               ;
import io.dvlopt.linux.LinuxException       ;
import io.dvlopt.linux.gpio.GpioChipInfo    ;
import io.dvlopt.linux.gpio.GpioLineInfo    ;
import io.dvlopt.linux.gpio.GpioEventHandle ;
import io.dvlopt.linux.gpio.GpioHandle      ;
import io.dvlopt.linux.io.LinuxIO           ;




/**
 * Class for requesting and handling a GPIO device.
 * <p>
 * Nowadays, a single machine can have several GPIO chips providing lines. Using this class, the
 * user can request one, obtain information about it and handle it.
 */
public class GpioDevice implements AutoCloseable {


    private static final NativeLong GPIO_GET_CHIPINFO_IOCTL   = new NativeLong( 2151986177L ,
                                                                                true        ) ; 

    private static final NativeLong GPIO_GET_LINEINFO_IOCTL   = new NativeLong( 3225990146L ,
                                                                                true        ) ;

    private static final NativeLong GPIO_GET_LINEHANDLE_IOCTL = new NativeLong( 3245126659L ,
                                                                                true        ) ;

    private static final NativeLong GPIO_GET_LINEEVENT_IOCTL  = new NativeLong( 3224417284L ,
                                                                                true        ) ;




    private int fd ;




    /**
     * Opens the GPIO device located at the given path.
     *
     * @param path  Path to the GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioDevice( String path ) throws LinuxException {

        int fd = LinuxIO.open64( path           ,
                                 LinuxIO.O_RDWR ) ;

        if ( fd < 0 ) throw new LinuxException( "Unable to open GPIO device" ) ;

        this.fd = fd ;
    }




    /**
     * Opens a GPIO device by number.
     *
     * @param number  Number of the GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioDevice( int number ) throws LinuxException {
    
        this( "/dev/gpiochip" + number ) ;
    }




    /**
     * Closes this GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public void close() throws LinuxException {

        if ( LinuxIO.close( this.fd ) != 0 ) {
        
            throw new LinuxException( "Unable to close GPIO device" ) ;
        }
    }




    /**
     * Obtains information about this GPIO device.
     *
     * @return  Basic information about this GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioChipInfo requestChipInfo() throws LinuxException {
    
        return this.requestChipInfo( new GpioChipInfo() ) ;
    }




    /**
     * Obtains information about this GPIO device and writes it to `<code>info</code>`.
     *
     * @param info  Where the data will be written.
     *
     * @return  Basic information about this GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioChipInfo requestChipInfo( GpioChipInfo info ) throws LinuxException {
    
       if ( LinuxIO.ioctl( this.fd                        ,
                           GPIO_GET_CHIPINFO_IOCTL        ,
                           info.nativeStruct.getPointer() ) < 0 ) {
           
           throw new LinuxException( "Unable to retrieve informations about GPIO device" ) ;
       }

       info.nativeStruct.read() ;

       return info ;
    }




    /**
     * Obtains information about a particular GPIO line from this GPIO device.
     *
     * @param line  The number of the line.
     *
     * @return  Basic information about the GPIO line.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioLineInfo requestLineInfo( int line ) throws LinuxException {
    
        return this.requestLineInfo( new GpioLineInfo( line ) ) ;
    }




    /**
     * Obtains information about a particular GPIO line from this GPIO device and writes it to `<code>info</code>`.
     *
     * @param info  Where the data will be written.
     *
     * @return  Basic information about the GPIO line.
     *
     * @throws LinuxException  When something fails on the native side.
     */
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




    /**
     * Obtains a GPIO handle for handling the requested GPIO lines.
     *
     * @param request  Request specifying what lines will be handled and how.
     *
     * @return  A GPIO handle.
     *
     * @throws LinuxException  When something fails on the native side.
     */
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




    /**
     * Obtains a GPIO event handle for a GPIO line.
     * <p>
     * This handle can be used to read the current value of the line or wait for an interrupt.
     *
     * @param request  Request specifying which line will be monitored and how.
     *
     * @return  A GPIO event handle.
     *
     * @throws LinuxException  When something fails on the native side.
     */
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
