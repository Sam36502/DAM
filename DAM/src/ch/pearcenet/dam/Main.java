package ch.pearcenet.dam;

import java.io.File;

public class Main {
	
	public static boolean logsEnabled = false;
	public static boolean keepFiles = false;
	
	public static void main(String[] args) {
		
		//Variables for the VM and Language
		Tag mainTag = null;
		String stdinString = "";
		
		//Help menu
		if (argmumentIndex(args, "help") != -1) {
			helpMenu();
			System.exit(0);
		}
		
		//Check the number of arguments is correct
		if (args.length < 1) {
			System.out.println("Too few arguments.\n");
			helpMenu();
			System.exit(0);
		}
		
		//Set all logs to be displayed
		if (argmumentIndex(args, "log") != -1) {
			logsEnabled = true;
			Main.log("Logs are enabled.");
		}
		
		//Set a custom tag to be used
		if (argmumentIndex(args, "tag") != -1) {
			int tIndex = argmumentIndex(args, "tag");
			String customTag = args[tIndex + 1];
			mainTag = new Tag(customTag);
		} else {
			mainTag = new Tag();
		}
		
		//Set what input should be passed to the vm
		if (argmumentIndex(args, "in") != -1) {
			int iIndex = argmumentIndex(args, "in");
			stdinString = args[iIndex + 1];
		}
		
		//Set the temp files to not be deleted
		if (argmumentIndex(args, "save") != -1) {
			Main.log("Temporary files will be kept.");
			keepFiles = true;
		}
		
		//Start Transpiling
		String program = args[args.length - 1];
		Main.log("Loading " + program + "...");
		Transpiler tp = new Transpiler(program, mainTag);
		tp.transpile();
		
		//Get the name of the program and write the transpiled code to the temp file
		File progFile = new File(program);
		tp.writeToFile("tmp/" + progFile.getName() + "-" + mainTag.getTag() + ".txt");
		
		//TODO: Add compiler steps here
		
		RandVM vm = new RandVM(mainTag, stdinString);
		
	}
	
	//Logs to the screen if logging is enabled
	public static void log(String msg) {
		if (logsEnabled) {
			System.out.println(msg);
		}
	}
	
	//Checks if a String array contains a value
	public static int argmumentIndex(String[] arr, String val) {
		for (int i=0; i<arr.length; i++) {
			if (("--" + val).equals(arr[i]) ||
				("-" + val.charAt(0)).equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}
	
	//Help Menu
	//TODO: Needs some fixing up
	public static void helpMenu() {
		System.out.println("Usage:\n" + 
				"  DAM.bat [-ht] program\n" + 
				"\n" + 
				"Options:\n" + 
				"  -h, --help      Display the help menu\n" + 
				"  -t, --tag tag   Run a program with a specific tag\n" + 
				"  -l, --log       Runs program and shows info about the process.\n" + 
				"  -i, --in input  Starts the program with input\n" +
				"  -s, --save      Saves the temporary files from transpiling\n" + 
				"                  and compiling, for debugging.\n" + 
				"\n" + 
				"Examples:\n" + 
				"  DAM.bat -h\n" + 
				"  DAM.bat helloworld.dam\n" + 
				"  DAM.bat -t 674daf093e8b51c2948f5bb14b426c3d helloworld.dam");
	}

}
