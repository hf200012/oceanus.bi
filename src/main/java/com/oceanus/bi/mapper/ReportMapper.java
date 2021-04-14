package com.oceanus.bi.mapper;

import com.oceanus.bi.domain.Report;
import com.oceanus.bi.domain.ReportShare;

import java.util.List;
import java.util.Map;

public interface ReportMapper {

    public List<Report> selectAllReportByCreateor(Map<String,Object> params);

    public List<Report> selecgAllReportListOfOwner(Report report);

    public Report selectBiReportById(Map<String,Object> params);

    public int deleteBiReportById(Map<String,Object> params);

    public int deleteBiShareReportById(Map<String,Object> params);

    public int updateBiReport(Report report);

    public int insertReport(Report report);

    public int insertShare(List<ReportShare> shares);

    public List<Map<String,Object>> selectShareReportList(Map<String,Object> params);

    public List<Map<String,Object>> selectPublicReportList(Map<String,Object> params);
}
