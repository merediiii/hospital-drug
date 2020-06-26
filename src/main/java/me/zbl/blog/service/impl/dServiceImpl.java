package me.zbl.blog.service.impl;

import me.zbl.blog.dao.dDao;
import me.zbl.blog.domain.dDO;
import me.zbl.blog.service.dService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class dServiceImpl implements dService {

    @Autowired
    private dDao dMapper;


    @Override
    public dDO get(Long id) {
        return dMapper.get(id);
    }

    @Override
    public List<dDO> list(int id) {
        return dMapper.list(id);
    }

    @Override
    public int count(Map<String, Object> map) {
        return dMapper.count(map);
    }
}
