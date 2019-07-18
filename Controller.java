package projMAZE;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Pane pane, mazeGUI;
    @FXML private Label status, clock, explore;
    @FXML private Button genMaze, solveMaze, restart;
    @FXML private CheckBox fails;
    @FXML private TextField width, height, box;
    private Maze maze = new Maze();
    private static volatile boolean solving = false;

    @Override public void initialize(URL location, ResourceBundle resources) {
        maze.generateMaze();
        update();
        explore.setText("EXPLORED 0.00%");

        width.setText(Maze.WIDTH+"");
        height.setText(Maze.HEIGHT+"");
        box.setText(Maze.BOXSIZE+"");

        genMaze.setOnAction(event -> {
            if (!solving) {
                int newWidth = Maze.WIDTH;
                int newHeight = Maze.HEIGHT;
                int newBoxsize = Maze.BOXSIZE;
                try {
                    newWidth = Integer.parseInt(width.getText());
                    if (!inRange(newWidth, 5, 300))
                        return;
                } catch (NumberFormatException e) {
                    System.out.println("width wrong");
                }
                try {
                    newHeight = Integer.parseInt(height.getText());
                    if (!inRange(newHeight, 5, 300))
                        return;
                } catch (NumberFormatException e) {
                    System.out.println("height wrong");
                }
                try {
                    newBoxsize = Integer.parseInt(box.getText());
                    if (!inRange(newBoxsize, 3, 100))
                        return;
                } catch (NumberFormatException e) {
                    System.out.println("box size");
                }
                if (Maze.WIDTH != newWidth || Maze.HEIGHT != newHeight || Maze.BOXSIZE != newBoxsize) {
                    Maze.WIDTH = newWidth;
                    Maze.HEIGHT = newHeight;
                    Maze.BOXSIZE = newBoxsize;
                    maze = new Maze();
                }
                runGenerator();
            }
        });
        solveMaze.setOnAction(event -> {
            if(!solving)
                runSolver();
        });
        restart.setOnAction(event -> {
            if (!solving) {
                status.setText("STATUS: NOT STARTED");
                clock.setText("SOLVE TIME ---");
                explore.setText("EXPLORED 0.00%");
                maze.clearVisits();
                update();
            }
        });

    private void runGenerator() {
        status.setText("STATUS: GENERATING");
        clock.setText("SOLVE TIME ---");
        solving = true;
        Thread t = new Thread(() -> {
            Runnable run = () -> {
                maze.generateMaze();
                update();
                explore.setText("EXPLORED 0.00%");
                status.setText("STATUS: NOT STARTED");
                solving = false;
            };
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(run);
        });
        t.setDaemon(true);
        t.start();
    }
    private void runSolver() {
        status.setText("STATUS: IN PROGRESS");
        clock.setText("SOLVE TIME ---");
        solving = true;
        long startTime = System.currentTimeMillis() + 500;
        Thread t = new Thread(() -> {
            Runnable run = () -> {
                maze.solveMaze();
                update();
                explore.setText(String.format("EXPLORED %.02f%%",maze.percentExplored()));
                status.setText("STATUS: SOLVED");
                long elapsedTime = System.currentTimeMillis() - startTime;
                clock.setText("SOLVE TIME "+ elapsedTime/1000. + "s");
                solving = false;
            };
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(run);
        });
        t.setDaemon(true);
        t.start();
    }

    boolean inRange(int num, int min, int max) {
        return num >= min && num <= max;
    }
    void drawLine(int x1, int y1, int x2, int y2) {
        Line line = new Line(x1*Maze.BOXSIZE,y1*Maze.BOXSIZE,x2*Maze.BOXSIZE,y2*Maze.BOXSIZE);
        line.setStrokeWidth(Maze.BOXSIZE/9.);
        mazeGUI.getChildren().add(line);
    }
    Label box(int x, int y, String color) {
        Label label = new Label();
        label.setMinSize(Maze.BOXSIZE, Maze.BOXSIZE);
        label.setPrefSize(Maze.BOXSIZE, Maze.BOXSIZE);
        label.setMaxSize(Maze.BOXSIZE, Maze.BOXSIZE);
        label.setLayoutX(x*Maze.BOXSIZE);
        label.setLayoutY(y*Maze.BOXSIZE);
        label.setStyle("-fx-background-color: "+color);
        return label;
    }
    Label path(int x, int y) {
        return box(x,y,"#32ff7e");
    }
    Label tried(int x, int y) {
        return box(x,y,"#ff4d4d");
    }
    void update() {
        mazeGUI.setTranslateX(7);
        mazeGUI.setTranslateY(7);
        mazeGUI.setPrefSize(Maze.WIDTH*Maze.BOXSIZE + 10,Maze.HEIGHT*Maze.BOXSIZE + 10);
        mazeGUI.getChildren().clear();
        Box[][] array = maze.getMaze();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {

                Box b = array[i][j];

                if (b.isOnPath())
                    mazeGUI.getChildren().add(path(i,j));
                else if (fails.isSelected() && b.isVisited())
                    mazeGUI.getChildren().add(tried(i,j));

                if (b.isUp())
                    drawLine(i,j,i+1,j);
                if (b.isLeft())
                    drawLine(i,j,i,j+1);
                if (i == array.length - 1 && b.isRight())
                    drawLine(i+1,j,i+1,j+1);
                if (j == array[0].length - 1 && b.isDown())
                    drawLine(i,j+1,i+1,j+1);
            }
        }

    }
}
