package pr2Calc;

public class Coordinate {
	final public double x_;
	final public double y_;

	public Coordinate( double x, double y ) {
		this.x_ = x;
		this.y_ = y;
	}

	public Coordinate( double x, Equation e ) {
		this.x_ = x;
		this.y_ = e.f(x);
	}
}