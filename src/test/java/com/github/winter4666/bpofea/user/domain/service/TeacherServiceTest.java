package com.github.winter4666.bpofea.user.domain.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void should_add_teacher_successfully() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String jobNumber = String.valueOf(faker.number().randomNumber());

        teacherService.addTeacher(name, jobNumber);

        verify(teacherDao).save(argThat(t -> name.equals(t.getName())
                && jobNumber.equals(t.getJobNumber())));
    }
}