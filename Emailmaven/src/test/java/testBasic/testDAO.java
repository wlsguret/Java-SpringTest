package testBasic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import testDao.BoardDAO;

public class testDAO {
	
	@Test
	public void testDao() {
		BoardDAO dao = new BoardDAO();
		assertNotNull(dao.select());
		assertEquals(dao.insert(null),1);
	}
}
