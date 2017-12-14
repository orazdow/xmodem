package xmodem;

import com.fazecast.jSerialComm.*;


public class Serial {
    
    SerialPort comPort;
    byte[] bytebuff = new byte[1];
    
    public static final byte FLAG = (byte)126; //0x7E
    public static final byte ESC = (byte)125; //0x7D   
    
    public static final byte SOH = (byte)(1&0xff); 
    public static final byte ACK = (byte)(6&0xff); 
    public static final byte NAK = (byte)0x15;  
    public static final byte C_ = (byte)0x43;
    public static final byte EOT = (byte)(4&0xff); 

    public static final byte SUB = (byte)0x1a;  
    public static final byte NUL = (byte)(0&0xff);
    
    Serial(){
        try{
            comPort = SerialPort.getCommPorts()[0];
            comPort.openPort();
            comPort.setBaudRate(115200);
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
        }catch(Exception e){
            System.out.println("No COM ports found");
            return;
        }
    }
       
    byte readByte(){
        try {       
             comPort.readBytes(bytebuff, 1);
           } catch (Exception e) { e.printStackTrace(); }  
        return bytebuff[0];
    }   
    
    byte[] read(int num){
         try {       
              byte[] readBuffer = new byte[num];
              comPort.readBytes(readBuffer, num);
              return readBuffer;
           
        } catch (Exception e) { e.printStackTrace(); }  
         return null;
    } 
   
    void write(byte b){
        bytebuff[0] = b;
        try{
            comPort.writeBytes(bytebuff, 1);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }        
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
}
