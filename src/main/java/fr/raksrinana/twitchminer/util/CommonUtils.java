package fr.raksrinana.twitchminer.util;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtils{
	@SneakyThrows
	public static void randomSleep(long delay, long delta){
		long actualDelay = delay - delta / 2 + ThreadLocalRandom.current().nextLong(delta);
		if(actualDelay > 0){
			Thread.sleep(actualDelay);
		}
	}
	
	/**
	 * Get a user input.
	 *
	 * @param message The message to be displayed before asking input.
	 *
	 * @return User input.
	 */
	@NotNull
	public static String getUserInput(@NotNull String message){
		System.out.println(message);
		
		var scanner = new Scanner(System.in);
		return scanner.nextLine();
	}
}