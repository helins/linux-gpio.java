package io.dvlopt.linux.gpio ;


import com.sun.jna.NativeLong              ;
import io.dvlopt.linux.LinuxException      ;
import io.dvlopt.linux.gpio.GpioHandleData ;
import io.dvlopt.linux.io.LinuxIO          ;




public class GpioHandle implements AutoCloseable {


    public static final NativeLong GPIOHANDLE_GET_LINE_VALUES_IOCTL = new NativeLong( 3225465864L ,
                                                                                      true        ) ;

    public static final NativeLong GPIOHANDLE_SET_LINE_VALUES_IOCTL = new NativeLong( 3225465865L ,
                                                                                      true        ) ;



    protected       int fd   ;
    public    final int size ;



    GpioHandle( int fd   ,
                int size ) {
    
        this.fd   = fd   ;
        this.size = size ;
    }



    private void checkBounds( GpioHandleData data ) {
    
        if ( data.size < this.size ) {
        
            throw new IllegalArgumentException( "The size of data must be at least as large as the number of lines this handle handles." ) ;
        }
    }




    public GpioHandleData read() throws LinuxException {
    
        return this.read( new GpioHandleData( this.size ) ) ;
    }




    public GpioHandleData read( GpioHandleData data ) throws LinuxException {

        this.checkBounds( data ) ;
    
        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_GET_LINE_VALUES_IOCTL ,
                            data.getPointer()                ) < 0 ) {
        
            throw new LinuxException( "Unable to read values for the given GPIO handle" ) ;
        }

        return data ;
    }




    public GpioHandleData write( GpioHandleData data ) throws LinuxException {

        this.checkBounds( data ) ;

        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_SET_LINE_VALUES_IOCTL ,
                            data.getPointer()                ) < 0 ) {

            throw new LinuxException( "Unable to write values for the given GPIO handle" ) ;
        }

        return data ;
    }




    public void close() throws LinuxException {

        if ( LinuxIO.close( this.fd ) != 0 ) {
        
            throw new LinuxException( "Unable to close this GPIO handle" ) ;
        }
    }
}
