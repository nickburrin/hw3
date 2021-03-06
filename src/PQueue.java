import java.util.ArrayList;
/** Import ImplicitMonitor Library **/
import autosynch.*;


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
	/** Create Monitor Object. **/
	private final AbstractImplicitMonitor monitor_1892124631 = 
			new NaiveImplicitMonitor();

	ArrayList<Node> list;
	private int MAX;
	/**
	 * Creates a Priority queue with max size m
	 * @param m
	 */
	public PQueue(int m){
		list = new ArrayList<Node>();
		this.MAX = m;
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
		/* monitor */
		monitor_1892124631.enter();

		Node newNode = new Node(name, pri);
		if(list.contains(newNode)){
			int ret_944706736 =  -1;

			/* leave monitor */
			monitor_1892124631.leave();
			return ret_944706736;

		}
		if (!(list.size() < MAX)) {
			/* Create Condition Variable*/
			AbstractCondition condition_1339509681 = monitor_1892124631.makeCondition(
					new Assertion() {
						public boolean isTrue() {
							return (list.size() < MAX);
						}
					}
					);
			condition_1339509681.await();
		}


		for(int i = 0; i < list.size(); i++){
			if(pri > list.get(i).priority){	
				//Scan the list until you find the first element with a lower priority
				list.add(i, newNode);
				int ret_204808045 =  i;

				/* leave monitor */
				monitor_1892124631.leave();
				return ret_204808045;

			}
		}

		//If you've made it here, then this element has the lowest priority
		//Insert it as the last element (or the first element is list is empty)
		list.add(newNode);
		int ret_1940009821 =  list.size() - 1;

		/* leave monitor */
		monitor_1892124631.leave();
		return ret_1940009821;

	}

	/**
	 * Returns the position of the name in the list
	 * Returns -1 if the name is not found
	 * @param name
	 * @return
	 */
	public int search(String name){
		/* monitor */
		monitor_1892124631.enter();

		int index = list.indexOf(new Node(name, -1));
		int ret_1783184229 =  index;

		/* leave monitor */
		monitor_1892124631.leave();
		return ret_1783184229;

	}

	/**
	 * Returns the name with the highest priority in the list
	 * If the list is empty, the method blocks
	 * Finally, the name is deleted from the list
	 * @return
	 */
	public String getFirst(){
		/* monitor */
		monitor_1892124631.enter();

		if (!(list.size() > 0)) {
			/* Create Condition Variable*/
			AbstractCondition condition_718198498 = monitor_1892124631.makeCondition(
					new Assertion() {
						public boolean isTrue() {
							return (list.size() > 0);
						}
					}
					);
			condition_718198498.await();
		}

		String ret_1904059746 =  list.remove(0).name;

		/* leave monitor */
		monitor_1892124631.leave();
		return ret_1904059746;

	}
}

/**
 * Stores the priority and String value so that 
 * we don't have to use a mapping that involves keys.
 */
class Node{
	String name;
	int priority;

	public Node(String n, int p){
		this.name = n;
		this.priority = p;
	}

	public boolean equals(Object other){
		if(other instanceof Node){
			return this.name.equals(((Node)other).name);
		}
		else{
			return false;
		}
	}

	public int hashCode(){
		return this.name.length() * this.priority;
	}

	public String toString(){
		return name + "-" + priority;
	}
}
