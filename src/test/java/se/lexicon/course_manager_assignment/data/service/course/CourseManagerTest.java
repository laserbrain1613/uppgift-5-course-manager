package se.lexicon.course_manager_assignment.data.service.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.dao.CourseCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.data.service.converter.ModelToDto;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.dto.views.StudentView;


import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CourseManager.class, CourseCollectionRepository.class, ModelToDto.class, StudentCollectionRepository.class})
public class CourseManagerTest  {

    @Autowired
    private CourseService testObject;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private StudentDao studentDao; // Note, needed to add this to be able to create students

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
        assertNotNull(courseDao);
    }


    @Test
    public void create() {
        //Arrange
        CreateCourseForm form = new CreateCourseForm(null, "Test Course", LocalDate.of(2011, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        int oldSize = testObject.findAll().size();

        //Act
        CourseView result = testObject.create(form);

        //Assert
        assertEquals(oldSize+1, testObject.findAll().size());
        assertEquals(1, result.getId());
        assertEquals("Test Course", result.getCourseName());
        assertEquals(LocalDate.of(2011,12,13), result.getStartDate());
        assertEquals(12, result.getWeekDuration());
    }

    @Test
    public void update() {
        //Arrange
        CreateCourseForm form = new CreateCourseForm(1, "Test Course", LocalDate.of(2011, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        UpdateCourseForm updateForm = new UpdateCourseForm(1, "New Course", LocalDate.of(2015, 5, 2), 18);
        testObject.create(form);
        int oldSize = testObject.findAll().size();

        //Act
        CourseView result = testObject.update(updateForm);

        //Assert
        assertEquals(oldSize, testObject.findAll().size());
        assertEquals(1, result.getId());
        assertEquals("New Course", result.getCourseName());
        assertEquals(LocalDate.of(2015,5,2), result.getStartDate());
        assertEquals(18, result.getWeekDuration());
    }

    @Test
    public void update_Null() {
        //Act
        testObject.update(null);

        //Assert
        assertTrue(testObject.findAll().isEmpty());
    }

    @Test
    public void searchByCourseName() {
        //Arrange
        CreateCourseForm form1 = new CreateCourseForm(1, "Java Part 1", LocalDate.of(2011, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        CreateCourseForm form2 = new CreateCourseForm(2, "Java Part 2", LocalDate.of(2011, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        CourseView course1 = testObject.create(form1);
        CourseView course2 = testObject.create(form2);

        //Act
        List<CourseView> result = testObject.searchByCourseName("Java");

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(course1));
        assertTrue(result.contains(course2));
    }

    @Test
    public void searchByDateBefore() {
        CreateCourseForm form1 = new CreateCourseForm(1, "Java Part 1", LocalDate.of(2020, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        CreateCourseForm form2 = new CreateCourseForm(2, "Java Part 2", LocalDate.of(2030, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        CourseView course1 = testObject.create(form1);
        CourseView course2 = testObject.create(form2);

        //Act
        List<CourseView> result = testObject.searchByDateBefore(LocalDate.of(2025, 1, 1)); // Expected to find only 2020

        //Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(course1));
    }

    @Test
    public void searchByDateAfter() {
        CreateCourseForm form1 = new CreateCourseForm(1, "Java Part 1", LocalDate.of(2020, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        CreateCourseForm form2 = new CreateCourseForm(2, "Java Part 2", LocalDate.of(2030, 12, 13), 12); // Note, can't assign ID manually (not implemented)
        CourseView course1 = testObject.create(form1);
        CourseView course2 = testObject.create(form2);

        //Act
        List<CourseView> result = testObject.searchByDateAfter(LocalDate.of(2010, 1, 1)); // Expected to find both 2020 and 2030

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(course1));
        assertTrue(result.contains(course2));
    }

    @Test
    public void addStudentToCourse() {
        //Arrange
        CreateCourseForm form = new CreateCourseForm(1, "Java Part 1", LocalDate.of(2020, 12, 13), 12);
        studentDao.createStudent("nisse", "nisse@hotmail.com", "nisseboda"); // Is assigned ID 1
        testObject.create(form);
        int foundMatch = 0;

        //Act
        boolean result = testObject.addStudentToCourse(1, 1);
        for (StudentView search : testObject.findById(1).getStudents()) {
            if(search.getId() == 1) { // Searches for ID 1
                foundMatch++;
            }
        }

       //Assert
        assertTrue(result);
        assertEquals(1, testObject.findAll().size());
        assertEquals(1, foundMatch);
    }

    @Test
    public void addStudentToCourse_CourseNull() {

    }

    @Test
    public void addStudentToCourse_StudentNull() {

    }

    @Test
    public void removeStudentFromCourse() {

    }

    @Test
    public void removeStudentFromCourse_CourseNull() {

    }

    @Test
    public void removeStudentFromCourse_StudentNull() {

    }

    @Test
    public void findById() {
        //Arrange
        CreateCourseForm form = new CreateCourseForm(1, "Java Part 1", LocalDate.of(2020, 12, 13), 12);
        CourseView course = testObject.create(form);

        //Act
        CourseView result = testObject.findById(1);

        //Assert
        assertEquals(course, result);
    }

    @Test
    public void findById_Null() {
        //Act
        CourseView result = testObject.findById(Integer.MAX_VALUE);

        //Assert
        assertNull(result);
    }

    @Test
    public void findAll() {

    }

    @Test
    public void findByStudentId() {

    }

    @Test
    public void deleteCourse() {

    }

    @Test
    public void deleteCourse_Null() {

    }


    @AfterEach
    void tearDown() {
        courseDao.clear();
        CourseSequencer.setCourseSequencer(0);
    }
}
