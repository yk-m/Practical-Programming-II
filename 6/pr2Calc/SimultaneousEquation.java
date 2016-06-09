package pr2Calc;

public class SimultaneousEquation extends Matrix {
	double[] answers_;

	public SimultaneousEquation( double[][] input ) {
		super( input );
		this.answers_ = new double[ _getNumOfRow() ];
	}

	protected void _normalize( int targetRow ) {
		double coefficient = 1/_getComponentOf( targetRow, targetRow );

		for ( int col = 0; col < _getNumOfColumn(); col++ ) {
			this.m_[ targetRow ][ col ] *= coefficient;
		}
	}

	protected void _subtractRowFrom( int minuendRow, int subtrahendRow ) {
		double coefficient = _getComponentOf( minuendRow, subtrahendRow ) / _getComponentOf( subtrahendRow, subtrahendRow );

		for ( int col = 0; col < _getNumOfColumn(); col++ ) {
			this.m_[ minuendRow ][ col ] -= coefficient * this.m_[ subtrahendRow ][ col ];
		}
	}

	// Answerの値をセットします
	protected void _setAnswer() {
		for ( int row = 0; row < _getNumOfRow(); row++ ) {
			this.answers_[ row ] = this.m_[ row ][ _getNumOfColumn() - 1 ];
		}
	}

	// Answerをプリントします
	protected void _printAnswer() {
		System.out.println( "Answer:" );
		for ( int row = 0; row < _getNumOfRow(); row++ ) {
			System.out.printf( "x%d = %.1f ", row+1, this.answers_[ row ] );
		}
		System.out.print( "\n" );
	}

	public void _solveByGaussJordan() {
		this._printMatrix();
		System.out.print( "\n" );

		for ( int subtrahend = 0; subtrahend < _getNumOfRow(); subtrahend++ ) {
			_normalize( subtrahend );
			System.out.println( "" + (subtrahend+1) + "行" + (subtrahend+1) + "列目が1となるように割り、他の行の" + (subtrahend+1) + "列目が0となるように引く" );
			for ( int minuend = 0; minuend < _getNumOfRow(); minuend++ ) {
				if ( subtrahend == minuend )
					continue;
				_subtractRowFrom( minuend, subtrahend );
			}
			this._printMatrix();
			System.out.print( "\n" );
		}
		this._setAnswer();
		this._printAnswer();
	}

	public void _solveByGauss() {
		this._printMatrix();
		System.out.print( "\n" );

		for ( int pivot = 0; pivot < _getNumOfRow() - 1; pivot++ ) {
			for ( int target = _getNumOfRow()-1; target > pivot; target-- ) {
				_subtractRowFrom( target, pivot );
			}
			this._printMatrix();
			System.out.print( "\n" );
		}

		this._setAnswer();
		for ( int row = _getNumOfRow()-1; 0 <= row; row-- ) {
			double diff = this.answers_[row];
			for ( int col = row+1; col < _getNumOfColumn()-1; col++ ) {
				diff -= _getComponentOf( row, col ) * this.answers_[ col ];
			}
			this.answers_[ row ] = diff / _getComponentOf( row, row );
		}
		this._printAnswer();
	}

	public static void main(String[] args) {
		SimultaneousEquation equation = new SimultaneousEquation( new double[][] {
			{  1,  2,  3,  4,  22 }
		  , { -3,  3, -2,  2, -14 }
		  , {  6, -2,  4, -8,   8 }
		  , {  3, -5,  1,  1,  23 }
		} );

		equation._solveByGauss();
	}
}