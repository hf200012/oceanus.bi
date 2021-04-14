package com.oceanus.bi.service;

import com.oceanus.bi.domain.Project;
import com.oceanus.bi.mapper.BIDashboradMapper;
import com.oceanus.bi.mapper.BIProjectMapper;
import com.oceanus.common.util.DateUtil;
import com.oceanus.common.utils.IdUtils;
import com.oceanus.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BIProjectService {


    @Autowired
    private BIProjectMapper biProjectMapper;

    private BIDashboradMapper biDashboradMapper;

    /**
     * 获取我创建的项目及公开的项目
     * 这里目前没有实现SAAS功能，后期要加上企业编码，人员只能获取企业内部公开项目
     * @return
     */
    public List<Project> getAllProjectByCreateor(){
        Map<String,Object> params = new HashMap<>();
        params.put("creator_id",SecurityUtils.getLoginUser().getUser().getUserId());
        return this.biProjectMapper.selectAllProjectByCreateor(params);
    }

    public Project getBiProjectById(String project_id){
        Map<String,Object> params = new HashMap<>();
        params.put("project_id",project_id);
        return this.biProjectMapper.selectBiProjectById(params);
    }

    /**
     * SAAS化要加上企业编号
     * @return
     */
    public List<Project> getPublicProjectList(){
        Map<String,Object> params = new HashMap<>();
        return this.biProjectMapper.selectAllPublicProject(params);
    }

    public int deleteBiProjectById(String project_id){
        Map<String,Object> params = new HashMap<>();
        params.put("project_id",project_id);
        try{
            //首先删除改项目对应的概览，包括概览共享数据，概览和图表对应关系
            this.biDashboradMapper.deleteBiDashboardChartMapByProjectId(params);
            this.biDashboradMapper.deleteBiShareDashboardByProjectId(params);
            this.biDashboradMapper.deleteBiDashboardByProjectId(params);
            //删除项目
            this.biProjectMapper.deleteBiProjectById(params);
        } catch (Exception e){

        }
        return this.biProjectMapper.deleteBiProjectById(params);
    }

    public int updateBiproject(Project project){
        if(project.isIs_public()) {
            project.setPrivate_status(0);
        } else {
            project.setPrivate_status(1);
        }
        return this.biProjectMapper.updateBiproject(project);
    }

    public int saveProject(Project project){
        project.setProject_id(IdUtils.randomUUID());
        project.setCreated_at(DateUtil.getCurrentDateMFormat());
        project.setCreator_id(SecurityUtils.getLoginUser().getUser().getUserId());
        project.setStatus(1);
        if(project.isIs_public()) {
            project.setPrivate_status(0);
        } else {
            project.setPrivate_status(1);
        }
        project.setCreator_username(SecurityUtils.getUsername());
        return this.biProjectMapper.insertProject(project);
    }

}
