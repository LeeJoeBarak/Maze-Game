package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class ServerStrategySolveSearchProblem implements IServerStrategy{
    private File mazesDir;
    private File solutionsDir;
    int mazeSN;

    public  ServerStrategySolveSearchProblem(){
        mazeSN = 0;/*the mazes Serial Number is used to name maze-files*/
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        /* tried using this File Ctor to avoid submission systems trouble (idk if windows or linux/unix)*/
        File tempDirectory = new File(tempDirectoryPath, "SolvedMazesArchive");
        mazesDir = new File(tempDirectory.getPath(), "Mazes");
        solutionsDir = new File(tempDirectory.getPath(), "Solutions");
        try {/*mkdir could throw Security Exception*/
                 boolean dirCreated = tempDirectory.mkdir();
                 boolean mazesDirCreated = mazesDir.mkdir();
                 boolean solutionsDCreated = solutionsDir.mkdir();
                 /*
            if(!dirCreated || !mazesDirCreated || !solutionsDCreated ){
                throw new Exception("Solver wasn't able to create the temp directory");
            }*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * sends a Solution object back to the client, which solves the Maze object received by the Client.
     * each request's data(Maze/Solution) is saved and if the given maze exists in the database
     * then its existing solution will be sent to client .
     * @param inputStream The client's input stream
     * @param outputStream  The client's output stream
     */
    @Override
    public synchronized void serverStrategy(InputStream inputStream, OutputStream outputStream) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inputStream);
            ObjectOutputStream toClient = new ObjectOutputStream(outputStream);
            Maze maze = (Maze)fromClient.readObject();
            Solution solution;
            /* check if a solution exists or solve the maze */
            int mazeSN = findMazeInDir(maze);//-1 if wasn't found
            if (mazeSN != -1/*a solution already exists*/) {
                solution = getExistingSolution(mazeSN);
            } else/*a solution wasn't found*/{
                solution = solveMazeInServerStrategy(maze);
            }
            /* send solution to client */
            toClient.writeObject(solution);
            /* flush out stream and close it*/
            toClient.flush();
            toClient.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private synchronized int findMazeInDir(Maze maze){
        /*search in existing mazes dir */
        File[] mazesFileArr = mazesDir.listFiles();//unordered list!
        try {
            int i;
            if (mazesFileArr != null/*dir exists*/) {
                for (i = 0; i < mazesFileArr.length; i++) {
                    if (mazesFileArr[i].isFile()/*not a dir*/) {
                        byte[] baCurrMaze = Files.readAllBytes(mazesFileArr[i].toPath());
                        /*maze exists and equals file number i*/
                        if (Arrays.equals(baCurrMaze, maze.toByteArray())) {
                            /*return the file name (SN) of the matching maze*/
                           return Integer.parseInt(mazesFileArr[i].getName());
                        }
                    }
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        /* maze wasn't found in dir */
        return -1;
    }

    /**
     * maze already exists is dir
     * returns the Solution object for the maze [or null if a Exception was thrown]
     * @param mazeSN the SN of the maze to which a Solution is required */
    private synchronized Solution getExistingSolution(int mazeSN) throws IOException,  ClassNotFoundException{
            ObjectInputStream solutionFile = new ObjectInputStream(new FileInputStream(new File(solutionsDir.getPath(), ""+mazeSN)));
            Solution solution = (Solution) solutionFile.readObject();
            return solution;
    }


    /**
     * maze solution DOESN'T exist in system
     * the method solves the maze and returns the Solution object
     * @param maze - a Maze object to solve */
    private synchronized Solution solveMazeInServerStrategy(Maze maze) throws IOException {
            /* write the maze to file */
            OutputStream mazeOS = new FileOutputStream(new File(mazesDir.getPath(), ""+ mazeSN));
            mazeOS.write(maze.toByteArray());
            /*get searching Algorithm from config */
            ISearchingAlgorithm solver = Configurations.getSearchingAlgorithm();
            /* solve Maze */
            Solution solution = solver.solve(new SearchableMaze(maze));
            try{
                if(solution == null){
                    throw new Exception("null solution received for Maze ");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            /*write the Solution to file */
            ObjectOutputStream solutionOS = new ObjectOutputStream(new FileOutputStream(new File(solutionsDir.getPath(), ""+ mazeSN)));
            solutionOS.writeObject(solution);
            /*increment the SN tracker "mazeSN" AFTER done handling current call's maze
            (the first ever call to solve() will create files with name: "0")  */
            mazeSN++;
            return solution;
    }
}








