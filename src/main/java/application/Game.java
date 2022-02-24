package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;

public class Game extends Application {
  public static final int[] dim = {800, 600};
  public static final int width = 800;
  public static final int height = 600;
  public static Player player;

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    Canvas canvas = new Canvas(dim[0], dim[1]);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e->run(gc)));
    tl.setCycleCount(Timeline.INDEFINITE);
    stage.setScene(new Scene(new StackPane(canvas)));
    stage.show();
    stage.setTitle("Game");
    tl.play();

    // setup game
    InputStream stream = new FileInputStream("../");
    player = new Player(10, 15, );
  }

  private void run(GraphicsContext gc) {
    calc(gc);
    draw(gc);
  }

  private void calc(GraphicsContext gc) {

  }

  private void draw(GraphicsContext gc) {
    drawBG(gc);
  }

  private void drawBG(GraphicsContext gc) {
    // background
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, width, height);

    // setup text
    gc.setFill(Color.WHITE);
    gc.setFont(Font.font(25));

    // render player

  }

  public static void main(String[] args) {
    launch();
  }
}