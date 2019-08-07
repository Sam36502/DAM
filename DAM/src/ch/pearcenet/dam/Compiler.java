package ch.pearcenet.dam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Compiler {
	
	private String[] content = new String[RandVM.MAX_PROGLEN];
	private String[] syntax = new String[16];
	private Tag tag;
	
	public Compiler(String filename, Tag tag) {
		this.tag = tag;
		
		Main.log("Loading Transpiled program into the compiler.");
		try {
			FileInputStream fis = new FileInputStream(filename);
			Scanner in = new Scanner(fis);
			
			int index = 0;
			while(in.hasNextLine()) {
				String line = in.nextLine();
				if (!line.contains("//") && line.length() > 0) {
					content[index++] = line;
				} else {
					content[index++] = "";
				}
			}
			
			in.close();
			fis.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to load transpiled program file.");
			System.exit(1);
		}
		Main.log("Done.\n");
		
		//Load the chosen syntax from the syntax file
		Main.log("Loading RandLang Syntax:");
		try {
			FileInputStream fis = new FileInputStream("RandLang_Syntax.txt");
			Scanner in = new Scanner(fis);
			
			int opcode = 0;
			int innerIndex = 0;
			while (in.hasNextLine()) {
				int randOpcode = tag.getIntOfIndex(16 + opcode);
				String line = in.nextLine().trim();
				
				if (line.contains("{")) {
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
			System.out.println("[ERROR] Can't find syntax file.");
			System.exit(1);
		} catch (IOException e) {
			Main.log("Failed to close FileInputStream for syntax file.");
		}
		Main.log("Done.\n");
		
	}
	
	public void compile(String compFileName) {
		Main.log("Randomized instruction set:\n" + tag.getTag().substring(0, 16) + "\n||||||||||||||||\n0123456789abcdef\n");
		Main.log("Compiling program...");
		
		//Open File for writing compiled binary
		File file = new File(compFileName);
		try {
			
			FileWriter out = new FileWriter(file);
			
			//Compile each line of the transpiled code
			for (String line: content) {
				
				String[] parts = line.split(" ");
				
				//Find the instruction number of the current instruction
				int instruction = Main.indexInArray(syntax, parts[0]);
				
				if (instruction == -1) {
					
					out.write(16);
					
				} else {
					
					//Get random syntax of the instruction
					out.write(tag.getIntOfIndex(instruction));
					
					for (int i=1; i<parts.length; i++) {
						out.write(Integer.parseInt(parts[i]));
					}
					
				}
				
			}
			
			out.close();
			
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to write to compiled file.");
			System.exit(1);
		}
		
		Main.log("Done.\n");
		
	}

}
