package test;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Aviadjo on 3/26/2017.
 */
public class RunCompressDecompressMaze {
    public static void main(String[] args) {
        String mazeFileName = "savedMaze.maze";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
        Maze maze = mazeGenerator.generate(5, 5); //Generate new maze

        try {
            // create compressor object
            OutputStream myCompressorOS = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            //Compress & write the given byte[] to FileOS (passed through the constructor)
            myCompressorOS.write(maze.toByteArray());
            myCompressorOS.flush();
            myCompressorOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte savedMazeBytes[] = new byte[0];
        try {
            //read & decompress maze from FileOS (passed through the constructor)
            InputStream myDecompressorOS = new MyDecompressorInputStream(new FileInputStream(mazeFileName));
            //create new empty byte[] of correct size
            savedMazeBytes = new byte[maze.toByteArray().length];
            // read the decompressed data from "myDecompressorOS" into byte[] (savedMazeBytes)
            myDecompressorOS.read(savedMazeBytes);
            myDecompressorOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create Maze instance from byte[] (savedMazeBytes)
        Maze loadedMaze = new Maze(savedMazeBytes);
        //compare the original Maze (maze) with Maze created from the decompressed file (loadedMaze)
        boolean areMazesEquals = Arrays.equals(loadedMaze.toByteArray(), maze.toByteArray());
        System.out.println(String.format("Mazes equal: %s",areMazesEquals)); //maze should be equal to loadedMaze
    }
}