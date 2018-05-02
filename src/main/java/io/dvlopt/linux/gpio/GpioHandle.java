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



    protected int fd ;



    public GpioHandle( int fd ) {
    
        this.fd = fd ;
    }


    public GpioHandleData readValues() throws LinuxException {
    
        return this.readValues( new GpioHandleData() ) ;
    }


    public GpioHandleData readValues( GpioHandleData data ) throws LinuxException {
    
        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_GET_LINE_VALUES_IOCTL ,
                            data.nativeStruct.getPointer()   ) < 0 ) {
        
            throw new LinuxException( "Unable to read values for the given GPIO handle" ) ;
        }

        data.nativeStruct.read() ;

        return data ;
    }


    public GpioHandleData writeValues( GpioHandleData data ) throws LinuxException {

        data.nativeStruct.write() ;

        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_SET_LINE_VALUES_IOCTL ,
                            data.nativeStruct.getPointer()   ) < 0 ) {

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
