package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.GpioEventData                ;
import io.dvlopt.linux.gpio.GpioHandle                   ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




/**
 * Class for controlling a GPIO line that can be monitored for interrupts.
 */
public class GpioEventHandle extends GpioHandle {


    private final int line ;




    GpioEventHandle( int fd   ,
                     int line ) {

        super( fd ,
               1  ) ;

        this.line = line ;
    }




    /**
     * Retrieves the number of the line this handle handles.
     *
     * @return The number of the line.
     */
    public int getLine() {
    
        return this.line ;
    }




    /**
     * Waits for an event to happen.
     *
     * @return  Information about what happened.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEventData waitForEvent() throws LinuxException {

        return this.waitForEvent( new GpioEventData() ) ;
    }




    /**
     * Waits for an event to happen and writes what happened to the given object.
     *
     * @param data  Object for holding data about what happened.
     *
     * @return  Information about what happened.
     *
     * @throws LinuxException  When something fails on the native side.
     */
    public GpioEventData waitForEvent( GpioEventData data ) throws LinuxException {

        data.read( this.fd   ,
                   this.line ) ;

        return data ;
    }




    int getFD() {
    
        return fd ;
    }
}
