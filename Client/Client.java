
package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/** On instantiation of Socket the CONNECTION to ServerSocket is done internally(!)
 *new Socket("127.0.0.1", 9090); Creates a stream socket and connects it to the
 * specified port number at the specified IP address. */
public class Client{
    private IClientStrategy clientStrategy;
    private  InetAddress address;
    private int port;

    public Client(InetAddress address,int port, IClientStrategy clientStrategy) {
        this.clientStrategy = clientStrategy;
        this.address = address;
        this.port = port;
    }

    public void communicateWithServer()
    {
        try {
            Socket clientSocket = new Socket(address,port);
            //System.out.println("Client is connected to server!");
            clientStrategy.clientStrategy(clientSocket.getInputStream(),clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

