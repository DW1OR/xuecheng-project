package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuecheng.base.exception.XueChengException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplayTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto dto) {
        Long id = dto.getId();
        Teachplan teachplan = teachplanMapper.selectById(id);
        //判断课程计划是否存在
        if (teachplan == null) {
            teachplan = new Teachplan();
            BeanUtils.copyProperties(dto, teachplan);
            //找到同级课程计划的数量
            int count = getTeachplanCount(dto.getCourseId(), dto.getParentid());
            //新课程计划的值
            teachplan.setOrderby(count + 1);
            teachplanMapper.insert(teachplan);
        } else {
            BeanUtils.copyProperties(dto, teachplan);
            //更新
            teachplanMapper.updateById(teachplan);
        }
    }

    @Override
    @Transactional
    public void deleteTeachplan(Long teachplanId) {
        //查询是章还是节
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            XueChengException.cast("不存在该课程计划");
        }

        //删除的课程计划是节
        if (teachplan.getParentid() != 0) {
            this.deletePlanAndMedia(teachplanId);
            return;
        }

        //删除的课程计划是章
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, teachplanId);
        List<Teachplan> list = teachplanMapper.selectList(queryWrapper);
        //判断章下还有没节
        if (!list.isEmpty()) {
            XueChengException.cast("课程计划信息还有子级信息，无法操作");
        }

        /*//遍历删除节
        list.stream().forEach(item -> {
            this.deletePlanAndMedia(item.getId());
        });*/

        //删除章
        teachplanMapper.deleteById(teachplanId);
    }

    @Override
    @Transactional
    public void movedownTeachplan(Long teachplanId) {
        //查找下一个课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        Integer orderby = teachplan.getOrderby() + 1;
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getOrderby, orderby);
        queryWrapper.eq(Teachplan::getParentid, teachplan.getParentid());
        queryWrapper.eq(Teachplan::getCourseId, teachplan.getCourseId());
        Teachplan change = teachplanMapper.selectOne(queryWrapper);
        //判断下一个课程计划是否存在
        if (change == null) {
            XueChengException.cast("此课程计划已无法下移");
        }

        //修改课程计划顺序
        teachplan.setOrderby(orderby);
        change.setOrderby(orderby - 1);
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(change);
    }

    @Override
    public void moveupTeachplan(Long teachplanId) {
        //查找上一个课程计划
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        Integer orderby = teachplan.getOrderby() - 1;
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getOrderby, orderby);
        queryWrapper.eq(Teachplan::getParentid, teachplan.getParentid());
        queryWrapper.eq(Teachplan::getCourseId, teachplan.getCourseId());
        Teachplan change = teachplanMapper.selectOne(queryWrapper);
        //判断上一个课程计划是否存在
        if (change == null) {
            XueChengException.cast("此课程计划已无法上移");
        }

        //修改课程计划顺序
        teachplan.setOrderby(orderby);
        change.setOrderby(orderby + 1);
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(change);
    }

    @Override
    public TeachplanMedia associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        //教学计划id
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);

        //约束校验
        //教学计划不存在无法绑定
        if (teachplan == null) {
            XueChengException.cast("教学计划不存在");
        }
        //只有二级目录才可以绑定视频
        if (teachplan.getGrade() != 2) {
            XueChengException.cast("只有二级目录才可以绑定视频");
        }

        //删除原来的绑定关系
        LambdaQueryWrapper<TeachplanMedia> wrapper = new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId, teachplanId);
        teachplanMediaMapper.delete(wrapper);

        //添加新的绑定关系
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setTeachplanId(teachplanId);
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMedia.setMediaId(bindTeachplanMediaDto.getMediaId());
        teachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(teachplanMedia);

        return teachplanMedia;
    }

    //找到同级课程计划的数量
    public int getTeachplanCount(Long courseId, Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count.intValue();
    }

    //删除节
    public void deletePlanAndMedia(Long id) {
        //删除与课程计划关联的媒资
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getTeachplanId, id);
        TeachplanMedia teachplanMedia = teachplanMediaMapper.selectOne(queryWrapper);
        //查询是否存在媒资与课程计划关联
        if (teachplanMedia != null) {
            //存在关联，进行删除
            int delete = teachplanMediaMapper.delete(queryWrapper);
            //删除失败
            if (delete <= 0) {
                XueChengException.cast("课程计划信息还有视频信息，无法操作");
            }
        }

        //删除课程计划
        teachplanMapper.deleteById(id);
    }
}