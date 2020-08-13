package se.lexicon.course_manager_assignment.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.model.Student;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {StudentCollectionRepository.class})
public class StudentCollectionRepositoryTest {

    private StudentCollectionRepository repository;
    private Student testStudent;

    @Autowired
    private StudentDao testObject;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertNotNull(testObject);
    }


    @Test
    public void createStudent_IsWorking() {
        //Arrange
        int oldSize = testObject.findAll().size();

        //Act
        testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Assert
        assertEquals(oldSize+1, testObject.findAll().size());
    }

    @Test
    public void findByEmailIgnoreCase_FoundEntry() {
        //Arrange
        testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Act
        Student searchStudent = testObject.findByEmailIgnoreCase("nisse@hotmail.com");

        //Assert
        assertTrue(searchStudent.getEmail().equalsIgnoreCase("nisse@hotmail.com"));
    }

    @Test
    public void findByEmailIgnoreCase_FoundEntryMixedCase() {
        //Arrange
        testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Act
        Student searchStudent = testObject.findByEmailIgnoreCase("nisse@HOTmail.Com");

        //Assert
        assertTrue(searchStudent.getEmail().equalsIgnoreCase("nisse@hotmail.com"));
    }

    @Test
    public void findByEmail_NoEntryFound() {
        //Arrange
        testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Act
        Student searchStudent = testObject.findByEmailIgnoreCase("rolf@gmail.com");

        //Assert
        assertNull(searchStudent);
    }

    @Test
    public void findByNameContains_EntryFound() {
        testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla");
        testObject.createStudent("Nisse Bengtsson", "nisse2@hotmail.com", "Nilsvägen 6 Nilsmåla");
        testObject.createStudent("Berit Beritsson", "berit@hotmail.com", "Beritsvägen 2 Beritsmåla");

        //Act
        Collection<Student> result = testObject.findByNameContains("Nisse"); // Expecting two hits from the list

        //Assert
        assertEquals(2 , result.size());
    }

    @Test
    public void findByNameContains_EntryNotFound() {
        //Arrange
        testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Act
        Collection<Student> result = testObject.findByNameContains("Student_Who_Quit_The_Course");

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void findById_EntryFound() {
        //Arrange
        testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla"); // Assigned ID 1

        //Act
        Student searchStudent = testObject.findById(1);

        //Assert
        assertEquals(1, searchStudent.getId());
        assertEquals("Nisse Nilsson", searchStudent.getName());
        assertEquals("nisse1@hotmail.com", searchStudent.getEmail());
        assertEquals("Nilsvägen 5 Nilsmåla", searchStudent.getAddress());
    }

    @Test
    public void findById_EntryNotFound() {
        //Act
        Student searchStudent = testObject.findById(Integer.MAX_VALUE);

        //Assert
        assertNull(searchStudent);
    }

    @Test
    public void findAll() {
        //Arrange
        testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla");
        testObject.createStudent("Berit Beritsson", "nisse@hotmail.com", "Beritsvägen 2 Beritsmåla");

        //Act
        Collection<Student> result = new ArrayList<>(testObject.findAll());

        //Assert
        assertEquals(2, result.size());
        assertNotNull(result);
    }

    @Test
    public void removeStudent_StudentFound() {
        //Arrange
        testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla");
        testObject.createStudent("Berit Beritsson", "nisse@hotmail.com", "Beritsvägen 2 Beritsmåla"); // ID 2
        Student student = testObject.findById(2); // Berit
        int oldSize = testObject.findAll().size(); // 2

        //Act
        boolean result = testObject.removeStudent(student);

        //Assert
        assertEquals(oldSize-1, testObject.findAll().size());
        assertTrue(result);
        assertFalse(testObject.findAll().contains(student));
    }





    @AfterEach
    void tearDown() {
        testObject.clear();
        StudentSequencer.setStudentSequencer(0);
    }
}
