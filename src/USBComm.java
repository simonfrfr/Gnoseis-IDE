import gnu.io.*;

import java.io.*;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.IOException;

import org.jfree.util.Log;

/**
 * This class provides the utilities to read the data exchanged via USB port.
 */
public class USBComm implements SerialPortEventListener {

	/**
	 * Stream for the storage of incoming data
	 */
	private InputStream inputStream;
	/**
	 * Stream for the dispatching of data
	 */
	private OutputStream outputStream;
	/**
	 * Timeout of the USB port
	 */
	private final int PORT_TIMEOUT = 2000;
	/**
	 * Representation of the serial port used for the communication
	 */
	private SerialPort serialPort;
	/**
	 * Buffer that stores the received bytes from the media
	 */
	public boolean started = false;
	protected LinkedBlockingQueue<Byte> receivedBytes;

	/**
	 * Builds a new manager for the communication via USB port.
	 * @exception IOException if an error occurred during the opening of the USB port
	 */
	public USBComm(String d) throws IOException {
	  receivedBytes = new LinkedBlockingQueue<Byte>(100000);
		String port = d; //place the right COM port here, OS dependent
	
		//Check that the USB port exists and is recognized:
		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
		boolean portFound = false;
		CommPortIdentifier portId = null;
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
		    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
		    	Log.info(portId.getName());
				if (portId.getName().equals(port)) {
					System.out.println("Found port: " + port);
				    portFound = true;
				    break;
				} 
		    } 
		} 
	
		if (!portFound) 
		    throw new IOException("port " + port + " not found.");
	
		try {
			System.out.println("USB port opening...");
		    serialPort = (SerialPort) portId.open("USBCommunicator", PORT_TIMEOUT);
		    System.out.println("USB port opened");
		    inputStream = serialPort.getInputStream();
		    outputStream = serialPort.getOutputStream();
		    serialPort.addEventListener(this);
			    	serialPort.notifyOnDataAvailable(true);
			//#==================================================================#
			// WARNING! - DO NOT SET THE FOLLOWING PROPERTY WITH RXTX LIBRARY, IT
			// 			  CAUSES A PROGRAM LOCK:
			// 	serialPort.notifyOnOutputEmpty(true);
			//#==================================================================#
			    	
		    //wait for a while to leave the time to javax.comm to
		    //correctly configure the port:
		    Thread.sleep(1000);
		    
			int baudRate = 115200; //set propertly
	    	serialPort.setSerialPortParams(baudRate, 
	    		SerialPort.DATABITS_8, 
	    		SerialPort.STOPBITS_1, 
				SerialPort.PARITY_NONE);
		    
	    	serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	    		
		    System.out.println("setted SerialPortParams");
		    started = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new IOException(e.getMessage());
		}
	}
	public USBComm(){started = false;}
	
	public void closeUSB(){
		//close the streams for serial port:
		try {
			inputStream.close();
			outputStream.close();
			started = false;
		} catch (IOException e) {
			System.out.println("Cannot close streams:" + e.getMessage());
		}
	}

	/**
	 * Listener for USB events
	 * 
	 * @param event new event occurred on the USB port
	 */
	public void serialEvent(SerialPortEvent event){
		switch (event.getEventType()) {
	
			//case SerialPortEvent.BI:
			//case SerialPortEvent.OE:
			//case SerialPortEvent.FE:
			//case SerialPortEvent.PE:
			//case SerialPortEvent.CD:
			//case SerialPortEvent.CTS:
			//case SerialPortEvent.DSR:
			//case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				//nothing to do...
			    break;
	
			case SerialPortEvent.DATA_AVAILABLE:
				byte received = -1;
				do {
					try {
						received = (byte)inputStream.read();
					} catch (IOException e) {
						System.out.println("Error reading USB:" + e.getMessage());
					}
				
					synchronized (receivedBytes) {
						try {
							//System.out.print((char) received);
							
							receivedBytes.add(received);
						} catch (IllegalStateException ew) {
							System.out.println(ew.getMessage());
							receivedBytes.poll();
							receivedBytes.add(received);
						}
					}
				} while(received != -1);

			    break;
		}
	}

	protected void write(byte[] buffer){
	    try {
	    	outputStream.write(buffer);
	    	outputStream.flush();
	    } catch (Exception e) {
	    	System.out.println("Cannot write: " + e.getMessage());
	    }
	}
}