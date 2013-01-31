package services;

import java.io.IOException;
import java.util.Scanner;

public class ShellScriptExecutor {
	
	public static String execute(String input) throws IOException {
		String output = new String();
		
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "shell-script.sh", input);
		Process process = pb.start();
		Scanner scanner = new Scanner(process.getInputStream());
		
		while (scanner.hasNext()) {
			output += scanner.nextLine();
		}
		
		return output;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(ShellScriptExecutor.execute("Sup bitches!"));
	}

}
