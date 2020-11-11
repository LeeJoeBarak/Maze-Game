package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int playerPosRowIdx;
    private int playerPosColIdx;
    public StringProperty spPlayerPosRow = new SimpleStringProperty();
    public StringProperty spPlayerPosCol = new SimpleStringProperty();

    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            if(arg.equals("playerMove")){
                movePlayerwasInvoked();
            }
            if (arg.equals("mazeGenerated")){
                generateMazewasInvoked();
            }
            if ((arg.equals("mazeSolved"))){
                solveMazewasInvoked();
            }
            if((arg.equals("mazeLoaded"))){
                LoadMazewasInvoked();
            }
        }
    }

    private void generateMazewasInvoked(){ updatePlayerPosition();
        setChanged();
        notifyObservers("mazeGenerated");//call view.update()
         }
    private void LoadMazewasInvoked(){
        updatePlayerPosition();
        setChanged();
        notifyObservers("mazeLoaded");//call view.update()
    }
    private void solveMazewasInvoked(){
        setChanged();
        notifyObservers("mazeSolved");//call view.update()
    }
    private void movePlayerwasInvoked(){
        updatePlayerPosition();
        setChanged();
        notifyObservers("playerMove");//call view.update()
    }

    /*-------UTILS--------------------------------------------------------------*/
    public boolean validateMazeGenerationParams(int row, int col) {
        if (row <= 1 || col <= 1) {
            return false;
        }
        return true;
    }

    private void updatePlayerPosition(){
        playerPosRowIdx = model.getPlayerPosRowIdx();
        playerPosColIdx = model.getPlayerPosColIdx();
        spPlayerPosRow.set(playerPosRowIdx + "");
        spPlayerPosCol.set(playerPosColIdx + "");
    }

    /*-------MyViewController(and such) methods-----------------------------*/
    public Maze getMazeObject(){
        return model.getGameObject();
    }
    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }
    public void SaveGame(File saveFile) throws IOException {
        model.saveMaze(saveFile);
    }
    public void loadGame(File file) throws IOException, ClassNotFoundException {
        model.loadMaze(file);
    }
    public void movePlayer(KeyCode direction){
        model.movePlayerModelLogic(direction);
    }
    public void solveMaze(){
        model.solveMaze();
    }
    public void stopServers(){
        model.stopServers();
    }


    public int[][] getMaze() {
        return model.getMazeGrid();
    }
    public int getPlayerPosRow() {
        return playerPosRowIdx;
    }
    public int getPlayerPosCol() {
        return playerPosColIdx;
    }
    public Solution getSolution(){ return model.getSolution() ; }


}
