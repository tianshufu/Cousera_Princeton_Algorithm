import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import  java.util.TreeSet;


public class PointSET {

     private final TreeSet<Point2D> pointSet;

    /**
     * construct an empty set of points
     */
    public  PointSET(){
        pointSet= new TreeSet<>();

    }

    /**
     *  is the set empty?
     * @return
     */
    public boolean isEmpty() {

        return  pointSet.isEmpty();
    }

    /**
     * number of points in the set
     * @return
     */
    public  int size()  {
        return  pointSet.size();
    }

    /**
     * add the point to the set (if it is not already in the set)
      * @param p
     */
    public  void insert(Point2D p) {
        if(p==null){
            throw  new IllegalArgumentException();
        }
        //Check if the point is in the set
        if(!pointSet.contains(p)){
            pointSet.add(p);
        }

    }

    /**
     * / does the set contain point p?
     * @param p
     * @return
     */
    public  boolean contains(Point2D p) {
        if(p==null){
            throw  new IllegalArgumentException();
        }
        return  pointSet.contains(p);
    }

    /**
     *  draw all points to standard draw
     */
    public  void draw()  {
        for(Point2D p:pointSet){
            p.draw();
        }

    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect){
        if(rect==null){
            throw  new IllegalArgumentException();
        }
        TreeSet<Point2D> pointSetIn=new TreeSet<>();
        for(Point2D p: pointSet){
            if(rect.contains(p)){
                pointSetIn.add(p);
            }

        }
        return pointSetIn;

    }

    /**
     *  a nearest neighbor in the set to point p; null if the set is empty
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p)  {
        if(p==null){
            throw  new IllegalArgumentException();
        }
        Double minDist=Double.POSITIVE_INFINITY;
        Point2D pNearest=null;
        for(Point2D p0: pointSet){
            //Update the nearest
            Double tmpDist=p.distanceTo(p0);
            if(tmpDist<minDist){
                minDist=tmpDist;
                pNearest=p0;

            }
        }

        return pNearest;

    }

    @Override
    public String toString(){
        String ans="";
        for(Point2D p:pointSet){
            ans+=p.toString();
        }
        return  ans;
    }



    public static void main(String[] args)   {
        PointSET pointSET=new PointSET();
        Point2D p1 = new Point2D(0.1,0.2);
        Point2D p2 = new Point2D(0.24,0.4);
        Point2D p3 = new Point2D(0.3,0.3);
        Point2D p4 = new Point2D(-1,-1);
        RectHV r1=new RectHV(0,0,0.5,0.5);
        //System.out.println(r1.distanceSquaredTo(p2));
        pointSET.insert(p1);
        pointSET.insert(p2);
        pointSET.insert(p3);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        pointSET.draw();
        //System.out.println(pointSET.nearest(p4));
        p3.draw();
        r1.draw();
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(0.2,0.2,0.3,0.3);
        //System.out.println(pointSET.toString());
    }               // unit testing of the methods (optional)
}
