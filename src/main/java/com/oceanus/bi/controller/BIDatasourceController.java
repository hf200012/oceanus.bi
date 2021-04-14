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

import com.oceanus.bi.domain.Datasource;
import com.oceanus.bi.domain.DatasourceTable;
import com.oceanus.bi.domain.TableField;
import com.oceanus.bi.service.BIDatasourceService;
import com.oceanus.bi.service.BITableService;
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
@RequestMapping("/rest/bi/datasource/")
public class BIDatasourceController extends BaseController {

    @Autowired
    private BIDatasourceService biDatasourceService;
    @Autowired
    private BITableService biTableService;

    /**
     * 添加数据源
     * @return
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Datasource datasource){
        //判断conn_url中是否存在
        if(datasource.getConn_url().contains("&amp;")){
            datasource.setConn_url(datasource.getConn_url().replaceAll("&amp;","&"));
        }
        if(!datasource.getConn_url().contains("?")){
            String url = datasource.getConn_url() + "?useSSL=false&serverTimezone=Asia/Shanghai";
            datasource.setConn_url(url);
        }
        //验证数据库连接是否正确
        boolean isSuccess = this.biDatasourceService.verifyDBConnect(datasource);
        if(isSuccess){
            int rows = this.biDatasourceService.insertDatasource(datasource);
            return toAjax(rows);
        } else {
            AjaxResult ajax = AjaxResult.error("数据库配置错误,请检查后重新提交");
            return ajax;
        }
    }

    /**
     * 修改数据源
     * @param datasource
     * @return
     */
    @PutMapping("/edit")
    public AjaxResult update(@RequestBody Datasource datasource){
        //判断conn_url中是否存在
        if(datasource.getConn_url().contains("&amp;")){
            datasource.setConn_url(datasource.getConn_url().replaceAll("&amp;","&"));
        }
        //验证数据库连接是否正确
        boolean isSuccess = this.biDatasourceService.verifyDBConnect(datasource);
        if(isSuccess){
            int rows = this.biDatasourceService.updateDatasource(datasource);
            return toAjax(rows);
        } else {
            AjaxResult ajax = AjaxResult.error("数据库配置错误,请检查后重新提交");
            return ajax;
        }
    }

    /**
     * 删除数据源
     * 会同时删除改数据源对应的所有数据，包括表，表对应的字段，chart，dashboard等
     * 请谨慎操作
     * @param source_id
     * @return
     */
    @DeleteMapping("/delete/{source_id}")
    public AjaxResult delete(@PathVariable(value = "source_id") int source_id){
        Map<String,Object> params = new HashMap<>();
        params.put("source_id",source_id);
        int rows = this.biDatasourceService.deleteDatasource(params);
        return toAjax(rows);
    }

    /**
     * 查询该用户的所有定义的数据源
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(){
        List<Datasource> list = this.biDatasourceService.getAllDatasourceByUser();
        return getDataTable(list);
    }

    /**
     * 根据数据源编号查询该数据源下所有的表
     * @param source_id
     * @return
     */
    @GetMapping("/getTable/{source_id}")
    public TableDataInfo getTableById(@PathVariable(value = "source_id", required = true) int source_id){
        List<DatasourceTable> list = this.biDatasourceService.getTableBySourceId(source_id);
        return getDataTable(list);
    }


    /**
     * 根据数据源编号查询数据源信息
     * @param source_id
     * @return
     */
    @GetMapping("/query/{source_id}")
    public AjaxResult query(@PathVariable(value = "source_id", required = true) int source_id){
        AjaxResult ajax = AjaxResult.success();
        Datasource datasource = this.biDatasourceService.getDatasourceById(source_id);
        ajax.put(AjaxResult.DATA_TAG, datasource);
        return ajax;
    }

    /**
     * 同步元数据
     * @param source_id
     * @return
     */
    @GetMapping("/sync/{source_id}")
    public AjaxResult syncMeta(@PathVariable(value = "source_id") int source_id){
        System.out.println("数据源编号：" + source_id);
        int rows = this.biDatasourceService.syncMeta(source_id);
        return toAjax(rows);
    }

    /**
     * 元数据树
     * @return
     */
    @GetMapping("/tabletree/{source_id}")
    public AjaxResult tableTreeBySourceIs(@PathVariable(value = "source_id") int source_id){
            Map<String,Object> root = new HashMap<>();
            root.put("id",0);
            root.put("label","Table列表");
            List<DatasourceTable> tables = this.biDatasourceService.getTableBySourceId(source_id);
            List<Map<String,Object>> tree = new ArrayList<>();
            for(DatasourceTable table : tables){
                Map<String,Object> child = new HashMap<>();
                child.put("id", table.getTable_id());
                child.put("label",table.getTable_alias() + "("+table.getTable_name()+")");
                List<TableField> list = this.biDatasourceService.getTableFieldsByTableId(table.getTable_id());
                List<Map<String,Object>> fields = new ArrayList<>();
                for(TableField field : list){
                    Map<String,Object> map = new HashMap<>();
                    map.put("id",field.getField_id());
                    map.put("label",field.getField_name()+" (" + field.getField_type()+") " + field.getField_cname());
                    fields.add(map);
                }
                child.put("children",fields);
                tree.add(child);
            }
            root.put("children",tree);
            return AjaxResult.success(tree);
        }

    /**
     * 元数据树
     * @return
     */
    @GetMapping("/metatree")
    public AjaxResult tableTree(){
        Map<String,Object> root = new HashMap<>();
        root.put("id","metadate-0");
        root.put("label","元数据列表");
        //获取所有数据源
        List<Datasource> datasourceList = this.biDatasourceService.getAllDatasourceByUser();
        List<Map<String,Object>> tree = new ArrayList<>();
        for(Datasource datasource : datasourceList){
            Map<String,Object> child = new HashMap<>();
            List<DatasourceTable> list = this.biTableService.getTableBySourceId(datasource.getSource_id());
            child.put("id",datasource.getSource_id());
            child.put("label",datasource.getBase_alias());
            child.put("dbtype",datasource.getDb_type());
            List<Map<String,Object>> childs = new ArrayList<>();
            for(DatasourceTable table : list){
                Map<String,Object> childTable = new HashMap<>();
                childTable.put("id",table.getTable_id());
                childTable.put("label",table.getTable_alias());
                childTable.put("tableName",table.getTable_name());
                childs.add(childTable);
            }
            child.put("children",childs);
            tree.add(child);
        }
        root.put("children",tree);
        return AjaxResult.success(tree);
    }

    /**
     * 查询表的所有字段名称
     * @param tableName
     * @param sourceId
     * @return
     */
    @GetMapping("/fields")
    public TableDataInfo fields(@RequestParam(value = "tableName") String tableName,
                                @RequestParam(value = "sourceId") int sourceId){
        List<TableField> list = this.biDatasourceService.getTableFieldsByTableName(sourceId,tableName);
        return getDataTable(list);
    }

    @GetMapping("/fieldsById")
    public TableDataInfo fieldsById(@RequestParam(value = "table_id") int table_id){
        List<TableField> list = this.biDatasourceService.getTableFieldsByTableId(table_id);
        return getDataTable(list);
    }


    @GetMapping("/linked/{chartId}")
    public AjaxResult linked(@PathVariable(value = "chartId") String chartId){
        AjaxResult ajax = AjaxResult.success();
        Datasource datasource = this.biDatasourceService.getDatasourceByChartId(chartId);
        ajax.put(AjaxResult.DATA_TAG, datasource);
        return ajax;
    }

    @GetMapping("/linkfields")
    public TableDataInfo linkFields(@RequestParam(value = "sourceId") int sourceId,
                                    @RequestParam(value = "tableName") String tableName){
        List<TableField> list = this.biTableService.selectTabelFieldBySourceAndTableName(sourceId,tableName);
        return getDataTable(list);
    }

    /**
     * 保存表中文名称
     * @return
     */
    @PostMapping("/saveTableConfig")
    public AjaxResult saveTableConfig(@RequestBody String json){
        int rows = this.biDatasourceService.saveTableConfig(json);
        return toAjax(rows);
    }


}
