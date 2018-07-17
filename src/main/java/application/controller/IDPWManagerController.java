package application.controller;

import java.io.IOException;
import java.util.List;

import org.myapp.authentication.constant.AuthenticationConst;
import org.myapp.authentication.data.User;
import org.myapp.idpwmanager.constant.IDPWConst;
import org.myapp.idpwmanager.constant.IDPWMessage;
import org.myapp.idpwmanager.data.IDPW;
import org.myapp.idpwmanager.data.IDPWManager;

import application.constant.Message;
import application.contoller.ControllerBase;
import application.exception.ApplicationException;
import application.model.ApplicationContext;
import application.util.CommonUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;

public class IDPWManagerController extends ControllerBase {

	@FXML
	private Label loginId;
	@FXML
	private Label loginName;

	@FXML
	private TableView<IDPW> idpwTable;

	private ObservableList<IDPW> idpwList;

	@FXML
	public void initialize() throws IOException {

		this.idpwList = FXCollections.observableArrayList();
		this.idpwTable.getSelectionModel().setCellSelectionEnabled(true);
		this.idpwTable.setItems(this.idpwList);
		MenuItem copyItem = new MenuItem("コピー　(Ctrl + C)");
		copyItem.setOnAction(event -> {
			copyProcess();
		});
		MenuItem deleteItem = new MenuItem("削除　(Ctrl + D)");
		deleteItem.setOnAction(event -> {
			deleteProcess();
		});
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(copyItem);
		menu.getItems().add(deleteItem);
		this.idpwTable.setContextMenu(menu);
		this.idpwTable.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
				IDPW targetData = idpwTable.getSelectionModel().getSelectedItem();
				if (targetData != null) {
					showDataBox("変更画面", targetData);
					refreshIDPW();
				}
			}
		});
		this.idpwTable.setOnKeyPressed(event -> {
			if (event.isControlDown() && (event.getCode() == KeyCode.C)) {
				copyProcess();
			} else if (event.isControlDown() && (event.getCode() == KeyCode.D)) {
				deleteProcess();
			} else if (event.getCode() == KeyCode.ENTER) {
				IDPW targetData = idpwTable.getSelectionModel().getSelectedItem();
				if (targetData != null) {
					showDataBox("変更画面", targetData);
					refreshIDPW();
				}
			}
		});

		User loginUser = (User) ApplicationContext.get(AuthenticationConst.CONTEXT_KEY_LOGIN_USER);

		this.loginId.setText(loginUser.getId());
		this.loginName.setText(loginUser.getName());

		ApplicationContext.set(IDPWConst.KEY_VALUE, new IDPWManager(loginUser));

		refreshIDPW();
	}

	private void refreshIDPW() {
		this.idpwList.clear();
		IDPWManager idpwMng = (IDPWManager) ApplicationContext.get(IDPWConst.KEY_VALUE);
		List<IDPW> dataList = idpwMng.getDataList();
		this.idpwList.addAll(dataList);
	}

	public void onClickUserSettingButton() throws IOException {
		UserInfoSettingController.show(this.getStage());

		User loginUser = (User) ApplicationContext.get(AuthenticationConst.CONTEXT_KEY_LOGIN_USER);
		this.loginName.setText(loginUser.getName());
	}

	public void onClickAddDataButton() {
		showDataBox("登録画面", null);
		refreshIDPW();
	}

	public void onClickIDPWTable() {
		IDPW targetData = this.idpwTable.getSelectionModel().getSelectedItem();
		if (targetData == null) {
			this.showErrorMessage(Message.getMessage(IDPWMessage.E0006));
		} else {
			showDataBox("変更画面", targetData);
			refreshIDPW();
		}
	}

	private final void copyProcess() {
		CommonUtil.saveClipboardSelectedTableViewCellData(this.idpwTable);
	}

	private final void deleteProcess() {
		IDPW targetData = this.idpwTable.getSelectionModel().getSelectedItem();
		if (targetData == null) {
			this.showErrorMessage(Message.getMessage(IDPWMessage.E0007));
		} else {
			try {
				IDPWManager idpwMng = (IDPWManager) ApplicationContext.get(IDPWConst.KEY_VALUE);
				idpwMng.delete(targetData);
				idpwMng.commit();
				this.idpwList.remove(targetData);
			} catch (ApplicationException e) {
				this.showErrorMessage(Message.getMessage(IDPWMessage.E0005), e);
			} catch (IOException e) {
				this.showErrorMessage(Message.getMessage(IDPWMessage.E9999), e);
			}
		}
	}

	protected void showDataBox(String title, IDPW targetData) {
		try {
			IDPWDataBoxController.show(this.getStage(), title, targetData);
			refreshIDPW();
		} catch (IOException e) {
			this.showErrorMessage(Message.getMessage(IDPWMessage.E9999), e);
		}
	}
}
