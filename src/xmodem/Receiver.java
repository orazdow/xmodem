
package xmodem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Receiver extends Serial{
    
    boolean printOutpt = false;
    FileOutputStream stream;   
    enum runmode{READING, WAITING, SENDING}
    runmode mode = runmode.WAITING;
    boolean frameStart = false;
    int expectednum = 0;
    
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
    
    byte getByte(){return read();}
    
    void readPacket(){
        int paritycount = 0;
        byte b = getByte();
        
        if(b != SOH){
            nack();
            System.out.println("SOH missing");
            return;   
        }   
        b = getByte();
        
        byte seqnum = (byte)(b&0xff);
        b = getByte();
        byte seqsum = (byte)((seqnum+b)&0xff);
        
        if((seqsum&0xff) != 255){
            nack();
            System.out.println("sequence block corrupt");
            return;
        }
        // hande expected seqnum
        if((seqnum&0xff)!= expectednum){
            nack();
            System.out.println("out of sequence error");
            return;          
        }
        
        for(int i = 0; i < 128; i++){
            data[i] = getByte();
            paritycount += Integer.bitCount(data[i]);

        }
        b = getByte();
        byte pcount = (byte)((paritycount%2)&0xff);
        
        if((pcount&0xff)!=(b&0xff)){
            nack();
            System.out.println("bad parity check");
            return;  
        }       
            b = getByte();
//        byte crc1 = getByte();
//        byte crc2 = getByte();
//        int _crc = 1;//concatBytes(crc1, crc2);
//        
        
//        if(_crc != crc(data)){
//            nack();
//            System.out.println("crc error");
//            return;
//        }
         flush();
        
        
    }
    
    void nack(){
        byte[] send = new byte[1];
        send[0] = NAK;
        write(send);
        flush();
    }
    
    void protocall2(){
        byte b = 0; //= getNextByte()
        
        if(b == FLAG){
            mode = runmode.READING;
        }
    }
    
    void run()throws InterruptedException{
        if(test){
             testRead();
        }else{
            while(true){
            readPacket();
            Thread.sleep(100);
        }
        }
    }
        
}
