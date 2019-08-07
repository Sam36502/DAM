package ch.pearcenet.dam;

public class Tag {
	
	private String content;
	
	//Generate a 32-digit tag unique for each machine (Checks for duplicate digits in the first half)
	public Tag() {
		StringBuilder tag = new StringBuilder("");
		
		char[] insSet = {'0', '1', '2', '3',
				'4', '5', '6', '7',
				'8', '9', 'a', 'b',
				'c', 'd', 'e', 'f'};
		
		//Scramble instruction set
		for (int i=0; i<16; i++) {
			int rand = (int)(Math.random() * 15 + 1);
			char swp = insSet[rand];
			insSet[rand] = insSet[i];
			insSet[i] = swp;
		}
		
		tag.append(insSet);
		
		//Making random language layout
		for (int digit=16; digit<32; digit++) {
			int rand = (int)(Math.random() * 15 + 1);
			String hexStr = Integer.toHexString(rand);
			tag.append(hexStr);
		}
		content = tag.toString();
		Main.log("Generated tag '" + content + "'");
	}
	
	//Assign a specific tag
	public Tag(String tag) {
		Main.log("Set tag to '" + tag + "'.");
		this.content = tag;
	}
	
	//Returns the tag string
	public String getTag() {
		return content;
	}
	
	//Returns the integer value of a single character
	public int getIntOfIndex(int index) {
		char ch = content.charAt(index);
		return Integer.parseInt("" + ch, 16);
	}

}
