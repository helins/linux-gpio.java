package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.internal.NativeGpioChipInfo ;




/**
 * Class holding information about a GPIO device.
 */
public class GpioChipInfo {


    NativeGpioChipInfo nativeStruct = new NativeGpioChipInfo() ;




    /**
     * Basic constructor
     */
    public GpioChipInfo() {}




    /**
     * Retrieves the label of this GPIO device.
     *
     * @return  String acting as a label.
     */
    public String getLabel() {
    
        return new String( this.nativeStruct.label ) ;
    }




    /**
     * Retrieves how many lines this GPIO device can handle, at most 64.
     *
     * @return  The number of lines.
     */
    public int getLines() {
    
        return this.nativeStruct.lines ;
    }




    /**
     * Retrieves the name of this GPIO device.
     *
     * @return  String representing the name.
     */
    public String getName() {
    
        return new String( this.nativeStruct.name ) ;
    }
}
