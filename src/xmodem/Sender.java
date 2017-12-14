package xmodem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Sender extends Serial{
    
    boolean abort = false;
    boolean started = false;
    int tries = 0;
    int receivetries = 0;
    int receivelimit = 500;
    int startlimit = 500; 
    int sleeptimeout = 500;
    
    byte[] data;
    byte[] packet;
    File file;
    int filepos = 0;
    long packetnum = 0;
    boolean fileEnd = false;
    boolean finished = false;
    
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
    
    void run() throws InterruptedException{
        byte b = 0;
        while(!abort && !finished){
            if(started){
                routine();
            }else if((b = readByte()) == C_){
                started = true;   
                System.out.println("STARTING");
                flush();
            }else if(++tries < startlimit){
                int busywait = 0;
                while(busywait++ < 50000){}
            }else{
                abort("No signal from receiver");
            }
            
        }
    }
    
    byte[] makePacket(byte[] data){
        byte packet[] = new byte[133];
        byte d;
        int paritycount = 0;
        
        packet[0] = SOH;
        byte seq = (byte)((packetnum%255)&0xff);
        byte seq2 = (byte)((255-seq)&0xff);     
        packet[1] = seq;
        packet[2] = seq2;
        
        System.out.println("sending packet: "+(seq&0xff));
        
        for(int i = 0; i < 128; i++){
            if(!fileEnd && (filepos < data.length)){
            packet[3+i] = data[filepos]; 
            }else{ packet[3+i] = 0&0xff; fileEnd = true;}
            filepos++;
            paritycount += Integer.bitCount(packet[3+i]);
        }
        // parity count in crc's first byte for now
        packet[131] = (byte)((paritycount%2)&0xff);
        packet[132] = 0;
        packetnum++;
        return packet;       
    }
    
    void routine() throws InterruptedException{
        byte r = 0;
        if(!fileEnd)
        packet = makePacket(data); 
        System.out.println("writing "+packet.length+" bytes:");
//        for (int i = 0; i < packet.length; i++) {
//                System.out.print((char)packet[i]&0xff);
//        }
        write(packet);
        
        while(((r = readByte()) == 0 || r == C_)){
            if(++receivetries > receivelimit){
                abort("No response from receiver");
                return;
            }  
             int busywait = 0;
             while(++busywait < 20000){}
        }
        receivetries = 0;
        if((r&0xff) == NAK){ 
            System.out.println("RECEIVED NAK");
            if(!fileEnd){
            filepos -= 127;
            if(filepos < 0){filepos = 0;}
            packetnum = packetnum > 0? packetnum-1 : 0;
            }
        }else if((r&0xff) == ACK){
            System.out.println("RECEIVED ACK");
            if(fileEnd){ 
                finished = true; 
                System.out.println("Send complete");
            }     
        }else{
            System.out.println("unregognized response");
            System.out.println(Integer.toHexString(r&0xff));
            abort("NO RESPONSE");
        }
               
        System.out.println("OK+ "+Integer.toHexString(r&0xff)); //
        
    }

    void abort(String msg){
        abort = true;
        System.out.println(msg);
    }
    
}
