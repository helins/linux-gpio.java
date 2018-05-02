package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.internal.NativeGpioChipInfo ;




public class GpioChipInfo {


    NativeGpioChipInfo nativeStruct = new NativeGpioChipInfo() ;




    public String getLabel() {
    
        return new String( this.nativeStruct.label ) ;
    }




    public int getLines() {
    
        return this.nativeStruct.lines ;
    }




    public String getName() {
    
        return new String( this.nativeStruct.name ) ;
    }
}
