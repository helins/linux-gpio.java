/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import static org.junit.jupiter.api.Assertions.* ;


import io.helins.linux.gpio.*                                  ;
import org.junit.jupiter.api.DisplayName                       ;
import org.junit.jupiter.api.Test                              ;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty ;
import java.io.IOException                                     ;




@EnabledIfSystemProperty( named   = "onRaspberry" ,
                          matches = "true"        )
public class RaspberryTest {


    // Consumer we will used throughout these tests.
    //
    static final String CONSUMER = "some-consumer" ;

    // The lines we will be testing.
    //
    final int[] LINES = { 17, 27, 22 , 5, 6, 13, 19, 26 } ;



    @Test
    @DisplayName( "Getting information about a GPIO chip on the Raspberry Pi." )
    void chipInfo() throws IOException {
    
        GpioDevice device = new GpioDevice( 0 ) ;

        GpioChipInfo info = new GpioChipInfo() ;

        device.requestChipInfo( info ) ;

        device.close() ;

        assertTrue( info.getLines() > 0 )  ;
        assertTrue( info.getLines() < 64 ) ;

        String name = info.getName() ;

        if ( name != null )  assertFalse( name.isEmpty() ) ;

        String label = info.getLabel() ;

        if ( label != null )  assertFalse( label.isEmpty() ) ;
    }




    @Test
    @DisplayName( "Getting information about a GPIO line on the Raspberry Pi." )
    void lineInfo() throws IOException {
    
        GpioDevice   device = new GpioDevice( 0 ) ;
        GpioLineInfo info   = new GpioLineInfo()  ;

        // Holders for the data we are going to test.
        //
        final GpioFlags[] FLAGS = new GpioFlags[ LINES.length ] ;
        final String[]    NAMES = new String[ LINES.length ]    ;

        // Retrieves the names and flags of all the tested lines, ensures the rest is fine.
        //
        for ( int i = 0        ;
              i < LINES.length ;
              i += 1           ) {

            device.requestLineInfo( LINES[ i ] ,
                                    info       ) ;

            assertEquals( info.getLine() ,
                          LINES[ i ]     ) ;

            assertFalse( info.isUsed() ) ;

            String name = info.getName() ;

            NAMES[ i ] = name ;

            if ( name != null )  assertFalse( name.isEmpty() ) ;

            assertNull( info.getConsumer() ) ;

            GpioFlags flags = info.getFlags() ;

            FLAGS[ i ] = flags ;
        }

        // Acquires a handle for the tested lines, as they are.
        //
        GpioHandleRequest request   = new GpioHandleRequest().setConsumer( CONSUMER ) ;
        GpioLine[]        gpioLines = new GpioLine[ LINES.length ] ;

        for ( int i = 0        ;
              i < LINES.length ;
              i += 1           ) {
              
            gpioLines[ i ] = request.addLine( LINES[ i ] ) ;
        }

        GpioHandle handle = device.requestHandle( request ) ;

        // Acquiring a handle for lines as they are should not change any prior data we had about them,
        // besides the consumer.
        //
        for ( int i = 0        ;
              i < LINES.length ;
              i += 1           ) {

            device.requestLineInfo( gpioLines[ i ].lineNumber ,
                                    info                      ) ;

            assertEquals( info.getLine() ,
                          LINES[ i ]     ) ;

            assertTrue( info.isUsed() ) ;

            assertEquals( info.getName() ,
                          NAMES[ i ]     ) ;

            /* TODO
             *
             * Unfortunately this will fail and it seems to bug on the Linux side.
             *
             * The 'outputs' test acquires lines whose natural direction is intput as outputs.
             * When released, those lines returns to their natural direction. However, the kernel
             * still present them as outputs.
            assertEquals( info.getFlags() ,
                          FLAGS[ i ]      ) ;
             */

            assertEquals( info.getConsumer() ,
                          CONSUMER           ) ;
        }

        handle.close() ;

        // Lines should be released.
        //
        for ( int i = 0        ;
              i < LINES.length ;
              i += 1           ) {

            device.requestLineInfo( LINES[ i ] ,
                                    info       ) ;

            assertFalse( info.isUsed() ) ;

            assertNull( info.getConsumer() ) ;
        }

        device.close() ;
    }




    @Test
    @DisplayName( "Writing to outputs on the Raspberry Pi." )
    void outputs() throws IOException {

        GpioDevice device = new GpioDevice( 0 ) ;
        
        GpioHandleRequest request = new GpioHandleRequest().setFlags( new GpioFlags().setOutput() ) ;


        GpioLine[] gpioLines = new GpioLine[ LINES.length ] ;

        for ( int i = 0        ;
              i < LINES.length ;
              i += 1           ) {

            gpioLines[ i ] = request.addLine( LINES[ i ] ,
                                              false      ) ;
        }

        GpioHandle handle = device.requestHandle( request ) ;

        device.close() ;

        GpioBuffer buffer = new GpioBuffer() ;

        // Lines were requested as low, checking that and preparing multi-write as high.
        //
        handle.read( buffer ) ;

        for ( GpioLine line : gpioLines ) {
        
            assertFalse( buffer.get( line ) ) ;
            buffer.set( line ,
                        true ) ;
        }

        // Checking multi-write as high.
        //
        handle.write( buffer ) ;
        handle.read( buffer )  ;

        for ( GpioLine line : gpioLines ) {
        
            assertTrue( buffer.get( line ) ) ;
        }

        // Clearing buffer and reseting all lines to low + preparing toggle.
        //
        handle.write( buffer.clear() ) ;

        for ( GpioLine line : gpioLines ) {
        
            assertFalse( buffer.get( line ) ) ;
            buffer.toggle( line )             ;
        }

        // Checking toggling.
        //
        handle.write( buffer ) ;

        for ( GpioLine line : gpioLines ) {
        
            assertTrue( buffer.get( line ) ) ;
        }

        handle.close() ;
    }
}
