import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class Backend extends Interface {
    public static String result;
    public static String[] dict;
    public static Stage newStage;//kludge xD

    public static int rng(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public static void run(String algo, String mode, String input, int dictSize, int buffSize){
        Log.initialEntry();
        Log.writeLog("Input: "+input+"\n",true);
        Log.writeLog("Algorithm: "+algo+"\nMode: "+mode+"\n",true);
        Log.writeLog("Dictionary size: "+dictSize+"\t Buffer size: "+buffSize,true);

        if(Objects.equals(algo,"LZ77")) {
            if(Objects.equals(mode,"Encode")) {
                LZ77 lz77 = new LZ77("", "", 0, 0, "", "");
                result = lz77.encodeLZ77(input, dictSize, buffSize);
                Log.writeLog("ANSWER:\n"+"bin: "+result+"\nhex: 0x"+binaryToHex(result),true);
            }else{
                if(Backend.dict==null){
                    Backend.showDictionary(mode,input);
                }else{
                    System.out.println(Arrays.toString(Backend.dict));
                    LZ77 lz77 = new LZ77("", "", 0, 0, "", "");
                    result = lz77.decodeLZ77(input, dictSize, buffSize);
                }
            }
            return;
        }
        if(Objects.equals(algo,"LZ78")){
            if(Objects.equals(mode,"Encode")) {
                LZ78 lz78 = new LZ78(new String[dictSize], "", "", 0, "", "");
                result = lz78.encodeLZ78(input, dictSize, buffSize);
                Log.writeLog("ANSWER:\n"+"bin: "+result+"\nhex: 0x"+binaryToHex(result),true);
            }else{
                if(Backend.dict==null){
                    Backend.showDictionary(mode,input);
                }else{
                    System.out.println(Arrays.toString(Backend.dict));
                    LZ78 lz78 = new LZ78(new String[dictSize], "", "", 0, "", "");
                    result = lz78.decodeLZ78(input, dictSize, buffSize);
                }
            }
            return;
        }
    }

    public static void showDictionary(String mode, String input) {//rework that func
        try {
            newStage.close();
        } catch (Exception e) {
            System.out.println("No dictionary window to close (Backend.java)");
        }
        newStage = new Stage();
        Group root = new Group();
        newStage.setTitle("Dictionary");
        newStage.setWidth(400);
        newStage.setHeight(400);

        final int[] lines = {3};
        GridPane gridPane = new GridPane();
        ColumnConstraints column0 = new ColumnConstraints(25);//index
        ColumnConstraints column1 = new ColumnConstraints(40);//"letter"
        ColumnConstraints column2 = new ColumnConstraints(40);//letterTF
        ColumnConstraints column3 = new ColumnConstraints(40);//"code"
        ColumnConstraints column4 = new ColumnConstraints(200);//codeTF
        column4.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(column0);
        gridPane.getColumnConstraints().add(column1);
        gridPane.getColumnConstraints().add(column2);
        gridPane.getColumnConstraints().add(column3);
        gridPane.getColumnConstraints().add(column4);
        gridPane.setGridLinesVisible(false);
        TextField[] letterTF = new TextField[1000];
        TextField[] codeTF = new TextField[1000];
        //those arrays size must be dynamically changed, but I don't care XD

        Label errorLabel = new Label(" ");
        errorLabel.setLayoutX(75 - 3);
        errorLabel.setLayoutY(330 + 3);
        root.getChildren().add(errorLabel);

        if(Objects.equals(mode,"Encode")) {
            Backend.dict=null;
            Backend.dict = Backend.createDict(input);
            lines[0] = (Backend.dict).length;

            int symbsBits=(int)(Math.ceil(Math.log(dict.length)/Math.log(2)));
            for (int i = 0; i < lines[0]; i++) {
                letterTF[i] = new TextField(Backend.dict[i]);
                letterTF[i].setEditable(false);
                codeTF[i] = new TextField(Backend.addLeadingZeros(Integer.toBinaryString(i),symbsBits));
                codeTF[i].setEditable(false);
                gridPane.add(new Label((i + 1) + ")"), 0, i);
                gridPane.add(new Label("Letter: "), 1, i);
                gridPane.add(letterTF[i], 2, i);
                gridPane.add(new Label(" Code: "), 3, i);
                gridPane.add(codeTF[i], 4, i);
            }
        }else{
            if (Backend.dict == null) {
                for (int i = 0; i < lines[0]; i++) {
                    letterTF[i] = new TextField();
                    if (i == 0) codeTF[i] = new TextField("0");
                    else codeTF[i] = new TextField();
                    gridPane.add(new Label((i + 1) + ")"), 0, i);
                    gridPane.add(new Label("Letter: "), 1, i);
                    gridPane.add(letterTF[i], 2, i);
                    gridPane.add(new Label(" Code: "), 3, i);
                    gridPane.add(codeTF[i], 4, i);
                }
            } else {
                lines[0] = (Backend.dict).length;
                for (int i = 0; i < lines[0]; i++) {
                    letterTF[i] = new TextField(Backend.dict[i]);
                    codeTF[i] = new TextField(Integer.toBinaryString(i));
                    gridPane.add(new Label((i + 1) + ")"), 0, i);
                    gridPane.add(new Label("Letter: "), 1, i);
                    gridPane.add(letterTF[i], 2, i);
                    gridPane.add(new Label(" Code: "), 3, i);
                    gridPane.add(codeTF[i], 4, i);
                }
            }

            Button addLineButton = new Button("+");
            addLineButton.setLayoutX(350);
            addLineButton.setLayoutY(10-2);
            addLineButton.setPrefSize(25, 15);
            addLineButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        errorLabel.setText(" ");
                        letterTF[lines[0]] = new TextField();
                        codeTF[lines[0]] = new TextField();
                        gridPane.add(new Label((lines[0] + 1) + ")"), 0, lines[0]);
                        gridPane.add(new Label("Letter: "), 1, lines[0]);
                        gridPane.add(letterTF[lines[0]], 2, lines[0]);
                        gridPane.add(new Label(" Code: "), 3, lines[0]);
                        gridPane.add(codeTF[lines[0]], 4, lines[0]);
                        lines[0] += 1;
                    } catch (Exception e) {
                        errorLabel.setText("Error! Cannot add more lines");
                    }
                }
            });
            root.getChildren().add(addLineButton);

            Button enterButton = new Button("Enter!");
            enterButton.setLayoutX(10);
            enterButton.setLayoutY(330);
            enterButton.setPrefSize(50, 15);
            enterButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    errorLabel.setText("Dictionary written. Window can be closed");
                    int k = 0;
                    while (true) {
                        try {
                            if (Objects.equals(codeTF[k].getText(), "")) {
                                throw new Exception();
                            } else k += 1;
                        } catch (Exception e) {
                            break;
                        }
                    }
                    dict = new String[k];
                    for (int i = 0; i < k; i++) {
                        try {
                            if (Integer.parseInt(codeTF[i].getText(), 2) != 0) {
                                dict[Integer.parseInt(codeTF[i].getText(), 2)]=(letterTF[i].getText()).substring(0, 1);
                            } else dict[Integer.parseInt(codeTF[i].getText(), 2)] = (letterTF[i].getText());
                            //this if-else made to pass empty symbol to dictionary
                        } catch (Exception e) {
                            errorLabel.setText("Error! Wrong code at " + (i + 1) + " pos");
                            System.out.println(e);
                            break;
                        }
                    }
                }
            });
            root.getChildren().add(enterButton);

            Button closeButton = new Button("Close");
            closeButton.setLayoutX(300);
            closeButton.setLayoutY(330);
            closeButton.setPrefSize(75, 15);
            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    newStage.close();
                }
            });
            root.getChildren().add(closeButton);
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setLayoutX(10);
        scrollPane.setLayoutY(35);
        scrollPane.setPrefViewportHeight(275);
        scrollPane.setPrefViewportWidth(350);
        root.getChildren().add(scrollPane);

        Label emptySymbWarning = new Label("Note: empty symbol must be encoded as \"0\"");
        emptySymbWarning.setLayoutX(10);
        emptySymbWarning.setLayoutY(10);
        emptySymbWarning.setFont(Font.font("System",16));
        root.getChildren().add(emptySymbWarning);

        Scene newScene = new Scene(root, Color.SNOW);
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();
    }

    public static String[] createDict(String string){
        //create string without repeated symbols
        //create array with length as that string
        //insert symbols in
        //sort array
        //index of each symbols in binary equals to it "code"
        String individual=string.substring(0,1);
        for(int i=0;i<string.length();i++){
            int k=0;
            for(int n=0;n<individual.length();n++) {
                if (!Objects.equals(string.substring(i, i + 1), individual.substring(n, n + 1))) {
                    k += 1;
                }
            }
            if(k==individual.length()){
                individual+=string.substring(i,i+1);
            }
        }
        String[] dict = new String[individual.length()+1];
        for(int i=0;i<dict.length-1;i++){
            dict[i]=individual.substring(i,i+1);
        }
        dict[dict.length-1]="";//null symbol will be encoded
        Arrays.sort(dict);
        return dict;
    }

    public static String addLeadingZeros(String num,int finalLen){
        String zeros="";
        for(int i=0;i<finalLen-(num).length();i++){
            zeros+="0";
        }
        return (zeros+num);
    }

    public static String binaryToHex(String binary){
        String ans="";
        String zeros="0000";
        for(int i=0;i<binary.length()/4;i++){
            if(i!=binary.length()/4-1){
                ans+=Integer.toHexString(Integer.parseInt(binary.substring(i*4,(i+1)*4),2));
            }else{
                ans+=Integer.toHexString(Integer.parseInt(binary.substring(i*4,(i+1)*4),2));
                String temp=binary.substring((i+1)*4);
                if (temp.length()!=0) {
                    ans += Integer.toHexString(Integer.parseInt(temp + zeros.substring(temp.length()),2));
                }
            }
            //System.out.println(i+") "+ans);
        }
        return ans;
    }

    public static String hexToBinary(String input){
        //check if input is already binary
        int k=0;
        for(int i=0;i<input.length();i++){
            if(Objects.equals("0",input.substring(i,i+1)) || Objects.equals("1",input.substring(i,i+1))){
                k+=1;
            }
        }
        if(k==input.length()) return null;

        String result="";

        try{
            input=input.split("0x")[1];
        }catch (Exception e){
            ;
        }

        for(int i=0;i<input.length();i++){
            result+=addLeadingZeros(Integer.toBinaryString(Integer.parseInt(input.substring(i,i+1),16)),4);
        }

        return result;
    }

    public static String removeBackslashN(String input){
        String result="";
        String[] splitted={null};
        try {
            splitted = input.split(Pattern.quote("\n"));
        }catch (Exception e){
            return input;
        }
        for(int i=0;i<splitted.length;i++){
            if(i==splitted.length-1) result+=splitted[i];
            else result+=splitted[i]+" ";
        }

        return result;
    }
}