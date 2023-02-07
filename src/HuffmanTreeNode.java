import java.util.Comparator;

/**
 * The Class HuffmanTreeNode. 
 */
public class HuffmanTreeNode {
	
	/** The weight of this node. represents the character count if a leaf
	 *  otherwise, it is the sum of the weights of the left and right children
	 */
	private int weight;
	
	/** The ord value: -1 if the node is not a leaf node */
	private int ordValue;
	
	/** The char value: valid if this is a leaf node; otherwise 0*/
	private char charValue;
	
	/** STATIC version of the id used to initialize the instance id to a unique value for every node. */
	private static int ID=0;
	
	/** The unique id for each instance - required to ensure consistent sorting in compareOrdWeights. */
	private int id;
	
	/** The left child of this Huffman Tree Node */
	private HuffmanTreeNode left;
	
	/** The right child of this Huffman Tree Node. */
	private HuffmanTreeNode right;
	
	/**
	 * Instantiates a new huffman tree node. This constructor is used to generate unconnected leaf 
	 * nodes.
	 *
	 * @param ordValue the ord value (the integer value of the character)
	 * @param weight the weight (the frequency of occurrence of the character)
	 */
	public HuffmanTreeNode(int ordValue, int weight) {
		this.weight = weight;
		this.ordValue = ordValue;
		this.charValue = (char) ordValue;
		left = null;
		right = null;
		id = ID++;
	}
	
	/**
	 * Instantiates a new huffman tree node. This constructor is used to connect two nodes
	 * together, which may or may not be leaf nodes. This node will have two nodes - left and right.
	 * The weight of this node is the sum of the weights of the left and right nodes. Since this node
	 * is not a leaf node, the ordValue is -1 and the charValue is 0;
	 *
	 * @param weight the weight
	 * @param left the left
	 * @param right the right
	 */
	public HuffmanTreeNode(int weight, HuffmanTreeNode left, HuffmanTreeNode right) {
		this.weight = weight;
		this.ordValue = -1;
		this.charValue = 0;
		this.left = left;
		this.right = right;
		id = ID++;
	}

	// Getters - no Setters!
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Gets the ordinal value.
	 *
	 * @return the ordinal value
	 */
	public int getOrdValue() {
		return ordValue;
	}

	/**
	 * Gets the char value.
	 *
	 * @return the char value
	 */
	public char getCharValue() {
		return charValue;
	}

	/**
	 * Gets the left.
	 *
	 * @return the left
	 */
	public HuffmanTreeNode getLeft() {
		return left;
	}

	/**
	 * Gets the right.
	 *
	 * @return the right
	 */
	public HuffmanTreeNode getRight() {
		return right;
	}

	// Helper methods
	/**
	 * Reset ID. If you are going to rebuild the tree - you must reset the ID....
	 */
	public void resetID() {
		ID = 0;
	}
	
	/**
	 * Checks if is leaf.
	 *
	 * @return true, if is leaf
	 */
	public boolean isLeaf() {
		return(ordValue != -1);
	}

	/** The comparator used to sort nodes in the following order:
	 *  1) by increasing weight
	 *  2) by increasing ordinal value.
	 *  3) by increasing id 
	 *  
	 *  Inclusion of the unique ID enforces a repeatable order when both the weight
	 *  and ordinal values of the nodes being compared are equal - the node that 
	 *  created first will be first (higher priority)
	 * 
	 *  This will be used by the Priority Queue to determine the ordering of 
	 *  nodes in the queue. 
	 */  
	public static Comparator<HuffmanTreeNode> compareWeightOrd = new Comparator<HuffmanTreeNode>() {
		@Override
		public int compare(HuffmanTreeNode ht1, HuffmanTreeNode ht2) {
			if (ht1.weight > ht2.weight) { // by increasing weight
				return 1;
			} else if (ht1.weight < ht2.weight) {
				return -1;
			}
			
			if (ht1.ordValue > ht2.ordValue) { // by increasing ordinal value
				return 1;
			} else if (ht1.ordValue < ht2.ordValue) {
				return -1;
			}
			
			if (ht1.id > ht2.id) { // by increasing id
				return 1;
			} else if (ht1.id < ht2.id) {
				return -1;
			}
			
			return 0; // equal by every value
		}
	};
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ord = " + this.ordValue + "   weight = " + this.weight;
	}
	
}
