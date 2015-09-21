package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats 
{
	private int mGridDim              = 0;
	private int mGridSize             = 0;
	private int mNumTests             = 0;
	private double[] mPercThreshArray = null;
	
	private double mCurMean  = 0;
	private double mCurStdev = 0;
	
	private static final double CONF_FACTOR = 1.96;
	
	
	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T)   
	{
		if ( (N <= 0) || (T <= 0) )
		{
			throw new IllegalArgumentException();
		}
		
		mGridDim 		 = N;
		mNumTests        = T;
		mGridSize        = N * N;
		mPercThreshArray = new double[T];	
		
		runTests();	
	}
	
	private void runTests()
	{
		
		for (int testIdx = 0; testIdx<mNumTests; ++testIdx)
		{
			// the number of open sites; runs until percolated
			int curNumOpen = doMonteCarloSim();
			// compute the % open
			mPercThreshArray[testIdx] = (double)curNumOpen / mGridSize;
		}		
	}
	
	private int doMonteCarloSim() 
	{
		Percolation curPerc = new Percolation(mGridDim);
		
		int openCount = 0;
		
		while ( !curPerc.percolates() )
		{
			// compute random indexes
			// add 1 to account for 1 to N instead of 0 to N-1
			int i = StdRandom.uniform(mGridDim) + 1;
			int j = StdRandom.uniform(mGridDim) + 1;
			
			boolean isOpen = curPerc.isOpen(i, j);
			if (!isOpen)
			{
				curPerc.open(i, j);
				++openCount;
			}
		}				
		return openCount;
	}
	
	// sample mean of percolation threshold
	public double mean()       
	{
		mCurMean = StdStats.mean(mPercThreshArray);
		
		return mCurMean;
	}
	
	// sample standard deviation of percolation threshold
	public double stddev()     
	{
		mCurStdev = StdStats.stddev(mPercThreshArray);
		
		return mCurStdev;
	}
	
	// low  endpoint of 95% confidence interval
	public double confidenceLo()  
	{
		double confLo = mCurMean - (mCurStdev * CONF_FACTOR) / mNumTests;
				
		return confLo;
	}
	
	// high endpoint of 95% confidence interval
	public double confidenceHi()     
	{
		double confHi = mCurMean + (mCurStdev * CONF_FACTOR) / mNumTests;
		
		return confHi;
	}

	// test client 
	public static void main(String[] args)    
	{
        //int N = Integer.parseInt(args[0]);
        //int T = Integer.parseInt(args[1]);
		
		int N = 400;
		int T = 100;
			
		
		//long start = System.currentTimeMillis();		
		PercolationStats myStats = new PercolationStats(N, T);
		//long end   = System.currentTimeMillis();
		//double time = (end - start) / 1000.0;
		
		System.out.println("mean                    = " + myStats.mean()   );
		System.out.println("stdev                   = " + myStats.stddev() );
		System.out.println("95% confidence interval = " + myStats.confidenceLo() 
				+ ", " + myStats.confidenceHi() );
		
		//System.out.println("Took: " + time + " sec");
		
	}
	
	
}
