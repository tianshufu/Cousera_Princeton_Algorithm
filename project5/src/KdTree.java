
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {

    private  int size;
    private  Node rootNode;
    private  Node nearest;


    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int level;


        private Node(Point2D p, RectHV rect, Node lb, Node rt, int level) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.level = level;

        }

    }

    public KdTree(){
        size=0;
        //rootNode=new Node(null,null,null,null);

    }

    /**
     * is the  KdTree empty?
     * @return
     */
    public boolean isEmpty(){
        return size==0;
    }

    /**
     * number of points in the KdTree
     * @return
     */
    public int size(){
        return size;
    }

    /**
     * add the point to the KdTree(if it is not already in the tree)
     * @param p
     */
    public void insert(Point2D p){
        RectHV rootRect=new RectHV(0,0,1,1);
        //Check if the tree is empty
        if(isEmpty()){
            //Create the root node and the initial Rect
            rootNode=new Node(p,rootRect,null,null,1);
            size+=1;
        }
        else {
            if (!contains(p))
            {
                insertC(rootNode,p,1,rootRect);
                size+=1;
            }

        }

    }

    /**
     *  Helper function in insertion
     * @param root
     * @param p
     * @param level
     * @param rect
     * @return
     */
    private Node insertC(Node root,Point2D p, int level, RectHV rect){
        if(root == null){
            //Initial the original big box
            root = new Node(p,rect,null,null, level);
        }

        else {
            //left, right compare
            if(level%2!=0){
                if(p.x()<root.p.x()){
                    //Split the current rect and pass in the left part
                    RectHV leftRect = new RectHV(rect.xmin(),rect.ymin(),root.p.x(),rect.ymax());
                    root.lb = insertC(root.lb,p,level+1,leftRect);
                }
                else {
                    RectHV rightRect = new RectHV(root.p.x(),rect.ymin(),rect.xmax(),rect.ymax());
                    root.rt = insertC(root.rt,p,level+1,rightRect);
                }
            }
            //top, bottom compare
            else {
                if(p.y()<root.p.y()){
                    RectHV downRect = new RectHV(rect.xmin(),rect.ymin(),rect.xmax(),root.p.y());
                    root.lb = insertC(root.lb,p,level+1,downRect);
                }
                else {
                    RectHV upRect = new RectHV(rect.xmin(),root.p.y(),rect.xmax(),rect.ymax());
                    root.rt = insertC(root.rt,p,level+1,upRect);
                }
            }
        }

        return root;
    }

    /**
     * does the set contain point p?
     * @param p
     * @return
     */
    public boolean contains(Point2D p){
        if (p == null) {
            throw  new IllegalArgumentException("argument to contains()");
        }
        //Check if the kdTree is empty
        if(isEmpty()){
            return false;
        }
        else {
            return get(rootNode,p,1) != null;
        }

    }

    /**
     * Function to fetch element
     * @param cur
     * @param p
     * @return
     */
    private Node get(Node cur, Point2D p, int level){
        if(p == null){
            throw  new IllegalArgumentException("Argument null");
        }
        if(cur == null){
            return  null;
        }
        if(p.equals(cur.p)){
            return  cur;
        }
        //Right, left compare
        if(level%2 != 0){
            //if on the left side
            if(p.x()<cur.p.x()){
                return  get(cur.lb,p,level+1);
            }
            //On the right side
            else {
                return  get(cur.rt,p, level+1);
            }
        }
        // Up , down compare
        else {
            //if on the down side
            if(p.y()<cur.p.y()){
                return  get(cur.lb,p,level+1);
            }

            //Else in the upside
            else {
                return  get(cur.rt,p,level+1);
            }
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw(){
        // draw the big square
        drawSquare();
        //KdTree kdTree = new KdTree();
        //Get all the nodes
        Iterable<Node> nodes = levelOrderNode();
        for(Node n : nodes){
            // Extract infos from node
            int level = n.level;
            RectHV rect = n.rect;
            Point2D p = n.p;
            //Draw the points
            drawPoint(p);
            // if odd level, draw vertical red lines
            if(level%2!=0){
                drawVerticalLine(p.x(),rect.ymin(),p.x(),rect.ymax());
            }
            //if even ,draw horizontal black lines
            else {
                drawHorizontalLine(rect.xmin(),p.y(),rect.xmax(),p.y());
            }
        }


    }

    private void  drawSquare(){
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        RectHV rectHV=new RectHV(0.0,0.0,1.0,1.0);
        rectHV.draw();
    }

    /**
     * Draw point with radius = 0.01 , black
     */
    private void drawPoint(Point2D p){
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        p.draw();

    }

    /**
     *  Draw horizontal blue  line
     * @param xs
     * @param ys
     * @param xt
     * @param yt
     */
    private  void drawHorizontalLine(double xs, double ys, double xt, double yt){
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(xs,ys,xt,yt);
    }

    /**
     *  Draw vertical red line
     * @param xs
     * @param ys
     * @param xt
     * @param yt
     */
    private  void drawVerticalLine(double xs, double ys, double xt, double yt){
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(xs,ys,xt,yt);
    }

    /**
     * BFS , outputs the point format
     * @return
     */
    private  Iterable<Point2D> levelOrderPoint(){
        Queue<Node> queue = new Queue<>();
        Queue<Point2D> pointsList = new Queue<>();
        queue.enqueue(rootNode);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) {
                continue;
            }
            pointsList.enqueue(x.p);
            queue.enqueue(x.lb);
            queue.enqueue(x.rt);
        }
        return pointsList;
    }

    /**
     * BFS , outputs the node format
     * @return
     */
    private  Iterable<Node> levelOrderNode(){
        Queue<Node> queue = new Queue<>();
        Queue<Node> nodesList = new Queue<>();
        queue.enqueue(rootNode);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) {
                continue;
            }
            nodesList.enqueue(x);
            queue.enqueue(x.lb);
            queue.enqueue(x.rt);
        }
        return nodesList;
    }




    /**
     * all points that are inside the rectangle (or on the boundary)
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect){
        //private TreeSet<Point2D> pointsInRect
        if(rect == null){
            throw  new  IllegalArgumentException("argument to contains()");
        }
        //if no points
        if ( rootNode == null){
            return  null;
        }
        Queue<Point2D> pointsInRect = new Queue<>();
        rangeCheck(rootNode,rect,pointsInRect);
        return pointsInRect;
    }




    /**
     * Recursely check the nodes
     * @param cur
     * @param rect
     */
    private void rangeCheck (Node cur , RectHV rect ,Queue pointsInRect){
        if (cur == null) {
            return;
        }
        //System.out.println(cur.p);
        //System.out.println(cur.p);
        if(rect.contains(cur.p)){
                pointsInRect.enqueue(cur.p);
        }
        //if the cur node rect intersects with the target rect
        if (cur.rect.intersects(rect)){
            if (cur.lb != null){
                    //check if the left or down side intersect with the rect
                    if (getRectange(cur, 0 ).intersects(rect)){
                        rangeCheck(cur.lb,rect,pointsInRect);
                    }
                }


            }

            if (cur.rt != null){
                if (getRectange(cur, 1 ).intersects(rect)){
                    rangeCheck(cur.rt,rect,pointsInRect);
                }
            }
    }



    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * @param p
     * @return
     */
    public  Point2D nearest(Point2D p){
        if (p == null){
            throw  new IllegalArgumentException();
        }

        if (rootNode.p == null){
            return  null;
        }

        nearest = rootNode;
        return  nodeSearch(rootNode,p,rootNode).p;
        //return  nearest.p;
    }

    private  Node nodeSearch(Node n, Point2D p , Node champion){
        if(n == null){
            return  champion;
        }

        //Update the champion if needed
        Double dist = n.p.distanceTo(p);
        if (dist < champion.p.distanceTo(p)){
            champion = n;
        }

        if (champion.p.distanceTo(p) < nearest.p.distanceTo(p)){
            nearest = champion;
        }


        System.out.println("Cur node: "+n.p+"Champ node:"+champion.p);
        //if left , right side
        if (n.level%2 != 0){
            // if p on the left side of the node , check left side anyway
            if(p.x() < n.p.x()){
                // search the left side
                Node tmpChampion = nodeSearch(n.lb,p,champion);
                //get the right rec
                RectHV rightRect = getRectange(n , 1);
                // if there might be closer point in the right rec
                if (tmpChampion.p.distanceTo(p)  > rightRect.distanceTo(p)){
                    // Search the right rec
                    return  nodeSearch(n.rt,p,tmpChampion);
                }

                return tmpChampion;

            }

            //if p in the right side of the node check the right side
            else{
                //search the right side
                    Node tmpChampion = nodeSearch(n.rt,p,champion);
                    //get the left rec
                    RectHV leftRect = getRectange(n ,0);
                    //System.out.println(leftRect);
                    if(tmpChampion.p.distanceTo(p) > leftRect.distanceTo(p)){
                        return nodeSearch(n.lb,p,tmpChampion);
                    }

                    return  tmpChampion;


            }

        }
        // if up, down side
        else {
            //check the  down side
            if(p.y() < n.p.y()){
                // search the down side
                Node tmpChampion = nodeSearch(n.lb,p,champion);
                //get the up rec
                RectHV topRect = getRectange(n , 1);
                // if there might be closer point in the right rec
                if (tmpChampion.p.distanceTo(p)  > topRect.distanceTo(p)){
                        nodeSearch(n.rt,p,tmpChampion);
                }
                return  tmpChampion;
            }
            //if p in the upside of the node check the down side
            else{
                //search the up side
                    Node tmpChampion = nodeSearch(n.rt,p,champion);
                    //get the down rec
                    RectHV downRect = getRectange(n ,0);
                    if(tmpChampion.p.distanceTo(p) > downRect.distanceTo(p)) {
                        nodeSearch(n.lb, p, tmpChampion);
                    }

                    return tmpChampion;


            }


        }


    }




    /**
     * Get the require part rectangle, choose==0 : l or b    choose==1 : r or t depend on the level
     * @param n
     * @param choose
     * @return
     */
    private RectHV getRectange(Node n, Integer choose){
        if (n == null){
            throw  new IllegalArgumentException();
        }
        //get the big rec
        RectHV nodeRect = n.rect;
        //if left , right side
        if (n.level%2 !=0 ){
            //left rec
            if (choose == 0 ){
                return  new RectHV(nodeRect.xmin(),nodeRect.ymin(),n.p.x(),nodeRect.ymax());
            }
            // if right side
            if(choose ==1 ){
                return  new RectHV(n.p.x(),nodeRect.ymin(),nodeRect.xmax(),nodeRect.ymax());
            }

        }
        //if up, down side
        else {
            //if down side
            if( choose ==0){
                return  new RectHV(nodeRect.xmin(),nodeRect.ymin(),nodeRect.xmax(),n.p.y());
            }
            //if upside
            else {
                return  new RectHV(nodeRect.xmin(),n.p.y(),nodeRect.xmax(),nodeRect.ymax());
            }

        }
        return  null;

    }

    @Override
    public   String toString(){
        String s = " ";
        Iterable<Node> nodes = levelOrderNode();
        for (Node n : nodes){
            s+="Point: "+n.p + "Level:" + n.level + "Square:" + n.rect+"\n";
            //System.out.println("Point: "+n.p + "Level:" + n.level + "Square:" + n.rect);
            //System.out.println(n.rect);
        }

        return s;
    }

    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        KdTree kdTree = new KdTree();
        Point2D p1 = new Point2D(0.7,0.2);
        Point2D p2 = new Point2D(0.5,0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4,0.7);
        Point2D p5 = new Point2D(0.9,0.6);
        //Point2D p6 = new Point2D(0.875, 0.625);
        //Point2D p7 = new Point2D(0.9 ,0.6);
        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p3);
        kdTree.insert(p4);
        kdTree.insert(p5);

        //RectHV r1 = new RectHV(0.82,0.66,0.86,0.79);
        //System.out.println(kdTree.range(r1));
        System.out.println(kdTree.nearest(new Point2D(0.183, 0.51)));
        //kdTree.draw();
        //r1.draw();
        //System.out.println(r1);
        //System.out.println(kdTree);
        /**
        for(int i=0;i<= 10;i++){
            Double x =  StdRandom.random();
            Double y =  StdRandom.random();
            kdTree.insert(new Point2D(x,y));
            pointSET.insert(new Point2D(x,y));
        }
        r1 = new RectHV(0.0,0.0,0.25,0.25);
        System.out.println(kdTree.range(r1));
        System.out.println(pointSET.range(r1));

        //System.out.println(kdTree.range(r1));
        //System.out.println(pointSET.range(r1));
        //System.out.println(kdTree.contains(p7));
         **/
        //kdTree.insert(p7);

        //System.out.println(kdTree.size);
        //System.out.println(kdTree.nearest(p7));
        //System.out.println(pointSET.);

        //Iterable<Node> nodes = kdTree.levelOrderNode();
        //RectHV rect1 = new RectHV(0.85,0.55,0.95, 0.65);
        //System.out.println(kdTree.range(rect1));
        //System.out.println(rect1.contains(p5));
        //kdTree.getRectange(Node(p1),0);

        //System.out.println(kdTree);

        //System.out.println(kdTree.size());
        //System.out.println();
        //System.out.println(kdTree.rootNode.p);
        //System.out.println(kdTree.rootNode.lb.p);
        //System.out.println(kdTree.rootNode.lb.lb.p);
        //Node tmpNode=kdTree.rootNode.lb;
        //System.out.println(kdTree.rootNode.rt.p);
       // System.out.println(kdTree.rootNode.lb.lb.lb.p);

    }




















}
