package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




public class GpioEventData {


    private static final int GPIO_EVENT_RISING_EDGE  = 0x01 ;
    private static final int GPIO_EVENT_FALLING_EDGE = 0x02 ;


    final NativeGpioEventData nativeStruct = new NativeGpioEventData() ;


    int line ;




    public GpioEventData() {
    
        this.line = -1 ;
    }




    GpioEventData( int line ) {
    
        this.line = line ;
    }




    void read( int fd   ,
               int line ) throws LinuxException {
    
        if ( LinuxIO.read( fd                             ,
                           this.nativeStruct.getPointer() ,
                           NativeGpioEventData.SIZE       ).intValue() < 0 ) {
        
            throw new LinuxException( "Unable to read GPIO event" ) ;
        }

        this.line = line ;
    }




    GpioEventData setLine( int line ) {
    
        this.line = line ;

        return this ;
    }




    public int getLine() {
    
        return this.line ;
    }




    public long getNanoTimestamp() {
    
        return this.nativeStruct.readTimestamp() ;
    }




    public boolean isRising() {
    
        return ( this.nativeStruct.readId() & GPIO_EVENT_RISING_EDGE ) > 0 ;
    }




    public boolean isFalling() {
    
        return ( this.nativeStruct.readId() & GPIO_EVENT_FALLING_EDGE ) > 0 ;
    }
}
