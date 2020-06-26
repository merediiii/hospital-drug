package me.zbl.blog.controller;

import me.zbl.blog.domain.ContentDO;
import me.zbl.blog.domain.dDO;
import me.zbl.blog.service.dService;
import me.zbl.common.controller.BaseController;
import me.zbl.common.utils.PageWrapper;
import me.zbl.common.utils.Query;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/d")
public class DController extends BaseController {

    @Autowired
    dService ddService;

    @ResponseBody
    @GetMapping("/list")
    public PageWrapper list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        int id = Integer.parseInt(params.get("type").toString());
        List<dDO> dSer = ddService.list(id);
        int total = ddService.count(query);
        PageWrapper pageWrapper = new PageWrapper(dSer, total);
        return pageWrapper;
    }
}
