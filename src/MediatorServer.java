import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MediatorServer implements  Mediator{

    public ServerSocket server;
    public int puerto = 9500;
    public Vector<Connection> connectionList = new Vector<Connection>();
    static int counter = 0;

    public MediatorServer(int puerto) {
        this.puerto = puerto;
    }

    public static void main(String args[]){
        MediatorServer server = new MediatorServer(9500);
        server.init();
    }

    @Override
    public void init() {
        Socket socket;
        try {
            server = new ServerSocket(puerto);
            System.out.println("Server ready");
            while (true){

                socket = server.accept();

                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                Connection connection = new Connection(socket, input, output);
                connectionList = connection.getList();
                connectionList.add(connection);
                connection.run();

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }


}
