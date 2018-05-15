/*
 * Copyright 2018 Adam Helinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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
