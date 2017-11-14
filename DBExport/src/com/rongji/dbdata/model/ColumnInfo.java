package com.rongji.dbdata.model;

import java.io.Serializable;

public class ColumnInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String columnName;

	private String columnDesc;

	private String columnValue;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnDesc() {
		return columnDesc;
	}

	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

}
