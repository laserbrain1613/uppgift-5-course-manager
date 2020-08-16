package se.lexicon.course_manager_assignment.data.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.course_manager_assignment.data.dao.CourseDao;
import se.lexicon.course_manager_assignment.data.dao.StudentDao;
import se.lexicon.course_manager_assignment.data.service.converter.Converters;
import se.lexicon.course_manager_assignment.dto.forms.CreateCourseForm;
import se.lexicon.course_manager_assignment.dto.forms.UpdateCourseForm;
import se.lexicon.course_manager_assignment.dto.views.CourseView;
import se.lexicon.course_manager_assignment.model.Course;
import se.lexicon.course_manager_assignment.model.Student;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseManager implements CourseService {

    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final Converters converters;

    @Autowired
    public CourseManager(CourseDao courseDao, StudentDao studentDao, Converters converters) {
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.converters = converters;
    }

    @Override
    public CourseView create(CreateCourseForm form) {
        Course course = courseDao.createCourse(form.getCourseName(), form.getStartDate(), form.getWeekDuration());
        return converters.courseToCourseView(course);
    }

    @Override
    public CourseView update(UpdateCourseForm form) {
        if (form  == null) {
            return null; }
        Course course = courseDao.findById(form.getId());
        course.setCourseName(form.getCourseName());
        course.setStartDate(form.getStartDate());
        course.setWeekDuration(form.getWeekDuration());
        return converters.courseToCourseView(course);
    }

    @Override
    public List<CourseView> searchByCourseName(String courseName) {
        List<CourseView> list = new ArrayList<>();
        for (Course course : courseDao.findAll()) {
            if (course.getCourseName().contains(courseName)) {
                list.add(converters.courseToCourseView(course));
            }
        }
        return list;
    }

    @Override
    public List<CourseView> searchByDateBefore(LocalDate end) {
        List<CourseView> list = new ArrayList<>();
        for (Course course : courseDao.findAll()) {
            if(course.getStartDate().isBefore(end)) {
                list.add(converters.courseToCourseView(course));
            }
        }
        return list;
    }

    @Override
    public List<CourseView> searchByDateAfter(LocalDate start) {
        List<CourseView> list = new ArrayList<>();
        for (Course course : courseDao.findAll()) {
            if(course.getStartDate().isAfter(start)) {
                list.add(converters.courseToCourseView(course));
            }
        }
        return list;
    }

    @Override
    public boolean addStudentToCourse(int courseId, int studentId) {
        Student student = studentDao.findById(studentId);
        Course course = courseDao.findById(courseId);
        if (course == null || student == null) { return false; }
        return course.enrollStudent(student);
    }

    @Override
    public boolean removeStudentFromCourse(int courseId, int studentId) {
        Student student = studentDao.findById(studentId);
        Course course = courseDao.findById(courseId);
        if (course == null || student == null) { return false; }
        return course.unenrollStudent(student);
    }

    @Override
    public CourseView findById(int id) {
        for (Course course : courseDao.findAll()) {
            if (course.getId() == id) { return (converters.courseToCourseView(course)); }
        }
        return null;
    }

    @Override
    public List<CourseView> findAll() {
        return converters.coursesToCourseViews(courseDao.findAll());
    }

    @Override
    public List<CourseView> findByStudentId(int studentId) {
        List<CourseView> list = new ArrayList<>();
        Student student = studentDao.findById(studentId);
        for (Course course : courseDao.findAll()) {
            if (course.getStudents().contains(student)) {
                list.add(converters.courseToCourseView(course));
            }
        }
        return list;
    }

    @Override
    public boolean deleteCourse(int id) {
        Course course = courseDao.findById(id);
        if (course == null) { return false; }
        courseDao.removeCourse(course);
        return true;
    }
}