import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Collegue {

    public Client(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public void init(){
        try {
            client = new Socket(ip,port);
            outputStream = new DataOutputStream(client.getOutputStream());
            inputStream = new DataInputStream(client.getInputStream());
            teclado = new DataInputStream(System.in);
            Scanner sc = new Scanner(System.in);
            String msg = sc.nextLine();
            outputStream.writeUTF(name);
            outputStream.flush();
            receiveData();
            writeData();
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args){
        Client client = new Client("Gil", "localhost", 9500);
        client.init();
    }
}
