package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> queryCourseTeacherList(Long courseId) {
        //批量查找课程老师
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(queryWrapper);
        return courseTeachers;
    }

    @Override
    public CourseTeacher saveCourseTeacher(CourseTeacher courseTeacher) {
        //判断是新增还是更新
        if (courseTeacher.getId() == null) {
            int insert = courseTeacherMapper.insert(courseTeacher);
            //判断是否新增成功
            if (insert <= 0) {
                XueChengException.cast("新增课程老师信息失败");
            }
        } else {
            int update = courseTeacherMapper.updateById(courseTeacher);
            //判断是否新增成功
            if (update <= 0) {
                XueChengException.cast("修改课程老师信息失败");
            }
        }

        //返回结果
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseTeacher.getCourseId());
        queryWrapper.eq(CourseTeacher::getTeacherName, courseTeacher.getTeacherName());
        return courseTeacherMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);
        queryWrapper.eq(teacherId != null, CourseTeacher::getId, teacherId);
        int delete = courseTeacherMapper.delete(queryWrapper);
        //判断是否删除成功
        if (delete <= 0) {
            XueChengException.cast("课程老师信息删除失败");
        }
    }
}