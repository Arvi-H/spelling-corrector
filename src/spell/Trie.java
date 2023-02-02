package spell;

public class Trie implements ITrie{
    private Node root = new Node();
    private int wordCount;
    private int nodeCount = 1;

    @Override
    public void add(String word) {
        // Convert word to lowercase
        word = word.toLowerCase();

        // Start at the root
        Node curr = root;

        for (int i = 0; i < word.length(); i++) {
            // Find the index of the letter
            int index = word.charAt(i) - 'a';

            // Create new node if the child is null
            if (curr.getChildren()[index] == null) {
                curr.getChildren()[index] = new Node();
                nodeCount++;
            }

            // Make the child the current node
            curr = curr.getChildren()[index];
        }

        if (curr.getValue() == 0) {
            wordCount++;
        }

        curr.incrementValue();
    }

    @Override
    public INode find(String word) {
        // Start at the root
        Node curr = root;

        for (int i = 0; i < word.length(); i++) {
            // Find the index of the letter
            int index = word.charAt(i) - 'a';

            // Return null if the child is null
            if (curr.getChildren()[index] == null) {
                return null;
            }

            // Make the child the current node
            curr = curr.getChildren()[index];
        }

        // Return reference to the node
        if (curr.getValue() > 0) {
            return curr;
        } else {
            return null;
        }
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder word = new StringBuilder();
        StringBuilder output = new StringBuilder();

        trieTraversal(root, word, output);

        return output.toString();
    }

    public void trieTraversal(Node curr, StringBuilder word, StringBuilder output) {
        if (curr.getValue() > 0) {
            // Append curr word to output
            output.append(word.toString());
            output.append("\n");
        }

        for (int i = 0; i < curr.getChildren().length; i++) {
            Node child = curr.getChildren()[i];

            if (child != null) {
                // Find the letter the index represents
                char letter = (char)('a' + i);

                // Update word with child's letter
                word.append(letter);

                // Recurse on that child
                trieTraversal(child, word, output);

                // Remove last letter from word
                word.deleteCharAt(word.length()-1);
            }
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 0; // Reset hashcode in between function calls

        for (int i = 0; i < root.getChildren().length; i++) {
            if (root.getChildren()[i] != null) {
                // Make the code "complex" (12345) and "unique" (wordCount * nodeCount + i)
                hashCode = hashCode * 12345 * wordCount * nodeCount + i;
            }
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        // Is this == 0?
        if (this == o) return true;

        // Is o is an instance of the Trie class, or any of its subclasses?
        if (!(o instanceof Trie)) return false;

        // Cast o to type Trie
        Trie t = (Trie) o;

        // Helper Function
        return equalsTraversal(this.root, t.root);
    }

    private boolean equalsTraversal(Node n1, Node n2) {
        // Compare n1 and n2 to see if they are the same | And
        if (n1 == null && n2 == null) return true;

        // Compare n1 and n2 to see if they are the same | Or
        if (n1 == null || n2 == null) return false;

        // Do n1 and n2 have the same count?
        if (n1.getValue() != n2.getValue()) return false;

        // Recurse on the children and compare the child subtrees
        for (int i = 0; i < n1.getChildren().length; i++) {
            if (!equalsTraversal(n1.getChildren()[i], n2.getChildren()[i])) { return false; }
        }

        // Objects are equal
        return true;
    }
}
