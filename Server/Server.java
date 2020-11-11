package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;


public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy serverStrategy;
    private volatile boolean stop;
    private Thread mainThread;
    private ExecutorService threadPollExecutor;

    public Server(int port, int listeningIntervalMS, IServerStrategy serverStrategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.serverStrategy = serverStrategy;
        mainThread = new Thread(this::runServer);
        Configurations config = new Configurations();
        threadPollExecutor = Executors.newFixedThreadPool(Configurations.getAmountOfThreads());
    }

    public void start(){
        /* calls "runServer() */
        mainThread.start();
    }

    private void runServer()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            while (!stop)
            {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPollExecutor.execute(() -> {
                        try {
                            serverStrategy.serverStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                catch (IOException e) {
                    /* do nothing.. keep waiting*/
                    //e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stop()
    {
        //LOG.info("Stopping server..");
        this.stop = true;
        try {
            mainThread.join();
            threadPollExecutor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}