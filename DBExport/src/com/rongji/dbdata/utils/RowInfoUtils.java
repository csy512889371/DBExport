package com.rongji.dbdata.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rongji.dbdata.model.ColumnInfo;

public class RowInfoUtils {

	private static final Logger LOG = LoggerFactory.getLogger(RowInfoUtils.class);

	public List<List<ColumnInfo>> getRowInfo(String tableName) {

		List<List<ColumnInfo>> rowsList = new ArrayList<List<ColumnInfo>>();
		try {
			// 获取元数据
			JdbcUtils jdbcUtils = new JdbcUtils();
			
			/**
			 * key: 字段名 
			 * vale:字段描述
			 */
			Map<String, String> tableInfoMap = new LinkedHashMap<String, String>();;
			List<String> columnNameList = new ArrayList<String>();
			List<String> columnDescList = new ArrayList<String>();

			// 获取元数据
			JdbcUtils dcm = new JdbcUtils();
			getTableInfo(tableName, jdbcUtils, tableInfoMap, dcm);

			// 根据元数据 构造SQL
			StringBuffer sqlStr = jdbcUtils.buildSql(tableInfoMap, columnNameList, columnDescList);
			sqlStr.append(" from ").append(tableName);

			List<Object[]> dataInfoList = jdbcUtils.excuteQuery(sqlStr.toString(), null, dcm.getConnection());

			for (Object[] rows : dataInfoList) {
				if (rows == null || rows.length <= 0) {
					continue;
				}

				List<ColumnInfo> rowInfoList = new ArrayList<ColumnInfo>();

				for (int i = 0; i < rows.length; i++) {
					try {
						String columnName = columnNameList.get(i);
						String columnDesc = columnDescList.get(i);
						Object columnValue = rows[i];
						String value = FastjsonUtils.toJson(columnValue);
						ColumnInfo columnInfo = new ColumnInfo();
						columnInfo.setColumnDesc(columnDesc);
						columnInfo.setColumnName(columnName);
						columnInfo.setColumnValue(value);
						rowInfoList.add(columnInfo);

					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}
				rowsList.add(rowInfoList);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return rowsList;
	}

	private void getTableInfo(String tableName, JdbcUtils jdbcUtils, Map<String, String> tableInfoMap, JdbcUtils dcm) {
		String sql = buildSqlByDict(tableName);
		List<Object[]> tableInfo = jdbcUtils.excuteQuery(sql, null, dcm.getConnection());
		for (Object[] rows : tableInfo) {
			if (rows == null || rows.length <= 0) {
				continue;
			}
			String columnName = (String) rows[0];
			String columnDesc = (String) rows[1];
			tableInfoMap.put(columnName, columnDesc);
		}
	}

	public String buildSqlByDict(String tableName) {
		StringBuffer sqlStr = new StringBuffer("select columnName, columnDesc from TABLE_INFO where tableName = '" + tableName + "'");
		return sqlStr.toString();
	}
}
