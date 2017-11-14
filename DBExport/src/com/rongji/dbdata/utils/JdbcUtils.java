package com.rongji.dbdata.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rongji.dbdata.Constant;

public class JdbcUtils {

	private static final Logger LOG = LoggerFactory.getLogger(JdbcUtils.class);

	// SQLServer

	public Connection getConnection() {
		try {
			Class.forName(Constant.driverName);
			return DriverManager.getConnection(Constant.url, Constant.user, Constant.password);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public StringBuffer buildSql(Map<String, String> tableInfoMap, List<String> columnNameList, List<String> columnDescList) {
		int count = tableInfoMap.entrySet().size();
		StringBuffer sqlStr = new StringBuffer("select ");
		int index = 0;
		for (Entry<String, String> entrySet : tableInfoMap.entrySet()) {
			String columnName = entrySet.getKey();
			columnNameList.add(columnName);
			columnDescList.add(entrySet.getValue());

			sqlStr.append(columnName).append(" ");
			if (index != (count - 1)) {
				sqlStr.append(" , ");
			}
			index++;
		}
		return sqlStr;
	}

	/*public Map<String, String> getTableInfo(String tableName) {
		String sql = "SELECT COLUMN_NAME,DATA_TYPE FROM INFORMATION_SCHEMA.columns WHERE TABLE_NAME='" + tableName + "'";

		Map<String, String> tableInfoMap = new LinkedHashMap<String, String>();
		try {
			JdbcUtils dcm = new JdbcUtils();
			List<Object[]> cadreInfoList = excuteQuery(sql, null, dcm.getConnection());

			for (Object[] columns : cadreInfoList) {
				if (columns == null || columns.length <= 0) {
					continue;
				}

				String columnName = (String) columns[0];
				String dataType = (String) columns[1];
				tableInfoMap.put(columnName, dataType);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return tableInfoMap;
	}*/

	public List<Object[]> excuteQuery(String sql, Object[] objs, Connection conn) {
		PreparedStatement psta = null;
		ResultSet rs = null;
		List<Object[]> iResult = null;
		Object[] objArr = null;
		try {
			PreparedStatement state = conn.prepareStatement(sql);
			if (null != objs) {
				for (int i = 0; i < objs.length; i++) {
					state.setObject(i + 1, objs[i]);
				}
			}
			ResultSet resultSet = state.executeQuery(); // 执行查询，返回结果接集合
			iResult = new ArrayList<Object[]>();
			int count = resultSet.getMetaData().getColumnCount(); // 一共有多少列数据
			while (resultSet.next()) {
				objArr = new Object[count];
				for (int i = 1; i <= count; i++) {
					objArr[i - 1] = resultSet.getObject(i); // 增加到返回的集合中
				}
				iResult.add(objArr);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			iResult = null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psta != null) {
					psta.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {

			}
		}
		return iResult;
	}

}
