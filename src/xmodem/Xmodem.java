
package xmodem;


public class Xmodem {

    static String filePath = null;
    static String opt = null;
    static boolean isString = false;
    static boolean test = false;
    static String mode = "READ";
    
    public static void main(String[] args) {
       
        // parse arguments:
        // -r : read to file, -rs: read and output string, -w: write from file, -ws: write from string 
        // -tr: test read, -tw: test write
        for(String arg : args){
            if(arg.charAt(0) == '-'){
                opt = arg.replace("-", "").toLowerCase();               
            }else{ filePath = arg; }
        }     
        if(opt == null){
            System.out.println("first option required, permitted: -r -rs -w -ws");
            return;
        }
        if(!(opt.equals("rs")|| opt.equals("tr"))){
            if(filePath == null){ System.out.println("file path or string required"); return; }
        }
        if(!(opt.equals("r") || opt.equals("rs") || opt.equals("w") || opt.equals("ws"))){
            if(opt.equals("tr") || opt.equals("tw")){
                test = true;
            }else{ System.out.println("invalid option"); return; }           
        }     
        if(opt.equals("ws")){
            isString = true;
        }
        if(opt.equals("ws") || opt.equals("w") || opt.equals("tw")){
           mode = "WRITE";
        }
        
        if(mode.equals("READ")){
            Receiver r = new Receiver(filePath);
            if(test){
                r.setTest(test);
                System.out.println("test read mode");
            }
           try{ 
            r.run();
           }catch(InterruptedException e){
               System.out.println(e.getMessage());
           }
        }else{
            Sender s = new Sender(filePath);
            if(test){
                s.setTest(test);
                System.out.println("test write mode");
            }
            try{
            s.run();
            }catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
    
}
