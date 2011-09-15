import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item>
{
	private Node first, last;
	
	private class Node
	{
		Item item;
		Node next;
	}
	
	public boolean isEmpty()
	{ return first == null;}
	
	public Item peek()
	{
		if (first == null) return null;
		return first.item;
	}
	
	public void enqueue(Item item)
	{
		Node newlast = new Node();
		newlast.item = item;
		if (first == null)
		{
			first = newlast;
			last = newlast;
		}
		else
		{
			last.next = newlast;
			last = newlast;
		}
	}
	
	public Item dequeue()
	{
		if (first == null) return null;
		Item x = first.item;
		first = first.next;
		if (first == null) last = null;
		return x;
	}
	
	public Iterator<Item> iterator()
	{	return new ListIterator();}
	
	private class ListIterator implements Iterator<Item>
	{
		private Node current = first;
		
		public boolean hasNext()
		{	return current != null;}
		
		public void remove() 
		{throw new UnsupportedOperationException();}
		
		public Item next()
		{ 
			if (!hasNext()) throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
	}
	
	
	
}