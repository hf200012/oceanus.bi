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
import com.oceanus.bi.service.BIChartService;
import com.oceanus.common.utils.IdUtils;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.framework.web.controller.BaseController;
import com.oceanus.framework.web.domain.AjaxResult;
import com.oceanus.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/bi/chart")
public class BIChartController extends BaseController {

    @Autowired
    private BIChartService biChartService;

    @PostMapping("/create")
    public AjaxResult add(@RequestBody Chart chart){
        chart.setChart_id(IdUtils.randomUUID());
        int rows = this.biChartService.saveChart(chart);
        Map<String,String> id = new HashMap<>();
        id.put("id",chart.getChart_id());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, id);
        return ajax;
    }

    @PutMapping("/update")
    public AjaxResult update(@RequestBody Chart chart){
        Chart ch = this.biChartService.selectBiChartById(chart.getChart_id());
        long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if(ch.getCreator_id() == currentUserId) {
            int rows = this.biChartService.updateBiChart(chart);
            Map<String, String> id = new HashMap<>();
            id.put("id", chart.getChart_id());
            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, id);
            return ajax;
        } else {
            AjaxResult ajax = AjaxResult.error(403,"不是该图表的创建者,不能修改");
            return ajax;
        }
    }

    /**
     * 查询该用户创建的所有图表
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(Chart chart){
        startPage();
        List<Chart> list = this.biChartService.getAllChartByUser(chart);
        return getDataTable(list);
    }


    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestParam(value = "chartId") String chartId){
        Chart ch = this.biChartService.selectBiChartById(chartId);
        long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if(ch.getCreator_id() == currentUserId) {
            int rows = this.biChartService.deleteBiChartById(chartId);
            return toAjax(rows);
        } else {
            AjaxResult ajax = AjaxResult.error(403,"不是该图表的创建者,不能删除");
            return ajax;
        }
    }

    @GetMapping("/tables/{chartId}")
    public AjaxResult query(@PathVariable(value = "chartId", required = true) String chartId){
        AjaxResult ajax = AjaxResult.success();
        Chart chart = this.biChartService.selectBiChartById(chartId);
        String content = chart.getChartContent();
        content = content.replaceAll("&lt;",">");
        content = content.replaceAll("&gt;","<");
        System.out.println(content);
        chart.setContent(JSON.parse(content));
        ajax.put(AjaxResult.DATA_TAG, chart);
        return ajax;
    }

}
