
package xmodem;

import com.fazecast.jSerialComm.*;

public class Serial {
    
    SerialPort comPort;
     boolean test = false;
    
    Serial(){
        comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        comPort.setBaudRate(115200);
    }
    
    void testRead(){
        try {
           while (true)
           {
              byte[] readBuffer = new byte[2048];
              int numRead = comPort.readBytes(readBuffer, readBuffer.length);
              for(int i = 0; i < numRead; i++){
                  System.out.print((char)readBuffer[i]);
              }
           }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    void testWrite(byte[] bytes){
        try{
            comPort.writeBytes(bytes, bytes.length);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    
    void closePort(){
        comPort.closePort();
    }
    
    int crc(byte[] data){
        return 1;
    }
    
    int concatBytes(byte b1, byte b2){
        return((b1<<8)&0x0000ff00 | b2&0x000000ff);
    }
    byte leftbyte(int in){
        return (byte)((in >>8)&0x000000ff);
    }
    byte rightbyte(int in){
        return (byte)(in&0x000000ff);
    }
    
    void setTest(boolean test){
        this.test = test;
    }
    
}
