package task1;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class FileLogger {

	final int MAX_BACKUP = 5;
	private static final Random random = new Random();

	public void log(String baseFileName, String message) {
		String timestampedMessage = getCurrentTime() + " " + message;

		if (tryWrite(baseFileName, timestampedMessage)) return;

		// Try backup files
		for (int i = 1; i <= MAX_BACKUP; i++) {
			String backupFileName = baseFileName.replace(".txt", "") + i + ".txt";
			if (tryWrite(backupFileName, timestampedMessage)) return;
		}

		// Final fallback
		try (FileWriter writer = new FileWriter("principal_log.txt", true)) {
			writer.write(timestampedMessage + "\n");
			System.out.println("Successfully wrote to: principal_log.txt");
		} catch (IOException e) {
			System.err.println("Final fallback logging failed: " + e.getMessage());
		}
	}

	private boolean tryWrite(String filename, String message) {
		try {
			// Simulate 40% failure rate
			if (random.nextInt(100) < 40) {
				System.out.println("Simulated write failure for: " + filename);
				throw new IOException("Simulated write failure");
			}
			try (FileWriter writer = new FileWriter(filename, true)) {
				writer.write(message + "\n");
			}
			System.out.println("Successfully wrote to: " + filename);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private String getCurrentTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		return "[" + LocalDateTime.now().format(dtf) + "]";
	}
}
