public class BTree {
	private final int degree;
	private BTreeNode root;


	public BTree(int degree) {
		this.degree = degree;
		this.root = new BTreeNode(degree, null);;
	}

	void traverse() {
		if (root != null) root.traverse();
	}

	// 트리 형식으로 키 값을 출력하는 메서드
	public void printTree() {
		if (root != null) {
			printNode(root, 0); // 루트 노드부터 출력 시작
		}
		System.out.println("\n-------------------------\n");
	}

	// 각 노드를 재귀적으로 출력하는 메서드
	private void printNode(BTreeNode node, int level) {
		// 현재 노드의 키들을 출력
		System.out.println("  ".repeat(level) + "Keys: " + node.keys);

		// 자식 노드가 있으면, 각 자식 노드에 대해 재귀 호출
		for (BTreeNode child : node.children) {
			printNode(child, level + 1); // 레벨을 증가시켜 들여쓰기 조절
		}
	}

	public void insert(int key) {
		BTreeNode currentNode = root;
		while (!currentNode.isLeaf()) {
			int nextNodeLocation = -1;
			for (int i = 0; i < currentNode.keys.size(); i++) {
				if (currentNode.keys.get(i) > key) {
					nextNodeLocation = i;
					break;
				}
			}
			if (nextNodeLocation == -1) {
				nextNodeLocation = currentNode.keys.size();
			}
			currentNode = currentNode.children.get(nextNodeLocation);
		}
		currentNode.addKey(key);
		if (currentNode.keys.size() > degree - 1) {
			split(currentNode);
		}
	}

	private void split(BTreeNode currentNode) {
		if (currentNode.parent == null) {
			int mid = degree / 2;
			BTreeNode leftNode = new BTreeNode(degree, currentNode);
			BTreeNode rightNode = new BTreeNode(degree, currentNode);
			for (int i = 0; i < mid; i++) {
				leftNode.addKey(currentNode.keys.get(i));
				if (!currentNode.isLeaf()) {
					currentNode.children.get(i).parent = leftNode;
					leftNode.children.add(i, currentNode.children.get(i));
					currentNode.children.get(i + 1).parent = leftNode;
					leftNode.children.add(i + 1, currentNode.children.get(i + 1));
				}
			}
			for (int i = mid + 1; i < degree; i++) {
				rightNode.addKey(currentNode.keys.get(i));
				if (!currentNode.isLeaf()) {
					currentNode.children.get(i).parent = rightNode;
					rightNode.children.add(i - (mid + 1), currentNode.children.get(i));
					currentNode.children.get(i + 1).parent = rightNode;
					rightNode.children.add(i - (mid + 1) + 1, currentNode.children.get(i + 1));
				}
			}
			int midKey = currentNode.keys.get(mid);
			currentNode.keys.clear();
			currentNode.keys.add(midKey);
			if (!currentNode.isLeaf()) {
				currentNode.children.clear();
			}
			currentNode.children.add(leftNode);
			currentNode.children.add(rightNode);
		} else {
			int mid = degree / 2;
			currentNode.parent.addKey(currentNode.keys.get(mid));
			currentNode.parent.children.remove(currentNode);
			BTreeNode leftNode = new BTreeNode(degree, currentNode.parent);
			BTreeNode rightNode = new BTreeNode(degree, currentNode.parent);
			for (int i = 0; i < mid; i++) {
				leftNode.addKey(currentNode.keys.get(i));
				if (!currentNode.isLeaf()) {
					currentNode.children.get(i).parent = leftNode;
					leftNode.children.add(i, currentNode.children.get(i));
					currentNode.children.get(i + 1).parent = leftNode;
					leftNode.children.add(i + 1, currentNode.children.get(i + 1));
				}
			}
			for (int i = mid + 1; i < degree; i++) {
				rightNode.addKey(currentNode.keys.get(i));
				if (!currentNode.isLeaf()) {
					currentNode.children.get(i).parent = rightNode;
					rightNode.children.add(i - (mid + 1), currentNode.children.get(i));
					currentNode.children.get(i + 1).parent = rightNode;
					rightNode.children.add(i - (mid + 1) + 1, currentNode.children.get(i + 1));
				}
			}
			int index = currentNode.parent.keys.indexOf(currentNode.keys.get(mid));
			currentNode.parent.children.add(index, leftNode);
			currentNode.parent.children.add(index + 1, rightNode);
			if (currentNode.parent.keys.size() > degree - 1) {
				split(currentNode.parent);
			}
		}
	}
}
