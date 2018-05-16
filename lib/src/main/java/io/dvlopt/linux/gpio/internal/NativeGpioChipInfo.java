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


package io.dvlopt.linux.gpio.internal ;


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
