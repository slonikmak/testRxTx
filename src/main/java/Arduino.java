import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by User on 22.03.2017.
 */
public class Arduino implements SerialPortEventListener {
    SerialPort serialPort;

    int byteCount = 0;
    StringBuilder builder = new StringBuilder();

    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyUSB0", // Linux
            "COM4", // Windows
    };

    /**
     * Buffered input stream from the port
     */
    private InputStream input;

    private BufferedReader reader;

    private BufferedWriter writer;
    /**
     * The output stream to the port
     */
    private static OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;

    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // iterate through, looking for the port
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            System.out.println(currPortId.getName());
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }

        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        } else {
            System.out.println("Found your Port");
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();

            reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

            writer = new BufferedWriter(new OutputStreamWriter(output));

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }


    public synchronized void serialEvent(SerialPortEvent oEvent) {

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                builder.append(reader.readLine());
                builder.append(" ");
                byteCount++;
                if (byteCount==3) {
                    byteCount =0;
                    System.out.println("Out "+builder.toString());
                    builder = new StringBuilder();
                }
                //int myByte=input.read();
                //System.out.println("Out "+(char)input.read());
                //System.out.println("Out"+reader.readLine());
                /*int value = myByte & 0xff;//byte to int conversion:0...127,-127...0 -> 0...255
                if(value>=0 && value<256){//make shure everything is ok
                    System.out.println((char)myByte);
                    //sendSingleByte((byte)myByte);
                }*/
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    public synchronized void writeData(int data) {
        //System.out.println("Sent: " + data);
        try {
            output.write(data);
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }
    public synchronized void writeData(byte data) {
        //System.out.println("Sent: " + data);
        try {
            output.write(data);
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }

    public synchronized void writeData(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            builder.append((char) data[i]);
        }
        //System.out.println("Sent: " + builder.toString());
        try {
            output.write(data);
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }



    public static void main(String[] args) throws Exception {


        final Arduino arduino = new Arduino();


        arduino.initialize();

        Thread.sleep(3000);
        arduino.writeData("020".getBytes());
        Thread.sleep(500);
        arduino.writeData("040".getBytes());
        Thread.sleep(500);
        arduino.writeData("060".getBytes());
        Thread.sleep(500);
        arduino.writeData("080".getBytes());
        Thread.sleep(500);
        arduino.writeData("100".getBytes());

    }
}