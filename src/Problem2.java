import java.util.LinkedList;
import java.util.Queue;

// wait() 및 notify() 메서드를 사용하여 스레드 동기화를 위한 생산자-소비자 시나리오를 만드는 Java 프로그램을 작성하세요.
public class Problem2 {
	private static final Integer BUFFER = 5;
	private static final Queue<Integer> queue = new LinkedList<>();

	public static class Producer implements Runnable{
		@Override
		public void run() {
			int value = 0;
			while (true) {
				synchronized (queue) {
					// if가 아닌 while로 체크하는 이유
					// if로 하면 “spurious wakeup”이나 여러 스레드가 동시에 wait/notify에 접근할 때 예상치 못한 버그가 발생할 수 있음
					while(queue.size() == BUFFER) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}

					System.out.println("Producer: " + value);
					queue.add(value++);
					queue.notify();
				}
			}
		}
	}

	public static class Consumer implements Runnable{
		@Override
		public void run() {
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}

					System.out.println("Consumer: " + queue.poll());
					queue.notify();
				}
			}
		}
	}

	public static void main(String[] args) {
		Thread producer = new Thread(new Producer());
		Thread consumer = new Thread(new Consumer());
		producer.start();
		consumer.start();
	}
}
