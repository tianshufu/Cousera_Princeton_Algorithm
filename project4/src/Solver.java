import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {

    /**
     * Store the nodes which has already been accessed
     */
    //private List<Board> closedBoards;
    //private List<Board> twinClosedBoards;
    //private final MinPQ<Node> que;
    //private MinPQ<Node> twinque;
    private final Stack<Board> ansList;
    private  boolean isSolvable;


    /**
     * find a solution to the initial board (using the A* algorithm)
     * @param initial
     */
    public Solver(Board initial){
         if(initial==null){
             throw new IllegalArgumentException();
         }
         //closedBoards=new ArrayList<Board>();
         //twinClosedBoards=new ArrayList<>();
         ansList=new Stack<>();
         MinPQ<Node>que=new MinPQ<>();
         MinPQ<Node>twinque=new MinPQ<>();
         isSolvable=true;
         //Construct the node by initial
         Node initialNode=new Node(initial,initial.manhattan(),0,null);
         //Construct the twin node of the initial board
         Board twinBoard=initialNode.board.twin();
         Node initialTwinNode=new Node(twinBoard,twinBoard.manhattan(),0,null);
         //Add the initial node to the que
         que.insert(initialNode);
         //Add the twin node to the twin que
         twinque.insert(initialTwinNode);
         while(!que.isEmpty()){
             //Get the minNode from the que
             Node curNode=que.delMin();
             //Get the twin Node
             if(!twinque.isEmpty()){
                 Node curTwinNode=twinque.delMin();
                 //System.out.println("Twin board:"+curTwinNode.board);
                 if(curTwinNode.board.isGoal()){
                     isSolvable=false;
                     return;
                 }
                 else {
                     //Deal with the twin boards
                     //Add the curtwin to the went over list
                     //twinClosedBoards.add(curTwinNode.board);
                     Iterable<Board> twinNeighbours=curTwinNode.board.neighbors();
                     //System.out.println(twinNeighbours);

                     //Iterate the twin neighbours
                     for(Board twnb:twinNeighbours){
                         if((curTwinNode.parent!=null)&&(twnb.equals(curTwinNode.parent.board))){

                         }
                         else {
                             Node newNode=new Node(twnb,twnb.manhattan(),curTwinNode.steps+1,curTwinNode);
                             twinque.insert(newNode);
                         }
                     }
                 }
             }

             //Check if the node is the target
             if(curNode.board.isGoal()){
                 makeAns(curNode);
                 return;
             }
             else {
                 //Add the node to the closed Nodes list
                 //closedBoards.add(curNode.board);
                 //Get the neighbours
                 Iterable<Board> curNeighbours=curNode.board.neighbors();
                 //Iterate the neibours
                 for(Board nb:curNeighbours){
                     //If the neighbour has not been visited
                     if((curNode.parent!=null)&&(nb.equals(curNode.parent.board))){

                     }
                     else {
                         Node newNode=new Node(nb,nb.manhattan(),curNode.steps+1,curNode);
                         que.insert(newNode);
                     }
                 }


             }

         }

    }

    /**
     * is the initial board solvable? (see below)
     * @return
     */
    public boolean isSolvable(){
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        return ansList.size()-1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if(isSolvable()){
            return ansList;
        }
        else {
            return null;
        }

    }

    /**
     * Create the ansList according to the node
     * @param node
     */
    private void makeAns(Node node){
        while (node!=null){
            ansList.push(node.board);
            node=node.parent;
        }

    }

    private static class Node implements Comparable<Node>{
        private final   Board board;
        private  final int manDist;
        private  final int steps;
        private  final Node parent;

        private Node(Board board, int manDist,int steps, Node parent){
            this.board=board;
            this.manDist=manDist;
            this.parent=parent;
            this.steps=steps;
        }


        @Override
        public int compareTo(Node o) {
            //return this.huVal-o.huVal;
            return (this.steps+this.manDist)-(o.steps+o.manDist);
        }

        @Override
        public String toString() {
            return board.toString();
        }
    }

    // test client (see below)
    public static void main(String[] args){
        MinPQ<Node> que=new MinPQ<Node>();
        int tiles[][];
        tiles=new int[3][3];
        tiles[0][0]=1;
        tiles[0][1]=2;
        tiles[0][2]=3;
        tiles[1][0]=4;
        tiles[1][1]=5;
        tiles[1][2]=6;
        tiles[2][0]=8;
        tiles[2][1]=7;
        tiles[2][2]=0;

        int tiles2[][];
        tiles2=new int[3][3];
        tiles2[0][0]=0;
        tiles2[0][1]=1;
        tiles2[0][2]=3;
        tiles2[1][0]=4;
        tiles2[1][1]=2;
        tiles2[1][2]=5;
        tiles2[2][0]=7;
        tiles2[2][1]=8;
        tiles2[2][2]=6;


        Board board=new Board(tiles);
        Board board2=new Board(tiles2);
        //que.insert(new Node(board,board.manhattan(),0,null));
        //que.insert(new Node(board2,board2.manhattan(),0,null));
        //System.out.println(que.min().toString());
        Solver solver=new Solver(board2 );
        System.out.println(solver.isSolvable());
        System.out.println(solver.solution());
        System.out.println("Mininum moves:"+solver.moves());


    }

}
