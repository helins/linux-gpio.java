/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import com.sun.jna.Memory                               ;
import io.helins.linux.gpio.GpioFlags                   ;
import io.helins.linux.gpio.GpioUtils                   ;
import io.helins.linux.gpio.internal.NativeGpioLineInfo ;




/**
 * Class holding information about a GPIO line.
 */
public class GpioLineInfo {


    // Pointer to the native structure.
    //
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
     * @return The name of the consumer or null.
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
     * @return The name or null.
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
     * @return The flags.
     */
    public GpioFlags getFlags() {

        return new GpioFlags().fromLineInfo( this.getRawFlags() ) ;
    }




    /**
     * Is this line currently used ?
     *
     * @return True if the line is used.
     */
    public boolean isUsed() {

        return GpioUtils.isSet( this.getRawFlags()             ,
                                GpioFlags.LineInfoFlags.KERNEL ) ;
    }
}
