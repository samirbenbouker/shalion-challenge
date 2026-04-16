package com.shalion.challenge.config;

import com.shalion.challenge.domain.School;
import com.shalion.challenge.domain.Student;
import com.shalion.challenge.repository.SchoolRepository;
import com.shalion.challenge.repository.StudentRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitialDataSeeder implements ApplicationRunner {

    private static final Integer SCHOOL_1_MAX_CAPACITY = 100;
    private static final Integer SCHOOL_2_MAX_CAPACITY = 80;
    private static final Integer SCHOOL_3_MAX_CAPACITY = 120;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        School school1 = createSchool("School 1", SCHOOL_1_MAX_CAPACITY);
        School school2 = createSchool("School 2", SCHOOL_2_MAX_CAPACITY);
        School school3 = createSchool("School 3", SCHOOL_3_MAX_CAPACITY);

        int school1TargetCount = SCHOOL_1_MAX_CAPACITY / 2;
        int school2TargetCount = SCHOOL_2_MAX_CAPACITY;
        int school3TargetCount = 1;

        reseedStudents(school1, school1TargetCount);
        reseedStudents(school2, school2TargetCount);
        reseedStudents(school3, school3TargetCount);
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
