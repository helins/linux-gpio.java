package io.dvlopt.linux.gpio ;




/**
 * Enum for requesting GPIO lines to behave in certain ways.
 */
public enum GpioMode {


    /**
     * The line will remain as it is currently.
     */
    AS_IS             ( 0                                                   ) ,

    /**
     * The line will be a regular digital input.
     */
    INPUT             ( Flags.GPIOHANDLE_REQUEST_INPUT                      ) , 


    /**
     * The line will be an active low digital input, reversing high and low.
     */
    INPUT_ACTIVE_LOW  ( INPUT.flags | Flags.GPIOHANDLE_REQUEST_ACTIVE_LOW   ) , 

    /**
     * The line will be a regular digital output.
     */
    OUTPUT            ( Flags.GPIOHANDLE_REQUEST_OUTPUT                     ) , 

    /**
     * The line will be an active low digital output, reversing high and low.
     */
    OUTPUT_ACTIVE_LOW ( OUTPUT.flags | Flags.GPIOHANDLE_REQUEST_ACTIVE_LOW  ) , 

    /**
     * The line will be an open-drain digital output.
     */
    OUTPUT_OPEN_DRAIN ( OUTPUT.flags | Flags.GPIOHANDLE_REQUEST_OPEN_DRAIN  ) ,   

    /**
     * The line will be an open-source digital output.
     */
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
