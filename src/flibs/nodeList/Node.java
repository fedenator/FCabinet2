package flibs.nodeList;

public class Node<T> {
	public T obj;
	public Node<T> previous;
	public Node<T> next;
	
	public Node(T obj) {
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		return obj.toString();
	}
	
	public boolean hasNext() {
		return (next != null);
	}
	
	public boolean hasPrevius() {
		return previous != null;
	}
	
	public void insertBetween(Node<T> previous, Node<T> next) {
		if (previous != null) previous.next = this;
		this.previous = previous;
		
		if (next != null) next.previous = this;
		this.next = next;
	}
	
	/**
	 * Reconecta los nodos para que no contengan a este.
	 * Esto no cambia el las referencias internas de este nodo,
	 * de modo que:
	 * A <--> B <--> C
	 * Quedaria:
	 * A <---------> C
	 * A <--- B ---> C
	 */
	public void remove() {
		if (next != null)     next.previous = this.previous;
		if (previous != null) previous.next = this.next;
	}
	
	public Node<T> removeAndMoveForward() {
		Node<T> next = this.next;
		this.remove();
		return next;
	}
	
	public Node<T> removeAndMoveBackward() {
		Node<T> previous = this.previous;
		this.remove();
		return previous;
	}
}
