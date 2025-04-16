import java.util.concurrent.atomic.AtomicInteger;

// 공유 카운터 변수를 동시에 증가시키는 여러 스레드를 생성하고 시작하는 Java 프로그램을 작성하세요.
public class Problem1 {
	public static class IncrementThread extends Thread{
		private final Counter counter;
		private final int maxNumber;

		public IncrementThread(Counter counter, int maxNumber){
			this.counter = counter;
			this.maxNumber = maxNumber;
		}

		@Override
		public void run(){
			for(int i = 1; i <= maxNumber; i++){
				counter.increment();
			}
		}
	}

	public static class Counter{
		private int count = 0;

		public synchronized void increment(){
			count ++;
		}

		public int getCount() {
			return count;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		int maxNumber = 10000;
		int maxThread = 6;
		Counter counter = new Counter();
		IncrementThread[] incrementThreads = new IncrementThread[maxThread];
		for(int i = 0; i < incrementThreads.length; i++){
			incrementThreads[i] = new IncrementThread(counter, maxNumber);
			incrementThreads[i].start();
		}

		for (IncrementThread incrementThread : incrementThreads) {
			incrementThread.join();
		}

		System.out.println(counter.getCount());

		// ==

		AtomicInteger count = new AtomicInteger();

		Thread[] threads = new Thread[maxThread];
		for(int i = 0; i < maxThread; i++){
			threads[i] = new Thread(() -> {
				for(int x = 0; x < maxNumber; x++){
					count.getAndIncrement();
				}
			});
			threads[i].start();
		}

		// join의 역할은 다른 스레드를 기다려주는 것
		for (Thread thread : threads) {
			thread.join();
		}

		System.out.println(count);

	}
}
