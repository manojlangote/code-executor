package com.code.exe.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
@Component
public class ExecutionUtils {

	private static final String compileJavaCommand = "javac";
	private static final String JAVA = "java";
	public static final String INPUT_FILE_PATH = "C:\\Users\\mlangote.EAD\\Documents\\workspace-spring-tool-suite-4-4.17.2.RELEASE\\CodeExexutor\\src\\main\\java\\com\\code\\exe\\utils\\userInputFile.txt";

	public String decodeEncryptedCode(String encryptedCodeText) {
		return new String(Base64.getDecoder().decode(encryptedCodeText));
	}

	public void executeCode(String language, String code, List<String> consoleOutputList) {
		List<String> compileCommands = new ArrayList<>();
		List<String> runCommands = new ArrayList<>();

		createFileAndCommands(language, code, compileCommands, runCommands);
		
		try {	
			//Compile code
			ProcessBuilder compileProcessBuilder = new ProcessBuilder(compileCommands);
			Process compileProcess = compileProcessBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				consoleOutputList.add(line);
			}

			BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
			while ((line = errorReader.readLine()) != null) {
				consoleOutputList.add(line);
			}

			int exitCode = compileProcess.waitFor();
			if (exitCode == 0) {
				//Run code if there are compilation issues.
				ProcessBuilder runProcessBuilder = new ProcessBuilder(runCommands);
				Process runProcess = runProcessBuilder.start();
				
				//Runtime reading user input from console
				String input = null;
				BufferedReader runReader = null;
				BufferedWriter writer;
				BufferedReader inputReader = new BufferedReader(new FileReader(INPUT_FILE_PATH));
				writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
				while (!runProcess.waitFor(1, TimeUnit.SECONDS)) {
					input = inputReader.readLine();
					if (input == null) {
						continue;
					}
					while (input != null) {
						writer.write(input + "\n");
						input = inputReader.readLine();
					}
					writer.flush();
				}
				inputReader.close();
				writer.close();
				//Adding code output to output list
				runReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
				while ((input = runReader.readLine()) != null) {
					consoleOutputList.add(input);
				}

				//Adding error to output list
				BufferedReader runErrorReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
				while ((input = runErrorReader.readLine()) != null) {
					consoleOutputList.add(input);
				}
				int runExitCode = runProcess.waitFor();
				if (runExitCode == 0) {
					System.out.println("Executed successfully");
				} else {
					System.out.println("Execution Failed");
				}
			} else {
				System.out.println("Compilation failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private String createFileAndCommands(String language, String code, List<String> compileCommands,
			List<String> runCommands) {
		
		switch (language) {
		case JAVA:
			return createFileAndCommandsForJava(code, compileCommands, runCommands);
		default:
			break;
		}
		return null;
	}
	private String createFileAndCommandsForJava(String code, List<String> compileCommands, List<String> runCommands) {
		File tempFile=null;
		String codeFileLocation=null;
		try {
			tempFile = File.createTempFile("Solution", ".java");
			codeFileLocation = tempFile.getAbsolutePath();
			Files.writeString(tempFile.toPath(), code);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		compileCommands.add(compileJavaCommand);
		compileCommands.add(codeFileLocation);
		runCommands.add(JAVA);
		runCommands.add(codeFileLocation);
		return codeFileLocation;
	}
}
