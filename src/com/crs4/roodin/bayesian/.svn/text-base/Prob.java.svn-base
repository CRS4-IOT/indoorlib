/*
 * 
 */
package com.crs4.roodin.bayesian;

/**
 * @author ICT/LBS Team - CRS4 Sardinia, Italy
 * 
 * Evaluate probabilities of being in a certain cell
 *
 */
public class Prob {
    static int side = 4; // square side for the bayesian algorithm


	/**
	 * Evaluate probabilities
	 * 
	 * @param probs the old cell probabilities
	 * @param barred the barred grid of the sistem
	 * @param heading the compass heading
	 * @return new probabilities
	 */
	public static double[][] evalProbs(double[][] probs, int[][] barred, double heading) {
		
		double[][] newprobs = new double[probs.length][probs[0].length];  // newprobs = numpy.zeros(probs.shape)
		int rows = probs.length;
		int cols = probs[0].length;		//rows,cols = probs.shape
	    int[] max_index = {0, 0};
	    
	    if (heading < 0)  heading = 2 + heading;
	    
	    
	    //Cell previous_est = estimatePos(probs); // it is not essential
	    
	    
	    for (int r=0; r<rows; r++){
	    	for (int c=0; c<cols; c++){
		    	newprobs[r][c] = evalCell(r, c, probs, barred, heading);
		    	if (newprobs[r][c] >= newprobs[max_index[0]][max_index[1]]){
		    			max_index[0] = r;
		    			max_index[1] = c;   //max_index=(r,c)
		    	}
		    }
	    }
	    
	    double norm_factor = 0.;            // norm_factor=sum(sum(newprobs))
	    for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            		norm_factor = norm_factor + newprobs[i][j];
            }
	    }
	    
	    for (int r=0; r< rows; r++){
	        for (int c=0; c<cols; c++){
	        	newprobs[r][c] = (newprobs[r][c] / norm_factor) ;
	        }
	    }

		
		return newprobs;
	}

	
	/**
	 * Evaluate single cell
	 * 
	 * @param row the row index
	 * @param col the col index
	 * @param probs the old cell probabilities
	 * @param barred the barred grid of the sistem
	 * @param heading the compass heading
	 * @return the cell evaluation
	 */
	private static double evalCell(int row, int col, double[][] probs, int[][] barred, double heading) {
		double val = 0;
		
		for (int i=0; i <  barred.length; i++){
			if ((barred[i][0] == row) && (barred[i][1] == col)){  //if (row,col) in barred: return 0
				return 0.;                                       
			}
		}
		
		int rows = probs.length;
		int cols = probs[0].length;		//rows,cols = probs.shape
		
		
		int[] iRange = range(max(0, row-(side-1)), min(rows, row+side));
		int[] jRange = range(max(0, col-(side-1)), min(cols, col+side));
		
		//System.out.println("iRange: "+iRange[0]+" "+iRange[1]);
		//System.out.println("jRange: "+jRange[0]+" "+jRange[1]);


		for (int i : iRange){
			for(int j : jRange){
				if (probs[i][j]==0)
	            //if (probs[i][j] < 1./(rows*cols))
	                continue;
	            
	            double distance = Math.sqrt(Math.pow((row-i),2)+Math.pow((col-j),2));
	            
	            int dy=(i-row);
	            int dx=(col-j);
	            
	            double angle = Math.atan2(dy, dx)/Math.PI; 
	            if (angle < 0) 
	            	angle = 2+angle; //negative angles are expressed as positive 
	            
	            angle = angle % 2; //angles are always in the [0-2*pi] interval
	            
	            double err = Math.abs(heading-angle);
	            val = val + f_conj(distance, err) *probs[i][j];
	            
			}
		}
	
		
		return val;
	
	}
	
	/**
	 * f_conj is the p distribution of sigma and err
	 * 
	 * @param sigma
	 * @param err
	 * @return new sigma
	 */
	private static double f_conj(double sigma, double err){
	    
	    if (sigma < 0.1){
	        return f_sigma(sigma);
	    }else{
	        return f_sigma(sigma)*f_err(err);
	    }
	}
		

	/**
	 * err is angle fraction of pi
	 * 
	 * @param err
	 * @return new err
	 */
	private static double f_err(double err) {
	    
	    err = err % 2;
	    err = Math.abs(err);
	    
	    if (err < .1)
	        return .85;
	    if (err < .4)
	        return .14;
	    if (err < .8)
	        return .01;
	    else
	        return 0;
	}
	
	
	

	/**
	 * sigma is a fraction of Average Step Lenght
	 * 
	 * @param sigma
	 * @return sigma value
	 */
	private static double f_sigma(double sigma) {
	    
	    if (Math.abs(sigma) < 0.1)
	        return .05;

	    if (0.80 < Math.abs(sigma) && Math.abs(sigma) < 1.2)
	        return .95;

	    return 0;
	}
	
	/**
	 * range
	 * 
	 * @param start
	 * @param stop
	 * @return
	 */
	private static int[] range(int start, int stop){
	   int[] result = new int[stop-start];

	   for(int i=0;i<stop-start;i++)
	      result[i] = start+i;

	   return result;
	}
	
	/**
	 * max
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static int max(int a, int b){
		if (a >= b) 
			return a; 
		else
			return b;
	}
	
	/**
	 * min
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static int min(int a, int b){
		if (a <= b) 
			return a; 
		else
			return b;
	}
	
	/**
	 * estimate probs
	 * 
	 * @param probs
	 * @return
	 */
	public static Cell estimatePos(double[][] probs) {
		return max_cell(probs);
	}

	/**
	 * max_cell
	 * 
	 * @param probs
	 * @return
	 */
	private static Cell max_cell(double[][] probs) {
		double v = max(probs);
		
		int rows = probs.length;
		int cols = probs[0].length;		//rows,cols = probs.shape
		
		
	    for (int i=0; i<rows; i++){
	    	for (int j=0; j<cols; j++){
	            if (probs[i][j] == v){
	                double mean_probs = mean(probs);  
	                double stdev = Math.sqrt(  Math.pow((v-mean_probs),2));    //math.sqrt(((v-mean_probs)**2)/1)
	                //System.out.println("mean: "+mean_probs);
	                //System.out.println("stdev: "+stdev);

	                return new Cell(i, j, stdev);
	            }
	    	}
	    }
	    
	    return new Cell(0,0,0);
	}
	
	            
    /**
     * Get the mean of the entire matrix
     * 
     * @param matrix
     * @return the mean
     */
    private static double mean(double[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;		//rows,cols = probs.shape
    			
        return sum(matrix) / (rows*cols);
    }
    
    /**
     * Get the sum of the entire matrix
     * 
     * @param matrix
     * @return the sum
     */
    private static double sum(double[][] matrix){
    	double sum = 0.;
    
    	for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				sum = sum + matrix[i][j];
			}
		}
    	return sum;
    }
	
	
	/**
	 * The max value of the entire matrix
	 * 
	 * @param matrix
	 * @return max value
	 */
	private static double max(double[][] matrix) {
		double maxValue = matrix[0][0];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] > maxValue) {
					maxValue = matrix[i][j];
				}
			}

		}
		return maxValue;
	}

}
