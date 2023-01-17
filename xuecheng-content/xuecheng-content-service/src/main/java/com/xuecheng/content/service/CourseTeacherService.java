package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

public interface CourseTeacherService {
    //根据课程id查找课程老师列表
    List<CourseTeacher> queryCourseTeacherList(Long courseId);

    //新增或修改课程老师
    CourseTeacher saveCourseTeacher(CourseTeacher courseTeacher);

    //删除课程老师
    void deleteCourseTeacher(Long courseId, Long teacherId);
}