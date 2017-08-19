package flibs.nodeList;

/**
 * Lista de nodos
 */
public class NodeList<T> {
	private Node<T> first; //El nodo con index 0
	private Node<T> tail;  //El nodo con index size-1
	private int size = 0;
	
	public void add(T o) {
		Node<T> newNode = new Node<T>(o);
		
		if (size == 0) {
			first = newNode;
			tail  = newNode;
		} else {
			tail.next = newNode;
			newNode.previous = tail;
			tail = newNode;
		}
		
		size++;
	}
	
	
	public void add(T o, int index) {
		Node<T> newNode     = new Node<T>(o);
		Node<T> currentNode = findNode(index);
		Node<T> beforeNode  = currentNode.previous;
		
		currentNode.previous = newNode;
		beforeNode.next      = newNode;
		newNode.previous     = beforeNode;
		newNode.next         = currentNode;
		
		size++;
		
	}
	
	public Node<T> findNode(int index) {
		Node<T> flag = null;
		
		flag = (index > size/2)? findNodeBackwise(index) : findNodeFrontwise(index);
			
		return flag;
	}
	
	public Node<T> findNode(Object obj) {
		Node<T> node = first;
		
		while (node.hasNext()) {
			if (node.obj == obj) break;
			else node = node.next;
		}
		
		return node;
	}
	
	public Node<T> findNodeFrontwise(int index) {
		Node<T> flag = first;
		
		for (int i = 0; i < index; i++) {
			flag = flag.next;
		}
		
		return flag;
	}
	
	public Node<T> findNodeBackwise(int index) {
		Node<T> flag = tail;
		
		for (int i = size -1; i > index; i--) {
			flag = flag.previous;
		}
		
		return flag;
	}
	
	public T find(int index) {
		return findNode(index).obj;
	}
	
	public T find(Object obj) {
		return findNode(obj).obj;
	}
	
	public boolean hasObject(Object obj) {
		return ( find(obj) != null );
	}
	
	public Node<T> remove(int index) {
		Node<T> node = findNode(index);
		node.remove();
		return node;
	}
	
	public Node<T> remove(Object obj) {
		Node<T> node = findNode(obj);
		node.remove();
		return node;
	}
	
	public void reposition(int index, int indexDestino) {
		Node<T> node = findNode(index);
		node.remove();
		
		Node<T> previous = findNode(indexDestino-1);
		Node<T> next     = previous.next;
		
		node.insertBetween( previous, next );
	}
	
	public void reposition(T obj, int indexDestino) {
		Node<T> node = findNode(obj);
		node.remove();
		
		Node<T> previous = findNode(indexDestino-1);
		Node<T> next     = previous.next;
		
		node.insertBetween( previous, next );
	}
	/*---------------- Getters y Setters -------------------*/
	public Node<T> getFirst() {
		return first;
	}
	public Node<T> getTail() {
		return tail;
	}
	public int getSize() {
		return size;
	}
	
}