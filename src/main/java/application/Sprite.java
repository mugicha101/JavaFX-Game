package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Sprite {
  private ImageView[] images;
  private double scale; // scale of image
  private int[] offset; // offset in pixels of image
  public Position pos;
  public double dir; // direction
  public int frameDelay;
  public double alpha;

  public Sprite() {
    this.pos = new Position(0, 0);
    this.images = null;
    this.frameDelay = 20;
    this.offset = new int[] {0,0};
    this.alpha = 1;
  }

  public Sprite(String[] imgPaths, int[] offset, int frameDelay, double scale) throws FileNotFoundException {
    this();
    this.offset = offset == null? new int[] {0,0} : offset;
    setImages(imgPaths, frameDelay, scale);
  }

  public Sprite(String[] imgPaths, int[] offset, int frameDelay) throws FileNotFoundException {
    this(imgPaths, offset, frameDelay, 1);
  }

  public void setImages(String[] filePaths, int frameDelay, double scale) throws FileNotFoundException {
    images = new ImageView[filePaths.length];
    this.scale = scale;
    for (int i = 0; i < filePaths.length; i++) {
      InputStream stream = new FileInputStream("src/main/java/application/images/" + filePaths[i]);
      Image rawImage = new Image(stream);
      ImageView image = new ImageView(rawImage);
      image.setScaleX(scale);
      image.setScaleY(scale);
      images[i] = image;
    }
  }

  private ImageView getImage() {
    return images[(int)((double)Game.frame / frameDelay) % images.length];
  }

  private Rectangle2D getBounds() {
    Image snap = getSnapshot();
    return new Rectangle2D(pos.x-snap.getWidth()/2+(double)offset[0]*scale, pos.y-snap.getHeight()/2+(double)offset[1]*scale, snap.getWidth(), snap.getHeight());
  }

  public boolean intersects(Sprite sprite) {
    return this.getBounds().intersects(sprite.getBounds());
  }

  private void updateImage() {
    getImage().setRotate(dir);
  }

  private Image getSnapshot() {
    updateImage();
    SnapshotParameters sp = new SnapshotParameters();
    sp.setFill(Color.TRANSPARENT);
    return getImage().snapshot(sp, null);
  }

  public void draw(GraphicsContext gc) {
    Image snap = getSnapshot();
    gc.save();
    gc.setGlobalAlpha(alpha);
    gc.drawImage(snap, pos.x-snap.getWidth()/2+(double)offset[0]*scale, pos.y-snap.getHeight()/2-(double)offset[1]*scale);
    gc.restore();
  }
}
