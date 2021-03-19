/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/. */



package io.helins.linux.gpio ;


import io.helins.linux.epoll.Epoll           ;
import io.helins.linux.epoll.EpollEvent      ;
import io.helins.linux.gpio.GpioEvent        ;
import io.helins.linux.gpio.GpioEventHandle  ;
import java.io.IOException                   ;




/**
 * Class for efficiently monitoring several GPIO events at once.
 */
public class GpioEventWatcher implements AutoCloseable {


    // Epoll event flags meant to be reused.
    //
    private static final EpollEvent.Flags eventFlags = new EpollEvent.Flags().set( EpollEvent.Flag.EPOLLIN  )
                                                                             .set( EpollEvent.Flag.EPOLLPRI ) ;




    // Epoll instances for the monitoring.
    //
    private Epoll epoll ;


    // Epoll event meant to be reused when waiting for events.
    //
    private EpollEvent epollEvent ;




    /**
     * Basic constructor allocating the needed resources.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public GpioEventWatcher() throws IOException {
    
        this.epoll      = new Epoll()      ;
        this.epollEvent = new EpollEvent() ;
    }




    /**
     * Closes this instance and release associated resources.
     * <p>
     * The previously registered GPIO event handles WILL NOT be closed.
     *
     * @throws IOException
     *           When an unplanned error occured.
     */
    public void close() throws IOException {

        this.epoll.close() ;
    }





    // For storing a file descriptor and an arbitrary int within a long.
    //
    private static long encodeUserData( int fd ,
                                        int id ) {
    
        return   fd
               | ( ( id & 0xffffffffL ) << 32 ) ;
    }




    // For retrieving a file descriptor stored in a long.
    //
    private static int decodeFD( long userData ) {
    
        return (int)userData ;
    }




    // For retrieving an ID stored in a long.
    //
    private static int decodeID( long userData ) {

        return (int)( userData >>> 32 ) ;
    }




    /**
     * Adds a GPIO event to monitor.
     *
     * @param   handle
     *            The GPIO event handle to monitor.
     *
     * @param   id
     *            An arbitrary identifier so that when this event happens, it can be recognized by
     *            the user.
     *
     * @return  This instance.
     *
     * @throws  IOException
     *            When an unplanned error occured.
     */
    public GpioEventWatcher addHandle( GpioEventHandle handle ,
                                       int             id     ) throws IOException {

        EpollEvent epollEvent = new EpollEvent() ;

        epollEvent
            .setFlags( eventFlags )
            .setUserData( encodeUserData( handle.fd ,
                                          id        ) ) ;
    
        this.epoll.add( handle.fd  ,
                        epollEvent ) ;

        return this ;
    }




    /**
     * Removes a GPIO event from being monitored.
     *
     * @param   handle
     *            The GPIO event handle to remove.
     *
     * @return  This instance.
     *
     * @throws  IOException
     *            When an unplanned error occured.
     */
    public GpioEventWatcher removeHandle( GpioEventHandle handle ) throws IOException {
    
        this.epoll.remove( handle.fd ) ;

        return this ;
    }




    /**
     * Waits forever until a GPIO event occurs.
     *
     * @param  data
     *           Will be overwritten in order to describe what happened.
     *
     * @throws IOException
     *            When an unplanned error occured.
     */
    public void waitForEvent( GpioEvent data ) throws IOException {
    
        this.waitForEvent( data ,
                           -1   ) ;
    }




    /** Waits `<strong>timeout</strong>` milliseconds at most until a GPIO event occurs.
     *
     * @param   data
     *            Will be overwritten in order to describe what happened.
     *
     * @param   timeout
     *            Timeout in milliseconds (-1 means forever).
     *
     * @return  True if an event occured within the given timeout.
     *
     * @throws  IOException
     *             When an unplanned error occured.
     */
    public boolean waitForEvent( GpioEvent data    ,
                                 int       timeout ) throws IOException {

        if ( this.epoll.wait( this.epollEvent ,
                              timeout         ) ) {

            long userData = this.epollEvent.getUserData() ;

            int id = decodeID( userData ) ;

            if ( epollEvent.getFlags().isSet( EpollEvent.Flag.EPOLLERR ) ) {

                throw new IOException( "Error condition detected for monitored input with id " + id ) ;
            }

            data.read( decodeFD( userData ) ,
                       id                   ) ;

            return true ;
        }

        return false ;
    }
}
