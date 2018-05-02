package io.dvlopt.linux.gpio.internal ;


import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




public class NativeGpioChipInfo extends Structure {


    public byte[] name   = new byte[ 32 ] ;
    public byte[] label  = new byte[ 32 ] ;
    public int    lines  = 0              ;


    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "name"  ,
                                             "label" ,
                                             "lines" } ) ;
    }
}
