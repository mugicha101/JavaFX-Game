package application;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.ParallelCamera;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Sprite {
  private ImageView image;
  public Position pos;
  public Rectangle2D bounds; // hit box
  public double dir; // direction

  public Sprite() {
    this.pos = new Position(0, 0);
    this.bounds = new Rectangle2D(0,0,0,0);
    this.image = null;
  }

  public Sprite(String imgPath) throws FileNotFoundException {
    this();
    setImage(imgPath);
  }

  public void setImage(String filePath) throws FileNotFoundException {
    InputStream stream = new FileInputStream("src/main/java/application/images/" + filePath);
    Image rawImage = new Image(stream);
    image = new ImageView(rawImage);
    image.setScaleX(1);
    image.setScaleY(1);
    updateImage();
  }

  public Image getImage() {
    return image.getImage();
  }

  public boolean intersects(Sprite sprite) {
    return this.bounds.intersects(sprite.bounds);
  }

  private void updateImage() {
    image.setRotate(dir);
  }

  public void draw(GraphicsContext gc) {
    SnapshotParameters sp = new SnapshotParameters();
    sp.setFill(Color.TRANSPARENT);
    updateImage();
    Image snap = image.snapshot(sp, null);
    gc.drawImage(snap, -snap.getWidth()/2, -snap.getHeight()/2);
  }
}
