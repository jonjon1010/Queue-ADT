package edu.uwm.cs351.util;

import java.util.NoSuchElementException;
import edu.uwm.cs.junit.LockedTestCase;

//
//CST 751 - Homework 7 
//Name: Jonathan Nguyen
//Due Date: 10/28/2019 10:00pm 
//
// Got help from Henry in understanding how the invariant and methods should work 
// for this homework works. In addition, I got help from Natasha and Daniel in 
// understanding how I should implement the BlockQueue for when clicking on the 
// buttons for random, order, 7 sup, 14 sup, and advance should work for the JPanel. 
//

/*
 * This class implements a generic Queue, but does not use Java's Queue interface.
 * It uses a circular array data structure.
 * It does not allow null elements.
 */
public class Queue<E> implements Cloneable {

	/** Constants */
	private static final int DEFAULT_CAPACITY = 1; // force early resizing

	/** Fields */
	private E[] data;
	private int front;
	private int manyItems;

	private boolean report(String s) {
		System.out.println("invariant error: " + s);
		return false;
	}

	/** Invariant */
	private boolean wellFormed() {
		// The invariant:
		
		// 0. the data array cannot be null
		// TODO
		if(data == null) return report("data can't be null!");
		
		// 1. front must be a valid index within the bounds of the array
		// TODO
		if(front < 0 || front >= data.length) return report("front index is out of bound, either less than zero or greater or equal to data.length!");
		
		// 2. manyItems must not be negative or more than the length of the array
		if(manyItems < 0 || manyItems > data.length) return report("manyItems is either negative or greater or equal to data.length");
		
		// 3. If manyItems is not equal to 0, there are no null values in
		//		the range that holds elements
		//			NB: This range *may* wrap around the array.
		// TODO
		
		if(manyItems != 0) {
			int index = front;
			for(int i = 0; i < manyItems; i++) {
				if(data[index] == null) return report("there is a null value in the range that holds elements!");
				index = nextIndex(index);
			}
		}
		
		return true;
	}

	/**
	 * a private helper method to compute where the back of the queue is
	 * AKA where enqueue should put the element
	 * NB: Do not use / or % operations.
	 * @return the index after the last element
	 */
	private int getBack(){
		// TODO Implement this. Math.floorMod may be helpful.
		return Math.floorMod(front - manyItems, data.length);
	}

	/**
	 * Helper function to advance through the circular array.
	 * Keep in mind the direction the queue goes in the array.
	 * NB: Do not use / or % operations.
	 * @param i the index
	 * @return the next index after i
	 */
	private int nextIndex(int i) {
		// TODO Implement this. No loops allowed, one "if" permitted.
		return Math.floorMod(i - 1, data.length);
	}

	private Queue(boolean ignored) {} // do not change: used by invariant checker.

	/** Create an empty Queue with capacity DEFAULT_CAPACITY. */
	public Queue() {
		//TODO Implement the constructor. Use makeArray.
		data = makeArray(DEFAULT_CAPACITY);
		assert wellFormed() : "invariant fails at end of constructor";
	}

	@SuppressWarnings("unchecked")
	private E[] makeArray(int s) {
		return (E[]) new Object[s];
	}

	/**
	 * Determine whether the queue is empty.
	 * @return true if queue is empty
	 */
	public boolean isEmpty() {
		//TODO very little work.
		assert wellFormed() : "invariant fails at start of isEmpty()";
		return manyItems == 0;
	}

	/**
	 * Compute how many elements are in the queue.
	 * @return how many elements are in this queue
	 */
	public int size() {
		// TODO very little work.
		assert wellFormed() : "invariant fails at start of size()";
		return manyItems;
	}

	/**
	 * Add an element to the queue,
	 * @param x the element to add, must not be null
	 * @exception IllegalArgumentException if x is null
	 */
	public void enqueue(E x) {
		// TODO (no loops, no ifs)
		assert wellFormed() : "invariant fails at start of enqueue()";
		if(x == null) throw new IllegalArgumentException("can't add null to queue!");
		ensureCapacity(manyItems + 1);
		data[getBack()] = x;
		manyItems++;
		assert wellFormed() : "invariant fails at end of enqueue()";
	}

	/**
	 * Return (but do not remove) the front element of this queue.
	 * @return element at front of queue
	 * @exception NoSuchElementException if the queue is empty
	 */
	public E front() {
		// TODO very little work. "if" only for error
		assert wellFormed() : "invariant fails at start of front()";
		if(isEmpty()) throw new NoSuchElementException("the queue is empty!");
		return data[front];
	}

	/**
	 * Remove and return the front element from the queue.
	 * @return element formerly at front of queue
	 * @exception NoSuchElementException if the queue is empty
	 */
	public E dequeue() {
		// TODO (no loops, "if" only for error)
		assert wellFormed() : "invariant fails at start of dequeue()";
		if(isEmpty()) throw new NoSuchElementException("the queue is empty!");
		E temp = data[front];
		data[front] = null;
		front= nextIndex(front);
		manyItems--;
		assert wellFormed() : "invariant fails at end of dequeue()";
		return temp;
	}
	
	@Override
	public String toString() {
		// TODO Return a String that contains the toString of each element, front to back
		assert wellFormed() : "invariant fails at start of toString()";
		String temp = "";
		int j = front;
		for(int i = 0; i < manyItems; i++) {
			temp += data[j].toString() + " ";
			j = nextIndex(j);
		}
		assert wellFormed() : "invariant fails at end of toString()";
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Queue<E> clone()
	{
		assert wellFormed() : "invariant fails at start of clone()";
		Queue<E> result = null;
		try {
			result = (Queue<E>) super.clone( );
		}
		catch (CloneNotSupportedException e) {  
			// Shouldn't happen
		}
		result.data = data.clone();
		assert wellFormed() : "invariant fails at end of clone()";
		return result;
	}

	/**
	 * Ensure that the capacity of the array is such that
	 * at least minCap elements can be in queue.  If necessary,
	 * the capacity is doubled and the elements are arranged
	 * in the queue correctly. There is generally more
	 * than one valid arrangement for your data in the array.
	 * @param minCap the minimum capacity
	 */
	private void ensureCapacity(int minCap) {
		// TODO
		if (data.length >= minCap) return;
		int newCapacity = Math.max(data.length*2, minCap);
		Queue<E> temp = new Queue<E>();
		Object[] newData = new Object[newCapacity];
		temp.data = (E[]) newData;
		int index = front;
		for (int i = 0; i < manyItems; i++) { 
			temp.enqueue(data[index]);
			index = nextIndex(index);
		}
		data = temp.data;
	}


	public static class TestInvariant extends LockedTestCase {
		private Queue<Object> self;

		protected void setUp() {
			self = new Queue<Object>(false);
		}

		public void test00() {
			//data array is null
			assertFalse(self.wellFormed());
		}

		public void test01() {
			self.data = new Object[0];
			//Think about why this isn't well formed. Which field gives us a problem?
			assertFalse(self.wellFormed());
			self.data = new Object[DEFAULT_CAPACITY];
			assertTrue(self.wellFormed());
		}

		public void test02() {
			self.data = new Object[DEFAULT_CAPACITY];

			self.front = -1;
			assertFalse(self.wellFormed());
			
			self.front = DEFAULT_CAPACITY;
			assertFalse(self.wellFormed());
			
			self.front = 0;
			assertTrue(self.wellFormed());
			
		}

		public void test03() {
			self.data = new Object[] { null, null, null, null };
			//manyItems is 0

			self.front = 1;
			assertEquals(Tb(696236041), self.wellFormed());

			self.front = 2;
			assertEquals(true, self.wellFormed());
			
			self.front = 3;
			assertEquals(true, self.wellFormed());
			
			self.front = 0;
			assertEquals(true, self.wellFormed());
		}
		
		public void test04() {
			self.data = new Object[] { null, null, null, null };
			//front is 0
			
			self.manyItems = -1;
			assertEquals(false, self.wellFormed());
			
			self.manyItems = 5;
			assertEquals(false, self.wellFormed());

			self.manyItems = 1;
			assertEquals(Tb(1983819536), self.wellFormed());
		}
		
		public void test05() {
			self.data = new Object[] { new Integer(5), new Integer(3), new Integer(2), new Integer(4) };
			self.front = 3;
			self.manyItems = 4;
			assertEquals(Tb(1024095790), self.wellFormed());
			self.front = 1;
			assertEquals(true, self.wellFormed());
			self.manyItems = 0;
			assertEquals(true, self.wellFormed());
		}

		public void test06() {
			self.data = new Object[] { null, null, 6, null };
			self.manyItems = 1;
			//we do not care about array data outside of the queue range
			
			self.front = 1;
			assertEquals(Tb(269466524), self.wellFormed());

			self.front = 3;
			assertEquals(Tb(1201608649), self.wellFormed());

			self.front = 2;
			assertEquals(Tb(946336364), self.wellFormed());
		}

		public void test07() {
			self.data = new Object[] { 2, null, 6, 0 };
			self.manyItems = 2;
			
			//remember the queue goes right-to-left from the front
			self.front = 0;
			assertEquals(Tb(942141733), self.wellFormed());
			
			self.front = 1;
			assertEquals(Tb(1599463611), self.wellFormed());
			
			self.front = 2;
			assertEquals(Tb(1852259318), self.wellFormed());
			
			self.front = 3;
			assertEquals(Tb(1681530588), self.wellFormed());
		}

		public void test08() {
			self.data = new Object[] { 2, null, 6, 0 };

			self.front = 0;
			self.manyItems = 3;
			assertEquals(true, self.wellFormed());
			
		}

		public void test09() {
			self.data = new Object[] { 2, null, null, 0 };

			self.front = 2;
			self.manyItems = 2;
			assertEquals(false, self.wellFormed());

			self.front = 3;
			self.manyItems = 1;
			assertEquals(true, self.wellFormed());

			self.front = 0;
			self.manyItems = 2;
			assertEquals(true, self.wellFormed());
		}

		public void test10() {
			self.data = new Object[] { null };
			self.front = 0;
			self.ensureCapacity(1);
			assertEquals(1, self.data.length);
		}

		public void test11() {
			self.data = new Object[] { 1, null };
			self.front = 0;
			self.manyItems = 1;
			self.ensureCapacity(1);
			assertEquals(2, self.data.length);
			self.ensureCapacity(2);
			assertEquals(2, self.data.length);
			self.ensureCapacity(3);
			assertEquals(4, self.data.length);
		}

		public void test12() {
			self.data = new Object[] { 1, 2, null };
			self.front = 1;
			self.manyItems = 2;
			self.ensureCapacity(3);
			assertEquals(3, self.data.length);
			self.ensureCapacity(6);
			assertEquals(6, self.data.length);
		}

		public void test13() {
			self.data = new Object[4];
			self.front = 0;
			self.ensureCapacity(300);
			assertEquals(300, self.data.length);
		}

		public void test14() {
			self.data = new Object[100];
			self.front = 0;
			self.ensureCapacity(101);
			assertEquals(200, self.data.length);
		}

		public void test15() {
			self.data = new Object[8];
			assertEquals(Ti(1882901848), self.nextIndex(7));
			assertEquals(Ti(466756724), self.nextIndex(1));
			assertEquals(Ti(1207052122), self.nextIndex(0));
		}
		
		
		public void test16() {
			self.data = new Object[] { null, null, 8, 3, 0 };
			self.front = 4;
			self.manyItems = 3;
			assertEquals(Ti(455994013), self.getBack());
			
			self.data = new Object[] { 3, 0, null, null, 8 };
			self.front = 1;
			self.manyItems = 3;
			assertEquals(Ti(52298907), self.getBack());
			
			self.data = new Object[] { 0, null, null, 8, 3 };
			self.front = 0;
			self.manyItems = 3;
			assertEquals(Ti(1968034367), self.getBack());
			
		}
		
		public void test17() {
			self.data = new Object[] {8, 3, 0 };
			self.front = 2;
			self.manyItems = 0;
			assertEquals(2, self.getBack());
			self.manyItems = 1;
			assertEquals(1, self.getBack());
			self.manyItems = 2;
			assertEquals(0, self.getBack());
			self.manyItems = 3;
			assertEquals(2, self.getBack());
		}
		
		public void test18() {
			self.data = new Object[] {4};
			self.front = 0;
			self.manyItems = 0;
			assertEquals(0, self.getBack());
			self.manyItems = 1;
			assertEquals(0, self.getBack());
		}
	}
}
