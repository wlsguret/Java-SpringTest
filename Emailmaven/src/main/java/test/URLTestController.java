package test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class URLTestController {
	
	@RequestMapping("/hello")
	@ResponseBody
	public String hello() {
		return "hello world";
	}
	
	@RequestMapping("/pagetest")
	public String pageTest() {
		return "/WEB-INF/view/viewtest.jsp";
	}
	
	@RequestMapping("/datatest")
	public String dataTest(ModelMap modelmap) {
		modelmap.put("name", "hongildong");
		return "/WEB-INF/view/viewtest.jsp";
	}
	
	@RequestMapping("/modeldatatest")
	public ModelAndView modelDateTest() {
		ModelAndView mv = new ModelAndView("/WEB-INF/view/viewtest.jsp");
		mv.addObject("name", "sinbum");
		return mv;
	}
	
	@RequestMapping("/formtest")
	public String formTest() {
		return "/WEB-INF/view/formtest.jsp";
	}
	
	@RequestMapping("/formtestProc")
	@ResponseBody
	public String formTestProc(String test) {
		return test;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
