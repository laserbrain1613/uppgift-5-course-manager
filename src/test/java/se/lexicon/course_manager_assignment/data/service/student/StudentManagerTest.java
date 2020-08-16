package se.lexicon.course_manager_assignment.data.service.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.dao.CourseCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.data.service.converter.ModelToDto;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {StudentManager.class, CourseCollectionRepository.class, StudentCollectionRepository.class, ModelToDto.class})
public class StudentManagerTest {

    @Autowired
    private StudentService testObject;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private CourseDao courseDao;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
        assertNotNull(studentDao);
    }


    @Test
    public void create() {
        //Arrange
        CreateStudentForm form = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla"); // Note, can't assign ID manually (not implemented)
        int oldSize = testObject.findAll().size();

        //Act
        StudentView student = testObject.create(form);

        //Assert

        assertEquals(oldSize+1, testObject.findAll().size());
        assertEquals(1, student.getId());
        assertEquals("nisse", student.getName());
        assertEquals("nisse@hej.com", student.getEmail());
        assertEquals("nissemåla", student.getAddress());
    }

    @Test
    public void update() {
        //Arrange
        CreateStudentForm form = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla");
        UpdateStudentForm updateForm = new UpdateStudentForm(1, "berit", "berit@hej.com", "beritsmåla");
        StudentView oldStudent = testObject.create(form);

        //Act
        StudentView updateStudent = testObject.update(updateForm);

        //Assert
        assertNotEquals(oldStudent, updateStudent);
        assertEquals(1, testObject.findAll().size());
        assertEquals(1, updateStudent.getId());
        assertEquals("berit", updateStudent.getName());
        assertEquals("berit@hej.com", updateStudent.getEmail());
        assertEquals("beritsmåla", updateStudent.getAddress());
    }

    @Test
    public void update_Null() {
        //Act
        testObject.update(null);

        //Assert
        assertTrue(testObject.findAll().isEmpty());
    }

    @Test
    public void findById() {
        //Arrange
        CreateStudentForm form1 = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla"); // ID 1
        CreateStudentForm form2 = new CreateStudentForm(null, "berit", "berit@hej.com", "beritsmåla"); // ID 2
        testObject.create(form1);
        StudentView student = testObject.create(form2);

        //Act
        StudentView result = testObject.findById(2);

        //Assert
        assertEquals(student, result);
    }

    @Test
    public void findById_Null() {
        //Act
        StudentView result = testObject.findById(Integer.MAX_VALUE);

        //Assert
        assertNull(result);
    }

    @Test
    public void searchByEmail() {
        CreateStudentForm form1 = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla"); // ID 1
        CreateStudentForm form2 = new CreateStudentForm(null, "berit", "berit@hej.com", "beritsmåla"); // ID 2
        testObject.create(form1);
        StudentView student = testObject.create(form2);

        //Act
        StudentView result = testObject.searchByEmail("berit@hej.com");

        //Assert
        assertEquals(student, result);
    }

    @Test
    public void searchByEmail_Null() {
        //Act
        StudentView result = testObject.searchByEmail("email.doesnt@exist.com");

        //Assert
        assertNull(result);
    }

    @Test
    public void searchByName() {
        //Arrange
        CreateStudentForm form1 = new CreateStudentForm(null, "berit beritsson", "berit@hej.com", "beritsmåla");
        CreateStudentForm form2 = new CreateStudentForm(null, "berit bodilsson", "berit2@hej.com", "bodilsmåla");
        StudentView student1 = testObject.create(form1);
        StudentView student2 = testObject.create(form2);

        //Act
        List<StudentView> result = testObject.searchByName("berit");

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(student1));
        assertTrue(result.contains(student2));
    }

    @Test
    public void findAll() {
        //Arrange
        CreateStudentForm form1 = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla");
        testObject.create(form1);
        testObject.create(form1);
        testObject.create(form1);

        //Act
        List<StudentView> result = testObject.findAll();

        //Assert
        assertEquals(3, result.size());
    }

    @Test
    public void deleteStudent() {
        //Arrange
        CreateStudentForm form1 = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla"); // ID 1
        CreateStudentForm form2 = new CreateStudentForm(null, "berit", "berit@hej.com", "beritsmåla"); // ID 2
        StudentView deletedCourse = testObject.create(form1);
        testObject.create(form2);
        int oldSize = testObject.findAll().size();

        //Act
        boolean result = testObject.deleteStudent(1);

        //Assert
        assertEquals(oldSize-1, testObject.findAll().size());
        assertTrue(result);
        assertFalse(testObject.findAll().contains(deletedCourse));

    }

    @Test
    public void deleteStudent_Null() {
        //Arrange
        CreateStudentForm form1 = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla");
        testObject.create(form1);
        int oldSize = testObject.findAll().size();

        //Act
        testObject.deleteStudent(Integer.MAX_VALUE);

        //Assert
        assertEquals(oldSize, testObject.findAll().size());
    }

    @Test
    public void deleteStudent_EnrolledInCourse() {
        //Arrange
        Course course = courseDao.createCourse("Test", LocalDate.of(2011,11,11), 12);
        Student student = studentDao.createStudent("nisse", "nisse@hotmail.com", "nisseboda"); // Is assigned ID 1
        course.enrollStudent(student);
        int oldSize = testObject.findAll().size();

        //Act
        boolean result = testObject.deleteStudent(1);

        //Assert
        assertTrue(result);
        assertEquals(oldSize-1, testObject.findAll().size());
    }


    @AfterEach
    void tearDown() {
        StudentSequencer.setStudentSequencer(0);
        studentDao.clear();
    }
}
