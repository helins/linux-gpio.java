/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import com.sun.jna.Memory                               ;
import io.helins.linux.gpio.GpioUtils                   ;
import io.helins.linux.gpio.internal.NativeGpioChipInfo ;




/**
 * Class holding information about a GPIO device.
 */
public class GpioChipInfo {


    // Pointer to the native structure.
    //
    final Memory memory ;




    /**
     * Basic constructor
     */
    public GpioChipInfo() {
    
        this.memory = new Memory( NativeGpioChipInfo.SIZE ) ;

        this.memory.clear() ;
    }




    /**
     * Retrieves the label of this GPIO device.
     *
     * @return String acting as a label or null.
     */
    public String getLabel() {

        return GpioUtils.getString( this.memory                     ,
                                    NativeGpioChipInfo.OFFSET_LABEL ) ;
    }




    /**
     * Retrieves how many lines this GPIO device can handle, at most 64.
     *
     * @return The number of lines.
     */
    public int getLines() {
    
        return this.memory.getInt( NativeGpioChipInfo.OFFSET_LINES ) ;
    }




    /**
     * Retrieves the name of this GPIO device.
     *
     * @return String representing the name or null.
     */
    public String getName() {
    
        return GpioUtils.getString( this.memory                    ,
                                    NativeGpioChipInfo.OFFSET_NAME ) ;
    }
}
