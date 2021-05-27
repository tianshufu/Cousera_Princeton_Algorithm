import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class HelloWord {
    public static void main(String[] args) {
        System.out.println("Hello");
        int k = Integer.parseInt(args[0]);
        System.out.println("The num is:"+k);
        String[] input = In.readStrings(args[1]);
        for (String i: input){
            StdOut.println(i);
        }
    }
}
