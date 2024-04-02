package org.example.huffmanencodinggui;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HuffmanTreeVisualizer extends Application {
    private final int APP_WIDTH = 750;
    private Group nodeGroup;

    private double prevMouseX;
    private double prevMouseY;

    public void start(Stage stage) {
        Pane drawField = new Pane();
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

        drawNodeRecursive(APP_WIDTH / 2, 20, APP_WIDTH / 2, 50, root.getWidthNeeded(), root);

        drawField.getChildren().addAll(nodeGroup, hb);
        int APP_HEIGHT = 600;
        Scene scene = new Scene(drawField, APP_WIDTH, APP_HEIGHT);
        stage.setTitle("Huffman Encoding!");
        stage.setScene(scene);
        stage.show();

        btn.setOnAction(event ->
            Platform.runLater(() -> {
                String text = textField.getText();
                nodeGroup.getChildren().clear();
                gen.setSourceText(text);
                TreeNode rootNode = gen.getHuffmanTree();
                drawNodeRecursive(APP_WIDTH / 2, 20, APP_WIDTH / 2, 50, rootNode.getWidthNeeded(), rootNode);
                stage.setScene(scene);

                // CONSOLE LOGGING
                System.out.println("CHARACTER FREQUENCIES:\n" + gen.getCharFrequencyMap());
                System.out.println("\nCHARACTER CODE TABLE:\n" + gen.getCodeTable(rootNode));
            })
        );

        scene.setOnScroll(event -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();

            if (deltaY < 0){
                zoomFactor = 0.95;
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

            shiftTree(deltaX, deltaY);
            prevMouseX = newMouseX;
            prevMouseY = newMouseY;
        });
    }

    public void drawNodeRecursive(int x1,int y1,int x,int y, int width, TreeNode node){
        Line line = new Line(x1, y1, x, y);
        line.setStrokeWidth(2);
        nodeGroup.getChildren().add(line);

        if (node == null) {
            return;
        }

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
            drawNodeRecursive(x,y,x-((width - TreeNode.getNodeSpacing()) / 2),y + 50, node.getLeft().getWidthNeeded(), node.getLeft());
        if(node.getRight() != null)
            drawNodeRecursive(x,y,x+((width - TreeNode.getNodeSpacing()) / 2),y + 50, node.getRight().getWidthNeeded(), node.getRight());
    }

    private void shiftTree(double deltaX, double deltaY) {
        nodeGroup.setTranslateX(nodeGroup.getTranslateX() - deltaX);
        nodeGroup.setTranslateY(nodeGroup.getTranslateY() - deltaY);
    }

    public static void main(String[] args) {
        launch();
    }
}