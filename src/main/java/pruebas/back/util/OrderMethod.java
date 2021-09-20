package pruebas.back.util;

import java.util.ArrayList;

public interface OrderMethod<E extends Data> {
	public abstract void order(BinaryTree<E> bt, ArrayList<E> data);
}
