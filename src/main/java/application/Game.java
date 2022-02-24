package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Game extends Application {
  public static final int[] dim = {800, 600};
  public static final int width = 800;
  public static final int height = 600;
  public static Player player;
  public static ParallelCamera cam;
  public static GraphicsContext gc;
  public static int frame = 0;

  public void start(Stage stage) throws IOException {
    // setup JavaFX
    Canvas canvas = new Canvas(dim[0], dim[1]);
    gc = canvas.getGraphicsContext2D();
    cam = new ParallelCamera();
    Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e->run()));
    tl.setCycleCount(Timeline.INDEFINITE);
    stage.setScene(new Scene(new StackPane(canvas)));
    stage.show();
    stage.setTitle("Game");
    tl.play();

    // setup input
    stage.getScene().setOnKeyPressed(e->Input.keyRequest(e.getCode(), true));
    stage.getScene().setOnKeyReleased(e->Input.keyRequest(e.getCode(), false));

    // setup game
    player = new Player(10, 15, new Sprite("Reimu1.png"));
  }

  private void run() {
    Input.keyTick();
    calc();
    draw();
    frame++;
  }

  private void calc() {
    movePlayer();
  }

  private void draw() {
    drawBG();
  }

  private void movePlayer() {

  }

  private void drawBG() {
    // background
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, width, height);

    // setup text
    gc.setFill(Color.WHITE);
    gc.setFont(Font.font(25));

    // render player
    player.dir = frame;
    player.draw(gc);
  }

  public static void main(String[] args) {
    launch();
  }
}