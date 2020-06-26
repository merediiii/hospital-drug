package me.zbl.blog.service;

import me.zbl.blog.domain.ContentDO;
import me.zbl.blog.domain.dDO;

import java.util.List;
import java.util.Map;

public interface dService {

    dDO get(Long id);

    List<dDO> list(int id);

    int count(Map<String, Object> map);
}
