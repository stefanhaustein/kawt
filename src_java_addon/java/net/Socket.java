// Socket.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-09-05 MK fixed a bug in getInputStream/getOutputstream
// 2000-07-24 MK added Documentaion        
//
//#include ..\..\license.txt
//
// kAWT version 0.95
//
// Copyright (C) 1999-2000 by Michael Kroll & Stefan Haustein GbR, Essen
//
// Contact: kawt@kawt.de
// General Information about kAWT is available at: http://www.kawt.de
//
// Using kAWT for private and educational and in GPLed open source
// projects is free. For other purposes, a commercial license must be
// obtained. There is absolutely no warranty for non-commercial use.
//
//
// 1. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO
//    WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE
//    LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT
//    HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM "AS IS" WITHOUT
//    WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT
//    NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
//    FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS TO THE
//    QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE
//    PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY
//    SERVICING, REPAIR OR CORRECTION.
//   
// 2. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN
//    WRITING WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY
//    MODIFY AND/OR REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE
//    LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL,
//    INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
//    INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF
//    DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU
//    OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY
//    OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN
//    ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
//   
//    END OF TERMS AND CONDITIONS
// 
//#endinclude

package java.net;

import java.io.*;
import javax.microedition.io.*;


/** 
 * This class implements client sockets (also called just "sockets"). A socket is an endpoint for communication between two machines. 
 * It is done by encapsulating java.microedition.io.Connector.
 */

public class Socket {
    
    private String remoteHost = null;
    private int remotePort = 0;
    private StreamConnection socket = null;
    private InputStream is = null;
    private OutputStream os = null;
    
    /**
     * Creates a stream socket and connects it to the specified port number on the named host.
     * @param host the host name.
     * @param port the port number.
     * @exception IOException if an I/O error occurs when creating the socket.
     */

    public Socket (String host, int port) throws IOException, UnknownHostException {
	String target = "socket://" + host + ":" + String.valueOf (port);
	socket = (StreamConnection) Connector.open (target, Connector.READ_WRITE, true);
    }
    
    
    /**
     * Returns an input stream for this socket.
     * @return an output stream for writing bytes to this socket.
     * @exception if an I/O error occurs when creating the output stream.
     */
    
    public InputStream getInputStream () throws IOException {
	if (is == null && socket != null)
	    is = socket.openInputStream ();
	return is; 
    }


    /**
     * Returns an output stream for this socket.
     * @return an output stream for writing bytes to this socket.
     * @exception an output stream for writing bytes to this socket.
     */
    
    public OutputStream getOutputStream () throws IOException {
	if (os == null && socket != null)
	    os = socket.openOutputStream ();
	return os; 
    }

    /**
     * Returns the remote port to which this socket is connected.
     * @return the remote port number to which this socket is connected.
     */
    
    public int getPort () {
	return remotePort;
    }
    
    
    /**
     * Closes this socket.
     * @exception IOException if an I/O error occurs when closing this socket.
     */
    
    public void close () throws IOException {
	if (is != null)
	    is.close ();
	if (os != null)
	    os.close ();
	socket.close ();
    }
}



