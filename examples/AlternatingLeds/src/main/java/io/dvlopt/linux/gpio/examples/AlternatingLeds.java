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


package io.dvlopt.linux.gpio.examples ;


import io.dvlopt.linux.LinuxException ;
import io.dvlopt.linux.gpio.*         ;




/**
 * In this example, 3 leds are alternating every 500 milliseconds.
 * <p>
 * It works out of the box with a Raspberry Pi 3 and should work on any other board
 * exposing a GPIO character device.
 * <p>
 * You need to make sure that the user running this program has permissions for the needed
 * character device, '/dev/gpiochip0' in this example.
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
                                                                 LinuxException       {
    
        buffer.set( previousLed ,
                    false       ) ;

        buffer.set( nextLed ,
                    true    ) ;

        handle.write( buffer ) ;

        Thread.sleep( WAIT_MS ) ;
    }




    // Our example program.
    //
    public static void main( String[] args ) throws InterruptedException ,
                                                    LinuxException       {

        System.out.println( "\n\nStarting alternating leds..." ) ;

        // Opening our gpio device.
        //
        GpioDevice device = new GpioDevice( PATH_TO_DEVICE ) ;

        // Creating a handle request for outputs.
        //
        GpioHandleRequest request = new GpioHandleRequest().setConsumer( "my-program" )
                                                           .setFlags( new GpioFlags().setOutput() ) ;

        // Declares our leds and makes sure they are turned off.

        GpioLine led0 = request.addLine( LINE_NUMBER_0 ,
                                         false         ) ;

        GpioLine led1 = request.addLine( LINE_NUMBER_1 ,
                                         false         ) ;

        GpioLine led2 = request.addLine( LINE_NUMBER_2 ,
                                         false         ) ;

        // Obtaines a handle for controlling our leds.
        //
        GpioHandle handle = device.requestHandle( request ) ;

        // Creates a buffer for writing the state of our leds.
        GpioBuffer buffer = new GpioBuffer() ;


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
}
