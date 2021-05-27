import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


import java.util.ArrayList;


public class BoggleSolver {
    private final Trie treeDic;
    private ArrayList<Pos> directs;
    private ArrayList<String> validsWords;


    private class Pos{
        int x;
        int y;
        private Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
        private int getX(){
            return x;
        }
        private int getY(){
            return y;
        }
    }

    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
     * @param dictionary
     */
    public BoggleSolver(String[] dictionary){
        // initial the directions
        directs = new ArrayList<>();
        directs.add(new Pos(-1,-1));
        directs.add(new Pos(-1,0));
        directs.add(new Pos(-1,1));
        directs.add(new Pos(0,-1));
        directs.add(new Pos(0,1));
        directs.add(new Pos(1,-1));
        directs.add(new Pos(1,0));
        directs.add(new Pos(1,1));
        // create the trie tree and put the value into the trie tree
        treeDic = new Trie();
        for(String s: dictionary){
            treeDic.insert(s);
        }


    }



    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable
     * @param board
     * @return
     */
    public Iterable<String> getAllValidWords(BoggleBoard board){
        // initial the ans list to store all the valid words
        findAllPoints(board);
        return validsWords;
    }

    /**
     * Check whether the point is in the board
     * @param i x coordinate in the board
     * @param j y coordinate in the board
     * @param w width of the board
     * @param h height of the board
     * @return
     */
    private boolean isIn(int i,int j, int w, int h){
        return i >= 0 && i < w && j >=0 && j < h;
    }

    /**
     * Function recursive to check whether a word is in the board
     * @param boggleBoard the boggle object
     * @param i   the row of the pos
     * @param j   the col of the pos
     * @param word  the word to check
     * @param index the cur index of the word to compare with the (i,j) point on the board
     * @param visited  boolean board to check whether this point has been visited or not
     * @return
     */
    private boolean existsRecursive(BoggleBoard boggleBoard,int i,int j,String word,int index, boolean[][] visited){

        // check whether i,j is valid or not, check wheher i,j has been visited or not
        if(!isIn(i,j,boggleBoard.rows(),boggleBoard.cols()) || visited[i][j]){
            return false;
        }
        // check whether the value is equal or not
        if(boggleBoard.getLetter(i,j) != word.charAt(index)){
            return false;
        }

        // check whether reached to the end
        if(index == word.length()-1){
            return true;
        }

        // if not, keep iterating to find the result
        visited[i][j] = true;
        // try all the eight directions
        for(Pos pos: directs){
            int dirX = pos.getX();
            int dirY = pos.getY();
            boolean dir = existsRecursive(boggleBoard,i+dirX,j+dirY,word,index+1,visited);
            // if any path has been found, return true to the previous level
            if(dir){
                return true;
            }
        }
        // if reached to this part, means the all directions got 'false', then set false to the point
        visited[i][j] = false;
        return false;

    }

    /**
     * Find all points in the grid
     * @param boggleBoard
     */
    private void findAllPoints(BoggleBoard boggleBoard){
        validsWords = new ArrayList<>();
        int m = boggleBoard.rows();
        int n = boggleBoard.cols();
        for( int i = 0; i < m; i++){
            for( int j = 0; j< n; j++){
                findCurPosWords(boggleBoard,i,j,new boolean[boggleBoard.rows()][boggleBoard.cols()],treeDic.root);
            }
        }
    }

    /**
     * Find all the possible words for the cur dic
     * @param boggleBoard
     * @param i
     * @param j
     * @param visited
     * @return
     */
    private void findCurPosWords(BoggleBoard boggleBoard, int i, int j, boolean[][] visited, Trie.TrieNode trieNode){
       // validate the info of the board
        if (!isIn(i,j,boggleBoard.rows(),boggleBoard.cols()) || visited[i][j]){
            return;
        }
        char c = boggleBoard.getLetter(i,j);
        if (trieNode.tns[c-'A'] == null){
            return;
        }

        // move to the cur node
        trieNode = trieNode.tns[c-'A'];
        if(trieNode.end){
            if(trieNode.val.length() > 2 && !validsWords.contains(trieNode.val)){
                //System.out.println(trieNode.val);
                validsWords.add(trieNode.val);
            }
            // 'delete' the word in the trie, in case of searching again
            //trieNode.end = false;
        }
        // update the visited
        visited[i][j] = true;
        // recursive check it's neighbours
        for(Pos pos: directs){
            int dirX = pos.getX();
            int dirY = pos.getY();
            findCurPosWords(boggleBoard,i+dirX,j+dirY,visited,trieNode);
        }
        // reset the the visited
        visited[i][j] = false;
    }



    /**
     * Check if the word is in the boggle board
     * @param boggleBoard
     * @param word
     * @return
     */
    private boolean isWordIn(BoggleBoard boggleBoard,String word){
        // iterate all the start in the board
        for(int i = 0; i < boggleBoard.rows(); i++){
            for (int j = 0; j < boggleBoard.cols(); j++){
                // create a visited 2d array
                if (existsRecursive(boggleBoard,i,j,word,0,new boolean[boggleBoard.rows()][boggleBoard.cols()])){
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * Returns the score of the given word if it is in the dictionary, zero otherwise.
     * (You can assume the word contains only the uppercase letters A through Z.)
     * word length	  	points
     * 3–4		1
     * 5		2
     * 6		3
     * 7		5
     * 8+		11
     * @param word
     * @return
     */
    public int scoreOf(String word){
        if(!treeDic.search(word)){
            return 0;
        }
        switch (word.length()){
            case 0: return 0;
            case 1: return 0;
            case 2: return 0;
            case 3: return 1;
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 7: return 5;
            default: return 11;
        }

    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        //BoggleBoard board = new BoggleBoard(args[1]);
        char[][] a =  {
                { 'M', 'O', 'O', 'U' },
                { 'N', 'O', 'S', 'D' },
                { 'M', 'H', 'R','D' },
                { 'Y', 'A', 'G', 'I' }
        };

        char[][] b = {
                {'U','M','N'},
                {'E','F','D'},
                {'T','A','E'},
        };

        char[][] c = {
                {'S','A','S','R','G'},
                {'T','O','T','L','S'},
                {'C','C','O','O','F'},
                {'T','R','T','D','I'},
                {'O','N','E','E','Y'},
        };
        BoggleBoard board = new BoggleBoard(a);
        solver.getAllValidWords(new BoggleBoard(a));
        //solver.findCurPosWords(board,1,0,new boolean[board.rows()][board.cols()],solver.treeDic.root);
        System.out.println(solver.validsWords);
        System.out.println(solver.getAllValidWords(board));
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word +":  "+solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        //System.out.println(solver.scoreOf("QUINCE"));
    }





}
