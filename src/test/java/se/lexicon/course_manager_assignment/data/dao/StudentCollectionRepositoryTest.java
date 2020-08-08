package se.lexicon.course_manager_assignment.data.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.model.Student;


import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = {StudentCollectionRepository.class})
public class StudentCollectionRepositoryTest {

    private StudentCollectionRepository repository;
    private Student testStudent;

    @Autowired
    private StudentDao testObject;

    @Test
    @DisplayName("Test context successfully setup")
    void context_loads() {
        assertFalse(testObject == null);
    }

    @Test
    public void doesItEvenWork() {

        //testStudent = new Student(1, "Nisse", "test@test.com", "testamåla");
        //System.out.println(testStudent.getId() + " " + testStudent.getName() + " " + testStudent.getEmail() + " " + testStudent.getAddress());

        //repository.createStudent("Nisse", "test@test.com" , "testamåla");


    }







    @AfterEach
    void tearDown() {
        testObject.clear();
        StudentSequencer.setStudentSequencer(0);
    }
}
