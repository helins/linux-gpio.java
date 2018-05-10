package io.dvlopt.linux.gpio.internal ;


import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




/**
 * This class has be to public for JNA to work as needed, the user should not care about it.
 */
public class NativeGpioLineInfo extends Structure {


    public  int    lineOffset = 0              ;
    public  int    flags      = 0              ;
    public  byte[] name       = new byte[ 32 ] ;
    public  byte[] consumer   = new byte[ 32 ] ;


    protected List< String > getFieldOrder() {

        return Arrays.asList( new String[] { "lineOffset" ,
                                             "flags"      ,
                                             "name"       ,
                                             "consumer"   } ) ;
    }
}
