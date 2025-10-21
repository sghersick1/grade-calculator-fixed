/**
 * Create JUnit 5 Tests for the grade calculator in the Grade class (based on Main implementation)
 * @author Sam Hersick
 * @version 1.0.0
 * @since 10/20/2025
 */

package loyola.hersick.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import loyola.hersick.main.Grade;
import loyola.hersick.main.Main;

class GradeCalculatorTest {
	Grade grade;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	/**
	 * quit from the main method
	 * @author Sam Hersick
	 */
	@Test
	void quitMainMethod() {
		
		// Input
		String userInput = String.join(System.lineSeparator(), "q") + System.lineSeparator();
	    ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
	    System.setIn(bais);
		
		// Expected 
		String expected = "What do you want to do? Enter 1 for grade calculator and 2 for car recommendation (or q to quit)";
		
		// Output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream pStream = new PrintStream(baos);
		System.setOut(pStream);
		
		// Run Application
		Main.main(null);
		
		String[] lines = baos.toString().split(System.lineSeparator());
		String actual = lines[lines.length - 1];
		
		Assertions.assertEquals(expected, actual);
	}
	
	/**
	 * Test final grade works for a single grade
	 */
	@Test
	void singleExamScoreNumberTest() {
		grade = new Grade();
		grade.addGrade("exam", 100, 81);
		double actual = grade.getFinalGrade();
		
		Assertions.assertEquals(81, actual);
	}
	
	/**
	 * Test letter grade works for a single grade
	 */
	@Test
	void singleExamScoreLetterTest() {
		grade = new Grade();
		grade.addGrade("exam", 100, 81);
		String actual = grade.getLetterGrade();
		
		Assertions.assertEquals("B", actual);
	}
	
	/**
	 * Test letter grade works on edge case
	 */
	@Test
	void letterGradeEdgeTest() {
		grade = new Grade();
		grade.addGrade("exam", 100, 90.0f);
		String actual = grade.getLetterGrade();
		
		Assertions.assertEquals("A", actual);
	}
	
	/**
	 * Test letter grade works for F
	 */
	@Test
	void lowGradeTest() {
		grade = new Grade();
		grade.addGrade("exam", 100, 35.1f);
		String actual = grade.getLetterGrade();
		
		Assertions.assertEquals("F", actual);
	}
	
	/**
	 * Test letter grades being added < 0
	 */
	@Test
	void illegallyLowGradeTest() {
		grade = new Grade();
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
	        grade.addGrade("exam", 100, -10);
	    });
	}

	/**
	 * Test multiple scores
	 */
	@Test
	void multiScoreTest() {
		grade = new Grade();
		grade.addGrade("exam", 50, 100);
		grade.addGrade("examB", 50, 60);
		double actual = grade.getFinalGrade();
		
		Assertions.assertEquals(80, actual);
	}
	
	/**
	 * Test multiple scores with weights that don't add to 100
	 */
	@Test
	void multiScoreNonHundredWeightTest() {
		grade = new Grade();
		grade.addGrade("exam", 25, 100);
		grade.addGrade("examB", 25, 60);
		double actual = grade.getFinalGrade();
		
		Assertions.assertEquals(80, actual);
	}
	
	/**
	 * Test getting final grade with adding any individual grades
	 */
	@Test
	void emptyScoreTest() {
		grade = new Grade();
		double actual = grade.getFinalGrade();
		Assertions.assertEquals(0, actual);
	}
	
	/**
	 * Test right below letter grade threshold, shouldn't round up
	 */
	@Test
	void nullValueTest() {
		grade = new Grade();
		grade.addGrade("exam", 100, 89.65f);
		String actual = grade.getLetterGrade();
		Assertions.assertEquals("B", actual);
	}
	
	/**
	 * Test adding a grade with a non-positive weight
	 */
	@Test
	void negativeWeightTest() {
		grade = new Grade();
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			grade.addGrade("exam", -10, 87);
		});
	}
}
