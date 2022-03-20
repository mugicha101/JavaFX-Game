package application.bullet.attr;

import application.bullet.attr.bullet.BulletAttr;

import java.util.HashMap;

public abstract class Attr {
  private final String id;
  public boolean enabled;

  public String getId() {
    return id;
  }

  public Attr(String id) {
    this.id = id;
    enabled = true;
  }

  public abstract Attr clone(String newId); // deep clones object

  public Attr clone() {
    return clone(id);
  }

  public abstract void toMap(
      HashMap<String, Attr> map,
      String prefix); // add self and all child BulletAttr instances to map
}
