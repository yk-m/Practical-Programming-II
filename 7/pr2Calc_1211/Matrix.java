package pr2Calc;

public class Matrix {
	protected double[][] m_;
	
	public Matrix( double[][] input ) {
		m_ = new double [ input.length ][];
		for ( int i = 0; i < input.length; i++ ) {
			m_[ i ] = input[i].clone();
		}
	}

	protected int _getNumOfRow() {
		return m_.length;
	}
	
	protected int _getNumOfColumn() {
		return m_[0].length;
	}

	// 引数がこの行列の行数として正しいか判定します
	private boolean _isOutOfRow( int row ) {
		if ( row < 0 || row >= _getNumOfRow() )
			return true;
		return false;
	}

	// 引数がこの行列の列数として正しいか判定します
	private boolean _isOutOfColumn( int column ) {
		if ( column < 0 || column >= _getNumOfColumn() )
			return true;
		return false;
	}
	
	protected double _getComponentOf(int row, int column) {
		if( _isOutOfRow( row ) || _isOutOfColumn( column ) )
			throw new ArrayIndexOutOfBoundsException( "指定する要素は存在しません．" );

		return m_[row][column];
	}
	
	protected void _printMatrix() {
		for ( double[] row: m_ ) {
			System.out.print( "| " );
			_printRow( row );
			System.out.print( "|\n" );
		}
	}

	// 行列の行の表示をします．
	private void _printRow( double[] row ) {
		for ( double value : row ) {
			System.out.printf( "%5.1f ", value );
		}
	}

	// 行列を転置します
	protected double[][] _transpose( double[][] a ) {
		double[][] transposed = new double[ a[0].length ][ a.length ];
		for ( int i = 0; i < transposed.length; i++ ) {
			for ( int j = 0; j < transposed[0].length; j++ ) {
				transposed[i][j] = a[j][i];
			}
		}
		return transposed;
	}

	// 内積計算をします
	protected double _getInnerProduct( double[] a, double[] b ) {
		if ( a.length != b.length )
			throw new ArithmeticException( "要素数が一致していません．" );

		double innerProduct = 0;
		for ( int i = 0; i < a.length; i++ ) {
			innerProduct += a[i] * b[i];
		}
		return innerProduct;
	}

	// 行列積計算をします
	protected double[][] _multiplyMatrix( double[][] a, double[][] b ) {
		if ( a[0].length != b.length )
			throw new ArithmeticException( "掛ける方の列要素数が掛けられる方の行要素数と一致しません．" );

		double[][] transposedB = this._transpose( b )
		         , product     = new double[ a.length ][ transposedB.length ]
		;
		for ( int row = 0; row < product.length; row++ ) {
			for ( int column = 0; column < product[0].length; column++ ) {
				product[ row ][ column ] = this._getInnerProduct( a[ row ], transposedB[ column ] );
			}
		}
		return product;
	}

	// 機能：自分自身とtargetで指定された行列について積を計算します．
	// 使い方：doubleの2次元配列で積を求めたい行列を指定してください．行列などに問題がある場合はエラーをスローします．
	protected double[][] _multiplyMatrix( double[][] target ) {
		return this._multiplyMatrix( this.m_, target );
	}
	
	// 機能：自分自身とtargetで指定された行列について積を計算します．
	// 使い方：Matrixで積を求めたい行列を指定してください．行列などに問題がある場合はエラーをスローします．
	protected Matrix _multiplyMatrix( Matrix target ) {
		return new Matrix( this._multiplyMatrix( this.m_, target.m_ ) );
	}

	protected double[][] _transpose() {
		return this._transpose( this.m_ );
	}

	public static void main(String[] args) {
		double[][]
			a = {
					{ -3.0 }
				  , {  3.0 }
			}
		  , b = {
					{  2.000 }
				  , { -3.464 }
		    }
		;
		
		MyVector vec1 = new MyVector( a )
			   , vec2 = new MyVector( b )
		;

		vec1 = vec1._rotate( 45.0 );
		vec2 = vec2._rotate( 60.0 );

		vec1._printVector();
		vec2._printVector();
	}

}