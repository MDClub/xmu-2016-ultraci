package com.uitraci.hotel.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class LimitQueue<E> implements Queue<E>{

	private int limit;
	Queue<E> queue = new LinkedList<E>();
	
	public LimitQueue(int limit) {
		this.limit = limit;
	}
	
	 public Queue<E> getQueue(){  
	        return queue;  
	    }  
	 
	public int getLimit(){
		return limit;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return queue.size() == 0 ? true : false;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return queue.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return queue.iterator();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return queue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return queue.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return queue.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return queue.contains(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return queue.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return queue.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return queue.retainAll(c);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		queue.clear();
	}

	@Override
	public boolean add(E e) {
		// TODO Auto-generated method stub
		return queue.add(e);
	}

	@Override
	public boolean offer(E e) {
		// TODO Auto-generated method stub
		if(queue.size() >= limit){  
            //如果超出长度,入队时,先出队  
            queue.poll();  
        }  
        return queue.offer(e);
	}

	@Override
	public E remove() {
		// TODO Auto-generated method stub
		return queue.remove();
	}

	@Override
	public E poll() {
		// TODO Auto-generated method stub
		return queue.poll();
	}

	@Override
	public E element() {
		// TODO Auto-generated method stub
		return queue.element();
	}

	@Override
	public E peek() {
		// TODO Auto-generated method stub
		return queue.peek();
	}

}
