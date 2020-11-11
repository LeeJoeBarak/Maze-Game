package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayerChooserController {
    public MyViewController viewController;
    public Stage primaryStage;
    public Scene scene;

    /**sets the Stage memeber of PlayerController (that is the Stage to use when invoking the PlayerController Start() method) */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    /**sets the Scene memeber of PlayerController (that is the Scene to use when invoking the PlayerController Start() method) */
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    /**
     * This is another start() method but it doeasn't override Application's start() method
     * --> associates MyModel - ViewModel - MyViewModel & presents the MyView.fxml scene in the primaryStage(->passed through setter)
     **/
    public void start_playerChooserController() throws Exception {
        //ViewModel -> Model
        MyModel model = new MyModel();
        model.startServers();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);

        //Loading Main Windows
        FXMLLoader ViewFXML = new FXMLLoader(getClass().getResource("/View/MyView.fxml"));
        Parent myViewRoot = ViewFXML.load();
        /*get instance of MyViewController from MyView.fxml */
        viewController = ViewFXML.getController();
        // view.initialize(viewModel,primaryStage,scene);
        /*set the primaryStage of MyViewController to this.primaryStage*/
        viewController.setStageInView(primaryStage);
        scene = new Scene(myViewRoot,1200,800);
        primaryStage.setScene(scene);
        /*handle resizing of the window by binding scene.widthProperty & scene.heightProperty
        to pane.widthProperty/pane.HeightProperty*/
        viewController.setResizeEvent(scene);
        viewController.setViewModel(viewModel);
        // viewModel.addObserver(view);
        viewModel.addObserver(viewController);
        //Show the Main Window
        primaryStage.show();
    }

    /**Handler for when "rick" button was clicked */
    public void rick() throws Exception {
        this.start_playerChooserController();
            viewController.setPlayerCharacterAccordingToUserChoice("rick");
    }
    /**Handler for when "morty" button was clicked */
    public void morty() throws Exception {
        this.start_playerChooserController();
        viewController.setPlayerCharacterAccordingToUserChoice("morty");
    }
    /**Handler for when "summer" button was clicked */
    public void summer() throws Exception {
        this.start_playerChooserController();
        viewController.setPlayerCharacterAccordingToUserChoice("summer");
    }
    /**Handler for when "jerry" button was clicked */
    public void jerry() throws Exception {
        this.start_playerChooserController();
        viewController.setPlayerCharacterAccordingToUserChoice("jerry");
    }
    /**Handler for when "beth" button was clicked */
    public void beth() throws Exception {
        this.start_playerChooserController();
        viewController.setPlayerCharacterAccordingToUserChoice("beth");
    }
    /**Handler for when "poopybutt" button was clicked */
    public void poopybutt() throws Exception {
        this.start_playerChooserController();
        viewController.setPlayerCharacterAccordingToUserChoice("poopybutt");
    }

}
