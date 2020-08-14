package se.lexicon.course_manager_assignment.data.service.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ModelToDto.class})
public class ModelToDtoTest {

    @Autowired
    private Converters testObject;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
    }


    @Test
    public void studentToStudentView() {
        //Arrange
        Student student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");

        //Act
        StudentView result = testObject.studentToStudentView(student);

        //Assert
        System.out.println(student);
        System.out.println(result);

        assertEquals(1, result.getId());
        assertEquals("Nisse Nilsson", result.getName());
        assertEquals("nisse.nilsson@hotmail.com" , result.getEmail());
        assertEquals("Nilsvägen 5 Nilstorp", result.getAddress());
    }

    @Test
    public void courseToCourseView() {
        //Arrange
        Course course = new Course(1, "Test Course", LocalDate.of(2021, 10, 15), 12);
        Student student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        course.enrollStudent(student);

        //Act
        CourseView result = testObject.courseToCourseView(course);

        //Assert
        assertEquals(1, result.getId());
        assertEquals("Test Course", result.getCourseName());
        assertEquals(LocalDate.of(2021, 10, 15), result.getStartDate());
        assertEquals(12, result.getWeekDuration());
        assertFalse(result.getStudents().isEmpty());
    }

    @Test
    public void coursesToCourseViews() {
        //Arrange
        Collection<Course> courses = new HashSet<>();
        Course course1 = new Course(1, "Java part 1 - Simple stuff", LocalDate.of(2021, 10, 15), 12);
        Course course2 = new Course(2, "Java part 2 - Complex stuff", LocalDate.of(2021, 10, 15), 12);
        courses.add(course1);
        courses.add(course2);
        boolean foundCourse1 = false, foundCourse2 = false;

        //Act
        List<CourseView> result = testObject.coursesToCourseViews(courses);
        for (CourseView searchFor : result) {
            if (searchFor.getCourseName().contains("Java part 1 - Simple stuff")) { foundCourse1 = true; }
            if (searchFor.getCourseName().contains("Java part 2 - Complex stuff")) { foundCourse2 = true; }
        }

        //Assert
        assertTrue(foundCourse1);
        assertTrue(foundCourse2);
    }

    @Test
    public void coursesToCourseViews_Null() {
        //Act
        List<CourseView> result = testObject.coursesToCourseViews(null);

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void studentsToStudentViews() {
        //Arrange
        Collection<Student> students = new HashSet<>();
        Student student1 = new Student(1, "Arne Arnesson", "arne@gmail.com", "Arnsvägen 5 Arnstorp");
        Student student2 = new Student(2, "Berit Beritsson", "berit@gmail.com", "Beritsvägen 2 Beritstorp");
        students.add(student1);
        students.add(student2);
        boolean foundStudent1 = false, foundStudent2 = false;

        //Act
        List<StudentView> result = testObject.studentsToStudentViews(students);
        for (StudentView searchFor : result) {
            if (searchFor.getName().contains("Arne Arnesson")) { foundStudent1 = true; }
            if (searchFor.getName().contains("Berit Beritsson")) { foundStudent2 = true; }
        }

        //Assert
        assertTrue(foundStudent1);
        assertTrue(foundStudent2);
    }
}