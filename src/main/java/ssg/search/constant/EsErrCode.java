package ssg.search.constant;

public enum EsErrCode {
	PAGE_SIZE("01"),
	TIMEOUT("02")
	;

	private String code;

	EsErrCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
