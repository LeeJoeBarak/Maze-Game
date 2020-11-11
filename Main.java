import Model.MyModel;
import View.PlayerChooserController;
import View.firstSceneController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    public Scene startScene;
    public Scene newGameScene;
    public Scene playScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        MyModel model = new MyModel();
        primaryStage.setTitle("Rick and Morty Maze Game");
        /*#################################################################################################*/
        FXMLLoader firstSceneFXML = new FXMLLoader(getClass().getResource("View/firstScene.fxml"));
        Parent firstAceneRoot = firstSceneFXML.load();
        startScene = new Scene(firstAceneRoot,1200,800);
        firstSceneController firstSceneCont = firstSceneFXML.getController();
        /*#################################################################################################*/
        FXMLLoader playerChooserFXML = new FXMLLoader(getClass().getResource("View/choosePlayer.fxml"));
        Parent secondSceneRoot = playerChooserFXML.load();
        newGameScene = new Scene(secondSceneRoot,900,600);
        PlayerChooserController charController = playerChooserFXML.getController();
        /*#################################################################################################*/
        FXMLLoader myViewFXML = new FXMLLoader(getClass().getResource("View/MyView.fxml"));
        Parent thirdAndLastSceneRoot = myViewFXML.load();
        playScene = new Scene(thirdAndLastSceneRoot,1200,800);
        /*#################################################################################################*/
        firstSceneCont.setPrimaryStage(primaryStage);
        firstSceneCont.setScene(newGameScene);
        charController.setPrimaryStage(firstSceneCont.getPrimaryStage());
        charController.setScene(playScene);
        /*#################################################################################################*/
        setStageCloseEvent(primaryStage, model);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }


    private void setStageCloseEvent(Stage primaryStage, MyModel model) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to migrate to another dimension?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    // Close the program properly
                    model.stopServers();
                    System.exit(0);
                } else { windowEvent.consume(); }
            }
        });
    }

}
