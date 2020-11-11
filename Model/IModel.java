package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {
    public void generateMaze(int width,int height);
    public void solveMaze();
    public int[][] getMazeGrid();
    public Maze getGameObject();
    public void movePlayerModelLogic(KeyCode movement);
    public int getPlayerPosRowIdx();
    public int getPlayerPosColIdx();
    public void stopServers();
    public void saveMaze(File saveFile);
    public void loadMaze(File file);
    public Solution getSolution();

}
