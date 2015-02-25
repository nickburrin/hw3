import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {
	public static void main(String[] args){
		Problem1();
	}

	private static void Problem1(){
		ExecutorService threadPool = Executors.newCachedThreadPool();

		PQueue test = new PQueue(4);

		TestThread t1 = new TestThread("one", test);
		TestThread t2 = new TestThread("two", test);
		TestThread t3 = new TestThread("three", test);
		TestThread t4 = new TestThread("four", test);
		TestThread t5 = new TestThread("five", test);
		TestThread t6 = new TestThread("six", test);
		TestThread t7 = new TestThread("seven", test);

		Future<?> f1 = threadPool.submit(t1);
		Future<?> f2 = threadPool.submit(t2);
		Future<?> f3 = threadPool.submit(t3);
		Future<?> f4 = threadPool.submit(t4);
		Future<?> f5 = threadPool.submit(t5);
		Future<?> f6 = threadPool.submit(t6);
		Future<?> f7 = threadPool.submit(t7);

		try{
			f1.get();
			f2.get();
			f3.get();
			f4.get();
			f5.get();
			f6.get();
			f7.get();
		} catch(Exception e){e.printStackTrace();}
		finally{
			threadPool.shutdown();
		}
	}
}

class TestThread implements Runnable{
	String name;
	PQueue priQueue;
	Random randGen = new Random();

	public TestThread(String n, PQueue p){
		this.name = n;
		this.priQueue = p;
	}

	@Override
	public void run() {
		while(true){
			int p;
			System.out.println("Added \"" + this.name + "\" at index " + priQueue.insert(this.name, p = randGen.nextInt(10)) + " with priority " + p);
			
			//try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}

			System.out.println("Found \"" + this.name + "\" at index " + priQueue.search(this.name));
			
			try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}

			System.out.printf("Removing the first element \"%s\" with priority %d\n", priQueue.getFirst(), p);
			//System.out.println("Found " + this.name + " at index " + priQueue.search(this.name));
		}
	}
}
