package com.luckytom.patch.util;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public final class AlertUtil {

	public static void showInfoAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(message);
		alert.setHeaderText(null);
		alert.show();
	}
	
	public static void test() {
		DialogPane dialogPane = new DialogPane();
		dialogPane.setVisible(true);
	}

	public static void showWarnAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setContentText(message);
		alert.setHeaderText(null);
		alert.show();
	}

	public static void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(message);
		alert.setHeaderText(null);
		alert.show();
	}

	public static Alert showConfirmationAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText(message);
		alert.setHeaderText(null);
		return alert;
	}
}
