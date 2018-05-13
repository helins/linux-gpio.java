package io.dvlopt.linux.gpio ;


import com.sun.jna.Memory            ;
import com.sun.jna.Pointer           ;
import io.dvlopt.linux.gpio.GpioLine ;




/**
 * Class representing a buffer for reading and/or writing the state of up to 64 GPIO lines.
 * <p>
 * It does not do any IO on its own.
 */
public class GpioBuffer {


    /**
     * How many lines a buffer can drive.
     */
    public static final int SIZE = 64 ;


    private final Memory buffer ;




    /**
     * Basic constructor.
     */
    public GpioBuffer() {
    
        this.buffer = new Memory( SIZE ) ;

        this.buffer.clear() ;
    }




    /**
     * Retrieves the state of a given line.
     *
     * @param line  The GPIO line
     *
     * @return  A boolean representing the state.
     */
    public boolean get( GpioLine line ) {

        return this.buffer.getByte( line.index ) == 1 ;
    }




    /**
     * Sets the new state of a given line.
     *
     * @param line  The GPIO line.
     *
     * @param value  The new state.
     *
     * @return  This GpioBuffer.
     */
    public GpioBuffer set( GpioLine line  ,
                           boolean  value ) {

        this.buffer.setByte( line.index          ,
                             (byte)( value ? 1
                                           : 0 ) ) ;

        return this ;
    }




    /**
     * Toggles the state of a given line.
     *
     * @param line  The GPIO line.
     *
     * @return  This GpioBuffer.
     */
    public GpioBuffer toggle( GpioLine line ) {
    
        this.set( line                ,
                  !( this.get( line ) ) ) ;

        return this ;
    }




    /**
     * Clears this buffer by setting every state to false.
     *
     * @return  This GpioBuffer.
     */
    public GpioBuffer clear() {
    
        this.buffer.clear() ;

        return this ;
    }




    Pointer getPointer() {
    
        return (Pointer)( this.buffer ) ;
    }
}
