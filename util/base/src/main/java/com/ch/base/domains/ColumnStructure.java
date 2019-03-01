package com.ch.base.domains;

import lombok.Data;

import java.io.Serializable;

/**
 * 列结构信息
 *
 * @author ch
 */
@Data
public class ColumnStructure implements Serializable {
    /**
     * 主键
     */
    private String id;

    private static final long serialVersionUID = 677998367449480891L;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * 是否为空
     */
    private String isNullable;

    /**
     * 主键列
     */
    private String columnKey;

    /**
     * 表信息
     */
    private String tableInfoId;

    /**
     * <p>用户指定列名</p>
     */
    private String userColumnName;

    /**
     * <p>空值替换</p>
     */
    private String nullShow;

    /**
     * <p>是否显示</p>
     */
    private Boolean doesItShow = true;

    /**
     * <p>所属表</p>
     */
    private String tableName;

    /**
     * <p>流程节点</p>
     */
    private String processNodeId;
}