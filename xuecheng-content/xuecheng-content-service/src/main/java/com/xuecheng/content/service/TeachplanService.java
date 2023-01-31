package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.TeachplanMedia;

import java.util.List;

public interface TeachplanService {
    //课程计划查询
    List<TeachplanDto> findTeachplayTree(Long courseId);

    //新增或修改课程计划
    void saveTeachplan(SaveTeachplanDto dto);

    //删除课程计划
    void deleteTeachplan(Long teachplanId);

    //下移课程计划
    void movedownTeachplan(Long teachplanId);

    //上移课程计划
    void moveupTeachplan(Long teachplanId);

    //教学计划绑定媒资
    TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);
}
