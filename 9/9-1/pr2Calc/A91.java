package pr2Calc;

public class A91 implements Equation {
	public static final double ALPHA = 1.0;
	// public static final double ALPHA = 0.0;
	@Override
	public double f( double x ) {
		if ( x + ALPHA == 0 )
			return 1;
		return Math.sin( x + ALPHA ) / ( x + ALPHA );
	}
}