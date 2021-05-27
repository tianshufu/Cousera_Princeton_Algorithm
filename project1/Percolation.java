

import edu.princeton.cs.algs4.WeightedQuickUnionUF;



public class Percolation {
    private final WeightedQuickUnionUF wUF;
    private final WeightedQuickUnionUF wUFperculates;
    private  final int N;
    private  final int numCount;
    private  boolean[][] booleanGrids;


    /**
     * creates n-by-n grid, with all sites initially blocked
     * @param n
     */
    public Percolation(int n){
        if(n<=0){
            throw  new java.lang.IllegalArgumentException("negative number");

        }
        ///Initial all the nodes
        this.N=n;
        this.numCount=n*n+2;
        ///System.out.println(numCount);
        //Used to check if isFull
        wUF=new WeightedQuickUnionUF(numCount-1);
        //Used to check if perculates
        wUFperculates=new WeightedQuickUnionUF(numCount);

        //initial the grids to be False
        booleanGrids=new boolean[n][n];
        for (int i=0;i<n;i++){
            for (int j=0;j<n;j++){
                this.booleanGrids[i][j]=false;
            }
        }


    }

    /**
     * Map the 2d to 1d
     * Notes: the node start from 1 instead of 0
     * @param row
     * @param col
     * @param
     * @return
     */
    private int Mapping(int row, int col){
        return  (row-1)*N+col;

    }

    /**
     * Check if the node is in the board
     * @param row
     * @param col
     * @return
     */
    private   boolean isIn(int row, int col){
        return  ((row<=N)&(row>=1)&(col<=N)&(col>=1));
    }

    /**
     *  opens the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col){
        if (!isIn(row,col)){
            throw new IllegalArgumentException("node is not in the correct range");
        }
        ///open the grid
        if (!isOpen(row,col)){
            this.booleanGrids[row-1][col-1]=true;
        }

        if(row==1){
            wUF.union(Mapping(row,col),0);
            wUFperculates.union(Mapping(row,col),0);
        }

        if(row==N){
            wUFperculates.union(Mapping(row,col),numCount-1);
        }

        ///get the 1d position
        int pos=Mapping(row,col);
        //System.out.println(row+":"+col+":"+":"+pos);
        ///connect the grid if the new node is in the matrix and is opened
        if (isIn(row-1,col)&&(isOpen(row-1,col))){
            wUF.union(Mapping(row-1,col),pos);
            wUFperculates.union(Mapping(row-1,col),pos);
        }
        if (isIn(row+1,col)&&(isOpen(row+1,col))){
            wUF.union(Mapping(row+1,col),pos);
            wUFperculates.union(Mapping(row+1,col),pos);

        }
        if (isIn(row,col-1)&&(isOpen(row,col-1))){
            wUF.union(Mapping(row,col-1),pos);
            wUFperculates.union(Mapping(row,col-1),pos);
        }
        if (isIn(row,col+1)&&(isOpen(row,col+1))){
            wUF.union(Mapping(row,col+1),pos);
            wUFperculates.union(Mapping(row,col+1),pos);
        }

    }

    /**
     * is the site (row, col) open?
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col){
        if (!isIn(row,col)){
            throw new IllegalArgumentException("node is not in the correct range");
        }
        return  booleanGrids[row-1][col-1];

    }

    /**
     * is the site (row, col) full?
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col){
        if (!isIn(row,col)){
            throw new IllegalArgumentException("node is not in the correct range");
        }
        int pos=Mapping(row,col);
        return (wUF.find(pos)==wUF.find(0));
        //return  wUF.connected(pos,0);
    }

    /**
     * returns the number of open sites
     * @return
     */
    public int numberOfOpenSites(){
        int count=0;
        for(int i=1;i<=N;i++){
            for (int j=1;j<=N;j++){
                if (booleanGrids[i-1][j-1]){
                    count+=1;
                }
            }
        }
        return  count;
    }

    /**
     *  does the system percolate?
     * @return
     */
    public boolean percolates(){
        if (N==1){
            return  isOpen(1,1);
        }

        return  (wUFperculates.find(0)==wUFperculates.find(numCount-1));
    }


    /**
     *  test client (optional)
     * @param args
     */
    public static void main(String[] args){
        Percolation percolation=new Percolation(2);
        System.out.println("Is full: "+percolation.isFull(2,1));
        percolation.open(1,1);
        System.out.println("Is full: "+percolation.isFull(2,1));
        percolation.open(2,2);
        System.out.println("Is full: "+percolation.isFull(2,1));
        percolation.open(1,2);
        System.out.println("Is full: "+percolation.isFull(2,2));
        System.out.println(percolation.percolates());


    }
}
