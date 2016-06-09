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

	// 機能：転置ベクトルを返します．
	// 使い方：転置したいベクトルからメソッドを呼べば，転置したベクトルを返します．
	@Override
	protected MyVector _transpose() {
		return new MyVector( this._transpose( this.m_ ) );
	}

	protected double _getInnerProduct( MyVector a ) {
		if ( !this._isRowVector() )
			throw new ArithmeticException( "行ベクトルからメソッドを呼んでください．" );

		if ( !a._isRowVector() ) {
			a = a._transpose();
		}

		if ( this._getNumOfColumn() != a._getNumOfColumn() )
			throw new ArithmeticException( "引数のベクトルの次元が一致しないため計算できません．" );

		return this._getInnerProduct( this.m_[0], a.m_[0] );
	}

	// 機能：自分自身とtargetで指定されたベクトルについて積を計算します．
	// 使い方：MyVectorで積を求めたいベクトルを指定してください．ベクトルなどに問題がある場合はエラーをスローします．
	protected MyVector _multiplyVector( MyVector target ) {
		return new MyVector( this._multiplyMatrix( this.m_, target.m_ ) );
	}
}