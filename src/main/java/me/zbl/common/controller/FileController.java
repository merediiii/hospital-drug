package me.zbl.common.controller;

import me.zbl.common.config.HospitalConfig;
import me.zbl.common.domain.FileDO;
import me.zbl.common.service.FileService;
import me.zbl.common.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author 郑保乐
 * @email 18333298410@163.com
 * @date 2017-09-19 16:02:20
 */
@Controller
@RequestMapping("/common/sysFile")
public class FileController extends BaseController {

  @Autowired
  private FileService sysFileService;

  @Autowired
  private HospitalConfig hospitalConfig;

  @GetMapping()
  @RequiresPermissions("common:sysFile:sysFile")
  String sysFile(Model model) {
    Map<String, Object> params = new HashMap<>(16);
    return "common/file/file";
  }

  @ResponseBody
  @GetMapping("/list")
  @RequiresPermissions("common:sysFile:sysFile")
  public PageWrapper list(@RequestParam Map<String, Object> params) {
    // 查询列表数据
    Query query = new Query(params);
    List<FileDO> sysFileList = sysFileService.list(query);
    int total = sysFileService.count(query);
    PageWrapper pageWrapper = new PageWrapper(sysFileList, total);
    return pageWrapper;
  }

  @GetMapping("/add")
    // @RequiresPermissions("common:bComments")
  String add() {
    return "common/sysFile/add";
  }

  @GetMapping("/edit")
    // @RequiresPermissions("common:bComments")
  String edit(Long id, Model model) {
    FileDO sysFile = sysFileService.get(id);
    model.addAttribute("sysFile", sysFile);
    return "common/sysFile/edit";
  }

  @GetMapping("/judge/{id}")
  String judge(Model model, @PathVariable("id") Long id) {
    FileDO sysFile = sysFileService.get(id);
    String url = sysFile.getUrl();
    String[] res = url.split("/files/");
//    System.out.println("!@#!$!#$#!$#!$!@#\n"+sysFile.getUrl());
    String des = "D:\\var\\uploaded_files\\" + res[1];
    String type = judge(des);
    System.out.println("@#$%#%#^$&^$&$&^$");
    System.out.println(des + " " + type);
    if(type.equals("yaopian"))
      type = "1";
    if(type.equals("yaoshui"))
      type = "2";
    System.out.println(des + " " + type);
    model.addAttribute("type", Integer.valueOf(type));
    return "common/file/judge";
  }


  /**
   * 信息
   */
  @RequestMapping("/info/{id}")
  @RequiresPermissions("common:info")
  public R info(@PathVariable("id") Long id) {
    FileDO sysFile = sysFileService.get(id);
    return R.ok().put("sysFile", sysFile);
  }

  /**
   * 保存
   */
  @ResponseBody
  @PostMapping("/save")
  @RequiresPermissions("common:save")
  public R save(FileDO sysFile) {
    if (sysFileService.save(sysFile) > 0) {
      return R.ok();
    }
    return R.error();
  }

  /**
   * 修改
   */
  @RequestMapping("/update")
  @RequiresPermissions("common:update")
  public R update(@RequestBody FileDO sysFile) {
    sysFileService.update(sysFile);

    return R.ok();
  }

  /**
   * 删除
   */
  @PostMapping("/remove")
  @ResponseBody
  // @RequiresPermissions("common:remove")
  public R remove(Long id, HttpServletRequest request) {
    if ("test".equals(getUsername())) {
      return R.error(1, "演示系统不允许修改,完整体验请部署程序");
    }
    String fileName = hospitalConfig.getUploadPath() + sysFileService.get(id).getUrl().replace("/files/", "");
    if (sysFileService.remove(id) > 0) {
      boolean b = FileUtil.deleteFile(fileName);
      if (!b) {
        return R.error("数据库记录删除成功，文件删除失败");
      }
      return R.ok();
    } else {
      return R.error();
    }
  }

  /**
   * 删除
   */
  @PostMapping("/batchRemove")
  @ResponseBody
  @RequiresPermissions("common:remove")
  public R remove(@RequestParam("ids[]") Long[] ids) {
    if ("test".equals(getUsername())) {
      return R.error(1, "演示系统不允许修改,完整体验请部署程序");
    }
    sysFileService.batchRemove(ids);
    return R.ok();
  }

  @ResponseBody
  @PostMapping("/upload")
  R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    if ("test".equals(getUsername())) {
      return R.error(1, "演示系统不允许修改,完整体验请部署程序");
    }
    String fileName = file.getOriginalFilename();
    fileName = FileUtil.renameToUUID(fileName);
    FileDO sysFile = new FileDO(FileType.fileType(fileName), "/files/" + fileName, new Date());
    try {
      FileUtil.uploadFile(file.getBytes(), hospitalConfig.getUploadPath(), fileName);
    } catch (Exception e) {
      return R.error();
    }

    if (sysFileService.save(sysFile) > 0) {
      return R.ok().put("fileName", sysFile.getUrl());
    }
    return R.error();
  }

  public static String judge(String url){
    String py = "D:\\workspace\\python\\HIS\\predict.py";
    String img = url;
    Process proc;
    HashMap<String, Double> map = new HashMap<String, Double>();
    try {
      proc = Runtime.getRuntime().exec("python D:\\workspace\\python\\HIS\\predict.py " + img);// 执行py文件
      //用输入输出流来截取结果
      BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      String line = null;
      int count = 0;
      while ((line = in.readLine()) != null) {
        if (line.equals(""))
          continue;
        String[] res = line.split(" ");
//                System.out.println(line);
        map.put(res[0], Double.valueOf(res[1].substring(res[1].indexOf("=") + 1, res[1].length() - 1)));

      }
      in.close();
      proc.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Double max = Double.MIN_VALUE;
    String type = null;
    for (Map.Entry<String, Double> kv : map.entrySet())
      if (kv.getValue() > max) {
        type = kv.getKey();
        max = kv.getValue();
      }
//        System.out.println(type);
    return type;
  }

}
