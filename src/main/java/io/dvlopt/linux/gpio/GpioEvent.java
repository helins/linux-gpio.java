package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.LinuxException                    ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventData ;
import io.dvlopt.linux.io.LinuxIO                        ;




/**
 * Class for holding information about a GPIO event.
 */
public class GpioEvent {


    private static final int GPIO_EVENT_RISING_EDGE  = 0x01 ;
    private static final int GPIO_EVENT_FALLING_EDGE = 0x02 ;


    final NativeGpioEventData nativeStruct = new NativeGpioEventData() ;


    int id = 0 ;




    /**
     * Basic constructor.
     */
    public GpioEvent() {}



    void read( int fd ) throws LinuxException{

        this.read( fd ,
                   0  ) ;
    }




    void read( int fd ,
               int id ) throws LinuxException {
    
        if ( LinuxIO.read( fd                             ,
                           this.nativeStruct.getPointer() ,
                           NativeGpioEventData.SIZE       ).intValue() < 0 ) {
        
            throw new LinuxException( "Unable to read GPIO event" ) ;
        }

        this.id = id ;
    }




    /**
     * Retrieves the id associated with this event.
     * <p>
     * When using an event watcher, the user can associated an event with an arbitraty id in
     * order to recognize it when it happens.
     *
     * @return  The number of the line.
     *
     * @see GpioEventWatcher
     */
    public int getId() {
    
        return this.id ;
    }




    /**
     * Retrieves the best estimation of when the event happened.
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
