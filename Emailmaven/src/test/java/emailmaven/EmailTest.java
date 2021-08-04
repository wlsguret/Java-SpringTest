package emailmaven;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"})
public class EmailTest {
	
	@Autowired
	JavaMailSender mail;
	
	@Test
	public void test() {
		//MimeMessage message; (보낸 사람 메일주소, 받는 사람 메일주소, 제목, 내용)
		MimeMessagePreparator mailInfo = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
				message.setTo("wlsguret@naver.com");
				message.setSubject("자바로 메일 보내기");
				message.setText("자바 메일 보내기 성공");
				message.setFrom("jinhyuk0117@gmail.com");
			}
		};
		mail.send(mailInfo);;
	}

}
