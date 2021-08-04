package testURL;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import test.URLTestController;

public class URLTest {

	MockMvc mock;
	
	@Before
	public void setup() {
		//MockMvc 객체를 생성하는 부분
		//null자리는 무엇이 오는가? 테스트할 컨트롤러 객체
		//mock = MockMvcBuilders.standaloneSetup(null).build();
		mock = MockMvcBuilders.standaloneSetup(new URLTestController()).build();
	}
	
	//내용을 테스트하는 함수(content)
	//@Test
	public void urlTest() throws Exception {
		mock.perform(get("/hello").accept(MediaType.parseMediaType("application/html;charset=UTF-8")))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/html;charset=UTF-8"))
		.andExpect(content().string("hello11 world"));
	}
	
	//view 테스트(jsp페이지 있는지 여부 확인) url을 통해 page여부 확인 (파일이 존재하는 여부 확인)
	//@Test
	public void pageTest() throws Exception {
		mock.perform(get("/pagetest").accept(MediaType.parseMediaType("application/html;charset=UTF-8")))
		.andExpect(view().name("/WEB-INF/view/viewtest.jsp"));
		mock.perform(get("/modeldatatest").accept(MediaType.parseMediaType("application/html;charset=UTF-8")))
		.andExpect(view().name("/WEB-INF/view/viewtest.jsp"));
	}
	
	//model 테스트( ctag ${} 데이터가 정상적으로 나타나는지 테스트)
	//@Test
	public void modelTest() throws Exception {
		mock.perform(get("/datatest").accept(MediaType.parseMediaType("application/html;charset=UTF-8")))
		.andExpect(model().attribute("name", "parkjeabum"));
	}
	
	/*
	 * @Test public void modelTest() throws Exception {
	 * mock.perform(get("/datatest").accept(MediaType.parseMediaType(
	 * "application/html;charset=UTF-8"))) .andExpect(model().attribute("name",
	 * "parkjeabum"));
	 * 
	 * }
	 */
}
