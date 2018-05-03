package io.dvlopt.linux.gpio.internal ;


import com.sun.jna.Structure ;
import io.dvlopt.linux.SizeT ;
import java.util.Arrays      ;
import java.util.List        ;




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
