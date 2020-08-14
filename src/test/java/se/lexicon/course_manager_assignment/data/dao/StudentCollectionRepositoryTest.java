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
    public void createStudent() {
        //Arrange
        int oldSize = testObject.findAll().size();

        //Act
        Student result = testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Assert
        assertEquals(oldSize+1, testObject.findAll().size());
        assertEquals("Nisse", result.getName());
        assertEquals("nisse@hotmail.com", result.getEmail());
        assertEquals("Nilsvägen 5 Nilsmåla", result.getAddress());
    }

    @Test
    public void findByEmailIgnoreCase_FoundEntry() {
        //Arrange
        testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Act
        Student result = testObject.findByEmailIgnoreCase("nisse@hotmail.com");

        //Assert
        assertEquals("Nisse", result.getName());
        assertEquals("nisse@hotmail.com", result.getEmail());
        assertEquals("Nilsvägen 5 Nilsmåla", result.getAddress());
    }

    @Test
    public void findByEmailIgnoreCase_FoundEntryMixedCase() {
        //Arrange
        testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Act
        Student result = testObject.findByEmailIgnoreCase("nisse@HOTmail.Com");

        //Assert
        assertEquals("Nisse", result.getName());
        assertEquals("nisse@hotmail.com", result.getEmail());
        assertEquals("Nilsvägen 5 Nilsmåla", result.getAddress());
    }

    @Test
    public void findByEmail_NoEntryFound() {
        //Arrange
        testObject.createStudent("Nisse", "nisse@hotmail.com", "Nilsvägen 5 Nilsmåla");

        //Act
        Student result = testObject.findByEmailIgnoreCase("rolf@gmail.com");

        //Assert
        assertNull(result);
    }

    @Test
    public void findByNameContains_EntryFound() {
        Student nisse1 = testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla");
        Student nisse2 = testObject.createStudent("Nisse Bengtsson", "nisse2@hotmail.com", "Nilsvägen 6 Nilsmåla");
        testObject.createStudent("Berit Beritsson", "berit@hotmail.com", "Beritsvägen 2 Beritsmåla");
        int namesMatch = 0;

        //Act
        Collection<Student> result = testObject.findByNameContains("Nisse"); // Expecting two hits from the list
        for (Student search : result) {
            if(search.equals(nisse1)) { namesMatch++; }
            if(search.equals(nisse2)) { namesMatch++; }
        }

        //Assert
        assertEquals(namesMatch, result.size());
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
        Student result = testObject.findById(1);

        //Assert
        assertEquals(1, result.getId());
        assertEquals("Nisse Nilsson", result.getName());
        assertEquals("nisse1@hotmail.com", result.getEmail());
        assertEquals("Nilsvägen 5 Nilsmåla", result.getAddress());
    }

    @Test
    public void findById_EntryNotFound() {
        //Act
        Student result = testObject.findById(Integer.MAX_VALUE);

        //Assert
        assertNull(result);
    }

    @Test
    public void findAll() {
        //Arrange
        Student nisse = testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla");
        Student berit = testObject.createStudent("Berit Beritsson", "nisse@hotmail.com", "Beritsvägen 2 Beritsmåla");
        int matchingNames = 0;


        //Act
        Collection<Student> result = new ArrayList<>(testObject.findAll());
        for(Student search : result) {
            if(search.equals(nisse)) { matchingNames++; }
            if(search.equals(berit)) { matchingNames++; }
        }

        //Assert
        assertEquals(matchingNames, result.size());
    }

    @Test
    public void removeStudent_StudentFound() {
        //Arrange
        Student nisse = testObject.createStudent("Nisse Nilsson", "nisse1@hotmail.com", "Nilsvägen 5 Nilsmåla");
        testObject.createStudent("Berit Beritsson", "nisse@hotmail.com", "Beritsvägen 2 Beritsmåla");
        int oldSize = testObject.findAll().size(); // 2

        //Act
        boolean result = testObject.removeStudent(nisse);

        //Assert
        assertEquals(oldSize-1, testObject.findAll().size());
        assertTrue(result);
        assertFalse(testObject.findAll().contains(nisse));
    }





    @AfterEach
    void tearDown() {
        testObject.clear();
        StudentSequencer.setStudentSequencer(0);
    }
}
