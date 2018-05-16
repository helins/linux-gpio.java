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
import io.dvlopt.linux.gpio.GpioEventMode                   ;
import io.dvlopt.linux.gpio.GpioFlags                       ;
import io.dvlopt.linux.gpio.GpioUtils                       ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventRequest ;




/**
 * Class representing a request for obtaining a GPIO event handle for reading a line and
 * especially getting interrupts.
 */
public class GpioEventRequest {


    private static final int DEFAULT_FLAGS = new GpioFlags().setInput()
                                                            .forRequest() ;

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
              DEFAULT_FLAGS           ) ;
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

        this( line          ,
              mode          ,
              DEFAULT_FLAGS ) ;
    }




    /**
     * Constructor leaving all configuration to the user.
     *
     * @param line  The number of the line.
     *
     * @param mode  The kind of edge-detection.
     *
     * @param flags  Flags describing how the line will be handled.
     */
    public GpioEventRequest( int           line  ,
                             GpioEventMode mode  ,
                             GpioFlags     flags ) {

        this( line               ,
              mode               ,
              flags.forRequest() ) ;
    }




    // Private constructor accepting raw flags.
    //
    private GpioEventRequest( int           line  ,
                              GpioEventMode mode  ,
                              int           flags ) {
    
        this.memory = new Memory( NativeGpioEventRequest.SIZE ) ;

        this.memory.clear() ;

        this.setLine( line )      ;
        this.setEventMode( mode ) ;
        this.setRawFlags( flags ) ;
    }




    // Sets flags using an int.
    //
    private GpioEventRequest setRawFlags( int flags ) {
    
        this.memory.setInt( NativeGpioEventRequest.OFFSET_HANDLE_FLAGS ,
                            flags                                      ) ;

        return this ;
    }




    public GpioEventRequest setFlags( GpioFlags flags ) {
    
        return this.setRawFlags( flags.forRequest() ) ;
    }




    public GpioEventRequest unsetFlags() {
    
        return this.setRawFlags( 0 ) ;
    }




    public GpioFlags getFlags() {
    
        return new GpioFlags().fromRequest( this.memory.getInt( NativeGpioEventRequest.OFFSET_HANDLE_FLAGS ) ) ;
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




    // Retrieves the linux file descriptor after the request has been accepted.
    //
    int getFD() {
    
        return this.memory.getInt( NativeGpioEventRequest.OFFSET_FD ) ;
    }




    /**
     * Retrieves which line will be requested.
     *
     * @return  The number of the line.
     */
    public int getLine() {
    
        return this.memory.getInt( NativeGpioEventRequest.OFFSET_LINE ) ;
    }




    /**
     * Selects which line will be requested.
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
