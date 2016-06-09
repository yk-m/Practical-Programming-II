package pr2Calc;

public class SimultaneousEquation extends Matrix {
	double[] answers_;
	int[] orderOfColumns_;

	public SimultaneousEquation( double[][] input ) {
		super( input );
		this.answers_ = new double[ this._getNumOfRow() ];
		this.orderOfColumns_ = new int[ this._getNumOfColumn() ];
		for ( int i = 0; i < this._getNumOfColumn(); i++ ) {
			this.orderOfColumns_[i] = i;
		}
	}

	protected void _normalize( int targetRow ) {
		double coefficient = 1/this._getComponentOf( targetRow, targetRow );

		for ( int col = 0; col < this._getNumOfColumn(); col++ ) {
			this.m_[ targetRow ][ col ] *= coefficient;
		}
	}

	protected void _subtractRowFrom( int minuendRow, int subtrahendRow ) {
		double coefficient = this._getComponentOf( minuendRow, subtrahendRow ) / this._getComponentOf( subtrahendRow, subtrahendRow );

		for ( int col = 0; col < this._getNumOfColumn(); col++ ) {
			this.m_[ minuendRow ][ col ] -= coefficient * this.m_[ subtrahendRow ][ col ];
		}
	}

	protected void _exchangeRows( int a, int b ) {
		double[] tmp;
		tmp = this.m_[a];
		this.m_[a] = this.m_[b];
		this.m_[b] = tmp;
	}

	// aとbの列を入れ替えます
	protected void _swapColumns( int a, int b ) {
		double tmp;

		for ( int row = 0; row < this._getNumOfRow(); row++ ) {
			tmp = this.m_[row][a];
			this.m_[row][a] = this.m_[row][b];
			this.m_[row][b] = tmp;
		}

		int tmp2;
		tmp2 = this.orderOfColumns_[ a ];
		this.orderOfColumns_[ a ] = this.orderOfColumns_[ b ];
		this.orderOfColumns_[ b ] = tmp2;
	}

	protected void _exchangeRowsAndColumns( int aRow, int aColumn, int bRow, int bColumn ) {
		this._exchangeRows( aRow, bRow );
		this._swapColumns( aColumn, bColumn );
	}

	protected int _selectPivotFromRow( int targetColumn ) {
		int maxRow = 0;
		double max = 0;
		for ( int row = targetColumn; row < this._getNumOfRow(); row++ ) {
			if ( Math.abs( this.m_[row][targetColumn] ) < max )
				continue;
			maxRow = row;
			max = Math.abs( this.m_[row][targetColumn] );
		}
		return maxRow;
	}

	// 最大値を探して，インデックスを返します
	protected Coordinate _locateMaxValue( int pivot ) {
		int maxRow = pivot
		  , maxColumn = pivot
		;
		double max = this._getComponentOf( pivot, pivot );

		for ( int row = pivot; row < this._getNumOfRow(); row++ ) {
			for ( int col = pivot; col < this._getNumOfColumn() - 1; col++ ) {
				if ( max >= Math.abs( this.m_[row][col] ) )
					continue;
				max = Math.abs( this.m_[row][col] );
				maxRow = row;
				maxColumn = col;
			}
		}
		return new Coordinate( maxRow, maxColumn );
	}

	// Answerの値をセットします
	protected void _setAnswer() {
		for ( int row = 0; row < this._getNumOfRow(); row++ ) {
			this.answers_[ row ] = this.m_[ row ][ this._getNumOfColumn() - 1 ];
		}
	}

	// Answerをプリントします
	protected void _printAnswer() {
		System.out.println( "Answer:" );
		for ( int row = 0; row < this._getNumOfRow(); row++ ) {
			System.out.printf( "x%d = %.1f ", row+1, this.answers_[ this.orderOfColumns_[ row ] ] );
		}
		System.out.print( "\n" );
	}

	public void _solveByGaussJordan() {
		this._printMatrix();
		System.out.print( "\n" );

		for ( int subtrahend = 0; subtrahend < this._getNumOfRow(); subtrahend++ ) {
			this._normalize( subtrahend );
			System.out.println( "" + (subtrahend+1) + "行" + (subtrahend+1) + "列目が1となるように割り、他の行の" + (subtrahend+1) + "列目が0となるように引く" );
			for ( int minuend = 0; minuend < this._getNumOfRow(); minuend++ ) {
				if ( subtrahend == minuend )
					continue;
				this._subtractRowFrom( minuend, subtrahend );
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

		for ( int pivot = 0; pivot < this._getNumOfRow() - 1; pivot++ ) {
			for ( int target = this._getNumOfRow()-1; target > pivot; target-- ) {
				this._subtractRowFrom( target, pivot );
			}
			this._printMatrix();
			System.out.print( "\n" );
		}

		this._setAnswer();
		for ( int row = this._getNumOfRow()-1; 0 <= row; row-- ) {
			double diff = this.answers_[row];
			for ( int col = row+1; col < this._getNumOfColumn()-1; col++ ) {
				diff -= this._getComponentOf( row, col ) * this.answers_[ col ];
			}
			this.answers_[ row ] = diff / this._getComponentOf( row, row );
		}
		this._printAnswer();
	}

	public void _solveByGaussWithPartialSelection() {
		this._printMatrix();
		System.out.print( "\n" );

		for ( int pivot = 0; pivot < this._getNumOfRow() - 1; pivot++ ) {
			int maxRow = this._selectPivotFromRow( pivot );
			this._exchangeRows( pivot, maxRow );
			for ( int target = this._getNumOfRow()-1; target > pivot; target-- ) {
				this._subtractRowFrom( target, pivot );
			}
			this._printMatrix();
			System.out.print( "\n" );
		}

		this._setAnswer();
		for ( int row = this._getNumOfRow()-1; 0 <= row; row-- ) {
			double diff = this.answers_[row];
			for ( int col = row+1; col < this._getNumOfColumn()-1; col++ ) {
				diff -= this._getComponentOf( row, col ) * this.answers_[ col ];
			}
			this.answers_[ row ] = diff / this._getComponentOf( row, row );
		}
		this._printAnswer();
	}

	public void _solveByGaussWithCompleteSelection() {
		this._printMatrix();
		System.out.print( "\n" );

		for ( int pivot = 0; pivot < this._getNumOfRow() - 1; pivot++ ) {
			Coordinate max = this._locateMaxValue( pivot );
			this._exchangeRowsAndColumns( pivot, pivot, max.row_, max.col_ );
			for ( int target = this._getNumOfRow()-1; target > pivot; target-- ) {
				this._subtractRowFrom( target, pivot );
			}
			this._printMatrix();
			System.out.print( "\n" );
		}

		this._setAnswer();
		for ( int row = this._getNumOfRow()-1; 0 <= row; row-- ) {
			double diff = this.answers_[row];
			for ( int col = row+1; col < this._getNumOfColumn()-1; col++ ) {
				diff -= this._getComponentOf( row, col ) * this.answers_[ col ];
			}
			this.answers_[ row ] = diff / this._getComponentOf( row, row );
		}
		this._printAnswer();
	}

	public static void main(String[] args) {
		double[][] equations = {
			{  1,  2,  3,  4,  22 }
		  , { -3,  3, -2,  2, -14 }
		  , {  3, -5,  1,  1,  23 }
		  , {  6, -2,  4, -8,   8 }
		};

		System.out.println( "部分選択法" );
		SimultaneousEquation partial = new SimultaneousEquation( equations );
		partial._solveByGaussWithPartialSelection();
		System.out.print( "\n" );

		System.out.println( "完全選択法" );
		SimultaneousEquation complete = new SimultaneousEquation( equations );
		complete._solveByGaussWithCompleteSelection();
	}
}