package View;

import ViewModel.MyViewModel;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements IView, Observer {
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public TextField txtfld_rowsNum;
    @FXML
    public TextField txtfld_colsNum;
    @FXML
    public Label lbl_playerRow;
    @FXML
    public Label lbl_playerColumn;
    @FXML
    public Button btn_generateMaze;
    @FXML
    public Pane pane;

    private Stage stage;
    private MyViewModel viewModel;
    public static boolean mute=false;
    public StringProperty playerPosRow = new SimpleStringProperty();
    public StringProperty playerPosCol = new SimpleStringProperty();

    private int displayCounter = 1;//todo ehat is this?!@#
    //private Timeline timeline = new Timeline(60);//todo what is this?

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            if (arg.equals("mazeGenerated")) {
                generateNewMazeWasInvoked();
            }
            if ((arg.equals("mazeSolved"))) {
                solveMazeWasInvoked();
            }
            if (arg.equals("playerMove")) {
               playerMoveWasInvoked();
            }
            /* load a maze file from current system disk */
            if ((arg.equals("mazeLoaded"))) {
                loadMazeWasInvoked();
            }
        }
    }

    private void generateNewMazeWasInvoked(){
         /*In JavaFX, a node is rendered as disabled, and ignores any user input,
                 if its disable property is true*/
        btn_generateMaze.setDisable(false);
        mazeDisplayer.setMaze(viewModel.getMazeObject());
        int playerRowIdx = viewModel.getMazeObject().getStartPosition().getRowIndex();
        int playerColIdx = viewModel.getMazeObject().getStartPosition().getColumnIndex();
        mazeDisplayer.setPlayerStartPosition(playerRowIdx, playerColIdx);
        bindProperties(viewModel);
        displayMaze(viewModel.getMaze());
    }
    private void solveMazeWasInvoked(){
        mazeDisplayer.setSolutionObj(getSolutionFromViewModel());
        mazeDisplayer.drawSolution();
    }
    private void playerMoveWasInvoked(){
        displayMaze(viewModel.getMaze());
    }
    private void loadMazeWasInvoked(){
        mazeDisplayer.setMaze(viewModel.getMazeObject());
        mazeDisplayer.setMazeGridAndRedraw(viewModel.getMaze());
        mazeDisplayer.setPlayerPositionAndRedraw(viewModel.getPlayerPosRow(), viewModel.getPlayerPosCol());
        bindProperties(viewModel);
        displayMaze(viewModel.getMaze());
    }


    private void bindProperties(MyViewModel viewModel) {
        lbl_playerRow.textProperty().bind(viewModel.spPlayerPosRow);
        lbl_playerColumn.textProperty().bind(viewModel.spPlayerPosCol);
    }

    public String getPlayerPosRow() {
        return playerPosRow.get();
    }
    public String getPlayerPosCol() {
        return playerPosCol.get();
    }
    /*--------------------- HANDLERS ------------------------------------------------*/
    /** Handler for exit Menu control*/
    public void exit(){
        System.exit(0);
    }
    /**About->Information Menu Item Handler */
    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 748, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }
    /**Options->Properties Menu Item Handler */
    public void Options() throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Properties");
        FXMLLoader propFXML = new FXMLLoader(getClass().getResource("/View/Properties.fxml"));
        Parent root = propFXML.load();
        PropertiesController propController = propFXML.getController();
        propController.setStage(stage);
        Scene scene = new Scene(root, 500, 250);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    /** Help->Game Instructions */
    public void Help() {
        try{
        Stage newStage = new Stage();
        newStage.setTitle("Help");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
        Scene scene = new Scene(root, 1000, 500);//possible to specify w and h
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        newStage.show();
        }catch (Exception e){ }
    }
    /**File->Save Menu Item Handler */
    @Override
    public void Save() throws IOException {
        FileChooser fileChooser = new FileChooser();
        /*
        System.getProperty("user.home") :
        on Windows:  home directory of the current logged in user. c:\Users\${current_user_name}
        * on Linux: "/home/user/"  */
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("User's previous maze games", "*.maze")
        );
        fileChooser.setInitialFileName("mySavedMazeGame");
        File saveFile = fileChooser.showSaveDialog(stage);
        if (saveFile != null) {
            viewModel.SaveGame(saveFile);
        }
    }
    /**File->New Menu Item Handler */
    @Override
    public void New(){
        mazeDisplayer.mediaPlayer.stop();
        try {
            int rows = Integer.parseInt(txtfld_rowsNum.getText());
            int columns = Integer.parseInt(txtfld_colsNum.getText());
            if (viewModel.validateMazeGenerationParams(rows, columns)) {
                btn_generateMaze.setDisable(false);
                viewModel.generateMaze(rows, columns);
                mazeDisplayer.audioChooser(1);
            } else {
                showAlert("Aw, man! are you trying to create a negative size maze? we're putting you on Megaseeds... ");
            }
        } catch (NumberFormatException e) {
            showAlert("Aw, man! are you trying to create a negative size maze? we're putting you on Megaseeds... ");
        }
    }
    /**File->Load Menu Item Handler */
    @Override
    public void Load() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze Files", "*.maze"));
        File loadFile = fileChooser.showOpenDialog(stage);
        if (loadFile != null) {
            viewModel.loadGame(loadFile);
            mazeDisplayer.audioChooser(1);
        } else {
        }
    }
    /**
     * Handler for btn_generateMaze
     */
    public void generateMaze() {
        try {
            int height = Integer.parseInt(txtfld_rowsNum.getText());
            int width = Integer.parseInt(txtfld_colsNum.getText());
            if (viewModel.validateMazeGenerationParams(height, width)) {
                btn_generateMaze.setDisable(true);
                this.viewModel.generateMaze(width, height);
                mazeDisplayer.audioChooser(1);
            } else {
                showAlert("Aw, man! are you trying to create a negative size maze? we're putting you on Megaseeds... ");
            }
        } catch (NumberFormatException e) {
            showAlert("Aw, man! are you trying to create a negative size maze? we're putting you on Megaseeds... ");
        }
    }
    /**handler for MUTE button */
    public  void MuteMaze(){
        if(mute){
            mute = false;
        }
        else{ mute = true;
        }
        MazeDisplayer.stopMusic();
    }
    /**
     * Solve Maze button handler
     * get maze from server ,solving it and returning solution.
     */
    public void solveMaze() {
        viewModel.solveMaze();
    }
    /**
     * handler for onMouseDragged(dragginf the player) inside the Maze  */
    public void mouseDragged(MouseEvent mouseEvent) {
        if(viewModel.getMaze() != null) {
            int maximumSize = Math.max(viewModel.getMaze()[0].length, viewModel.getMaze().length);
            double mousePosX=helperMouseDragged(maximumSize,mazeDisplayer.getHeight(),
                    viewModel.getMaze().length,mouseEvent.getX(),mazeDisplayer.getWidth() / maximumSize);
            double mousePosY=helperMouseDragged(maximumSize,mazeDisplayer.getWidth(),
                    viewModel.getMaze()[0].length,mouseEvent.getY(),mazeDisplayer.getHeight() / maximumSize);
            if ( mousePosX == viewModel.getPlayerPosCol() && mousePosY < viewModel.getPlayerPosRow() )
                viewModel.movePlayer(KeyCode.NUMPAD8);
            else if (mousePosY == viewModel.getPlayerPosRow() && mousePosX > viewModel.getPlayerPosCol() )
                viewModel.movePlayer(KeyCode.NUMPAD6);
            else if ( mousePosY == viewModel.getPlayerPosRow() && mousePosX < viewModel.getPlayerPosCol() )
                viewModel.movePlayer(KeyCode.NUMPAD4);
            else if (mousePosX == viewModel.getPlayerPosCol() && mousePosY > viewModel.getPlayerPosRow()  )
                viewModel.movePlayer(KeyCode.NUMPAD2);

        }
    }
    private  double helperMouseDragged(int maxsize, double canvasSize, int mazeSize,double mouseEvent,double temp){
        double cellSize=canvasSize/maxsize;
        double start = (canvasSize / 2 - (cellSize * mazeSize / 2)) / cellSize;
        double mouse = (int) ((mouseEvent) / (temp) - start);
        return mouse;
    }
    /** handler */
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
    /** handler */
    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent.getCode());
        keyEvent.consume();
    }
    /**Handler for zoom in/out on ctrl + MouseScroller */
    public void setOnScroll(ScrollEvent scroll) {
        if (scroll.isControlDown()) {
            double zoom_fac = 1.05;
            if (scroll.getDeltaY() < 0) {
                zoom_fac = 2.0 - zoom_fac;
            }
            Scale newScale = new Scale();
            newScale.setPivotX(scroll.getX());
            newScale.setPivotY(scroll.getY());
            newScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
            newScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
            mazeDisplayer.getTransforms().add(newScale);
            scroll.consume();
        }
    }
    /** Handler for maximize/restore down Stage Button (the square button right-hand corner)*/
    public void setResizeEvent(Scene scene) {
        mazeDisplayer.widthProperty().bind(pane.widthProperty());
        mazeDisplayer.heightProperty().bind(pane.heightProperty());
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.widthProperty().bind(pane.widthProperty());
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.heightProperty().bind(pane.heightProperty());
        });
    }
    /** MyViewController acts as a "mediator" between PlayerController(where the user's choice of character happens)
     * and MazeDisplayer(the GUI "drawer") therefore, this method simply takes the chosen character (string) from PlayerController
     * and delivers that information (string) to MazeDisplayer*/
    public void setPlayerCharacterAccordingToUserChoice(String chosenCharacterName) throws Exception {
        mazeDisplayer.getUserChoiceOfPlayer(chosenCharacterName);
    }

    /*--------------------- END HANDLERS ------------------------------------------------*/


    /*--------------------- UTILS ------------------------------------------------*/
    public Solution getSolutionFromViewModel() {
        return viewModel.getSolution();
    }
    @Override
    public void displayMaze(int[][] maze) {
        mazeDisplayer.setMazeGridAndRedraw(maze);
        int playerPositionRow = viewModel.getPlayerPosRow();
        int playerPositionColumn = viewModel.getPlayerPosCol();
        if (displayCounter != 1)
            mazeDisplayer.setPlayerPositionAndRedraw(playerPositionRow, playerPositionColumn);
        this.playerPosRow.set(playerPositionRow + "");
        this.playerPosCol.set(playerPositionColumn + "");
        displayCounter++;
    }
    /** Helper function to pop alerts when necessary
     * @param  alertMessage the message(string) to present to User*/
    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }
    public void stopServers() {
        viewModel.stopServers();
    }
    /*--------------------- END UTILS ------------------------------------------------*/


    public void setStageInView(Stage stage) {
        this.stage = stage;
    }
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }
    public void setPane(Pane pane) {
        this.pane = pane;
    }
    //getter
    public Pane getPane() {
        return pane;
    }


}
