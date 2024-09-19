import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import BST.BinarySearchTree;
import BTree.BTree;

public class Main {
	public static void main(String[] args) {
		bTreeTest();
		bstTest();
		BTreeVsBSTSequential();
		BTreeVsBSTRandom();
		BTreeVsArrayListSequential();
		BTreeVsArrayListRandom();
		BTreeVsBTreeDegree();
	}

	public static void bTreeTest() {
		BTree bTree = new BTree(3);
		for(int i = 1; i <= 11; i++) {
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
		Random random = new Random();
		int degree1 = 3;
		BTree bTree1 = new BTree(degree1);
		int degree2 = 7;
		BTree bTree2 = new BTree(degree2);


		// 성능을 측정할 데이터 크기
		int numElements = 1000000;
		System.out.println("\n<B-Tree Vs B-Tree Degree>");
		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree1.insert(i);
			}
		}, "[B Tree 3] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree2.insert(i);
			}
		}, "[B Tree 7] Insert time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree1.search(i);
			}
		}, "[B Tree 3] Search time:");

		measureExecutionTime(() -> {
			for (int i = 0; i < numElements; i++) {
				bTree2.search(i);
			}
		}, "[B Tree 7] Search time:");
	}



	// 시간을 측정하고 해당 코드의 실행 시간을 반환하는 함수
	public static void measureExecutionTime(Runnable task, String taskName) {


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

