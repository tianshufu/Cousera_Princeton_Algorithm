import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


import java.util.ArrayList;
import java.util.Arrays;


public class BruteCollinearPoints {


    private  int numSegments=0;
    private ArrayList<LineSegment> lineSegmentList;
    private LineSegment[] segments;


    /**
     * finds all line segments containing 4 points
     * @param points
     */
    public BruteCollinearPoints(Point[] points)   {
        //Check
        if(points==null){
            throw new IllegalArgumentException("Points null");
        }
        int n=points.length;
        //Check it point is null
        for(int i=0;i<n;i++){
            if(points[i]==null){
                throw  new IllegalArgumentException("Points["+i+"] is null");
            }
        }
        Arrays.sort(points);
        //Check if there are duplicate elements
        for(int i=1;i<n;i++){
            if(points[i].compareTo(points[i-1])==0){
                throw new IllegalArgumentException("Points["+i+"] is same to Point["+(i-1)+"]");
            }
        }
        numSegments=0;
        lineSegmentList=new ArrayList<>();
        //Iterate all the points
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                for (int k=j+1;k<n;k++){
                    for (int q=k+1;q<n;q++){
                        //Check the four points in the same line
                        if(checkLine(points[i],points[j],points[k],points[q])){
                            //Put the 4 points to the point list
                            Point[] pointsSubList=new Point[4];
                            pointsSubList[0]=points[i];
                            pointsSubList[1]=points[j];
                            pointsSubList[2]=points[k];
                            pointsSubList[3]=points[q];
                            LineSegment seg=lineToSegment(pointsSubList);
                            //Add the segment to the result list
                            lineSegmentList.add(seg);
                            //Update the segment num value
                            numSegments+=1;
                        }

                    }
                }
            }
        }

        segments=new LineSegment[numSegments];
        //Copy the result to segment
        for(int i=0;i<numSegments;i++){
            segments[i]= lineSegmentList.get(i);
        }


    }

    /**
     * Check if these 4 points are in the same line
     * @param point1
     * @param point2
     * @param point3
     * @param point4
     * @return
     */
    private boolean checkLine(Point point1,Point point2,Point point3,Point point4){
        return  (point1.slopeTo(point2)==point1.slopeTo(point3))&&(point1.slopeTo(point3)==point1.slopeTo(point4));
    }


    /**
     * Find the smallest point and the biggest point and return the min->big segment
     * @param points
     * @return
     */
    private LineSegment lineToSegment(Point[] points){
        int size=points.length;
        Point maxPoint=points[0];
        Point minPoint=points[size-1];
        for(Point p:points){
            if(p.compareTo(maxPoint)==1){
                maxPoint=p;
            }
            if(p.compareTo(minPoint)==-1){
                minPoint=p;
            }
        }
        LineSegment lineSegment=new LineSegment(minPoint,maxPoint);
        return  lineSegment;
    }

    /**
     * the number of line segments
     * @return
     */
    public  int numberOfSegments()   {
        return numSegments;
    }

    /**
     * the line segments
     * @return
     */
    public LineSegment[] segments()  {
        return segments.clone();
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();


    }
}
