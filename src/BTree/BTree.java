package BTree;

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
		insert(root, key);
		if (root.isFull()) {
			BTreeNode newRoot = new BTreeNode(degree);
			newRoot.children.add(root);
			split(newRoot, 0);
			root = newRoot;
		}
	}

	// 가득 차지 않은 노드에 삽입하는 메서드
	private void insert(BTreeNode node, int key) {
		if (node.isLeaf()) {
			node.addKey(key);
		} else {
			// 자식 노드로 내려가며 삽입
			int nextNodeIndex = node.getNextNodeIndex(key);
			BTreeNode nextNode = node.children.get(nextNodeIndex);
			insert(nextNode, key);
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

	private void deleteInternalNode(BTreeNode node, int deletedKeyIndex) {
		BTreeNode nextChild;
		if (node.children.get(deletedKeyIndex).keys.size() >= minimumKeyCount) {
			int pred = getPredecessor(node, deletedKeyIndex);
			node.keys.set(deletedKeyIndex, pred);
			nextChild = node.children.get(deletedKeyIndex);
			delete(nextChild, pred);
		} else {
			int succ = getSuccessor(node, deletedKeyIndex);
			node.keys.set(deletedKeyIndex, succ);
			nextChild = node.children.get(deletedKeyIndex + 1);
			delete(nextChild, succ);
		}
		if (nextChild.keys.size() < minimumKeyCount) {
			fill(node, deletedKeyIndex);
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

	private void fill(BTreeNode node, int deletedNodeIndex) {
		// 왼쪽 형제에게 빌리는 경우 2-1
		if (deletedNodeIndex != 0 && node.children.get(deletedNodeIndex - 1).keys.size() > minimumKeyCount) {
			borrowFromPrev(node, deletedNodeIndex);
		}
		// 오른쪽 형제에게 빌리는 경우 2-1
		else if (deletedNodeIndex != node.children.size() - 1 && node.children.get(deletedNodeIndex + 1).keys.size() > minimumKeyCount) {
			borrowFromNext(node, deletedNodeIndex);
		}
		else {
			// 왼쪽 형제와 부모노드를 합치는 경우 2-2
			if (deletedNodeIndex != 0) {
				leftMerge(node, deletedNodeIndex);
			}
			// 오른쪽 형제와 부모노드를 합치는 경우 2-2
			else {
				rightMerge(node, deletedNodeIndex);
			}
		}
	}

	private void borrowFromPrev(BTreeNode node, int deletedNodeIndex) {
		BTreeNode deletedChild = node.children.get(deletedNodeIndex);
		BTreeNode sibling = node.children.get(deletedNodeIndex - 1);

		deletedChild.addKey(node.keys.get(deletedNodeIndex - 1));
		if (!deletedChild.isLeaf()) {
			deletedChild.children.add(0, sibling.children.remove(sibling.children.size() - 1));
		}
		node.keys.set(deletedNodeIndex - 1, sibling.keys.remove(sibling.keys.size() - 1));
	}

	private void borrowFromNext(BTreeNode node, int deletedNodeIndex) {
		BTreeNode deletedChild = node.children.get(deletedNodeIndex);
		BTreeNode sibling = node.children.get(deletedNodeIndex + 1);

		deletedChild.keys.add(node.keys.get(deletedNodeIndex));
		if (!deletedChild.isLeaf()) {
			deletedChild.children.add(sibling.children.remove(0));
		}
		node.keys.set(deletedNodeIndex, sibling.keys.remove(0));
	}

	private void leftMerge(BTreeNode node, int deletedNodeIndex) {
		BTreeNode deletedChild = node.children.get(deletedNodeIndex);
		BTreeNode sibling = node.children.get(deletedNodeIndex - 1);

		for (int i = 0; i < sibling.keys.size(); i++) {
			deletedChild.keys.add(i, sibling.keys.get(i));
		}
		if (!deletedChild.isLeaf()) {
			for (int i = 0; i < sibling.children.size(); i++) {
				deletedChild.children.add(i, sibling.children.get(i));
			}
		}
		deletedChild.addKey(node.keys.remove(deletedNodeIndex - 1));

		node.children.remove(sibling);
	}

	private void rightMerge(BTreeNode node, int deletedNodeIndex) {
		BTreeNode deletedChild = node.children.get(deletedNodeIndex);
		BTreeNode sibling = node.children.get(deletedNodeIndex + 1);

		deletedChild.addKey(node.keys.remove(deletedNodeIndex));
		deletedChild.keys.addAll(sibling.keys);
		if (!deletedChild.isLeaf()) {
			deletedChild.children.addAll(sibling.children);
		}

		node.children.remove(sibling);
	}

	public int countNodesVisited(int key) {
		return countNodesVisited(root, key, 0);
	}

	// 재귀적으로 방문한 노드를 세는 메서드
	private int countNodesVisited(BTreeNode node, int key, int nodesVisited) {
		int i = 0;
		nodesVisited++; // 현재 노드를 방문했으므로 방문 노드 수 증가

		while (i < node.keys.size() && key > node.keys.get(i)) {
			i++;
		}
		if (i < node.keys.size() && key == node.keys.get(i)) {
			return nodesVisited; // 키를 찾았으므로 현재까지의 방문 노드 수 반환
		}
		if (node.isLeaf()) {
			return nodesVisited; // 리프 노드에 도달, 트리에 키가 없음
		}
		return countNodesVisited(node.children.get(i), key, nodesVisited);
	}
}
