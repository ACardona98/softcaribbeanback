package pruebas.back.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pruebas.back.entidades.Usuario;

public class BinaryTree<E extends Data> {
	private Node<E> root;

	private static BinaryTree<Usuario> userTree;

	public static BinaryTree<Usuario> getUserTree() {
		if (userTree == null) {
			userTree = new BinaryTree<Usuario>();
		}
		return userTree;
	}

	public E find(int id) {
		return find(this.root, id);
	}

	private E find(Node<E> n, int id) {
		if (n == null) {
			return null;
		}
		if (n.getData().getId() == id) {
			return n.getData();
		} else if (id < n.getData().getId()) {
			return find(n.getLeft(), id);
		} else {
			return find(n.getRight(), id);
		}

	}

	public boolean isEmpty() {
		return this.root == null;
	}

	public void insert(E data) {
		if (this.root == null) {
			this.root = new Node<E>(data);
		} else {
			this.insert(this.root, data);
		}
	}

	public void insert(List<E> elements) {
		for (E element : elements) {
			this.insert(element);
		}
	}

	private void insert(Node<E> parent, E data) {
		if (data.getId() > parent.getData().getId()) {
			if (parent.getRight() == null) {
				parent.setRight(new Node<E>(data));
			} else {
				this.insert(parent.getRight(), data);
			}
		} else {
			if (parent.getLeft() == null) {
				parent.setLeft(new Node<E>(data));
			} else {
				this.insert(parent.getLeft(), data);
			}
		}
	}

	private void preorden(Node<E> n, ArrayList<E> data) {
		if (n != null) {
			// n.printData();
			data.add(n.getData());
			System.out.print(n.getData().getId());
			preorden(n.getLeft(), data);
			preorden(n.getRight(), data);
		}
	}

	private void inorden(Node<E> n, ArrayList<E> data) {
		if (n != null) {
			inorden(n.getLeft(), data);
			data.add(n.getData());
			System.out.print(n.getData().getId());
			inorden(n.getRight(), data);
		}
	}

	private void postorden(Node<E> n, ArrayList<E> data) {
		if (n != null) {
			postorden(n.getLeft(), data);
			postorden(n.getRight(), data);
			data.add(n.getData());
			System.out.print(n.getData().getId());
		}
	}

	public static void preorden(BinaryTree bt, ArrayList data) {
		bt.preorden(bt.root, data);
	}

	public static void inorden(BinaryTree bt, ArrayList data) {
		bt.inorden(bt.root, data);
	}

	public static void postorden(BinaryTree bt, ArrayList data) {
		bt.postorden(bt.root, data);
	}

	public List<E> getData(OrderMethod<E> method) {
		ArrayList<E> data = new ArrayList<E>();
		method.order(this, data);
		return data;
	}

	private void deleteDeepest(Node<E> delNode) {
		Queue<Node<E>> q = new LinkedList<Node<E>>();
		q.add(root);

		Node temp = null;

		while (!q.isEmpty()) {
			temp = q.peek();
			q.remove();

			if (temp == delNode) {
				temp = null;
				return;

			}
			if (temp.getRight() != null) {
				if (temp.getRight() == delNode) {
					temp.setRight(null);
					return;
				} else
					q.add(temp.getRight());
			}

			if (temp.getLeft() != null) {
				if (temp.getLeft() == delNode) {
					temp.setLeft(null);
					return;
				} else
					q.add(temp.getLeft());
			}
		}
	}

	public void delete(int id) {
		if (root == null)
			return;

		if (root.getLeft() == null && root.getRight() == null) {
			if (root.getData().getId() == id) {
				root = null;
				return;
			} else
				return;
		}

		Queue<Node<E>> q = new LinkedList<Node<E>>();
		q.add(root);
		Node<E> temp = null, idNode = null;

		while (!q.isEmpty()) {
			temp = q.peek();
			q.remove();

			if (temp.getData().getId() == id)
				idNode = temp;

			if (temp.getLeft() != null)
				q.add(temp.getLeft());

			if (temp.getRight() != null)
				q.add(temp.getRight());
		}

		if (idNode != null) {
			int x = temp.getData().getId();
			deleteDeepest(temp);
			idNode.getData().setId(x);
		}
	}

}
