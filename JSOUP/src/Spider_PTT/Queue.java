package Spider_PTT;

import java.util.LinkedList;

public class Queue {

	private static LinkedList queue = new LinkedList();
	
	public void enQueue (Object t){
		queue.addLast(t);
	}
	public Object deQueue(){
		return queue.removeLast();
	}
	
	public boolean isQueueEmpty(){
		return queue.isEmpty();
	}
	
	public boolean contians(Object t){
		return queue.contains(t);
	}
	
	public boolean empty(){
		return queue.isEmpty();
	}
}
