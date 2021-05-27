import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;



public class BurrowsWheeler {

    /**
     * apply Burrows-Wheeler transform,
     * reading from standard input and writing to standard output
     */
    public static void transform(){
        while (!BinaryStdIn.isEmpty()){
            String curStr = BinaryStdIn.readString();
            int length = curStr.length();
            CircularSuffixArray circularSuffixArray = new CircularSuffixArray(curStr);
            // find the 'first' and write the output
            for (int i = 0; i < length; i++){
                if(circularSuffixArray.index(i) == 0){
                    // write as an int
                    BinaryStdOut.write(i);
                }
            }
            for(int i = 0; i < length;i++){
                // get the index of the sorted suffix, exp: 0 -> 11 -> 'A'
                int org_i = circularSuffixArray.index(i);
                // get the offset of the 'start' th suffixes last char
                int offset = (org_i+length-1)%length;
                // get the char from the the string
                char c = curStr.charAt(offset);
                BinaryStdOut.write(c,8);
            }

            BinaryStdOut.close();

        }

    }


    /**
     * apply Burrows-Wheeler inverse transform,
     * reading from standard input and writing to standard output
     */
    public static void inverseTransform(){
        // define the max val
        final int max_index = 256;
        // read the int
        int first = BinaryStdIn.readInt();
        StringBuilder curStr = new StringBuilder();
        while(!BinaryStdIn.isEmpty()){
            char cur = BinaryStdIn.readChar();
            curStr.append(cur);
        }
        int wordLength = curStr.length();
        // get the count of each char
        int[] count = new int[max_index+1];
        for(int i=0 ;i < wordLength;i++){
            count[curStr.charAt(i)+1]++;
        }

        for (int i = 0; i < max_index; i++) {
            count[i + 1] += count[i];
        }

        // generate next array
        int[] next = new int[wordLength];
        for (int i = 0; i < wordLength; i++) {
            char c = curStr.charAt(i);
            next[count[c]] = i;
            count[c]++;
        }

        // output
        for (int i = 0; i < wordLength; i++) {
            BinaryStdOut.write(curStr.charAt(next[first]));
            first = next[first];
        }
        BinaryStdOut.close();


    }

    /**
     * if args[0] is "-", apply Burrows-Wheeler transform
     * if args[0] is "+", apply Burrows-Wheeler inverse transform
     * @param args
     */
    public static void main(String[] args) {
        String sign = args[0];
        if (sign.equals("-")) {
            transform();
        } else if (sign.equals("+")){
            inverseTransform();
        }

    }


}
