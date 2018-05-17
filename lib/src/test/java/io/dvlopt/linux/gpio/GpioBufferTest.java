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


import static org.junit.jupiter.api.Assertions.* ;


import io.dvlopt.linux.gpio.GpioBuffer   ;
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
