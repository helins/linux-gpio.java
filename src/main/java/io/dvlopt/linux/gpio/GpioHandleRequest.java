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


import com.sun.jna.Memory                                    ;
import io.dvlopt.linux.gpio.GpioMode                         ;
import io.dvlopt.linux.gpio.GpioLine                         ;
import io.dvlopt.linux.gpio.GpioUtils                        ;
import io.dvlopt.linux.gpio.internal.NativeGpioHandleRequest ;




/**
 * Class representing a request for obtaining a GPIO handle for controlling the needed lines.
 * <p>
 * Several lines can be requested at once by specifying a mode (ie. <code>GpioMode.OUTPUT</code>)
 * and the resulting handle allows the user to control them all at once. In practise, there is no
 * garantee that any kind of IO will be atomic, actually happening at the exact same time for all
 * the lines. This depends on the underlying driver and is opaque to this library and user space in
 * general.
 *
 * @see GpioBuffer
 * @see GpioMode
 * @see GpioHandle
 */
public class GpioHandleRequest {


    final Memory memory ;


    GpioMode mode ;




    /**
     * Basic constructor selecting the AS_IS mode.
     *
     * @see GpioMode
     */
    public GpioHandleRequest() {
    
        this( GpioMode.AS_IS ) ;
    }




    /**
     * Constructor selecting a given mode.
     *
     * @param mode  The needed mode.
     */
    public GpioHandleRequest( GpioMode mode ) {

        this.memory = new Memory( NativeGpioHandleRequest.SIZE ) ;

        this.memory.clear() ;
    
        this.setMode( mode ) ;
    }




    /**
     * Selects which mode the requested lines will be in.
     *
     * @param mode  The needed mode.
     *
     * @return  This GpioHandleRequest.
     */
    public GpioHandleRequest setMode( GpioMode mode ) {
    
        this.memory.setInt( NativeGpioHandleRequest.OFFSET_FLAGS ,
                            mode.flags                           ) ;

        this.mode = mode ;

        return this ;
    }




    /**
     * Retrieves which mode this request will demand.
     *
     * @return  The mode.
     */
    public GpioMode getMode() {
    
        return this.mode ;
    }




    /**
     * Retrieves the consumer of this request.
     *
     * @return A string representing the consumer or null.
     */
    public String getConsumer() {

        return GpioUtils.getString( this.memory                             ,
                                    NativeGpioHandleRequest.OFFSET_CONSUMER ) ;
    }




    /**
     * Sets the consumer of the requested lines.
     *
     * @param  consumer  String representing the consumer, length must be smaller than 32.
     *
     * @return  This GpioHandleRequest.
     */
    public GpioHandleRequest setConsumer( String consumer ) {

        GpioUtils.setConsumer( this.memory                             ,
                               NativeGpioHandleRequest.OFFSET_CONSUMER ,
                               consumer                                ) ;

        return this ;
    }




    int getFD() {
    
        return this.memory.getInt( NativeGpioHandleRequest.OFFSET_FD ) ;
    }




    /**
     * Adds a GPIO line to the request.
     * <p>
     * A handle can drive at most 64 lines at once.
     *
     * @param lineNumber  Which line.
     *
     * @return  A GPIO line for reading or writing state to a buffer.
     *
     * @see GpioBuffer
     */
    public GpioLine addLine( int lineNumber ) {

        int index = this.memory.getInt( NativeGpioHandleRequest.OFFSET_LINES ) ;

        this.memory.setInt( NativeGpioHandleRequest.OFFSET_LINE_OFFSETS + 4 * index ,
                            lineNumber                                              ) ;

        this.memory.setInt( NativeGpioHandleRequest.OFFSET_LINES ,
                            index + 1                            ) ;

        return new GpioLine( lineNumber ,
                             index      ) ;
    }
                                           



    /**
     * Adds a GPIO line to the request with a default value.
     * <p>
     * A handle can drive at most 64 lines.
     * <p>
     * Providing a default value works only for outputs and will be ignored for inputs.
     *
     * @param lineNumber  Which line.
     *
     * @param value  Default value.
     *
     * @return  A GPIO line for reading or writing state to a buffer.
     *
     * @see GpioBuffer
     */
    public GpioLine addLine( int     lineNumber ,
                             boolean value      ) {

        GpioLine gpioLine = this.addLine( lineNumber ) ;

        this.memory.setByte( NativeGpioHandleRequest.OFFSET_DEFAULT_VALUES + gpioLine.index ,
                             (byte)( value ? 1
                                           : 0 )                                            ) ;

        return gpioLine ;
    }
}
