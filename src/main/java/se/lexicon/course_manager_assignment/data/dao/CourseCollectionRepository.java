package se.lexicon.course_manager_assignment.data.dao;

import se.lexicon.course_manager_assignment.data.sequencers.CourseSequencer;
import se.lexicon.course_manager_assignment.model.Course;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CourseCollectionRepository implements CourseDao{

    private Collection<Course> courses;

    public CourseCollectionRepository(Collection<Course> courses) {
        this.courses = courses;
    }

    @Override
    public Course createCourse(String courseName, LocalDate startDate, int weekDuration) {
        Course course = new Course(CourseSequencer.nextCourseId(), courseName, startDate, weekDuration);
        courses.add(course);
        return course;
    }

    @Override
    public Course findById(int id) {
        for (Course course : courses) {
            if (course.getId() == id) { return course; }
        }
        return null;
    }

    @Override
    public Collection<Course> findByNameContains(String name) {
        Iterator<Course> itr = courses.iterator();
        Collection<Course> matchingName = new HashSet<>();
        while (itr.hasNext()) {
            Course course = itr.next();
            if (course.getCourseName().contains(name)) {
                matchingName.add(course);
            }
        }
        return matchingName;
    }

    @Override
    public Collection<Course> findByDateBefore(LocalDate end) {
        Iterator<Course> itr = courses.iterator();
        Collection<Course> matchingDates = new HashSet<>();
        while (itr.hasNext()) {
            Course course = itr.next();
            if (course.getStartDate().isBefore(end)) {
                matchingDates.add(course);
            }
        }
        return matchingDates;
    }

    @Override
    public Collection<Course> findByDateAfter(LocalDate start) {
            Iterator<Course> itr = courses.iterator();
            Collection<Course> matchingDates = new HashSet<>();
            while (itr.hasNext()) {
                Course course = itr.next();
                if (course.getStartDate().isAfter(start)) {
                    matchingDates.add(course);
                }
            }
            return matchingDates;
        }

    @Override
    public Collection<Course> findAll() {
        return courses;
    }

    @Override
    public Collection<Course> findByStudentId(int studentId) { // Is the purpose to return all the courses a student is part of?

        Iterator<Course> itr = courses.iterator();
        Collection<Course> enrolledCourses = new HashSet<>();
        while (itr.hasNext()) {
            Course course = itr.next();
            if (course.getStudents().contains(studentId)) { // Might be wrong
                enrolledCourses.add(course);
            }
        }
        return enrolledCourses;
    }

    @Override
    public boolean removeCourse(Course course) {
        if (courses.contains(course)) {
            courses.remove(course);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.courses = new HashSet<>();
    }
}