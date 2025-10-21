package loyola.MubarakParmveer.main;

import java.util.ArrayList;

public class Grade {

    String Student;
    ArrayList<Float> weight = new ArrayList<>();
    ArrayList<Float> list_of_score = new ArrayList<>();

    public void addGrade(String student, float weight, float score) {
        this.weight.add(weight);
        list_of_score.add(score);
        Student = student;
    }

    public double getFinalGrade() {
        double total = 0.0;
        double total_weight = 0.0;
        for (int i = 0; i < list_of_score.size(); i++) {
            double decimal_form = weight.get(i) / 100.0;
            total_weight += decimal_form;
            double grade_value = decimal_form * list_of_score.get(i);
            total += grade_value;
        }
        if (total_weight == 0.0) return 0.0;
        return total / total_weight;
    }

    public String getLetterGrade() {
        double Letter_value = getFinalGrade();
        if (Letter_value >= 90) {
            return "A";
        } else if (Letter_value >= 80) {
            return "B";
        } else if (Letter_value >= 70) {
            return "C";
        } else if (Letter_value >= 60) {
            return "D";
        } else {
            return "E";
        }
    }
}
