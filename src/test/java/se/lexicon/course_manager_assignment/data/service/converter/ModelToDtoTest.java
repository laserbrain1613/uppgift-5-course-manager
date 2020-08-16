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
        assertEquals(student.hashCode(), result.hashCode());
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
        assertEquals(result.getId(), course.getId());
        assertEquals(result.getCourseName(), course.getCourseName());
        assertEquals(result.getStartDate(), course.getStartDate());
        assertEquals(result.getWeekDuration(), course.getWeekDuration());
        assertEquals(result.getStudents(), testObject.studentsToStudentViews(course.getStudents()));
    }

    @Test
    public void coursesToCourseViews() {
        //Arrange
        Collection<Course> courses = new HashSet<>();
        Course course1 = new Course(1, "Java part 1 - Simple stuff", LocalDate.of(2021, 10, 15), 12);
        Course course2 = new Course(2, "Java part 2 - Complex stuff", LocalDate.of(2021, 10, 15), 12);
        courses.add(course1);
        courses.add(course2);
        CourseView course1Converted = testObject.courseToCourseView(course1);
        CourseView course2Converted = testObject.courseToCourseView(course1);

        //Act
        List<CourseView> result = testObject.coursesToCourseViews(courses);

        //Assert
        assertTrue(result.contains(course1Converted));
        assertTrue(result.contains(course2Converted));
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
        StudentView student1Converted = testObject.studentToStudentView(student1);
        StudentView student2Converted = testObject.studentToStudentView(student1);

        //Act
        List<StudentView> result = testObject.studentsToStudentViews(students);

        //Assert
        assertTrue(result.contains(student1Converted));
        assertTrue(result.contains(student2Converted));
    }

}