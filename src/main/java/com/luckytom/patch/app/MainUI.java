package com.luckytom.patch.app;

import java.io.IOException;
import java.net.URL;

import com.luckytom.patch.constants.Resource;
import com.luckytom.patch.controller.MainUIController;
import com.luckytom.patch.service.PatchService;
import com.luckytom.patch.teamPlugin.util.SvnPatchUtil;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 启动类
 *
 * @author luckytom
 * @version 1.0 2017年11月5日 上午11:54:54
 */
public class MainUI extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL mainUIUrl = classLoader.getResource(Resource.Fxml.MAIN_UI);
		FXMLLoader fxmlLoader = new FXMLLoader(mainUIUrl);
		
		Scene scene = new Scene(fxmlLoader.load());
		URL mainCssUrl = classLoader.getResource(Resource.Css.MAIN_UI);
		scene.getStylesheets().add(mainCssUrl.toExternalForm());
		
		primaryStage.setResizable(true);
		primaryStage.setTitle(Resource.APP_NAME);
		
		URL appIconUrl = classLoader.getResource(Resource.Icon.APP);
		primaryStage.getIcons().add(new Image(appIconUrl.toExternalForm()));
		primaryStage.setScene(scene);
		primaryStage.show();
		
		MainUIController controller = fxmlLoader.getController();
		controller.setPrimaryStage(primaryStage);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				SvnPatchUtil.disposeAllSvnOperationFactory();
				PatchService.shutdownPatchSingleThreadExecutor();
				
				System.exit(0);
			}
		});
	}
	
}
