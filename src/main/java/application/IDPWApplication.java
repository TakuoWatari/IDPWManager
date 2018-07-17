package application;

import java.net.URL;

import application.util.ApplicationUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class IDPWApplication extends AuthenticationApplication {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public FXMLLoader getLoader() {
		return ApplicationUtil.getFXMLLoader("IDPWManager.fxml");
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	protected boolean isNecessaryAuthentication() {
		return true;
	}

	@Override
	public URL getStyleSheetUrl() {
		return ApplicationUtil.getStyleSheetURL("application.css");
	}

	@Override
	public Image getIcon() {
		return ApplicationUtil.getImage("IDPWManager.bmp");
	}
}
