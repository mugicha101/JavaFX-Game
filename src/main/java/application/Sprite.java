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
  public double dir; // direction

  public Sprite() {
    this.pos = new Position(0, 0);
    this.image = null;
  }

  public Sprite(String imgPath, double scale) throws FileNotFoundException {
    this();
    setImage(imgPath, scale);
  }

  public Sprite(String imgPath) throws FileNotFoundException {
    this(imgPath, 1);
  }

  public void setImage(String filePath, double scale) throws FileNotFoundException {
    InputStream stream = new FileInputStream("src/main/java/application/images/" + filePath);
    Image rawImage = new Image(stream);
    image = new ImageView(rawImage);
    image.setScaleX(scale);
    image.setScaleY(scale);
    updateImage();
  }

  public Image getImage() {
    return image.getImage();
  }

  private Rectangle2D getBounds() {
    Image snap = getSnapshot();
    return new Rectangle2D(pos.x-snap.getWidth()/2, pos.y-snap.getHeight()/2, snap.getWidth(), snap.getHeight());
  }

  public boolean intersects(Sprite sprite) {
    return this.getBounds().intersects(sprite.getBounds());
  }

  private void updateImage() {
    image.setRotate(dir);
  }

  private Image getSnapshot() {
    updateImage();
    SnapshotParameters sp = new SnapshotParameters();
    sp.setFill(Color.TRANSPARENT);
    return image.snapshot(sp, null);
  }

  public void draw(GraphicsContext gc) {
    Image snap = getSnapshot();
    gc.drawImage(snap, pos.x-snap.getWidth()/2, pos.y-snap.getHeight()/2);
  }
}
