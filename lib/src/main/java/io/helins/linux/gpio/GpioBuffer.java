/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import com.sun.jna.Memory            ;
import com.sun.jna.Pointer           ;
import io.helins.linux.gpio.GpioLine ;




/**
 * Class representing a buffer for reading and/or writing the state of up to 64 GPIO lines.
 * <p>
 * It does not do any IO on its own.
 */
public class GpioBuffer {


    /**
     * How many lines a buffer can drive.
     */
    public static final int SIZE = 64 ;


    // Pointer to the buffer's native memory.
    //
    private final Memory buffer ;




    /**
     * Basic constructor.
     */
    public GpioBuffer() {
    
        this.buffer = new Memory( SIZE ) ;

        this.buffer.clear() ;
    }




    /**
     * Retrieves the state of a given line.
     *
     * @param  line
     *           The GPIO line
     *
     * @return A boolean representing the state.
     */
    public boolean get( GpioLine line ) {

        return this.buffer.getByte( line.index ) == 1 ;
    }




    /**
     * Sets the new state of a given line.
     *
     * @param  line
     *           The GPIO line.
     *
     * @param  value
     *           The new state.
     *
     * @return This instance.
     */
    public GpioBuffer set( GpioLine line  ,
                           boolean  value ) {

        this.buffer.setByte( line.index          ,
                             (byte)( value ? 1
                                           : 0 ) ) ;

        return this ;
    }




    /**
     * Toggles the state of a given line.
     *
     * @param  line
     *           The GPIO line.
     *
     * @return This instance.
     */
    public GpioBuffer toggle( GpioLine line ) {
    
        this.set( line                ,
                  !( this.get( line ) ) ) ;

        return this ;
    }




    /**
     * Clears this buffer by setting every state to false.
     *
     * @return  This instance.
     */
    public GpioBuffer clear() {
    
        this.buffer.clear() ;

        return this ;
    }




    // Retrieves the pointer to the buffer's native memory.
    //
    Pointer getPointer() {
    
        return (Pointer)( this.buffer ) ;
    }
}
