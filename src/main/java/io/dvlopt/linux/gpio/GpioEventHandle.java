package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.GpioEventData                ;
import io.dvlopt.linux.gpio.GpioHandle                   ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




public class GpioEventHandle extends GpioHandle {


    private final int line ;




    GpioEventHandle( int fd   ,
                     int line ) {

        super( fd ,
               1  ) ;

        this.line = line ;
    }




    public int getLine() {
    
        return this.line ;
    }




    public GpioEventData waitForEvent() throws LinuxException {

        return this.waitForEvent( new GpioEventData() ) ;
    }




    public GpioEventData waitForEvent( GpioEventData data ) throws LinuxException {

        data.read( this.fd   ,
                   this.line ) ;

        return data ;
    }




    int getFD() {
    
        return fd ;
    }
}
