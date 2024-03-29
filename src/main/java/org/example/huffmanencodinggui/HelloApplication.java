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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private final int APP_WIDTH = 750;
    private final int APP_HEIGHT = 600;
    private Pane drawField;
    private Group nodeGroup;

    private double prevMouseX;
    private double prevMouseY;
    double zoomFactor = 1;

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

        drawNodeRecursive(200,200,500,200, APP_WIDTH / 2, root);
        drawField.getChildren().addAll(nodeGroup, hb);
        Scene scene = new Scene(drawField, APP_WIDTH, APP_HEIGHT);
        stage.setTitle("Huffman Encoding!");
        stage.setScene(scene);
        stage.show();

        btn.setOnAction(event -> {
            Platform.runLater(() -> {
                String text = textField.getText();
                nodeGroup.getChildren().clear();
                gen.setSourceText(text);
                TreeNode rootNode = gen.getHuffmanTree();
                int maxDepth = maxDepth(rootNode);
                int maxTreeWidth = (2 << (maxDepth - 1)) * maxDepth * 5;
                drawNodeRecursive(APP_WIDTH / 2, 50, APP_WIDTH / 2, 50, maxTreeWidth, rootNode);
                stage.setScene(scene);
            });
        });

        scene.setOnScroll(event -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }
            nodeGroup.setScaleX(nodeGroup.getScaleX() * zoomFactor);
            nodeGroup.setScaleY(nodeGroup.getScaleY() * zoomFactor);
        });

        scene.setOnMousePressed(event -> {
            prevMouseX = event.getScreenX();
            prevMouseY = event.getScreenY();
        });

        scene.setOnMouseDragged(event -> {
            double newMouseX = event.getScreenX();
            double newMouseY = event.getScreenY();

            double deltaX = prevMouseX - newMouseX;
            double deltaY = prevMouseY - newMouseY;

            nodeGroup.setTranslateX(nodeGroup.getTranslateX() - deltaX);
            nodeGroup.setTranslateY(nodeGroup.getTranslateY() - deltaY);

            prevMouseX = newMouseX;
            prevMouseY = newMouseY;
        });
    }

    public void drawNodeRecursive(int x1,int y1,int x,int y, int width, TreeNode node){
        Line line = new Line(x1, y1, x, y);
        line.setStrokeWidth(2);
        nodeGroup.getChildren().add(line);
        Text txt;
        if (node.isLeaf()) {
            txt = new Text(x, y, "'" + node.getCharacter() + "'" + "\n" + node.getCodeValue());
            Circle circle = new Circle(x, y, txt.getLayoutBounds().getWidth(), Paint.valueOf("lightgrey"));
            nodeGroup.getChildren().add(circle);
        } else {
            txt = new Text(x, y + 15, node.getCodeValue());
        }
        txt.setStyle("-fx-font-weight: bold");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setTranslateX(txt.getTranslateX() - (txt.getLayoutBounds().getWidth() / 2));
        nodeGroup.getChildren().add(txt);
        if(node.getLeft() != null)
            drawNodeRecursive(x,y,x-(width / 2),y+50, width / 2, node.getLeft());
        if(node.getRight() != null)
            drawNodeRecursive(x,y,x+(width / 2),y+50, width / 2, node.getRight());
    }

    int maxDepth(TreeNode node)
    {
        if (node == null)
            return 0;
        else {
            /* compute the depth of each subtree */
            int lDepth = maxDepth(node.getLeft());
            int rDepth = maxDepth(node.getRight());

            /* use the larger one */
            if (lDepth > rDepth)
                return (lDepth + 1);
            else
                return (rDepth + 1);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}