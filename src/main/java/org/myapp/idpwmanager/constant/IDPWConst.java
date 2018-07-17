package org.myapp.idpwmanager.constant;

import java.io.File;

public class IDPWConst {

	public static final File DATA_SAVE_DIR = new File("data");

	public static final File COMMON_DATA_SAVE_DIR = new File(
			DATA_SAVE_DIR, "common");

	public static final String KEY_VALUE = "Key:IDPW-Manager";

	public static final String IDPW_SAVE_FILE_NAME = "idpw.dat";

	public static final String DATA_SEPARATOR = "\t";

	public static final int IDPW_DATA_COLUMN = 5;

	public static final String NULL_FORMAT_VALUE = "";

	public static enum DataType{
		COMMON("共通"),
		ORIGINAL("個人");

		private String name;
		private DataType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	};
}
