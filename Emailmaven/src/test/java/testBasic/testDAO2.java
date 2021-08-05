package testBasic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import testDAO2.MemoDAO;

@RunWith(SpringJUnit4ClassRunner.class) // ContextConfiguration을 적용시키기 위해서 현재 클래스를 동작시킨다.
@ContextConfiguration(locations = {"file:/src/main/webapp/WEB-INF/dispatcher-servlet.xml"}) // Spring 어노테이션들을 사용하기 위해 설정파일을 읽어와야한다.
public class testDAO2 {
	
	// 일반 객체일 때 객체를 생성하여 테스트한다.
	//MemoDAO dao = new MemoDAO();
	
	// 웹프로젝트일 때 DI해줌
	@Autowired
	MemoDAO dao;
	
	@Test
	public void test() {
		assertEquals(dao.update(), 0);
//		assertSame(dao.searchlist("hello"), new ArrayList<MemoVO>());
		
	}
}
