import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.List;

/**
 * Outcast detection. Given a list of WordNet nouns x1, x2, ..., xn,
 * which noun is the least related to the others? To identify an outcast,
 * compute the sum of the distances between each noun and every other one:
 * di   =   distance(xi, x1)   +   distance(xi, x2)   +   ...   +   distance(xi, xn)
 * and return a noun xt for which dt is maximum. Note that distance(xi, xi) = 0,
 * so it will not contribute to the sum.
 */
public class Outcast {
    private WordNet wordNet;
    private List<String> wordsList;
    private int numWords;
    /**
     * constructor takes a WordNet object
     * @param wordNet
     */
    public Outcast(WordNet wordNet){
        this.wordNet = wordNet;
        //this.wordsList = wordNet.getWordsList();
        //this.numWords = wordNet.getNumWords();

    }

    /**
     * given an array of WordNet nouns, return an outcast
     * return a noun xt for which dt is maximum
     * @param nouns
     * @return
     */
    public String outcast(String[] nouns){
        int maxDist = Integer.MIN_VALUE;
        String ansNoun = "";
        int nounNums = nouns.length;
        for(int i = 0; i<nounNums;i++){
            // get the ith nouns
            int dist = 0;
            for(int j=0 ; j<nounNums;j++){
                if(j!=i){
                    if(wordNet.isNoun(nouns[i]) && wordNet.isNoun(nouns[j])){
                        dist +=  calculateDistance(nouns[i],nouns[j]);
                    } else {
                        //System.out.println("Not in :"+nouns[i]);
                    }

                }
            }
            //System.out.println("Noun:"+nouns[i]+" Dist:"+dist);
            // compare to the maxDist
            if(dist > maxDist){
                ansNoun = nouns[i];
                maxDist = dist;
            }
        }
        return ansNoun;
    }

    /**
     * Calculate the distance between two nouns
     * @param noun1
     * @param noun2
     * @return
     */
    private int calculateDistance(String noun1, String noun2){
        if(wordNet.isNoun(noun1) && wordNet.isNoun(noun2)){
            return wordNet.distance(noun1,noun2);
        } else {
            //System.out.println("Noun1:"+noun1+" Noun2:"+noun2);
            throw  new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        //System.out.println(wordnet.getNumWords());
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }

    }


}
