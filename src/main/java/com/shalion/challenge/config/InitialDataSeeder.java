package com.shalion.challenge.config;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.repository.SchoolRepository;
import com.shalion.challenge.repository.StudentRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitialDataSeeder implements ApplicationRunner {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        School school1 = createSchool("School 1", 100);
        School school2 = createSchool("School 2", 80);
        School school3 = createSchool("School 3", 120);

        reseedStudents(school1, school1.getMaxCapacity() / 2);
        reseedStudents(school2, school2.getMaxCapacity());
        reseedStudents(school3, 1);
    }

    private School createSchool(String name, int maxCapacity) {
        School school = new School();
        school.setName(name);
        school.setMaxCapacity(maxCapacity);
        return schoolRepository.save(school);
    }

    private void reseedStudents(School school, int targetCount) {
        List<Student> students = new ArrayList<>(targetCount);
        for (int i = 1; i <= targetCount; i++) {
            Student student = new Student();
            student.setName("pending");
            student.setSchool(school);
            students.add(student);
        }

        List<Student> savedStudents = studentRepository.saveAll(students);
        for (Student student : savedStudents) {
            student.setName("Student " + student.getId());
        }
        studentRepository.saveAll(savedStudents);
    }
}
