package se.lexicon.course_manager_assignment.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.model.Course;


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
        int oldSize =testObject.findAll().size();

        //Act
        testObject.createCourse("Test Course", LocalDate.of(2022,10,20), 12);

        //Assert
        assertEquals(oldSize+1, testObject.findAll().size());
    }

    @Test
    public void findById_EntryFound() {
        //Arrange
        testObject.createCourse("Test Course", LocalDate.of(2022,10,20), 12); // ID 1

        //Act
        Course searchCourse = testObject.findById(1);

        //Assert
        assertEquals(1, searchCourse.getId());
        assertEquals("Test Course", searchCourse.getCourseName());
        assertEquals(LocalDate.of(2022,10,20), searchCourse.getStartDate());
        assertEquals(12, searchCourse.getWeekDuration());
    }

    @Test
    public void findById_EntryNotFound() {
        //Act
        Course searchCourse = testObject.findById(Integer.MAX_VALUE);

        //Assert
        assertNull(searchCourse);
    }

    @Test
    public void findByNameContains_EntriesFound() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2022,10,20), 2);
        testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2022,10,20), 50);
        testObject.createCourse("C# for dummies", LocalDate.of(2022,10,20), 12);

        //Act
        Collection<Course> searchCourse = testObject.findByNameContains("Java"); //Expecting two hits from the list

        //Assert
        assertEquals(2, searchCourse.size());
    }

    @Test
    public void findByNameContains_NoEntryFound() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2022,10,20), 2); // ID 1

        //Act
        Collection<Course> searchCourse = testObject.findByNameContains("Deleted Course");

        //Assert
        assertTrue(searchCourse.isEmpty());
    }

    @Test
    public void findByDateBefore() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2);
        testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50);
        testObject.createCourse("C# for dummies", LocalDate.of(2040,10,20), 12);

        //Act
        Collection<Course> searchDate = testObject.findByDateBefore(LocalDate.of(2035, 1, 1)); // Expecting to find 2020 and 2030
        System.out.println(searchDate);

        //Assert
        assertEquals(2, searchDate.size());
        assertTrue(searchDate.contains(testObject.findById(1))); // 2020
        assertTrue(searchDate.contains(testObject.findById(2))); // 2030
    }

    @Test
    public void findByDateAfter() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2);
        testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50);
        testObject.createCourse("C# for dummies", LocalDate.of(2040,10,20), 12);

        //Act
        Collection<Course> searchDate = testObject.findByDateAfter(LocalDate.of(2025, 1, 1)); // Expecting to find 2030 and 2040

        //Assert
        assertEquals(2, searchDate.size());
        assertTrue(searchDate.contains(testObject.findById(2))); // 2030
        assertTrue(searchDate.contains(testObject.findById(3))); // 2040
    }

    @Test
    public void findAll() {
        //Arrange
        testObject.createCourse("Java part 1 - Simple stuff", LocalDate.of(2020,10,20), 2);
        testObject.createCourse("Java part 2 - Complex stuff", LocalDate.of(2030,10,20), 50);

        //Act
        Collection<Course> result =  new ArrayList<>(testObject.findAll());

        //Assert
        assertEquals(2, result.size());
        assertNotNull(result);
        assertTrue(result.contains(testObject.findById(1)));
        assertTrue(result.contains(testObject.findById(1)));
    }

    @Test
    public void findByStudentId() { // Note, needs careful checking

    }

    @Test
    public void removeCourse_FoundCourse() {

    }

    @Test
    public void removeCourse_NoCourseFound() {

    }

    @Test
    public void clear() {

    }





    @AfterEach
    void tearDown() {
        testObject.clear();
        CourseSequencer.setCourseSequencer(0);
    }
}
