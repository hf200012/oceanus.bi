// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.oceanus.bi.controller;


import com.alibaba.fastjson.JSON;
import com.oceanus.bi.domain.Chart;
import com.oceanus.bi.domain.Dashboard;
import com.oceanus.bi.domain.Project;
import com.oceanus.bi.service.BIChartService;
import com.oceanus.bi.service.BIDashboardService;
import com.oceanus.bi.service.BIProjectService;
import com.oceanus.common.utils.IdUtils;
import com.oceanus.common.entity.ResponseEntity;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.common.utils.StringUtils;
import com.oceanus.framework.web.controller.BaseController;
import com.oceanus.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/bi/dashboard")
public class BIDashboardController extends BaseController {

    @Autowired
    private BIDashboardService biDashboardService;

    @Autowired
    private BIChartService biChartService;

    @Autowired
    private BIProjectService biProjectService;

    @PostMapping("/create")
    public AjaxResult add(@RequestBody Dashboard dashboard) {
        dashboard.setDashboard_id(IdUtils.randomUUID());
        int rows = this.biDashboardService.saveBiDashboard(dashboard);
        Map<String, String> id = new HashMap<>();
        id.put("id", dashboard.getDashboard_id());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, id);
        return ajax;
    }

    @PutMapping("/update")
    public AjaxResult update(@RequestBody Dashboard dashboard) {
        Dashboard dash = this.biDashboardService.selectBiDashboardById(dashboard.getDashboard_id());
        long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if(currentUserId == dash.getCreator_id()) {
            int rows = this.biDashboardService.updateBiDashboard(dashboard);
            Map<String, String> id = new HashMap<>();
            id.put("id", dashboard.getDashboard_id());
            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, id);
            return ajax;
        } else {
            AjaxResult ajax = AjaxResult.error(403,"?????????????????????????????????,????????????");
            return ajax;
        }
    }

    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestParam(value = "dashboardId") String dashboardId) {
        Dashboard dash = this.biDashboardService.selectBiDashboardById(dashboardId);
        long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if(currentUserId == dash.getCreator_id()) {
            int rows = this.biDashboardService.deleteBiDashboardById(dashboardId);
            return toAjax(rows);
        } else {
            AjaxResult ajax = AjaxResult.error(403,"?????????????????????????????????,????????????");
            return ajax;
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param project_id
     * @return
     */
    @GetMapping("/list/{project_id}")
    public Object list(@PathVariable(value = "project_id") String project_id) {
        List<Dashboard> list = this.biDashboardService.getDashboardListByCreatorId(project_id);
        List<String> orders = this.biDashboardService.getOrderByCreatorId(project_id);
        Map<String, Object> map = new HashMap<>();
        map.put("dashboards", list);
        map.put("order", orders);
        return ResponseEntity.ok().build(map);
    }

    /**
     * ????????????????????????????????????????????????
     * @return
     */
    private List<Map<String, Object>> getAllProjectDashboard(){
        List<Project> projects = this.biProjectService.getAllProjectByCreateor();
        //?????????????????????
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        child.put("id", "MyCreateDashboarList");
        child.put("label", "????????????");
        //???????????????????????????
        Map<String, Object> sharechild = new HashMap<>();
        sharechild.put("id", "shareToMeDashboarList");
        sharechild.put("label", "?????????????????????");
        List<Map<String,Object>> myList = new ArrayList<>();
        List<Map<String,Object>> shares = new ArrayList<>();
        for(Project project : projects){
            List<Map<String,Object>> list = this.biDashboardService.getMyDashboardList(project.getProject_id());
            for (Map<String,Object> dash : list) {
                dash.put("isChild",true);
            }
            myList.addAll(list);
            List<Map<String,Object>> sharelist = this.biDashboardService.getShareDashboardList(project.getProject_id());
            for (Map<String,Object> dash : sharelist) {
                dash.put("isChild",true);
                dash.put("isShare",true);
            }
            shares.addAll(sharelist);
        }
        child.put("children", myList);
        if(!myList.isEmpty()) {
            tree.add(child);
        }
        sharechild.put("children", shares);
        if(!shares.isEmpty()) {
            tree.add(sharechild);
        }

        return tree;
    }

    /**
     * ???????????????????????????
     * @param project_id
     * @return
     */
    private List<Map<String, Object>> getProjectDashboardList(String project_id){
        //?????????????????????
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        List<Map<String,Object>> list = this.biDashboardService.getMyDashboardList(project_id);
        child.put("id", "MyCreateDashboarList");
        child.put("label", "????????????");
        for (Map<String,Object> dash : list) {
            dash.put("isChild",true);
        }
        child.put("children", list);
        if(!list.isEmpty()) {
            tree.add(child);
        }
        //???????????????????????????
        Map<String, Object> sharechild = new HashMap<>();
        List<Map<String,Object>> sharelist = this.biDashboardService.getShareDashboardList(project_id);
        sharechild.put("id", "shareToMeDashboarList");
        sharechild.put("label", "?????????????????????");
        for (Map<String,Object> dash : sharelist) {
            dash.put("isChild",true);
        }
        sharechild.put("children", sharelist);
        if(!sharelist.isEmpty()) {
            tree.add(sharechild);
        }
        return tree;
    }

    /**
     * ?????????????????????
     * @return
     */
    @GetMapping("/mytree/{project_id}")
    public AjaxResult myDashboardTree(@PathVariable(value = "project_id") String project_id) {
        Map<String, Object> root = new HashMap<>();
        root.put("id", "MyDashboardTree");
        root.put("label", "??????????????????");
        List<Map<String, Object>> tree = new ArrayList<>();
        if(StringUtils.equalsIgnoreCase(project_id,"undefined")){
            tree = this.getAllProjectDashboard();
        } else {
            tree = this.getProjectDashboardList(project_id);
        }
        root.put("children", tree);
        return AjaxResult.success(tree);
    }

    /**
     * ?????????????????????????????????????????????
     * @return
     */
    private List<Map<String, Object>> getAllPublicProjectDashboardList(){
        List<Project> projects = this.biProjectService.getPublicProjectList();
        //?????????????????????
        List<Map<String, Object>> tree = new ArrayList<>();
        List<Map<String, Object>> publicList = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        child.put("id", "publicDashboardList");
        child.put("label", "????????????");
        for(Project project : projects) {
            List<Map<String,Object>> list = this.biDashboardService.getPublicDashboardList(project.getProject_id());
            for (Map<String,Object> dash : list) {
                dash.put("isChild",true);
                dash.put("isShare",true);
            }
            publicList.addAll(list);
        }
        if(!publicList.isEmpty()) {
            child.put("children", publicList);
            tree.add(child);
        }
        return tree;
    }

    private List<Map<String,Object>> getPublicDashboardByProject(String project_id){
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        List<Map<String,Object>> list = this.biDashboardService.getPublicDashboardList(project_id);
        child.put("id", "publicDashboardList");
        child.put("label", "????????????");
        for (Map<String,Object> dash : list) {
            dash.put("isChild",true);
        }
        if(!list.isEmpty()) {
            child.put("children", list);
            tree.add(child);
        }
        return tree;
    }

    /**
     * ?????????????????????
     * @return
     */
    @GetMapping("/publictree/{project_id}")
    public AjaxResult publicTree(@PathVariable(value = "project_id") String project_id) {
        Map<String, Object> root = new HashMap<>();
        root.put("id", "publicDashboardTree");
        root.put("label", "??????????????????");
        //?????????????????????
        List<Map<String, Object>> tree = new ArrayList<>();
        if(StringUtils.equalsIgnoreCase(project_id,"undefined")){
            tree = this.getAllPublicProjectDashboardList();
        } else {
            tree = this.getPublicDashboardByProject(project_id);
        }
        root.put("children", tree);
        return AjaxResult.success(tree);
    }

    /**
     * ??????????????????????????????
     *
     * @param dashboardId
     * @return
     */
    @GetMapping("/chartByDashboard/{dashboardId}")
    public Object chartByDashboard(@PathVariable(value = "dashboardId") String dashboardId) {
        List<Chart> list = this.biChartService.selectChartListByDashboardId(dashboardId);
        return ResponseEntity.ok().build(list);
    }

    /**
     * ???????????????dashboard???
     *
     * @param dashboardId
     * @return
     */
    @GetMapping("/map")
    public AjaxResult map(@RequestParam(value = "chartId") String chartId,
                          @RequestParam(value = "dashboardId") String dashboardId) {
        int rows = this.biDashboardService.addChartToDashboard(chartId, dashboardId);
        return toAjax(rows);
    }

    /**
     * ????????????????????????
     *
     * @param dashboardId
     * @return
     */
    @GetMapping("/unmap")
    public AjaxResult unmap(@RequestParam(value = "chartId") String chartId,
                            @RequestParam(value = "dashboardId") String dashboardId) {
        Dashboard dash = this.biDashboardService.selectBiDashboardById(dashboardId);
        long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if(currentUserId == dash.getCreator_id()) {
            int rows = this.biDashboardService.removeChartToDashboard(chartId, dashboardId);
            return toAjax(rows);
        } else {
            AjaxResult ajax = AjaxResult.error(403,"?????????????????????????????????,????????????????????????????????????");
            return ajax;
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param dashboardId
     * @return
     */
    @GetMapping("/query/{dashboardId}")
    public AjaxResult query(@PathVariable(value = "dashboardId", required = true) String dashboardId) {
        AjaxResult ajax = AjaxResult.success();
        Dashboard dashboard = this.biDashboardService.selectBiDashboardById(dashboardId);
        if(dashboard.getPrivate_status() == 0){
            dashboard.setIs_public(true);
        } else {
            dashboard.setIs_public(false);
        }
        dashboard.setContent(JSON.parse(dashboard.getDashbordContent()));
        ajax.put(AjaxResult.DATA_TAG, dashboard);
        return ajax;
    }

    /**
     * ??????????????????
     *
     * @param dashboards
     * @return
     */
    @PostMapping("/order")
    public AjaxResult order(@RequestBody List<Dashboard> dashboards) {
        int rows = this.biDashboardService.updateBiDashboardOrder(dashboards);
        return toAjax(rows);
    }

}
