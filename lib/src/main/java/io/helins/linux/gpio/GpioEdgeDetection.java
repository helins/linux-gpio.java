/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;




/**
 * Class representing edge-detection for a GPIO event.
 */
public enum GpioEdgeDetection {


    /**
     * The line is monitored only for rising signals.
     */
    RISING             ( 1 << 0 )                               ,

    /**
     * The line is monitored only for falling signals.
     */
    FALLING            ( 1 << 1 )                               ,

    /**
     * The line is monitored for both rising and falling signals.
     */
    RISING_AND_FALLING ( RISING.flags | FALLING.flags ) ;




    // Package protected internal value.
    //
    int flags ;


    // Private constructor.
    //
    private GpioEdgeDetection( int flags ) {
    
        this.flags = flags ;
    }
}
