package se.tfalklof.android.monsterattacken;

public class HorizAnalogDPad extends TouchControl implements TouchControlActionHandler {
	
	private double pressedPosition = 0.0; // Ranging from [-1, 1]. -1 = max left, 1 = max right
	
	HorizAnalogDPad() {
		super();
	}

	double getPressedPosition() {
		return pressedPosition;
	}

	private void setPressedPosition(double pressedPosition) {
		this.pressedPosition = pressedPosition;
	}

	public void actionDownAt(int pointerId, int x, int y) {
		// Already pressed down by a pointer
		if(getPressedByPointer() != -1)
			return;

		super.actionDownAt(pointerId, x, y);
		
		// If pointer position is inside this virtualdpad's "small area" then calculate pressed position
		if(isPosInTouchControlFocusArea(x, y)) {
			setPressedPosition((double) (x - getX()) / (double) (getWidth()/2));
		}
	}

	public void actionUpAt(int pointerId, int x, int y) {
		super.actionUpAt(pointerId, x, y);
	}

	public void actionMoveTo(int pointerId, int x, int y) {
		if(pointerId != getPressedByPointer())
			return;
		
		super.actionMoveTo(pointerId, x, y);
		
		// If pointer position is inside this horizanalogdpads's focusarea "big area" then update pressedPosition
		if(isPosInTouchControlFocusArea(x, y)) {
			// when calculating pressedPosition make sure that: this.x-width/2 <= xpos <= this.x+width/2
			int xpos = Math.max(Math.min(x, getX() + getWidth()/2), getX() - getWidth()/2);
			setPressedPosition((double) (xpos - getX()) / (double) (getWidth()/2));
		}
	}
	
	public void reset() {
		super.reset();
		pressedPosition = 0.0;
	}

	public boolean isPosInTouchControlFocusArea(int x, int y) {
		return (x >= (getX() - getWidth()/2) && x <= (getX() + getWidth()/2) && y >= (getY() - getHeight()/2) && y <= (getY() + getHeight()/2));
	}
	
	public boolean isPosInTouchControlUnfocusArea(int x, int y) {
		return (x >= (getX() - getWidth()) && x <= (getX() + getWidth()) && y >= (getY() - getHeight()) && y <= (getY() + getHeight()));
	}

	public String toString() {
		return "horizanalogdpad[name="+getName()+", x="+getX()+", y="+getY()+", width="+getWidth()+", height="+getHeight()+", pressedByPointer="+getPressedByPointer()+", pressedPosition="+getPressedPosition()+"]";
	}
}
