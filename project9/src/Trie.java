public class Trie {
    class TrieNode{
        // check if the cur Node is the end
        boolean end;
        String val;
        // create a sub array with the length of 26
        TrieNode [] tns = new TrieNode[26];


        @Override
        public  String toString(){
            String subNodes = "";
            for(int i = 0; i < 26; i++){
                if(tns[i]!=null){
                    subNodes += (char)(i+'A');
                    subNodes += " ";
                }
            }
            return subNodes;
        }
    }

    public TrieNode root;

    public Trie(){
        // initial the root
        root = new TrieNode();
    }


    /**
     * Insert the String s to the Trie
     * @param s
     */
    public void insert(String s){
        TrieNode p = root;
        // check if the node is valid or not
        if(s.contains("Q") && !s.contains("QU")){
            return;
        }
        // iterate the s to create the tree node
        for(int i = 0; i < s.length(); i++){
            // get the offset of the ith val
            int u = s.charAt(i) - 'A';
            // skip the ch 'u' if it's present is 'q'
            if(i>0 && (s.charAt(i) == 'U' && s.charAt(i-1) == 'Q')){
                //System.out.println("New element found:"+s);
                continue;
            }
            // if u is not in the cur node's list
            if(p.tns[u] == null){
                // create a new node at the uth pos
                p.tns[u] = new TrieNode();
                // update the node
            }
            // move to the next
            p = p.tns[u];
        }
        p.end = true;
        p.val = s;
    }


    /**
     * Search whether the string is in the Trie
     * @param s
     * @return
     */
    public  boolean search(String s){
        TrieNode p = root;
        for(int i = 0; i < s.length(); i++){
            if(i>0 && (s.charAt(i) == 'U' && s.charAt(i-1) == 'Q')){
                //System.out.println("New element found:"+s);
                continue;
            }
            int u = s.charAt(i) - 'A';
            if(p.tns[u] == null){
                return false;
            }

            p = p.tns[u];
        }
        // the reason to return p.end is to because this could be a substring
        return p.end;
    }

    /**
     * Check whether there is strings start with 's'
     * @param s
     * @return
     */
    public boolean startWith(String s){
        TrieNode p = root;
        for(int i = 0; i < s.length(); i++){
            int u = s.charAt(i) - 'A';
            if(p.tns[u] == null){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("APP");
        trie.insert("APPLE");
        trie.insert("BAT");
        trie.insert("BATTLE");
        System.out.println(trie.search("BATE"));
    }




}
