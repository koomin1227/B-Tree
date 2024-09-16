import java.util.ArrayList;
import java.util.List;

public class BTreeNode {
	public final List<Integer> keys;
	public final List<BTreeNode> children;
	public BTreeNode parent;

	public BTreeNode(int degree, BTreeNode parent) {
		keys = new ArrayList<>(degree - 1);
		children = new ArrayList<>(degree);
		this.parent = parent;
	}

	public void addKey(int key) {
		if (keys.isEmpty()) {
			keys.add(key);
		} else {
			for (int i = 0; i < keys.size(); i++) {
				if (keys.get(i) >= key) {
					keys.add(i, key);
					return;
				}
			}
			keys.add(key);
		}
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	public BTreeNode getParent() {
		return parent;
	}

	void traverse() {
		int i;
		for (i = 0; i < keys.size(); i++) {
			if (!isLeaf())
				children.get(i).traverse();
			System.out.print(" " + keys.get(i));
		}
		if (!isLeaf())
			children.get(i).traverse();
	}
}
