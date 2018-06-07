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
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;
import java.io.IOException                               ;




/**
 * Class for holding information about a GPIO event.
 */
public class GpioEvent {


    // Values for representing edge detection.
    //
    private static final int GPIO_EVENT_RISING_EDGE  = 0x01 ;
    private static final int GPIO_EVENT_FALLING_EDGE = 0x02 ;




    // Internal native structure.
    //
    final NativeGpioEventData nativeStruct = new NativeGpioEventData() ;

    // ID associated with an event.
    //
    int id = 0 ;




    /**
     * Basic constructor.
     */
    public GpioEvent() {}



    // Reads an event from a file descriptor.
    //
    void read( int fd ) throws IOException {

        this.read( fd ,
                   0  ) ;
    }




    // Reads an event from a file descriptor and updating the ID.
    //
    void read( int fd ,
               int id ) throws IOException {
    
        if ( LinuxIO.read( fd                             ,
                           this.nativeStruct.getPointer() ,
                           NativeGpioEventData.SIZE       ).intValue() < 0 ) {
        
            throw new IOException( "Native error while reading a GPIO event : errno " + Linux.getErrno() ) ;
        }

        this.id = id ;
    }




    /**
     * Retrieves the id associated with this event.
     * <p>
     * When using an event watcher, the user can associated an event with an arbitraty id in
     * order to recognize it when it happens.
     *
     * @return  The number of the line.
     *
     * @see GpioEventWatcher
     */
    public int getId() {
    
        return this.id ;
    }




    /**
     * Retrieves the best estimation of when the event happened.
     *
     * @return  Unix timestamp in nanoseconds.
     */
    public long getNanoTimestamp() {
    
        return this.nativeStruct.readTimestamp() ;
    }




    /**
     * Did this event happened on a rising edge ?
     *
     * @return A boolean.
     */
    public boolean isRising() {
    
        return ( this.nativeStruct.readId() & GPIO_EVENT_RISING_EDGE ) > 0 ;
    }




    /**
     * Did this event happened on a falling edge ?
     *
     * @return A boolean.
     */
    public boolean isFalling() {
    
        return ( this.nativeStruct.readId() & GPIO_EVENT_FALLING_EDGE ) > 0 ;
    }
}
