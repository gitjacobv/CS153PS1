import java.util.*;
public class StringPartitioning{
	public static void main(String args[]){
		
		String s = "Lorem Ipsum dolor sit amet";
		
		ArrayList<String> segmented = new ArrayList<String>();
		
		for(int a=0; a<s.length(); a+=8){
			segmented.add(partitionedString(s.substring(a)));
			System.out.println(segmented.get(a/8));
		}
	}
	
	public static String partitionedString(String s){
		
		if(s.length()>8) return s.substring(0, 8);
		else{
			for(int a=s.length(); a<8; a++){
				s=s+"0";
			}
			return s;
		}
	}
}