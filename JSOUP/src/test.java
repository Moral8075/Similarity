import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Test;
import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.Term;
import tw.cheyingwu.ckip.WordSegmentationService;


public class test {

	public static void main(String[] args) {
		
		int WordKeyMax = 12;
		ArrayList<String> inputList = new ArrayList<String>(); //宣告動態陣列 存切詞的name
        ArrayList<String> TagList = new ArrayList<String>();   //宣告動態陣列 存切詞的詞性
        
        Set<String> wordkey1 = new LinkedHashSet<String>(); //排序前n個的關鍵字集合
        Set<String> wordkey2 = new LinkedHashSet<String>(); //排序前n個的關鍵字集合
	
		String key1[] = new String[WordKeyMax];
		String key2[] = new String[WordKeyMax];
		
		CKIP c = new CKIP("140.109.19.104",1501,
				"807544", "0101001");
		String e ="";
		
		
		Set<String> wordkey_total = new HashSet<String>();
		Map<String,Integer> map1 = new HashMap<String,Integer>();
		Map<String,Integer> map2 = new HashMap<String,Integer>();
		
		try {
			FileReader file = new FileReader("word4");
			BufferedReader br = new BufferedReader(file);
			
			while(br.ready()){
				e += br.readLine()+"\n";
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(e.trim());
		c.setRawText(e.trim());
		c.send();
		
		//System.out.print(c.getRawText());
		
		for (Term t : c.getTerm()) {
	        if (t.getTag().toString().startsWith("N") ||
	        		t.getTag().toString().startsWith("V")	)
	        {
	        	inputList.add(t.getTerm()); // t.getTerm()會讀到斷詞的String，將其存到inputList陣列
	            TagList.add(t.getTag());    // t.getTag() 會讀到斷詞的詞性，將其存到TagList陣列
	        
	        }
            
        }
		Comparator comparator = Collator.getInstance(Locale.TRADITIONAL_CHINESE);
		String vDataSortTemp;
		for( int i = 0; i <= inputList.size(); i++ )
            for( int j = i; j < inputList.size(); j++ )
            {
                if( comparator.compare(inputList.get(i), inputList.get(j)) > 0 )
                {
                    vDataSortTemp = inputList.get(j);
                    inputList.set(j, inputList.get(i))  ;
                    inputList.set(i, vDataSortTemp) ;
                    
                    vDataSortTemp = TagList.get(j);
                    TagList.set(j, TagList.get(i))  ;
                    TagList.set(i, vDataSortTemp) ;
                }
            }
		
		String str ="";
		
		
		for(int i=0;i<inputList.size();i++)
        {
			
			/*
			if(!set.contains(inputList.get(i)))
				set.add(inputList.get(i));
				*/
			
			if(!map1.containsKey(inputList.get(i)))
				map1.put(inputList.get(i), 1);
			else{
				int count = map1.get(inputList.get(i)) + 1;
				map1.put(inputList.get(i), count);
			}
				
			
//			str += inputList.get(i);
//        	System.out.print(inputList.get(i));
//        	System.out.println("\t"+TagList.get(i));
        	
        }
	
		
		
		/** ----------------------------------------------------*/
		
		CKIP c2 = new CKIP("140.109.19.104",1501,
				"807544", "0101001");
		e ="";
		try {
			FileReader file = new FileReader("word5");
			BufferedReader br = new BufferedReader(file);
			
			while(br.ready()){
				e += br.readLine();
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		c2.setRawText(e);
		c2.send();
		
				
		inputList = new ArrayList<String>();
		TagList = new ArrayList<String>();
		
		for (Term t : c2.getTerm()) {
	        if (t.getTag().toString().startsWith("N") ||
	        		t.getTag().toString().startsWith("V")	)
	        {
	        	inputList.add(t.getTerm()); // t.getTerm()會讀到斷詞的String，將其存到inputList陣列
	            TagList.add(t.getTag());    // t.getTag() 會讀到斷詞的詞性，將其存到TagList陣列
	            
	        }
            
        }
		
		
		
		for( int i = 0; i <= inputList.size(); i++ )
            for( int j = i; j < inputList.size(); j++ )
            {
                if( comparator.compare(inputList.get(i), inputList.get(j)) > 0 )
                {
                    vDataSortTemp = inputList.get(j);
                    inputList.set(j, inputList.get(i))  ;
                    inputList.set(i, vDataSortTemp) ;
                    
                    vDataSortTemp = TagList.get(j);
                    TagList.set(j, TagList.get(i))  ;
                    TagList.set(i, vDataSortTemp) ;
                }
            }
		
		
		
		str ="";
		
		for(int i=0;i<inputList.size();i++)
        {
			/*
			if(!set.contains(inputList.get(i)))
				set.add(inputList.get(i));
			*/
			if(!map2.containsKey(inputList.get(i)))
				map2.put(inputList.get(i), 1);
			else{
				int count = map2.get(inputList.get(i)) + 1;
				map2.put(inputList.get(i), count);
			}
	
        }
        
		
		
		System.out.println();
		System.out.println("--------sort---------");
		
		List<Map.Entry<String,Integer>> list= new ArrayList<Map.Entry<String,Integer>>();  
        list.addAll(map1.entrySet());  
        ValueComparator vc = new ValueComparator();  
        Collections.sort(list,vc);  
        
        for(Iterator<Map.Entry<String,Integer>> it = list.iterator();it.hasNext();)  
        {  
            System.out.print(it.next()+" ");
        }  
        
        System.out.println();
        
        List<Map.Entry<String,Integer>> list2= new ArrayList<Map.Entry<String,Integer>>();
        list2.addAll(map2.entrySet());  
        Collections.sort(list2,vc);  
        
        for(Iterator<Map.Entry<String,Integer>> it = list2.iterator() ; it.hasNext();)  
        {  
        	
            System.out.print(it.next()+" ");  
        }  
        
        System.out.println();
        
        int Intersection =0;
        int count =0;
        
        for(Iterator<Map.Entry<String,Integer>> it = list.iterator();it.hasNext();)  
        {  
        	if (count >= WordKeyMax)
            	break;
            else
            	++count;
        	
        	int count2 = 0;
        	
            Entry<String,Integer> data =(Entry<String,Integer>)it.next();
            if(!wordkey1.contains(data.getKey()))
            	wordkey1.add(data.getKey());
            
            for(Iterator<Map.Entry<String,Integer>> it2 = list2.iterator();it2.hasNext();)  
            {  
            	
            	if (count2 >= WordKeyMax)
                	break;
                else
                	++count2;
            	
            	Entry<String,Integer> data2 =(Entry<String,Integer>)it2.next();
            	if(!wordkey2.contains(data2.getKey()))
                	wordkey2.add(data2.getKey());
            	
            	if (data.getKey().equals(data2.getKey()))
            	{
            		++Intersection;
            		
            		break;
            	}
            	
            	
            }
            
            
            	
        }  
        
        System.out.println();
        
        int setsize = wordkey_total.size();
        System.out.println("set:"+setsize+"\t Intersection:"+Intersection);
        float J = (float)Intersection / setsize ;
        System.out.println("J="+J); 
        
        System.out.println("key1="+wordkey1.size()+" key2="+wordkey2.size()); 
        
        
        
        int index1 = 0;
        int index2 = 0;
        Iterator  it = wordkey1.iterator() ;
        
        while (it.hasNext()) {   
             key1[index1] = it.next()+"";
             if(!wordkey_total.contains(key1[index1]))
     			wordkey_total.add(key1[index1]);
             ++index1;
             
        }
        System.out.println(" ");
        it = wordkey2.iterator() ;
        while (it.hasNext()) {   
        	 key2[index2] = it.next()+"";
        	 if(!wordkey_total.contains(key2[index2]))
      			wordkey_total.add(key2[index2]);
             ++index2;
        }
		
        System.out.println(" -------------------------------- ");
        
        for (String s : key1){
        	System.out.print(s+" "); 
        }
        System.out.println(" ");
        for (String s : key2){
        	System.out.print(s+" "); 
        }
        System.out.println(" ");
        System.out.println("wordkey_total "+wordkey_total.size());
     
        
        J = 0f;
        float D = 0f;
        float D2 = 0f;
        Intersection = 0;
        
        System.out.println("J="+J+" Intersection="+Intersection);
        
        WordKeyMax = wordkey_total.size();
        
        for(index1=0 ;index1 < key1.length ;index1++){
        	for(index2=0 ;index2 < key2.length ;index2++){
        	
        		//是否有相同關鍵字
        		if (key1[index1].equals(key2[index2])){
        			++Intersection;
        			D = (float)(WordKeyMax+1-index1+1) / ((WordKeyMax*(WordKeyMax+1))/2);
        			D2 = (float)(WordKeyMax+1-index2+1) / ((WordKeyMax*(WordKeyMax+1))/2);
        		
        			J = (float) J + ( WordKeyMax * D )+ ( WordKeyMax * D2);
        			
        		}
        	}
        }
        System.out.println(" D="+D+" D2="+D2);
        System.out.println("WordKeyMax="+WordKeyMax+"  Intersection="+Intersection);
        
        System.out.println("改良傑卡德相似系數 J="+ (float)(J/2)/WordKeyMax);
        System.out.println("傑卡德相似系數 J="+ (float) Intersection / WordKeyMax);
        
        
        
    }
		
	
	private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>  
    {  
        public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n)  
        {  
            return n.getValue()-m.getValue();  
        }  
    }  
	
}
