package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.GpioMode                         ;
import io.dvlopt.linux.gpio.GpioLine                         ;
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
 * @see GpioBuffer
 * @see GpioMode
 * @see GpioHandle
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
     * @return A string representing the consumer.
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
     * A handle can drive at most 64 lines at once.
     *
     * @param lineNumber  Which line.
     *
     * @return  A GPIO line for reading or writing state to a buffer.
     *
     * @see GpioBuffer
     */
    public GpioLine addLine( int lineNumber ) {

        int index = this.nativeStruct.lines ;
    
        this.nativeStruct.lineOffsets[ index ] = lineNumber ;

        this.nativeStruct.lines = index + 1 ;

        return new GpioLine( lineNumber ,
                             index      ) ;
    }
                                           



    /**
     * Adds a GPIO line to the request with a default value.
     * <p>
     * A handle can drive at most 64 lines.
     * <p>
     * Providing a default value works only for outputs and will be ignored for inputs.
     *
     * @param lineNumber  Which line.
     *
     * @param value  Default value.
     *
     * @return  A GPIO line for reading or writing state to a buffer.
     *
     * @see GpioBuffer
     */
    public GpioLine addLine( int     lineNumber ,
                             boolean value      ) {

        GpioLine gpioLine = this.addLine( lineNumber ) ;
    
        this.nativeStruct.defaultValues[ gpioLine.index ] = (byte)( value ? 1
                                                                          : 0 ) ;

        return gpioLine ;
    }
}
