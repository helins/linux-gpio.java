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


import com.sun.jna.Memory                               ;
import io.dvlopt.linux.gpio.GpioFlags                   ;
import io.dvlopt.linux.gpio.GpioUtils                   ;
import io.dvlopt.linux.gpio.internal.NativeGpioLineInfo ;




/**
 * Class holding information about a GPIO line.
 */
public class GpioLineInfo {


    final Memory memory ;




    /**
     * Basic constructor.
     */
    public GpioLineInfo() {

        this.memory = new Memory( NativeGpioLineInfo.SIZE ) ;

        this.memory.clear() ;
    }




    /**
     * Retrieves the current consumer of this line.
     *
     * @return  The name of the consumer or null.
     */
    public String getConsumer() {

        return GpioUtils.getString( this.memory                        ,
                                    NativeGpioLineInfo.OFFSET_CONSUMER ) ;
    }




    /**
     * Retrieves the number of this line.
     *
     * @return The number of the line.
     */
    public int getLine() {

        return this.memory.getInt( NativeGpioLineInfo.OFFSET_LINE ) ;
    }




    // Sets a line for a request.
    //
    void setLine( int line ) {
    
        this.memory.setInt( NativeGpioLineInfo.OFFSET_LINE ,
                            line                           ) ;
    }




    /**
     * Retrieves the name of this line.
     *
     * @return  The name or null.
     */
    public String getName() {

        return GpioUtils.getString( this.memory                    ,
                                    NativeGpioLineInfo.OFFSET_NAME ) ;
    }




    // Retrieves flags
    //
    private int getRawFlags() {
    
        return this.memory.getInt( NativeGpioLineInfo.OFFSET_FLAGS ) ;
    }




    /**
     * Retrieves flags qualifying this line.
     *
     * @return  The flags.
     */
    public GpioFlags getFlags() {

        return new GpioFlags().fromLineInfo( this.getRawFlags() ) ;
    }




    /**
     * Is this line currently used ?
     *
     * @return  A boolean.
     */
    public boolean isUsed() {

        return GpioUtils.isSet( this.getRawFlags()             ,
                                GpioFlags.LineInfoFlags.KERNEL ) ;
    }
}
