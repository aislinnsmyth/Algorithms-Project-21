//code adapted from Algorithms 4th edition, authors being Robert Sedgewick and Kevin Wayne.
//https://algs4.cs.princeton.edu/13stacks/Bag.java.html

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<Item> implements Iterable<Item> {

	private Node<Item> first;		//beginning of bag
	private int n;					//number of elements in bag

	private static class Node<Item> {
		private Item item;
		private Node<Item> next;

	}

	public Bag() {
		first = null;
		n=0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return n;
	}

	public void add(Item item) {
		Node<Item> oldfirst = first;
		first = new Node<Item>();
		first.item = item;
		first.next = oldfirst;
		n++;
	}

	public Iterator<Item> iterator()  {
		return new LinkedIterator(first);  
	}

	// an iterator, doesn't implement remove() since it's optional
	private class LinkedIterator implements Iterator<Item> {
		private Node<Item> current;

		public LinkedIterator(Node<Item> first) {
			current = first;
		}

		public boolean hasNext()  { 
			return current != null;                   
		}

		public void remove()   
		{ 
			throw new UnsupportedOperationException(); 
		}

		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item item = current.item;
			current = current.next; 
			return item;
		}
	}
}
