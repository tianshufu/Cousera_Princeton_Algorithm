
import edu.princeton.cs.algs4.Stack;

public class Board {
    private char[][] intGrids;
    private final int N;



    /**create a board from an n-by-n array of tiles,
     * where tiles[row][col] = tile at (row, col)
     * @param tiles
     */
    public Board(int[][] tiles){
        int rowlength=tiles.length;
        int collength=tiles[0].length;
        this.N=rowlength;
        intGrids=new char[rowlength][collength];
        for(int i=0;i<rowlength;i++){
            for(int j=0;j<collength;j++){
                intGrids[i][j]=(char)tiles[i][j];
            }
        }

    }

    // string representation of this board

    /**
     * string representation of this board,
     * The toString() method returns a string composed of n + 1 lines.
     * The first line contains the board size n; the remaining n lines contains the n-by-n grid of tiles in row-major order, using 0 to designate the blank square.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(intGrids[i][j]);
            }
            s.append("\n");
        }
        return s.toString();
    }



    /**
     * board dimension n
     * @return
     */
    public int dimension(){
        return N;
    }


    /**
     * number of tiles out of place
     * @return
     */
    public int hamming(){
        int hamCount=0;
        for(int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                int mandist=manhatDist(intGrids[i][j],i,j);
                if(mandist!=0){
                    hamCount+=1;
                }
            }
        }

        return hamCount;
    }

    /**
     * Compute the dist of one point to it's target
     * Exp: 8 (0,0)   ->(2,1)  dist=2+1=3
     * @param num
     * @return
     */
    private int manhatDist(int num,int i,int j){
        //If the place is blank
        if(num==0){
            return 0;
        }
        //Calculate the correct index
        int row=(num-1)/N;
        int col=(num-1)%N;
        return abs(row,i)+abs(col,j);
    }

    /**
     * Calculate the absolute value of two values
     * @param i
     * @param j
     * @return
     */
    private int abs(int i,int j){
        if(i>j){
            return i-j;
        }
        else {
            return j-i;
        }
    }



    /**
     * sum of Manhattan distances between tiles and goal
     * @return
     */
    public int manhattan(){
        int hamDist=0;
        for(int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                int mandist=manhatDist(intGrids[i][j],i,j);
                if(mandist!=0){
                    hamDist+=mandist;
                }
            }
        }
        return  hamDist;
    }


    /**
     * is this board the goal board?
     * @return
     */
    public boolean isGoal(){
        return manhattan()==0;

    }


    /**
     * does this board equal y?
     *  Two boards are equal if they are have the same size and their corresponding tiles are in the same positions.
     *  The equals() method is inherited from java.lang.Object, so it must obey all of Java’s requirements.
     * @param y
     * @return
     */
    @Override
    public boolean equals(Object y){
        if(y==null){
            return false;
        }
        if(y.getClass()!=this.getClass()){
            return  false;
        }

        final Board other=(Board) y;
        if(this.dimension()!=other.dimension()){
            return  false;
        }
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(this.intGrids[i][j]!=other.intGrids[i][j]){
                    return false;
                }
            }
        }

        return true;
    }




    /**all neighboring boards
     * The neighbors() method returns an iterable containing the neighbors of the board. Depending on the location of the blank square,
     * a board can have 2, 3, or 4 neighbors.
     * @return
     */
    public Iterable<Board> neighbors(){
        Stack<Board> nears=new Stack<Board>();
        int blankRow=-1;
        int blankCol=-1;
        //Find the zero points
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(intGrids[i][j]==0){
                    blankRow=i;
                    blankCol=j;
                }
            }
        }
        //Find the near points of the blank
        if(isInBoard(blankRow-1,blankCol)){
            //Create a board base on the cur grids
           nears.push(exchange(blankRow,blankCol,blankRow-1,blankCol));
        }
        if(isInBoard(blankRow+1,blankCol)){
            nears.push(exchange(blankRow,blankCol,blankRow+1,blankCol));
        }

        if(isInBoard(blankRow,blankCol-1)){
            nears.push(exchange(blankRow,blankCol,blankRow,blankCol-1));
        }
        if(isInBoard(blankRow,blankCol+1)){
            nears.push(exchange(blankRow,blankCol,blankRow,blankCol+1));

        }
        return nears;
    }

    /**
     * Exchange two points in the board
     * @param oldRow
     * @param oldCol
     * @param newRow
     * @param newCol
     * @return
     */
    private Board exchange(int oldRow,int oldCol,int newRow,int newCol ){
        /**
        Board boardCopy=new Board(intGrids);
        char tmp=boardCopy.intGrids[newRow][newCol];
        boardCopy.intGrids[newRow][newCol]=boardCopy.intGrids[oldRow][oldCol];
        boardCopy.intGrids[oldRow][oldCol]=tmp;
         **/
        int [][] newGrids=new int[N][N];
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                newGrids[i][j]=intGrids[i][j]-'0';
            }

        }
        int tmp=newGrids[oldRow][oldCol];
        newGrids[oldRow][oldCol]=newGrids[newRow][newCol];
        newGrids[newRow][newCol]= tmp;
        return  new Board(newGrids);
    }

    /**
     * Check if a point is in the board
     * @param r
     * @param c
     * @return
     */
    private boolean isInBoard(int r, int c){
        return (r>=0)&&(r<N)&&(c>=0)&&(c<N);

    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        //Board tb=null;
        //StdStats stat=new StdStats();
        for(int i=0;i<N*N-1;i++){
            int x=i/N;
            int y=i%N;
            int xx=(i+1)/N;
            int yy=(i+1)%N;
            if(isNeighbours(x,y,xx,yy)){
                return exchange(x,y,xx,yy);
            }
        }
        /**
        while(true){
            int x= StdRandom.uniform(N);
            int y=StdRandom.uniform(N);
            int xx=StdRandom.uniform(N);
            int yy=StdRandom.uniform(N);
            if(isNeighbours(x,y,xx,yy)){
                return  exchange(x,y,xx,yy);
            }

        }
         **/
        return null;

    }

    /**
     * Check if two points are closed together
     * @param x
     * @param y
     * @param xx
     * @param yy
     * @return
     */
    private boolean isNeighbours(int x,int y,int xx,int yy){
        //0 means the break, not the tiles
        if((intGrids[x][y]==0)||(intGrids[xx][yy]==0)){
            return false;
        }
        if((x==xx)&&(abs(y,yy)==1)){
            return true;
        }
        if((y==yy)&&(abs(x,xx)==1)){
            return true;
        }

        return  false;
    }

    // unit testing (not graded)
    public static void main(String[] args){
        int tiles[][];
        tiles=new int[3][3];
        tiles[0][0]=1;
        tiles[0][1]=0;
        tiles[0][2]=3;
        tiles[1][0]=4;
        tiles[1][1]=2;
        tiles[1][2]=5;
        tiles[2][0]=7;
        tiles[2][1]=8;
        tiles[2][2]=6;

        int tiles2[][];
        tiles2=new int[3][3];
        tiles2[0][0]=8;
        tiles2[0][1]=1;
        tiles2[0][2]=3;
        tiles2[1][0]=4;
        tiles2[1][1]=0;
        tiles2[1][2]=2;
        tiles2[2][0]=7;
        tiles2[2][1]=6;
        tiles2[2][2]=5;

        int tiles3[][];
        tiles3=new int[2][2];
        tiles3[0][0]=1;
        tiles3[0][1]=2;
        tiles3[1][0]=3;
        tiles3[1][1]=0;


        Board board=new Board(tiles);
        Board board2=new Board(tiles2);
        Board board3=new Board(tiles3);
        System.out.println(board.intGrids.toString());
        //System.out.println(board2);
        System.out.println(board2.twin().toString());





    }

}
