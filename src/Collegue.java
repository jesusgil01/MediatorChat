import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public abstract class Collegue {

    public Socket client;
    public DataOutputStream outputStream;
    public DataInputStream inputStream;
    public  DataInputStream teclado;
    String name;
    String ip;
    int port;

    public void receiveData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while ( true ){
                        String msgIn = inputStream.readUTF();
                        System.out.println(msgIn);
                    }
                } catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        });
        thread.start();
    }

    public void sendData(String msg){
        try {
            outputStream.writeUTF("<" + name + ">" + msg);
            outputStream.flush();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void writeData(String msg){
        try {
            //String line = "";
            //while (!line.equals(".bye")){
                System.out.println(name+": ");
                //line = teclado.readUTF().toString();

                outputStream.writeUTF(msg);
                outputStream.flush();
            //}
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

}
