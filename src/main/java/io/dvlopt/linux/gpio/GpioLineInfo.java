package io.dvlopt.linux.gpio ;


import io.dvlopt.linux.gpio.internal.NativeGpioLineInfo ;




public class GpioLineInfo {


    NativeGpioLineInfo nativeStruct = new NativeGpioLineInfo() ;


    private static final int GPIOLINE_FLAG_KERNEL      = 1 << 0 ; 
    private static final int GPIOLINE_FLAG_IS_OUT      = 1 << 1 ;
    private static final int GPIOLINE_FLAG_ACTIVE_LOW  = 1 << 2 ;
    private static final int GPIOLINE_FLAG_OPEN_DRAIN  = 1 << 3 ;
    private static final int GPIOLINE_FLAG_OPEN_SOURCE = 1 << 4 ;




    public GpioLineInfo() {
    
        this( 0 ) ;
    }


    public GpioLineInfo( int line ) {

        this.nativeStruct.lineOffset = line ;
    }


    public String getConsumer() {

        return new String( this.nativeStruct.consumer ) ;
    }


    public int getLine() {
    
        return this.nativeStruct.lineOffset ;
    }


    public GpioLineInfo setLine( int line ) {
    
        this.nativeStruct.lineOffset = line ;

        return this ;
    }


    public String getName() {
    
        return new String( this.nativeStruct.name ) ;
    }


    public boolean isActiveLow() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_ACTIVE_LOW ) > 0 ;
    }


    public boolean isOpenDrain() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_OPEN_DRAIN ) > 0 ;
    }


    public boolean isOpenSource() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_OPEN_SOURCE ) > 0 ;
    }


    public boolean isInput() {
    
        return !( this.isOutput() ) ;
    }

    public boolean isOutput() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_IS_OUT ) > 0 ;
    }


    public boolean isUsed() {
    
        return ( this.nativeStruct.flags & GPIOLINE_FLAG_KERNEL ) > 0 ;
    }
}
