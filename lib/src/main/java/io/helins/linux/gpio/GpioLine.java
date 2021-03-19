/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;




/**
 * Class representing a GPIO line.
 */
public class GpioLine {


    // Position of the line in a GPIO buffer.
    //
    final int index ;


    /**
     * The number of the line.
     */
    public final int lineNumber ;





    // Private constructor
    //
    GpioLine( int lineNumber ,
              int index      ) {
    
        this.lineNumber = lineNumber ;
        this.index      = index      ;
    }
}
