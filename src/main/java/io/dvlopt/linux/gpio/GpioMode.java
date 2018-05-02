package io.dvlopt.linux.gpio ;




public enum GpioMode {


    AS_IS             ( 0                                                   ) ,
    INPUT             ( Flags.GPIOHANDLE_REQUEST_INPUT                      ) , 
    INPUT_ACTIVE_LOW  ( INPUT.flags | Flags.GPIOHANDLE_REQUEST_ACTIVE_LOW   ) , 
    OUTPUT            ( Flags.GPIOHANDLE_REQUEST_OUTPUT                     ) , 
    OUTPUT_ACTIVE_LOW ( OUTPUT.flags | Flags.GPIOHANDLE_REQUEST_ACTIVE_LOW  ) , 
    OUTPUT_OPEN_DRAIN ( OUTPUT.flags | Flags.GPIOHANDLE_REQUEST_OPEN_DRAIN  ) ,   
    OUTPUT_OPEN_SOURCE( OUTPUT.flags | Flags.GPIOHANDLE_REQUEST_OPEN_SOURCE ) ;




    private static class Flags {


        private static final int GPIOHANDLE_REQUEST_INPUT       = 1 << 0 ;
        private static final int GPIOHANDLE_REQUEST_OUTPUT      = 1 << 1 ;
        private static final int GPIOHANDLE_REQUEST_ACTIVE_LOW  = 1 << 2 ;
        private static final int GPIOHANDLE_REQUEST_OPEN_DRAIN  = 1 << 3 ;
        private static final int GPIOHANDLE_REQUEST_OPEN_SOURCE = 1 << 4 ; 
    }


    int flags ;


    private GpioMode( int flags ) {
    
        this.flags = flags ;
    }
}
