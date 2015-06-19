package Spider_PTT;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class testPrint {
	final static int FEATURE_NUM = 5;
	
	public static void main(String[] args) {
		
		
	}
	
	public static String getFingerPrint(String name,String content){
		
		
		int min = Math.min(content.length(), 2000);
		content = content.substring(0,min);
		StringBuilder fingerPrint = new StringBuilder();
		
		//String[] ret = getTag(name,content,FEATURE_NUM);
		
		
		return content;

	}
	
	public static byte[] getMD5(String content){
		
		
		MessageDigest md = null;
		
		byte[] encryptMsh = null;
		
		try{
			
			md = MessageDigest.getInstance("MD5");
			encryptMsh = md.digest(content.getBytes());
			
			
		}catch(NoSuchAlgorithmException e){
			System.out.println("NoSuchAlgorithmException");
		}
		
		for (byte b: encryptMsh){
			System.out.print(b);
		}
		System.out.println();
		return encryptMsh;
	}
	

}
