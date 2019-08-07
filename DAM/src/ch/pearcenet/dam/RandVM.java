package ch.pearcenet.dam;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RandVM {
	
	//Maximum number of lines a program can be
	public static final int MAX_PROGLEN = 256;
	
	private String stdin;
	private String stdout = "";
	private int[] instructionSet = new int[16];
	private byte[] RAM = new byte[256];
	private int[][] progMem = new int[MAX_PROGLEN][4];
	
	private boolean isIfStatement = false;
	private boolean isIfSuccessful = false;
	
	private int programCounter = 1;
	private int inputIndex = 0;
	
	//Stores the number of arguments for each instruction
	private static final int[] argCount = {1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0, 0, 1, 0};
	
	public RandVM (Tag tag, String stdin) {
		this.stdin = stdin;
		
		//Check tag length
		if (tag.getTag().length() < 32) {
			System.out.println("[ERROR] Given tag is too short. Must be 32-digit hexadecimal string.");
			System.exit(1);
		}
		
		//Generate InstructionSet
		for (int i=0; i<16; i++) {
			instructionSet[i] = tag.getIntOfIndex(i);
		}
	}
	
	//Loads a binary executable file
	public void loadBinary(String filename) {
		Main.log("Loading Compiled binary...");
		try {
			File file = new File(filename);
			FileReader in = new FileReader(file);
			
			//Load all the instructions into program memory
			int progCount = 1;
			while (in.ready() && progCount < MAX_PROGLEN) {
				byte insByte = (byte) in.read();
				int logicalIns = getLogicalIns(insByte);
				
				if (logicalIns == -1) {
					
					progMem[progCount][0] = 16;
					
				} else {
					
					progMem[progCount][0] = logicalIns;					
					for (int i=0; i<argCount[progMem[progCount][0]]; i++) {
						progMem[progCount][i + 1] = (byte) in.read();
					}
					
				}
				
				progCount++;
			}
			
			in.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Couldn't load Binary file '"+filename+"'");
			System.exit(4);
		}
		Main.log("Done.\n");
		
	}
	
	//Run a full program from Prog Memory
	public void runProg() {
		Main.log("Running program...\n--------------------\nProgram Output:\n--------------------");
		
		int[] instruction = {0};
		
		if (Main.showProg) System.out.println("\nprg: ins arg arg arg\n--------------------");
		while (programCounter < MAX_PROGLEN) {
			instruction = progMem[programCounter++];
			
			if (Main.showProg && instruction[0] != 16) {
				if (programCounter < 10) System.out.print(" ");
				if (programCounter < 100) System.out.print(" ");
				System.out.print(programCounter + ":");
				
				for (int i: instruction) {
					if (i < 10) System.out.print(" ");
					if (i < 100) System.out.print(" ");
					System.out.print(" " + i);
				}
				System.out.println(" ");
			}
			
			compute(instruction);
		}
		System.out.println(stdout);
		
		Main.log("\n--------------------\nProgram Ended.");
	}
	
	//Perform a computation using the VM
	private void compute(int[] instruction) {
		int ins = instruction[0];
		
		//Check if we're in an unsuccessful if block
		if (isIfStatement && !isIfSuccessful && ins != 13 && ins != 14) {
			return;
		}
		
		switch(ins) {
		
			//Do Nothing
			case 16:
				return;
			
			//Get a char from stdin and store in addr
			case 0:
				if (inputIndex < stdin.length()) {
					RAM[instruction[1]] = (byte) stdin.charAt(inputIndex++);
				} else {
					RAM[instruction[1]] = 0;
				}
				break;
			
			//Print a char to stdout from addr
			case 1:
				if (Main.showProg) {
					stdout += (char) RAM[instruction[1]];
				} else {
					System.out.print((char) RAM[instruction[1]]);
				}
				break;
			
			//Sets an address to a value
			case 2:
				RAM[instruction[1]] = (byte) instruction[2];
				break;
			
			//Copies a value from one address to another
			case 3:
				RAM[instruction[2]] = RAM[instruction[1]];
				break;
			
			//Adds two numbers and stores result
			case 4:
				RAM[instruction[3]] = (byte) (RAM[instruction[1]] + RAM[instruction[2]]);
				break;
			
			//Subs two numbers and stores result
			case 5:
				RAM[instruction[3]] = (byte) (RAM[instruction[1]] - RAM[instruction[2]]);
				break;
			
			//Multiplies two numbers and stores result
			case 6:
				RAM[instruction[3]] = (byte) (RAM[instruction[1]] * RAM[instruction[2]]);
				break;
				
			//Divides two numbers and stores result
			case 7:
				if (instruction[2] < 1) {
					System.out.println("[ERROR] Attempted to divide by zero.");
					System.exit(1);
				}
				
				RAM[instruction[3]] = (byte) Math.floorDiv(RAM[instruction[1]],  RAM[instruction[2]]);
				break;
				
			//Divides two numbers and stores remainder
			case 8:
				if (instruction[2] < 1) {
					System.out.println("[ERROR] Attempted to divide by zero.");
					System.exit(1);
				}
				
				RAM[instruction[3]] = (byte) (RAM[instruction[1]] % RAM[instruction[2]]);
				break;
			
			//Compares if two numbers are equal and stores boolean
			case 9:
				if (RAM[instruction[1]] == RAM[instruction[2]]) {
					RAM[instruction[3]] = 1;
				} else {
					RAM[instruction[3]] = 0;
				}
				break;
			
			//Compares if one number is greater than the other and stores boolean
			case 10:
				if (RAM[instruction[1]] > RAM[instruction[2]]) {
					RAM[instruction[3]] = 1;
				} else {
					RAM[instruction[3]] = 0;
				}
				break;
			
			//Compares if one number is less than the other and stores boolean
			case 11:
				if (RAM[instruction[1]] < RAM[instruction[2]]) {
					RAM[instruction[3]] = 1;
				} else {
					RAM[instruction[3]] = 0;
				}
				break;

			//Start if statement
			case 12:
				isIfStatement = true;
				isIfSuccessful = (RAM[instruction[1]] > 0);
				break;
			
			//end if, start else
			case 13:
				isIfSuccessful = !isIfSuccessful;
				break;
			
			//end if/else statement
			case 14:
				isIfStatement = false;
				break;
			
			//Jumps to a specific line in program memory
			case 15:
				programCounter = instruction[1];
				break;
		}
		
	}
	
	//Returns the Logical instruction number for the given random instruction
	public int getLogicalIns(int randIns) {
		for (int i=0; i<16; i++) {
			if (instructionSet[i] == randIns) {
				return i;
			}
		}
		
		return -1;
	}

}
