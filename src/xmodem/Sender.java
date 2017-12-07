
package xmodem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;



public class Sender extends Serial{
    
    byte[] data;
    File file;
   
    
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
    
    void run(){
        if(test){ //System.out.println(data.length);
             testWrite(data);
        }
    }
    
}
