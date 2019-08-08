package ch.pearcenet.dam;

import java.io.File;

public class Main {
				
	private static final String VERSION = "1.1";
	
	public static boolean logsEnabled = false;
	public static boolean keepFiles = false;
	public static boolean showProg = false;
	
	public static void main(String[] args) {
		
		//Variables for the VM and Language
		Tag mainTag = null;
		String stdinString = "";
		
		//Help menu
		if (argumentIndex(args, "help") != -1) {
			helpMenu();
			System.exit(0);
		}
		
		//Print Version
		if (argumentIndex(args, "version") != -1) {
			System.out.println("DAM Version " + VERSION);
			System.exit(0);
		}
		
		//Check the number of arguments is correct
		if (args.length < 1) {
			System.out.println("Too few arguments.\n");
			helpMenu();
			System.exit(0);
		}
		
		//Set all logs to be displayed
		if (argumentIndex(args, "log") != -1) {
			logsEnabled = true;
			Main.log("Logs are enabled.");
		}
		
		//Set to display the program while it's running
		if (argumentIndex(args, "prog-show") != -1) {
			Main.log("Program will be displayed while it's running");
			showProg = true;
		}
		
		//Set what input should be passed to the vm
		if (argumentIndex(args, "in") != -1) {
			int iIndex = argumentIndex(args, "in");
			stdinString = args[iIndex + 1];
			Main.log("Input set to '" + stdinString + "'.");
		}
		
		//Set the temp files to not be deleted
		if (argumentIndex(args, "save-tmp") != -1) {
			Main.log("Temporary files will be kept.");
			keepFiles = true;
		}
		
		//Set a custom tag to be used
		if (argumentIndex(args, "tag") != -1) {
			int tIndex = argumentIndex(args, "tag");
			String customTag = args[tIndex + 1];
			mainTag = new Tag(customTag);
		} else {
			mainTag = new Tag();
		}
		
		//Start Transpiling
		Main.log("--------------------\nTranspiler:\n--------------------\n");
		String program = args[args.length - 1];
		Main.log("Loading " + program + "...");
		Transpiler tp = new Transpiler(program, mainTag);
		tp.transpile();
		
		//Get the name of the program and write the transpiled code to the temp file
		File progFile = new File(program);
		String transFile = "tmp/" + progFile.getName() + "-transpiled-" + mainTag.getTag() + ".txt";
		tp.writeToFile(transFile);
		
		//Start Compiling
		Main.log("--------------------\nCompiler:\n--------------------\n");
		Compiler cp = new Compiler(transFile, mainTag);
		String compFile = "tmp/" + progFile.getName() + "-compiled-" + mainTag.getTag() + ".txt";
		cp.compile(compFile);
		
		//Delete temporary transpiled file
		if (!keepFiles) new File(transFile).delete();
		
		//Run the VM with the newly compiled file
		Main.log("--------------------\nVirtual Machine:\n--------------------\n");
		RandVM vm = new RandVM(mainTag, stdinString);
		vm.loadBinary(compFile);
		vm.runProg();
		
		//Delete the temporary compiled file
		if (!keepFiles) new File(compFile).delete();
		
	}
	
	//Logs to the screen if logging is enabled
	public static void log(String msg) {
		if (logsEnabled) {
			System.out.println(msg);
		}
	}
	
	//Checks if a String array contains a value
	public static int argumentIndex(String[] arr, String val) {
		for (int i=0; i<arr.length; i++) {
			if (("--" + val).equals(arr[i]) ||
				("-" + val.charAt(0)).equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}
	
	//Returns the index of an element in an array (-1 if no result)
	public static int indexInArray(String[] arr, String element) {
		for (int i=0; i<arr.length; i++) {
			if (element.equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}
	
	//Help Menu
	public static void helpMenu() {
		System.out.println("Usage:\n" + 
				"  DAM.bat [OPTIONS] program\n" + 
				"\n" + 
				"Options:\n" + 
				"  -h, --help      Display the help menu.\n" + 
				"  -v, --version   Prints the current version of the DAM.\n" +
				"  -t, --tag tag   Run a program with a specific tag.\n" + 
				"  -l, --log       Runs program and shows info about the process.\n" + 
				"  -i, --in input  Starts the program with input.\n" +
				"  -p, --prog-show Displays the instructions being run on the VM.\n" +
				"  -s, --save-tmp  Saves the temporary files from transpiling.\n" + 
				"                  and compiling, for debugging.\n" + 
				"\n" + 
				"Examples:\n" + 
				"  DAM.bat -h\n" + 
				"  DAM.bat helloworld.dam\n" + 
				"  DAM.bat -t 674daf093e8b51c2948f5bb14b426c3d progs/helloworld.dam\n" +
				"  DAM.bat --log --prog-show --save-tmp --in Hello progs/cat.dam");
	}

}
