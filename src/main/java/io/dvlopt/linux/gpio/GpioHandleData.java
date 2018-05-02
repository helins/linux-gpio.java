package io.dvlopt.linux.gpio ;


import com.sun.jna.Memory  ;
import com.sun.jna.Pointer ;




public class GpioHandleData {




    private Memory buffer ;
    public  int    size   ;




    public GpioHandleData() {
    
        this( 64 ) ;
    }




    public GpioHandleData( int size ) {

        if ( size > 64 ) {
        
            throw new IllegalArgumentException( "Size must be <= 64" ) ;
        }


        this.buffer = new Memory( size ) ;
        this.size   = size               ;
    }




    private void checkBounds( int index ) {
    
        if ( index >= this.buffer.size() ) {
        
            throw new IndexOutOfBoundsException() ;
        }
    }




    public boolean get( int index ) {

        this.checkBounds( index ) ;

        return this.buffer.getByte( index ) == 1 ;
    }




    public GpioHandleData set( int     index ,
                               boolean value ) {

        this.checkBounds( index ) ;

        this.buffer.setByte( index               ,
                             (byte)( value ? 1
                                           : 0 ) ) ;

        return this ;
    }




    Pointer getPointer() {
    
        return (Pointer)( this.buffer ) ;
    }
}
