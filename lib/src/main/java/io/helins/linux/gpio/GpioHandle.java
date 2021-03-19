/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import com.sun.jna.NativeLong          ;
import io.helins.linux.Linux           ;
import io.helins.linux.gpio.GpioBuffer ;
import io.helins.linux.io.LinuxIO      ;
import java.io.IOException             ;




/**
 * Class for controlling requested GPIO lines.
 * <p>
 * Each line driven by a handle is referred to by its index, as specified in the request, rather than by its number.
 */
public class GpioHandle implements AutoCloseable {


    //
    // IOCTL requests.
    //
    static final NativeLong GPIOHANDLE_GET_LINE_VALUES_IOCTL = new NativeLong( 3225465864L ,
                                                                               true        ) ;

    static final NativeLong GPIOHANDLE_SET_LINE_VALUES_IOCTL = new NativeLong( 3225465865L ,
                                                                               true        ) ;


    // Associated file descriptor.
    //
    final int fd ;


    // Bookkeeping of state.
    //
    private boolean isClosed = false ;




    // Private constructor
    //
    GpioHandle( int fd ) {
    
        this.fd = fd   ;
    }




    /**
     * Closes this GPIO handle and releases resources.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public void close() throws IOException {

        if ( this.isClosed == false ) {

            if ( LinuxIO.close( this.fd ) != 0 ) {
            
                throw new IOException( "Native error while closing GPIO handle : errno " + Linux.getErrno() ) ;
            }

            this.isClosed = true ;
        }
    }




    // Throws an IllegalStateException if the handle is closed.
    // Meant to be used before IO operations.
    //
    private void guardClosed() {
    
        if ( this.isClosed ) {
        
            throw new IllegalStateException( "Unable to perform IO operation on closed GPIO handle" ) ;
        }
    }




    /**
     * Reads the current state of the lines this handle controls and writes it back to the given
     * buffer.
     *
     * @param  buffer
     *           Buffer meant to store the data.
     *
     * @throws IllegalStateException
     *           When the handle has been closed.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public void read( GpioBuffer buffer ) throws IOException {

        this.guardClosed() ;
    
        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_GET_LINE_VALUES_IOCTL ,
                            buffer.getPointer()              ) < 0 ) {
        
            throw new IOException( "Native error while reading a GPIO handle : errno " + Linux.getErrno() ) ;
        }
    }




    /**
     * Writes the new state of the lines this handle controls using the given buffer.
     * <p>
     * Obviously, this methods does not do anything for inputs.
     *
     * @param  buffer
     *           Buffer holding the new state of the lines.
     *
     * @throws IllegalStateException
     *           When the handle has been closed.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public void write( GpioBuffer buffer ) throws IOException {

        this.guardClosed() ;

        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_SET_LINE_VALUES_IOCTL ,
                            buffer.getPointer()              ) < 0 ) {

            throw new IOException( "Native error while writing to a GPIO handle : errno " + Linux.getErrno() ) ;
        }
    }
}
