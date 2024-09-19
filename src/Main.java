import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import BST.BinarySearchTree;
import BTree.BTree;

public class Main {
	public static void main(String[] args) {
		bTreeTest();
		bTreeVisitedNodeTest();
		bstTest();
		bstVisitedNodeTest();
		BTreeVsBSTSequential();
		BTreeVsBSTRandom();
		BTreeVsArrayListSequential();
		BTreeVsArrayListRandom();
		BTreeVsBTreeDegree();
	}

	public static void bTreeTest() {
		BTree bTree = new BTree(4);
		for(int i = 1; i <= 15; i++) {
			bTree.insert(i);
			bTree.printTree();
		}
		bTree.delete(9);
		bTree.printTree();
		bTree.delete(6);
		bTree.printTree();
		bTree.delete(8);
		bTree.printTree();
		bTree.delete(2);
		bTree.printTree();
		bTree.delete(5);
		bTree.printTree();
		bTree.delete(3);
		bTree.printTree();
		bTree.delete(7);
		bTree.printTree();
		bTree.delete(4);
		bTree.printTree();
		bTree.delete(10);
		bTree.printTree();
		bTree.delete(11);
		bTree.printTree();
	}

	public static void bstTest() {
		Random random = new Random();
		BinarySearchTree bst = new BinarySearchTree();
		for(int i = 1; i <= 11; i++) {
			bst.insert(random.nextInt(100));
			// bst.insert(i);
		}
		bst.delete(10);
		bst.printTree();
		System.out.println(bst.search(10));
	}

	public static void BTreeVsBSTSequential() {
		int degree = 3;
		BTree bTree = new BTree(degree);
		BinarySearchTree bst = new BinarySearchTree();
		int numElements = 10000;
		System.out.println("\n<BTree Vs BST Sequential>");
		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.insert(i);
			}
		}, "[B Tree] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bst.insert(i);
			}
		}, "[Binary Search Tree] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.search(i);
			}
		}, "[B Tree] Search time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bst.search(i);
			}
		}, "[Binary Search Tree] Search time:");

		int bTreeCount = 0;
		for (int i = 0; i < numElements; i++) {
			bTreeCount += bTree.countNodesVisited(i);
		}
		int averageBTreeCount = bTreeCount / numElements;

		int bstCount = 0;
		for (int i = 0; i < numElements; i++) {
			bstCount += bst.countNodesVisited(i);
		}
		int averageBstCount = bstCount / numElements;

		System.out.println("[B Tree] average visited count:" + averageBTreeCount);
		System.out.println("[Binary Search Tree] average visited count:" + averageBstCount);

	}

	public static void BTreeVsBSTRandom() {
		Random random = new Random();
		int degree = 3;
		BTree bTree = new BTree(degree);
		BinarySearchTree bst = new BinarySearchTree();
		int numElements = 10000;
		System.out.println("\n<BTree Vs BST Random>");
		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.insert(random.nextInt(numElements));
			}
		}, "[B Tree] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bst.insert(random.nextInt(numElements));
			}
		}, "[Binary Search Tree] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.search(random.nextInt(numElements));
			}
		}, "[B Tree] Search time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bst.search(random.nextInt(numElements));
			}
		}, "[Binary Search Tree] Search time:");

		int bTreeCount = 0;
		for (int i = 0; i < numElements; i++) {
			bTreeCount += bTree.countNodesVisited(i);
		}
		int averageBTreeCount = bTreeCount / numElements;

		int bstCount = 0;
		for (int i = 0; i < numElements; i++) {
			bstCount += bst.countNodesVisited(i);
		}
		int averageBstCount = bstCount / numElements;

		System.out.println("[B Tree] average visited count:" + averageBTreeCount);
		System.out.println("[Binary Search Tree] average visited count:" + averageBstCount);
	}

	public static void BTreeVsArrayListSequential() {
		int degree = 3;
		BTree bTree = new BTree(degree);
		List<Integer> list = new ArrayList<>();

		// 성능을 측정할 데이터 크기
		int numElements = 100000;
		System.out.println("\n<BTree Vs ArrayList Sequential>");
		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.insert(i);
			}
		}, "[B Tree] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				list.add(i);
			}
		}, "[ArrayList] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.search(i);
			}
		}, "[B Tree] Search time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				list.indexOf(i);
			}
		}, "[ArrayList] Search time:");
	}

	public static void BTreeVsArrayListRandom() {
		Random random = new Random();
		int degree = 3;
		BTree bTree = new BTree(degree);
		List<Integer> list = new ArrayList<>();

		// 성능을 측정할 데이터 크기
		int numElements = 100000;
		System.out.println("\n<BTree Vs ArrayList Random>");
		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.insert(random.nextInt(numElements));
			}
		}, "[B Tree] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				list.add(random.nextInt(numElements));
			}
		}, "[ArrayList] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree.search(random.nextInt(numElements));
			}
		}, "[B Tree] Search time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				list.indexOf(random.nextInt(numElements));
			}
		}, "[ArrayList] Search time:");
	}

	public static void BTreeVsBTreeDegree() {
		for (int i = 3; i < 400; i = i + 10) {
			BTree bTree1 = new BTree(i);

			String taskName = "[B Tree " + i +"] Insert time:";
			String taskName2 = "[B Tree " + i +"] Search time:";
			String taskName3 = "[B Tree " + i +"] Average Visited Count:";

			int numElements = 1000000;

			measureExecutionTime(() -> {
				for (int j = 0; j < numElements; j++) {
					bTree1.insert(j);
				}
			}, taskName);

			measureExecutionTime(() -> {
				for (int j = 0; j < numElements; j++) {
					bTree1.search(j);
				}
			}, taskName2);

			int bTreeCount = 0;
			for (int j = 0; j < numElements; j++) {
				bTreeCount += bTree1.countNodesVisited(j);
			}
			int averageBTreeCount = bTreeCount / numElements;
			System.out.println(taskName3 + averageBTreeCount + "\n");
		}
	}

	public static void bTreeVisitedNodeTest() {
		BTree bTree = new BTree(4);
		for(int i = 1; i <= 15; i++) {
			bTree.insert(i);
			bTree.printTree();
		}
		int count = bTree.countNodesVisited(12);
		System.out.println(count);
	}

	public static void bstVisitedNodeTest() {
		BinarySearchTree bst = new BinarySearchTree();
		// for(int i = 1; i <= 15; i++) {
		// 	bst.insert(i);
		// 	bst.printTree();
		// }
		bst.insert(5);
		bst.printTree();
		bst.insert(7);
		bst.printTree();
		bst.insert(9);
		bst.printTree();
		bst.insert(3);
		bst.printTree();
		bst.insert(1);
		bst.printTree();
		int count = bst.countNodesVisited(1);
		System.out.println(count);
	}



	// 시간을 측정하고 해당 코드의 실행 시간을 반환하는 함수
	public static void measureExecutionTime(Runnable task, String taskName) {
		int degree = 3;
		BTree bTree = new BTree(degree);
		BinarySearchTree bst = new BinarySearchTree();
		int numElements = 10000;

		// 시작 시간
		long startTime = System.nanoTime();

		// 실행할 코드 블록 실행
		task.run();

		// 끝나는 시간
		long endTime = System.nanoTime();

		// 시간 차이를 계산 (밀리초 단위로 변환)
		long duration = (endTime - startTime) / 1_000_000;

		// 실행 시간을 출력
		System.out.println(taskName + " 실행 시간: " + duration + " ms");
	}
}

