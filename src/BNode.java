import java.util.ArrayList;
import java.util.List;

public class BNode {
	private final List<Integer> keys;
	private final List<BNode> children;
	private final BNode parent;

	public BNode(int degree, BNode parent) {
		keys = new ArrayList<>(degree - 1);
		children = new ArrayList<>(degree);
		this.parent = parent;
	}

	public void addKey(int key) {
		if (keys.isEmpty()) {
			keys.add(key);
		} else {
			int insertIndex = 0;
			boolean found = false;
			for (int i = 0; i < keys.size(); i++) {
				if (keys.get(i) >= key) {
					keys.add(insertIndex, key);
					found = true;
					break;
				}
				insertIndex++;
			}
			if (!found) {
				keys.add(insertIndex, key);
			}
		}
	}

	public BNode getParent() {
		return parent;
	}
}
