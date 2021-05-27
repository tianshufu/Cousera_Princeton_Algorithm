import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private  int numSegments=0;
    private ArrayList<LineSegment> lineSegmentList;
    private LineSegment[] segments;


    /**
     * finds all line segments containing 4 or more points
     * @param points
     */
    public FastCollinearPoints(Point[] points)  {
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
        lineSegmentList=new ArrayList<>();
        Arrays.sort(points);
        //Check if there are duplicate elements
        for(int i=1;i<n;i++){
            if(points[i].compareTo(points[i-1])==0){
                throw new IllegalArgumentException("Points["+i+"] is same to Point["+(i-1)+"]");
            }
        }
        for (int i=0;i<n;i++){
            Arrays.sort(points);
            Point p=points[i];
            //Only focus on the i->n part of the array
            Arrays.sort(points,i,n, p.slopeOrder());
            int count=0;
            Point start=points[i];
            Point end=null;
            for(int j=i+1;j<n-1;j++){
                Point cur=points[j];
                Point next=points[j+1];
                if(cur.slopeTo(p)==next.slopeTo(p)){
                    count+=1;
                    end=next;
                }

                else if(count>=2){
                    if ((i>0)&&(points[i].slopeTo(points[i-1])==points[i].slopeTo(end))){
                        count=0;
                    }
                    else {
                        numSegments+=1;
                        lineSegmentList.add(new LineSegment(start,end));
                        count=0;
                    }

                }
                else {
                    count=0;
                }


            }
            if(count>=2){
                if ((i>0)&&(points[i].slopeTo(points[i-1])==points[i].slopeTo(end))){
                    count=0;
                }
                else {
                    numSegments+=1;
                    lineSegmentList.add(new LineSegment(start,end));
                    count=0;
                }
            }
        }
        //  Copy the result to the segments
        segments=new LineSegment[numSegments];
        for (int i=0;i<numSegments;i++){
            segments[i]=lineSegmentList.get(i);
        }


    }

    /**
     * the number of line segments
     * @return
     */
    public  int numberOfSegments()  {
        return  numSegments;
    }

    /**
     * the line segments
     * @return
     */
    public LineSegment[] segments()   {
        return segments.clone();

    }

    private  String  pointsToString(Point[] points){
        String s="";
        for(Point p:points){
            s+=p.toString();
            s+="->";
        }
        return  s;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println(collinear.lineSegmentList);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
