package mab.util;

/**
 * Created by Allquantor on 26.10.14.
 */
public class Tuple<T,M> {

	private final T left;
	private final M right;


	public Tuple(T left,M right) {
		this.left = left;
		this.right = right;
	}

	public T getLeft() {
		return left;
	}
	public M getRight() {
		return right;
	}


}
