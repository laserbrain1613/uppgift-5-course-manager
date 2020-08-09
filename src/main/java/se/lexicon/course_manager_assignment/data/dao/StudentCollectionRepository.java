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
        if (findByEmailIgnoreCase(email) == null) { // Mail address not found, therefore creating student (note, not sure if this check is needed)
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
        Iterator<Student> itr = students.iterator();
        Collection<Student> foundName = new HashSet<>();
        while (itr.hasNext()) { // IntelliJ suggests enhanced for loop without iterator, must test if it works
            Student student = itr.next();
            if (student.getName().equalsIgnoreCase(name)) {
                foundName.add(student);
                return foundName;
            }
        }
        return null;
    }

    @Override
    public Student findById(int id) {
        Iterator<Student> itr = students.iterator();
        while ( itr.hasNext() ) {
            Student student = itr.next();
            if (student.getId() == id) { return student; }
        }
        return null;
    }

    @Override
    public Collection<Student> findAll() {
        return students;
    }

    @Override
    public boolean removeStudent(Student student) {
        if (students.contains(student)) {
            students.remove(student);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.students = new HashSet<>(); // O RLY? (Note, isn't students a generic?)
    }
}