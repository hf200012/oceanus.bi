package com.oceanus.bi.mapper;

import com.oceanus.bi.domain.Project;

import java.util.List;
import java.util.Map;

public interface BIProjectMapper {

    public List<Project> selectAllProjectByCreateor(Map<String,Object> params);

    public List<Project> selectAllPublicProject(Map<String,Object> params);

    public Project selectBiProjectById(Map<String,Object> params);

    public int deleteBiProjectById(Map<String,Object> params);

    public int updateBiproject(Project project);

    public int insertProject(Project project);
}
