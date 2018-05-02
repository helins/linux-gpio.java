package io.dvlopt.linux.gpio.internal ;


import com.sun.jna.Structure ;
import io.dvlopt.linux.SizeT ;
import java.util.Arrays      ;
import java.util.List        ;




public class NativeGpioEventData extends Structure {


    public long timestamp = 0 ;
    public int  id        = 0 ;


    public static final SizeT SIZE = new SizeT( ( new NativeGpioEventData()).size() ) ;




    protected List< String > getFieldOrder() {

        return Arrays.asList( new String[] { "timestamp" ,
                                             "id"        } ) ;
    }
}
