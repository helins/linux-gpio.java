/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import com.sun.jna.Pointer ;




// Utilities shared amongst classes.
//
class GpioUtils {




    // Is a flag set ?
    //
    static boolean isSet( int flags ,
                          int flag  ) {

        return ( flags & flag ) != 0 ;
    }




    // From a pointer, reads and returns a string or null if it is empty.
    //
    static String getString( Pointer ptr    ,
                             int     offset ) {
    
        String s = ptr.getString( offset ) ;

        return s.isEmpty() ? null
                           : s    ;
    }




    // Given a pointer, validates and sets a consumer.
    //
    static void setConsumer( Pointer ptr      ,
                             int     offset   ,
                             String  consumer ) {

        if ( consumer.length() > 31 ) {
        
            throw new IllegalArgumentException( "Consumer length must be < 32" ) ;
        }

        ptr.setString( offset   ,
                       consumer ) ;
    }
}
