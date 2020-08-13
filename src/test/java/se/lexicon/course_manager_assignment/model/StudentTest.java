package se.lexicon.course_manager_assignment.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest  {

    private Student student;

    @Test
    public void constructor_IsWorking() {
        //Arrange
        student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");

        //Assert
        assertEquals(1, student.getId());
        assertEquals("Nisse Nilsson", student.getName());
        assertEquals("nisse.nilsson@hotmail.com", student.getEmail());
        assertEquals("Nilsvägen 5 Nilstorp" , student.getAddress());
    }

    @Test
    public void emptyConstructor_IsWorking() {
        //Arrange
        student = new Student();

        //Assert
        assertEquals(0, student.getId());
        assertNull(student.getName());
        assertNull(student.getEmail());
        assertNull(student.getAddress());
    }

    @Test
    public void setters_IsWorking() {
        //Arrange
        student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        student.setName("NewName");
        student.setEmail("NewEmail");
        student.setAddress("NewAddress");

        //Assert
        assertEquals("NewName", student.getName());
        assertEquals("NewEmail", student.getEmail());
        assertEquals("NewAddress", student.getAddress());
    }

    @Test
    public void equals_And_HashCode_IsWorking() {
        //Arrange
        student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");
        Student duplicate = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");

        //Assert
        assertEquals(duplicate, student);
        assertEquals(duplicate.hashCode(), student.hashCode());
    }

    @Test
    public void testToString() {
        //Arrange
        student = new Student(1, "Nisse Nilsson", "nisse.nilsson@hotmail.com", "Nilsvägen 5 Nilstorp");

        //Act
        String result = student.toString();

        //Assert
        assertTrue(result.contains(Integer.toString (student.getId()) ));
        assertTrue(result.contains (student.getName()) ) ;
        assertTrue(result.contains (student.getEmail()) );
        assertTrue(result.contains (student.getAddress()) );
    }
}