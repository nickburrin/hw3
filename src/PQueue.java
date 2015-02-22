

/**
 * A linked list based priority queue. Each node in the linked list 
 * has two fields: a name and a priority, with 9 being the highest 
 * priority and 0 being the lowest. Nodes with the same priority are 
 * kept in the order of which they were inserted. Any thread that invokes 
 * insert on a full list must be blocked, and wait for space to become available.
 * 
 * @author Nick Burrin
 *
 */
public class PQueue {
	private int size;
	private int MAX;
	/**
	 * Creates a Priority queue with max size m
	 * @param m
	 */
	public PQueue(int m){
		this.MAX = m;
		this.size = 0;
	}
	
	/**
	 * Inserts the name with its priority in the PQueue
	 * Returns -1 if name exists in the list
	 * Otherwise returns the current position in the list where the name was inserted
	 * The method will block when the list is full
	 * @param name
	 * @param pri
	 * @return
	 */
	public int insert(String name, int pri){
		if(list.contains(name))
			return -1;
		
		waituntil(size < MAX);
		int index = list.add(name);
		size++;
		
		
		return index;
	}
	
	/**
	 * Returns the position of the name in the list
	 * Returns -1 if the name is not found
	 * @param name
	 * @return
	 */
	public int search(String name){
		int index = list.indexOF(name);
		
		return index;
	}
	
	/**
	 * Returns the name with the highest priority in the list
	 * If the list is empty, the method blocks
	 * Finally, the name is deleted from the list
	 * @return
	 */
	public String getFirst(){
		waituntil(size > 0);
		size--;
		return list.remove(0);
	}
}
