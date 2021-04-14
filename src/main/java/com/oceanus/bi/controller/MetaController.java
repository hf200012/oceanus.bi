package com.oceanus.bi.controller;

import com.oceanus.bi.domain.DatasourceTable;
import com.oceanus.bi.domain.TableField;
import com.oceanus.bi.service.BIDatasourceService;
import com.oceanus.bi.service.BITableService;
import com.oceanus.common.util.DateUtil;
import com.oceanus.common.utils.SecurityUtils;
import com.oceanus.common.utils.poi.ExcelUtil;
import com.oceanus.framework.web.controller.BaseController;
import com.oceanus.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/rest/bi/meta")
public class MetaController extends BaseController {

    @Autowired
    private BITableService biTableService;
    @Autowired
    private BIDatasourceService biDatasourceService;

    /**
     * 修改表所有字段中文名称
     * @param json
     * @return
     */
    @PostMapping("/saveTableFieldConfig")
    public AjaxResult saveTableFieldConfig(@RequestBody String json){
        int rows = this.biDatasourceService.saveTableFieldsConfig(json);
        return toAjax(rows);
    }


    /**
     * 添加表
     * @return
     */
    @PostMapping("/addtable")
    public AjaxResult addtable(@RequestBody DatasourceTable table){
        int rows = this.biTableService.saveTable(table);
        return toAjax(rows);
    }

    /**
     * 导入
     * @return
     */
    @PostMapping("/importField")
    public AjaxResult importField(MultipartFile file,int table_id){
        try {
            ExcelUtil<TableField> util = new ExcelUtil<TableField>(TableField.class);
            List<TableField> tableFields = util.importExcel(file.getInputStream());
            for(TableField field : tableFields){
                field.setIs_enable(1);
                field.setCreatedate(DateUtil.getCurrentDateFormat());
                field.setCreate_user_id(SecurityUtils.getLoginUser().getUser().getUserId());
                field.setCreate_user_name(SecurityUtils.getUsername());
                field.setTable_id(table_id);

            }
            this.biTableService.insertTableField(tableFields);
            return AjaxResult.success("导入表字段成功");
        } catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.success("导入表字段失败");
    }

    /**
     *下载字段导入模板
     * @return
     */
    @GetMapping("/dowloadTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<TableField> util = new ExcelUtil<TableField>(TableField.class);
        return util.importTemplateExcel("数据表字段");
    }
}
