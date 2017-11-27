package com.luckytom.patch.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.luckytom.patch.model.SettingDO;
import com.luckytom.patch.model.TeamPluginType;
import com.luckytom.patch.service.PatchService;
import com.luckytom.patch.util.AlertUtil;
import com.luckytom.patch.util.ConsoleUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainUIController implements Initializable {

	@FXML
	private ChoiceBox<String> teamPluginTypeChoiceBox;
	@FXML
	private RadioMenuItem logSwitchRadioMenuItem;
	@FXML
	private TextArea logTextArea;
	@FXML
	private GridPane configGridPane;
	@FXML
	private TextField serverUrlTextField;
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField localProjectPathTextField;
	@FXML
	private TextField patchPathTextField;
	@FXML
	private TextArea excludeFilesTextArea;
	private SettingDO settingDO;
	private Stage primaryStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fixLogTextArea();
		ConsoleUtil.setLogTextArea(logTextArea);

		ObservableList<String> teamPluginTypeObservableList = FXCollections
				.observableArrayList(TeamPluginType.getTeamPluginTypes());
		teamPluginTypeChoiceBox.setItems(teamPluginTypeObservableList);
		teamPluginTypeChoiceBox.setValue(TeamPluginType.SVN.getName());

		settingDO = new SettingDO();
	}
	
	public void fixLogTextArea() {
		
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void onExportFile() {

	}

	public void onExit() {
		System.exit(0);
	}

	public void onLogSwitch() {
		String logSwitchRadioMenuItemText = "";
		if (logSwitchRadioMenuItem.isSelected()) {
			logSwitchRadioMenuItemText = "日志开关（开）";
		} else {
			logSwitchRadioMenuItemText = "日志开关（关）";
		}
		logSwitchRadioMenuItem.setText(logSwitchRadioMenuItemText);
		settingDO.setLogSwitch(logSwitchRadioMenuItem.isSelected());
	}

	public void onChangeConfig() {

	}

	public void onAbout() {
		AlertUtil.test();
	}

	public void onSelectLocalProjectPath() {
		setDirectoryChooserPath(localProjectPathTextField);
	}

	public void onSelectPatchPath() {
		setDirectoryChooserPath(patchPathTextField);
	}

	public void onGeneratePatch() {
		String apiPath = "E:/github/demo/SpringSecuritySimpleDemo";

//		List<String> dependencyProjectList = new ArrayList<>();
//		dependencyProjectList.add("E:\\jfq\\code\\V520_for_deploy\\server_common");
//		dependencyProjectList.add("E:\\jfq\\code\\V520_for_deploy\\domain_common");
//		dependencyProjectList.add("E:\\jfq\\code\\V520_for_deploy\\commbusi_api");
		
		PatchService.generatePatch(apiPath, null);
	}

	public void onSaveConfig() {

	}

	private boolean ConfigValidate() {
		return false;
	}

	private void setDirectoryChooserPath(TextField textField) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedFolder = directoryChooser.showDialog(primaryStage);
		if (selectedFolder != null) {
			textField.setText(selectedFolder.getAbsolutePath());
		}
	}
}
