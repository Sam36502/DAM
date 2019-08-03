package ch.pearcenet.dam;

public class Main {
	
	public static boolean logsEnabled = false;
	public static boolean keepFiles = false;
	
	public static void main(String[] args) {
		
		//Variables for the VM and Language
		Tag mainTag = null;
		String stdinString = "";
		
		//Help menu
		if (indexInArray(args, "help") != -1) {
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
		if (indexInArray(args, "log") != -1) {
			logsEnabled = true;
			Main.log("Logs are enabled.");
		}
		
		//Set a custom tag to be used
		if (indexInArray(args, "tag") != -1) {
			int tIndex = indexInArray(args, "tag");
			String customTag = args[tIndex + 1];
			mainTag = new Tag(customTag);
		} else {
			mainTag = new Tag();
		}
		
		//Set what input should be passed to the vm
		if (indexInArray(args, "in") != -1) {
			int iIndex = indexInArray(args, "in");
			stdinString = args[iIndex + 1];
		}
		
		//Set the temp files to not be deleted
		if (indexInArray(args, "save") != -1) {
			Main.log("Temporary files will be kept.");
			keepFiles = true;
		}
		
		//Start Transpiling
		String program = args[args.length - 1];
		Transpiler tp = new Transpiler(program, mainTag);
		tp.transpile();
		tp.writeToFile("./tmp/" + program + "-" + mainTag.getTag() + ".txt");
		
		//TODO: Add compiler steps here
		
		RandVM vm = new RandVM(mainTag, stdinString);
		
	}
	
	//Logs to the screen as long as logging is enabled
	public static void log(String msg) {
		if (logsEnabled) {
			System.out.println(msg);
		}
	}
	
	//Checks if a String array contains a value
	public static int indexInArray(String[] arr, String val) {
		for (int i=0; i<arr.length; i++) {
			if (("--" + val).equals(arr[i]) ||
				("-" + val.charAt(0)).equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}
	
	//Help Menu
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
				"  DAM.bat -t ea9140b67f3c2d858bdd3d88595239a8 helloworld.dam");
	}

}
