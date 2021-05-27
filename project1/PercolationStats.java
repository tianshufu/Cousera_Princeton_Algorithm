

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;



public class PercolationStats {

    private  final double historyT[];
    private final int T;
    private static final float CONFIDENCE_95= (float) 1.96;




    /**
     *  perform independent trials on an n-by-n grid
     * @param n
     * @param trials
     */

    public PercolationStats(int n, int trials){
        if((n<=0)||(trials<=0)){
            throw  new IllegalArgumentException("n or trials not qualified !");
        }
        int gridCount=n*n;

        this.historyT=new double[trials];
        this.T=trials;
        for(int i=0;i<trials;i++){
            int count=0;
            Percolation percolation=new Percolation(n);
            while (!percolation.percolates()&&(count<=gridCount)){

                int row=StdRandom.uniform(1,n+1);
                int col=StdRandom.uniform(1,n+1);
                //if the point has not been opened
                if(!percolation.isOpen(row,col)){
                    percolation.open(row,col);
                    count+=1;
                }
            }
            double p=(double) count/(double) gridCount;
            this.historyT[i]= p;
        }

    }


    ///public int

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean(){
        return StdStats.mean(historyT);

    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev(){
        return  StdStats.stddev(historyT);
    }


    /**
     * low endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLo(){
        return mean()-CONFIDENCE_95*stddev()/(Math.sqrt(T)) ;
    }


    /**
     *  high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi(){
        return mean()+CONFIDENCE_95*stddev()/(Math.sqrt(T)) ;
    }

    public static void main(String[] args) {
        PercolationStats ps;
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        ps = new PercolationStats(n, trials);
        System.out.println("mean            =" + ps.mean());
        System.out.println("stddev          =" + ps.stddev());
        System.out.println("95% confidence interval ="
                + ps.confidenceLo() + "," + ps.confidenceHi());

    }

}


