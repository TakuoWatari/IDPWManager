package org.myapp.idpwmanager.data;

import org.myapp.idpwmanager.constant.IDPWConst.DataType;

import application.model.Data;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IDPW implements Data {

	/** 内部管理用キー値 **/
	private String id;
	/** 内部管理用キー値 **/
	private String key;
	/** データタイプ **/
	private DataType type;
	/** データタイプ(表示用) **/
	private StringProperty dataType = new SimpleStringProperty();
	/** 名称 **/
	private StringProperty name = new SimpleStringProperty();
	/** ID **/
	private StringProperty accountId = new SimpleStringProperty();
	/** Password **/
	private StringProperty accountPw = new SimpleStringProperty();
	/** 備考 **/
	private StringProperty comment = new SimpleStringProperty();

	IDPW(String key, DataType dataType, String name, String accountId, String accountPw, String comment) {
		this.id = name + "-" + key;
		this.key = key;
		this.type = dataType;
		this.dataType.set(dataType.toString());
		this.name.set(name);
		this.accountId.set(accountId);
		this.accountPw.set(accountPw);
		this.comment.set(comment);
	}

	@Override
	public String getId() {
		return this.id;
	}

	public String getKey() {
		return this.key;
	}

	public DataType getDataType() {
		return this.type;
	}

	public String getName() {
		return this.name.get();
	}

	public String getAccountId() {
		return this.accountId.get();
	}

	public String getAccountPw() {
		return this.accountPw.get();
	}

	public String getComment() {
		return this.comment.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setAccountId(String accountId) {
		this.accountId.set(accountId);
	}

	public void setAccountPw(String accountPw) {
		this.accountPw.set(accountPw);
	}

	public void setComment(String comment) {
		this.comment.set(comment);
	}

	public StringProperty getDataTypeProperty() {
		return this.dataType;
	}

	public StringProperty getAccountIdProperty() {
		return this.accountId;
	}

	public StringProperty getAccountPwProperty() {
		return this.accountPw;
	}

	public StringProperty getCommentProperty() {
		return this.comment;
	}

	@Override
	public void disable() {
	}
}
