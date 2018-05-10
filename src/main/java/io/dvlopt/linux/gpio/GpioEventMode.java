package io.dvlopt.linux.gpio ;




/**
 * Class representing edge-detection for a GPIO event.
 */
public enum GpioEventMode {


    /**
     * The line is monitored only for rising signals.
     */
    RISING_EDGE ( 1 << 0 )                               ,

    /**
     * The line is monitored only for falling signals.
     */
    FALLING_EDGE( 1 << 1 )                               ,

    /**
     * The line is monitored for both rising and falling signals.
     */
    BOTH_EDGE   ( RISING_EDGE.flags | FALLING_EDGE.flags ) ;




    int flags ;


    GpioEventMode( int flags ) {
    
        this.flags = flags ;
    }
}
