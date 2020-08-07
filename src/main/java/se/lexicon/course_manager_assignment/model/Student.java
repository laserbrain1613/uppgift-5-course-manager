package se.lexicon.course_manager_assignment.model;

import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;

import java.util.Objects;

public class Student {
    private final int id; // Unique attribute, should be set through constructor with StudentSequencer.class
    private String name; // representing the full name of the Student.
    private String email; // unique attribute representing the Student’s email.
    private String address; // String representation of the Student’s address.

    public Student(int id) {
        this.id = StudentSequencer.nextStudentId(); // minimum requirement is to pass in id through constructor
    }

    public Student(int id, String name, String email, String address) {
        this.id = StudentSequencer.nextStudentId();
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public boolean equals(Object o) { // needed internally in Collections
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                name.equals(student.name) &&
                email.equals(student.email) &&
                address.equals(student.address);
    }

    @Override
    public int hashCode() { // needed internally in Collections
        return Objects.hash(id, name, email, address);
    }

    @Override
    public String toString() { // for debugging purpose
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
