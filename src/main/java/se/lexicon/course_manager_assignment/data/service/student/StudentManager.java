package se.lexicon.course_manager_assignment.data.service.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.service.converter.Converters;
import se.lexicon.course_manager_assignment.dto.forms.CreateStudentForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateStudentForm;
import se.lexicon.course_manager_assignment.dto.views.StudentView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;

import java.util.*;

@Service
public class StudentManager implements StudentService {

    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final Converters converters;

    @Autowired
    public StudentManager(StudentDao studentDao, CourseDao courseDao, Converters converters) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.converters = converters;
    }

    @Override
    public StudentView create(CreateStudentForm form) {
        Student student = studentDao.createStudent(form.getName(), form.getEmail(), form.getAddress());
        return converters.studentToStudentView(student);
    }

    @Override
    public StudentView update(UpdateStudentForm form) {
        if (form == null) {
            return null;
        }
        Student student = studentDao.findById(form.getId());
        student.setName(form.getName());
        student.setAddress(form.getAddress());
        student.setEmail(form.getEmail());
        return converters.studentToStudentView(student);
    }

    @Override
    public StudentView findById(int id) {
        Student student = studentDao.findById(id);
        return (student == null) ? null : converters.studentToStudentView(student);
    }

    @Override
    public StudentView searchByEmail(String email) {
        Student student = studentDao.findByEmailIgnoreCase(email);
        return (student == null) ? null : converters.studentToStudentView(student);
    }

    @Override
    public List<StudentView> searchByName(String name) {
        List<StudentView> result = new ArrayList<>();
        for (Student student : studentDao.findAll()) {
            if (student.getName().contains(name)) {
                result.add(converters.studentToStudentView(student));
            }
        }
        return result;
    }

    @Override
    public List<StudentView> findAll() {
        return converters.studentsToStudentViews(studentDao.findAll());
    }

    @Override
    public boolean deleteStudent(int id) {
        Student student = studentDao.findById(id);
        if (student == null) {
            return false;
        } else {
            Collection<Course> courses = courseDao.findAll();
            for (Course course : courses) {
                if (course.getStudents().contains(student)) {
                    course.unenrollStudent(student); // Prevents deleted student from being part of a course
                }
            }
            studentDao.removeStudent(student);
        }
        return true;
    }

}