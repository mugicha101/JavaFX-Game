package application.bullet.types;

import application.Position;
import application.bullet.BulletColor;
import application.bullet.attr.Attr;
import application.bullet.attr.bullet.BulletAttr;
import application.bullet.staging.BulletStage;

import java.util.ArrayList;
import java.util.HashMap;

public class BulletTemplate {
    public Position pos;
    public double size;
    public BulletColor color;
    public BulletAttr[] attrArr;
    public BulletStage[] stageArr;
    public BulletType type;
    private final HashMap<String, Attr> attrMap;
    public BulletTemplate(BulletType type, Position pos, double size, BulletColor color, BulletAttr[] attrArr, BulletStage[] stageArr) {
        this.type = type;
        this.pos = pos;
        this.size = size;
        this.color = color;
        this.attrArr = attrArr;
        this.stageArr = stageArr;
        attrMap = new HashMap<>();
        for (BulletAttr ba : attrArr)
            ba.toMap(attrMap, "");
    }
    public BulletTemplate(BulletType type, Position pos, double size, BulletColor color, BulletAttr[] attrArr) {
        this(type, pos, size, color, attrArr, null);
    }

    public final Attr getAttr(String id) {
        return attrMap.get(id);
    }

    public void spawn() {
        switch(type) {
            case ORB -> new Bullet(this);
            case RICE -> new RiceBullet(this);
        }
    }

    public BulletTemplate clone() {
        BulletAttr[] attrArrClone = new BulletAttr[attrArr.length];
        for (int i = 0; i < attrArr.length; i++)
            attrArrClone[i] = attrArr[i].clone();
        BulletStage[] stageArrClone = stageArr == null? null : new BulletStage[stageArr.length];
        if (stageArr != null)
            for (int i = 0; i < stageArr.length; i++)
                stageArrClone[i] = stageArr[i].clone();
        return new BulletTemplate(type, pos.clone(), size, color, attrArrClone, stageArrClone);
    }
}
