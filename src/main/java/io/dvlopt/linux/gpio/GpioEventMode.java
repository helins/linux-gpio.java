package io.dvlopt.linux.gpio ;




public enum GpioEventMode {


    RISING_EDGE ( 1 << 0 )                               ,
    FALLING_EDGE( 1 << 1 )                               ,
    BOTH_EDGE   ( RISING_EDGE.flags | FALLING_EDGE.flags ) ;




    int flags ;


    GpioEventMode( int flags ) {
    
        this.flags = flags ;
    }
}
