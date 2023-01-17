package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

public interface CourseBaseInfoService {
    //课程查询
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    //新增课程
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    //根据课程id查询课程的基本和营销信息
    CourseBaseInfoDto getCourseBaseInfo(Long courseId);

    //修改课程
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);

    //删除课程
    void deleteCourseById(Long courseId);
}
