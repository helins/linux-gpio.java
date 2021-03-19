/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio.examples ;


import io.helins.linux.gpio.* ;
import java.io.IOException    ;




/*
 * In this example, 3 leds are alternating every 500 milliseconds.
 *
 * It works out of the box with a Raspberry Pi 3 and should work on any other board
 * exposing a GPIO character device.
 *
 * You need to make sure that the user running this program has permissions for the needed
 * character device, '/dev/gpiochip0' in this example.
 *
 * Attention, error checking not included !
 */
public class AlternatingLeds {




    // Path to our GPIO device in the file system.
    //
    public static final String PATH_TO_DEVICE = "/dev/gpiochip0" ;


    // The lines we are going to use.
    //
    public static final int LINE_NUMBER_0 = 17 ;
    public static final int LINE_NUMBER_1 = 27 ;
    public static final int LINE_NUMBER_2 = 22 ;

    // The number of milliseconds we will wait before switching to the next led.
    //
    public static final int WAIT_MS = 500 ;




    // Private constructor.
    //
    private AlternatingLeds() {}




    // Turns off previous led, turns on next one and waits a bit.
    //
    private static void nextLed( GpioHandle handle      ,
                                 GpioBuffer buffer      ,
                                 GpioLine   previousLed ,
                                 GpioLine   nextLed     ) throws InterruptedException ,
                                                                 IOException          {
    
        buffer.set( previousLed ,
                    false       ) ;

        buffer.set( nextLed ,
                    true    ) ;

        handle.write( buffer ) ;

        Thread.sleep( WAIT_MS ) ;
    }




    // Our example program.
    //
    public static void main( String[] args ) {

        System.out.println( "\n\nStarting alternating leds...\n" ) ;

        // Creating a handle request for outputs.
        //
        GpioHandleRequest request = new GpioHandleRequest().setConsumer( "my-program" )
                                                           .setFlags( new GpioFlags().setOutput() ) ;

        // Declaring our leds and makes sure they will be turned off.

        GpioLine led0 = request.addLine( LINE_NUMBER_0 ,
                                         false         ) ;

        GpioLine led1 = request.addLine( LINE_NUMBER_1 ,
                                         false         ) ;

        GpioLine led2 = request.addLine( LINE_NUMBER_2 ,
                                         false         ) ;

        // Creating a buffer for writing the state of our leds.
        //
        GpioBuffer buffer = new GpioBuffer() ;


        // Acquiring all needed GPIO resources in a "try-with-resources" manner in order to ensure they will be closed properly
        // if anything goes wrong.
        //
        // We need a GPIO device and a unique handle for controlling all our leds.
        //
        try ( GpioDevice device = new GpioDevice( PATH_TO_DEVICE ) ;
                
              GpioHandle handle = device.requestHandle( request )  ;

              ) {

            // Alternates leds for ever.
            //
            while( true ) {

                nextLed( handle ,
                         buffer ,
                         led2   ,
                         led0   ) ;

                nextLed( handle ,
                         buffer ,
                         led0   ,
                         led1   ) ;

                nextLed( handle ,
                         buffer ,
                         led1   ,
                         led2   ) ;
            }
        }

        catch ( Throwable e ) {
        
            System.out.println( "\nDamn, something went wrong !i\n" ) ;

            e.printStackTrace() ;
        }
    }
}
