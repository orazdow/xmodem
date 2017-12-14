package xmodem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class Receiver extends Serial{
    
    boolean abort = false;
    boolean started = false;
    int starttries = 0;
    int sohtries = 0;
    int startlimit = 10000; //.....
    int sohlimit = 20;
    int sleeptimeout = 200;
    int sohtimeout = 200;
    int expectednum = 0;
    boolean lastpacket = false;
    boolean finished = false;
    boolean csent = false;
    
    File file;
    FileOutputStream out;
    
    Receiver(String path){
        try {
            file = new File(path);
            out = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            System.out.println("Could not create file");
            ex.printStackTrace();
            return;
        }
    }
    

    void run() throws InterruptedException{        
        while(!abort && !finished){ 
            routine();
            if(!started){
            if(++starttries >= startlimit){
                abort("No response from sender"); 
                return;
            }else{               
                write(C_);
                int busywait = 0;
                while(busywait++ < 100000){ }           

            }
            }
        }  
        if(finished){
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                System.out.println("IOExcpetion: error writing file\n"+ex.getMessage());
            } 
            System.out.println("received file");
        }
    }
    
    void routine() throws InterruptedException{
        byte data[] = new byte[128];
        int paritycount = 0;
        boolean nullfound = false;
        
        byte b = readByte();        
        if(b == SOH){ System.out.println("Received SOH");
            started = true;
        }else if(!started){
            return;
        }else if(++sohtries < sohlimit){
            write(NAK); 
            Thread.sleep(sohtimeout);
            return;
        }else{
            abort("Frame header timeout");
            return;
        }
        byte seqnum = (byte)(readByte()&0xff);
        b = (byte)(readByte()&0xff);
        byte seqsum = (byte)((seqnum+b)&0xff);
        
        if((seqsum&0xff) != 0xff){
            write(NAK);
            System.out.println("sequence block corrupt");
            return;
        }
        if((seqnum&0xff)!= expectednum){
            write(NAK);
            System.out.println("out of sequence error");
            return;          
        }
        
        for(int i = 0; i < 128; i++){
              b = (byte)(readByte()&0xff);
              if(b == NUL){nullfound = true;}
              data[i] = b;
              paritycount += Integer.bitCount(b);            
        }
        // eof check
        if(nullfound){ if(data[127] == NUL){ lastpacket = true; } }
        
        byte pcount = (byte)((paritycount%2)&0xff);
        b = (byte)(readByte()&0xff);
        if((pcount&0xff)!=(b&0xff)){
            write(NAK);
            System.out.println("bad parity check");
            return;  
        }  
        
        b = (byte)(readByte()&0xff);
            
        System.out.println("ACK OK");  
        appendData(data);
        if(lastpacket){ finished = true; }
        write(ACK);
        if(!lastpacket)
        expectednum++;
    }
    
    void appendData(byte[] data){
        try {
            for (int i = 0; i < data.length; i++) {
                System.out.print((char)data[i]&0xff);
            }
            System.out.println("");
            out.write(data);
        } catch (IOException ex) {
            abort("IOExcpetion: error writing to file\n"+ex.getMessage());
        }
    }
    
    void abort(String msg){
        abort = true;
        System.out.println(msg);
    }
    
}
