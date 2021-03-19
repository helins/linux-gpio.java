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
public class NativeGpioEventRequest extends Structure {


    public int    line                         ;
    public int    handleFlags                  ;
    public int    eventFlags                   ;
    public byte[] consumer    = new byte[ 32 ] ;
    public int    fd                           ;


    public static int OFFSET_LINE         ;
    public static int OFFSET_HANDLE_FLAGS ;
    public static int OFFSET_EVENT_FLAGS  ;
    public static int OFFSET_CONSUMER     ;
    public static int OFFSET_FD           ;
    public static int SIZE                ;


    static {
    
        NativeGpioEventRequest nativeStruct = new NativeGpioEventRequest() ;

        OFFSET_LINE         = nativeStruct.fieldOffset( "line"        ) ;
        OFFSET_HANDLE_FLAGS = nativeStruct.fieldOffset( "handleFlags" ) ;
        OFFSET_EVENT_FLAGS  = nativeStruct.fieldOffset( "eventFlags"  ) ;
        OFFSET_CONSUMER     = nativeStruct.fieldOffset( "consumer"    ) ;
        OFFSET_FD           = nativeStruct.fieldOffset( "fd"          ) ;
        SIZE                = nativeStruct.size()                       ;
    }




    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "line"        ,
                                             "handleFlags" ,
                                             "eventFlags"  ,
                                             "consumer"    ,
                                             "fd"          } ) ;
    }
}


