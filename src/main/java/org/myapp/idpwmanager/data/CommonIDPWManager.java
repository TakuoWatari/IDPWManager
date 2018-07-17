package org.myapp.idpwmanager.data;

import java.io.File;
import java.io.IOException;

import org.myapp.idpwmanager.constant.IDPWConst;
import org.myapp.idpwmanager.constant.IDPWConst.DataType;

class CommonIDPWManager extends IDPWManagerBase {

	private static CommonIDPWManager instace;

	/**
	 * インスタンス生成
	 * @param loginUser 実行ユーザー
	 * @return アカウント情報オブジェクト
	 * @throws IOException
	 */
	public static final synchronized IDPWManagerBase getInstance() throws IOException {
		if (instace == null){
			File saveFile = new File(IDPWConst.COMMON_DATA_SAVE_DIR,
					IDPWConst.IDPW_SAVE_FILE_NAME);
			instace = new CommonIDPWManager(saveFile);
		}

		return instace;
	}

	protected CommonIDPWManager(File saveFile) throws IOException {
		super(saveFile);
	}

	@Override
	DataType getDataType() {
		return DataType.COMMON;
	}
}
