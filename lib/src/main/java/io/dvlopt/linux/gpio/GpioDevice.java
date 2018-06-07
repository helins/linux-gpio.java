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

import com.sun.jna.Pointer ;


import com.sun.jna.NativeLong               ;
import io.dvlopt.linux.Linux                ;
import io.dvlopt.linux.errno.Errno          ;
import io.dvlopt.linux.gpio.GpioChipInfo    ;
import io.dvlopt.linux.gpio.GpioLineInfo    ;
import io.dvlopt.linux.gpio.GpioEventHandle ;
import io.dvlopt.linux.gpio.GpioHandle      ;
import io.dvlopt.linux.io.LinuxIO           ;
import java.io.FileNotFoundException        ;
import java.io.IOException                  ;




/**
 * Class for requesting and handling a GPIO device.
 * <p>
 * Nowadays, a single machine can have several GPIO chips providing lines. Typically, such a device
 * is available at '/dev/gpiochipX' where `X` is the number of the chip.
 * <p>
 * Only one process can request a specific GPIO device at once. The user can then retrieve information
 * about the chip itself or a given GPIO line.
 * <p>
 * A handle can be requested and obtained for driving
 * one or several GPIO lines. When several lines are handled at once, whether doing any kind of IO is
 * atomic depends on the underlying driver. For instance, there is no garantee that writing to several
 * lines at once will indeed happen at the exact time for each line. This fact is opaque to this library and
 * user space in general. Reading and writing the state of the lines is done using an additional buffer.
 * <p>
 * An input line can be monitored for events by requesting an event handle. This event handle can be used
 * for blocking until a declared event happens, such as the line transitioning from low to high. Once an
 * event handle is obtained, the requested events are queued by the kernel until read by the user. However,
 * Linux not being a real-time operating system out of the box, such interrupts from user space should not be
 * used for highly critical tasks. For such matters, a simple microcontroller is prefered.
 * <p>
 * Instead of using one thread per monitored pin, the user can start an event watcher and add several event handles.
 * The event watcher can then be used to wait until one of the registered lines transitions to its relevant state,
 * effectively using one thread for monitoring several lines at once.
 * <p>
 * A GPIO line, just like a GPIO device, can only be requested and handled by one instance at a time. When a handle
 * is closed, all associated resources are cleaned-up by the kernel. A GPIO line is associated with a consumer, an
 * optional string provided by the user describing "who" is controlling that line.
 * <p>
 * Closing a GPIO device does not close the acquired handles. Hence, it is a good practise to close it when it is
 * not needed anymore.
 */
public class GpioDevice implements AutoCloseable {


    //
    // IOCTL requests.
    //

    private static final NativeLong GPIO_GET_CHIPINFO_IOCTL   = new NativeLong( 2151986177L ,
                                                                                true        ) ; 

    private static final NativeLong GPIO_GET_LINEINFO_IOCTL   = new NativeLong( 3225990146L ,
                                                                                true        ) ;

    private static final NativeLong GPIO_GET_LINEHANDLE_IOCTL = new NativeLong( 3245126659L ,
                                                                                true        ) ;

    private static final NativeLong GPIO_GET_LINEEVENT_IOCTL  = new NativeLong( 3224417284L ,
                                                                                true        ) ;




    // Associated file descriptor.
    //
    private int fd ;


    // Bookkeeping of state.
    //
    private boolean isClosed = false ;




    /**
     * Opens the GPIO device located at the given path.
     *
     * @param  path
     *           Path to the GPIO device.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public GpioDevice( String path ) throws IOException {

        int fd = LinuxIO.open64( path             ,
                                 LinuxIO.O_RDONLY ) ;

        if ( fd < 0 ) {

            int errno = Linux.getErrno() ;

            switch ( errno ) {

                case Errno.EACCES : throw new FileNotFoundException( "Permission denied : " + path )                                     ;

                case Errno.ENOENT : throw new FileNotFoundException( "No such file : " + path )                                          ;
        
                default           : throw new IOException( "Native error while opening GPIO device at '" + path + "' : errno " + errno ) ;
            }
        }

        this.fd = fd ;
    }




    /**
     * Opens a GPIO device by number.
     *
     * @param  number
     *           Number of the GPIO device.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public GpioDevice( int number ) throws IOException {
    
        this( "/dev/gpiochip" + number ) ;
    }




    /**
     * Closes this GPIO device.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public void close() throws IOException {

        if ( this.isClosed == false ) {

            if ( LinuxIO.close( this.fd ) != 0 ) {
            
                throw new IOException( "Native error while closing GPIO device : errno " + Linux.getErrno() ) ;
            }

            this.isClosed = true ;
        }
    }




    // Throws an IllegalStateException if the GPIO device is closed.
    //
    private void guardClosed() {
    
        if ( this.isClosed ) {
        
            throw new IllegalStateException( "Unable to perform IO operation on a closed GPIO device" ) ;
        }
    }




    // Throws an IOException if errno seems to indicate that the GPIO device is inappropriate.
    // For instance, it is possible to open a regular file and obviously, nothing will work.
    //
    private static void throwIfBadDevice( int errno ) throws IOException {
    
        if ( errno == Errno.ENOTTY ) {
        
            throw new IOException( "Device does not support this GPIO operation" ) ;
        }
    }




    /**
     * Obtains information about this GPIO device.
     *
     * @return Information.
     *
     * @throws IllegalStateException
     *           When the GPIO device has been closed.
     *
     * @throws IOException
     *           When the device is not a proper GPIO device or an unplanned error occured.
     */
    public GpioChipInfo requestChipInfo() throws IOException {
    
        return this.requestChipInfo( new GpioChipInfo() ) ;
    }




    /**
     * Obtains information about this GPIO device and writes it to `<code>info</code>`.
     *
     * @param  info
     *           Will be overwritten.
     *
     * @return Information.
     *
     * @throws IllegalStateException
     *           When the GPIO device has been closed.
     *
     * @throws IOException
     *           When the device is not a proper GPIO device or an unplanned error occured.
     */
    public GpioChipInfo requestChipInfo( GpioChipInfo info ) throws IOException {

        this.guardClosed() ;
    
        if ( LinuxIO.ioctl( this.fd                 ,
                            GPIO_GET_CHIPINFO_IOCTL ,
                            info.memory             ) < 0 ) {

            int errno = Linux.getErrno() ;

            throwIfBadDevice( errno ) ;

            throw new IOException( "Native error while requesting chip information : errno " + errno ) ;
        }

        return info ;
    }




    /**
     * Obtains information about a particular GPIO line from this GPIO device.
     *
     * @param  line 
     *           Number of the line.
     *
     * @return Information.
     *
     * @throws IllegalStateException
     *           When the GPIO device has been closed.
     *
     * @throws IOException
     *           When the device is not a proper GPIO device or an unplanned error occured.
     */
    public GpioLineInfo requestLineInfo( int line ) throws IOException {
    
        return this.requestLineInfo( line               ,
                                     new GpioLineInfo() ) ;
    }




    /**
     * Obtains information about a particular GPIO line from this GPIO device and writes it to `<strong>info</strong>`.
     *
     * @param  line 
     *           Number of the line.
     *
     * @param  info
     *           Will be overwritten.
     *
     * @return Information.
     *
     * @throws IllegalStateException
     *           When the GPIO device has been closed.
     *
     * @throws IOException
     *           When the device is not a proper GPIO device or an unplanned error occured.
     */
    public GpioLineInfo requestLineInfo( int          line ,
                                         GpioLineInfo info ) throws IOException {

        this.guardClosed() ;

        info.setLine( line ) ;
    
        Pointer ptr = new Pointer( Pointer.nativeValue( info.memory ) ) ;

        if ( LinuxIO.ioctl( this.fd                 ,
                            GPIO_GET_LINEINFO_IOCTL ,
                            info.memory             ) < 0 ) {

            int errno = Linux.getErrno() ;

            throwIfBadDevice( errno ) ;

            throw new IOException( "Native error while requesting information about a GPIO line : errno " + errno ) ;
        }

        return info ;
    }




    // Throws if a line is already being used or a handle/event-handle request is invalid.
    //
    private static void throwIfHandleError( int errno ) throws IOException {

        throwIfBadDevice( errno ) ;
    
        switch ( errno ) {

            case Errno.EBUSY  : throw new IOException( "At least one request line is already being used elsewhere" )                     ;

            case Errno.EINVAL : throw new IllegalArgumentException( "Part of the request for a GPIO handle or event handle is invalid" ) ;
        }
    }




    /**
     * Obtains a GPIO handle for driving the requested GPIO lines.
     *
     * @param  request
     *           Request specifying what lines will be handled and how.
     *
     * @return A GPIO handle.
     *
     * @throws IllegalArgumentException
     *           When something about the request is invalid, such as the number of a line.
     *
     * @throws IllegalStateException
     *           When the GPIO device has been closed.
     *
     * @throws IOException
     *           When the device is not a proper GPIO device or an unplanned error occured.
     */
    public GpioHandle requestHandle( GpioHandleRequest request ) throws IOException {

        this.guardClosed() ;
    
        if ( LinuxIO.ioctl( this.fd                   ,
                            GPIO_GET_LINEHANDLE_IOCTL ,
                            request.memory            ) < 0 ) {

            int errno = Linux.getErrno() ;

            throwIfHandleError( errno ) ;

            throw new IOException( "Native error while requesting a GPIO handle : errno " + errno ) ;
        }

        return new GpioHandle( request.getFD() ) ;
    }




    /**
     * Obtains a GPIO event handle for a GPIO line.
     * <p>
     * This handle can be used to read the current value of the line or wait for an interrupt.
     *
     * @param  request
     *           Request specifying which line will be monitored and how.
     *
     * @return A GPIO event handle.
     *
     *
     * @throws IllegalArgumentException
     *           When something about the request is invalid, such as the number of the line.
     *
     * @throws IllegalStateException
     *           When the GPIO device has been closed.
     *
     * @throws IOException
     *           When the device is not a proper GPIO device or an unplanned error occured.
     */
    public GpioEventHandle requestEvent( GpioEventRequest request ) throws IOException {

        this.guardClosed() ;

        if ( LinuxIO.ioctl( this.fd                  ,
                            GPIO_GET_LINEEVENT_IOCTL ,
                            request.memory           ) < 0 ) {

            int errno = Linux.getErrno() ;

            throwIfHandleError( errno ) ;

            throw new IOException( "Native error while requesting a GPIO event handle : errno " + errno ) ;
        }

        return new GpioEventHandle( request.getFD()   ,
                                    request.getLine() ) ;
    }
}
