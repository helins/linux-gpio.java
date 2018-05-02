package io.dvlopt.linux.gpio.internal ;


import com.sun.jna.Structure ;
import java.util.Arrays      ;
import java.util.List        ;




public class NativeGpioHandleData extends Structure {


    public byte[] values = new byte[ 64 /*GPIOHANDLES_MAX*/ ] ;




    protected List< String > getFieldOrder() {

        return Arrays.asList( new String[] { "values" } ) ;
    }
}

