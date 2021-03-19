/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import static org.junit.jupiter.api.Assertions.* ;


import io.helins.linux.gpio.GpioBuffer   ;
import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.Test        ;




public class GpioBufferTest {


    @Test
    @DisplayName( "Clearing a buffer." )
    void clear() {
    
        GpioBuffer buffer = new GpioBuffer() ;

        for ( int i = 0           ;
              i < GpioBuffer.SIZE ;
              i += 1              ) {
              
            GpioLine line = new GpioLine( i ,
                                          i ) ;

            buffer.set( line ,
                        true ) ;
        }

        buffer.clear() ;

        for ( int i = 0           ;
              i < GpioBuffer.SIZE ;
              i += 1              ) {
              
            GpioLine line = new GpioLine( i ,
                                          i ) ;

            assertFalse( buffer.get( line ) ) ;
        }
    }




    @Test
    @DisplayName( "Setting and getting line values from a buffer." )
    void setAndGet() {
    
        GpioBuffer buffer = new GpioBuffer() ;

        for ( int i = 0           ;
              i < GpioBuffer.SIZE ;
              i += 1              ) {

            GpioLine line = new GpioLine( i ,
                                          i ) ;

            buffer.set( line ,
                        true ) ;

            assertEquals( buffer.get( line ) ,
                          true               ) ;

            buffer.set( line  ,
                        false ) ;

            assertEquals( buffer.get( line ) ,
                          false              ) ;
        }
    }




    @Test
    @DisplayName( "Toggling lines." )
    void toggle() {
    
        GpioBuffer buffer = new GpioBuffer() ;

        for ( int i = 0           ;
              i < GpioBuffer.SIZE ;
              i += 1              ) {
              
            GpioLine line = new GpioLine( i ,
                                          i ) ;

            assertFalse( buffer.get( line ) ) ;

            buffer.toggle( line ) ;

            assertTrue( buffer.get( line ) ) ;

            buffer.toggle( line ) ;

            assertFalse( buffer.get( line ) ) ;
        }
    }
}
