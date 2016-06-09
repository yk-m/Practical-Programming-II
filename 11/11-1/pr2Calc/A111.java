package pr2Calc;

public class A111 implements Equation {
	public static final double ALPHA = 2.95
	                         , DELTA = 0.00001
	;
	@Override
	public double f( double x ) {
		return Math.exp(x) - ALPHA*x;
	}

	public double df( double x ) {
		// return Math.exp(x) - ALPHA;
		return ( this.f( x + DELTA ) - this.f(x) ) / DELTA;
	}
}