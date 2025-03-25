import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Interface extends Application {


    @Override
    public void start(Stage stage){

        Group mainGroup = new Group();
        Group inputGroup = new Group();
        Group labelGroup = new Group();
        Group outputGroup = new Group();

        ObservableList<String> algo = FXCollections.observableArrayList("LZ77");
        ComboBox<String> algoCB = new ComboBox<>(algo);
        algoCB.setLayoutX(10);
        algoCB.setLayoutY(10);
        algoCB.setValue("LZ77");
        algoCB.setMinWidth(75);
        algoCB.setMaxWidth(75);
        inputGroup.getChildren().add(algoCB);

        ObservableList<String> mode = FXCollections.observableArrayList("Encode");
        ComboBox<String> modeCB = new ComboBox<>(mode);
        modeCB.setLayoutX(100);
        modeCB.setLayoutY(10);
        modeCB.setValue("Encode");
        modeCB.setMinWidth(100);
        modeCB.setMaxWidth(100);
        inputGroup.getChildren().add(modeCB);

        Label bufSizeLabel = new Label("Buffer size:");
        bufSizeLabel.setLayoutX(225);
        bufSizeLabel.setLayoutY(10+2);
        bufSizeLabel.setFont(Font.font("Consolas",20));
        labelGroup.getChildren().add(bufSizeLabel);

        TextField bufSizeTF = new TextField("8");
        bufSizeTF.setLayoutX(365);
        bufSizeTF.setLayoutY(10);
        bufSizeTF.setMaxSize(35,25);
        inputGroup.getChildren().add(bufSizeTF);

        Label dicSizeLabel = new Label("Dictionary size:");
        dicSizeLabel.setLayoutX(425);
        dicSizeLabel.setLayoutY(10+2);
        dicSizeLabel.setFont(Font.font("Consolas",20));
        labelGroup.getChildren().add(dicSizeLabel);

        TextField dicSizeTF = new TextField("8");
        dicSizeTF.setLayoutX(610);
        dicSizeTF.setLayoutY(10);
        dicSizeTF.setMaxSize(35,25);
        inputGroup.getChildren().add(dicSizeTF);

        Label inputAreaLabel = new Label("Dictionary size:");
        inputAreaLabel.setLayoutX(10);
        inputAreaLabel.setLayoutY(45+2);
        inputAreaLabel.setFont(Font.font("Consolas",20));
        labelGroup.getChildren().add(inputAreaLabel);

        TextArea inputArea = new TextArea();
        inputArea.setLayoutX(10);
        inputArea.setLayoutY(80);
        inputArea.maxWidth(645);
        inputArea.minWidth(645);
        inputArea.maxHeight(225);
        inputArea.minHeight(225);
        inputGroup.getChildren().add(inputArea);

        mainGroup.getChildren().addAll(inputGroup, labelGroup, outputGroup);

        Scene scene = new Scene(mainGroup, Color.SNOW);
        stage.setScene(scene);
        stage.setTitle("Lempel-Ziv algorithm");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    public static void show(){
        Application.launch();
    }
}
