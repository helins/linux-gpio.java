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
import io.dvlopt.linux.LinuxException       ;
import io.dvlopt.linux.gpio.GpioChipInfo    ;
import io.dvlopt.linux.gpio.GpioLineInfo    ;
import io.dvlopt.linux.gpio.GpioEventHandle ;
import io.dvlopt.linux.gpio.GpioHandle      ;
import io.dvlopt.linux.io.LinuxIO           ;




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


    private static final NativeLong GPIO_GET_CHIPINFO_IOCTL   = new NativeLong( 2151986177L ,
                                                                                true        ) ; 

    private static final NativeLong GPIO_GET_LINEINFO_IOCTL   = new NativeLong( 3225990146L ,
                                                                                true        ) ;

    private static final NativeLong GPIO_GET_LINEHANDLE_IOCTL = new NativeLong( 3245126659L ,
                                                                                true        ) ;

    private static final NativeLong GPIO_GET_LINEEVENT_IOCTL  = new NativeLong( 3224417284L ,
                                                                                true        ) ;




    private int     fd               ;
    private boolean isClosed = false ;




    /**
     * Opens the GPIO device located at the given path.
     *
     * @param path  Path to the GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioDevice( String path ) throws LinuxException {

        int fd = LinuxIO.open64( path             ,
                                 LinuxIO.O_RDONLY ) ;

        if ( fd < 0 ) throw new LinuxException( "Unable to open GPIO device" ) ;

        this.fd = fd ;
    }




    /**
     * Opens a GPIO device by number.
     *
     * @param number  Number of the GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioDevice( int number ) throws LinuxException {
    
        this( "/dev/gpiochip" + number ) ;
    }




    /**
     * Closes this GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public void close() throws LinuxException {

        if ( this.isClosed == false ) {

            if ( LinuxIO.close( this.fd ) != 0 ) {
            
                throw new LinuxException( "Unable to close GPIO device" ) ;
            }

            this.isClosed = true ;
        }
    }




    /**
     * Obtains information about this GPIO device.
     *
     * @return  Basic information about this GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioChipInfo requestChipInfo() throws LinuxException {
    
        return this.requestChipInfo( new GpioChipInfo() ) ;
    }




    /**
     * Obtains information about this GPIO device and writes it to `<code>info</code>`.
     *
     * @param info  Where the data will be written.
     *
     * @return  Basic information about this GPIO device.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioChipInfo requestChipInfo( GpioChipInfo info ) throws LinuxException {
    
       if ( LinuxIO.ioctl( this.fd                 ,
                           GPIO_GET_CHIPINFO_IOCTL ,
                           info.memory             ) < 0 ) {
           
           throw new LinuxException( "Unable to retrieve informations about GPIO device" ) ;
       }

       return info ;
    }




    /**
     * Obtains information about a particular GPIO line from this GPIO device.
     *
     * @param line  The number of the line.
     *
     * @return  Basic information about the GPIO line.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioLineInfo requestLineInfo( int line ) throws LinuxException {
    
        return this.requestLineInfo( line               ,
                                     new GpioLineInfo() ) ;
    }




    /**
     * Obtains information about a particular GPIO line from this GPIO device and writes it to `<code>info</code>`.
     *
     * @param line  The number of the line.
     *
     * @param info  Where the data will be written.
     *
     * @return  Basic information about the GPIO line.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioLineInfo requestLineInfo( int          line ,
                                         GpioLineInfo info ) throws LinuxException {

        System.out.println( "Is out 1 : " + info.getFlags().isOutput() ) ;
        info.setLine( line ) ;
        System.out.println( "Is out 2 : " + info.getFlags().isOutput() ) ;
    
        Pointer ptr = new Pointer( Pointer.nativeValue( info.memory ) ) ;

        System.out.println( "FLAGS = " + ptr.getInt( 4 ) ) ;
        if ( LinuxIO.ioctl( this.fd                 ,
                            GPIO_GET_LINEINFO_IOCTL ,
                            info.memory             ) < 0 ) {
            
            throw new LinuxException( "Unable to retrieve information about the request GPIO line" ) ;
        }

        Pointer ptr2 = new Pointer( Pointer.nativeValue( info.memory ) ) ;

        System.out.println( "FLAGS = " + ptr2.getInt( 4 ) ) ;

        System.out.println( "Is out 3 : " + info.getFlags().isOutput() ) ;

        return info ;
    }




    /**
     * Obtains a GPIO handle for driving the requested GPIO lines.
     *
     * @param request  Request specifying what lines will be handled and how.
     *
     * @return  A GPIO handle.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioHandle requestHandle( GpioHandleRequest request ) throws LinuxException {
    
        if ( LinuxIO.ioctl( this.fd                   ,
                            GPIO_GET_LINEHANDLE_IOCTL ,
                            request.memory            ) < 0 ) {
        
            throw new LinuxException( "Unable to provide a GPIO handle" ) ;
        }

        return new GpioHandle( request.getFD() ) ;
    }




    /**
     * Obtains a GPIO event handle for a GPIO line.
     * <p>
     * This handle can be used to read the current value of the line or wait for an interrupt.
     *
     * @param request  Request specifying which line will be monitored and how.
     *
     * @return  A GPIO event handle.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEventHandle requestEvent( GpioEventRequest request ) throws LinuxException {

        if ( LinuxIO.ioctl( this.fd                  ,
                            GPIO_GET_LINEEVENT_IOCTL ,
                            request.memory           ) < 0 ) {
        
            throw new LinuxException( "Unable to provide a GPIO event handle" ) ;
        }

        return new GpioEventHandle( request.getFD()   ,
                                    request.getLine() ) ;
    }
}
