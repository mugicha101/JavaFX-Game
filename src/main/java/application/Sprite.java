package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Sprite {
  private final String[] imgPaths;
  private final ImageView iv;
  private Image[] imgs;
  private double scale; // scale of image
  private final int[] offset; // offset in pixels of image
  public Position pos; // dont make final
  public double dir; // direction
  public final int frameDelay;
  public double alpha;
  private final Group sceneGroup;

  public Sprite(Group sceneGroup, String[] imgPaths, int[] offset, int frameDelay, double scale)
      throws FileNotFoundException {
    this.sceneGroup = sceneGroup;
    iv = new ImageView();
    sceneGroup.getChildren().add(iv);
    this.pos = new Position(0, 0);
    this.imgs = null;
    this.frameDelay = frameDelay;
    this.alpha = 1;
    this.imgPaths = imgPaths;
    this.offset = offset == null ? new int[] {0, 0} : offset;
    setImages(imgPaths, frameDelay, scale);
  }

  public void setImages(String[] filePaths, int frameDelay, double scale)
      throws FileNotFoundException {
    imgs = new Image[filePaths.length];
    this.scale = scale;
    for (int i = 0; i < filePaths.length; i++) {
      InputStream stream = new FileInputStream("src/main/java/application/images/" + filePaths[i]);
      Image image = new Image(stream);
      imgs[i] = image;
    }
  }

  private Image getImage() {
    return imgs[(int) ((double) Game.frame / frameDelay) % imgs.length];
  }

  public void drawUpdate() {
    Image img = getImage();
    if (iv.getImage() != img) iv.setImage(img);
    double x = pos.x - img.getWidth() / 2 + offset[0] * scale;
    double y = pos.y - img.getHeight() / 2 + offset[1] * scale;
    if (alpha != iv.getOpacity())
      iv.setOpacity(alpha);
    if (iv.getX() != x) iv.setX(x);
    if (iv.getY() != y) iv.setY(y);
    if (iv.getScaleX() != scale) {
      iv.setScaleX(scale);
      iv.setScaleY(scale);
    }
    if (iv.getRotate() != dir) iv.setRotate(dir);
  }

  public void delete() {
    sceneGroup.getChildren().remove(iv);
  }

  public Sprite clone() {
    try {
      return new Sprite(sceneGroup, imgPaths, offset, frameDelay, scale);
    } catch (FileNotFoundException e) {
      throw new RuntimeException();
    }
  }
}
