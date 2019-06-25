package com.ch116221.document.server.domains;

import lombok.Data;

import java.io.Serializable;

/**
 * 列结构信息
 *
 * @author ch
 */
@Data
public class ColumnStructure implements Serializable {

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
     * 所属表
     */
    private String tableName;
    /**
     * 列注释
     * */
    private String columnComment;
}
