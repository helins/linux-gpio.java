package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.GpioMode                         ;
import io.dvlopt.linux.gpio.internal.NativeGpioHandleRequest ;




/**
 * Class representing a request for obtaining a GPIO handle for controlling the needed lines.
 */
public class GpioHandleRequest {


    final NativeGpioHandleRequest nativeStruct = new NativeGpioHandleRequest() ;

    GpioMode mode ;




    /**
     * Basic constructor.
     */
    public GpioHandleRequest() {
    
        this.setMode( GpioMode.AS_IS ) ;
    }




    /**
     * Selects which mode the requested lines will be in.
     *
     * @param mode  The needed mode.
     *
     * @return  This GpioHandleRequest.
     */
    public GpioHandleRequest setMode( GpioMode mode ) {
    
        this.mode               = mode       ;
        this.nativeStruct.flags = mode.flags ;

        return this ;
    }




    /**
     * Retrieves which mode this request will demand.
     *
     * @return  The mode.
     */
    public GpioMode getMode() {
    
        return this.mode ;
    }




    /**
     * Retrieves the consumer of this request.
     *
     * @return  String representing the consumer.
     */
    public String getConsumer() {
    
        return new String( this.nativeStruct.consumerLabel ) ;
    }




    /**
     * Sets the future consumer of the requested lines.
     *
     * @param  consumer  String representing the consumer.
     *
     * @return  This GpioHandleRequest.
     */
    public GpioHandleRequest setConsumer( String consumer ) {
    
        byte[] consumerBytes = consumer.getBytes() ;

        System.arraycopy( consumerBytes                    ,
                          0                                ,
                          this.nativeStruct.consumerLabel  ,
                          0                                ,
                          Math.min( consumerBytes.length ,
                                    32                   ) ) ;

        return this ;
    }




    /**
     * Adds a GPIO line to be requested.
     *
     * @param index  Position of the line in the handle this request will provide.
     *
     * @param line  Which line.
     *
     * @return  This GpioHandleRequest.
     */
    public GpioHandleRequest addLine( int index ,
                                      int line  ) {
    
        this.nativeStruct.lineOffsets[ index ] = line ;

        this.nativeStruct.lines = Math.max( this.nativeStruct.lines ,
                                            index + 1               ) ;

        return this ;
    }
                                           



    /**
     * Adds a GPIO line to be requested with a default value.
     * <p>
     * Works only for outputs, the default value will be ignored for inputs.
     *
     * @param index  Position of the line in the handle this request will provide.
     *
     * @param line  Which line.
     *
     * @param value  Default value.
     *
     * @return  This GpioHandleRequest.
     */
    public GpioHandleRequest addLine( int     index ,
                                      int     line  ,
                                      boolean value ) {

        this.addLine( index ,
                      line  ) ;
    
        this.nativeStruct.defaultValues[ index ] = (byte)( value ? 1
                                                                 : 0 ) ;

        return this ;
    }
}
