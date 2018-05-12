package io.dvlopt.linux.gpio ;


import com.sun.jna.NativeLong              ;
import io.dvlopt.linux.LinuxException      ;
import io.dvlopt.linux.gpio.GpioHandleData ;
import io.dvlopt.linux.io.LinuxIO          ;




/**
 * Class for controlling requested GPIO lines.
 * <p>
 * Each line driven by a handle is referred to by its index, as specified in the request, rather than by its number.
 */
public class GpioHandle implements AutoCloseable {


    private static final NativeLong GPIOHANDLE_GET_LINE_VALUES_IOCTL = new NativeLong( 3225465864L ,
                                                                                       true        ) ;

    private static final NativeLong GPIOHANDLE_SET_LINE_VALUES_IOCTL = new NativeLong( 3225465865L ,
                                                                                       true        ) ;


    final int fd ;

    private boolean isClosed = false ;


    /**
     * How many lines this handle controls.
     */
    public final int size ;




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




    /**
     * Reads the current state of the lines this handle controls.
     *
     * @return  Data containing the state of the lines.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioHandleData read() throws LinuxException {
    
        return this.read( new GpioHandleData( this.size ) ) ;
    }



    
    /**
     * Reads the current state of the lines this handle controls and writes it back to the given
     * data object which has to be able to hold that many lines.
     *
     * @param data  Object holding the data.
     *
     * @return  Data containing the state of the lines.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioHandleData read( GpioHandleData data ) throws LinuxException {

        this.checkBounds( data ) ;
    
        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_GET_LINE_VALUES_IOCTL ,
                            data.getPointer()                ) < 0 ) {
        
            throw new LinuxException( "Unable to read values for the given GPIO handle" ) ;
        }

        return data ;
    }




    /**
     * Writes the new state of the lines this handle controls with a data object that has to provide
     * state for at least that many lines.
     *
     * @param data  Object holding the new state of the lines.
     *
     * @return  This GpioHandle.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioHandleData write( GpioHandleData data ) throws LinuxException {

        this.checkBounds( data ) ;

        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_SET_LINE_VALUES_IOCTL ,
                            data.getPointer()                ) < 0 ) {

            throw new LinuxException( "Unable to write values for the given GPIO handle" ) ;
        }

        return data ;
    }




    /**
     * Closes this GPIO handle and releases resources.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public void close() throws LinuxException {

        if ( this.isClosed == false ) {

            if ( LinuxIO.close( this.fd ) != 0 ) {
            
                throw new LinuxException( "Unable to close this GPIO handle" ) ;
            }

            this.isClosed = true ;
        }
    }
}
