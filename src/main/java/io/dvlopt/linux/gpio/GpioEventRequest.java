package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.GpioMode                        ;
import io.dvlopt.linux.gpio.GpioEventMode                   ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventRequest ;




/**
 * Class representing a request for obtaining a GPIO event handle for reading a line and
 * especially getting interrupts.
 */
public class GpioEventRequest {


    NativeGpioEventRequest nativeStruct = new NativeGpioEventRequest() ;


    // TODO get handle and event modes ?




    /**
     * Basic constructor.
     */
    public GpioEventRequest() {}




    /**
     * Specifies the requested line will be active low, reversing high and low.
     *
     * @param isActiveLow  If the line should be active low.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setActiveLow( boolean isActiveLow ) {
    
        this.nativeStruct.handleFlags = isActiveLow ? GpioMode.INPUT_ACTIVE_LOW.flags
                                                    : GpioMode.INPUT.flags            ;

        return this ;
    }




    /**
     * Selects edge-detection, the kind of event the user is interested in (eg. when the state
     * change from low to high).
     *
     * @param mode  Which event mode.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setEventMode( GpioEventMode mode ) {
    
        this.nativeStruct.eventFlags = mode.flags ;

        return this ;
    }




    /**
     * Retrieves which line will be requested (0 by default).
     *
     * @return  The number of the line.
     */
    public int getLine() {
    
        return this.nativeStruct.lineOffset ;
    }




    /**
     * Sets which line will be requested.
     *
     * @param line  The number of the line.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setLine( int line ) {
    
        this.nativeStruct.lineOffset = line ;

        return this ;
    }




    /**
     * Retrieves the consumer this line will be request under.
     *
     * @return  The name of the consumer.
     */
    public String getConsumer() {
    
        return new String( this.nativeStruct.consumerLabel ) ;
    }




    /**
     * Sets the consumer this line will be request under.
     *
     * @param consumer  The name of the consumer.
     *
     * @return  This GpioEventRequest.
     */
    public GpioEventRequest setConsumer( String consumer ) {
    
        byte[] consumerBytes = consumer.getBytes() ;

        System.arraycopy( consumerBytes                    ,
                          0                                ,
                          this.nativeStruct.consumerLabel  ,
                          0                                ,
                          Math.min( consumerBytes.length ,
                                    32                   ) ) ;

        return this ;
    }
}
