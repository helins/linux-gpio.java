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


import com.sun.jna.Memory                                   ;
import io.dvlopt.linux.gpio.GpioMode                        ;
import io.dvlopt.linux.gpio.GpioEventMode                   ;
import io.dvlopt.linux.gpio.GpioUtils                       ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventRequest ;




/**
 * Class representing a request for obtaining a GPIO event handle for reading a line and
 * especially getting interrupts.
 */
public class GpioEventRequest {


    final Memory memory ;


    private GpioEventMode eventMode ;




    /**
     * Basic constructor requesting a regular input for both rising and falling edge.
     *
     * @param line  The number of the line.
     */
    public GpioEventRequest( int line ) {
    
        this( line                    ,
              GpioEventMode.BOTH_EDGE ,
              false                   ) ;
    }




    /**
     * Constructor requesting a regular input leaving the choice of edge-detection.
     *
     * @param line  The number of the line.
     *
     * @param mode  The kind of edge-detection.
     */
    public GpioEventRequest( int           line ,
                             GpioEventMode mode ) {

        this( line  ,
              mode  ,
              false ) ;
    }




    /**
     * Constructor leaving all configuration to the user.
     *
     * @param line  The number of the line.
     *
     * @param mode  The kind of edge-detection.
     *
     * @param isActiveLow  Set as active-low input ?
     */
    public GpioEventRequest( int           line        ,
                             GpioEventMode mode        ,
                             boolean       isActiveLow ) {
    
        this.memory = new Memory( NativeGpioEventRequest.SIZE ) ;

        this.memory.clear() ;

        this.setLine( line )             ;
        this.setEventMode( mode )        ;
        this.setActiveLow( isActiveLow ) ;
    }




    /**
     * Is this a request for an active low line ?
     *
     * @return  A boolean.
     */
    public boolean isActiveLow() {
    
        return this.memory.getInt( NativeGpioEventRequest.OFFSET_HANDLE_FLAGS ) == GpioMode.INPUT_ACTIVE_LOW.flags ;
    }




    /**
     * Specifies the requested line will be active low, reversing high and low.
     *
     * @param isActiveLow  If the line should be active low.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setActiveLow( boolean isActiveLow ) {

        this.memory.setInt( NativeGpioEventRequest.OFFSET_HANDLE_FLAGS    ,
                            isActiveLow ? GpioMode.INPUT_ACTIVE_LOW.flags
                                        : GpioMode.INPUT.flags            ) ;

        return this ;
    }




    /**
     * Retrieves what kind of edge-detection this request if for.
     *
     * @return The kind of edge-detection.
     */
    public GpioEventMode getEventMode() {
    
        return this.eventMode ;
    }




    /**
     * Selects edge-detection, the kind of event the user is interested in (eg. when the state
     * change from low to high).
     *
     * @param mode The kind of edge-detection.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setEventMode( GpioEventMode mode ) {

        this.memory.setInt( NativeGpioEventRequest.OFFSET_EVENT_FLAGS ,
                            mode.flags                                ) ;

        this.eventMode = mode ;

        return this ;
    }




    int getFD() {
    
        return this.memory.getInt( NativeGpioEventRequest.OFFSET_FD ) ;
    }




    /**
     * Retrieves which line will be requested (0 by default).
     *
     * @return  The number of the line.
     */
    public int getLine() {
    
        return this.memory.getInt( NativeGpioEventRequest.OFFSET_LINE ) ;
    }




    /**
     * Sets which line will be requested.
     *
     * @param line  The number of the line.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setLine( int line ) {

        this.memory.setInt( NativeGpioEventRequest.OFFSET_LINE ,
                            line                               ) ;

        return this ;
    }




    /**
     * Retrieves the consumer this line will be request under.
     *
     * @return  The name of the consumer.
     */
    public String getConsumer() {

        return GpioUtils.getString( this.memory                            ,
                                    NativeGpioEventRequest.OFFSET_CONSUMER ) ;
    }




    /**
     * Sets the consumer this line will be request under.
     *
     * @param consumer  The name of the consumer, length must be smaller than 32.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setConsumer( String consumer ) {

        GpioUtils.setConsumer( this.memory                            ,
                               NativeGpioEventRequest.OFFSET_CONSUMER ,
                               consumer                               ) ;

        return this ;
    }
}
