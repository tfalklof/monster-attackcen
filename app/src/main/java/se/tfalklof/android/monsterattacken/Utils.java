package se.tfalklof.android.monsterattacken;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by tobias on 2018-01-05.
 */

class Utils {

    private static double scaleFactor = 1.0;

    public static int getWidth(Drawable drawable) {
        return (int) (drawable.getIntrinsicWidth() * scaleFactor);
    }

    public static int getHeight(Drawable drawable) {
        return (int) (drawable.getIntrinsicHeight() * scaleFactor);
    }

    public static int getWidth(List<Rect> bounds) {
        Rect result = new Rect();
        int i = 0;
        for (Rect r : bounds) {
            if (i == 0) {
                result.set(r);
            } else {
                result.union(r);
            }
        }
        return result.width();
    }

    public static int getHeight(List<Rect> bounds) {
        Rect result = new Rect();
        int i = 0;
        for (Rect r : bounds) {
            if (i == 0) {
                result.set(r);
            } else {
                result.union(r);
            }
        }
        return result.height();
    }

    public static double getScaleFactor() {
        return scaleFactor;
    }

    public static void setScaleFactor(double scaleFactor) {
        Utils.scaleFactor = scaleFactor;
    }
}
