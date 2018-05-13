package io.dvlopt.linux.gpio ;




/**
 * Class representing a GPIO line.
 */
public class GpioLine {


    final int index ;

    /**
     * The number of the line.
     */
    public final int lineNumber ;




    GpioLine( int lineNumber ,
              int index      ) {
    
        this.lineNumber = lineNumber ;
        this.index      = index ;
    }
}
