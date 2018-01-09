package se.tfalklof.android.monsterattacken;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobias on 2017-12-30.
 */

class Spelsak {
    private final Drawable bild;
    private double posX;
    private double posY;
    private List<Rect> unScaledBounds = new ArrayList<>();
    private List<Rect> scaledBounds = new ArrayList<>();

    Spelsak(Drawable bild, double posX, double posY, double scaleFactor) {
        this(bild, posX, posY, scaleFactor, new ArrayList<Rect>());
        List<Rect> defaultUnscaledBounds = new ArrayList<>();
        defaultUnscaledBounds.add(new Rect(-1 * bild.getIntrinsicWidth() / 2, -1 * bild.getIntrinsicHeight() / 2, bild.getIntrinsicWidth() / 2, bild.getIntrinsicHeight() / 2));
        unScaledBounds.addAll(defaultUnscaledBounds);
        scaleBounds(scaleFactor);
    }

    Spelsak(Drawable bild, double posX, double posY, double scaleFactor, List<Rect> unscaledBounds) {
        this.bild = bild;
        this.posX = posX;
        this.posY = posY;
        this.unScaledBounds.addAll(unscaledBounds);
        scaleBounds(scaleFactor);
    }

    public Drawable getBild() {
        return bild;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    @Override
    public String toString() {
        return "Spelsak{" +
                "bild=" + bild +
                ", posX=" + posX +
                ", posY=" + posY +
                ", unScaledBounds=" + unScaledBounds +
                ", scaledBounds=" + scaledBounds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Spelsak spelsak = (Spelsak) o;

        if (Double.compare(spelsak.posX, posX) != 0) return false;
        if (Double.compare(spelsak.posY, posY) != 0) return false;
        return bild != null ? bild.equals(spelsak.bild) : spelsak.bild == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = bild != null ? bild.hashCode() : 0;
        temp = Double.doubleToLongBits(posX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public void minusPosY(double hastighet) {
        posY -= hastighet;
    }

    public void plusPosY(double hastighet) {
        posY += hastighet;
    }

    public void minusPosX(double hastighet) {
        posX -= hastighet;
    }

    public void plusPosX(double hastighet) {
        posX += hastighet;
    }

    public List<Rect> getScaledBounds() {
        return scaledBounds;
    }

    public void scaleBounds(double scaleFactor) {
        scaledBounds.clear();
        for(Rect r : unScaledBounds) {
            scaledBounds.add(new Rect((int) (r.left * scaleFactor), (int) (r.top * scaleFactor), (int) (r.right * scaleFactor), (int) (r.bottom * scaleFactor)));
        }
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
}
