import edu.princeton.cs.algs4.Picture;

import java.awt.Color;


public class SeamCarver {
    private int height;
    private int width;
    private final static Double Edge_Energy = 1000.0;
    private Picture seamPicture;

    /**
     * create a seam carver object based on the given picture
     * @param picture
     *
     */
    public SeamCarver(Picture picture){
        if (picture == null){
            throw new IllegalArgumentException();
        }
        this.seamPicture = new Picture(picture);
        this.height = seamPicture.height();
        this.width = seamPicture.width();


    }

    /**
     * Calculate the square dif between the two points
     * @param r1
     * @param r2
     * @param g1
     * @param g2
     * @param b1
     * @param b2
     * @return
     */
    private double calculateDelta(int r1,int r2,int g1,int g2,int b1,int b2){
        int deltaR = Math.abs(r1-r2);
        int deltaG = Math.abs(g1-g2);
        int deltaB = Math.abs(b1-b2);
        return Math.pow(deltaR,2)+Math.pow(deltaG,2)+Math.pow(deltaB,2);
    }

    /**
     * Calulate the delta x for a specific point, note that
     * X is on the col left, which calucate the diff of it's left point and it's right point
     * @param col
     * @param row
     * @return
     */
    private double getDeltaX(int col,int row){
        int p1c,p1r,p2c,p2r;
        // if the point is on the left edge
        if(col==0){
            p1c = width-1;
            p1r = row;
            if (col+1>=width){
                p2c = col;
            } else {
                p2c = col+1;
            }
            p2r = row;
            // if the point is on the right edge
        } else if (col==width-1){
            p1c = 0;
            p1r = row;
            if(col-1<0){
                p2c = col;
            } else {
                p2c = col-1;
            }
            p2r = row;
            // if the point is in the center
        } else {
            p1c = col-1;
            p1r = row;
            p2c = col+1;
            p2r = row;
        }
        Color colP1 = seamPicture.get(p1c,p1r);
        Color colP2 = seamPicture.get(p2c,p2r);
        // calculate the dist
        Double deltaX = calculateDelta(colP1.getRed(),colP2.getRed(),colP1.getGreen(),colP2.getGreen(),colP1.getBlue(),colP2.getBlue());
        return  deltaX;
    }

    /**
     * Calulate the delta y for a specific point, up down shift
     * @param col
     * @param row
     * @return
     */
    private double getDeltaY(int col,int row){
        int p1c,p1r,p2c,p2r;
        // if the point is on the up edge
        if(row==0){
            // the first point should be the down bottom point in the same col
            p1r = row;
            p1c = width-1;
            // the second point should be the point below it
            // if the second point exceed the bottom
            p2c = col;
            if (row+1>=height){
                p2r = row;
            } else {
                p2r = row+1;
            }
            // if the point is on the bottom edge
        } else if (row==height-1){
            // the first point should be the up point of the same col
            p1r = row;
            p1c = 0;
            // the second point should be the up point of the same col
            p2c = col;
            if(row-1<0){
                p2r = row;
            } else {
                p2r = row-1;
            }
        } else {
            p1c = col;
            p1r = row-1;
            p2c = col;
            p2r = row+1;
        }
        Color colP1 = seamPicture.get(p1c,p1r);
        Color colP2 = seamPicture.get(p2c,p2r);
        return  calculateDelta(colP1.getRed(),colP2.getRed(),colP1.getGreen(),colP2.getGreen(),colP1.getBlue(),colP2.getBlue());

    }


    /**
     * current picture
     * @return
     */
    public Picture picture(){
        return seamPicture;
    }

    /**
     * width of current picture
     * @return
     */
    public int width(){
        return  width;
    }

    /**
     * height of current picture
     */
    public int height(){
        return height;
    }

    /**
     * energy of pixel at column x and row y,The energy of pixel (x, y) is Δx2(x, y) + Δy2(x, y), where the square of the x-gradient
     * Δx2(x, y) = Rx(x, y)2 + Gx(x, y)2 + Bx(x, y)2, and where the central differences Rx(x, y), Gx(x, y),
     * and Bx(x, y) are the absolute value in differences of red, green, and blue components between pixel (x + 1, y) and pixel (x − 1, y).
     * The square of the y-gradient Δy2(x, y) is defined in an analogous manner.
     * To handle pixels on the borders of the image, calculate energy by defining the leftmost and rightmost columns as adjacent and the topmost and bottommost rows as adjacent.
     * For example, to compute the energy of a pixel (0, y) in the leftmost column, we use its right neighbor (1, y) and its left neighbor (W − 1, y).
     * Ref: https://www.cs.princeton.edu/courses/archive/fall14/cos226/assignments/seamCarving.html
     * @param x
     * @param y
     * @return
     */
    public double energy(int x, int y){
        if(x<0 || x>=width || y<0 || y>=height){
            throw new IllegalArgumentException();
        } else {
            // check the edge
            if (x==0 || x==width-1 || y==0 || y==height-1){
                return Edge_Energy;
            } else {
                Double deltaX = getDeltaX(x,y);
                Double deltaY = getDeltaY(x,y);
                return Math.sqrt(deltaX+deltaY);
            }
        }

    }

    /**
     * sequence of indices for horizontal seam
     * @return
     */
    public int[] findHorizontalSeam(){
        // transpose the pic
        seamImageTranspose();
        // find the vertical
        int[] nodesList = findVerticalSeam();
        seamImageTranspose();
        return nodesList;
    }

    /**
     * Transpose the image and update the height and width
     * parameter
     */
    private void seamImageTranspose(){
        this.seamPicture = pictureTranspose(this.seamPicture);
        this.height = this.seamPicture.height();
        this.width = this.seamPicture.width();
    }



    /**
     * sequence of indices for vertical seam
     * @return
     */
    public int[] findVerticalSeam(){
        int[] colList = new int[height];
        // check if there is only one row
        if(height<=1){
            colList[0] = 0;
            return colList;
        } else if(width == 1){
            for (int j = 0; j<height;j++){
                colList[j] = 0;
            }
            return colList;
        }
        // create the 2d vertical energy array
        Double[][] energyArray = new Double[height][width];
        Double[][] dp = new Double[height][width];
        int[][] preCol = new int[height][width];
        for (int r = 0;r < height; r++){
            for(int c=0; c< width;c++){
                energyArray[r][c] = energy(c,r);
            }
        }
        // initial the first row of the dp
        for (int c=0; c<width; c++){
            dp[0][c] = energyArray[0][c];
            preCol[0][c] = -1;
        }
        // initial the other col
        for (int r=1; r<height; r++){
            for(int c=0; c<width;c++){
                // deal with the left edge
                if(c == 0){
                    dp[r][c] = energyArray[r][c]+ Math.min(dp[r-1][c],dp[r-1][c+1]);
                    if (dp[r-1][c]>=dp[r-1][c+1]){
                            preCol[r][c] = c+1;
                    } else {
                        preCol[r][c] = c;
                    }
                }else if(c == width-1){
                    dp[r][c] = energyArray[r][c]+Math.min(dp[r-1][c],dp[r-1][c-1]);
                    if(dp[r-1][c]>=dp[r-1][c-1]){
                        preCol[r][c] = c-1;
                    }else {
                        preCol[r][c] = c;
                    }
                }else {
                    dp[r][c] = energyArray[r][c]+Math.min(Math.min(dp[r-1][c-1],dp[r-1][c]),dp[r-1][c+1]);
                    preCol[r][c] = findMinCol(dp,r-1,c);
                }
            }
        }
        // find the min index of the last row in the dp
        int curIndex = minIndex(dp[height-1]);
        colList[height-1] = curIndex;
        for(int r=height-1;r>0;r--){
            int parCol = preCol[r][curIndex];
            colList[r-1] = parCol;
            curIndex = parCol;
        }

        return colList;
    }



    /**
     * Transpose the pic
     * @param picture
     * @return
     */
    private Picture pictureTranspose(Picture picture){
        int m = picture.height();
        int n = picture.width();
        Picture pictureT = new Picture(m,n);
        for(int i = 0; i < m ;i++){
            for(int j = 0; j < n ;j++){
                Color color = picture.get(j,i);
                pictureT.set(i,j,color);
            }
        }
        return  pictureT;
    }

    /**
     * Find the min of the three val in the matrix and
     * return the smallest column
     * @param dp
     * @param r
     * @param c
     * @return
     */
    private int findMinCol(Double[][] dp,int r,int c){
        if (c == 0 || c== width){
            throw new IllegalArgumentException();
        } else {
            double n1 = dp[r][c-1];
            double n2 = dp[r][c];
            double n3 = dp[r][c+1];
            if(n1<n2){
                if(n1<n3){
                    // n1 is the smallest
                    return c-1;
                } else {
                    // n3 is the smallest
                    return c+1;
                }
                // if n2 is smaller
            } else {
                if(n2<n3){
                    return c;
                } else {
                    return c+1;
                }
            }
        }

    }

    /**
     * Find the min val in the array and return it's index
     * @param array
     * @return
     */
    private int minIndex(Double[] array){
        int index = 0;
        for (int i=0;i<array.length;i++){
            if(array[i]<array[index]){
                index = i;
            }
        }
        return index;
    }



    /**
     * remove horizontal seam from current picture
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam){
        if(seam == null || !seamCheck(seam,width) || height<=1){
            throw new IllegalArgumentException();
        } else {
            // transpose the pic and update the val
            seamImageTranspose();
            // find the seam
            removeVerticalSeam(seam);
            // transpose the image back
            seamImageTranspose();
        }
    }

    /**
     * remove vertical seam from current picture
     * @param seam
     */
    public void removeVerticalSeam(int[] seam){

        if(seam == null || !seamCheck(seam,height) || width<=1){
            throw new IllegalArgumentException();
        } else {
            for(int i=0;i<seam.length;i++){
                if(seam[i]<0){
                    throw new IllegalArgumentException();
                }
            }
            // get the vertical seams
            int[] nodeList = seam;
            // copy the nodeList
            Picture pictureRemoved = new Picture(width-1,height);
            // i represent the row num while j means the col num
            for(int i = 0 ; i < height; i++){
                for(int j = 0; j< width-1; j++){
                    if (j < nodeList[i]){
                        pictureRemoved.set(j,i,seamPicture.get(j,i));
                    } else if(j >= nodeList[i]){
                        //System.out.println("J:"+j+" nodeList[i]:"+nodeList[i]);
                        pictureRemoved.set(j,i,seamPicture.get(j+1,i));
                    }
                }
            }
            this.seamPicture = pictureRemoved;
            this.width = pictureRemoved.width();
            this.height = pictureRemoved.height();
        }


    }

    /**
     * Check whether the seam is valid or not
     * @param seam
     * @param size
     * @return
     */
    private boolean seamCheck(int[] seam , int size){
        if (seam.length!=size){
            return  false;
        }

        // iterate the seam to check the val in it
        for(int i = 1; i<seam.length; i++){
            if(Math.abs(seam[i]-seam[i-1])>1){
                return false;
            }
        }
        return  true;
    }





    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver seamCarver = new SeamCarver(picture);
        //Picture picT = seamCarver.pictureTranspose(picture);
        //picT.show();
        //System.out.println(seamCarver.findHorizontalSeam());
        //seamCarver.findVerticalSeam();
        //seamCarver.findHorizontalSeam();
        seamCarver.removeVerticalSeam(new int[]{-1});
        //seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        System.out.println("Height:"+ seamCarver.height + " Width:"+seamCarver.width);
        //System.out.println(seamCarver.getDeltaY(1,2));
        //System.out.println(seamCarver.energy(1,2));
        //System.out.println(picture.width());
        // Test the matrix transpose
        /**
        Double[][] tmp = new Double[2][2];
        tmp[0][0] = 1.0;
        tmp[0][1] = 2.0;
        tmp[1][0] = 3.0;
        tmp[1][1] = 4.0;
        Double[][] tmpT = seamCarver.matrixTranspose(tmp);
        System.out.println(tmpT[1][0]);
         **/




    }























}
