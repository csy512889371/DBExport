package com.rongji.dbdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rongji.dbdata.model.ColumnInfo;
import com.rongji.dbdata.utils.RowInfoUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CreateXML {

	private static final Logger LOG = LoggerFactory.getLogger(CreateXML.class);

	public static void main(String[] args) {

		// TODO 使用时候改成对应的 表面
		String tableName = "A01";
		
		try {
			// 配置模版
			Configuration configuration = buildConfiguration();
			Template template = configuration.getTemplate(Constant.TEMPNAME);

			RowInfoUtils rowInfoUtils = new RowInfoUtils();
			// 构造数据 根据表名
			List<List<ColumnInfo>> rowsList = rowInfoUtils.getRowInfo(tableName);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("rowsList", rowsList);
			// 生成文件
			outputFile(template, paramMap, tableName + ".xml");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (TemplateException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private static Configuration buildConfiguration() throws IOException {
		Configuration configuration = new Configuration();
		configuration.setDirectoryForTemplateLoading(new File(Constant.TEMPLATEPATH));
		configuration.setObjectWrapper(new DefaultObjectWrapper());
		configuration.setDefaultEncoding("UTF-8");
		return configuration;
	}

	private static void outputFile(Template template, Map<String, Object> paramMap, String fileName) throws UnsupportedEncodingException, FileNotFoundException, TemplateException, IOException {
		File outFile = new File(Constant.OUTPUT_FILE_PATH + fileName);
		Writer writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
		template.process(paramMap, writer);
		System.out.println("恭喜，生成成功~~");
	}

}
