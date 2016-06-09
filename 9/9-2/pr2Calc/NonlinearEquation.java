package pr2Calc;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;

public class NonlinearEquation extends Applet {
	public static final double EPSILON      = 0.001
							 , NEGATIVE_MAX = 0.0
							 , POSITIVE_MAX = 5.0
							 // , POSITIVE_MAX = 6.0
	;
	public static final int MAXIMUM_IT = 100;

	// private Coordinate origin_, leftTop_;
	private Coordinate topLeft_, bottomRight_;
	private double xScale_, yScale_;

	private Answer _solveNLEByLinearIteration( double initialValue, Equation e ){
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

	private Answer _solveNLEByBisectionMethod( double initialValue, Equation e ){
		double current // value
		     , previous = initialValue // pastValue
		     , lowerEnd = NEGATIVE_MAX
		     , upperEnd = POSITIVE_MAX
		;
		Graphics g = getGraphics();

		Font f = g.getFont();
		int fontPosition = 50;
		for( int i = 0; i < MAXIMUM_IT; i++ ) {
			current = (lowerEnd + upperEnd)/2;
			System.out.printf( "xMid = %.6f, f(xMid)=%2.6f, xPastMid = %.6f\n", current, e.f( current ), previous );

			if ( Math.abs( current - previous ) < EPSILON || e.f( current ) == 0.0 )
				return new Answer( i, current );

			drawLine( new Coordinate( current, e ), new Coordinate( current, 0 ), Color.green );
			g.drawString( String.valueOf(i+1), toXPositionOnApplet(current), fontPosition);
			fontPosition += f.getSize();

			previous = current;

			if ( _haveTheSameSign( e.f( current ), e.f( lowerEnd ) ) ) {
				lowerEnd = current;
				continue;
			}
			upperEnd = current;
		}

		return null;
	}

	public void drawScale( double xDistance, double yDistance, double xStep, double yStep ) {
		Graphics g = getGraphics();
		Dimension d = getSize();
		int scaleLength = 5;
		for( double x = topLeft_.x_; x <= bottomRight_.x_; x+=xStep ) {
			if ( x == 0 )
				continue;
			g.drawLine(
				toXPositionOnApplet(x), toYPositionOnApplet(0)-scaleLength
			  , toXPositionOnApplet(x), toYPositionOnApplet(0)
			);
		}
		for( double y = bottomRight_.y_; y <= topLeft_.y_; y+=yStep ) {
			if ( y == 0 )
				continue;
			g.drawLine(
				toXPositionOnApplet(0)+scaleLength,	toYPositionOnApplet(y)
			  , toXPositionOnApplet(0),				toYPositionOnApplet(y)
			);
		}
	}

	public void drawAxis() {
		Graphics g = getGraphics();
		// X Axis
		drawLine( new Coordinate( topLeft_.x_, 0 ), new Coordinate( bottomRight_.x_, 0 ), Color.black );
		// Y Axis
		drawLine( new Coordinate( 0, bottomRight_.y_ ), new Coordinate( 0, topLeft_.y_ ), Color.black );

		Font f = g.getFont();
		g.drawString( "0", toXPositionOnApplet(0)+3, toYPositionOnApplet(0)+f.getSize());
	}
	public void initAxis( Coordinate topLeft, Coordinate bottomRight, double xStep, double yStep ) {
		double xDistance = Math.abs( bottomRight.x_ - topLeft.x_ )
		     , yDistance = Math.abs( topLeft.y_ - bottomRight.y_ )
		;

		Dimension d = getSize();
		this.xScale_ = xDistance / d.width;
		this.yScale_ = yDistance / d.height;
		this.topLeft_     = topLeft;
		this.bottomRight_ = bottomRight;

		this.drawAxis();
		this.drawScale( xDistance, yDistance, xStep, yStep );
	}

	public int toXPositionOnApplet( double x ) {
		return (int)( ( x - topLeft_.x_ ) / this.xScale_ );
	}

	public int toYPositionOnApplet( double y ) {
		return (int)( ( topLeft_.y_ - y ) / this.yScale_ );
	}

	public void drawLine( Coordinate start, Coordinate end, Color c ) {
		Graphics g = getGraphics();
		g.setColor(c);
		g.drawLine(
			toXPositionOnApplet(start.x_),	toYPositionOnApplet(start.y_),
			toXPositionOnApplet(end.x_),	toYPositionOnApplet(end.y_)
		);
	}

	public void drawEquation( Equation e, Color c ) {
		Coordinate previous = new Coordinate( this.topLeft_.x_, e )
		         , current
		;
		Dimension d = getSize();
		for( int i = 0; i < d.width; i++ ) {
			current = new Coordinate( previous.x_ + this.xScale_, e );
			drawLine( previous, current, c );
			previous = current;
		}
	}

	public void drawEquationInRange( Equation e, double xStart, double xEnd, Color c1, Color c2 ) {
		Coordinate previous = new Coordinate( xStart, e )
		         , current
		;
		drawLine( previous, new Coordinate( xStart, 0 ), c1 );
		for( int i = 0; previous.x_ <= xEnd; i++ ) {
			current = new Coordinate( previous.x_ + this.xScale_, e );
			drawLine( previous, current, c2 );
			previous = current;
		}
		drawLine( new Coordinate( xEnd, e ), new Coordinate( xEnd, 0 ), c1 );
	}

	public void paint( Graphics g ){
		Dimension d = getSize();
		// initAxis( new Coordinate( d.width/7, d.height*10/13 ), 7.0, 1.3, 1, 0.1 );
		initAxis( new Coordinate( -1, 1 ), new Coordinate( 6, -0.3 ), 1, 0.1 );
		drawEquationInRange( new A91(), NEGATIVE_MAX, POSITIVE_MAX, Color.red, Color.blue );

		try {
			// 値２つ返したかったのでAnswerクラスを作りました
			// 他の方程式でやりたくなったときのためにクラスを分けました
			Answer a = this._solveNLEByBisectionMethod( 0, new A91() );
			System.out.printf( "x = %.6f at iteration %d.\n", a.value, a.trial );
		} catch( NullPointerException e ) {
			System.out.println( "解が求められませんでした．" );
		}
	}
}