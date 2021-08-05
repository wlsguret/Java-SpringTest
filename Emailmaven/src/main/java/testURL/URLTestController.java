package testURL;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	@RequestMapping("/datatest2")
	public String dataTest2(Model model) {
		model.addAttribute("name", "hongildong");
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
	public String formTestProc(String name, Model model) {
		model.addAttribute("name", name);
		return "/WEB-INF/view/viewtest.jsp";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "/WEB-INF/view/login.jsp";
	}
	
	@RequestMapping("/loginProc")
	public String loginProc(LoginVO login, Model model) {
		model.addAttribute("login", login); 
		return "/WEB-INF/view/loginProc.jsp";
	}
	
	@RequestMapping("/loginProc2")
	public String loginProc2(String id, String password, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("password", password);
		return "/WEB-INF/view/loginProc.jsp";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
