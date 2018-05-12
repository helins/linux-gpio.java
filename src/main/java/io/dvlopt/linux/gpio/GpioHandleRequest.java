package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.GpioMode                         ;
import io.dvlopt.linux.gpio.internal.NativeGpioHandleRequest ;




/**
 * Class representing a request for obtaining a GPIO handle for controlling the needed lines.
 * <p>
 * Several lines can be requested at once by specifying a mode (ie. <code>GpioMode.OUTPUT</code>)
 * and the resulting handle allows the user to control them all at once. In practise, there is no
 * garantee that any kind of IO will be atomic, actually happening at the exact same time for all
 * the lines. This depends on the underlying driver and is opaque to this library and user space in
 * general.
 *
 * @see GpioMode
 * @see GpioHandle
 * @see GpioHandleData
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
     * Adds a GPIO line to the request.
     * <p>
     * A handle can drive at most 64 lines at once and each line is refered to by using an index specified
     * by the request rather than by using the number of the line.
     *
     * @param index  Position of the line in the handle this request will provide, must be smaller than 64.
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
     * Adds a GPIO line to the request with a default value.
     * <p>
     * A handle can drive at most 64 lines at once and each line is refered to by using an index specified
     * by the request rather than by using the number of the line.
     * <p>
     * Providing a default value works only for outputs and will be ignored for inputs.
     *
     * @param index  Position of the line in the handle this request will provide, must be smaller than 64.
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
