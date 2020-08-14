package se.lexicon.course_manager_assignment.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CourseCollectionRepository.class})
public class CourseCollectionRepositoryTest {

    @Autowired
    private CourseDao testObject;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
    }


    @Test
    public void createCourse() {
        //Arrange
        int oldSize = testObject.findAll().size();

        //Act
        Course result = testObject.createCourse("Test Course", LocalDate.of(2022, 10, 20), 12);

        //Assert
        assertEquals(oldSize + 1, testObject.findAll().size());
        assertTrue(testObject.findAll().contains(result));
    }

    @Test
    public void findById_EntryFound() {
        //Arrange
        Course course = testObject.createCourse("Test Course", LocalDate.of(2022,10,20), 12); // ID 1

        //Act
        Course result = testObject.findById(1);

        //Assert
        assertSame(course, result);
    }

    @Test
    public void findById_EntryNotFound() {
        //Act
        Course result = testObject.findById(Integer.MAX_VALUE);

        //Assert
        assertNull(result);
    }

    @Test
    public void findByNameContains_EntriesFound() {
        //Arrange
        Course java1 = testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2022,10,20), 2);
        Course java2 = testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2022,10,20), 50);
        testObject.createCourse("C# for dummies", LocalDate.of(2022,10,20), 12);
        int matchingCourses = 0;

        //Act
        Collection<Course> result = testObject.findByNameContains("Java"); //Expecting two hits from the list
        for(Course search : result) {
            if(search.equals(java1)) { matchingCourses++; }
            if(search.equals(java2)) { matchingCourses++; }
        }

        //Assert
        assertEquals(matchingCourses, result.size());
    }

    @Test
    public void findByNameContains_NoEntryFound() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2022,10,20), 2); // ID 1

        //Act
        Collection<Course> result = testObject.findByNameContains("Deleted Course");

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void findByDateBefore() {
        //Arrange
        Course course1 = testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2);
        Course course2 = testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50);
        testObject.createCourse("C# for dummies", LocalDate.of(2040,10,20), 12);
        int matchingDates = 0;

        //Act
        Collection<Course> result = testObject.findByDateBefore(LocalDate.of(2035, 1, 1)); // Expecting to find 2020 and 2030
        for(Course search : result) {
            if(search.equals(course1)) { matchingDates++; }
            if(search.equals(course2)) { matchingDates++; }
        }

        //Assert
        assertEquals(matchingDates, result.size());
    }

    @Test
    public void findByDateAfter() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2);
        Course course1 = testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50);
        Course course2 = testObject.createCourse("C# for dummies", LocalDate.of(2040,10,20), 12);
        int matchingDates = 0;

        //Act
        Collection<Course> result = testObject.findByDateAfter(LocalDate.of(2025, 1, 1)); // Expecting to find 2030 and 2040
        for (Course search : result) {
            if(search.equals(course1)) { matchingDates++; }
            if(search.equals(course2)) { matchingDates++; }
        }

        //Assert
        assertEquals(matchingDates, result.size());
    }

    @Test
    public void findAll() {
        //Arrange
        Course course1 = testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2);
        Course course2 = testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50);
        int matchingCourses = 0;

        //Act
        Collection<Course> result =  new ArrayList<>(testObject.findAll());
        for(Course search : result) {
            if (search.equals(course1)) { matchingCourses++; }
            if (search.equals(course2)) { matchingCourses++; }
        }

        //Assert
        assertEquals(matchingCourses, result.size());
    }

    @Test
    public void findByStudentId() {
        //Arrange
        Student student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        Course course1 = testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2); // Course ID 1
        Course course2 = testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50); // Course ID 2
        testObject.findById(1).enrollStudent(student); // Adds student to course ID 1
        testObject.findById(2).enrollStudent(student); // Adds student to course ID 2
        int matchingCourses = 0;

        //Act
        Collection<Course> result = testObject.findByStudentId(1);
        for(Course search : result) {
            if (search.equals(course1)) { matchingCourses++; }
            if (search.equals(course2)) { matchingCourses++; }
        }

        //Assert
        assertEquals(matchingCourses, result.size());
    }

    @Test
    public void removeCourse_FoundCourse() {
        //Arrange
        Course course1 = testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2); // Course ID 1
        Course course2 = testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50); // Course ID 2
        int oldSize = testObject.findAll().size();

        //Act
        testObject.removeCourse(course1);

        //Assert
        assertFalse(testObject.findAll().contains(course1));
        assertTrue(testObject.findAll().contains(course2));
        assertEquals(oldSize - 1, testObject.findAll().size());
    }

    @Test
    public void removeCourse_NoCourseFound() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2); // Course ID 1
        int oldSize = testObject.findAll().size();

        //Act
        testObject.removeCourse(null);

        //Assert
        assertEquals(oldSize, testObject.findAll().size());
        assertFalse(testObject.findAll().isEmpty());
    }

    @Test
    public void clear() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2);

        //Act
        testObject.clear();

        //Assert
        assertTrue(testObject.findAll().isEmpty());
    }


    @AfterEach
    void tearDown() {
        testObject.clear();
        CourseSequencer.setCourseSequencer(0);
    }
}
