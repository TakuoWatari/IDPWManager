package org.myapp.idpwmanager.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.myapp.idpwmanager.constant.IDPWConst;
import org.myapp.idpwmanager.constant.IDPWConst.DataType;
import org.myapp.idpwmanager.constant.IDPWMessage;

import application.dao.BaseDAO;
import application.util.CipherManager;
import application.util.CommonUtil;

abstract class IDPWManagerBase extends BaseDAO<IDPW> {

	private static CipherManager cipherMng;

	private static synchronized CipherManager getChipherMng() {
		if (cipherMng == null) {
			cipherMng = new CipherManager(IDPWConst.KEY_VALUE);
		}
		return cipherMng;
	}

	protected IDPWManagerBase(File saveFile) throws IOException {
		super(saveFile);
		load();
	}

	abstract DataType getDataType();

	public IDPW create(String name, String id, String pw, String comment) {
		if (CommonUtil.isEmpty(name)) {
			//ERROR
			throw new IllegalArgumentException(IDPWMessage.E0008);
		}
		if (CommonUtil.isEmpty(id)) {
			//ERROR
			throw new IllegalArgumentException(IDPWMessage.E0001);
		}
		if (CommonUtil.isEmpty(pw)) {
			//ERROR
			throw new IllegalArgumentException(IDPWMessage.E0002);
		}

		String privateKey = UUID.randomUUID().toString();

		IDPW data = new IDPW(privateKey, this.getDataType(), name, id, pw, comment);
		this.insert(data);

		return data;
	}

	public void delete(IDPW deleteData) {
		if (deleteData != null) {
			super.delete(deleteData);
		}
	}

	public List<IDPW> getDataList() {
		Collection<IDPW> values = this.getDataValues();
		List<IDPW> dataList = new ArrayList<>();
		dataList.addAll(values);
		return dataList;
	}

	@Override
	protected IDPW parse(String data) {
		String[] datas = data.split(IDPWConst.DATA_SEPARATOR);

		if (datas.length != IDPWConst.IDPW_DATA_COLUMN) {
			throw new IllegalStateException("データ保存用ファイルの読み込みに失敗しました。");
		}

		CipherManager cipherMng = getChipherMng();
		String name = cipherMng.decrypt(datas[0]);
		String id = cipherMng.decrypt(datas[1]);
		String pw = cipherMng.decrypt(datas[2]);
		String comment = datas[3];
		if (comment != null) {
			comment = cipherMng.decrypt(CommonUtil.parseNullValue(
					comment, IDPWConst.NULL_FORMAT_VALUE));
		}
		String privateKey = datas[4];

		IDPW dataObj = new IDPW(privateKey, this.getDataType(), name, id, pw, comment);
		return dataObj;
	}

	@Override
	protected String format(IDPW data) {
		StringBuilder buf = new StringBuilder();

		CipherManager cipherMng = getChipherMng();
		buf.append(cipherMng.encrypt(data.getName()));
		buf.append(IDPWConst.DATA_SEPARATOR);
		buf.append(cipherMng.encrypt(data.getAccountId()));
		buf.append(IDPWConst.DATA_SEPARATOR);
		buf.append(cipherMng.encrypt(data.getAccountPw()));
		buf.append(IDPWConst.DATA_SEPARATOR);
		String comment = CommonUtil.formatNullValue(
				cipherMng.encrypt(data.getComment()), IDPWConst.NULL_FORMAT_VALUE);
		comment = comment.replaceAll("\\r", "");
		comment = comment.replaceAll("\\n", "");
		buf.append(comment);
		buf.append(IDPWConst.DATA_SEPARATOR);
		buf.append(data.getKey());

		return buf.toString();
	}

	@Override
	public void commit() throws IOException {
		this.save();
	}
}
