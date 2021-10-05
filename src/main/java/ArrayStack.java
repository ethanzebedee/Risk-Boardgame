//Class written by Cathal Ó Mealláin Ó Faoláin
import java.util.*;

public class ArrayStack<E> implements Stack<E> {
	//The default capacity size
	public static final int CAPACITY=1000;
	//The two variables needed to implement an array stack
	private int t;
	private E data[];
	//This holds the max size of the array to ensure it isn't exceeded
	private int N;
	
	
	public static void main(String[] args) {
		ArrayStack<Integer> s = new ArrayStack<>();
		for(int i = 0; i < 10; ++i)
			s.push(i);
		System.out.println(s.size());
		System.out.println(s.toString());

	}
	
	//If no size is entered, just runs with default size
	public ArrayStack() {
		this(CAPACITY);
	}
	//Constructs the stack
	public ArrayStack (int capacity){
		this.t=-1;
		this.N=capacity+1;
		this.data= (E[]) new Object[capacity];
	}
	@Override
	public int size() {
		return t+1;
	}

	@Override
	public boolean isEmpty() {
		return t==-1;
	}

	@Override
	public void push(E e) {
		if(this.size()==N) {
			throw new IndexOutOfBoundsException("The size of the stack array's max capacity has been exceeded");
		}
		else {
		this.t++;
		this.data[t]=e;
		}
	}

	@Override
	public E top() {
		if(t==-1) {
		return null;
		}
		else {
			return this.data[t];
		}
	}

	//Doesn't actually need to delete the element, as moving t will mean that the element can be overwritten 
	@Override
	public E pop() {
		if(this.isEmpty()) {
			return null;
			}
			else {
				this.t--;
				return this.data[t+1];
			}
	}
	
	public String toString() {
		String result="[";
		for(int i=t;i>0;i--) {
			result=result+ data[i] + ", ";
		}
		result=result + data[0]+ "]";
		return result;
	}
	
}
