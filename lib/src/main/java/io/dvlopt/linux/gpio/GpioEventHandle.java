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


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.GpioBuffer                   ;
import io.dvlopt.linux.gpio.GpioEvent                    ;
import io.dvlopt.linux.gpio.GpioHandle                   ;
import io.dvlopt.linux.gpio.GpioLine                     ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




/**
 * Class for controlling a GPIO line that can be monitored for interrupts.
 *
 * @see GpioEventWatcher
 */
public class GpioEventHandle implements AutoCloseable {


    final int fd ;

    private final GpioLine line ;

    private boolean isClosed = false ;




    GpioEventHandle( int fd   ,
                     int line ) {

        this.fd   = fd                   ;
        this.line = new GpioLine( line ,
                                  0    ) ;
    }




    /**
     * Retrieves the GPIO line associated with this handle.
     *
     * @return The GPIO line.
     */
    public GpioLine getLine() {
    
        return this.line ;
    }




    /**
     * Reads the current state of the line this handle controls and write it back to the given
     * buffer.
     *
     * @param buffer  Buffer meant to hold the data.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public void read( GpioBuffer buffer ) throws LinuxException {
    
        if ( LinuxIO.ioctl( this.fd                                     ,
                            GpioHandle.GPIOHANDLE_GET_LINE_VALUES_IOCTL ,
                            buffer.getPointer()                         ) < 0 ) {
        
            throw new LinuxException( "Unable to read the line using the given GPIO event handle" ) ;
        }
    }




    /**
     * Waits for an event to happen.
     *
     * @return  Information about what happened.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEvent waitForEvent() throws LinuxException {

        return this.waitForEvent( new GpioEvent() ) ;
    }




    /**
     * Waits for an event to happen and writes what happened to the given `<code>data</code>` object.
     *
     * @param data  Object for holding data about what happened.
     *
     * @return  Information about what happened.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEvent waitForEvent( GpioEvent data ) throws LinuxException {

        data.read( this.fd              ,
                   this.line.lineNumber ) ;

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
            
                throw new LinuxException( "Unable to close this GPIO event handle" ) ;
            }

            this.isClosed = true ;
        }
    }
}
