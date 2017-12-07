
package xmodem;

import com.fazecast.jSerialComm.*;

public class Serial {
    
    SerialPort comPort;
    
    Serial(){
        comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        comPort.setBaudRate(115200);
    }
    
    void testRead(){
        try {
           while (true)
           {
              byte[] readBuffer = new byte[1024];
              int numRead = comPort.readBytes(readBuffer, readBuffer.length);
              for(int i = 0; i < numRead; i++){
                  System.out.print((char)readBuffer[i]);
              }
           }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    
    void closePort(){
        comPort.closePort();
    }
    
}
