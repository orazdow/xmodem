
package xmodem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Receiver extends Serial{
    
    boolean printOutpt = false;
    FileOutputStream stream;
    
    public static final byte FLAG = (byte)126; //0x7E
    public static final byte ESC = (byte)125; //0x7D
    
    public static final byte SOH = (byte)126; //0x7E
    public static final byte ACK = (byte)125; //0x7D   
    public static final byte NAK = (byte)125; //0x7D    
    
    enum runmode{READING, WAITING, SENDING}
    runmode mode = runmode.WAITING;
    boolean frameStart = false;
    
    byte data[] = new byte[128];

    
    Receiver(String path){
        super();
        if(path == null){
            printOutpt = true;
        }else{
            try{
                stream = new FileOutputStream(path);
            }catch(FileNotFoundException e){
                System.out.println(e.getMessage());
            }

        }
    }
    
    byte getByte(){return 0;}
    
    void xRead(){
        
        byte b = getByte();
        
        if(b == SOH)   
            b = getByte();
        
        int seqnum = b;
        b = getByte();
        byte seqsum = (byte)(seqnum+b);
        
        if(seqsum != 255){
            nack();
            System.out.println("sequence error");
            return;
        }
        // hande expected seqnum
        
        for(int i = 0; i < 128; i++){
            data[i] = getByte();
        }
        
        byte crc1 = getByte();
        byte crc2 = getByte();
        int _crc = 1;//concatBytes(crc1, crc2);
        
        
        if(_crc != crc(data)){
            nack();
            System.out.println("crc error");
            return;
        }
        
        
        
    }
    
    void nack(){
        
    }
     
    void protocall2(){
        byte b = 0; //= getNextByte()
        
        if(b == FLAG){
            mode = runmode.READING;
        }
    }
    
    void run(){
        if(test){
             testRead();
        }
    }
        
}
