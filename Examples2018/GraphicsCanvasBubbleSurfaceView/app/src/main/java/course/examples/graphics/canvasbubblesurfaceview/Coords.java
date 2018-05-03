package course.examples.graphics.canvasbubblesurfaceview;

class Coords {
	final float mX;
	final float mY;

	public Coords(float x, float y) {
		mX = x;
		mY = y;
	}

	synchronized Coords move (Coords dxdy) {
		return new Coords(mX + dxdy.mX , mY + dxdy.mY);
	}

    @Override
	public String toString () {
		return "(" + mX + "," + mY + ")";
	}
}
