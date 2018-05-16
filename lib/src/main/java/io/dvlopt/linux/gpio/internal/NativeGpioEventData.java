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
import io.dvlopt.linux.SizeT ;
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
