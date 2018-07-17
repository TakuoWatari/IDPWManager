package org.myapp.idpwmanager.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.myapp.authentication.data.User;
import org.myapp.idpwmanager.constant.IDPWConst;
import org.myapp.idpwmanager.constant.IDPWConst.DataType;


class OriginalIDPWManager extends IDPWManagerBase {

	private static Map<User, OriginalIDPWManager> INSTANCE_MAP = new HashMap<User, OriginalIDPWManager>();

	/**
	 * インスタンス生成
	 * @param loginUser 実行ユーザー
	 * @return アカウント情報オブジェクト
	 * @throws IOException
	 */
	public static final synchronized IDPWManagerBase getInstance(User loginUser) throws IOException {
		if (!INSTANCE_MAP.containsKey(loginUser)){
			File saveFile = new File(loginUser.getDataDir(),
					IDPWConst.IDPW_SAVE_FILE_NAME);
			INSTANCE_MAP.put(loginUser, new OriginalIDPWManager(saveFile));
		}

		return INSTANCE_MAP.get(loginUser);
	}

	protected OriginalIDPWManager(File saveFile) throws IOException {
		super(saveFile);
	}

	@Override
	DataType getDataType() {
		return DataType.ORIGINAL;
	}
}
