import java.util.ArrayList;
import java.util.Collections;

/**
 * To efficiently implement the key component in the Burrows–Wheeler transform,
 * you will use a fundamental data structure known as the circular suffix array,
 * which describes the abstraction of a sorted array of the n circular suffixes of a string of length n.
 * As an example, consider the string "ABRACADABRA!" of length 12.
 * The table below shows its 12 circular suffixes and the result of sorting them.
 */

public class CircularSuffixArray {

    private int length;
    private ArrayList<Character> wordsList;
    private ArrayList<CircularSuffix> suffixList;

    /**
     * CircularSuffix that represents a circular suffix implicitly
     * (via a reference to the input string and a pointer to the first character in the circular suffix).
     */
    private class CircularSuffix implements Comparable<CircularSuffix>{
        private int start;
        private int suffixLength;

        private CircularSuffix(int startIndex){
            this.start = startIndex;
            this.suffixLength = length;
        }

        private int getStart() {
            return start;
        }

        @Override
        public int compareTo(CircularSuffix circularSuffix){
            for(int i = 0; i < suffixLength; i++){
                int curIndex = (start+i)%suffixLength;
                int otherIndex = (circularSuffix.getStart()+i)%suffixLength;
                Character curChar = wordsList.get(curIndex);
                Character otherChar = wordsList.get(otherIndex);
                // start of compare
                if(curChar > otherChar){
                    return 1;
                } else if(curChar < otherChar){
                    return  -1;
                }

            }
            return 0;
        }

        @Override
        public String toString(){
            String org = "";
            for(int i = 0; i < suffixLength; i++){
                int curIndex = (start+i)%suffixLength;
                org += wordsList.get(curIndex);
            }
            return org;
        }




    }


    /**
     * circular suffix array of s
     */
    public CircularSuffixArray(String s){
        if (s == null){
            throw new IllegalArgumentException();
        }
        // create a new array
        wordsList = new ArrayList<>();
        length = s.length();
        for(int i = 0; i< length; i++){
            wordsList.add(s.charAt(i));
        }
        suffixList = new ArrayList<>();
        for(int i = 0; i< length;i++){
            suffixList.add(new CircularSuffix(i));
        }
        Collections.sort(suffixList);
        //System.out.println(wordsList);
        //System.out.println(suffixList);

    }



    /**
     *  length of s
     * @return
     */
    public int length(){
        return length;
    }



    /**
     * returns index of ith sorted suffix
     * @param i
     * @return
     */
    public int index(int i){
        if(i < 0 || i >= length){
            throw new IllegalArgumentException();

        }
        return suffixList.get(i).getStart();
    }

    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(circularSuffixArray.index(11));
        //circularSuffixArray

    }







}
