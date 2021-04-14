package com.oceanus.bi.controller;

import com.oceanus.bi.domain.ExecSql;
import com.oceanus.bi.domain.Report;
import com.oceanus.bi.service.ExecSqlService;
import com.oceanus.bi.service.ReportService;
import com.oceanus.common.constant.HttpStatus;
import com.oceanus.common.entity.ResponseEntity;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.framework.web.controller.BaseController;
import com.oceanus.framework.web.domain.AjaxResult;
import com.oceanus.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/report/")
public class ReportController extends BaseController {

    @Autowired
    private ExecSqlService execSqlService;

    @Autowired
    private ReportService reportService;

    @PostMapping("/sql_exec")
    public Object sqlExec(@RequestBody ExecSql sql){
        Map<String,Object> result = this.execSqlService.execSQL(sql);
        ResponseEntity entity = ResponseEntity.ok().build();
        List<Map<String,String>> tables = (List<Map<String,String>>)result.get("tables");
        if(tables != null) {
            entity.setCount(tables.size());
            entity.setData(result);
        } else {
            entity.setCode(HttpStatus.BAD_REQUEST);
            entity.setMsg(result.get("error").toString());
        }
        return entity;
    }


    @PostMapping("/create")
    public AjaxResult add(@RequestBody Report report){
        int rows = this.reportService.saveReport(report);
        Map<String,String> id = new HashMap<>();
        id.put("id",report.getReport_id());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, id);
        return ajax;
    }

    @PutMapping("/update")
    public AjaxResult update(@RequestBody Report report){
        Report re = this.reportService.getBiReportById(report.getReport_id());
        long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if(currentUserId == re.getCreator_id()) {
            int rows = this.reportService.updateBiReport(report);
            Map<String, String> id = new HashMap<>();
            id.put("id", report.getReport_id());
            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, id);
            return ajax;
        } else {
            AjaxResult ajax = AjaxResult.error(403,"不是该报表的创建者,不能修改");
            return ajax;
        }
    }

    /**
     * 根据报表编号获取报表信息
     * @param report_id
     * @return
     */
    @DeleteMapping("/remove/{report_id}")
    public AjaxResult delete(@PathVariable(value = "report_id", required = true) String report_id) {
        Report re = this.reportService.getBiReportById(report_id);
        long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
        if(currentUserId == re.getCreator_id()) {
            int rows = this.reportService.deleteBiReportById(report_id);
            return toAjax(rows);
        } else {
            AjaxResult ajax = AjaxResult.error(403,"不是该报表的创建者,不能删除");
            return ajax;
        }
    }

        /**
         * 根据报表编号获取报表信息
         * @param report_id
         * @return
         */
    @GetMapping("/query/{report_id}")
    public AjaxResult query(@PathVariable(value = "report_id", required = true) String report_id) {
        AjaxResult ajax = AjaxResult.success();
        Report report = this.reportService.getBiReportById(report_id);
        if(report.getPrivate_status() ==1){
            report.setIs_public(false);
        } else {
            report.setIs_public(true);
        }
        ajax.put(AjaxResult.DATA_TAG, report);
        return ajax;
    }

    /**
     * 我的报表树
     * @return
     */
    @GetMapping("/myreporttree")
    public AjaxResult myDashboardTree() {
        Map<String, Object> root = new HashMap<>();
        root.put("id", "MyReportTree");
        root.put("label", "我的数据报表");
        //获取所有数据源
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        List<Report> list = this.reportService.getAllReportByCreateor();
        child.put("id", "MyCreateDashboarList");
        child.put("label", "我的报表");
        List<Map<String, Object>> reportList = new ArrayList<>();
        for (Report report : list) {
            Map<String, Object> reportMap = new HashMap<>();
            reportMap.put("isChild",true);
            reportMap.put("id",report.getReport_id());
            reportMap.put("source_id",report.getSource_id());
            reportMap.put("report_name",report.getReport_name());
            reportMap.put("report_desc",report.getReport_desc());
            reportMap.put("label",report.getReport_name());
            reportMap.put("isShare",false);
            reportList.add(reportMap);
        }
        child.put("children", reportList);
        if(!list.isEmpty()) {
            tree.add(child);
        }
        //分享给我的数据报表
        Map<String, Object> sharechild = new HashMap<>();
        List<Map<String,Object>> sharelist = this.reportService.getShareReportList();
        sharechild.put("id", "shareToMeReportList");
        sharechild.put("label", "分享给我的报表");
        for (Map<String,Object> dash : sharelist) {
            dash.put("isChild",true);
            dash.put("isShare",true);
            dash.put("source_id",dash.get("source_id"));
            dash.put("report_name",dash.get("label"));
        }
        sharechild.put("children", sharelist);
        if(!sharelist.isEmpty()) {
            tree.add(sharechild);
        }
        root.put("children", tree);
        return AjaxResult.success(tree);
    }

    /**
     * 公共数据概览树
     * @return
     */
    @GetMapping("/publictree")
    public AjaxResult publicTree() {
        Map<String, Object> root = new HashMap<>();
        root.put("id", "publicReportTree");
        root.put("label", "公共数据报表");
        //获取所有数据源
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> child = new HashMap<>();
        List<Map<String,Object>> list = this.reportService.getPublicReportList();
        child.put("id", "publicDashboardList");
        child.put("label", "公共报表");
        for (Map<String,Object> dash : list) {
            dash.put("isChild",true);
            dash.put("isShare",true);
            dash.put("source_id",dash.get("source_id"));
            dash.put("report_name",dash.get("label"));
        }
        if(!list.isEmpty()) {
            child.put("children", list);
            tree.add(child);
        }
        root.put("children", tree);
        return AjaxResult.success(tree);
    }


    /**
     * 根据report，执行报表sql，获取报表结果
     * @param report
     * @return
     */
    @GetMapping("/reportresult")
    public Object reportResult(Report report){
        startPage();
        Map<String,Object> result = this.execSqlService.getReportResult(report);
        List<Map<String,String>> tables = (List<Map<String,String>>)result.get("tables");
        TableDataInfo table = null;
        if(tables != null) {
            table = getDataTable(tables);
            List<Map<String,String>> headers = (List<Map<String,String>>)result.get("tableData");
            Long total = (Long)result.get("total");
            table.setHeaders(headers);
            table.setTotal(total);
        }
        return table;
    }

    @GetMapping("/share")
    public AjaxResult saveShare(@RequestParam(value = "report_id") String report_id,
                                @RequestParam(value = "userids") String userids){
        int rows = this.reportService.saveShare(report_id,userids);
        return toAjax(rows);
    }

    /**
     * 报表列表查看页面
     * @param report
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(Report report){
        startPage();
        List<Report> reports = this.reportService.getAllReportListOfOwner(report);
        return getDataTable(reports);
    }
}
