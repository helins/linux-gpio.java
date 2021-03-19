/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import static org.junit.jupiter.api.Assertions.* ;


import io.helins.linux.gpio.GpioFlags    ;
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
