/*
 * Copyright 2018 Adam Helinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.dvlopt.linux.gpio ;


import com.sun.jna.NativeLong          ;
import io.dvlopt.linux.Linux           ;
import io.dvlopt.linux.gpio.GpioBuffer ;
import io.dvlopt.linux.io.LinuxIO      ;
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
