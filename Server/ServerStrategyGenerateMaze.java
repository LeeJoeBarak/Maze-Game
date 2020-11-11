package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    @Override
    public void serverStrategy(InputStream inputStream, OutputStream outputStream) {
        try
        {
            ObjectInputStream fromClient = new ObjectInputStream(inputStream);
            ObjectOutputStream toClient = new ObjectOutputStream(outputStream);
            toClient.flush();
            int[] dim = (int[]) fromClient.readObject();

            AMazeGenerator generator = (AMazeGenerator)Configurations.getGenerator();
            Maze maze = generator.generate(dim[0],dim[1]);
            try{
            ByteArrayOutputStream bytearrayOS = new ByteArrayOutputStream();
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(bytearrayOS);
            compressor.write(maze.toByteArray());
            toClient.writeObject(bytearrayOS.toByteArray());
            toClient.flush();
            toClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    /**
     * Append a given string to file
     *
     * @param text some text
     * @param destinationFilePath the path of the destination file
     */
    public static void AppendFile(String text, String destinationFilePath) {
        try (java.io.FileWriter fw = new java.io.FileWriter(destinationFilePath, true)) {
            fw.write(text);
        } catch (IOException ex) {
            /*Read and write operations are synchronized to guarantee the atomic completion of critical operations;
            therefore invoking methods readLine(), readPassword(), format(), printf() as well as the read, format and write
             operations on the objects returned by reader() and writer() may block in multithreaded scenarios.
             Invoking close() on the objects returned by the reader() and the writer() will not
              close the underlying stream of those objects.*/
            //Console.printf(String.format("Error appending text to file: %s", destinationFilePath), ex);
            ex.printStackTrace();
        }
    }

    /**
     * Creates new file according to the given path
     *
     * @param destinationFilePath the path of the destination file
     */
    public static void CreateNewFile(String destinationFilePath) {
        File file = new java.io.File(destinationFilePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                //Console.PrintException("Error creating new file!", e);
                e.printStackTrace();
            }
        }
    }


}

