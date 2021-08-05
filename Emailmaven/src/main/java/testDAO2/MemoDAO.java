package testDAO2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MemoDAO {
	Connection conn;

	public MemoDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "testuser", "1111");
			/* System.out.println(conn); */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<MemoVO> searchlist(String search) {
		String sql = "select * from memo where content like '%" + search + "%'";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			List<MemoVO> list = new ArrayList<MemoVO>();
			while (rs.next()) {
				list.add(new MemoVO(rs.getString("content")));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int insert() {
		return 0;
	}
	
	public int update() {
		return 0;
	}
	
	public int delete() {
		return 0;
	}
}
