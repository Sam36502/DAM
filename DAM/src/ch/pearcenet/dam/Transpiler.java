package ch.pearcenet.dam;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Transpiler {
	
	String[] content = new String[RandVM.MAX_PROGLEN];
	String[] syntax = new String[16];
	Tag tag;
	
	public static final String[] DAM_ASSEMBLY = {
		"in", "out", "set",
		"cpy", "add", "sub",
		"mul", "div", "mod",
		"equ", "grt", "lst",
		"if", "else", "end",
		"jmp"
	};
	
	public Transpiler(String filename, Tag tag) {
		this.tag = tag;
		Main.log("Loading the transpiler...");
		
		//Load the dam universal program into content
		try {
			FileInputStream fis = new FileInputStream("./progs/" + filename + ".dam");
			Scanner in = new Scanner(fis);
			
			int index = 0;
			while (in.hasNextLine()) {
				content[index] = in.nextLine();
			}
			Main.log("Loaded " + (index + 1) + "Lines of code");
			
			in.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Can't find program '" + filename + "'");
			System.exit(2);
		} catch (IOException e) {
			Main.log("Failed to close FileInputStream for program");
		}
		
		//Load the chosen syntax from the syntax file
		try {
			FileInputStream fis = new FileInputStream("./data/RandLang_Syntax");
			Scanner in = new Scanner(fis);
			
			int opcode = 0;
			int innerIndex = 0;
			while (in.hasNextLine()) {
				int randOpcode = tag.getIntOfIndex(16 + opcode);
				String line = in.nextLine();
				
				if (line.contains("{")) {
					line = line.substring(0, line.indexOf('{') - 1).trim();
					Main.log("Loading RandLang syntax for '" + line + "'...");
					innerIndex = 0;
				} else if (line.contains("}")) {
					opcode++;
				} else if (line.length() != 0) {
					if (randOpcode == innerIndex) {
						syntax[opcode] = line;
					}
					innerIndex++;
				}
				
			}
			
			in.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Can't find syntax file");
			System.exit(2);
		} catch (IOException e) {
			Main.log("Failed to close FileInputStream for syntax file");
		}
		
		Main.log("Finished loading all necessary files.");
	}
	
	//Write content to a file
	public void writeToFile(String filename) {
		
		Main.log("Writing transpiled program to '" + filename + "'...");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			
			for (String line: content) {
				out.write(line + System.lineSeparator());
			}
			
			out.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to create the temporary files necessary.");
			System.exit(2);
		}
		Main.log("Done.");
	}
	
	//Returns the index of an element in an array (-1 if no result)
	public int indexInArray(String[] arr, String element) {
		for (int i=0; i<arr.length; i++) {
			if (element.equals(arr[i])) {
				return i;
			}
		}
		return -1;
	}
	
	//Transpile the file and save changes into content array
	public void transpile() {
		
		Main.log("Transpiling the file to RandLang...");
		for (int i=0; i<content.length; i++) {
			String curr = content[i];
			
			//So long as the line isn't empty or a comment
			if (curr.length() > 0 && !"//".equals(curr.substring(0, 2))) {
				
				//Get the universal syntax string and search for the corresponding random Syntax
				String univSyntax = curr.substring(0, curr.indexOf(' ') - 1);
				String randSyntax = null;
				do {
					int syndex = indexInArray(DAM_ASSEMBLY, univSyntax);
					
					if (syndex != -1) {
						randSyntax = syntax[syndex];
					}
				} while (randSyntax == null);
				
				//Swap the universal syntax for random syntax
				content[i] = curr.replace(univSyntax, randSyntax);
				
			}
			
		}
		Main.log("Finished Transpiling file.");
		
	}

}
