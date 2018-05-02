package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




public class GpioEventData {



    public static final int GPIO_EVENT_RISING_EDGE  = 0x01 ;
    public static final int GPIO_EVENT_FALLING_EDGE = 0x02 ;


    NativeGpioEventData nativeStruct = new NativeGpioEventData() ; // TODO user directly a pointer ? probably much faster
    int                 line                                     ;



    public GpioEventData() {
    
        this.line = -1 ;
    }


    GpioEventData( int line ) {
    
        this.line = line ;
    }



    GpioEventData readEvent( int fd   ,
                             int line ) throws LinuxException {
    
        if ( LinuxIO.read( fd                             ,
                           this.nativeStruct.getPointer() ,
                           NativeGpioEventData.SIZE       ).intValue() < 0 ) {
        
            throw new LinuxException( "Unable to read GPIO event" ) ;
        }

        this.nativeStruct.read() ;
        this.line = line         ;

        return this ;
    }



    GpioEventData setLine( int line ) {
    
        this.line = line ;

        return this ;
    }


    public int getLine() {
    
        return this.line ;
    }




    public long getNanoTimestamp() {
    
        return this.nativeStruct.timestamp ;
    }


    public boolean isRising() {
    
        return ( this.nativeStruct.id & GPIO_EVENT_RISING_EDGE ) > 0 ;
    }


    public boolean isFalling() {
    
        return ( this.nativeStruct.id & GPIO_EVENT_FALLING_EDGE ) > 0 ;
    }
}
