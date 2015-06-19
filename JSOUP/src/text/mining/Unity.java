package text.mining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.Term;

public class Unity {
	
	 ArrayList<String> inputList = new ArrayList<String>(); //存切詞的name,例如天氣、氣溫
     ArrayList<String> TagList = new ArrayList<String>();   //存切詞的詞性 ,例如名詞、動詞
     
     
	public  String getWordKey (FileReader file){
		
		String wordkey="";
		BufferedReader br = new BufferedReader(file);
		
		try {
			while(br.ready()){
				
				wordkey += br.readLine();
				
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		CallCKIP(wordkey);
		
		return wordkey;
	}
	
	
	public Map<String,Integer> getWordKeyMap (String str){
		
		Map<String,Integer> map = new HashMap<String,Integer>();
		map = CallCKIP(str);
		
		return map;
	}
	
	public Map<String,Integer> CallCKIP(String str){

		CKIP c = new CKIP("IP","port","Account", "Password");
		
		c.setRawText(str);
		c.send();
		
		for (Term t : c.getTerm()) {
	        if (t.getTag().toString().startsWith("N") ||
	        		t.getTag().toString().startsWith("V")	)
	        {
	        	//去除無意義字詞
	        	if ( !(t.getTerm().equals("他") || t.getTerm().equals("我")  ||
	        			t.getTerm().equals("是") || t.getTerm().equals("說") ||
	        			t.getTerm().equals("人") || t.getTerm().equals("爽") ||
	        			t.getTerm().equals("你") || t.getTerm().equals("內") ||
	        			t.getTerm().equals("昨") || t.getTerm().equals("有"))
	        			)
	        	{
	        		inputList.add(t.getTerm()); // t.getTerm()會讀到斷詞的String，將其存到inputList陣列
		            TagList.add(t.getTag());    // t.getTag() 會讀到斷詞的詞性，將其存到TagList陣列

	        	}
	        }
	    }
		
		
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(int i=0;i<inputList.size();i++)
	    {
			if(!map.containsKey(inputList.get(i))){
				map.put(inputList.get(i), 1);

			}else{
				int count = map.get(inputList.get(i)) + 1;
				map.put(inputList.get(i), count);
			}
			
			
	    }
		
		return map;
	}

}
