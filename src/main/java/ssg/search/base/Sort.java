package ssg.search.base;

public class Sort {
	private String sortName;
	private int    operator;
	
	public Sort(String sortName, int operator){
		this.sortName = sortName;
		this.operator = operator;
	}
	
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public int getOperator() {
		return operator;
	}
	public void setOperator(int operator) {
		this.operator = operator;
	}
}
