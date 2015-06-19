package Spider;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import text.mining.ChineseParser;
import Spider_PTT.ConnMySQL;
import Spider_PTT.LinkQueue;

/**
 * 	ETtoday 東森新聞雲
 * **/

public class Spider_Ettoday {

	
	static int count = 0;
	private static void initCrawlerWithSends(String  sends){
		
		LinkQueue.addUnVisitedUrl(sends);
	}
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		
		
		long startTime = System.currentTimeMillis();
		
		ConnMySQL mysql = new ConnMySQL();

		trustEveryone();
		enableSSLSocket();
		

		String Jurl ="http://www.ettoday.net/news/news-list-2015-06-18-6-1.htm";
		//String Jurl="http://news.ltn.com.tw/news/society/paper/827489";
		
		initCrawlerWithSends(Jurl);
		

	    while(!LinkQueue.nuVisitedUrlEmpty() 
	    		&& count < 500){
	    	
	    	
	    	Jurl = (String)LinkQueue.unVisitedUrl();
	    	
	    	Connection conn = Jsoup.connect(Jurl)
	    			//.userAgent("Mozilla/5.0 Chrome/26.0.1410.64 Safari/537.31")
	    			//.userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
	    			.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
	    			.timeout(10000)
	    			.followRedirects(true)
	    			.maxBodySize(1024*1024*3)
	    			.ignoreContentType(true)
	    			.ignoreHttpErrors(true);
	    	
	    	LinkQueue.addVisitedUrl(Jurl);
	    
			try {
				//Thread.sleep(100);
				Document doc = conn.get();
				
				////////
				Elements bodyelements = doc.select("a[href]");
				
				Set<String> links = new HashSet<String>();
				for(Element element : bodyelements) {
					String url = element.getElementsByTag("a").attr("abs:href");
					                   
					
						
					if (url.startsWith("http://www.ettoday.net/news/20150612") ||
							url.startsWith("http://www.ettoday.net/news/20150613") ||
							url.startsWith("http://www.ettoday.net/news/20150614") ||
							url.startsWith("http://www.ettoday.net/news/20150615") ||
							url.startsWith("http://www.ettoday.net/news/20150616") ||
							url.startsWith("http://www.ettoday.net/news/20150617") ||
							url.startsWith("http://www.ettoday.net/news/20150618") ||
							url.startsWith("http://www.ettoday.net/news/news-list-2015-06-18-6") 
							){
						links.add(url);
					}
				}
	
				for(String link:links){
					LinkQueue.addUnVisitedUrl(link);
				}

				////////
				Elements ClsEs = doc.select("#society > div.wrapper_box > div > div.container_box > div > div > div.c1 > div.menu_bread_crumb > em");
				Elements elements = doc.select("#society > div.wrapper_box > div > div.container_box > div > div > div.c1 > div.subjcet_news > article > header > h2");
				Elements timeEs = doc.select("#society > div.wrapper_box > div > div.container_box > div > div > div.c1 > div.menu_bread_crumb > span.news-time");
				Elements contentEs = doc.select("#society > div.wrapper_box > div > div.container_box > div > div > div.c1 > div.subjcet_news > article > div.story");
				
				
				if (elements.size() > 0 && timeEs.size() > 0 && contentEs.size() >0 )
				{
					
					String title = elements.get(0).getElementsByTag("h2").text();
					
					String Cls = ClsEs.get(0).getElementsByTag("em").text();
					
					if(Cls.equals("社會")){
						
						String date = timeEs.get(0).text().trim();
						//date = date.substring(0, 10);
					    date = date.replaceAll("年", "-");
					    date = date.replaceAll("月", "-");
					    date = date.replaceAll("日", " ");
						//System.out.println(" "+date);
						
						String contentHTML = contentEs.get(0).toString();
						String content = contentEs.get(0).text();
						
						++count;
						/** 儲存至MySQL **/
						//mysql.LTNexeBatchParparedSQL(title, Jurl, content,contentHTML, "0", date);
						/***/
						ChineseParser chineseparser = new ChineseParser(content);
						
						/** 儲存至MySQL 測試數據 **/
						mysql.UnityBatchParparedSQL(title, Cls, Jurl, content, contentHTML, "0", chineseparser.getmap(), date);
						
						
//						System.out.println(title);
//						System.out.println(Cls);
//						System.out.println(Jurl);
//						System.out.println(" "+content);
//						System.out.println(" "+contentHTML);
//						System.out.println("Reply=0");
//						System.out.println("WordKey ");
//						System.out.println(date);
						System.out.println("Time: "+date);
						
						//System.out.println("關鍵字:"+chineseparser.getmap());
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/** 抓取FB回覆文章 **/
						
						/*
						String fburl ="https://www.facebook.com/plugins/comments.php?href="+Jurl;
						Connection conn2 = Jsoup.connect(fburl)
						.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
						
						Document doc2 = conn2.get();
						
						Elements fbName = doc2.getElementsByClass("profileName");
						Elements fbcontent = doc2.getElementsByClass("postText");
						
						for (int i=0;i<fbName.size();i++){
							System.out.print(i+"\t "+fbName.get(i).text());
							System.out.println("\t "+fbcontent.get(i).text());
						}
						System.out.println(count+"  "+Jurl);
						*/
						/** ------ **/
						
						
					}
					
					
					
				}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(Jurl);
			} catch (java.lang.IndexOutOfBoundsException e) {
				
			} 
			
	    }
	    
	    System.out.println(count);
	    
	    long endTime = System.currentTimeMillis();
	    
	    long totTime = endTime - startTime;
	  //印出執行時間
	    System.out.println("Using Time:" + totTime/1000);
	}
	
	public static void trustEveryone() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return false;
				}
			});

			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	
	public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
 
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
 
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
 
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

}
