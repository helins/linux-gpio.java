package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




/**
 * Class for holding information about a GPIO event.
 */
public class GpioEventData {


    private static final int GPIO_EVENT_RISING_EDGE  = 0x01 ;
    private static final int GPIO_EVENT_FALLING_EDGE = 0x02 ;


    final NativeGpioEventData nativeStruct = new NativeGpioEventData() ;


    int line ;




    /**
     * Basic constructor.
     */
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




    /**
     * Retrieves the line the event happened on.
     *
     * @return  The number of the line.
     */
    public int getLine() {
    
        return this.line ;
    }




    /**
     * Retrieves when the event happened.
     *
     * @return  Unix timestamp in nanoseconds.
     */
    public long getNanoTimestamp() {
    
        return this.nativeStruct.readTimestamp() ;
    }




    /**
     * Did this event happened on a rising edge ?
     *
     * @return A boolean.
     */
    public boolean isRising() {
    
        return ( this.nativeStruct.readId() & GPIO_EVENT_RISING_EDGE ) > 0 ;
    }




    /**
     * Did this event happened on a falling edge ?
     *
     * @return A boolean.
     */
    public boolean isFalling() {
    
        return ( this.nativeStruct.readId() & GPIO_EVENT_FALLING_EDGE ) > 0 ;
    }
}
