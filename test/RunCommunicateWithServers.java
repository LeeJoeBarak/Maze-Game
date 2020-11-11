package test;

import IO.MyDecompressorInputStream;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Aviadjo on 3/27/2017.
 */
public class RunCommunicateWithServers {
    public static void main(String[] args) {
        int i;
            //Initializing servers
            Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
            Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
            //Server stringReverserServer = new Server(5402, 1000, new ServerStrategyStringReverser());

            //Starting  servers
            solveSearchProblemServer.start();
            mazeGeneratingServer.start();
            /*for(i=0; i < 20; i++) {
                //Communicating with servers
                CommunicateWithServer_MazeGenerating();
                CommunicateWithServer_SolveSearchProblem();
                //CommunicateWithServer_StringReverser();
            }*/

            //Communicating with servers
            CommunicateWithServer_MazeGenerating();
            CommunicateWithServer_SolveSearchProblem();
            //CommunicateWithServer_StringReverser();
            //Stopping all servers
            mazeGeneratingServer.stop();
            solveSearchProblemServer.stop();
            //stringReverserServer.stop();

    }

    private static void CommunicateWithServer_MazeGenerating() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy(){
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{2, 3};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[10000]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        Maze maze = new Maze(decompressedMaze);
                        /*print Maze*/
                        maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                        /*An attempt was made to read an object when the next element in the stream is primitive data. In this case,
                        the OptionalDataException's length field is set to the number of bytes of primitive data immediately readable from the stream,
                         and the eof field is set to false.*/
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(2, 3);
                        /* print maze */
                        maze.print();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read Solution from server
                        //Print Maze Solution retrieved from the server
                       if(mazeSolution != null){
                           System.out.println(String.format("Solution steps: %s", mazeSolution.getSolutionPath().size()));
                           ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                           for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                               System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                           }
                       }
                       else{
                           System.out.println("Solution from Server was null!");
                       }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void CommunicateWithServer_StringReverser() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5402, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        BufferedReader fromServer = new BufferedReader(new InputStreamReader(inFromServer));
                        PrintWriter toServer = new PrintWriter(outToServer);

                        String message = "Client Message";
                        String serverResponse;
                        toServer.write(message + "\n");
                        toServer.flush();
                        serverResponse = fromServer.readLine();
                        System.out.println(String.format("Server response: %s", serverResponse));
                        toServer.flush();
                        fromServer.close();
                        toServer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}