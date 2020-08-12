package se.lexicon.course_manager_assignment.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Course {
    private int id; // Unique attribute, should be set through constructor with CourseSequencer.class in CourseCollectionRepository.class
    private String courseName; // representing the name of a Course object (like “Java advanced”)
    private LocalDate startDate; // of type LocalDate defines the start date of a course object.
    private int weekDuration; // of type int or Integer is a description of the length of a course
    private Collection<Student> students; // representing all objects of Student.class that is enrolled to this course.
    public int getWeekDuration() { return weekDuration; } // Not part of the assignment?

    public Course() { } // minimum requirement is to pass in id through constructor.

    public Course(int id, String courseName, LocalDate startDate, int weekDuration) {
        this.id = id;
        this.courseName = courseName;
        this.startDate = startDate;
        this.weekDuration = weekDuration;
        this.students = new ArrayList<>(); // Students will be added afterwards
    }

    public void setWeekDuration(int weekDuration) { this.weekDuration = weekDuration; }
    public int getId() { return id; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public Collection<Student> getStudents() { return students; }
    public void setStudents(Collection<Student> students) { this.students = students; }

    // should be used to add a Student.class object to Collection<Student> students. Make sure you avoid adding a
    // duplicate or null into the Collection. Should return true when student was successfully added, otherwise false.
    public boolean enrollStudent(Student student) {
        if (student == null || students.contains(student)) {  return false; }
        else {
            students.add(student);
            return true;
        }
    }

    // should be used to remove a Student.class object from Collection<Student> students. Returns true when the Student
    // object was successfully removed.
    public boolean unenrollStudent(Student student) { // This typo is not my fault!
        if (students.contains(student)) {
            students.remove(student);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) { // needed internally in Collections
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                weekDuration == course.weekDuration &&
                courseName.equals(course.courseName) &&
                startDate.equals(course.startDate) &&
                students.equals(course.students);
    }

    @Override
    public int hashCode() { // needed internally in Collections
        return Objects.hash(id, courseName, startDate, weekDuration, students);
    }

    @Override
    public String toString() { // for debugging purposes
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", startDate=" + startDate +
                ", weekDuration=" + weekDuration +
                ", students=" + students +
                '}';
    }
}