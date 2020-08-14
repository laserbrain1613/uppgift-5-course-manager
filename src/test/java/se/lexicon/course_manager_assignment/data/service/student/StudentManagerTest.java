package se.lexicon.course_manager_assignment.data.service.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.dao.CourseCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentCollectionRepository;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.data.service.converter.ModelToDto;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {StudentManager.class, CourseCollectionRepository.class, StudentCollectionRepository.class, ModelToDto.class})
public class StudentManagerTest {

    @Autowired
    private StudentService testObject;
    @Autowired
    private StudentDao studentDao;

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
        testObject.create(form);

        //Assert
        assertEquals(oldSize+1, testObject.findAll().size());
        assertEquals(1, testObject.findById(1).getId());
        assertEquals("nisse", testObject.findById(1).getName());
        assertEquals("nisse@hej.com", testObject.findById(1).getEmail());
        assertEquals("nissemåla", testObject.findById(1).getAddress());

    }

    @Test
    public void update() {
        //Arrange
        CreateStudentForm form = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla");
        UpdateStudentForm updateForm = new UpdateStudentForm(1, "berit", "berit@hej.com", "beritsmåla");
        testObject.create(form);

        //Act
        testObject.update(updateForm);

        //Assert
        assertEquals(1, testObject.findAll().size());
        assertEquals(1, testObject.findById(1).getId());
        assertEquals("berit", testObject.findById(1).getName());
        assertEquals("berit@hej.com", testObject.findById(1).getEmail());
        assertEquals("beritsmåla", testObject.findById(1).getAddress());
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
        testObject.create(form2);

        //Act
        StudentView result = testObject.findById(2);

        //Assert
        assertEquals(2, result.getId());
        assertEquals("berit", result.getName());
        assertEquals("berit@hej.com", result.getEmail());
        assertEquals("beritsmåla", result.getAddress());
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
        testObject.create(form2);

        //Act
        StudentView result = testObject.searchByEmail("berit@hej.com");

        //Assert
        assertEquals(2, result.getId());
        assertEquals("berit", result.getName());
        assertEquals("berit@hej.com", result.getEmail());
        assertEquals("beritsmåla", result.getAddress());
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
        CreateStudentForm form1 = new CreateStudentForm(null, "nisse", "nisse@hej.com", "nissemåla");
        CreateStudentForm form2 = new CreateStudentForm(null, "berit", "berit@hej.com", "beritsmåla");
        testObject.create(form1);
        testObject.create(form2);
        testObject.create(form2);
        int numberCount = 0;

        //Act
        List<StudentView> result = testObject.searchByName("berit");
        for (StudentView searchFor : result) {
            if(searchFor.getName().contains("berit")) {
                numberCount++;
            }
        }

        //Assert
        assertEquals(2, result.size());
        assertEquals(2, numberCount);
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
        testObject.create(form1);
        testObject.create(form2);
        int oldSize = testObject.findAll().size();

        //Act
        testObject.deleteStudent(1);

        //Assert
        assertEquals(oldSize-1, testObject.findAll().size());
        assertTrue(testObject.findById(2).getName().contains("berit"));
        assertFalse(testObject.findAll().contains(testObject.findById(1)));

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


    @AfterEach
    void tearDown() {
        StudentSequencer.setStudentSequencer(0);
        studentDao.clear();
    }
}
