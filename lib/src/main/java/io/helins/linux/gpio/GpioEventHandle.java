/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import io.helins.linux.Linux                             ;
import io.helins.linux.gpio.GpioBuffer                   ;
import io.helins.linux.gpio.GpioEvent                    ;
import io.helins.linux.gpio.GpioHandle                   ;
import io.helins.linux.gpio.GpioLine                     ;
import io.helins.linux.gpio.internal.NativeGpioEventData ;
import io.helins.linux.io.LinuxIO                        ;
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
