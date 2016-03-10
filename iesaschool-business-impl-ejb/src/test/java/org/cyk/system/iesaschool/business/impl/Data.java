package org.cyk.system.iesaschool.business.impl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.CommonUtils.ReadExcelSheetArguments;

public class Data {

	public static void main(String[] args) {
		File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\data");
		ReadExcelSheetArguments arguments = new ReadExcelSheetArguments();
		try {
			arguments.setWorkbookInputStream(new FileInputStream(new File(directory, "data.xlsx")));
			arguments.setSheetIndex(0);
			arguments.setFromRowIndex(2);
			arguments.setFromColumnIndex(1);
			List<String[]> list = CommonUtils.getInstance().readExcelSheet(arguments);
			System.out.println("###   Teachers   ###");
			for(String[] line : list)
				System.out.println(StringUtils.join(line," ; "));

			arguments.setWorkbookInputStream(new FileInputStream(new File(directory, "data.xlsx")));
			arguments.setSheetIndex(1);
			arguments.setFromRowIndex(2);
			arguments.setFromColumnIndex(1);
			list = CommonUtils.getInstance().readExcelSheet(arguments);
			System.out.println("###   Students(PK to K3)   ###");
			for(String[] line : list)
				System.out.println(StringUtils.join(line," ; "));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
