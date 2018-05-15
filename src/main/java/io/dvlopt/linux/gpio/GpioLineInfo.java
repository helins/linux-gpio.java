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


import io.dvlopt.linux.gpio.internal.NativeGpioLineInfo ;




/**
 * Class holding information about a GPIO line.
 */
public class GpioLineInfo {


    NativeGpioLineInfo nativeStruct = new NativeGpioLineInfo() ;


    private static final int GPIOLINE_FLAG_KERNEL      = 1 << 0 ; 
    private static final int GPIOLINE_FLAG_IS_OUT      = 1 << 1 ;
    private static final int GPIOLINE_FLAG_ACTIVE_LOW  = 1 << 2 ;
    private static final int GPIOLINE_FLAG_OPEN_DRAIN  = 1 << 3 ;
    private static final int GPIOLINE_FLAG_OPEN_SOURCE = 1 << 4 ;




    /**
     * Basic constructor (line 0 by default).
     */
    public GpioLineInfo() {
    
        this( 0 ) ;
    }




    /**
     * Constructor for the given line.
     *
     * @param line  Which line.
     */
    public GpioLineInfo( int line ) {

        this.nativeStruct.lineOffset = line ;
    }




    /**
     * Retrieves the current consumer of the requested line.
     *
     * @return  The name of the consumer.
     */
    public String getConsumer() {

        return new String( this.nativeStruct.consumer ) ;
    }




    /**
     * Retrieves the number of the line this object is describing.
     *
     * @return The number of the line.
     */
    public int getLine() {
    
        return this.nativeStruct.lineOffset ;
    }




    /**
     * Retrieves the name of the line.
     *
     * @return  The name.
     */
    public String getName() {
    
        return new String( this.nativeStruct.name ) ;
    }




    /**
     * Is this line active low ?
     *
     * @return  A boolean.
     */
    public boolean isActiveLow() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_ACTIVE_LOW ) > 0 ;
    }




    /**
     * Is this line open-drain ?
     *
     * @return  A boolean.
     */
    public boolean isOpenDrain() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_OPEN_DRAIN ) > 0 ;
    }




    /**
     * Is this line open-source ?
     *
     * @return  A boolean.
     */
    public boolean isOpenSource() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_OPEN_SOURCE ) > 0 ;
    }




    /**
     * Is this line an input ?
     *
     * @return  A boolean.
     */
    public boolean isInput() {
    
        return !( this.isOutput() ) ;
    }




    /**
     * Is this line an output ?
     *
     * @return  A boolean.
     */
    public boolean isOutput() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_IS_OUT ) > 0 ;
    }




    /**
     * Is this line currently used ?
     *
     * @return  A boolean.
     */
    public boolean isUsed() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_KERNEL ) > 0 ;
    }
}
