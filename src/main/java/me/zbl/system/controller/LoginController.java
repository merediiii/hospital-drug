package me.zbl.system.controller;

import me.zbl.app.controller.ConsumerController;
import me.zbl.common.annotation.Log;
import me.zbl.common.config.Constant;
import me.zbl.common.controller.BaseController;
import me.zbl.common.domain.FileDO;
import me.zbl.common.domain.Tree;
import me.zbl.common.service.FileService;
import me.zbl.common.utils.MD5Utils;
import me.zbl.common.utils.R;
import me.zbl.common.utils.ShiroUtils;
import me.zbl.system.domain.MenuDO;
import me.zbl.system.domain.RoleDO;
import me.zbl.system.domain.UserDO;
import me.zbl.system.service.MenuService;
import me.zbl.system.service.RoleService;
import me.zbl.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class LoginController extends BaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  MenuService menuService;
  @Autowired
  FileService fileService;
  @Autowired
  UserService userService;
  @Autowired
  RoleService roleService;

  @GetMapping({"/", ""})
  String welcome(Model model) {

    return "redirect:/login";
  }

  @Log("请求访问主页")
  @GetMapping({"/index"})
  String index(Model model) {
    List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
    model.addAttribute("menus", menus);
    model.addAttribute("name", getUser().getName());
    FileDO fileDO = fileService.get(getUser().getPicId());
    if (fileDO != null && fileDO.getUrl() != null) {
      if (fileService.isExist(fileDO.getUrl())) {
        model.addAttribute("picUrl", fileDO.getUrl());
      } else {
        model.addAttribute("picUrl", "/img/photo_s.jpg");
      }
    } else {
      model.addAttribute("picUrl", "/img/photo_s.jpg");
    }
    model.addAttribute("username", getUser().getUsername());
    return "index_v1";
  }

  @GetMapping("/login")
  String login() {
    return "login";
  }

  @Log("登录")
  @PostMapping("/login")
  @ResponseBody
  R ajaxLogin(String username, String password) {
    password = MD5Utils.encrypt(username, password);
    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
    Subject subject = SecurityUtils.getSubject();
    try {
      subject.login(token);
      return R.ok();
    } catch (AuthenticationException e) {
      return R.error("用户或密码错误");
    }
  }

  @RequestMapping("/sign")
  String sign(Model model) {
    List<RoleDO> roles = roleService.list();
    model.addAttribute("roles", roles);
    return "sign";
  }

  @Log("注册")
  @PostMapping("/sign")
  @ResponseBody
  R signin(UserDO user) {
    if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
      return R.error(1, "演示系统不允许修改,完整体验请部署程序");
    }
    user.setPassword(MD5Utils.encrypt(user.getUsername(), user.getPassword()));
    String passwd = MD5Utils.encrypt(user.getUsername(), user.getPassword());
//    UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), passwd);
//    Subject subject = SecurityUtils.getSubject();
    if (userService.save(user) > 0) {
//      subject.login(token);
      return R.ok();
    }
    return R.error();
  }

  @Log("退出注册")
  @PostMapping("/exit")
  @ResponseBody
  boolean exit(@RequestParam Map<String, Object> params) {
    // 存在，不通过，false
    return !userService.exit(params);
  }

  @GetMapping("/logout")
  String logout() {
    ShiroUtils.logout();
    return "redirect:/login";
  }

  @GetMapping("/main")
  String main() {
    return "main";
  }

}
