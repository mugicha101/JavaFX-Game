package application;

public class Position {
  public double x;
  public double y;

  public Position(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Position() {
    this(0, 0);
  }

  public Position(Position pos) {
    this(pos.x, pos.y);
  }

  public double[] getArr() {
    return new double[] {x, y};
  }

  public Position move(double x, double y, double multi) {
    this.x += x * multi;
    this.y += y * multi;
    return this;
  }

  public Position move(double x, double y) {
    return move(x, y, 1);
  }

  public Position move(double[] offset, double multi) {
    return move(offset[0], offset[1], multi);
  }

  public Position move(int[] offset, double multi) {
    return move(offset[0], offset[1], multi);
  }

  public Position move(double[] offset) {
    return move(offset[0], offset[1], 1);
  }

  public Position move(Position pos) {
    return move(pos.x, pos.y);
  }

  public Position moveInDir(double dir, double amount) {
    x += Math.cos(dir * Math.PI / 180) * amount;
    y -= Math.sin(dir * Math.PI / 180) * amount;
    return this;
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

  public Position clone() {
    return new Position(this);
  }

  public double distSqd(double x, double y) {
    return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2);
  }

  public double distSqd(Position pos) {
    return distSqd(pos.x, pos.y);
  }

  public double dist(double x, double y) {
    return Math.sqrt(distSqd(x, y));
  }

  public double dist(Position pos) {
    return Math.sqrt(distSqd(pos));
  }

  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
