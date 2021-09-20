package pruebas.back.util;

public class Node <E> {
	private E data;
    private Node<E> left, right;

    public Node(E data) {
        this.data = data;
        this.left = this.right = null;
    }


    public E getData() {
        return data;
    }

    public Node<E> getLeft() {
        return left;
    }

    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public Node<E> getRight() {
        return right;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }
}
