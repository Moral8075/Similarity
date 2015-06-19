package Spider_PTT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnMySQL {

	static Connection conn ;
	
	public ConnMySQL(){
		   String driver = "com.mysql.jdbc.Driver";   // 連線工具
		   String url = "jdbc:mysql://localhost/Spider?characterEncoding=UTF-8";   // 目的地的位置
		   String user = "root";   // MySQL localhost 的用戶名
		   String password = "root123";   // MySQL localhost 的密碼
		   try {
		      Class.forName(driver);
		      conn = DriverManager.getConnection(url, user, password);
		      if(conn != null && !conn.isClosed()){
		         System.out.println("資料庫連線測試成功！");
		      }
		   }
		   catch(ClassNotFoundException cnfe){
		      System.out.println("找不到驅動程式類別");
		      cnfe.printStackTrace();
		   }
		   catch(SQLException sqle) {
		      sqle.printStackTrace();
		   }
		}
	
	
	public static void exeBatchParparedSQL(String Title,String Link,String Time,String Repnum) { 

        try { 
                String sql = "insert into PTT (TITLE,LinkURL, PoTIME,Repnum) values (?,?,?,?)"; 
                PreparedStatement pstmt = conn.prepareStatement(sql); 
                
                pstmt.setString(1, Title); 
                pstmt.setString(2, Link); 
                pstmt.setString(3, Time);
                pstmt.setString(4, Repnum);
                pstmt.addBatch();                     
                
                
                pstmt.executeBatch(); 
        } catch (SQLException e) { 
                e.printStackTrace(); 
        } 
	} 
	
	
	public static void AppledailyexeBatchParparedSQL(String Title,String Link,String content
													,String Repnum,String Time) { 

        try { 
                String sql = "insert into Appledaily (TITLE,PoURL,Content,NREC,TIME) values (?,?,?,?,?)"; 
                PreparedStatement pstmt = conn.prepareStatement(sql); 
                
                pstmt.setString(1, Title); 
                pstmt.setString(2, Link); 
                pstmt.setString(3, content);
                pstmt.setString(4, Repnum);
                pstmt.setString(5, Time);
                pstmt.addBatch();                     
                
                
                pstmt.executeBatch(); 
        } catch (SQLException e) { 
                e.printStackTrace(); 
        } 
	} 
	
	public static void LTNexeBatchParparedSQL(String Title,String Link,String content
			,String contentHTML ,String Repnum,String Time) { 
		try { 
			String sql = "insert into LTN (TITLE,PoURL,Content,ContentHTML,NREC,TIME) values (?,?,?,?,?,?)"; 
			PreparedStatement pstmt = conn.prepareStatement(sql); 
			
			pstmt.setString(1, Title); 
			pstmt.setString(2, Link); 
			pstmt.setString(3, content);
			pstmt.setString(4, contentHTML);
			pstmt.setString(5, Repnum);
			pstmt.setString(6, Time);
			pstmt.addBatch();                     
			
			
			pstmt.executeBatch(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			} 
	} 
	
	public static void UnityBatchParparedSQL(String Title,String Cla,String Link,String content
			,String contentHTML ,String Repnum,String WordKey,String Time) { 
		try { 
			String sql = "insert into WebSpider (Title,Cla,PoURL,Content,ContentHTML,Reply,WordKey,Time) values (?,?,?,?,?,?,?,?)"; 
			PreparedStatement pstmt = conn.prepareStatement(sql); 
			
			pstmt.setString(1, Title); 
			pstmt.setString(2, Cla); 
			pstmt.setString(3, Link); 
			pstmt.setString(4, content);
			pstmt.setString(5, contentHTML);
			pstmt.setString(6, Repnum);
			pstmt.setString(7, WordKey);
			pstmt.setString(8, Time);
			pstmt.addBatch();                     
			
			
			pstmt.executeBatch(); 
			} catch (SQLException e) { 
				e.printStackTrace(); 
			} 
	} 
	
	public ResultSet getUnitySQL(){
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM WebSpider");
			
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
}
