package Spider_PTT;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue {
	
	private static Set VisitedUrl = new HashSet();
	private static Queue unVisitedUrl = new Queue();
	
	
	public static Queue getUnVisitedUrl(){
		return unVisitedUrl;
	}
	
	public static void addVisitedUrl(String url){
		VisitedUrl.add(url);
	}
	
	public static void removeVisitedUrl(String url){
		VisitedUrl.remove(url);
	}
	
	public static Object unVisitedUrl(){
		return unVisitedUrl.deQueue();
	}
	
	public static void addUnVisitedUrl(String url){
		if (url != null && !url.trim().equals("") 
				&& !VisitedUrl.contains(url) && !url.contains("#")
				&& !unVisitedUrl.contians(url) ){
			
			unVisitedUrl.enQueue(url);
			
		}
	}
	
	public static int getVisitedUrlNum(){
		return VisitedUrl.size();
	}
	
	public static boolean nuVisitedUrlEmpty(){
		return unVisitedUrl.empty();
	}
	
	
}
