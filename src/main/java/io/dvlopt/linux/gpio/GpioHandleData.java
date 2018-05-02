package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.internal.NativeGpioHandleData ;




public class GpioHandleData {


    NativeGpioHandleData nativeStruct = new NativeGpioHandleData() ;


    public boolean getValue( int index ) {
    
        return this.nativeStruct.values[ index ] == 1 ;
    }


    public GpioHandleData setValue( int     index ,
                                    boolean value ) {

        this.nativeStruct.values[ index ] = (byte)( value ? 1
                                                          : 0 ) ;

        return this ;
    }
}
