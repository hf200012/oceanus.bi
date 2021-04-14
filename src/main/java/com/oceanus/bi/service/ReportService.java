package com.oceanus.bi.service;

import com.oceanus.bi.domain.Report;
import com.oceanus.bi.domain.ReportShare;
import com.oceanus.bi.mapper.ReportMapper;
import com.oceanus.common.util.DateUtil;
import com.oceanus.common.utils.IdUtils;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {


    @Autowired
    private ReportMapper reportMapper;

    /**
     * 获取我创建的报表及公开的报表
     * 这里目前没有实现SAAS功能，后期要加上企业编码，人员只能获取企业内部公开报表
     * @return
     */
    public List<Report> getAllReportByCreateor(){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id",SecurityUtils.getLoginUser().getUser().getUserId());
        return this.reportMapper.selectAllReportByCreateor(params);
    }


    /**
     * 获取我创建的报表及公开的报表
     * 这里目前没有实现SAAS功能，后期要加上企业编码，人员只能获取企业内部公开报表
     * @return
     */
    public List<Report> getAllReportListOfOwner(Report report){
//        Map<String,Object> params = new HashMap<>();
        report.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        return this.reportMapper.selecgAllReportListOfOwner(report);
    }

    public Report getBiReportById(String report_id){
        Map<String,Object> params = new HashMap<>();
        params.put("report_id",report_id);
        return this.reportMapper.selectBiReportById(params);
    }

    public int deleteBiReportById(String report_id){
        Map<String,Object> params = new HashMap<>();
        params.put("report_id",report_id);
        try{
            //删除报表共享数据
            this.reportMapper.deleteBiShareReportById(params);
            //删除报表
            return this.reportMapper.deleteBiReportById(params);
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int updateBiReport(Report report){
        if(report.isIs_public()) {
            report.setPrivate_status(0);
        } else {
            report.setPrivate_status(1);
        }
        report.setUpdate_date(DateUtil.getCurrentDateFormat());
        return this.reportMapper.updateBiReport(report);
    }

    public int saveReport(Report report){
        report.setReport_id(IdUtils.randomUUID());
        report.setCreate_date(DateUtil.getCurrentDateMFormat());
        report.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        report.setStatus(1);
        if(report.isIs_public()) {
            report.setPrivate_status(0);
        } else {
            report.setPrivate_status(1);
        }
        return this.reportMapper.insertReport(report);
    }

    public List<Map<String,Object>> getShareReportList(){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id",SecurityUtils.getLoginUser().getUser().getUserId());
        return this.reportMapper.selectShareReportList(params);
    }

    public List<Map<String,Object>> getPublicReportList(){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id",SecurityUtils.getLoginUser().getUser().getUserId());
        return this.reportMapper.selectPublicReportList(params);
    }



    /**
     * 保存分享数据
     * @param reportId
     * @param shareUserIds
     * @return
     */
    public int saveShare(String reportId,String shareUserIds){
        if(StringUtils.isNotEmpty(shareUserIds)){
            List<ReportShare> shares = new ArrayList<>();
            String[] ids = shareUserIds.split(",");
            long currentUserId = SecurityUtils.getLoginUser().getUser().getUserId();
            for(String id : ids){
                ReportShare share = new ReportShare();
                share.setReport_id(reportId);
                share.setTo_user_id(Long.parseLong(id));
                share.setShare_user_id(currentUserId);
                share.setShare_date(DateUtil.getCurrentDateFormat());
                shares.add(share);
            }
            return this.reportMapper.insertShare(shares);
        }
        return 0;
    }

}
