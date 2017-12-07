
package xmodem;

import com.fazecast.jSerialComm.*;

public class Serial {
 
    public static final byte FLAG = (byte)126; //0x7E
    public static final byte ESC = (byte)125; //0x7D
    
    public static final byte SOH = (byte)1; 
    public static final byte EOT = (byte)4; 
    public static final byte ACK = (byte)6; 
    public static final byte NAK = (byte)0x15;  
    public static final byte SUB = (byte)0x1a;  // using 0 

    
    SerialPort comPort;
    boolean test = false;
    
    Serial(){
        comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setBaudRate(115200);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
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
    
    byte read(){
        byte b = (byte)(0&0xff);
         try {       
              byte[] readBuffer = new byte[1];
              comPort.readBytes(readBuffer, 1);
              b = readBuffer[0];
           
        } catch (Exception e) { e.printStackTrace(); }  
         return b;
    }
    byte[] read(int num){
        byte b = (byte)(0&0xff);
         try {       
              byte[] readBuffer = new byte[num];
              comPort.readBytes(readBuffer, num);
              return readBuffer;
           
        } catch (Exception e) { e.printStackTrace(); }  
         return null;
    }  
    
    int bytesAvailable(){
        return comPort.bytesAvailable();
    }
    
    void write(byte[] bytes){
        try{
            comPort.writeBytes(bytes, bytes.length);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    void write(byte[] bytes, int len){
        try{
            comPort.writeBytes(bytes, len);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    void flush(){
        int remaining = 1;
        try {
           while (remaining > 1)
           {
              byte[] readBuffer = new byte[1024];
              remaining = comPort.readBytes(readBuffer, readBuffer.length);
           }
        } catch (Exception e) { e.printStackTrace(); }
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
