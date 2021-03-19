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
public class NativeGpioLineInfo extends Structure {


    public  int    line     = 0              ;
    public  int    flags    = 0              ;
    public  byte[] name     = new byte[ 32 ] ;
    public  byte[] consumer = new byte[ 32 ] ;


    public static final int OFFSET_LINE     ;
    public static final int OFFSET_FLAGS    ;
    public static final int OFFSET_NAME     ;
    public static final int OFFSET_CONSUMER ;
    public static final int SIZE            ;


    static {
    
        NativeGpioLineInfo nativeStruct = new NativeGpioLineInfo() ;

        OFFSET_LINE     = nativeStruct.fieldOffset( "line"     ) ;
        OFFSET_FLAGS    = nativeStruct.fieldOffset( "flags"    ) ;
        OFFSET_NAME     = nativeStruct.fieldOffset( "name"     ) ;
        OFFSET_CONSUMER = nativeStruct.fieldOffset( "consumer" ) ;
        SIZE            = nativeStruct.size()                    ;
    }




    protected List< String > getFieldOrder() {

        return Arrays.asList( new String[] { "line"     ,
                                             "flags"    ,
                                             "name"     ,
                                             "consumer" } ) ;
    }
}
