package testURL;

public class LoginVO {
	String id;
	String password;

	public LoginVO() {
		System.out.println("기본생성자 실행");
	}

	public LoginVO(String id, String password) {
		this.id = id;
		this.password = password;
		System.out.println("필드생성자 실행");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		System.out.println("setId");
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		System.out.println("setPassword");
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginVO [id=" + id + ", password=" + password + "]";
	}
}
