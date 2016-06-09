package pr2Calc;

enum VectorType { ROW, COLUMN }

public class MyVector extends Matrix {
	protected VectorType vectorType_;
	
	public MyVector(double[][] input){
		super( input );

		if ( this._getNumOfRow() == 1 ) {
			this.vectorType_ = VectorType.ROW;
			return;
		}

		if ( this._getNumOfColumn() == 1 ) {
			this.vectorType_ = VectorType.COLUMN;
			return;
		}

		throw new IllegalArgumentException( "引数の形式が正しくありません．" );
	}

	public MyVector( Matrix input ) {
		this( input.m_ );
	}

	// 機能：行ベクトルか判断します
	// 使い方：行ベクトルの場合はtrue，列ベクトルの場合はfalseを返します．
	private boolean _isRowVector() {
		if ( this.vectorType_ == VectorType.ROW )
			return true;
		return false;
	}

	protected void _printVector() {
		this._printMatrix();
	}

	protected double _getInnerProduct( MyVector a ) {
		if ( !this._isRowVector() )
			throw new ArithmeticException( "行ベクトルからメソッドを呼んでください．" );

		double[][] row_a;
		if ( !a._isRowVector() ) {
			row_a = a._transpose();
		} else {
			row_a = a.m_;
		}

		if ( this._getNumOfColumn() != row_a[0].length )
			throw new ArithmeticException( "引数のベクトルの次元が一致しないため計算できません．" );

		return this._getInnerProduct( this.m_[0], row_a[0] );
	}

	protected MyVector _rotate( double theta ) {
		double rad = this._toRadianValue( theta );
		Matrix rotation_matrix = new Matrix( new double [][] {
			{ Math.cos( rad ), -Math.sin( rad ) }
		  , { Math.sin( rad ),  Math.cos( rad ) }
		} );

		if ( this._isRowVector() )
			throw new ArithmeticException( "列ベクトルからメソッドを呼んでください．" );

		return new MyVector( rotation_matrix._multiplyMatrix( this ) );
	}

	protected double _toRadianValue( double theta ) {
		return theta * Math.PI / 180;
	}
}