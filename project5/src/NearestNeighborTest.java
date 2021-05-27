import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;

public class NearestNeighborTest {
    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        //initial test point
        Point2D p1 = new Point2D(0.0,0.5);
        System.out.println("Kdtree nearset: "+kdtree.nearest(p1)+"dist: "+kdtree.nearest(p1).distanceTo(p1));
        System.out.println("Brute nearset:"+brute.nearest(p1)+"dist: "+brute.nearest(p1).distanceTo(p1));

    }



}
