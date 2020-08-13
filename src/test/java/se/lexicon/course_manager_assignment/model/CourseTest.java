package se.lexicon.course_manager_assignment.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Course course;

    @Test
    public void constructor_IsWorking() {
        //Arrange
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);

        //Assert
        assertEquals(1, course.getId());
        assertEquals("Test Course", course.getCourseName());
        assertEquals(LocalDate.of(2021, 10, 15), course.getStartDate());
        assertEquals(12, course.getWeekDuration());
    }

    @Test
    public void emptyConstructor_IsWorking() {
        //Arrange
        course = new Course();

        //Assert
        assertEquals(0, course.getId());
        assertNull(course.getCourseName());
        assertNull(course.getStartDate());
        assertEquals(0, course.getWeekDuration());
        assertNull(course.getStudents());
    }

    @Test
    public void test_Setters() {
        //Arrange
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        course.setCourseName("NewName");
        course.setStartDate(LocalDate.of(2022, 12, 15));
        course.setWeekDuration(15);

        //Assert
        assertEquals("NewName", course.getCourseName());
        assertEquals(LocalDate.of(2022, 12, 15), course.getStartDate());
        assertEquals(15, course.getWeekDuration());
    }

    @Test
    public void setStudents_IsWorking() {
        // Arrange
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        Student student1 = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        Student student2 = new Student(2, "Berit Beritsson", "berit.beritsson@hotmail.com", "Beritssvägen 5 Beritstorp");
        course.enrollStudent(student1);
        Collection<Student> oneStudent = new ArrayList<>(course.getStudents()); // New object with only one student
        course.enrollStudent(student2); // course now has two students

        // Act
        course.setStudents(oneStudent);

        //Assert
        assertEquals(oneStudent, course.getStudents());
    }

    @Test
    public void enrollStudent_ValidEntry() {
        //Arrange
        Student student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        int oldSize = course.getStudents().size();

        //Act
        boolean result = course.enrollStudent(student);

        //Assert
        assertTrue(course.getStudents().contains(student));
        assertEquals(oldSize + 1, course.getStudents().size());
        assertTrue(result);
    }

    @Test
    public void enrollStudent_AddNull() {
        //Arrange
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);

        //Act
        boolean result = course.enrollStudent(null);

        //Assert
        assertTrue(course.getStudents().isEmpty());
        assertFalse(result);
    }

    @Test
    public void enrollStudent_Duplicate() {
        //Arrange
        Student student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        course.enrollStudent(student);
        int expectedSize = course.getStudents().size();

        //Act
        boolean result = course.enrollStudent(student);

        //Assert
        assertEquals(expectedSize, course.getStudents().size());
        assertFalse(result);
    }

    @Test
    public void unenrollStudent_ValidEntry() {
        //Arrange
        Student student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        course.enrollStudent(student);
        int oldSize = course.getStudents().size();

        //Act
        boolean result = course.unenrollStudent(student);

        //Assert
        assertEquals(oldSize - 1, course.getStudents().size());
        assertTrue(course.getStudents().isEmpty());
        assertTrue(result);
    }

    @Test
    public void unenrollStudent_InvalidEntry() {
        Student student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        course.enrollStudent(student);
        int expectedSize = course.getStudents().size();

        //Act
        boolean result = course.unenrollStudent(null);

        //Assert
        assertEquals(expectedSize, course.getStudents().size());
        assertFalse(course.getStudents().isEmpty());
        assertFalse(result);
    }

    @Test
    public void equals_And_HashCode_IsWorking() {
        //Arrange
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        Course duplicate = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);

        //Assert
        assertEquals(duplicate, course);
        assertEquals(duplicate.hashCode(), course.hashCode());
    }

    @Test
    public void testToString() {
        //Arrange
        course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);

        //Act
        String result = course.toString();

        //Assert
        assertTrue(result.contains(Integer.toString(course.getId())));
        assertTrue(result.contains(course.getCourseName()));
        assertTrue(result.contains(course.getStartDate().toString()));
        assertTrue(result.contains(Integer.toString(course.getWeekDuration())));
    }
}