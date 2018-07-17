package org.myapp.idpwmanager.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.myapp.authentication.data.User;
import org.myapp.idpwmanager.constant.IDPWConst.DataType;
import org.myapp.idpwmanager.constant.IDPWMessage;

import application.exception.ApplicationException;
import application.util.CommonUtil;

public class IDPWManager {

	private IDPWManagerBase commonMng;
	private IDPWManagerBase originalMng;

	public IDPWManager(User loginUser) throws IOException {
		commonMng = CommonIDPWManager.getInstance();
		originalMng = OriginalIDPWManager.getInstance(loginUser);
	}

	public IDPW create(DataType dataType, String name, String id, String pw, String comment) throws ApplicationException {
		if (CommonUtil.isEmpty(name)) {
			//ERROR
			throw new ApplicationException(IDPWMessage.E0008);
		}
		if (CommonUtil.isEmpty(id)) {
			//ERROR
			throw new ApplicationException(IDPWMessage.E0001);
		}
		if (CommonUtil.isEmpty(pw)) {
			//ERROR
			throw new ApplicationException(IDPWMessage.E0002);
		}
		if (dataType == null) {
			//ERROR
			throw new IllegalArgumentException(IDPWMessage.E9999);
		}

		IDPW data;
		if (dataType.equals(DataType.COMMON)) {
			data = commonMng.create(name, id, pw, comment);
		} else if (dataType.equals(DataType.ORIGINAL)) {
			data = originalMng.create(name, id, pw, comment);
		} else {
			throw new IllegalArgumentException(IDPWMessage.E9999);
		}

		return data;
	}

	public void delete(IDPW deleteData) {
		commonMng.delete(deleteData);
		originalMng.delete(deleteData);
	}

	public List<IDPW> getDataList() {
		List<IDPW> dataList = new ArrayList<>();
		dataList.addAll(commonMng.getDataList());
		dataList.addAll(originalMng.getDataList());
		return dataList;
	}

	public void commit() throws ApplicationException, IOException {
		commonMng.commit();
		originalMng.commit();
	}
}
