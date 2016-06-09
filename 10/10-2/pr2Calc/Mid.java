package pr2Calc;

public class Mid implements Equation {
	@Override
	public double f( double x ) {
		return 1.5 * Math.exp(x) - 6.5 * x;
	}
}