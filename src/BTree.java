public class BTree {
	private final int degree;
	private BTreeNode root;
	private final int minimumKeyCount;

	public BTree(int degree) {
		this.degree = degree;
		this.root = new BTreeNode(degree);

		if (degree % 2 == 0) {
			this.minimumKeyCount = degree / 2 - 1;
		} else {
			this.minimumKeyCount = degree / 2;
		}
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

	public BTreeNode search(int key) {
		return search(root, key);
	}

	// 재귀적으로 검색하는 메서드
	private BTreeNode search(BTreeNode node, int key) {
		int i = 0;

		// 현재 노드에서 키와 비교
		while (i < node.keys.size() && key > node.keys.get(i)) {
			i++;
		}

		// 키가 현재 노드에 있는 경우
		if (i < node.keys.size() && key == node.keys.get(i)) {
			return node;  // 키를 찾았으므로 해당 노드를 반환
		}

		// 리프 노드에 도달한 경우, 트리에 키가 없음
		if (node.isLeaf()) {
			return null;
		}

		// 자식 노드로 내려가서 계속 검색
		return search(node.children.get(i), key);
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
		if (degree % 2 == 0) {
			mid = mid - 1;
		}

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

	// 삭제 메서드 추가
	public void delete(int key) {
		delete(root, key);

		// 루트 노드가 키를 잃었을 때, 자식이 있으면 그 자식으로 루트 갱신
		if (root.keys.isEmpty()) {
			if (!root.isLeaf()) {
				root = root.children.get(0);
			}
		}
	}

	private void delete(BTreeNode node, int key) {
		int idx = node.findKey(key);

		// 1. 키가 현재 노드에 있는 경우
		if (idx != -1) {
			if (node.isLeaf()) {
				// 리프 노드에서 직접 삭제
				node.keys.remove(idx);
			} else {
				// 내부 노드에서 삭제 처리
				deleteInternalNode(node, idx);
			}
		} else {
			// 키가 리프 노드에 없는 경우
			if (node.isLeaf()) {
				return;  // 찾으려는 key 가 없음
			}

			// 키가 없으면 적절한 자식 노드로 이동
			idx = node.getNextNodeIndex(key);
			BTreeNode nextChild = node.children.get(idx);

			//자식 노드에서 삭제
			delete(nextChild, key);
			// 삭제 후 자식 노드의 최소 키 값이 어긋나면
			if (nextChild.keys.size() < minimumKeyCount) {
				// 채워주기
				fill(node, idx);
			}
		}
	}

	private void deleteInternalNode(BTreeNode node, int idx) {
		BTreeNode nextChild;
		if (node.children.get(idx).keys.size() >= minimumKeyCount) {
			int pred = getPredecessor(node, idx);
			node.keys.set(idx, pred);
			nextChild = node.children.get(idx);
			delete(nextChild, pred);
		} else {
			int succ = getSuccessor(node, idx);
			node.keys.set(idx, succ);
			nextChild = node.children.get(idx + 1);
			delete(nextChild, succ);
		}
		if (nextChild.keys.size() < minimumKeyCount) {
			fill(node, idx);
		}
	}

	private int getPredecessor(BTreeNode node, int idx) {
		BTreeNode current = node.children.get(idx);
		while (!current.isLeaf()) {
			current = current.children.get(current.keys.size());
		}
		return current.keys.get(current.keys.size() - 1);
	}

	private int getSuccessor(BTreeNode node, int idx) {
		BTreeNode current = node.children.get(idx + 1);
		while (!current.isLeaf()) {
			current = current.children.get(0);
		}
		return current.keys.get(0);
	}

	private void fill(BTreeNode node, int idx) {
		// 형제 한테 빌리기
		if (idx != 0 && node.children.get(idx - 1).keys.size() > minimumKeyCount) {
			borrowFromPrev(node, idx);
		} else if (idx != node.children.size() - 1 && node.children.get(idx + 1).keys.size() > minimumKeyCount) {
			borrowFromNext(node, idx);
		} else {
			if (idx != 0) {
				leftMerge(node, idx);
			} else {
				rightMerge(node, idx);
			}
		}
	}

	private void borrowFromPrev(BTreeNode node, int idx) {
		BTreeNode child = node.children.get(idx);
		BTreeNode sibling = node.children.get(idx - 1);

		child.addKey(node.keys.get(idx - 1));
		if (!child.isLeaf()) {
			child.children.add(0, sibling.children.remove(sibling.children.size() - 1));
		}
		node.keys.set(idx - 1, sibling.keys.remove(sibling.keys.size() - 1));
	}

	private void borrowFromNext(BTreeNode node, int idx) {
		BTreeNode child = node.children.get(idx);
		BTreeNode sibling = node.children.get(idx + 1);

		child.keys.add(node.keys.get(idx));
		if (!child.isLeaf()) {
			child.children.add(sibling.children.remove(0));
		}
		node.keys.set(idx, sibling.keys.remove(0));
	}

	private void leftMerge(BTreeNode node, int idx) {
		BTreeNode child = node.children.get(idx);
		BTreeNode sibling = node.children.get(idx - 1);

		for (int i = 0; i < sibling.keys.size(); i++) {
			child.keys.add(i, sibling.keys.get(i));
		}
		if (!child.isLeaf()) {
			for (int i = 0; i < sibling.children.size(); i++) {
				child.children.add(i, sibling.children.get(i));
			}
		}
		child.addKey(node.keys.remove(idx - 1));

		node.children.remove(sibling);
	}

	private void rightMerge(BTreeNode node, int idx) {
		BTreeNode child = node.children.get(idx);
		BTreeNode sibling = node.children.get(idx + 1);

		child.addKey(node.keys.remove(idx));
		child.keys.addAll(sibling.keys);
		if (!child.isLeaf()) {
			child.children.addAll(sibling.children);
		}

		node.children.remove(sibling);
	}
}
