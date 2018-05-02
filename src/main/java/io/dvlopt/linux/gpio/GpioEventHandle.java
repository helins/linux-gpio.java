package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.GpioEventData                ;
import io.dvlopt.linux.gpio.GpioHandle                   ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




public class GpioEventHandle extends GpioHandle {


    private int line ;



    public GpioEventHandle( int fd   ,
                            int line ) {
    

        super( fd ) ;

        this.line = line ;
    }


    public int getLine() {
    
        return this.line ;
    }




    public GpioEventData readEvent() throws LinuxException {

        return this.readEvent( new GpioEventData() ) ;
    }


    public GpioEventData readEvent( GpioEventData data ) throws LinuxException {

        return data.readEvent( this.fd   ,
                               this.line ) ;
    }


    public int getFD() {
    
        return fd ;
    }
}
