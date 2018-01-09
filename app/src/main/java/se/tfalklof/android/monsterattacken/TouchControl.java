package se.tfalklof.android.monsterattacken;

public abstract class TouchControl implements TouchControlActionHandler {

	private String name = null;
	private int x = -1;
	private int y = -1;
	private int width = -1;
	private int height = -1;
	private int pressedByPointer = -1;

	TouchControl() {
	}
	
	int getHeight() {
		return height;
	}

	void setHeight(int height) {
		this.height = height;
	}

	int getWidth() {
		return width;
	}

	void setWidth(int width) {
		this.width = width;
	}

	int getX() {
		return x;
	}

	void setX(int x) {
		this.x = x;
	}

	int getY() {
		return y;
	}

	void setY(int y) {
		this.y = y;
	}

	boolean isPressed() {
		return (pressedByPointer != -1);
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	int getPressedByPointer() {
		return pressedByPointer;
	}

	private void setPressedByPointer(int pressedByPointer) {
		this.pressedByPointer = pressedByPointer;
	}

	public void actionDownAt(int pointerId, int x, int y) {
		// Already pressed down by a pointer
		if(getPressedByPointer() != -1)
			return;

		// If pointer position is inside the touchcontrol's focus area ("small area") then assign pointer to this touchcontrol
		if(isPosInTouchControlFocusArea(x, y)) {
			setPressedByPointer(pointerId);
		}
	}

	public void actionUpAt(int pointerId, int x, int y) {
		if(pointerId == getPressedByPointer())
			reset();
	}

	public void actionMoveTo(int pointerId, int x, int y) {
		if(pointerId != getPressedByPointer())
			return;
		
		// If pointer position is outside the touchcontrol's unfocus area ("big area") unassign the pointer from this touchcontrol
		if(!isPosInTouchControlUnfocusArea(x, y))
			reset();
	}

	public void reset() {
		setPressedByPointer(-1);
	}
	
	public abstract boolean isPosInTouchControlFocusArea(int x, int y);
	
	public abstract boolean isPosInTouchControlUnfocusArea(int x, int y);

	public String toString() {
		return "touchcontrol[name="+getName()+", x="+getX()+", y="+getY()+", width="+getWidth()+", height="+getHeight()+", pressedByPointer="+getPressedByPointer()+"]";
	}
}
