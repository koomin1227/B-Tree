package BTree;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode {
	public final List<Integer> keys;
	public final List<BTreeNode> children;
	public final int degree;

	public BTreeNode(int degree) {
		keys = new ArrayList<>(degree - 1);
		children = new ArrayList<>(degree);
		this.degree = degree;
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

	public int findKey(int key) {
		return keys.indexOf(key);
	}

	public int getNextNodeIndex(int key) {
		int nextNodeLocation = -1;
		for (int i = 0; i < keys.size(); i++) {
			if (keys.get(i) > key) {
				nextNodeLocation = i;
				break;
			}
		}
		if (nextNodeLocation == -1) {
			nextNodeLocation = keys.size();
		}
		return nextNodeLocation;
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	public boolean isFull() {
		return keys.size() > degree - 1;
	}
}
