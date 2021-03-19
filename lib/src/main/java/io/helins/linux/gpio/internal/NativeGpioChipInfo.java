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
public class NativeGpioChipInfo extends Structure {


    public byte[] name   = new byte[ 32 ] ;
    public byte[] label  = new byte[ 32 ] ;
    public int    lines  = 0              ;


    public static final int OFFSET_NAME  ;
    public static final int OFFSET_LABEL ;
    public static final int OFFSET_LINES ;
    public static final int SIZE         ;


    static {
    
        NativeGpioChipInfo nativeStruct = new NativeGpioChipInfo() ;

        OFFSET_NAME  = nativeStruct.fieldOffset( "name"  ) ;
        OFFSET_LABEL = nativeStruct.fieldOffset( "label" ) ;
        OFFSET_LINES = nativeStruct.fieldOffset( "lines" ) ;
        SIZE         = nativeStruct.size()                 ;
    }




    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "name"  ,
                                             "label" ,
                                             "lines" } ) ;
    }
}
