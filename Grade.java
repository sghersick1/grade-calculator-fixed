//Letter grade cuts off 90->B
//Skips first grade


package Grade;

import java.util.*;
import java.io.*;

public class Grade {
	
	String Student;
	ArrayList<Float> weight = new ArrayList<>();
	ArrayList<Float> list_of_score = new ArrayList<>();;

	public static void main(String[] args) {
		Grade grade = new Grade();
		grade.addGrade("Name", 10, 90);
		grade.addGrade("Name", 20, 85);
		grade.getFinalGrade();
		grade.getLetterGrade();
	}
	
	public void addGrade(String student, float weight, float score) {
		this.weight.add(weight);
		list_of_score.add(score);
		Student = student;
	}
	
	public void getFinalGrade() {
		float total = 0;
		float total_weight = 0;
		for (int i=1;i<list_of_score.size(); i++) {
			float decimal_form = weight.get(i) / 100;
			total_weight += decimal_form;
			float grade_value = decimal_form * list_of_score.get(i);
			total += grade_value;
		}
		System.out.println("The final grade is:" + total/total_weight);
	}
	
	public void getLetterGrade() {
		float total = 0;
		float total_weight = 0;
		for (int i=1;i<list_of_score.size(); i++) {
			float decimal_form = weight.get(i) / 100;
			total_weight += decimal_form;
			float grade_value = decimal_form * list_of_score.get(i);
			total += grade_value;
		}
		float Letter_value = total/total_weight;
		if (Letter_value > 90) {
			System.out.println("You got a A");
		}
		else if (Letter_value > 80) {
			System.out.println("you got a B");
		}
		else if (Letter_value > 70) {
			System.out.println("you got a C");
		}
		else if (Letter_value > 60) {
			System.out.println("you got a D");
		}
		else{
			System.out.println("you got a E");
		}
	}
}
