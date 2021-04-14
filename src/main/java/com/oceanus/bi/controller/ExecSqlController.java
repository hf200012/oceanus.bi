package com.oceanus.bi.controller;

import com.oceanus.bi.domain.ExecSql;
import com.oceanus.bi.service.ExecSqlService;
import com.oceanus.common.entity.ResponseEntity;
import com.oceanus.framework.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/bi/exec")
public class ExecSqlController extends BaseController {

    @Autowired
    private ExecSqlService execSqlService;

    /**
     * 执行sql，返回结果
     * @return
     */
    @PostMapping("/exec")
    public Object execSql(@RequestBody ExecSql sql){
        String buildSql = sql.getSql();
        buildSql = buildSql.replaceAll("&lt;",">");
        buildSql = buildSql.replaceAll("&gt;","<");
        System.out.println("执行查询SQL：" + buildSql);
        List<Map<String,Object>> list = this.execSqlService.execSql(sql.source_id,buildSql);
        return ResponseEntity.ok().build(list);
    }

}
