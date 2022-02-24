package application;

public class Position {
  double x;
  double y;
  public Position(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Position() {
    this(0,0);
  }

  public Position(Position pos) {
    this(pos.x, pos.y);
  }

  public double[] getArr() {
    return new double[] {x, y};
  }

  public void move(double[] offset, double multi) {
    x += offset[0] * multi;
    y += offset[1] * multi;
  }

  public void move(int[] offset, double multi) {
    move(new double[] {offset[0], offset[1]}, multi);
  }

  public void move(double[] offset) {
    move(offset, 1);
  }

  public void set(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void set(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void set(double[] arr) {
    set(arr[0], arr[1]);
  }

  public void set(int[] arr) {
    set(arr[0], arr[1]);
  }

  public void set(Position pos) {
    set(pos.getArr());
  }

  public void move(int[] offset) {
    move(offset, 1);
  }
}
