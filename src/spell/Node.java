package spell;

public class Node implements INode{
    private int count = 0;
    public Node children[] = new Node[26];

    @Override
    public void incrementValue() { count++; }

    @Override
    public int getValue() { return count; }

    @Override
    public Node[] getChildren() { return children; }
}
