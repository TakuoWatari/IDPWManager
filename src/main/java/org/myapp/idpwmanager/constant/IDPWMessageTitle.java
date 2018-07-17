package org.myapp.idpwmanager.constant;

public enum IDPWMessageTitle {

	INFO("情報"),
	WARN("警告"),
	ERROR("エラー"),
	SYSTEM_ERROR("システムエラー");

	private String title;
	/**
	 * コンストラクタ
	 * ※インスタンス生成不可
	 */
	private IDPWMessageTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}
}
