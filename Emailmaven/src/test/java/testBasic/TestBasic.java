package testBasic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

public class TestBasic {
	
	@Test
	public void test() {
//		assertArrayEquals(a,b) 배열 a와 b가 일치함을 확인
		int[] array1 = {1,2,3,4,5}; int[] array2 = {1,2,3,4,5};
		assertArrayEquals(array1, array2);
		 
//		assertEquals(a,b) 객체 a와 b가 일치함을 확인
		int a = 10;
		assertEquals(a, 10);
		
//		assertSame(a,b) 객체 a와 b가 같은 객체임을 확인
		String s1 = "12345";
		String s2 = s1;
		int s3 = 12345;
		assertSame(s1, s2);
		assertNotSame(s1, s1);
		System.out.println("실패후 계속 진행됨");
//		assertTrue(a) a값이 참인지 확인
		assertTrue(15 != 14);
		assertFalse(0 < 100);
		
//		assertNotNull(a) 객체 a가 null이 아님을 확인
		Date date1 = new Date();
		Date date2 = null;
		assertNotNull(date1);
		assertNull(date2);
	}
}
