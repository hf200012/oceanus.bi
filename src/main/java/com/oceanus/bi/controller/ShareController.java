package com.oceanus.bi.controller;

import com.oceanus.bi.service.ShareService;
import com.oceanus.framework.web.controller.BaseController;
import com.oceanus.framework.web.domain.AjaxResult;
import com.oceanus.framework.web.page.TableDataInfo;
import com.oceanus.system.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据概览分享操作
 */
@RestController
@RequestMapping("/rest/share")
public class ShareController extends BaseController {


    @Autowired
    private ShareService shareService;
    /**
     * 查询要分享的用户列表
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo listUser(){
        List<SysUser> list = this.shareService.getShareUserList();
        return getDataTable(list);
    }

    @GetMapping("/add")
    public AjaxResult saveShare(@RequestParam(value = "dashboard_id") String dashboard_id,
                                @RequestParam(value = "userids") String userids){
        int rows = this.shareService.saveShare(dashboard_id,userids);
        return toAjax(rows);
    }

}
