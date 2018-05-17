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


import io.dvlopt.linux.gpio.GpioFlags    ;
import org.junit.jupiter.api.DisplayName ;
import org.junit.jupiter.api.Test        ;




public class GpioFlagsTest {


    @Test
    @DisplayName( "Setting flags." )
    void settingFlags() {

        GpioFlags flags = new GpioFlags().setOutput()
                                         .setActiveLow( true )
                                         .setOpenDrain( true )
                                         .setOpenSource( true ) ;

        assertTrue( flags.isOutput() ) ;
        assertFalse( flags.isInput() ) ;
        assertTrue( flags.isActiveLow() ) ;
        assertTrue( flags.isOpenDrain() ) ;
        assertTrue( flags.isOpenSource() ) ;

        flags.setInput()
             .setActiveLow( false )
             .setOpenDrain( false )
             .setOpenSource( false ) ;

        assertTrue( flags.isInput() ) ;
        assertFalse( flags.isOutput() ) ;
        assertFalse( flags.isActiveLow() ) ;
        assertFalse( flags.isOpenDrain() ) ;
        assertFalse( flags.isOpenSource() ) ;
    }




    @Test
    @DisplayName( "Flag conversions and equalities." )
    void conversions() {

        GpioFlags flagsRef = new GpioFlags().setOutput()
                                            .setActiveLow( true )
                                            .setOpenDrain( true ) ;

        assertEquals( flagsRef ,
                      flagsRef ) ;

        GpioFlags flagsTest = new GpioFlags() ;

        flagsTest.fromRequest( flagsRef.forRequest() ) ;

        assertEquals( flagsRef  ,
                      flagsTest ) ;
    }
}
