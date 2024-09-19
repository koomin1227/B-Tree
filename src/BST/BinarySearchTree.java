package BST;

public class BinarySearchTree {
	private BSTNode root;

	// 트리에 삽입하는 메서드
	public void insert(int key) {
		root = insertRec(root, key);
	}

	private BSTNode insertRec(BSTNode root, int key) {
		if (root == null) {
			root = new BSTNode(key);
			return root;
		}
		if (key < root.key) {
			root.left = insertRec(root.left, key);
		} else if (key > root.key) {
			root.right = insertRec(root.right, key);
		}
		return root;
	}

	// 트리에서 키를 삭제하는 메서드
	public void delete(int key) {
		root = deleteRec(root, key);
	}

	private BSTNode deleteRec(BSTNode root, int key) {
		if (root == null) return root;

		// 키가 현재 노드보다 작으면 왼쪽 서브트리에서 삭제
		if (key < root.key) {
			root.left = deleteRec(root.left, key);
		}
		// 키가 현재 노드보다 크면 오른쪽 서브트리에서 삭제
		else if (key > root.key) {
			root.right = deleteRec(root.right, key);
		}
		// 키가 현재 노드와 같으면 이 노드를 삭제
		else {
			// 자식이 없는 경우 또는 하나인 경우
			if (root.left == null) return root.right;
			else if (root.right == null) return root.left;

			// 자식이 둘인 경우, 후임자(successor)로 대체
			root.key = minValue(root.right);

			// 후임자 노드를 삭제
			root.right = deleteRec(root.right, root.key);
		}

		return root;
	}

	// 후임자(successor)를 찾는 메서드 (오른쪽 서브트리에서 가장 작은 값)
	private int minValue(BSTNode root) {
		int minValue = root.key;
		while (root.left != null) {
			root = root.left;
			minValue = root.key;
		}
		return minValue;
	}

	// 키를 탐색하는 메서드
	public boolean search(int key) {
		return searchRec(root, key) != null;
	}

	private BSTNode searchRec(BSTNode root, int key) {
		if (root == null || root.key == key) return root;

		if (key < root.key)
			return searchRec(root.left, key);
		return searchRec(root.right, key);
	}

	// 트리의 구조를 출력하는 메서드
	public void printTree() {
		if (root != null) {
			printNode(root, 0); // 루트 노드부터 출력 시작
		}
		System.out.println("\n-------------------------\n");
	}

	// 각 노드를 재귀적으로 출력하는 메서드
	private void printNode(BSTNode node, int level) {
		// 현재 노드의 키 출력
		System.out.println("  ".repeat(level) + "Key: " + node.key);

		// 왼쪽 자식 노드가 있으면 재귀 호출
		if (node.left != null) {
			System.out.println("  ".repeat(level + 1) + "Left:");
			printNode(node.left, level + 2);
		}

		// 오른쪽 자식 노드가 있으면 재귀 호출
		if (node.right != null) {
			System.out.println("  ".repeat(level + 1) + "Right:");
			printNode(node.right, level + 2);
		}
	}

	public int countNodesVisited(int key) {
		return countNodesVisitedRec(root, key, 0);
	}

	// 재귀적으로 방문한 노드를 세는 메서드
	private int countNodesVisitedRec(BSTNode node, int key, int nodesVisited) {
		if (node == null) {
			return nodesVisited; // 트리에 키가 없을 경우, 방문한 노드 수 반환
		}

		nodesVisited++; // 현재 노드를 방문했으므로 방문 노드 수 증가

		if (node.key == key) {
			return nodesVisited; // 키를 찾았으므로 현재까지의 방문 노드 수 반환
		} else if (key < node.key) {
			return countNodesVisitedRec(node.left, key, nodesVisited); // 왼쪽 자식으로 이동
		} else {
			return countNodesVisitedRec(node.right, key, nodesVisited); // 오른쪽 자식으로 이동
		}
	}
}
