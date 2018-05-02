package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.GpioMode                         ;
import io.dvlopt.linux.gpio.internal.NativeGpioHandleRequest ;




public class GpioHandleRequest {


    NativeGpioHandleRequest nativeStruct = new NativeGpioHandleRequest() ;



    public GpioHandleRequest setMode( GpioMode mode ) {
    
        this.nativeStruct.flags = mode.flags ;

        return this ;
    }


    // TODO getMode ?




    public String getConsumer() {
    
        return new String( this.nativeStruct.consumerLabel ) ;
    }


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




    public GpioHandleRequest registerLine( int index ,
                                           int line  ) {
    
        this.nativeStruct.lineOffsets[ index ] = line ;

        this.nativeStruct.lines = Math.max( this.nativeStruct.lines ,
                                            index + 1               ) ;

        return this ;
    }
                                           

    public GpioHandleRequest registerLine( int     index ,
                                           int     line  ,
                                           boolean value ) {

        this.registerLine( index ,
                           line  ) ;
    
        this.nativeStruct.defaultValues[ index ] = (byte)( value ? 1
                                                                 : 0 ) ;

        return this ;
    }
}
