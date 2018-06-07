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


import io.dvlopt.linux.gpio.* ;
import java.io.IOException    ;



/**
 * In this example, 2 push buttons reacts everytime one of them is released.
 * <p>
 * It works out of the box with a Raspberry Pi 3 and should work on any other board
 * exposing a GPIO character device.
 * <p>
 * You need to make sure that the user running this program has permissions for the needed
 * character device, '/dev/gpiochip0' in this example.
 *
 * Attention, error checking not included !
 */
public class PushButtons {




    // Path to our GPIO device in the file system.
    //
    public static final String PATH_TO_DEVICE = "/dev/gpiochip0" ;


    // The lines we are going to use.
    //
    public static final int LINE_NUMBER_BUTTON_1 = 17 ;
    public static final int LINE_NUMBER_BUTTON_2 = 27 ;

    // Identifiers for recognizing the events when they happen.
    //
    public static final int ID_BUTTON_1 = 1 ;
    public static final int ID_BUTTON_2 = 2 ;

    // The number of milliseconds we will wait before terminating the program in absence of input.
    //
    public static final int WAIT_MS = 15000 ;




    // Private constructor.
    //
    private PushButtons() {}




    // Our example program.
    //
    public static void main( String[] args ) throws InterruptedException ,
                                                    IOException          {

        System.out.println( "\n\nStarting push buttons...\n" ) ;

        // Acquiring all needed GPIO resources in a "try-with-resources" manner to ensure they will be closed properly if
        // anything goes wrong.
        //
        // We need a GPIO device, 1 event handle per input and a watcher for efficiently monitoring them both.
        //
        try ( GpioDevice       device  = new GpioDevice( PATH_TO_DEVICE )                                         ;

              GpioEventHandle  handle1 = device.requestEvent( new GpioEventRequest( LINE_NUMBER_BUTTON_1      ,
                                                                                    GpioEdgeDetection.FALLING ) ) ;

              GpioEventHandle  handle2 = device.requestEvent( new GpioEventRequest( LINE_NUMBER_BUTTON_2      ,
                                                                                    GpioEdgeDetection.FALLING ) ) ;

              GpioEventWatcher watcher = new GpioEventWatcher()                                                   ;
              
              ) {
              
            // Now that our resources are acquired, let's do the rest.

            // Adding our event handles to the watcher.
            //
            watcher.addHandle( handle1     ,
                               ID_BUTTON_1 )
                   .addHandle( handle2     ,
                               ID_BUTTON_2 ) ;

            // For holding data about events.
            //
            GpioEvent event = new GpioEvent() ;

            System.out.println( "Come on, press any button and keep pressin' ! Program will terminates after " + WAIT_MS + "milliseconds of inactivity.\n" ) ;

            // Keeps printing events and terminates program when nothing happens for a while.
            //
            while ( watcher.waitForEvent( event   ,
                                          WAIT_MS ) ) {
            
                System.out.println( event.getNanoTimestamp() + " button " + event.getId() ) ;
            }

            System.out.println( "\nTimeout, terminating program..." ) ;
        }

        catch ( Throwable e ) {
        
            System.out.println( "\nDamn, something went wrong ! \n" ) ;

            e.printStackTrace() ;
        }
    }
}
