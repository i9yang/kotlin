package ssg.search.base;

import java.util.List;

public class Info {
	private String  predicate;
	private int 	operator = 1;
	private int		start = 0;
	private int		count = 1;
	private String 	categoryDepth;
	private List<Sort> sortList;
	
	// 생성자들
	public Info(){}
	public Info(String predicate){
		this.predicate = predicate;
	}
	public Info(int operator){
		this.operator = operator;
	}
	
	public Info(String predicate, int operator){
		this.predicate = predicate;
		this.operator = operator;
	}
	
	public Info(int start, int count){
		this.start = start;
		this.count = count;
	}
	public Info(List<Sort> sortList){
		this.sortList = sortList;
	}
	public Info(String predicate, String categoryDepth){
		this.predicate = predicate;
		this.categoryDepth = categoryDepth;
	}
	
	// 생성자들
	
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public int getOperator() {
		return operator;
	}
	public void setOperator(int operator) {
		this.operator = operator;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Sort> getSortList() {
		return sortList;
	}
	public void setSortList(List<Sort> sortList) {
		this.sortList = sortList;
	}
	public String getCategoryDepth(){
		return categoryDepth;
	}
	public void setCategoryDepth(String categoryDepth){
		this.categoryDepth = categoryDepth;
	}
}
