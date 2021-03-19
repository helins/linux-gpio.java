/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio.internal ;


import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




/**
 * This class has be to public for JNA to work as needed, the user should not care about it.
 */
public class NativeGpioHandleRequest extends Structure {


    public static final int GPIOHANDLES_MAX = 64 ;


    public  int[]  lineOffsets   = new int[ GPIOHANDLES_MAX ]  ;
    public  int    flags         = 0                           ;
    public  byte[] defaultValues = new byte[ GPIOHANDLES_MAX ] ;
    public  byte[] consumer      = new byte[ 32 ]              ;
    public  int    lines         = 0                           ;
    public  int    fd            = -1                          ;


    public static final int OFFSET_LINE_OFFSETS   ;
    public static final int OFFSET_FLAGS          ;
    public static final int OFFSET_DEFAULT_VALUES ;
    public static final int OFFSET_CONSUMER       ;
    public static final int OFFSET_LINES          ;
    public static final int OFFSET_FD             ;
    public static final int SIZE                  ;


    static {
    
        NativeGpioHandleRequest nativeStruct = new NativeGpioHandleRequest() ;

        OFFSET_LINE_OFFSETS   = nativeStruct.fieldOffset( "lineOffsets"   ) ;
        OFFSET_FLAGS          = nativeStruct.fieldOffset( "flags"         ) ;
        OFFSET_DEFAULT_VALUES = nativeStruct.fieldOffset( "defaultValues" ) ;
        OFFSET_CONSUMER       = nativeStruct.fieldOffset( "consumer"      ) ;
        OFFSET_LINES          = nativeStruct.fieldOffset( "lines"         ) ;
        OFFSET_FD             = nativeStruct.fieldOffset( "fd"            ) ;
        SIZE                  = nativeStruct.size() ;
    }



    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "lineOffsets"   ,
                                             "flags"         ,
                                             "defaultValues" ,
                                             "consumer"      ,
                                             "lines"         ,
                                             "fd"            } ) ;
    }
}


