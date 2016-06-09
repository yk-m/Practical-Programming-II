package pr2Calc;

public class A102 implements Equation {
	public static final double ALPHA = 2.0;
	// public static final double ALPHA = 0.0;
	@Override
	public double f( double x ) {
		if ( x + ALPHA == 0 )
			return 1;
		return Math.sin( x + ALPHA ) / ( x + ALPHA );
	}
}