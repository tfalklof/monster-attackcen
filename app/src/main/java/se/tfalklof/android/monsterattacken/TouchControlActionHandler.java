package se.tfalklof.android.monsterattacken;

public interface TouchControlActionHandler {
	
	void actionDownAt(int pointerId, int x, int y);
	void actionUpAt(int pointerId, int x, int y);
	void actionMoveTo(int pointerId, int x, int y);

}
