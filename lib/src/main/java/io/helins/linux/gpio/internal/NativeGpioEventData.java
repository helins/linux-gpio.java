/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio.internal ;


import com.sun.jna.Structure ;
import io.helins.linux.SizeT ;
import java.util.Arrays      ;
import java.util.List        ;




/**
 * This class has be to public for JNA to work as needed, the user should not care about it.
 */
public class NativeGpioEventData extends Structure {


    public long timestamp = 0 ;
    public int  id        = 0 ;


    private int offsetTimestamp = this.fieldOffset( "timestamp" ) ;
    private int offsetId        = this.fieldOffset( "id" )        ;


    public static final SizeT SIZE = new SizeT( ( new NativeGpioEventData()).size() ) ;




    public long readTimestamp() {
    
        return this.getPointer().getLong( offsetTimestamp ) ;
    }




    public int readId() {
    
        return this.getPointer().getInt( offsetId ) ;
    }




    protected List< String > getFieldOrder() {

        return Arrays.asList( new String[] { "timestamp" ,
                                             "id"        } ) ;
    }
}
