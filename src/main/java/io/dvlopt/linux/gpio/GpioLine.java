/*
 * Copyright 2018 Adam Helinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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
