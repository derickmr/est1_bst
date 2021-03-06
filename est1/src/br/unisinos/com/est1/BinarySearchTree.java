//

package br.unisinos.com.est1;

import java.util.ArrayDeque;
import java.util.Queue;

public class BinarySearchTree<K extends Comparable<K>, V> implements BinarySearchTreeADT<K, V> {

	protected Node root;

	protected class Node {

		private K key;
		private V value;
		private Node left, right;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public Node next(K other) {
			return other.compareTo(key) < 0 ? left : right;
		}

		public boolean isLeaf() {
			return left == null && right == null;
		}

		@Override

		public String toString() {
			return "" + key;
		}

	}

	@Override
	public void clear() {
		root = null;

	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public V search(K key) {
		return search(root, key);
	}

	private V search(Node node, K key) {
		if (node == null) {
			return null;
		} else if (key.compareTo(node.key) == 0) {
			return node.value;
		}
		return search(node.next(key), key);
	}

	@Override
	public void insert(K key, V value) {
		root = insert(root, key, value);
	}

	private Node insert(Node node, K key, V value) {
		if (node == null) {
			return new Node(key, value);
		} else if (key.compareTo(node.key) > 0) {
			node.right = insert(node.right, key, value);
		} else if (key.compareTo(node.key) < 0) {
			node.left = insert(node.left, key, value);
		}

		return node;
	}

	@Override
	public String toString() {
		return root == null ? "[empty]" : printTree(new StringBuffer());
	}

	private String printTree(StringBuffer sb) {
		if (root.right != null) {
			printTree(root.right, true, sb, "");
		}
		sb.append(root + "\n");
		if (root.left != null) {
			printTree(root.left, false, sb, "");
		}

		return sb.toString();
	}

	private void printTree(Node node, boolean isRight, StringBuffer sb, String indent) {
		if (node.right != null) {
			printTree(node.right, true, sb, indent + (isRight ? "        " : " |      "));
		}
		sb.append(indent + (isRight ? " /" : " \\") + "----- " + node + "\n");
		if (node.left != null) {
			printTree(node.left, false, sb, indent + (isRight ? " |      " : "        "));
		}
	}

	@Override
	public boolean delete(K key) {
		return deleteByMerging(key);
	}

	private boolean deleteByMerging(K key) {
		Node parent = null, current = root;
		for (; current != null && key.compareTo(current.key) != 0; parent = current, current = current.next(key))
			;

		if (current == null)
			return false;
		else if (current.left != null && current.right != null) {
			// Caso 3
			Node tmp = current.left;
			while (tmp.right != null)
				tmp = tmp.right;
			tmp.right = current.right;

			if (current.equals(root))
				root = current.left;
			else if (parent.left.equals(current))
				parent.left = current.left;
			else
				parent.right = current.left;
		} else {
			// Caso 1 ou Caso 2
			Node nextNode = current.right == null ? current.left : current.right;
			if (current.equals(root))
				root = nextNode;
			else if (parent.left.equals(current))
				parent.left = nextNode;
			else
				parent.right = nextNode;
		}

		return true;
	}

	private boolean deleteByCopying(K key) {
		Node parent = null, current = root;
		for (; current != null && key.compareTo(current.key) != 0; parent = current, current = current.next(key))
			;

		if (current == null)
			return false;
		else if (current.left != null && current.right != null) {
			// Caso 3
			Node tmp = current.left;
			while (tmp.right != null)
				tmp = tmp.right;
			deleteByCopying(tmp.key);
			current.key = tmp.key;
		} else {
			// Caso 1 ou Caso 2
			Node nextNode = current.right == null ? current.left : current.right;
			if (current.equals(root))
				root = nextNode;
			else if (parent.left.equals(current))
				parent.left = nextNode;
			else
				parent.right = nextNode;
		}

		return true;
	}

	@Override
	public void preOrder() {
		preOrder(root);
	}

	private void preOrder(Node node) {
		if (node != null) {
			System.out.print(node + " ");
			preOrder(node.left);
			preOrder(node.right);
		}
	}

	@Override
	public void inOrder() {
		inOrder(root);
	}

	private void inOrder(Node node) {
		if (node != null) {
			inOrder(node.left);
			System.out.print(node + " ");
			inOrder(node.right);
		}
	}

	@Override
	public void postOrder() {
		postOrder(root);
	}

	private void postOrder(Node node) {
		if (node != null) {
			postOrder(node.left);
			postOrder(node.right);
			System.out.print(node + " ");
		}
	}

	public void levelOrder() {
		levelOrder(root);
	}

	private void levelOrder(Node node) {

		if (node != null) {
			Queue<Node> level = new ArrayDeque<>();
			level.add(node);
			Node current = null;
			while (!level.isEmpty()) {
				current = level.poll();
				System.out.println(current + " ");
				if (current.left != null)
					level.add(current.left);
				if (current.right != null)
					level.add(current.right);
			}
		}

	}

	@Override
	public int countNodes() {

		if (isEmpty())
			return 0;

		return 1 + countNodes(root.left) + countNodes(root.right);

	}

	private int countNodes(Node node) {

		if (node != null)
			return 1 + (countNodes(node.left) + countNodes(node.right));

		return 0;

	}

	@Override
	public int countInternalNodes() {

		if (isEmpty())
			return 0;

		return countInternalNodes(root.left) + countInternalNodes(root.right);
	}

	private int countInternalNodes(Node node) {

		if (node != null && !node.isLeaf()) {

			return 1 + (countInternalNodes(node.right) + countInternalNodes(node.left));
		}

		return 0;

	}

	@Override
	public int countLeaves() {

		if (isEmpty() || root.isLeaf())
			return 0;

		return countLeaves(root.left) + countLeaves(root.right);

	}

	private int countLeaves(Node node) {

		if (node == null)
			return 0;

		if (!node.isLeaf())
			return countLeaves(node.left) + countLeaves(node.right);

		return 1;

	}

	@Override
	public int degree(K key) {
		if (isEmpty() || search(key) == null)
			return -1;

		return degree(root.left, root.key, key) + degree(root.right, root.key, key);
	}

	private int degree(Node node, K fatherKey, K searchedKey) {

		if (node != null) {
			if (fatherKey.compareTo(searchedKey) == 0)

				return 1 + degree(node.left, node.key, searchedKey) + degree(node.right, node.key, searchedKey);

			return degree(node.left, node.key, searchedKey) + degree(node.right, node.key, searchedKey);

		}

		return 0;

	}

	@Override
	public int degreeTree() {

		if (isEmpty())
			return -1;

		return degreeTree(root, 0);

	}

	private int degreeTree(Node node, int maior) {

		if (node != null) {

			int deg = degree(node.key);

			if (deg > maior)
				maior = deg;

			return Math.max(degreeTree(node.left, maior), degreeTree(node.right, maior));

		}

		return maior;

	}

	@Override
	public int height(K key) {
		Node node = getNode(root, key);

		if (node == null)
			return -1;

		return height(node, 0);
	}

	private int height(Node node, int h) {

		if (node != null && !node.isLeaf()) {

			int left = height(node.left, h + 1);
			int right = height(node.right, h + 1);

			if (left > h)
				h = left;
			if (right > h)
				h = right;

		}

		return h;

	}

	@Override
	public int heightTree() {

		if (isEmpty())
			return -1;

		return height(root.key);

	}

	private Node getNode(Node node, K key) {
		if (node == null) {
			return null;
		} else if (key.compareTo(node.key) == 0) {
			return node;
		}
		return getNode(node.next(key), key);
	}

	@Override
	public int depth(K key) {

		if (search(key) == null)
			return -1;

		return depth(root, key, 0);

	}

	private int depth(Node node, K key, int dpth) {

		if (node.key.compareTo(key) != 0) {

			return depth(node.next(key), key, dpth + 1);

		}

		return dpth;

	}

	@Override
	public String ancestors(K key) {

		if (search(key) == null)
			return null;

		return ancestors(root, key, "");

	}

	private String ancestors(Node node, K key, String anc) {

		anc += node.key + " ";

		if (node.key.compareTo(key) != 0)
			return ancestors(node.next(key), key, anc);

		return anc;

	}

	@Override
	public String descendents(K key) {

		Node node = null;

		if (!isEmpty())
			node = getNode(root, key);

		if (node == null)
			return null;

		return node.key + " " + descendents(node.left) + descendents(node.right);
	}

	private String descendents(Node node) {

		if (node != null)
			return node.key + " " + (descendents(node.left) + descendents(node.right));

		return "";

	}

}
