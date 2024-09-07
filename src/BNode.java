import java.util.ArrayList;
import java.util.List;

public class BNode {
	private final List<Integer> key;
	private final List<BNode> children;

	public BNode(int degree) {
		key = new ArrayList<>(degree - 1);
		children = new ArrayList<>(degree);
	}
}
