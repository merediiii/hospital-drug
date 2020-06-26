package me.zbl.blog.dao;

import me.zbl.blog.domain.dDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface dDao {
    dDO get(Long id);

    List<dDO> list(int id);

    int count(Map<String, Object> map);
}
