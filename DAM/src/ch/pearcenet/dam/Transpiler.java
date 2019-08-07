package ch.pearcenet.dam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Transpiler {
	
	private String[] content = new String[RandVM.MAX_PROGLEN];
	private String[] syntax = new String[16];
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
		
		//Load the dam universal program into content
		try {
			FileInputStream fis = new FileInputStream(filename);
			Scanner in = new Scanner(fis);
			
			int index = 0;
			while (in.hasNextLine()) {
				content[index++] = in.nextLine();
			}
			Main.log("Loaded " + (index + 1) + " Lines of code\n");
			
			in.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Can't find program '" + filename + "'.");
			System.exit(1);
		} catch (IOException e) {
			Main.log("Failed to close FileInputStream for program");
		}
		
		//Load the chosen syntax from the syntax file
		Main.log("Loading RandLang Syntax...");
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
			System.exit(2);
		} catch (IOException e) {
			Main.log("Failed to close FileInputStream for syntax file.");
		}
		
		Main.log("Finished loading all necessary files.\n");
	}
	
	//Write content to a file
	public void writeToFile(String filename) {
		
		Main.log("Writing transpiled program to '" + filename + "'...");
		File file = new File(filename);
		
		try {
			
			
			FileWriter out = new FileWriter(file);
			
			for (String line: content) {
				out.write(line + System.lineSeparator());
			}
			
			out.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to create the temporary files necessary.");
			e.printStackTrace();
			System.exit(1);
		}
		Main.log("Done.");
	}
	
	//Transpile the file and save changes into content array
	public void transpile() {
		
		Main.log("Transpiling the file to RandLang...");
		for (int i=0; i<content.length; i++) {
			if (content[i] == null) {
				break;
			}
			
			String curr = content[i].trim();
			
			//So long as the line isn't empty or a comment
			if (curr.length() > 0 && !"//".equals(curr.substring(0, 2))) {
				
				String univSyntax = "";
				
				//Get the universal syntax string and search for the corresponding random Syntax
				if (curr.indexOf(' ') == -1
					&& !"else".equals(curr)
					&& !"end".equals(curr)) {
					Main.log("[ERROR] Syntax error on line " + (i + 1) + ":\n"
							+ "  No whitespace character found."
							+ "  Line 24: '" + curr + "'.");
					System.exit(1);
				} else if ("else".equals(curr) || "end".equals(curr)) {
					univSyntax = curr;
				} else {
					univSyntax = curr.substring(0, curr.indexOf(' '));
				}
				
				String randSyntax;
				
				int syndex = Main.indexInArray(DAM_ASSEMBLY, univSyntax);
				
				if (syndex != -1) {
					randSyntax = syntax[syndex];
				} else {
					continue;
				}				
				
				//Swap the universal syntax for random syntax
				content[i] = curr.replace(univSyntax, randSyntax);
				
			}
			
		}
		Main.log("Finished Transpiling file.\n");
		
	}

}
