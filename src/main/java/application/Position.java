package application;

public class Position {
  int x;
  int y;
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position() {
    this(0,0);
  }

  public int[] getArr() {
    return new int[] {x, y};
  }
}
