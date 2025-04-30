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
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Objects;

public class Interface extends Application {
    @Override
    public void start(Stage stage){

        Group mainGroup = new Group();
        Group inputGroup = new Group();
        Group labelGroup = new Group();
        Group outputGroup = new Group();

        Label errorLabel = new Label(" ");
        errorLabel.setLayoutX(100);
        errorLabel.setLayoutY(525-2);
        errorLabel.setFont(Font.font("System",20));
        labelGroup.getChildren().add(errorLabel);

        ObservableList<String> algo = FXCollections.observableArrayList("LZ77","LZ78");
        ComboBox<String> algoCB = new ComboBox<>(algo);
        algoCB.setLayoutX(10);
        algoCB.setLayoutY(10);
        algoCB.setValue("LZ77");
        algoCB.setMinWidth(75);
        algoCB.setMaxWidth(75);
        inputGroup.getChildren().add(algoCB);

        ObservableList<String> mode = FXCollections.observableArrayList("Encode","Decode");
        ComboBox<String> modeCB = new ComboBox<>(mode);
        modeCB.setLayoutX(100);
        modeCB.setLayoutY(10);
        modeCB.setValue("Encode");
        modeCB.setMinWidth(100);
        modeCB.setMaxWidth(100);
        modeCB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    Backend.newStage.close();
                    Backend.dict=null;
                }catch (Exception e){
                    System.out.println("No dictionary window to close (interface.java)");
                }
            }
        });
        inputGroup.getChildren().add(modeCB);

        Label bufSizeLabel = new Label("Buffer size:");
        bufSizeLabel.setLayoutX(265);
        bufSizeLabel.setLayoutY(10-2);
        bufSizeLabel.setFont(Font.font("System",20));
        labelGroup.getChildren().add(bufSizeLabel);

        TextField buffSizeTF = new TextField("8");
        buffSizeTF.setLayoutX(365);
        buffSizeTF.setLayoutY(10);
        buffSizeTF.setMaxSize(35,25);
        inputGroup.getChildren().add(buffSizeTF);

        Label dictSizeLabel = new Label("Dictionary size:");
        dictSizeLabel.setLayoutX(470);
        dictSizeLabel.setLayoutY(10-2);
        dictSizeLabel.setFont(Font.font("System",20));
        labelGroup.getChildren().add(dictSizeLabel);

        TextField dictSizeTF = new TextField("8");
        dictSizeTF.setLayoutX(610);
        dictSizeTF.setLayoutY(10);
        dictSizeTF.setMaxSize(35,25);
        inputGroup.getChildren().add(dictSizeTF);

        Label inputAreaLabel = new Label("Input text:");
        inputAreaLabel.setLayoutX(10-2);
        inputAreaLabel.setLayoutY(45);
        inputAreaLabel.setFont(Font.font("System",20));
        labelGroup.getChildren().add(inputAreaLabel);

        TextArea inputArea = new TextArea();
        inputArea.setLayoutX(10);
        inputArea.setLayoutY(80);
        inputArea.setMinSize(500,190);
        inputArea.setMaxSize(500,190);
        inputArea.setWrapText(true);
        inputArea.setFont(Font.font("Consolas",20));
        inputGroup.getChildren().add(inputArea);

        Label outputAreaLabel = new Label("Output text:");
        outputAreaLabel.setLayoutX(10-2);
        outputAreaLabel.setLayoutY(285);
        outputAreaLabel.setFont(Font.font("System",20));
        labelGroup.getChildren().add(outputAreaLabel);

        TextArea outputArea = new TextArea();
        outputArea.setLayoutX(10);
        outputArea.setLayoutY(320);
        outputArea.setMinSize(500,190);
        outputArea.setMaxSize(500,190);
        outputArea.setWrapText(true);
        outputArea.setEditable(false);
        outputArea.setFont(Font.font("Consolas",20));
        outputGroup.getChildren().add(outputArea);

        ObservableList<String> numSys = FXCollections.observableArrayList("binary","hex");
        ComboBox<String> numSysCB = new ComboBox<>(numSys);
        numSysCB.setLayoutX(135);
        numSysCB.setLayoutY(285+2);
        numSysCB.setValue("binary");
        numSysCB.setMinWidth(75);
        numSysCB.setMaxWidth(75);
        numSysCB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(Objects.equals(modeCB.getValue(),"Encode")) {
                    if (!Objects.equals("", outputArea.getText())) {
                        if (Objects.equals("binary", numSysCB.getValue())) {
                            outputArea.setText(Backend.result);
                        } else {
                            outputArea.setText("0x" + Backend.binaryToHex(Backend.result));
                        }
                    }
                }
            }
        });
        outputGroup.getChildren().add(numSysCB);

        Button dictShowButton = new Button("Dictionary");
        dictShowButton.setLayoutX(525);
        dictShowButton.setLayoutY(115);
        dictShowButton.setPrefSize(120,15);
        dictShowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!Objects.equals("",inputArea.getText())){
                    try{
                        Backend.showDictionary(modeCB.getValue(),inputArea.getText());
                    }catch (Exception e) {
                        errorLabel.setText("Error! Dictionary input error!");
                    }
                }else{
                    errorLabel.setText("Error! No input text!");
                }
            }
        });
        inputGroup.getChildren().add(dictShowButton);


        Button runButton = new Button("Run program!");
        runButton.setLayoutX(525);
        runButton.setLayoutY(80);
        runButton.setPrefSize(120,15);
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                errorLabel.setText(" ");
                int dictSize;
                int buffSize;
                try {
                    dictSize = Integer.parseInt(dictSizeTF.getText());
                    buffSize = Integer.parseInt(buffSizeTF.getText());
                }catch (Exception e){
                    errorLabel.setText("Error! Incorrect data given!");
                    return;
                }

                if(dictSize<1 || buffSize<1){
                    errorLabel.setText("Error! Dictionary or Buffer size is lower than 1!");
                    return;
                }
                if(dictSize>50 || buffSize>50){
                    errorLabel.setText("Error! Dictionary or Buffer size is more than 50!");
                    return;
                }

                String inputString = inputArea.getText();
                if(inputString==""){
                    errorLabel.setText("Error! No input text!");
                    return;
                }
                inputString=Backend.removeBackslashN(inputString);
                try {
                    Backend.run(algoCB.getValue(), modeCB.getValue(), inputString, dictSize, buffSize);
                }catch (Exception e){
                    errorLabel.setText("Error! Probably dictionary not full.");
                    //e.printStackTrace();
                    System.out.println(e);
                    //Probably dictionary not full
                }

                if(Objects.equals("binary",numSysCB.getValue())){
                    outputArea.setText(Backend.result);
                }else{
                    outputArea.setText("0x"+Backend.binaryToHex(Backend.result));
                }

            }
        });
        inputGroup.getChildren().add(runButton);

        Button logOpenerButton = new Button("Open log.txt");
        logOpenerButton.setLayoutX(10);
        logOpenerButton.setLayoutY(525);
        logOpenerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    Runtime rs = Runtime.getRuntime();
                    rs.exec("notepad log.txt");
                }catch (Exception e){
                    errorLabel.setText("Error! Cannot open log file!");
                    return;
                }
            }
        });
        outputGroup.getChildren().add(logOpenerButton);

        mainGroup.getChildren().addAll(inputGroup, labelGroup, outputGroup);

        Scene scene = new Scene(mainGroup, Color.SNOW);
        stage.setScene(scene);
        stage.setTitle("Lempel-Ziv algorithms");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    public static void show(){
        Application.launch();
    }
}
