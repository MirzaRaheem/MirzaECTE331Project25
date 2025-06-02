package task1;

import java.util.Random;
import java.util.Scanner;

public class Driver {

	private static final Random random = new Random();
	private static Scanner sc = new Scanner(System.in);
	private static final int SENSOR_COUNT = 3;
	private static int lastValidSensor3 = -1;
	private static final FileLogger logger = new FileLogger();

	public static void main(String[] args) {
		double cap;

		System.out.println("Please enter the cap of the temperature: ");
		cap = sc.nextDouble();

		while (true) {
			System.out.println("\n--- New Simulation Round ---");

			double temperature = generateTemperature(cap);
			System.out.println("The randomly generated temperature is " + temperature);

			double humidity = generateHumidity();
			System.out.println("The randomly generated humidity is " + humidity + "%");

			int[] third_sensor = new int[SENSOR_COUNT];
			for (int i = 0; i < SENSOR_COUNT; i++) {
				third_sensor[i] = generateThirdSensor();
				System.out.println("Value " + (i + 1) + " of third sensor is " + third_sensor[i]);
			}

			int votedValue = MajorityVoter(third_sensor);
			System.out.println("Final voted value for Sensor 3: " + votedValue);

			System.out.print("Run again? (y/n): ");
			String response = sc.next();
			if (!response.equalsIgnoreCase("y")) {
				System.out.println("Exiting simulation.");
				break;
			}
		}
	}

	private static double generateTemperature(double cap) {
		return random.nextDouble() * cap;
	}

	private static double generateHumidity() {
		return random.nextDouble() * 100;
	}

	private static int generateThirdSensor() {
		return (int) (random.nextDouble() * 10);
	}

	private static int MajorityVoter(int[] third_sensor) {
		boolean flag01, flag12, flag02;
		float epsilon = 0.001f;

		flag01 = Math.abs(third_sensor[0] - third_sensor[1]) < epsilon;
		flag12 = Math.abs(third_sensor[1] - third_sensor[2]) < epsilon;
		flag02 = Math.abs(third_sensor[0] - third_sensor[2]) < epsilon;

		if (flag01 || flag02) {
			lastValidSensor3 = third_sensor[0];
			return third_sensor[0];
		} else if (flag12) {
			lastValidSensor3 = third_sensor[1];
			return third_sensor[1];
		} else {
			// Log discrepancy when no majority
			String logMessage = "Discrepancy detected: 3.1=" + third_sensor[0]
					+ ", 3.2=" + third_sensor[1]
					+ ", 3.3=" + third_sensor[2]
					+ ". Falling back to last valid value: " + lastValidSensor3;
			logger.log("log.txt", logMessage);
			return lastValidSensor3;
		}
	}
}
