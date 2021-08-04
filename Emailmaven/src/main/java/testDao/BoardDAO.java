package testDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
	
	int countPerPage = 10;
	public BoardDAO() {
	}

	public Connection connect() {
		Connection con = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String DBuser = "testuser";
		String DBpassword = "1111";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, DBuser, DBpassword);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if (con == null) {
			System.out.println("BoardDAO connect() error!");
		}
		return con;
	}

	public void close(Connection con, PreparedStatement psmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		close(con, psmt);
	}

	public void close(Connection con, PreparedStatement psmt) {
		try {
			psmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insert(BoardVO board) {
		Connection con = connect();
		PreparedStatement psmt = null;
		int result = 0;
		String sql = null;
		try {
			con.setAutoCommit(false);
			sql = "insert into board values(";
			sql += "board_idx_seq.nextval,";
			sql += "?,?,0,";
			sql += "board_groupid_seq.nextval,0,0,";
			sql += "0,";
			sql += "?,?,sysdate";
			sql += ")";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, board.getTitle());
			psmt.setString(2, board.getContent());
			psmt.setString(3, board.getWriteId());
			psmt.setString(4, board.getWriteName());

			result = psmt.executeUpdate();

			if (result > 0) {
				con.commit();
				System.out.println("sql �� �Է� ����");
			} else {
				con.rollback();
				System.out.println("sql �� �Է� ����");
			}
			close(con, psmt);
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	public List<BoardVO> select() {
		Connection con = connect();
		List<BoardVO> list = new ArrayList<BoardVO>();
		String sql = "select * from board order by groupid desc";
		try {
			PreparedStatement psmt = con.prepareStatement(sql);
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				list.add(new BoardVO(rs.getInt("idx"), rs.getString("title"), rs.getString("content"),
						rs.getInt("readcount"), rs.getInt("groupid"), rs.getInt("depth"), rs.getInt("re_order"),
						rs.getInt("isdel"), rs.getString("write_id"), rs.getString("write_name"),
						rs.getDate("write_day")));
			}
			close(con, psmt, rs);
		} catch (SQLException e) {
			System.out.println("BoardDAO.select() error!");
			e.printStackTrace();
		}
		return list;
	}

	public BoardVO select(int idx) {
		Connection con = connect();
		BoardVO board = null;
		String sql = "select * from board where idx=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, idx);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				board = new BoardVO();
				board.setIdx(rs.getInt("idx"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setReadcount(rs.getInt("readcount"));
				board.setGroupid(rs.getInt("groupid"));
				board.setDepth(rs.getInt("depth"));
				board.setReOrder(rs.getInt("re_order"));
				board.setIsdel(rs.getInt("isdel"));
				board.setWriteId(rs.getString("write_id"));
				board.setWriteName(rs.getString("write_name"));
				board.setWriteDay(rs.getDate("write_day"));
			}
			close(con, pstmt, rs);
		} catch (Exception e) {
		}
		return board;
	}
	
	public PageBoard searchlist(String field, String search) {
		int requestPage = 1;
		Connection con = connect();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = null;
		PageBoard pageboard = null;
		// ������ ���� ��������
		// ��û ������ ��������(requestPage)
		int 
		totalPage = 0, // ��ü ������
		beginPage = 0, // ���� ������
		endPage = 0, // ������ ������
		firstRow = 0, // ���� �۹�ȣ
		endRow = 0, // �� �۹�ȣ
		articleCount = 0, // ��ü ���Ǽ�
		countPerPage = this.countPerPage; // ���������� �Խñۼ�
		List<BoardVO> list = new ArrayList<BoardVO>();
		// countPerPage ��ŭ�� �Խñ� list
		try {
			// 1.��ü �Խù� �� ���ϱ�(articleCount)
			sql = "select count(*) from board where "+field+" like '%"+search+"%'";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			if (rs.next()) {
				articleCount = rs.getInt(1);
			} else {
				articleCount = 0;
			}
			System.out.println("�Խñۼ�:" + articleCount);
			// 2.��ü������ ��(totalPage)
			totalPage = articleCount / countPerPage;
			if (articleCount % countPerPage > 0) {
				totalPage++;
			}
			System.out.println("��ü������:" + totalPage);
			System.out.println("����������:"+ requestPage);
			// 3.��û�� �������� ���� ���� �۹�ȣ, �� �۹�ȣ(firstRow, endRow)
			firstRow = (requestPage - 1) * countPerPage + 1;
			endRow = firstRow + countPerPage - 1;
			System.out.printf("firstRow:%d, endRow:%d\n", firstRow, endRow);
			// 4.������������ȣ, �������� ��ȣ(beginPage, endPage)
			System.out.println("requestPage:"+requestPage);
			// ����,���� ��ư���� 5ĭ�� �����̰� ��������ư�� 5���� �����Ǿ� ���� ��
			int state = (requestPage-1)/5;
			beginPage = state*5+1;
			endPage = beginPage+4;
			if(beginPage < 1) {
				beginPage =1;
			}
			if(endPage > totalPage) {
				endPage = totalPage;
			}

			System.out.printf("beginPage:%d, endPage:%d\n", beginPage, endPage);
			// 5.�������� �ش��ϴ� ����Ʈ(firstRow, endRow)
			sql = "select c.* from " + "(select rownum rnum, b.* from "
					+ "(select * from board a where "+field+" like '%"+search+"%' order by a.groupid desc, a.depth asc, a.idx asc) b where rownum <= ?) c "
					+ "where rnum >= ? ";
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, endRow); // 10ū��
			psmt.setInt(2, firstRow);// 1������
			rs = psmt.executeQuery();
			// 6.DB�� ����Ʈ�� board��ü�� ��� ����
			while (rs.next()) {
				// System.out.println("check");
				BoardVO board = new BoardVO();
				board.setIdx(rs.getInt("idx"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setReadcount(rs.getInt("readcount"));
				board.setGroupid(rs.getInt("groupid"));
				board.setDepth(rs.getInt("depth"));
				board.setReOrder(rs.getInt("re_order"));
				board.setIsdel(rs.getInt("isdel"));
				board.setWriteId(rs.getString("write_id"));
				board.setWriteName(rs.getString("write_name"));
				board.setWriteDay(rs.getDate("write_day"));
				list.add(board);
			}
			pageboard = new PageBoard(list, requestPage, articleCount, totalPage, firstRow, endRow, beginPage, endPage);
			//System.out.println(pageboard.getList().toString());
			close(con, psmt, rs);
			// conn.commit();
		} catch (SQLException e) {
			System.out.println("BoardDAO.searchlist(String field, String search) error!");
			e.printStackTrace();
		}
		return pageboard;
	}
	
	public PageBoard pagemove(int requestPage,int pagemove) {
		Connection con = connect();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = null;
		PageBoard pageboard = null;
		// ������ ���� ��������
		// ��û ������ ��������(requestPage)
		int 
		totalPage = 0, // ��ü ������
		beginPage = 0, // ���� ������
		endPage = 0, // ������ ������
		firstRow = 0, // ���� �۹�ȣ
		endRow = 0, // �� �۹�ȣ
		articleCount = 0, // ��ü ���Ǽ�
		countPerPage = this.countPerPage; // ���������� �Խñۼ�
		List<BoardVO> list = new ArrayList<BoardVO>();
		// countPerPage ��ŭ�� �Խñ� list
		try {
			// 1.��ü �Խù� �� ���ϱ�(articleCount)
			sql = "select count(*) from board";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			if (rs.next()) {
				articleCount = rs.getInt(1);
			} else {
				articleCount = 0;
			}
			System.out.println("�Խñۼ�:" + articleCount);
			// 2.��ü������ ��(totalPage)
			totalPage = articleCount / countPerPage;
			if (articleCount % countPerPage > 0) {
				totalPage++;
			}
			if(pagemove == -1) {
				if(requestPage%5 !=0) {
					requestPage = (requestPage/5-1)*5+1;
				} else {
					requestPage = (requestPage/5-2)*5+1;
				}
			} else if (pagemove == 1) {
				if(requestPage%5 !=0) {
					requestPage = (requestPage/5+1)*5+1;
				} else {
					requestPage = (requestPage/5)*5+1;
				}
			}
			if(requestPage <1) {
				requestPage = 1;
			} else if (requestPage > totalPage) {
				requestPage = totalPage;
			}
			System.out.println("��ü������:" + totalPage);
			System.out.println("����������:"+ requestPage);
			// 3.��û�� �������� ���� ���� �۹�ȣ, �� �۹�ȣ(firstRow, endRow)
			firstRow = (requestPage - 1) * countPerPage + 1;
			endRow = firstRow + countPerPage - 1;
			System.out.printf("firstRow:%d, endRow:%d\n", firstRow, endRow);
			// 4.������������ȣ, �������� ��ȣ(beginPage, endPage)
			System.out.println("requestPage:"+requestPage);
			// ����,���� ��ư���� 5ĭ�� �����̰� ��������ư�� 5���� �����Ǿ� ���� ��
			int state = (requestPage-1)/5;
			beginPage = state*5+1;
			endPage = beginPage+4;
			if(beginPage < 1) {
				beginPage =1;
			}
			if(endPage > totalPage) {
				endPage = totalPage;
			}

			System.out.printf("beginPage:%d, endPage:%d\n", beginPage, endPage);
			// 5.�������� �ش��ϴ� ����Ʈ(firstRow, endRow)
			sql = "select c.* from " + "(select rownum rnum, b.* from "
					+ "(select * from board a order by a.groupid desc, a.depth asc, a.idx asc) b where rownum <= ?) c "
					+ "where rnum >= ? ";
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, endRow); // 10ū��
			psmt.setInt(2, firstRow);// 1������
			rs = psmt.executeQuery();
			// 6.DB�� ����Ʈ�� board��ü�� ��� ����
			while (rs.next()) {
				// System.out.println("check");
				BoardVO board = new BoardVO();
				board.setIdx(rs.getInt("idx"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setReadcount(rs.getInt("readcount"));
				board.setGroupid(rs.getInt("groupid"));
				board.setDepth(rs.getInt("depth"));
				board.setReOrder(rs.getInt("re_order"));
				board.setIsdel(rs.getInt("isdel"));
				board.setWriteId(rs.getString("write_id"));
				board.setWriteName(rs.getString("write_name"));
				board.setWriteDay(rs.getDate("write_day"));
				list.add(board);
			}
			pageboard = new PageBoard(list, requestPage, articleCount, totalPage, firstRow, endRow, beginPage, endPage);
			//System.out.println(pageboard.getList().toString());
			close(con, psmt, rs);
			// conn.commit();
		} catch (SQLException e) {
			System.out.println("BoardDAO.select(int requestPage) error!");
			e.printStackTrace();
		}
		return pageboard;
	}

	public int update(int idx, String title, String content) {
		Connection con = connect();
		PreparedStatement ps;
		try {
			String sql = "update board set title=?, content=? where idx=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, content);
			ps.setInt(3, idx);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO.java update() error!");
			return -1;
		}
	}

	public int delete(int idx) {
		Connection con = connect();
		PreparedStatement ps;
		try {
			String sql = "delete from board where idx=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, idx);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO.java update() error!");
			return -1;
		}
	}

	/* replyInsert�Լ����� �̿� */
	public boolean checkParent(int idx) {
		BoardVO board = null;
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = null;
		int result = 0;
		try {
			con = connect();
			sql = "select count(*) from board where idx=? and isdel=0";
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, idx);
			rs = psmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			if (result != 1) {
				return false;
			}
			close(con, psmt, rs);
		} catch (Exception e) {
		}
		return true;
	}

	/* replyInsert�Լ����� �̿� */
	public void reply_before_update(int depth, int groupid) {
		Connection con = null;
		PreparedStatement psmt = null;
		String sql = null;
		try {
			con = connect();
			sql = "update board set depth=depth+1 where groupid=? and depth>=?";
			psmt = con.prepareStatement(sql);
			psmt.setInt(1, groupid);
			psmt.setInt(2, depth);
			psmt.executeUpdate();
			close(con, psmt);
		} catch (Exception e) {
		}
	}

	/* ���� �� �Լ� �̿� */
	public int replyInsert(BoardVO board) {
		// �����
		// 1.�θ���� �����ϴ� ���� Ȯ�� - checkParent(board.getIdx());
		// 2.��۰� ����� �ִ� �ۿ� ���� groupid, depth,[re_order]���� ���� -
		// reply_before_update(depth, groutid);
		// 3.����� ��� //��� insert();
		Connection con = null;
		PreparedStatement psmt = null;
		String sql = null;
		int result = 0;
		try {
			con = connect();
			con.setAutoCommit(false);
			// �θ�� ���翩�� Ȯ��
			if (!checkParent(board.getIdx())) {
				con.rollback();
				return 0;
			}
			System.out.println("�θ�� Ȯ��");
			// ��۰� ���õ� �ۿ� ���� ������Ʈ
			reply_before_update(board.getDepth(), board.getGroupid());
			System.out.println("���� ��� �Է� �Ϸ�");
			// insert�۾�
			sql = "insert into board values(";
			sql += "board_idx_seq.nextval,";
			sql += "?,?,0,"; // 1.title, 2.content, readcount
			sql += "?,?,?,"; // 3.groupid, 4.depth, 5.re_order
			sql += "0,"; // ��������
			sql += "?,?,sysdate"; // 6.write_id, 7.write_name, 8.��¥
			sql += ")";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, board.getTitle());
			psmt.setString(2, board.getContent());
			psmt.setInt(3, board.getGroupid());
			psmt.setInt(4, board.getDepth());
			psmt.setInt(5, board.getReOrder());
			psmt.setString(6, board.getWriteId());
			psmt.setString(7, board.getWriteName());

			result = psmt.executeUpdate();
			System.out.println("result:" + result);
			if (result == 0) {
				con.rollback();
				System.out.println("��� ����");
				return 0;
			} else {
				con.commit();
				System.out.println("��� �Է� �Ϸ�");
			}

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	public int readcountUpdate(int idx) {
		Connection con = connect();
		PreparedStatement ps;
		try {
			String sql = "update board set readcount=readcount+1 where idx=?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, idx);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("BoardDAO.java readcountUpdate() error!");
			return -1;
		}
		
	}

}
