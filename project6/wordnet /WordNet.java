import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class WordNet {
    //private int numWords;
    private Digraph digraph;
    private Hashtable<String,ArrayList<Integer>> wordIndexSet;
    private Hashtable<Integer,ArrayList<String>> wordsList;
    private ArrayList<String> nouns;
    private final SAP sap;



    /**
     * constructor takes the name of the two input files,
     * WordNet digraph: each vertex v is an integer that represents a synset,
     * and each directed edge v→w represents that w is a hypernym of v.
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms){
        //numWords = 0;
        //String synsetsPath = synsets;
        //String hyperPath = hypernyms;
        long startTime = System.currentTimeMillis();
        wordIndexSet = new Hashtable<>();
        wordsList = new Hashtable<>();
        nouns = new ArrayList<>();
        In inSyn = new In(synsets);
        In inHyper = new In(hypernyms);
        //System.out.println("Time for loading part1:"+(System.currentTimeMillis()-startTime)+"ms");
        while (!inSyn.isEmpty()){
            String curLine = inSyn.readLine();
            String[] lineInfo =  curLine.split(",");
            String indexStr = lineInfo[0];
            int index = Integer.parseInt(indexStr);
            String wordstmp = lineInfo[1];
            //Word word = new Word(wordtmp,index);
            String[] wordsInfo = wordstmp.split(" ");
            ArrayList<String> tmpWordList = new ArrayList<String>();
            for(String s: wordsInfo){
                nouns.add(s);
                tmpWordList.add(s);
                // update the words-List<id>
                // if already in, get the hash set and append the new integer
                if(wordIndexSet.containsKey(s)){
                    ArrayList<Integer> wordSet = wordIndexSet.get(s);
                    wordSet.add(index);
                    //System.out.println(wordSet);
                    // update the hash set
                    wordIndexSet.replace(s,wordSet);
                } else {
                    ArrayList<Integer> wordSet = new ArrayList<>();
                    wordSet.add(index);
                    wordIndexSet.put(s,wordSet);
                }
            }
            wordsList.put(index,tmpWordList);



        }
        //System.out.println("Time for loading part2:"+(System.currentTimeMillis()-startTime)+"ms");
        // initial the new dia graph
        digraph = new Digraph(wordsList.size());
        // read from the sysnset
        while (!inHyper.isEmpty()){
            String curLine = inHyper.readLine();
            String[] lineInfoHyper = curLine.split(",");
            int targetIndex = Integer.parseInt(lineInfoHyper[0]);

            for(int i=1; i<lineInfoHyper.length; i++){
                int sourceIndex = Integer.parseInt(lineInfoHyper[i]);
                digraph.addEdge(targetIndex,sourceIndex);
            }

        }
        //System.out.println("V:"+digraph.V());
        //System.out.println("E:"+digraph.E());

        sap = new SAP(digraph);
        //System.out.println("Time for loading part3:"+(System.currentTimeMillis()-startTime)+"ms");

    }

    /**
     * GEt the wordIndex hashtable(key:hash,val:index)
     * @return
     */
    private Hashtable<String, ArrayList<Integer>> getWordIndexSet() {
        return wordIndexSet;
    }

    private int getNumWords() {
        return wordIndexSet.size();
    }

    /**
     * Get the connected graph
     * @return
     */
    private Digraph getDigraph() {
        return digraph;
    }



    /**
     *  returns all WordNet nouns
     * @return
     */
    public Iterable<String> nouns(){
        return wordIndexSet.keySet();
    }

    /**
     * is the word a WordNet noun?
     * @param word
     * @return
     */
    public boolean isNoun(String word){
        if (word == null){
            throw new IllegalArgumentException();
        } else {
            return wordIndexSet.containsKey(word);
        }
    }


    /**
     * distance between nounA and nounB (defined below)
     * @param nounA
     * @param nounB
     * @return
     */
    public int distance(String nounA, String nounB){
        // if both contains the val
        if(isNoun(nounA) && isNoun(nounB)){
                ArrayList aIndex = wordIndexSet.get(nounA);
                ArrayList bIndex = wordIndexSet.get(nounB);
                //SAP sap = new SAP(digraph)
                return sap.length(aIndex,bIndex);
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
     * in a shortest ancestral path (defined below)
     * @param nounA
     * @param nounB
     * @return
     */
    public String sap(String nounA, String nounB){
        // if both contains the val
        if(isNoun(nounA) && isNoun(nounB)){
            ArrayList aIndex = wordIndexSet.get(nounA);
            ArrayList bIndex = wordIndexSet.get(nounB);
            int index = sap.ancestor(aIndex,bIndex);
            ArrayList<String> words = wordsList.get(index);
            // Ref: https://stackoverflow.com/questions/1751844/java-convert-liststring-to-a-string
            return String.join(" ",words);
        } else {
            throw new IllegalArgumentException();
        }
    }



    public static void main(String[] args) {

        String syn = "/Users/futianshu/Desktop/普林斯顿算法/project6/src/synsets15.txt";
        String hyper = "/Users/futianshu/Desktop/普林斯顿算法/project6/src/hypernyms15Path.txt";
        long startTime = System.currentTimeMillis();
        WordNet wordNet = new WordNet(syn,hyper);
        //System.out.println(wordNet.getWordIndexSet());
        //System.out.println(wordNet.getDigraph());
        //.out.println("Time for loading whole constructor of wordnet:"+(System.currentTimeMillis()-startTime)+"ms");
       // System.out.println(wordNet.sap("a","b"));
        //System.out.println("Time for loading sap a:"+(System.currentTimeMillis()-startTime)+"ms");
        //System.out.println(wordNet.sap("palm_oil","pacifier"));
        System.out.println(wordNet.distance("e","f"));
        System.out.println(wordNet.sap("e","f"));
        //System.out.println("Time for loading sap b:"+(System.currentTimeMillis()-startTime)+"ms");


    }



}
