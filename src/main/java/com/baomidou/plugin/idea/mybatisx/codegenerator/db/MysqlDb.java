package com.baomidou.plugin.idea.mybatisx.codegenerator.db;

import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.ColumnInfo;
import com.baomidou.plugin.idea.mybatisx.codegenerator.domain.vo.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlDb extends BaseDb {
    private Logger logger = LoggerFactory.getLogger(MysqlDb.class);

    @Override
    public String getTableSql() {
        return "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables\n" +
            "where table_schema = (select database())";
    }

    @Override
    public String getColumnSql(String tableName) {
        return "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
            "where table_name = '" + tableName + "' and table_schema = (select database()) order by ordinal_position";
    }

    @Override
    public String dbName() {
        return "mysql";
    }

    @Override
    public List<TableInfo> getTableInfo() {

        List<TableInfo> tableInfos = new ArrayList<>();
        Connection conn;
        Statement stmt;
        try {
            Class.forName(getJdbcDriver());
            conn = DriverManager.getConnection(getDbUrl(), getUsername(), getPassword());
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getTableSql());
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                String createTime = rs.getString("create_time");
                String engine = rs.getString("engine");
                String coding = rs.getString("table_collation");
                String remark = rs.getString("table_comment");
                tableInfos.add(new TableInfo()
                    .setTableName(tableName)
                    .setCreateTime(createTime)
                    .setEngine(engine)
                    .setCoding(coding)
                    .setRemark(remark)
                );
            }
            stmt.close();
            conn.close();
            return tableInfos;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        Connection conn;
        Statement stmt;
        try {
            Class.forName(getJdbcDriver());
            conn = DriverManager.getConnection(getDbUrl(), getUsername(), getPassword());
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(getColumnSql(tableName));
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                String isNullable = rs.getString("is_nullable");
                String dataType = rs.getString("data_type");
                String columnComment = rs.getString("column_comment");
                String columnKey = rs.getString("column_key");
                String extra = rs.getString("extra");
                columnInfoList.add(new ColumnInfo().setColumnName(columnName)
                    .setIsNullable(isNullable)
                    .setColumnType(dataType)
                    .setColumnComment(columnComment)
                    .setColumnKey(columnKey)
                    .setExtra(extra));
            }
            stmt.close();
            conn.close();
            return columnInfoList;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.toString());
        }
        return new ArrayList<>();
    }

    @Override
    public String getJdbcDriver() {
        return "com.mysql.jdbc.Driver";
    }
}
