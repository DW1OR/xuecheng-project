package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

public interface TeachplanService {
    //课程计划查询
    List<TeachplanDto> findTeachplayTree(Long courseId);

    //新增或修改课程计划
    void saveTeachplan(SaveTeachplanDto dto);
}
