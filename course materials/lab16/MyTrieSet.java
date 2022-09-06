import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTrieSet implements TrieSet61BL{

    public Node root;

    public static class Node{
        public String item;
        public HashMap<String,Node> branches;
        public boolean isWord;


        public Node(String character){
            item = character;
            isWord = false;
            branches = new HashMap<String,Node>();

        }

        public Node(){
            item = null;
            isWord = false;
            branches = new HashMap<String,Node>();

        }

    }

    public MyTrieSet(){
        root = new Node();
    }


    public static void main(String[] args) {
        MyTrieSet t = new MyTrieSet();
        t.add("hik");
        boolean k = t.contains("hik");
        System.out.print(k);
    }



    /** Clears all items out of Trie */
    @Override
    public void clear(){
        root.branches = new HashMap<String,Node>();
    }

    /** Returns true if the Trie contains KEY, false otherwise */
    @Override
    public boolean contains(String key){
        return containHelper(key, root);
    }

    public boolean containHelper(String key, Node curr){
        if(curr.branches.keySet() == null || key.equals("")){
            return false;
        }
        if(curr.branches.keySet().contains(key.substring(0, 1))){
            if(curr.branches.get(key.substring(0, 1)).isWord && key.length() == 1){
                return true;
            }else{
                return containHelper(key.substring(1), curr.branches.get(key.substring(0, 1)));
            }
        }else{
            return false;
        }

    }


    /** Inserts string KEY into Trie */
    @Override
    public void add(String key){
        addHelper(key, root);
    }

    public void addHelper(String key, Node node){
        if(key.equals("")){
            return;
        }
        String input = key.substring(0, 1);

        if(! node.branches.keySet().contains(input)){
            node.branches.put(input, new Node(input));
        }

        addHelper(key.substring(1), node.branches.get(input));
        if(key.length() == 1) {
            node.branches.get(input).isWord = true;
        }
    }

    /** Returns a list of all words that start with PREFIX */
    @Override
    public List<String> keysWithPrefix(String prefix){
        Node node = root;
        String input;
        for(int i = 0; i< prefix.length(); i++){
            input = prefix.substring(i, i + 1);
            if(! node.branches.keySet().contains(input)){
                return new ArrayList<String>();
            }else{
                node = node.branches.get(input);
            }
        }

        ArrayList<String> words = keysHelper(node, new ArrayList<String>());
        for(int k = 0; k< words.size(); k++){
            words.set(k, prefix.substring(0,prefix.length()-1) + words.get(k));
        }
        return words;
    }

    public ArrayList<String> keysHelper(Node node, ArrayList<String> wordList){
        wordList = new ArrayList<String>();
        if(node == null){
            return wordList;
        }else{
            for(Node branch: node.branches.values()){
                wordList.addAll(keysHelper(branch, wordList));
            }
            for(int k = 0; k< wordList.size(); k++){
                wordList.set(k, node.item + wordList.get(k));
    
            }

            if(node.isWord && node.branches.keySet()!=null){
                wordList.add(node.item);
            }
        }
        return wordList;
    }

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 16. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public String longestPrefixOf(String key){
        throw new UnsupportedOperationException();
    }
}
