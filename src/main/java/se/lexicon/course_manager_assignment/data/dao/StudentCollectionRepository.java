package se.lexicon.course_manager_assignment.data.dao;

import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.model.Student;

import java.util.*;


public class StudentCollectionRepository implements StudentDao {

    private Collection<Student> students;

    public StudentCollectionRepository(Collection<Student> students) {
        this.students = students;
    }

    @Override
    public Student createStudent(String name, String email, String address) {
        if (findByEmailIgnoreCase(email) == null) { // Mail address not found, therefore creating student (note, not sure that this if case is needed)
            Student student = new Student(StudentSequencer.nextStudentId(), name, email, address);
            students.add(student);
            return student;
        }
        return null; // Mail address found, refusing to register new student
    }

    @Override
    public Student findByEmailIgnoreCase(String email) {
        for (Student student : students) {
            if (student.getEmail().equalsIgnoreCase(email)) {
                return student;
            }
        }
        return null;
    }

    @Override
    public Collection<Student> findByNameContains(String name) {
        return null;
    }

    @Override
    public Student findById(int id) {
        return null;
    }

    @Override
    public Collection<Student> findAll() { //StudentManager wants a List
        ArrayList<Student> listArray = new ArrayList<>(students);
        return listArray;
    }

    @Override
    public boolean removeStudent(Student student) {
        return false;
    }

    @Override
    public void clear() {
        this.students = new HashSet<>(); // O RLY?
    }
}
