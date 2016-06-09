package pr2Calc;

public class BasicProbability {
	protected int[][] rv_;
	protected int[] numOfStates_;
	protected int   numOfVariables_;
	protected int   numOfData_;

	public BasicProbability( int[][] inputData ){
		numOfVariables_ = inputData[0].length;
		numOfStates_ = new int[ numOfVariables_ ];
		for ( int h = 0; h < numOfVariables_; h++ ) {
			numOfStates_[h] = inputData[0][h];
		}
		
		numOfData_ = inputData.length - 1;
		rv_ = new int[ numOfData_ ][ numOfVariables_ ];
		for ( int h = 0; h < numOfData_; h++ ) {
			for ( int i = 0; i < numOfVariables_; i++ ) {
				rv_[h][i] = inputData[ h + 1 ][i];
			}
		}
	}

	// 確率変数のインデックスは正しいか
	private boolean _isCorrectRvNum( int rvNum ) {
		if ( rvNum  < 0 || rvNum  >= numOfVariables_ )
			return false;
		return true;
	}

	// 確率変数の値は正しいか
	private boolean _isCorrectRvValue( int rvNum, int rvValue ) {
		if ( rvValue  < 0 || rvValue  >= numOfStates_[ rvNum ] )
			return false;
		return true;
	}

	public double getProbability( int rvNum, int rvValue ) {
		double counter = 0.0;

		if ( !_isCorrectRvNum( rvNum ) || !_isCorrectRvValue( rvNum, rvValue ) )
			return -1.0;

		for ( int position = 0; position < numOfData_; position++ ) {
			if ( rv_[ position ][ rvNum ] != rvValue )
				continue;
			counter++;
		}

		return counter / numOfData_;
	}
	
	public double getConditionalProbability( int rvNum, int rvValue, int cRvNum, int cRvValue ){
		double hit = 0.0   // 条件部分を満たす中で、rvNum = rvValue となるデータ数をカウント
		     , total = 0.0 // 条件部分(cRvNum = cRvValue)を満たすデータ総数をカウント
		;

		if ( !_isCorrectRvNum( rvNum ) || !_isCorrectRvNum( cRvNum )
		  || !_isCorrectRvValue( rvNum, rvValue ) || !_isCorrectRvValue( cRvNum, cRvValue ) )
			return -1.0;

		for ( int h = 0; h < numOfData_; h++ ) {
			if ( rv_[ h ][ cRvNum ] != cRvValue )
				continue;
			total++;

			if ( rv_[ h ][ rvNum ] != rvValue )
				continue;
			hit++;
		}

		if ( total != 0.0 )
			return hit/total;

		System.out.println("Denominator = 0."); // 分母が0であることを明示
		return -1.0;
	}

	private boolean _isIdenticalState( int[] rvNums, int[] rvValues , int position ) {
		for ( int i = 0; i < rvNums.length; i++ ) {
			if ( rvValues[i] != rv_[ position ][ rvNums[i] ] )
				return false;
		}
		return true;
	}

	public double getJointProbability( int[] rvNums, int[] rvValues ){
		double counter = 0.0;
		for ( int position = 0; position < numOfData_; position++ ) {
			if ( !_isIdenticalState( rvNums, rvValues, position ) )
				continue;
			counter++;
		}

		return counter / numOfData_;
	}

	private int[] _mergeIntArrays( int[] array1, int[] array2 ) {
		int mergedArray[] = new int[ array1.length + array2.length ];
		System.arraycopy(array1, 0, mergedArray, 0, array1.length);
		System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
		return mergedArray;
	}
	
	public double getConditionalProbability( int[] rvNums, int[] rvValues, int[] cRvNums, int[] cRvValues ){
		for ( int i = 0; i < rvNums.length; i++ ) {
			if ( !_isCorrectRvNum( rvNums[i] ) || !_isCorrectRvNum( cRvNums[i] )
			  || !_isCorrectRvValue( rvNums[i], rvValues[i] ) || !_isCorrectRvValue( cRvNums[i], cRvValues[i] ) )
				return -1.0;
		}

		int allNums[]   = _mergeIntArrays( rvNums, cRvNums )
		  , allValues[] = _mergeIntArrays( rvValues, cRvValues )
		;

		double denominator = getJointProbability( cRvNums, cRvValues );
		if ( denominator == 0.0 ) {
			System.out.println("Denominator = 0."); // 分母が0であることを明示
			return -1.0;
		}

		return getJointProbability( allNums, allValues ) / denominator;

	}

	public static void main(String[] args) {
		int[][] data = {
						{2,2,2,3,2},
						{1,1,0,1,0},
						{1,0,1,2,1},
						{0,0,1,0,0},
						{1,0,0,0,1},
						{0,0,0,2,0},
						{1,1,1,1,0},
						{1,0,0,2,1},
						{0,0,1,1,1},
						{1,0,0,0,0},
						{0,1,1,1,1},
						{1,1,1,0,1}
					   };
		int[]   rv = new int[5] ,val = new int[5];
		
		BasicProbability pr = new BasicProbability(data);

		// rv[0] = 0;      val[0] = 1;     rv[1] = 3;      val[1] = 2;
		// System.out.println( "P(rv"+rv[0]+"="+val[0]+") = " + pr.getProbability(rv[0], val[0]) );
		// System.out.println( "P(rv"+rv[0]+"="+val[0]+"|rv"+rv[1]+"="+val[1]+") = "+
		// 	pr.getConditionalProbability(rv[0], val[0], rv[1], val[1]));

		System.out.printf( "P(X4 = 1) =  %.6f\n"                          , pr.getProbability( 4, 1 ) );
		System.out.printf( "P(X0 = 1, X1 = 0, X2 = 1) =  %.6f\n"          , pr.getJointProbability( new int[] {0,1,2}, new int[] {1,0,1} ) );
		System.out.printf( "P(X0 = 1, X2 = 1) =  %.6f\n"                  , pr.getJointProbability( new int[] {0,2}, new int[] {1,1} ) );
		System.out.printf( "P(X2 = 0 | X3 = 2) =  %.6f\n"                 , pr.getConditionalProbability( 2,0,3,2 ) );
		System.out.printf( "P(X0 = 0, X2 = 1 | X1 = 0, X4 = 0) =  %.6f\n" , pr.getConditionalProbability( new int[] {0,2}, new int[] {0,1}, new int[] {1,4}, new int[] {0,0} ) );
	}

}