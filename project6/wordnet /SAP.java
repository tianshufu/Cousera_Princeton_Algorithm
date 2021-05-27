import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class SAP {
    private Digraph G;

    /**
     * constructor takes a digraph (not necessarily a DAG)
     * @param G
     */
    public SAP(Digraph G){
        //breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(G);
        this.G = new Digraph(G);
    }

    /**
     * length of shortest ancestral path between v and w; -1 if no such path
     * @param v
     * @param w
     * @return
     */
    public int length(int v, int w){
        if(validate(v) && validate(w)){
            BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G,v);
            BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G,w);
            // get the public ancestor
            int ancestor = ancestor(v,w);
            if (ancestor>=0){
                // get the length
                return pathV.distTo(ancestor)+pathW.distTo(ancestor);
            }
            return -1;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     * @param v
     * @param w
     * @return
     */
    public int ancestor(int v, int w){
        if(validate(v) && validate(w)){
            BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G,v);
            BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G,w);
            return  getAncestor(pathV,pathW);
        } else {
            throw new IllegalArgumentException();
        }

    }


    /**
     * length of shortest ancestral path between any vertex in v and
     * any vertex in w; -1 if no such path
     * @param v
     * @param w
     * @return
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if(validIterable(v) && validIterable(w)){
            BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G,v);
            BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G,w);
            int ancestor = ancestor(v,w);
            if(ancestor >=0 ){
                return pathV.distTo(ancestor)+pathW.distTo(ancestor);
            }
            return -1;
        } else {
            throw  new IllegalArgumentException();
        }

    }

    private boolean validate(int tmp){
        return tmp >= 0 && tmp <= G.V();
    }


    /**
     * Check weather the length is null or not
     * @param tmp
     * @return
     */
    private boolean validIterable(Iterable<Integer> tmp){
        if( tmp ==null){
            return false;
        } else {
            for (Integer t: tmp){
                // if either the element is null or not valid, returns false
                if(t == null || validate(t)){
                    return false;
                }
            }
        }
        return true;
    }



    /**
     * a common ancestor that participates in shortest
     * ancestral path; -1 if no such path
     * @param v
     * @param w
     * @return
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if(validIterable(v) && validIterable(w)){
            BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(G,v);
            BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(G,w);
            return getAncestor(pathV,pathW);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Calculate the smallest ancestor among two points or two lists of points
     * @param pathV
     * @param pathW
     * @return
     */
    private int getAncestor(BreadthFirstDirectedPaths pathV, BreadthFirstDirectedPaths pathW){
        int minDist = Integer.MAX_VALUE;
        int ancestor = -1 ;
        // Go through all possible points
        for(int j=0; j<G.V() ; j++){
            // if both the point can reach to the point j
            if(pathV.hasPathTo(j) && pathW.hasPathTo(j)){
                int curDist = pathV.distTo(j)+pathW.distTo(j);
                if(curDist < minDist){
                    minDist = curDist;
                    ancestor = j;
                }
            }
        }
        return ancestor;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = 0;
        int w = 3;
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        int v1 = 0;
        int w1 = 3;
        int length1   = sap.length(v1, w1);
        int ancestor1 = sap.ancestor(v1, w1);
        StdOut.printf("length = %d, ancestor = %d\n", length1, ancestor1);


    }














}

