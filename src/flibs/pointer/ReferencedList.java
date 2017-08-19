package flibs.pointer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReferencedList<T> implements List<T>{

	private ArrayList<T> valueList = new ArrayList<T>();
	private ArrayList<Pointer<T>> pointerList = new ArrayList<Pointer<T>>();
	
	
	
	/*-------------------------- Manejo de referencias -------------------------*/
	public Pointer<T> getRef(int index) {
		return pointerList.get(index);
	}
	
	/*--------------------------- Manejo de valores ---------------------------*/
	@Override
	public boolean add(T e) {
		boolean flag;
		
		flag = valueList.add(e);
		pointerList.add(new Pointer<T>(e));
		
		return flag;
	}
	@Override
	public void add(int index, T element) {
		valueList.add(index, element);
		pointerList.add(index, new Pointer<T>(element));
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean flag;
		
		ArrayList<Pointer<T>> pointers = new ArrayList<Pointer<T>>();
		for (T item : c) pointers.add(new Pointer<T>(item));
		
		flag = valueList.addAll(c);
		pointerList.addAll(pointers);
		
		return flag;
	}
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		boolean flag;
		
		ArrayList<Pointer<T>> pointers = new ArrayList<Pointer<T>>();
		for (T item : c) pointers.add(new Pointer<T>(item));
		
		flag = valueList.addAll(index, c);
		pointerList.addAll(index, pointers);
		
		return flag;
	}
	@Override
	public void clear() {
		valueList.clear();
		pointerList.clear();
	}
	@Override
	public boolean contains(Object o) {
		return valueList.contains(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return valueList.containsAll(c);
	}
	@Override
	public T get(int index) {
		return valueList.get(index);
	}
	@Override
	public int indexOf(Object o) {
		return valueList.indexOf(o);
	}
	@Override
	public boolean isEmpty() {
		return valueList.isEmpty();
	}
	@Override
	public Iterator<T> iterator() {
		return valueList.iterator();
	}
	@Override
	public int lastIndexOf(Object o) {
		return valueList.lastIndexOf(o);
	}
	@Override
	public ListIterator<T> listIterator() {
		return valueList.listIterator();
	}
	@Override
	public ListIterator<T> listIterator(int index) {
		return valueList.listIterator(index);
	}
	@Override
	public boolean remove(Object o) {
		int index = valueList.indexOf(o);
		
		if (index > -1) pointerList.remove(o);
		return valueList.remove(o);
	}
	@Override
	public T remove(int index) {
		T flag = valueList.remove(index);
		pointerList.remove(index);
		return flag;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean flag = false;
		
		for (Object item : c) {
			int index = valueList.indexOf(item);
			if (index > -1) {
				flag = true;
				valueList.remove(index);
				pointerList.remove(index);
			}
		}
		
		return flag;
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean flag = valueList.retainAll(c);
		
		if (flag) {
			pointerList.clear();
			for (T object : valueList) pointerList.add(new Pointer<T>(object));
		}
		
		return flag;
	}
	@Override
	public T set(int index, T element) {
		
		T flag = valueList.set(index, element);
		pointerList.set(index, new Pointer<T>(element));
		
		return flag;
	}
	@Override
	public int size() {
		return valueList.size();
	}
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return valueList.subList(fromIndex, toIndex);
	}
	@Override
	public Object[] toArray() {
		return valueList.toArray();
	}
	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return valueList.toArray(a);
	}
	

}
