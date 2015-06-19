package text.mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.Term;

public class ChineseParser {
	
	int WordKeyMax = 30;
	
    
    Set<String> wordkey1 = new LinkedHashSet<String>(); //排序前n個的關鍵字集合
    Set<String> wordkey2 = new LinkedHashSet<String>(); //排序前n個的關鍵字集合
    Map<String,Integer> tmpmap = new HashMap<String,Integer>();
    String wordkeyCount="";
    
	public ChineseParser (FileReader file ,FileReader file2){
		
		BufferedReader br = new BufferedReader(file);
		String wordkey="";
		String wordkey2="";
		
		try {
			while(br.ready()){
				wordkey += br.readLine();
				
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		br = new BufferedReader(file2);
		
		try {
			while(br.ready()){
				wordkey2 += br.readLine();
				
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		/**-------------------**/
		
		Map<String,Integer> map1 = new HashMap<String,Integer>();
		Map<String,Integer> map2 = new HashMap<String,Integer>();
		
		Unity wordkeyMap = new Unity();
		map1 = wordkeyMap.getWordKeyMap(wordkey);
		
		wordkeyMap = new Unity();
		map2= wordkeyMap.getWordKeyMap(wordkey2);
		
		printMap(map1,map2);
		
	}
	
	public ChineseParser (String SampleStr1,String SampleStr2){
		
		Map<String,Integer> map1 = new HashMap<String,Integer>();
		Map<String,Integer> map2 = new HashMap<String,Integer>();
		
		Unity wordkeyMap = new Unity();
		map1 = wordkeyMap.getWordKeyMap(SampleStr1);
		
		wordkeyMap = new Unity();
		map2 = wordkeyMap.getWordKeyMap(SampleStr2);
		
		printMap(map1,map2);
		
	}
	
	public ChineseParser (String SampleStr){
		
		if (SampleStr.length() >640)
			SampleStr = SampleStr.substring(0, 640);
		Unity wordkeyMap = new Unity();
		tmpmap = wordkeyMap.getWordKeyMap(SampleStr);
		printMap(tmpmap);
	}
	
	public String getmap(){

		return wordkeyCount;
	} 
	
	public void printMap(Map<String,Integer> map1,Map<String,Integer> map2){
		List<Map.Entry<String,Integer>> list= new ArrayList<Map.Entry<String,Integer>>();  
	    list.addAll(map1.entrySet());  
	    ValueComparator vc = new ValueComparator();  
	    Collections.sort(list,vc);  
	    wordkeyCount ="";
	    
	    System.out.print("map1 ");
	    for(Iterator<Map.Entry<String,Integer>> it = list.iterator();it.hasNext();)  
	    {  
	    	String strbuf = it.next().toString();
	        System.out.print(strbuf+" ");
	        wordkeyCount += strbuf+"\t";
	    }  
	    
	    System.out.println();
	    
	    List<Map.Entry<String,Integer>> list2= new ArrayList<Map.Entry<String,Integer>>();
	    list2.addAll(map2.entrySet());  
	    Collections.sort(list2,vc);  
	    
	    System.out.print("map2 ");
	    for(Iterator<Map.Entry<String,Integer>> it = list2.iterator() ; it.hasNext();)  
	    {  
	    	
	        System.out.print(it.next()+" ");  
	    }  
	    System.out.println();
	}
	
	public void printMap(Map<String,Integer> map){
		
		
		List<Map.Entry<String,Integer>> list= new ArrayList<Map.Entry<String,Integer>>();  
	    list.addAll(map.entrySet());  
	    ValueComparator vc = new ValueComparator();  
	    Collections.sort(list,vc);  
	    
	    for(Iterator<Map.Entry<String,Integer>> it = list.iterator();it.hasNext();)  
	    {  
	    	String strbuf = it.next().toString();
	    	String []tmpstr = strbuf.split("=");
	    	if(Integer.parseInt(tmpstr[1]) > 1 ){
	    		//show map WordKey count
	    		//System.out.print(strbuf+" ");
		        wordkeyCount += strbuf+"\t";
	    	}
	    }  
	    //System.out.println();


	}
	
	


	private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>  
	{  
	    public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n)  
	    {  
	        return n.getValue()-m.getValue();  
	    }  
	} 

}
