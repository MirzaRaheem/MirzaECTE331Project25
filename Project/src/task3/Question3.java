package task3;

class Data {
	int A1, A2, A3, B1, B2, B3;
	boolean gotoA1 = false;
	boolean gotoB2 = false;
	boolean gotoA2 = false;
	boolean gotoB3 = false;
	boolean gotoA3 = false;
	// complete the rest
}

class Utility {
	public static int calculate(int n) {
		return (n * (n + 1)/2);
	}
}

class ThreadA extends Thread {
	private Data data;

	public ThreadA(Data data) {
		super();
		this.data = data;
	}
	
	public void run() {
		
		// FunA1 block
		synchronized(data) {
			data.A1 = Utility.calculate(500);
			System.out.println("A1 Completed " + data.A1);
			data.gotoB2 = true; // from this line to the next line we dont need this for B2
			data.notify();
		}
		
		// FunA2 block
		synchronized(data) {
			try {
			while(!data.gotoA2) {
				data.wait();
				System.out.println("I am in A2 waiting for B2 to send notification");
			}
			
			data.A2 = data.B2 + Utility.calculate(300);
			System.out.println("A2 Completed" + data.A2);
			data.gotoB3 = true;
			data.notify();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    // FunA3 block
		synchronized(data) {
			try {
			while(!data.gotoA3) {
				data.wait();
				System.out.println("I am in A3 waiting for B3 to send notification");
			}
			
			data.A3 = data.B3 + Utility.calculate(400);
			System.out.println("A3 Completed " + data.A3);
			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
}

	

public class Question3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        // look tutorial 5 question for the main 
	}

}
