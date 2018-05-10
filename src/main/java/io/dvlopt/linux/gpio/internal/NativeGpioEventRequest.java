package io.dvlopt.linux.gpio.internal ;


import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




/**
 * This class has be to public for JNA to work as needed, the user should not care about it.
 */
public class NativeGpioEventRequest extends Structure {


    public int    lineOffset                     ;
    public int    handleFlags                    ;
    public int    eventFlags                     ;
    public byte[] consumerLabel = new byte[ 32 ] ;
    public int    fd            = -1             ;




    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "lineOffset"    ,
                                             "handleFlags"   ,
                                             "eventFlags"    ,
                                             "consumerLabel" ,
                                             "fd"            } ) ;
    }
}


