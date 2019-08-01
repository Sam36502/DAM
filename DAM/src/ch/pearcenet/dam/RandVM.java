package ch.pearcenet.dam;

public class RandVM {
	
	private Tag tag;
	private int[] instructionSet = new int[8];
	private byte[] RAM = new byte[256];
	
	private boolean isIfStatement = false;
	private boolean isIfSuccessful = false;
	
	public RandVM (Tag tag) {
		this.tag = tag;
		
		//Generate InstructionSet
		for (int i=0; i<8; i++) {
			String currInstruction = tag.getTag().charAt(i) + "";
			instructionSet[i] = Integer.parseInt(currInstruction, 16);
		}
	}
	
	//Perform a computation using the VM
	public void compute(int ins, int... args) {
		
		//Check if we're in an unsuccessful if block
		if (isIfStatement && !isIfSuccessful) {
			return;
		}
		
		//TODO: Check number of args is correct
		
		switch(ins) {
			
			//Get a char from stdin and store in addr //PONDER
			case 1:
				break;
			
			//Print a char to stdout from addr
			case 2:
				break;
			
			//Sets an address to a value
			case 3:
				break;
			
			//Adds two numbers and stores result
			case 4:
				break;
			
			//Subs two numbers and stores result
			case 5:
				break;
			
			//Start if statement
			case 6:
				break;
			
			//end if, start else
			case 7:
				break;
			
			//end if/else statement
			case 8:
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
