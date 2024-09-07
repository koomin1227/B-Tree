public class BTree {
	private final int degree;
	private BNode root;


	public BTree(int degree) {
		this.degree = degree;
		this.root = null;
	}

	public void insert(int key) {
		if (root == null) {
			root = new BNode(degree, null);
		}
	}
}
