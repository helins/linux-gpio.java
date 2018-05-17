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
