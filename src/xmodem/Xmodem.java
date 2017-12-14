package xmodem;

public class Xmodem {

      
    public static void main(String[] args) {
       
        if(args.length != 2){
            System.out.println("2 arguments required: -r/s filename");
            return;
        }else if(args[0].equals("-r")){
            Receiver r = new Receiver(args[1]);
            try {
                System.out.println("starting receive");
                r.run();
                r.closePort();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }             
        }else if(args[0].equals("-s")){
            Sender s = new Sender(args[1]);
            try {
                System.out.println("starting send");
                s.run();
                s.closePort();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }             
        }
    }
    
}
