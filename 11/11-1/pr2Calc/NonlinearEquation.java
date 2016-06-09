package pr2Calc;

public class NonlinearEquation {
	public static final double EPSILON      = 0.001
							 , NEGATIVE_MAX = 0.0
							 // , POSITIVE_MAX = 4.0
							 , POSITIVE_MAX = 6.0
	;
	public static final int MAXIMUM_IT = 100;

	private static Answer _solveNLEByLinearIteration( double initialValue, Equation e ){
		double current // value
		     , previous = initialValue // pastValue
		;

		for( int i = 0; i < MAXIMUM_IT; i++ ) {
			current = e.f( previous );
			System.out.printf( "value: %.3f, pastValue %.3f\n", current, previous );
			if ( Math.abs( current - previous ) < EPSILON )
				return new Answer( i+1, current );
			previous = current;
		}

		return null;
	}

	private static boolean _haveTheSameSign( double a, double b ) {
		if ( a * b < 0 )
			return false;
		return true;
	}

	private static Answer _solveNLEByBisectionMethod( double initialValue, Equation e ){
		double current // value
		     , previous = initialValue // pastValue
		     , lowerEnd = NEGATIVE_MAX
		     , upperEnd = POSITIVE_MAX
		;

		for( int i = 0; i <= MAXIMUM_IT; i++ ) {
			current = (lowerEnd + upperEnd)/2;
			System.out.printf( "xMid = %.6f, f(xMid)=%2.6f, xPastMid = %.6f\n", current, e.f( current ), previous );

			if ( Math.abs( current - previous ) < EPSILON || e.f( current ) == 0.0 )
				return new Answer( i, current );

			previous = current;

			if ( _haveTheSameSign( e.f( current ), e.f( lowerEnd ) ) ) {
				lowerEnd = current;
				continue;
			}
			upperEnd = current;
		}

		return null;
	}

	private static Answer _solveNLEByRegulaFalsi( double initialValue, Equation e ) {
		double current // value
		     , previous = initialValue // pastValue
		     , lowerEnd = NEGATIVE_MAX
		     , upperEnd = POSITIVE_MAX
		;

		for( int i = 0; i <= MAXIMUM_IT; i++ ) {
			current = ( lowerEnd*e.f( upperEnd ) - upperEnd*e.f( lowerEnd ) )/( e.f( upperEnd ) - e.f( lowerEnd ) );
			System.out.printf( "xNext = %e, f(xNext)=%e, xPastNext = %e\n", current, e.f( current ), previous );

			if ( Math.abs( current - previous ) < EPSILON || e.f( current ) == 0.0 )
				return new Answer( i, current );

			previous = current;

			if ( _haveTheSameSign( e.f( current ), e.f( lowerEnd ) ) ) {
				lowerEnd = current;
				continue;
			}
			upperEnd = current;
		}

		return null;
	}

	private static Answer _solveNLEByNewton( double initialValue, Equation e ) {
		double current // value
		     , previous = initialValue // pastValue
		;

		for( int i = 0; i <= MAXIMUM_IT; i++ ) {
			current = previous - e.f( previous )/e.df( previous );
			System.out.printf( "xNext = %e, f(xNext)=%e\n", current, e.f( current ) );

			if ( Math.abs( current - previous ) < EPSILON || e.f( current ) == 0.0 )
				return new Answer( i, current );

			previous = current;
		}

		return null;
	}

	public static void main(String[] args) {
		try {
			// 値２つ返したかったのでAnswerクラスを作りました
			// 他の方程式でやりたくなったときのためにクラスを分けました
			Answer a = NonlinearEquation._solveNLEByNewton( 1.19, new A111() );
			System.out.printf( "x = %.6f at iteration %d.\n", a.value, a.trial );
		} catch( NullPointerException e ) {
			System.out.println( "解が求められませんでした．" );
		}
	}
}