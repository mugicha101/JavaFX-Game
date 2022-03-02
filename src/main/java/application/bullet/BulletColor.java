package application.bullet;

import javafx.scene.paint.Color;

public class BulletColor {
  // base colors
  public static final BulletColor RED = new BulletColor(Color.color(1, 1, 1), Color.color(1, 0, 0));
  public static final BulletColor ORANGE = new BulletColor(Color.color(1, 1, 1), Color.color(1, 0.6, 0));
  public static final BulletColor YELLOW = new BulletColor(Color.color(1, 1, 1), Color.color(1, 1, 0));
  public static final BulletColor GREEN = new BulletColor(Color.color(1, 1, 1), Color.color(0, 1, 0));
  public static final BulletColor TURQUOISE = new BulletColor(Color.color(1, 1, 1), Color.color(0, 1, 0.7));
  public static final BulletColor CYAN = new BulletColor(Color.color(1, 1, 1), Color.color(0, 1, 1));
  public static final BulletColor BLUE = new BulletColor(Color.color(1, 1, 1), Color.color(0, 0, 1));
  public static final BulletColor PURPLE = new BulletColor(Color.color(1, 1, 1), Color.color(0.7, 0, 1));
  public static final BulletColor MAGENTA = new BulletColor(Color.color(1, 1, 1), Color.color(1, 0, 1));
  public static final BulletColor ROSE = new BulletColor(Color.color(1, 1, 1), Color.color(1, 0, 0.5));

  // inverse colors
  public static final BulletColor INVERSE_RED = new BulletColor(Color.color(0.5, 0, 0), Color.color(1, 0, 0));
  public static final BulletColor INVERSE_ORANGE = new BulletColor(Color.color(0.5, 0.1, 0), Color.color(1, 0.6, 0));
  public static final BulletColor INVERSE_YELLOW = new BulletColor(Color.color(0.5, 0.3, 0), Color.color(1, 1, 0));
  public static final BulletColor INVERSE_GREEN = new BulletColor(Color.color(0, 0.4, 0), Color.color(0, 1, 0));
  public static final BulletColor INVERSE_TURQUOISE = new BulletColor(Color.color(0, 0.4, 0.2), Color.color(0, 1, 0.7));
  public static final BulletColor INVERSE_CYAN = new BulletColor(Color.color(0, 0.3, 0.5), Color.color(0, 1, 1));
  public static final BulletColor INVERSE_BLUE = new BulletColor(Color.color(0, 0, 0.5), Color.color(0, 0, 1));
  public static final BulletColor INVERSE_PURPLE = new BulletColor(Color.color(0.35, 0, 0.45), Color.color(0.7, 0, 1));
  public static final BulletColor INVERSE_MAGENTA = new BulletColor(Color.color(0.45, 0, 0.35), Color.color(1, 0, 1));
  public static final BulletColor INVERSE_ROSE = new BulletColor(Color.color(0.5, 0, 0.1), Color.color(1, 0, 0.5));

  // dark colors
  public static final BulletColor DARK_RED = new BulletColor(Color.color(1, 0, 0), Color.color(0.5, 0, 0));
  public static final BulletColor DARK_ORANGE = new BulletColor(Color.color(1, 0.6, 0), Color.color(0.5, 0.1, 0));
  public static final BulletColor DARK_YELLOW = new BulletColor(Color.color(1, 1, 0), Color.color(0.5, 0.3, 0));
  public static final BulletColor DARK_GREEN = new BulletColor(Color.color(0, 1, 0), Color.color(0, 0.4, 0));
  public static final BulletColor DARK_TURQUOISE = new BulletColor(Color.color(0, 1, 0.7), Color.color(0, 0.4, 0.2));
  public static final BulletColor DARK_CYAN = new BulletColor(Color.color(0, 1, 1), Color.color(0, 0.3, 0.5));
  public static final BulletColor DARK_BLUE = new BulletColor(Color.color(0, 0, 1), Color.color(0, 0, 0.5));
  public static final BulletColor DARK_PURPLE = new BulletColor(Color.color(0.7, 0, 1), Color.color(0.35, 0, 0.45));
  public static final BulletColor DARK_MAGENTA = new BulletColor(Color.color(1, 0, 1), Color.color(0.45, 0, 0.35));
  public static final BulletColor DARK_ROSE = new BulletColor(Color.color(1, 0, 0.5), Color.color(0.5, 0, 0.1));

  public final Color innerColor;
  public final Color outerColor;
  public BulletColor(Color innerColor, Color outerColor) {
    this.innerColor = innerColor;
    this.outerColor = outerColor;
  }

  public BulletColor(double[] innerColor, double[] outerColor) {
    this(Color.color(innerColor[0], innerColor[1], innerColor[2]), Color.color(outerColor[0], outerColor[1], outerColor[2]));
  }

  public BulletColor(double r1, double g1, double b1, double r2, double g2, double b2) {
    this(Color.color(r1, g1, b1), Color.color(r2, g2, b2));
  }

  public String getId() {
    return "(" + innerColor.getRed() + "," + innerColor.getGreen() + "," + innerColor.getBlue() +
            "),(" + outerColor.getRed() + "," + outerColor.getGreen() + "," + outerColor.getBlue() + ")";
  }
}
