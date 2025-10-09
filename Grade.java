package Grade;

import java.util.*;
import java.io.*;

public class Grade {
	
	String Student;
	ArrayList<Float> weight = new ArrayList<>();
	ArrayList<Float> list_of_score = new ArrayList<>();;

	public static void main(String[] args) {
		
		System.out.println("What do you want to do? Enter 1 for grade calculator and 2 for car recommendation");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		while (!(input.equals("1")) && !(input.equals("2"))) {
			System.out.println("Please enter 1 or 2");
			input = scanner.nextLine();
		}
		
		if(input.equals("1")) {
			Grade grade = new Grade();
			String action = "";
			while (!(action.equals("exit"))) {
				System.out.println("Please enter the Name, The weight of the grade, and the score. Enter exit when done.");
				action = scanner.nextLine();
				action.toLowerCase();
				if (action.equals("exit")) {
					continue;
				}
				String[] part = action.split(" ");
				
				String name = part[0];
				float weight = Float.parseFloat(part[1]);
				float score = Float.parseFloat(part[2]);
				grade.addGrade(name, weight, score);
				}
			String choise = "";
			while (!(choise.equals("exit"))) {
				System.out.println("What would you like to see. 1 for final grade and 2 for final letter grade.");
				choise = scanner.nextLine();
				choise.toLowerCase();
				if (choise.equals("exit")) {
					continue;
				}
				else if (choise.equals("1")) {
					grade.getFinalGrade();
				}
				else if (choise.equals("2")) {
					grade.getLetterGrade();
				}
				else {
					System.out.println("Error please enter 1, 2, or exit");
				}
			}
		}
		
		//if (input.equals("2")) {
			
		//}
		
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
			float grade_value = decimal_form + list_of_score.get(i);
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
