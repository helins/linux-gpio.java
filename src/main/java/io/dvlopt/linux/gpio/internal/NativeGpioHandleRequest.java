package io.dvlopt.linux.gpio.internal ;


import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




/**
 * This class has be to public for JNA to work as needed, the user should not care about it.
 */
public class NativeGpioHandleRequest extends Structure {


    public static final int GPIOHANDLES_MAX = 64 ;


    public  int[]  lineOffsets   = new int[ GPIOHANDLES_MAX ]  ;
    public  int    flags         = 0                           ;
    public  byte[] defaultValues = new byte[ GPIOHANDLES_MAX ] ;
    public  byte[] consumerLabel = new byte[ 32 ]              ;
    public  int    lines         = 0                           ;
    public  int    fd            = -1                          ;




    protected List< String > getFieldOrder() {
    
        return Arrays.asList( new String[] { "lineOffsets"   ,
                                             "flags"         ,
                                             "defaultValues" ,
                                             "consumerLabel" ,
                                             "lines"         ,
                                             "fd"            } ) ;
    }
}


