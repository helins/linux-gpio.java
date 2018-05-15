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
import io.dvlopt.linux.LinuxException  ;
import io.dvlopt.linux.gpio.GpioBuffer ;
import io.dvlopt.linux.io.LinuxIO      ;




/**
 * Class for controlling requested GPIO lines.
 * <p>
 * Each line driven by a handle is referred to by its index, as specified in the request, rather than by its number.
 */
public class GpioHandle implements AutoCloseable {


    static final NativeLong GPIOHANDLE_GET_LINE_VALUES_IOCTL = new NativeLong( 3225465864L ,
                                                                               true        ) ;

    static final NativeLong GPIOHANDLE_SET_LINE_VALUES_IOCTL = new NativeLong( 3225465865L ,
                                                                               true        ) ;


    final int fd ;

    private boolean isClosed = false ;




    GpioHandle( int fd ) {
    
        this.fd = fd   ;
    }




    /**
     * Reads the current state of the lines this handle controls and writes it back to the given
     * buffer.
     *
     * @param buffer  Buffer meant to store the data.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public void read( GpioBuffer buffer ) throws LinuxException {
    
        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_GET_LINE_VALUES_IOCTL ,
                            buffer.getPointer()              ) < 0 ) {
        
            throw new LinuxException( "Unable to read values for the given GPIO handle" ) ;
        }
    }




    /**
     * Writes the new state of the lines this handle controls using the given buffer.
     * <p>
     * Obviously, this methods does not do much for inputs.
     *
     * @param buffer  Buffer holding the new state of the lines.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public void write( GpioBuffer buffer ) throws LinuxException {

        if ( LinuxIO.ioctl( this.fd                          ,
                            GPIOHANDLE_SET_LINE_VALUES_IOCTL ,
                            buffer.getPointer()              ) < 0 ) {

            throw new LinuxException( "Unable to write values for the given GPIO handle" ) ;
        }
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
