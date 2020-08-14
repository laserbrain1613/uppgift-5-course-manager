package se.lexicon.course_manager_assignment.data.service.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.dao.CourseCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentCollectionRepository;
import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.data.service.converter.ModelToDto;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CourseManager.class, CourseCollectionRepository.class, ModelToDto.class, StudentCollectionRepository.class})
public class CourseManagerTest {

    @Autowired
    private CourseService testObject;

    @Autowired
    private CourseDao courseDao;

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

    }

    @Test
    public void searchByDateBefore() {

    }

    @Test
    public void searchByDateAfter() {

    }

    @Test
    public void addStudentToCourse() {

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

    }

    @Test
    public void findById_Null() {

    }

    @Test
    public void findAll() {

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
