package percolation;


import java.util.Arrays;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


// Percolation:
// inputs: insulating and metallic materials
// N x N grid of sites (each site open or blocked)
// full site = open site that is a percolated site between top and bottom

// p   = probability to be open
// 1-p = probability to be blocked
// X   = probability of percolation
// p*  = threshold value
// p > p* --> always percolates (almost)
// p < p* --> never percolates (almost)


public class Percolation 
{
	// Note: this grid is N x N with valid values of 1 to N, not 0 to N-1!!
	private static final int mMinIdx = 1;	// the min allowed  grid index
	private int       mMaxIdx;		// the max allowed grid index
	// indexes referring to the (0 to N-1) range
	private static final int mTopIdx = 0;
	private int       mBotIdx;
	
	// 2D array of booleans
	// true  --> open
	// false --> blocked
	private boolean[][] mOpenGrid = null;
	
	private WeightedQuickUnionUF mUnionFind = null;
	
	// create N x N grid; all sites blocked
	public Percolation(int N)	
	{
		if (N <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		// the max index in the N x N grid (1 to N system)
		mMaxIdx = N;
		// the max array index in the 1D array (0 to N*N-1)
		mBotIdx = N * N-1;
		
		// initialize the grid to blocked/false
		mOpenGrid  = new boolean[N][N];
		for (int j=0; j < N; ++j)
		{
			Arrays.fill(mOpenGrid[j], false);
		}
		// initialize the weighted union find with a 1D array of 0 to N*N-1
		mUnionFind = new WeightedQuickUnionUF(N*N);	
	}
	
	
	// open site (row i, column j) if not open
	public void open(int i, int j)
	{
		verifyGridInexes(i, j);
		
		// the rows and columns in the NxN grid of 0 to N in each dimensions
		int row = i-1;
		int col = j-1;
		
		// set the input index to open/true
		mOpenGrid[row][col] = true;

		// Top of grid
        if (row == 0) 
        { 
            mUnionFind.union( mTopIdx, get1DIdx(row, col) );
        }		
        // Bottom of grid
        if (row == mMaxIdx - 1) 
        { 
        	mUnionFind.union( mBotIdx, get1DIdx(row, col) );
        }        
        // interior portions of the grid
        // Note, the single "+1" added to row and col in isOpen() is to account for 
        // converting from a 0 to N-1 range to a 1 to N range
        if ( row+1 < mMaxIdx ) 
        { 
            if ( isOpen(row+1+1, col+1) )
            {
            	mUnionFind.union(get1DIdx(row, col), get1DIdx(row+1, col));
            }
        }
        if ( (row-1) >= 0) 
        { 
            if ( isOpen(row-1+1, col+1) )
            {
            	mUnionFind.union(get1DIdx(row, col), get1DIdx(row-1, col));
            }
        }
        if ( (col+1) < mMaxIdx ) 
        { 
            if ( isOpen(row+1, col+1+1) )
            {
            	mUnionFind.union(get1DIdx(row, col), get1DIdx(row, col+1));
            }
        }
        if ( (col-1) >= 0 ) 
        { 
            if ( isOpen(row+1, col-1+1) )
            {
            	mUnionFind.union(get1DIdx(row, col), get1DIdx(row, col-1));
            }
        }
		
	}
			
	// is site i,j open? (1 to N range)
	public boolean isOpen(int i, int j)
	{
		verifyGridInexes(i, j);
		
		boolean open = mOpenGrid[i-1][j-1];
		
		return open;
	}
	
	// is site i,j full?
	public boolean isFull(int i, int j)
	{
		verifyGridInexes(i, j);
		
		boolean isFull = mUnionFind.connected(mTopIdx, get1DIdx(i-1, j-1) );
		
		return isFull;
	}
	
	// verify that indexes i,j are valid (1 to N range)
	private void verifyGridInexes(int i, int j)
	{
		if (i < mMinIdx || i > mMaxIdx)
		{
			throw new IndexOutOfBoundsException();
		}
		if (j < mMinIdx || j > mMaxIdx)
		{	
			throw new IndexOutOfBoundsException();
		}		
	}
	
	// takes in row, col, based upon 0 to N-1 range
	private int get1DIdx(int row, int col)
	{
		int idx = (row * mMaxIdx) + col;
		
		return idx;
	}
	
    public boolean percolates() 
    {
    	boolean isConnected = mUnionFind.connected(mTopIdx, mBotIdx);
    	
        return isConnected;
    }
	
	// test client
	public static void main(String[] args)
	{			
		System.out.println("Percolation Main: does nothing.");
	}
}
