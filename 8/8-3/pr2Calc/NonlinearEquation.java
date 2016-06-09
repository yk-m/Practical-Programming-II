package pr2Calc;

import java.applet.*;
import java.awt.*;
import java.awt.image.*;

public class NonlinearEquation extends Applet {
	public static final double EPSILON = 0.001;
	public static final int MAXIMUM_IT = 100;

	private Coordinate origin_, leftTop_;
	private double xScale_, yScale_;

	private Answer _solveNLEByLinearIteration( double initialValue, Equation e ){
		double current // value
		     , previous = initialValue // pastValue
		     , preprevious = 0
		;

		for( int i = 0; i < MAXIMUM_IT; i++ ) {
			current = e.f( previous );
			System.out.printf( "value: %.3f, pastValue %.3f\n", current, previous );
			this.drawLine( new Coordinate( previous, preprevious ), new Coordinate( previous, current ), Color.green );
			if ( Math.abs( current - previous ) < EPSILON )
				return new Answer( i+1, current );
			this.drawLine( new Coordinate( previous, current ), new Coordinate( current, current ), Color.green );
			preprevious = current;
			previous = current;
		}

		return null;
	}

	public void initAxis( Coordinate origin, double xDistance, double yDistance ) {
		Dimension d = getSize();
		this.xScale_ = xDistance / d.width;
		this.yScale_ = yDistance / d.height;
		this.origin_ = origin;
		this.leftTop_ = new Coordinate( -origin.x_ * this.xScale_, origin.y_ * this.yScale_ );

		Graphics g = getGraphics();
		g.drawLine(
			0,			(int)origin_.y_
		  , d.width,	(int)origin_.y_
		);
		g.drawLine(
			(int)origin_.x_, 0
		  , (int)origin_.x_, d.height
		);

		Font f = g.getFont();
		g.drawString( "0", (int)origin_.x_+3, (int)origin_.y_+f.getSize());

		int a = 0;
		for( int x = (int)-xDistance; x <= xDistance; x++ ) {
			if ( x == 0 )
				continue;
			a = (x%5 == 0) ? 10 : 5;
			g.drawLine(
				toXPositionOnApplet(x), (int)origin_.y_-a
			  , toXPositionOnApplet(x), (int)origin_.y_
			);
		}
		for( int y = (int)-yDistance; y <= yDistance; y++ ) {
			if ( y == 0 )
				continue;
			a = (y%5 == 0) ? 10 : 5;
			g.drawLine(
				(int)origin_.x_+a,	toYPositionOnApplet(y)
			  , (int)origin_.x_,	toYPositionOnApplet(y)
			);
		}
	}

	public int toXPositionOnApplet( double x ) {
		return (int)this.origin_.x_ + (int)(x/this.xScale_);
	}

	public int toYPositionOnApplet( double y ) {
		return (int)this.origin_.y_ - (int)(y/this.yScale_);
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
		Coordinate previous = new Coordinate( this.leftTop_.x_, e )
		         , current
		;
		Dimension d = getSize();
		for( int i = 0; i < d.width; i++ ) {
			current = new Coordinate( previous.x_ + this.xScale_, e );
			drawLine( previous, current, c );
			previous = current;
		}
	}


	public void paint( Graphics g ){
		Dimension d = getSize();
		initAxis( new Coordinate( d.width/2, d.height*3/4 ), 30.0, 10.0 );
		drawEquation( new A831(), Color.red );
		drawEquation( new A82(), Color.blue );

		try {
			// 値２つ返したかったのでAnswerクラスを作りました
			// 他の方程式でやりたくなったときのためにクラスを分けました
			Answer a = this._solveNLEByLinearIteration( 10, new A82() );
			System.out.printf( "x = %.3f at iteration %d.\n", a.value, a.trial );
		} catch( NullPointerException e ) {
			System.out.println( "解が求められませんでした．" );
		}
	}
}