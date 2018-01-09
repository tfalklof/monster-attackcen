package se.tfalklof.android.monsterattacken;

public class RectangularButton extends TouchControl implements TouchControlActionHandler {
	
	public RectangularButton() {
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
		return (x >= (getX() - getWidth()/2) && x <= (getX() + getWidth()/2) && y >= (getY() - getHeight()/2) && y <= (getY() + getHeight()/2));
	}
	
	public boolean isPosInTouchControlUnfocusArea(int x, int y) {
		return (x >= (getX() - getWidth()) && x <= (getX() + getWidth()) && y >= (getY() - getHeight()) && y <= (getY() + getHeight()));
	}

	public String toString() {
		return "rectangularbutton[name="+getName()+", x="+getX()+", y="+getY()+", width="+getWidth()+", height="+getHeight()+", pressedByPointer="+getPressedByPointer()+"]";
	}
}
