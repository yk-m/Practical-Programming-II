package pr2Calc;

public class NonlinearEquation {
	public static final double EPSILON = 0.001;
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
	
	public static void main(String[] args) {
		try {
			// 値２つ返したかったのでAnswerクラスを作りました
			// 他の方程式でやりたくなったときのためにクラスを分けました
			Answer a = NonlinearEquation._solveNLEByLinearIteration( 0, new A82() );
			System.out.printf( "x = %.3f at iteration %d.\n", a.value, a.trial );
		} catch( NullPointerException e ) {
			System.out.println( "解が求められませんでした．" );
		}
	}
}