package com.github.winter4666.bpofea.user.domain.service;

import com.github.winter4666.bpofea.common.domain.exception.DataInvalidException;
import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import com.github.winter4666.bpofea.course.domain.service.CourseDao;
import com.github.winter4666.bpofea.user.domain.model.Gender;
import com.github.winter4666.bpofea.user.domain.model.Teacher;
import com.github.winter4666.bpofea.user.domain.model.TeacherMoreInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherDao teacherDao;

    private final CourseDao courseDao;

    private final TeacherInfoService teacherInfoService;

    public Teacher addTeacher(String name, String jobNumber) {
        Teacher teacher = Teacher.builder().name(name).jobNumber(jobNumber).build();;
        return teacherDao.save(teacher);
    }

    public TeacherInfo getTeacherInfo(long id) {
        Teacher teacher = findTeacherByIdAndThrowExceptionIfNotFound(id);
        Gender gender = teacherInfoService.getTeacherInfo(teacher.getJobNumber()).map(TeacherMoreInfo::gender).orElse(null);
        return new TeacherInfo(id, teacher.getName(), teacher.getJobNumber(), gender);
    }

    public record TeacherInfo(long id, String name, String jobNumber, Gender gender) {

    }

    @Transactional
    public Course createCourse(long teacherId, CreateCourseRequest createCourseRequest) {
        Teacher teacher = findTeacherByIdAndThrowExceptionIfNotFound(teacherId);
        Course course = createCourseRequest.toCourseBuilder().teacher(teacher).build();
        teacher.createCourse(course);
        return course;
    }

    public record CreateCourseRequest(String name, LocalDate startDate, LocalDate stopDate, List<ClassTime> classTimes, Long capacity) {

        public Course.CourseBuilder toCourseBuilder() {
            return Course.builder().name(name).startDate(startDate).stopDate(stopDate).classTimes(classTimes).capacity(capacity);
        }

    }

    @Transactional
    public void removeCourse(long teacherId, long courseId) {
        Course course = courseDao.getById(courseId);
        if(!course.belongToTeacher(teacherId)) {
            throw new DataInvalidException("Course {} cannot be removed by teacher {}", courseId, teacherId);
        }
        courseDao.delete(course);
    }

    private Teacher findTeacherByIdAndThrowExceptionIfNotFound(long teacherId) {
        return teacherDao.findById(teacherId).orElseThrow(() -> new DataNotFoundException("Teacher cannot be found by teacher id {}", teacherId));
    }
}
