package task3;

class Data {
	int A1, A2, A3, B1, B2, B3;
	boolean gotoB2 = false;
	boolean gotoA2 = false;
	boolean gotoB3 = false;
	boolean gotoA3 = false;
	boolean allowC = false; // flag to allow ThreadC to proceed
}

class Utility {
	public static int calculate(int n) {
		return (n * (n + 1) / 2);
	}
}

// ThreadA performs A1 -> A2 -> A3 based on B2, B3
class ThreadA extends Thread {
	private Data data;

	public ThreadA(Data data) {
		super();
		this.data = data;
	}

	public void run() {
		// FunA1
		synchronized (data) {
			data.A1 = Utility.calculate(500);
			System.out.println("A1 Completed " + data.A1);
			data.gotoB2 = true;
			data.notify();
		}

		// FunA2
		synchronized (data) {
			try {
				while (!data.gotoA2) {
					System.out.println("I am in A2 waiting for B2 to send notification");
					data.wait();
				}
				data.A2 = data.B2 + Utility.calculate(300);
				System.out.println("A2 Completed " + data.A2);
				data.gotoB3 = true;
				data.notifyAll(); // notify B3 and maybe C
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// FunA3
		synchronized (data) {
			try {
				while (!data.gotoA3) {
					System.out.println("I am in A3 waiting for B3 to send notification");
					data.wait();
				}
				data.A3 = data.B3 + Utility.calculate(400);
				System.out.println("A3 Completed " + data.A3);
				data.allowC = true;
				data.notifyAll(); // now C can proceed
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

// ThreadB performs B1 -> B2 -> B3 based on A1 and A2
class ThreadB extends Thread {
	private Data data;

	public ThreadB(Data data) {
		super();
		this.data = data;
	}

	public void run() {
		// FunB1
		synchronized (data) {
			data.B1 = Utility.calculate(250);
			System.out.println("B1 Completed " + data.B1);
		}

		// FunB2
		synchronized (data) {
			try {
				while (!data.gotoB2) {
					System.out.println("I am in B2 waiting for A1 to send notification");
					data.wait();
				}
				data.B2 = data.A1 + Utility.calculate(200);
				System.out.println("B2 Completed " + data.B2);
				data.gotoA2 = true;
				data.notifyAll(); // notify A2
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// FunB3
		synchronized (data) {
			try {
				while (!data.gotoB3) {
					System.out.println("I am in B3 waiting for A2 to send notification");
					data.wait();
				}
				data.B3 = data.A2 + Utility.calculate(400);
				System.out.println("B3 Completed " + data.B3);
				data.gotoA3 = true;
				data.notifyAll(); // notify A3 and C
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

// ThreadC performs after A3 completion
class ThreadC extends Thread {
	private Data data;

	public ThreadC(Data data) {
		super();
		this.data = data;
	}

	public void run() {
		synchronized (data) {
			try {
				while (!data.allowC) {
					System.out.println("I am in C waiting for A3 to send notification");
					data.wait();
				}
				int result = data.A2 + data.B3;
				System.out.println("C Result: A2 + B3 = " + result);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

// Main class
public class Question3 {

	public static void main(String[] args) {
		// Referred to tutorial 5's ProducerConsumer structure for this part
		for (int i = 0; i < 5; i++) {
			System.out.println("\n>>> Iteration " + (i + 1));

			Data data = new Data();

			ThreadA a = new ThreadA(data);
			ThreadB b = new ThreadB(data);
			ThreadC c = new ThreadC(data);

			a.start();
			b.start();
			c.start();

			try {
				a.join();
				b.join();
				c.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("<<< End of Iteration " + (i + 1));
		}
	}
}