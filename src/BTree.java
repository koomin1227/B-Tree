public class BTree {
	private final int degree;
	private BTreeNode root;

	public BTree(int degree) {
		this.degree = degree;
		this.root = new BTreeNode(degree);
	}

	public void traverse() {
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
		System.out.println("  ".repeat(level) + "Keys: " + node.keys);

		// 자식 노드가 있으면, 각 자식 노드에 대해 재귀 호출
		for (BTreeNode child : node.children) {
			printNode(child, level + 1); // 레벨을 증가시켜 들여쓰기 조절
		}
	}

	public void insert(int key) {
		insertIntoLeafNode(root, key);
		if (root.isFull()) {
			BTreeNode newRoot = new BTreeNode(degree);
			newRoot.children.add(root);
			split(newRoot, 0);
			root = newRoot;
		}
	}

	// 가득 차지 않은 노드에 삽입하는 메서드
	private void insertIntoLeafNode(BTreeNode node, int key) {
		if (node.isLeaf()) {
			node.addKey(key);
		} else {
			// 자식 노드로 내려가며 삽입
			int nextNodeIndex = node.getNextNodeIndex(key);
			BTreeNode nextNode = node.children.get(nextNodeIndex);
			insertIntoLeafNode(nextNode, key);
			if (nextNode.isFull()) {
				split(node, nextNodeIndex);
			}
		}
	}

	// 노드를 분할하는 메서드
	private void split(BTreeNode parentNode, int fullNodeIndex) {
		BTreeNode fullNode = parentNode.children.get(fullNodeIndex);
		BTreeNode newNode = new BTreeNode(degree);
		int mid = degree / 2;

		// 새로운 노드에 중간 값 이후의 키와 자식들을 이동
		for (int j = mid + 1; j < degree; j++) {
			newNode.keys.add(fullNode.keys.remove(mid + 1));
		}
		if (!fullNode.isLeaf()) {
			for (int j = mid + 1; j <= degree; j++) {
				newNode.children.add(fullNode.children.remove(mid + 1));
			}
		}

		// 부모 노드에 중간 키 추가
		parentNode.keys.add(fullNodeIndex, fullNode.keys.remove(mid));
		parentNode.children.add(fullNodeIndex + 1, newNode);
	}
}
