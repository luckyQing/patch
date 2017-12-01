package com.luckytom.patch.app;

import java.io.IOException;
import java.net.URL;

import com.luckytom.patch.constants.Constants;
import com.luckytom.patch.controller.MainUIController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 启动类
 *
 * @author liyulin
 * @version 1.0 2017年11月5日 上午11:54:54
 */
public class MainUI extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL mainUIUrl = classLoader.getResource(Constants.Fxml.MAIN_UI);
		FXMLLoader fxmlLoader = new FXMLLoader(mainUIUrl);
		
		Scene scene = new Scene(fxmlLoader.load());
		URL mainCssUrl = classLoader.getResource(Constants.Css.MAIN_UI);
		scene.getStylesheets().add(mainCssUrl.toExternalForm());
		
		primaryStage.setResizable(true);
		primaryStage.setTitle(Constants.APP_NAME);
		
		URL appIconUrl = classLoader.getResource(Constants.Icon.APP);
		primaryStage.getIcons().add(new Image(appIconUrl.toExternalForm()));
		primaryStage.setScene(scene);
		primaryStage.show();
		
		MainUIController controller = fxmlLoader.getController();
		controller.setPrimaryStage(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
