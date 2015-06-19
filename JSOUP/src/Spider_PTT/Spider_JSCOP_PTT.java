package Spider_PTT;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

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


public class Spider_JSCOP_PTT {

	
	static int count = 0;
	private static void initCrawlerWithSends(String  sends){
		/*
		for(String send:sends){
			LinkQueue.addUnBisitedUrl(send);
		}*/
		LinkQueue.addUnVisitedUrl(sends);
	}
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		
		ConnMySQL mysql = new ConnMySQL();

		trustEveryone();
		enableSSLSocket();
		
		String Jurl ="https://www.ptt.cc/bbs/Gossiping/index.html";
		initCrawlerWithSends(Jurl);
		

	    while(!LinkQueue.nuVisitedUrlEmpty()){
	    	
	    	++count;
	    	String  viditUrl = (String)LinkQueue.unVisitedUrl();
	    	
	    	Connection conn = Jsoup.connect(viditUrl)
	    			//.userAgent("Mozilla/5.0 Chrome/26.0.1410.64 Safari/537.31")
	    			.userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
	    			.timeout(10000)
	    			.followRedirects(true)
	    			.maxBodySize(1024*1024*3)
	    			.ignoreContentType(true)
	    			.ignoreHttpErrors(true);
	    	LinkQueue.addVisitedUrl(viditUrl);
	    
			try {
				Thread.sleep(100);
				Document doc = conn.get();
			
				Elements elements = doc.select("#action-bar-container > div > div.btn-group.pull-right > a:nth-child(2)");
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
				
				/*
				Elements Titleelements = doc.getElementsByClass("title");
				Elements Dateeelements = doc.getElementsByClass("date");
				
				for(int i=0; i <= elements.size() ;i++){
					String url = Titleelements.get(i).getElementsByTag("a").attr("abs:href");
					String title = Titleelements.get(i).getElementsByTag("a").text();
					String date = "2015/"+Dateeelements.get(i).getElementsByTag("div").text();
					System.out.print(date+ " -> ");
		        	System.out.print(url + " -> ");
		        	System.out.println(title);
		        	System.out.println("---------------------------------");
				}
			
				*/
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    
	    System.out.println(count);
	    
	    
		
		
		
	    /*
		try {
			Connection conn = Jsoup.connect(Jurl);
			conn.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
		    Document doc;
			doc = conn.get();
			
			/*
			doc = conn.get();
			
			//Elements elements = doc.select("body");
			Elements elements = doc.getElementsByClass("title");
			for(Element element : elements) {
				String url = element.getElementsByTag("a").attr("abs:href");
				String title = element.getElementsByTag("a").text();
				
				System.out.println(url);
	        	System.out.println(title);
	        	System.out.println("---------------------------------");
			}
			*/
			
			/*
			Elements elements = doc.select("tbody tr");
	        for(Element element : elements) {
	        	String companyName = element.getElementsByTag("company").text();
	        	String time = element.select("td.text-center").first().text();
	        	String address = element.getElementsByClass("preach-tbody-addre").text();
	        	
	        	System.out.println("公司："+companyName);
	        	System.out.println("宣讲时间："+time);
	        	System.out.println("宣讲学校：华中科技大学");
	        	System.out.println("具体地点："+address);
	        	System.out.println("---------------------------------");
	        }
			*/
			
			/*
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	    
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
