package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Li Ye
 * @Version 1.0
 */

@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        //获取根结点下所有节点
        List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        //将数据封装到List中，只包含根节点直接下属
        List<CourseCategoryTreeDto> list = new ArrayList<>();
        //用于处理节点间的关系
        Map<String, CourseCategoryTreeDto> map = new HashMap<>();

        categoryTreeDtos.stream().forEach(item -> {
            map.put(item.getId(), item);
            if (item.getParentid().equals(id)) {
                list.add(item);
            }

            //处理节点关系
            String parentId = item.getParentid();
            CourseCategoryTreeDto parentNode = map.get(parentId);
            if (parentNode != null) {
                List childrenTreeNodes = parentNode.getChildrenTreeNodes();
                if (childrenTreeNodes == null) {
                    parentNode.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                parentNode.getChildrenTreeNodes().add(item);
            }
        });

        return list;
    }
}
