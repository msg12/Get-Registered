
/**
 * 
 */
package com.flipkart.bean;

import java.util.HashMap;

/**
 * @author jedi08
 * Class to implement Report Card
 *
 */
public class ReportCard {
	private String studentId;
	private HashMap<String, String> grades = new HashMap<String, String>();
	private int semester;
	private String grade;

	/**
	 * Constructors to instantiate a report card object
	 */
	public ReportCard() {
		super();
	}

	public ReportCard(String studentId, HashMap<String, String> grades, int sem, String str) {
		super();
		this.studentId = studentId;
		this.grades = grades;
		this.semester = sem;
		this.grade = str;
	}

	

	/**
	 * Getter Setters for the Report Card
	 *
	 */
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public HashMap<String, String> getGrades() {
		return grades;
	}

	public void setGrades(HashMap<String, String> grades) {
		this.grades = grades;
	}

	public int getSem() {
		return semester;
	}

	public void setSem(int sem) {
		this.semester = sem;
	}

	public String getgrade() {
		return grade;
	}

	public void setgrade(String CPI) {
		this.grade = CPI;
	}

	/**
	 * Method to Convert professor Id to string
	 * @return ID as string value
	 */
	@Override
	public String toString() {
		return "Grade [studentId=" + studentId + ", grades=" + grades + ", sem=" + semester + ", CPI=" + grade + "]";
	}

}
