
package xmodem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;



public class Sender extends Serial{
    
    byte[] data;
    File file;
    int filepos = 0;
    long packetnum = 0;
    boolean fileEend = false;
   
    
    Sender(String path){
        try { 
            file = new File(path);      
            if(!file.exists()){
                System.out.println("file not found");
                return;
            }     
            data = Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    void routine()throws InterruptedException{
       byte[] packet = makePacket(data);
       write(packet);
       while((read()&0xff)!= (ACK&0xff)){
           System.out.println("waiting for ack");
           Thread.sleep(100);
       }
        System.out.println("RECEIVED ACK!");
    }
    
    byte[] makePacket(byte[] data){
        
        byte packet[] = new byte[133];
        int paritycount = 0;
        byte d;
        packet[0] = SOH;
        byte seq = (byte)((packetnum%255)&0xff);
        byte seq2 = (byte)((255-seq)&0xff);     
       // ((seq+seq2)&0xff) == 255)
       packet[1] = seq;
       packet[2] = seq2;
       
       for(int i = 0; i < 128; i++){
           if(!fileEend){
               d = getByte(data);
           }else{ d = (byte)(0&0xff); }
           packet[3+i] = d;
           paritycount += Integer.bitCount(d);
       }
       packet[131] = (byte)((paritycount%2)&0xff);
       packet[132] = 0;
       
       return packet;
    }
    
    Byte getByte(byte[] data){ 
         Byte rtn = 0&0xff;
        if(filepos < data.length){
             rtn = data[filepos]; 
             filepos++;
        }else{
            fileEend = true;
        }
        return rtn;
    } 
    
    
    void run()throws InterruptedException{
        if(test){ 
             write(data);
        }else{
            while(true){
                routine();
            }
        }
    }
    
    
}
