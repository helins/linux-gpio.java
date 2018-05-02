package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.GpioMode                        ;
import io.dvlopt.linux.gpio.GpioEventMode                   ;
import io.dvlopt.linux.gpio.internal.NativeGpioEventRequest ;




public class GpioEventRequest {


    NativeGpioEventRequest nativeStruct = new NativeGpioEventRequest() ;


    // TODO get handle and event modes ?

    public GpioEventRequest setActiveLow( boolean isActiveLow ) {
    
        this.nativeStruct.handleFlags = isActiveLow ? GpioMode.INPUT_ACTIVE_LOW.flags
                                                    : GpioMode.INPUT.flags            ;

        return this ;
    }


    public GpioEventRequest setEventMode( GpioEventMode mode ) {
    
        this.nativeStruct.eventFlags = mode.flags ;

        return this ;
    }


    public int getLine() {
    
        return this.nativeStruct.lineOffset ;
    }


    public GpioEventRequest setLine( int line ) {
    
        this.nativeStruct.lineOffset = line ;

        return this ;
    }


    public String getConsumer() {
    
        return new String( this.nativeStruct.consumerLabel ) ;
    }


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
