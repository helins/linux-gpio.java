package io.dvlopt.linux.gpio ;


import com.sun.jna.Memory  ;
import com.sun.jna.Pointer ;




/**
 * Class representing a buffer for reading the state of several GPIO lines or writing new state.
 * <p>
 * It does not do any IO on its own.
 */
public class GpioHandleData {


    private final Memory buffer ;


    public final int size   ;




    /**
     * Basic constructor for 64 lines, the maximum.
     */
    public GpioHandleData() {
    
        this( 64 ) ;
    }




    /**
     * Constructor for a specified number of lines.
     *
     * @param size  How many lines.
     */
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




    /**
     * Retrieves the state of a given line.
     *
     * @param index  The position of the line as defined by the GPIO handle, not to be confused
     *               with the number of the line.
     *
     * @return  A boolean representing the state.
     */
    public boolean get( int index ) {

        this.checkBounds( index ) ;

        return this.buffer.getByte( index ) == 1 ;
    }




    /**
     * Sets the new state of a given line.
     *
     * @param index  The position of the line as defined by the GPIO handle, not to be confused
     *               with the number of the line.
     *
     * @param value  The new state.
     *
     * @return  This GpioHandleData.
     */
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
