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
import io.dvlopt.linux.gpio.GpioEdgeDetection               ;
import io.dvlopt.linux.gpio.GpioFlags                       ;
import io.dvlopt.linux.gpio.GpioUtils                       ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventRequest ;




/**
 * Class representing a request for obtaining a GPIO event handle for reading a line and
 * especially setting edge-detection.
 * <p>
 * Edge-detection is monitoring changes of state, for instance when the line goes from low
 * to high.
 */
public class GpioEventRequest {


    // Gpio flags meant to be reused
    //
    private static final int DEFAULT_FLAGS = new GpioFlags().setInput()
                                                            .forRequest() ;

    


    // Pointer to the native structure
    //
    final Memory memory ;


    // Selected edge-detection mode ;
    //
    private GpioEdgeDetection edgeDetection ;




    /**
     * Basic constructor requesting a regular input for both rising and falling edge.
     *
     * @param line
     *          Number of the line.
     */
    public GpioEventRequest( int line ) {
    
        this( line                                 ,
              GpioEdgeDetection.RISING_AND_FALLING ,
              DEFAULT_FLAGS                        ) ;
    }




    /**
     * Constructor requesting a regular input leaving the choice of edge-detection.
     *
     * @param line
     *          Number of the line.
     *
     * @param edgeDetection
     *          Edge-detection mode.
     */
    public GpioEventRequest( int               line          ,
                             GpioEdgeDetection edgeDetection ) {

        this( line          ,
              edgeDetection ,
              DEFAULT_FLAGS ) ;
    }




    /**
     * Constructor leaving all configuration to the user.
     *
     * @param line
     *          Number of the line.
     *
     * @param edgeDetection
     *          Edge-detection mode.
     *
     * @param flags
     *          Flags describing how the line will be handled.
     */
    public GpioEventRequest( int           line              ,
                             GpioEdgeDetection edgeDetection ,
                             GpioFlags     flags             ) {

        this( line               ,
              edgeDetection      ,
              flags.forRequest() ) ;
    }




    // Private constructor accepting raw flags.
    //
    private GpioEventRequest( int               line          ,
                              GpioEdgeDetection edgeDetection ,
                              int               flags         ) {
    
        this.memory = new Memory( NativeGpioEventRequest.SIZE ) ;

        this.memory.clear() ;

        this.setLine( line )                   ;
        this.setEdgeDetection( edgeDetection ) ;
        this.setRawFlags( flags )              ;
    }




    // Sets flags using an int.
    //
    private GpioEventRequest setRawFlags( int flags ) {
    
        this.memory.setInt( NativeGpioEventRequest.OFFSET_HANDLE_FLAGS ,
                            flags                                      ) ;

        return this ;
    }




    /**
     * Retrieves the flags describing how the lines involved in this request should be handled.
     *
     * @return Flags.
     */
    public GpioFlags getFlags() {
    
        return new GpioFlags().fromRequest( this.memory.getInt( NativeGpioEventRequest.OFFSET_HANDLE_FLAGS ) ) ;
    }




    /**
     * Sets flags describing how the lines involved in this request should be handled.
     *
     * @param flags
     *          Flags.
     *
     * @return This instance.
     */
    public GpioEventRequest setFlags( GpioFlags flags ) {
    
        return this.setRawFlags( flags.forRequest() ) ;
    }




    /**
     * Clears the flags associated with this requests.
     * <p>
     * Means that lines will be requested "as-is".
     *
     * @return This instance.
     */
    public GpioEventRequest unsetFlags() {
    
        return this.setRawFlags( 0 ) ;
    }




    /**
     * Retrieves what kind of edge-detection this request if for.
     *
     * @return Edge-detection mode.
     */
    public GpioEdgeDetection getEdgeDetection() {
    
        return this.edgeDetection ;
    }




    /**
     * Selects edge-detection.
     *
     * @param  edgeDetection
     *           Edge-detection mode.
     *
     * @return This instance.
     */
    public GpioEventRequest setEdgeDetection( GpioEdgeDetection edgeDetection ) {

        this.memory.setInt( NativeGpioEventRequest.OFFSET_EVENT_FLAGS ,
                            edgeDetection.flags                       ) ;

        this.edgeDetection = edgeDetection ;

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
     * @return Number of the line.
     */
    public int getLine() {
    
        return this.memory.getInt( NativeGpioEventRequest.OFFSET_LINE ) ;
    }




    /**
     * Selects which line will be requested.
     *
     * @param line
     *          Number of the line.
     *
     * @return This instance.
     */
    public GpioEventRequest setLine( int line ) {

        this.memory.setInt( NativeGpioEventRequest.OFFSET_LINE ,
                            line                               ) ;

        return this ;
    }




    /**
     * Retrieves the consumer this line will be request under.
     *
     * @return Name of the consumer or null if none.
     */
    public String getConsumer() {

        return GpioUtils.getString( this.memory                            ,
                                    NativeGpioEventRequest.OFFSET_CONSUMER ) ;
    }




    /**
     * Sets the consumer this line will be request under.
     *
     * @param  consumer
     *           Name of the consumer, length must be smaller than 32.
     *
     * @return This instance.
     *
     * @throws IllegalArgumentException 
     *           When the length of the consumer is equal or greater than 32.
     */
    public GpioEventRequest setConsumer( String consumer ) {

        GpioUtils.setConsumer( this.memory                            ,
                               NativeGpioEventRequest.OFFSET_CONSUMER ,
                               consumer                               ) ;

        return this ;
    }
}
