import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

/**
 * Move-to-front encoding. Your task is to maintain an ordered sequence of the 256 extended ASCII characters.
 * Initialize the sequence by making the ith character in the sequence equal to the ith extended ASCII character.
 * Now, read each 8-bit character c from standard input, one at a time; output the 8-bit index in the sequence where c appears;
 * and move c to the front.
 *
 * Move-to-front decoding. Initialize an ordered sequence of 256 characters,
 * where extended ASCII character i appears ith in the sequence.
 * Now, read each 8-bit character i (but treat it as an integer between 0 and 255) from standard input one at a time;
 * write the ith character in the sequence; and move that character to the front.
 * Check that the decoder recovers any encoded message.
 */

public class MoveToFront {

    private static LinkedList<Character> generateSequence(){
        final int INDEX_MAX = 256;
        LinkedList<Character> sequence = new LinkedList<>();
        for(int i = 0; i< INDEX_MAX; i++){
            sequence.add((char) i);
        }
        return sequence;
    }

    /**
     * apply move-to-front encoding,
     * reading from standard input and writing to standard output
     * Read as char and write as int with the length of 8
     * Exp: input: ABRACADABRA!   -> output: 41 42 52 02 44 01 45 01 04 04 02 26
     */
    public static void encode(){
        LinkedList<Character> sequence = generateSequence();
        // read the data from the file
        while (!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            // get the index of the char c
            int index = sequence.indexOf(c);
            // write the index to the output file
            BinaryStdOut.write(index,8);
            // remove the object
            sequence.remove(index);
            // add the element to the start of the sequence
            sequence.addFirst(c);
        }
        BinaryStdOut.close();

    }

    /**
     * apply move-to-front decoding,
     *
     * reading from standard input and writing to standard output
     * Read as int and output as char
     * Exp: input: 41 42 52 02 44 01 45 01 04 04 02 26  output: ABRACADABRA!
     */
    public static void decode(){
        LinkedList<Character> sequence = generateSequence();
        while(!BinaryStdIn.isEmpty()){
            // read the int from the input stream
            int index = BinaryStdIn.readInt(8);
            char c = sequence.get(index);
            // write the char to the output
            BinaryStdOut.write(c);
            // move the char to the front
            sequence.remove(index);
            sequence.addFirst(c);

        }
        BinaryStdOut.close();

    }

    public static void main(String[] args) {
        if("-".equals(args[0])){
            encode();
        } else if ("+".equals(args[0])){
            decode();
        }
    }




}
