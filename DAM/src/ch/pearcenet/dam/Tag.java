package ch.pearcenet.dam;

public class Tag {
	
	private String content;
	
	//Generate a 16-digit tag unique for each machine (Checks for duplicate digits in the first half)
	public Tag() {
		StringBuilder tag = new StringBuilder("");
		for (int digit=0; digit<16; digit++) {
			
			String hexStr = "";
			if (digit < 8) {
				
				while (tag.indexOf(hexStr) != -1) {
					int rand = (int)(Math.random() * 15 + 1);
					hexStr = Integer.toHexString(rand);
				}
				
			} else {
				int rand = (int)(Math.random() * 15 + 1);
				hexStr = Integer.toHexString(rand);
			}
			
			tag.append(hexStr);
			
		}
		content = tag.toString();
	}
	
	public String getTag() {
		return content;
	}

}
