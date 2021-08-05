package testDAO2;

public class MemoVO {
	String content;

	public MemoVO() {
	}

	public MemoVO(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MemoVO [content=" + content + "]";
	}

}
