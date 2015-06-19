import  java.math.BigInteger;
import  java.util.ArrayList;
import  java.util.HashMap;
import  java.util.List;
import  java.util.StringTokenizer;
 
public  class  SimHash2 {
 
    private  String tokens;
 
    private  BigInteger intSimHash;
 
    private  String strSimHash;
 
    private  int  hashbits = 64 ;
 
    public  SimHash2(String tokens) {
        this .tokens = tokens;
        this .intSimHash = this .simHash();
    }
 
    public  SimHash2(String tokens, int  hashbits) {
        this .tokens = tokens;
        this .hashbits = hashbits;
        this .intSimHash = this .simHash();
    }
 
    HashMap<String,Integer> wordMap = new  HashMap<String,Integer>();
 
    public  BigInteger simHash() {
        // 定義特徵向量/數組
        int [] v = new  int [ this .hashbits];
        // 1、將文本去掉格式後, 分詞.
        StringTokenizer stringTokens = new  StringTokenizer( this .tokens);
        while  (stringTokens.hasMoreTokens()) {
            String temp = stringTokens.nextToken();
            // 2、將每一個分詞hash為一組固定長度的數列.比如64bit 的一個整數.
            BigInteger t = this .hash(temp);
            for  ( int  i = 0 ; i < this .hashbits; i++) {
                BigInteger bitmask = new  BigInteger( "1" ).shiftLeft(i);
                // 3、建立一個長度為64的整數數組(假設要生成64位的數字指紋,也可以是其它數字),
                // 對每一個分詞hash後的數列進行判斷,如果是1000...1,那麼數組的第一位和末尾一位加1,
                // 中間的62位減一,也就是說,逢1加1,逢0減1.一直到把所有的分詞hash數列全部判斷完畢.
                if  (t.and(bitmask).signum() != 0 ) {
                    // 這裡是計算整個文檔的所有特徵的向量和
                    // 這裡實際使用中需要+- 權重，而不是簡單的+1/-1，
                    v[i] += 1 ;
                } else  {
                    v[i] -= 1 ;
                }
            }
        }
        BigInteger fingerprint = new  BigInteger( "0" );
        StringBuffer simHashBuffer = new  StringBuffer();
        for  ( int  i = 0 ; i < this .hashbits; i++) {
            // 4、最後對數組進行判斷,大於0的記為1,小於等於0的記為0,得到一個64bit 的數字指紋/簽名.
            if  (v[i] >= 0 ) {
                fingerprint = fingerprint.add( new  BigInteger( "1" ).shiftLeft(i));
                simHashBuffer.append( "1" );
            } else  {
                simHashBuffer.append( "0" );
            }
        }
        this .strSimHash = simHashBuffer.toString();
        System.out.println( this .strSimHash + " length "  + this .strSimHash.length());
        return  fingerprint;
    }
 
    private  BigInteger hash(String source) {
        if  (source == null  || source.length() == 0 ) {
            return  new  BigInteger( "0" );
        } else  {
            char [] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf((( long ) sourceArray[ 0 ]) << 7 );
            BigInteger m = new  BigInteger( "1000003" );
            BigInteger mask = new  BigInteger( "2" ).pow( this .hashbits).subtract( new  BigInteger( "1" ));
            for  ( char  item : sourceArray) {
                BigInteger temp = BigInteger.valueOf(( long ) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor( new  BigInteger(String.valueOf(source.length())));
            if  (x.equals( new  BigInteger( "-1" ))) {
                x = new  BigInteger( "-2" );
            }
            return  x;
        }
    }
 
    public  int  hammingDistance(SimHash other) {
 
        BigInteger x = this .intSimHash.xor(other.intSimHash);
        int  tot = 0 ;
 
        // 統計x中二進制位數為1的個數
        // 我們想想，一個二進制數減去1，那麼，從最後那個1（包括那個1）後面的數字全都反了，對吧，然後，n&(n-1)就相當於把後面的數字清0，
        // 我們看n能做多少次這樣的操作就OK了。
 
        while  (x.signum() != 0 ) {
            tot += 1 ;
            x = x.and(x.subtract( new  BigInteger( "1" )));
        }
        return  tot;
    }
 
    public  int  getDistance(String str1, String str2) {
        int  distance;
        if  (str1.length() != str2.length()) {
            distance = - 1 ;
        } else  {
            distance = 0 ;
            for  ( int  i = 0 ; i < str1.length(); i++) {
                if  (str1.charAt(i) != str2.charAt(i)) {
                    distance++;
                }
            }
        }
        return  distance;
    }
 
    public  List subByDistance(SimHash simHash, int  distance) {
        // 分成幾組來檢查
        int  numEach = this .hashbits / (distance + 1 );
        List characters = new  ArrayList();
 
        StringBuffer buffer = new  StringBuffer();
 
        int  k = 0 ;
        for  ( int  i = 0 ; i < this .intSimHash.bitLength(); i++) {
            // 當且僅當設置了指定的位時，返回true
            boolean  sr = simHash.intSimHash.testBit(i);
 
            if  (sr) {
                buffer.append( "1" );
            } else  {
                buffer.append( "0" );
            }
 
            if  ((i + 1 ) % numEach == 0 ) {
                // 將二進制轉為BigInteger
                BigInteger eachValue = new  BigInteger(buffer.toString(), 2 );
                System.out.println( "----"  + eachValue);
                buffer.delete( 0 , buffer.length());
                characters.add(eachValue);
            }
        }
 
        return  characters;
    }
 
    public  static  void  main(String[] args) {
    	
    	
    	
    	CosineSimilarAlgorithm C = new CosineSimilarAlgorithm();
    	System.out.println(
    			C.getSimilarity(
    					"This is a test string for testing",
    			"testing This test  a test string for testing" ));
    	
    	/*
        String s = "This is a test string for testing" ;
        SimHash hash1 = new  SimHash(s, 64 );
        System.out.println(hash1.intSimHash + " "  + hash1.intSimHash.bitLength());
        hash1.subByDistance(hash1, 3 );
 
        s = "This is a test string for testing, This is a test string for testing abcdef" ;
        SimHash hash2 = new  SimHash(s, 64 );
        System.out.println(hash2.intSimHash + " "  + hash2.intSimHash.bitCount());
        hash1.subByDistance(hash2, 3 );
         
        s = "testing This test  a test string for testing" ;
        SimHash hash3 = new  SimHash(s, 64 );
        System.out.println(hash3.intSimHash + " "  + hash3.intSimHash.bitCount());
        hash1.subByDistance(hash3, 4 );
         
        System.out.println( "============================" );
         
        int  dis = hash1.getDistance(hash1.strSimHash, hash2.strSimHash);
        System.out.println(hash1.hammingDistance(hash2) + " "  + dis);
 
        int  dis2 = hash1.getDistance(hash1.strSimHash, hash3.strSimHash);
        System.out.println(hash1.hammingDistance(hash3) + " "  + dis2);
         
        //通過Unicode編碼來判斷中文
        /*String str = "中國chinese" ;
        for  ( int  i = 0 ; i < str.length(); i++) {
            System.out.println(str.substring(i, i + 1 ).matches( "[\\u4e00-\\u9fbb]+" ));
        }*/
 
    }
}