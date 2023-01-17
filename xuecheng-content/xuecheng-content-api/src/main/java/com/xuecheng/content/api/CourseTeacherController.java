package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "课程老师相关接口", tags = "课程老师相关接口")
@RestController
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;

    @ApiOperation("课程老师查询接口")
    @GetMapping("/courseTeacher/list/{courseId}")
    public List<CourseTeacher> list(@PathVariable Long courseId) {
        return courseTeacherService.queryCourseTeacherList(courseId);
    }

    @ApiOperation("新增或修改课程老师接口")
    @PostMapping("/courseTeacher")
    public CourseTeacher saveCourseTeacher(@RequestBody CourseTeacher CourseTeacher) {
        return courseTeacherService.saveCourseTeacher(CourseTeacher);
    }

    @ApiOperation("删除课程老师接口")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void deleteCourseTeacher(@PathVariable Long courseId, @PathVariable Long teacherId) {
        courseTeacherService.deleteCourseTeacher(courseId, teacherId);
    }
}
