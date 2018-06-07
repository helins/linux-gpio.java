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


import io.dvlopt.linux.Linux                             ;
import io.dvlopt.linux.gpio.GpioBuffer                   ;
import io.dvlopt.linux.gpio.GpioEvent                    ;
import io.dvlopt.linux.gpio.GpioHandle                   ;
import io.dvlopt.linux.gpio.GpioLine                     ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;
import java.io.IOException                               ;




/**
 * Class for controlling a GPIO line that can be monitored for interrupts.
 *
 * @see GpioEventWatcher
 */
public class GpioEventHandle implements AutoCloseable {


    // File descriptor associated with this handle.
    //
    final int fd ;


    // Line associated with this handle.
    //
    private final GpioLine line ;


    // Bookkeeping the state of this handle.
    //
    private boolean isClosed = false ;




    // Private constructor.
    //
    GpioEventHandle( int fd   ,
                     int line ) {

        this.fd   = fd                   ;
        this.line = new GpioLine( line ,
                                  0    ) ;
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
            
                throw new IOException( "Native error while closing a GPIO event handle for line " + this.line.lineNumber + " : errno " + Linux.getErrno() ) ;
            }

            this.isClosed = true ;
        }
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
     * @param  buffer
     *           Buffer meant to hold the data.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public void read( GpioBuffer buffer ) throws IOException {
    
        if ( LinuxIO.ioctl( this.fd                                     ,
                            GpioHandle.GPIOHANDLE_GET_LINE_VALUES_IOCTL ,
                            buffer.getPointer()                         ) < 0 ) {
        
            throw new IOException( "Native error while reading GPIO event handle for line " + this.line.lineNumber + " : errno " + Linux.getErrno() ) ;
        }
    }




    /**
     * Waits for an event to happen.
     *
     * @return  The event.
     *
     * @throws  IOException
     *            When an unplanned error occured.
     */
    public GpioEvent waitForEvent() throws IOException {

        return this.waitForEvent( new GpioEvent() ) ;
    }




    /**
     * Waits for an event to happen and writes what happened to the given `<strong>data</strong>` object.
     *
     * @param  data
     *           Will hold data about what happened.
     *
     * @return The event.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public GpioEvent waitForEvent( GpioEvent data ) throws IOException {

        data.read( this.fd              ,
                   this.line.lineNumber ) ;

        return data ;
    }
}
