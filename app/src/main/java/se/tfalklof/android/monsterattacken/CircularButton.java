package se.tfalklof.android.monsterattacken;

public class CircularButton extends TouchControl implements TouchControlActionHandler {
	
	CircularButton() {
		super();
	}

	public void actionDownAt(int pointerId, int x, int y) {
		super.actionDownAt(pointerId, x, y);
	}

	public void actionUpAt(int pointerId, int x, int y) {
		super.actionUpAt(pointerId, x, y);
	}

	public void actionMoveTo(int pointerId, int x, int y) {
		super.actionMoveTo(pointerId, x, y);
	}
	
	public void reset() {
		super.reset();
	}

	public boolean isPosInTouchControlFocusArea(int x, int y) {
		return ((x-getX())*(x-getX()) + (y-getY())*(y-getY()) <= getWidth()/2 * getWidth()/2);
	}
	
	public boolean isPosInTouchControlUnfocusArea(int x, int y) {
		return ((x-getX())*(x-getX()) + (y-getY())*(y-getY()) <= getWidth() * getWidth());
	}

	public String toString() {
		return "circularbutton[name="+getName()+", x="+getX()+", y="+getY()+", width="+getWidth()+", (height="+getHeight()+"), pressedByPointer="+getPressedByPointer()+"]";
	}
}
