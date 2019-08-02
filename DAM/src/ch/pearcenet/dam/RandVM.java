package ch.pearcenet.dam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RandVM {
	
	//Maximum number of lines a program can be
	private static final int MAX_PROGLEN = 256;
	
	private Tag tag;
	private String stdin;
	private int[] instructionSet = new int[8];
	private byte[] RAM = new byte[256];
	private int[][] progMem = new int[MAX_PROGLEN][4];
	
	private boolean isIfStatement = false;
	private boolean isIfSuccessful = false;
	
	private int programCounter = 1;
	private int inputIndex = 0;
	
	//Stores the number of arguments for each instruction
	private static final int[] argCount = {1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 1, 0, 0, 1, 0};
	
	public RandVM (Tag tag, String stdin) {
		this.tag = tag;
		this.stdin = stdin;
		
		//Generate InstructionSet
		for (int i=0; i<8; i++) {
			String currInstruction = tag.getTag().charAt(i) + "";
			instructionSet[i] = Integer.parseInt(currInstruction, 16);
		}
	}
	
	//Loads a binary executable file
	public void loadBinary(String filename) {
		Scanner in = null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			in = new Scanner(fis);
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] Couldn't load Binary file '"+filename+"'");
			return;
		}
		
		//Load all the instructions into program memory
		int progCount = 1;
		while (in.hasNext() && progCount < MAX_PROGLEN) {
			byte insByte = in.nextByte();
			progMem[progCount][0] = getLogicalIns(insByte);
			
			for (int i=0; i<argCount[progMem[progCount][0]]; i++) {
				progMem[progCount][i + 1] = in.nextByte();
			}
		}
		
		in.close();
		
	}
	
	//Run a full program from Prog Memory
	public void runProg() {
		Main.log("Running program...\n----");
		
		int[] instruction = {0};
		while (programCounter < MAX_PROGLEN) {
			instruction = progMem[programCounter++];
			compute(instruction);
		}
		
		Main.log("----\nProgram Ended.");
	}
	
	//Perform a computation using the VM
	private void compute(int[] instruction) {
		int ins = instruction[0];
		
		//Check if we're in an unsuccessful if block
		if (isIfStatement && !isIfSuccessful) {
			return;
		}
		
		//Check number of args is correct
		if (argCount[ins] != instruction.length - 1) {
			Main.log("Incorrect number of ");
			return;
		}
		
		switch(ins) {
		
			//Do Nothing
			case 16:
				return;
			
			//Get a char from stdin and store in addr
			case 0:
				if (inputIndex < stdin.length()) {
					RAM[instruction[1]] = (byte) stdin.charAt(inputIndex);
				} else {
					RAM[instruction[1]] = 0;
				}
				break;
			
			//Print a char to stdout from addr
			case 1:
				System.out.print("" + (char) RAM[instruction[1]]);
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
				RAM[instruction[3]] = (byte) Math.floorDiv(RAM[instruction[1]],  RAM[instruction[2]]);
				break;
				
			//Divides two numbers and stores remainder
			case 8:
				RAM[instruction[3]] = (byte) (RAM[instruction[1]] % RAM[instruction[2]]);
				break;
			
			//Compares if two numbers are equal and stores boolean
			case 9:
				if (RAM[instruction[1]] == RAM[instruction[2]]) {
					RAM[instruction[3]] = 1;
				} else {
					RAM[instruction[3]] = 2;
				}
				break;
			
			//Compares if one number is greater than the other and stores boolean
			case 10:
				if (RAM[instruction[1]] > RAM[instruction[2]]) {
					RAM[instruction[3]] = 1;
				} else {
					RAM[instruction[3]] = 2;
				}
				break;
			
			//Compares if one number is less than the other and stores boolean
			case 11:
				if (RAM[instruction[1]] < RAM[instruction[2]]) {
					RAM[instruction[3]] = 1;
				} else {
					RAM[instruction[3]] = 2;
				}
				break;
			
			//Start if statement
			case 12:
				isIfStatement = true;
				isIfSuccessful = (RAM[instruction[1]] == 1);
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
				programCounter = RAM[instruction[1]];
				break;
		}
		
	}
	
	//Returns the Logical instruction number for the given random instruction
	public int getLogicalIns(int randIns) {
		for (int i=0; i<8; i++) {
			if (instructionSet[i] == randIns) {
				return i;
			}
		}
		
		return -1;
	}

}
