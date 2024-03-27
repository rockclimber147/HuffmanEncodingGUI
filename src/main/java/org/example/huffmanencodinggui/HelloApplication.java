package org.example.huffmanencodinggui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private final int APP_WIDTH = 1000;
    private Pane drawField;
    private Group nodeGroup;

    public void start(Stage stage) {
        drawField = new Pane();
        nodeGroup = new Group();

        Button btn = new Button();
        btn.setText("Generate");
        btn.setMinWidth(100);
        TextField textField = new TextField ();
        textField.setText("ANNA AND DANNY");
        textField.setMinWidth(APP_WIDTH - btn.getMinWidth());
        HBox hb = new HBox();
        hb.getChildren().addAll(textField, btn);
        hb.setSpacing(0);
        HuffmanTreeGenerator gen = new HuffmanTreeGenerator("ANNA AND DANNY");
        TreeNode root = gen.getHuffmanTree();





        drawNodeRecursive(500,200,500,200, APP_WIDTH / 2, root);
        drawField.getChildren().addAll(nodeGroup, hb);
        Scene scene = new Scene(drawField, APP_WIDTH, APP_WIDTH);
        stage.setTitle("Huffman Encoding!");
        stage.setScene(scene);
        stage.show();

        btn.setOnAction(event -> {
            Platform.runLater(() -> {
                String text = textField.getText();
                nodeGroup.getChildren().clear();
                gen.setSourceText(text);
                drawNodeRecursive(APP_WIDTH/2, 50, APP_WIDTH / 2, 50, APP_WIDTH / 2, gen.getHuffmanTree());
                stage.setScene(scene);
            });
        });
    }

    public void drawNodeRecursive(int x1,int y1,int x,int y, int width, TreeNode node){
        Line line = new Line(x1, y1, x, y);
        nodeGroup.getChildren().add(line);
        Text txt;
        if (node.isLeaf()) {
            Circle circle = new Circle(x, y, 15, Paint.valueOf("lightblue"));
            nodeGroup.getChildren().add(circle);
            txt = new Text(x - 3, y, "'" + node.getCharacter() + "'" + "\n" + node.getCodeValue());

        } else {
            txt = new Text(x - 3, y + 15, node.getCodeValue());
        }
        nodeGroup.getChildren().add(txt);
        if(node.getLeft() != null)
            drawNodeRecursive(x,y,x-(width / 2),y+50, width / 2, node.getLeft());
        if(node.getRight() != null)
            drawNodeRecursive(x,y,x+(width / 2),y+50, width / 2, node.getRight());
    }

    public static void main(String[] args) {
        launch();
    }
}