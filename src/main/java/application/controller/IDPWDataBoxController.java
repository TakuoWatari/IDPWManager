package application.controller;

import java.io.IOException;

import org.myapp.idpwmanager.constant.IDPWConst;
import org.myapp.idpwmanager.constant.IDPWConst.DataType;
import org.myapp.idpwmanager.constant.IDPWMessage;
import org.myapp.idpwmanager.data.IDPW;
import org.myapp.idpwmanager.data.IDPWManager;

import application.constant.Message;
import application.content.PopupWindow;
import application.contoller.ControllerBase;
import application.exception.ApplicationException;
import application.model.ApplicationContext;
import application.util.ApplicationUtil;
import application.util.CommonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class IDPWDataBoxController extends ControllerBase {

	private static PopupWindow dialog;

	public static final synchronized void show(Window owner, String title, IDPW targetData) throws IOException {
		setup(owner, title);

		IDPWDataBoxController controller = (IDPWDataBoxController) dialog.getController();

		controller.setIDPWManager((IDPWManager) ApplicationContext.get(IDPWConst.KEY_VALUE));
		controller.setTargetIDPW(targetData);

		dialog.show();
	}

	private static void setup(Window owner, String title) throws IOException {
		if (dialog == null) {
			dialog =new PopupWindow(
					owner,
					StageStyle.DECORATED,
					title,
					"IDPWDataBox.fxml",
					Modality.WINDOW_MODAL,
					false,
					false,
					ApplicationUtil.getStyleSheetURL("application.css").toExternalForm());
		} else {
			dialog.setTitle(title);
		}
	}

	@FXML
	private ToggleGroup dataTypeGroup;
	@FXML
	private RadioButton originalType;
	@FXML
	private RadioButton commonType;

	@FXML
	private TextField nameField;
	@FXML
	private TextField idField;
	@FXML
	private TextField pwField;
	@FXML
	private TextArea commentArea;

	@FXML
	private Button registrationButton;
	@FXML
	private Button updateButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button cancelButton;

	private IDPWManager idpwMng;

	private IDPW targetIDPW;

	@FXML
	public void initialize() {
		this.targetIDPW = null;
		this.originalType.setSelected(true);
		this.commonType.setSelected(false);

		this.nameField.setText(null);
		this.idField.setText(null);
		this.pwField.setText(null);
		this.commentArea.setText(null);

		this.registrationButton.setVisible(true);
		this.updateButton.setVisible(false);
		this.deleteButton.setVisible(false);
	}

	public void setTargetIDPW(IDPW targetIDPW) {

		initialize();

		this.targetIDPW = targetIDPW;
		if (targetIDPW != null) {
			if (targetIDPW.getDataType().equals(DataType.COMMON)) {
				this.originalType.setSelected(false);
				this.commonType.setSelected(true);
			} else {
				this.originalType.setSelected(true);
				this.commonType.setSelected(false);
			}

			this.nameField.setText(targetIDPW.getName());
			this.idField.setText(targetIDPW.getAccountId());
			this.pwField.setText(targetIDPW.getAccountPw());
			this.commentArea.setText(targetIDPW.getComment());

			this.registrationButton.setVisible(false);
			this.updateButton.setVisible(true);
			this.deleteButton.setVisible(true);
		}
	}

	public void setIDPWManager(IDPWManager idpwMng) {
		this.idpwMng = idpwMng;
	}

	public void onClickRegistrationButton() {

		try {
			String name = this.nameField.getText();
			String id = this.idField.getText();
			String pw = this.pwField.getText();
			String comment = this.commentArea.getText();

			DataType dataType;
			if (this.commonType.isSelected()) {
				dataType = DataType.COMMON;
			} else if (this.originalType.isSelected()) {
				dataType = DataType.ORIGINAL;
			} else {
				showErrorMessage(Message.getMessage(IDPWMessage.E9999));
				return;
			}

			this.idpwMng.create(dataType, name, id, pw, comment);
			this.idpwMng.commit();
			this.close();

		} catch (ApplicationException e) {
			showErrorMessage(Message.getMessage(IDPWMessage.E0003), e);
		} catch (Exception e) {
			showSystemErrorMessage(Message.getMessage(IDPWMessage.E9999), e);
		}
	}

	public void onClickUpdateButton() {
		try {
			if (this.targetIDPW == null) {
				throw new ApplicationException(IDPWMessage.E0006);
			}

			String name = this.nameField.getText();
			if (CommonUtil.isEmpty(name)) {
				throw new ApplicationException(IDPWMessage.E0008);
			}
			String id = this.idField.getText();
			if (CommonUtil.isEmpty(id)) {
				throw new ApplicationException(IDPWMessage.E0001);
			}
			String pw = this.pwField.getText();
			if (CommonUtil.isEmpty(pw)) {
				throw new ApplicationException(IDPWMessage.E0002);
			}
			String comment = this.commentArea.getText();

			DataType dataType;
			if (this.commonType.isSelected()) {
				dataType = DataType.COMMON;
			} else if (this.originalType.isSelected()) {
				dataType = DataType.ORIGINAL;
			} else {
				this.showErrorMessage(Message.getMessage(IDPWMessage.E9999));
				return;
			}

			DataType targetDataType = this.targetIDPW.getDataType();
			if (dataType.equals(targetDataType)) {
				// 区分に変更がない場合
				this.targetIDPW.setName(name);
				this.targetIDPW.setAccountId(id);
				this.targetIDPW.setAccountPw(pw);
				this.targetIDPW.setComment(comment);
			} else {
				// 区分を変更する場合場合
				this.idpwMng.delete(this.targetIDPW);
				this.idpwMng.create(dataType, name, id, pw, comment);
			}
			this.idpwMng.commit();
			this.close();

		} catch (ApplicationException e) {
			this.showErrorMessage(Message.getMessage(IDPWMessage.E0004), e);
		} catch (Exception e) {
			this.showErrorMessage(Message.getMessage(IDPWMessage.E9999), e);
		}
	}

	public void onClickDeleteButton() {
		try {
			if (this.targetIDPW == null) {
				throw new ApplicationException(IDPWMessage.E0007);
			}
			this.idpwMng.delete(this.targetIDPW);
			this.idpwMng.commit();

			this.close();

		} catch (ApplicationException e) {
			this.showErrorMessage(Message.getMessage(IDPWMessage.E0004), e);
		} catch (Exception e) {
			this.showErrorMessage(Message.getMessage(IDPWMessage.E9999), e);
		}
	}

	public void onClickCancelButton() {
		this.close();
	}
}
