
package xmodem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Receiver extends Serial{
    
    boolean printOutpt = false;
    FileOutputStream stream;
    boolean test = false;
    
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
    
    void setTest(boolean test){
        this.test = test;
    }
    
    void run(){
        if(test){
             testRead();
        }
    }
        
}
