package com.github.winter4666.bpofea.course.domain.service;

import com.github.winter4666.bpofea.common.domain.exception.DataNotFoundException;
import com.github.winter4666.bpofea.common.domain.model.Page;
import com.github.winter4666.bpofea.common.domain.model.PageOptions;
import com.github.winter4666.bpofea.course.domain.model.ClassTime;
import com.github.winter4666.bpofea.course.domain.model.Course;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseDao courseDao;

    public Page<Course> getCourses(String namePrefix, PageOptions pageOptions) {
        return courseDao.findCoursesNotRelatedToStudent(null, namePrefix, Course.State.PUBLISHED, pageOptions);
    }

    public Course findCourseByIdAndThrowExceptionIfNotFound(long courseId) {
        return courseDao.findById(courseId).orElseThrow(() -> new DataNotFoundException("Course cannot be found by course id {}", courseId));
    }

    @Transactional
    public Course updateCourse(long courseId, LocalDate startDate, LocalDate stopDate, List<ClassTime> classTimes) {
        Course course = findCourseByIdAndThrowExceptionIfNotFound(courseId);
        course.setStartDateIfNotNull(startDate);
        course.setStopDateIfNotNull(stopDate);
        course.setClassTimesIfNotNull(classTimes);
        return course;
    }

}
