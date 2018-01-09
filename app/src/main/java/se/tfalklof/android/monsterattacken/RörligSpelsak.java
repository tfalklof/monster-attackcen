package se.tfalklof.android.monsterattacken;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

class RörligSpelsak extends Spelsak {
    private double hastX;
    private double hastY;

    public RörligSpelsak(Drawable bild, double posX, double posY, double scaleFactor, double hastX, double hastY) {
        super(bild, posX, posY, scaleFactor);
        this.hastX = hastX;
        this.hastY = hastY;
    }

    public RörligSpelsak(Drawable bild, double posX, double posY, double scaleFactor, List<Rect> unscaledBounds, double hastX, double hastY)  {
        super(bild, posX, posY, scaleFactor, unscaledBounds);
        this.hastX = hastX;
        this.hastY = hastY;
    }

    public double getHastX() {
        return hastX;
    }

    public void setHastX(double hastX) {
        this.hastX = hastX;
    }

    public double getHastY() {
        return hastY;
    }

    public void setHastY(double hastY) {
        this.hastY = hastY;
    }

    @Override
    public String toString() {
        return "RörligSpelsak{" + super.toString() +
                ", hastX=" + hastX +
                ", hastY=" + hastY +
                '}';
    }
}
