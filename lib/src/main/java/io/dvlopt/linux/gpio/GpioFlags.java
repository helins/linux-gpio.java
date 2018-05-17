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


import io.dvlopt.linux.gpio.GpioUtils ;




/**
 * Class for specifying how lines are handled or will be handled.
 */
public class GpioFlags {




    // Flags related to requests.
    //
    private static class RequestFlags {


        private static final int INPUT       = 1 << 0 ;
        private static final int OUTPUT      = 1 << 1 ;
        private static final int ACTIVE_LOW  = 1 << 2 ;
        private static final int OPEN_DRAIN  = 1 << 3 ;
        private static final int OPEN_SOURCE = 1 << 4 ; 
    }




    // Flags related to lines.
    //
    static class LineInfoFlags {


        static final int KERNEL      = 1 << 0 ; 
        static final int IS_OUT      = 1 << 1 ;
        static final int ACTIVE_LOW  = 1 << 2 ;
        static final int OPEN_DRAIN  = 1 << 3 ;
        static final int OPEN_SOURCE = 1 << 4 ;
    }




    private boolean isOutput     = false ;
    private boolean isActiveLow  = false ;
    private boolean isOpenDrain  = false ;
    private boolean isOpenSource = false ;




    /**
     * Basic constructor.
     */
    public GpioFlags() {}




    /**
     * Sets as a digital input.
     *
     * @return  This GpioFlags instance.
     */
    public GpioFlags setInput() {
    
        this.isOutput = false ;

        return this ;
    }




    /**
     * Sets as a digital output.
     *
     * @return  This GpioFlags instance.
     */
    public GpioFlags setOutput() {
    
        this.isOutput = true ;

        return this ;
    }




    /**
     * Sets or not to active low, meaning the kernel will inverse values.
     * <p>
     * A low signal will be logical high and vice-versa.
     *
     * @param isActiveLow  Should be active low ?
     *
     * @return  This GpioFlags instance.
     */
    public GpioFlags setActiveLow( boolean isActiveLow ) {
    
        this.isActiveLow = isActiveLow ;

        return this ;
    }




    /**
     * Sets or not to open drain.
     * <p>
     * For outputs, lines can be driven low but not high.
     * When driven high, the lines will actually be undefined and acting as inputs.
     *
     * @param isOpenDrain  Should be open drain ?
     *
     * @return  This GpioFlags instance.
     */
    public GpioFlags setOpenDrain( boolean isOpenDrain ) {
    
        this.isOpenDrain = isOpenDrain ;

        return this ;
    }




    /**
     * Sets or not to open source.
     * <p>
     * For outputs, lines can be driven high but no low.
     * When driven low, the lines will actually be undefined and acting as inputs.
     *
     * @param isOpenSource  Should be open source ?
     *
     * @return  This GpioFlags instance.
     */
    public GpioFlags setOpenSource( boolean isOpenSource ) {
    
        this.isOpenSource = isOpenSource ;

        return this ;
    }




    /**
     * Are those flags describing digital input(s) ?
     *
     * @return  A boolean.
     */
    public boolean isInput() {
    
        return !( this.isOutput ) ;
    }




    /**
     * Are those flags describing a digital output(s) ?
     *
     * @return  A boolean.
     */
    public boolean isOutput() {
    
        return this.isOutput ;
    }




    /**
     * Are those flags describing active low line(s) ?
     *
     * @return  A boolean.
     */
    public boolean isActiveLow() {
    
        return this.isActiveLow ;
    }




    /**
     * Are those flags describing open drain line(s) ?
     *
     * @return  A boolean.
     */
    public boolean isOpenDrain() {
    
        return this.isOpenDrain ;
    }




    /**
     * Are those flags describing open source line(s) ?
     *
     * @return  A boolean.
     */
    public boolean isOpenSource() {
    
        return this.isOpenSource ;
    }




    // Creates flags for a request.
    //
    int forRequest() {
    
        int flags = 0 ;

        flags |= this.isOutput ? RequestFlags.OUTPUT
                               : RequestFlags.INPUT  ;

        if ( this.isActiveLow ) {
        
            flags |= RequestFlags.ACTIVE_LOW ;
        }

        if ( this.isOpenDrain ) {
        
            flags |= RequestFlags.OPEN_DRAIN ;
        }

        if ( this.isOpenSource ) {
        
            flags |= RequestFlags.OPEN_SOURCE ;
        }

        return flags ;
    }




    // Interprets flags from a request.
    //
    GpioFlags fromRequest( int flags ) {
    
        this.isOutput = GpioUtils.isSet( flags               ,
                                         RequestFlags.OUTPUT ) ;

        this.isActiveLow = GpioUtils.isSet( flags                   ,
                                            RequestFlags.ACTIVE_LOW ) ;

        this.isOpenDrain = GpioUtils.isSet( flags                   ,
                                            RequestFlags.OPEN_DRAIN ) ;

        this.isOpenSource = GpioUtils.isSet( flags                    ,
                                             RequestFlags.OPEN_SOURCE ) ;

        return this ;
    }




    // Interprets flags from a line.
    //
    GpioFlags fromLineInfo( int flags ) {

        this.isOutput      = GpioUtils.isSet( flags                ,
                                              LineInfoFlags.IS_OUT ) ;

        this.isActiveLow   = GpioUtils.isSet( flags                    ,
                                              LineInfoFlags.ACTIVE_LOW ) ;

        this.isOpenDrain   = GpioUtils.isSet( flags                    ,
                                              LineInfoFlags.OPEN_DRAIN ) ;

        this.isOpenSource = GpioUtils.isSet( flags                     ,
                                             LineInfoFlags.OPEN_SOURCE ) ;

        return this ;
    }




    /**
     * Are those two flags equal ?
     *
     * @param flags  Flags for comparison.
     *
     * @return  A boolean.
     */
    public boolean equals( GpioFlags flags ) {
    
        return    this.isOutput     == flags.isOutput
               && this.isActiveLow  == flags.isActiveLow
               && this.isOpenDrain  == flags.isOpenDrain
               && this.isOpenSource == flags.isOpenSource ;
    }




    @Override
    public boolean equals( Object o ) {
    
        return o instanceof GpioFlags ? this.equals( (GpioFlags)o )
                                      : false                       ;
    }
}
