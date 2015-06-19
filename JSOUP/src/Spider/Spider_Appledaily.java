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
 * 	蘋果日報｜Apple Daily
 * **/
public class Spider_Appledaily {

	
	static int count = 0;
	private static void initCrawlerWithSends(String  sends){
		/*
		for(String send:sends){
			LinkQueue.addUnBisitedUrl(send);
		}*/
		LinkQueue.addUnVisitedUrl(sends);
	}
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		
		
		long startTime = System.currentTimeMillis();
		
		ConnMySQL mysql = new ConnMySQL();

		trustEveryone();
		enableSSLSocket();
		
		//String Jurl ="http://www.appledaily.com.tw/";
		String Jurl ="http://www.appledaily.com.tw/realtimenews/section/local/";
		//String Jurl ="http://www.appledaily.com.tw/appledaily/bloglist/supplement/222289";
		
		initCrawlerWithSends(Jurl);
		

	    while(!LinkQueue.nuVisitedUrlEmpty() 
	    		&& count < 500){
	    	
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	Jurl = (String)LinkQueue.unVisitedUrl();
	    	
	    	Connection conn = Jsoup.connect(Jurl)
	    			//.userAgent("Mozilla/5.0 Chrome/26.0.1410.64 Safari/537.31")
	    			.userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
	    			.timeout(30*1000);
	    			//.followRedirects(true)
	    			//.maxBodySize(1024*1024*3)
	    			//.ignoreContentType(true)
	    			//.ignoreHttpErrors(true);
	    	
	    	LinkQueue.addVisitedUrl(Jurl);
	    
			try {
				//Thread.sleep(100);
				Document doc = conn.get();
				
				////////
				Elements bodyelements = doc.select("a[href]");
				
				
				
				Set<String> links = new HashSet<String>();
				for(Element element : bodyelements) {
					String url = element.getElementsByTag("a").attr("abs:href");
					if (url.startsWith("http://www.appledaily.com.tw/realtimenews/article/local/") ||
							url.startsWith("https://www.appledaily.com.tw/realtimenews/article/local/") ||
							url.startsWith("http://www.appledaily.com.tw/realtimenews/section/")){
						
						links.add(url);
					}
				}
				
				for(String link:links){
					LinkQueue.addUnVisitedUrl(link);
				}
				
				////////
				
				Elements elements = doc.select("#maincontent > div.abdominis");
				for(Element element : elements) {
					//String url = element.getElementsByTag("a").attr("abs:href");
					
					if ( element.getElementsByTag("header").size()  == 0 ||
							element.getElementsByTag("h1").size() <=0 ||
							element.getElementsByTag("a").size() <=0){
						
						break;
					}else{
						
						String title;
						title = element.getElementsByTag("header").get(0).text();
						Elements nrecEs = doc.select("#maincontent > div.abdominis.lvl > div.collum > article > div.urcc > a.function_icon.clicked");
						
						//String nrec = element.getElementsByTag("a").get(3).text();
						String nrec = nrecEs.get(0).text();
						
						if (!nrec.contains("人氣")){
							if (  doc.select("#maincontent > div.abdominis.lvl > div.collum > article > div.urcc > a.function_icon.clicked").size() >0)
								nrec = doc.select("#maincontent > div.abdominis.lvl > div.collum > article > div.urcc > a.function_icon.clicked").get(0).text();
							
						}
						
						//
						if (nrec.contains("人氣")){
							
							String regEx="[^0-9]";   
							Pattern p = Pattern.compile(regEx);   
							Matcher m = p.matcher(nrec);   
							nrec = m.replaceAll("").trim();
							
						    String date ;
						    String Cls = "";
						    date = element.getElementsByTag("time").text();
						    date = date.replaceAll("年", "-");
						    date = date.replaceAll("月", "-");
						    date = date.replaceAll("日", " ");
						    
						    //System.out.println(date);
						    
						    if (Jurl.contains("realtimenews/article/local/20150617"))
						    	Cls = "社會";
						    else
						    	Cls = "雜";
						    
						    Elements Earticulum = element.getElementsByClass("articulum");
						    String contentHTML = Earticulum.toString();
						    String content = Earticulum.text();
						    ChineseParser chineseparser = new ChineseParser(content);
						    
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
//						    System.out.println("title: " + title);
//						    System.out.println("Class: " +Cls);
//						    System.out.println("URL: " +Jurl);
//						    System.out.println("Content: " +content);
//						    System.out.println("CHTML: " +contentHTML);
//						    System.out.println("date: " +date);
//						    System.out.println(nrec);
							System.out.println("date: " +date);
						    if (! (title.equals("") && Jurl.equals("") && content.equals("")
						    		&& nrec.equals("") && date.equals("") )){
						    	
						    	++count;
						    	/** 儲存至MySQL **/
						    	//mysql.AppledailyexeBatchParparedSQL(title, Jurl, Content, nrec, date);
						    	
						    	/** 儲存至MySQL 測試數據 **/
								mysql.UnityBatchParparedSQL(title, Cls, Jurl, content, contentHTML, nrec, chineseparser.getmap(), date);
	
						    }
						}
					}
					
				}
				

				/*
				Set<String> links = new HashSet<String>();
				for(Element element : elements) {
					String url = element.getElementsByTag("a").attr("abs:href");
					links.add(url);
				}
				
				for(String link:links){
					LinkQueue.addUnVisitedUrl(link);
				}
				
				
				elements = doc.getElementsByClass("r-ent");
				
				for(Element element : elements) {
					
					String nrec = element.getElementsByClass("nrec").text();
					String date = "2015/"+element.getElementsByClass("date").text();
					String url = element.getElementsByTag("a").attr("abs:href");
					String title = element.getElementsByTag("a").text();
					if (!title.trim().equals(""))
					{
			        	mysql.exeBatchParparedSQL(title,url, date, nrec);
					}
				}
				*/
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
