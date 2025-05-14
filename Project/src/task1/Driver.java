package task1;

import java.util.Random;
import java.util.Scanner;

class FileLogger{
	
	final int MAX_BACKUP = 5;
	
}

public class Driver {
	
	private static final Random random = new Random();
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double cap;
		
		System.out.println("Please enter the cap of the temperature: ");
		cap = sc.nextDouble();
		
		double temperature = generateTemperature(cap);
		System.out.println("The randomly generated temperature is " + temperature);
		
		double humidity = generateHumidity();
		System.out.println("The randomly generated humidity is " + humidity + "%");
		
		int [] third_sensor = new int[3];
		
		for (int i=0; i<3; i++) {    // to get full marks define constant array size
			third_sensor[i] = generateThirdSensor();
			System.out.println("Value " + i + " of third sensor is " + third_sensor[i]);
		}

	}
	
	private static double generateTemperature(double cap) {
		return random.nextDouble() * cap;
	}
	private static double generateHumidity() {
		return random.nextDouble()*100;
	}
	private static int generateThirdSensor() {
		return (int) (random.nextDouble()*10);
	}
	private static double MajorityVoter(double [] third_sensor) {
		boolean flag01, flag12, flag02;
		float epsilon = 0.001;
		// x == y ------> Math.abs(x-y) < epsilon
		
	}

}
