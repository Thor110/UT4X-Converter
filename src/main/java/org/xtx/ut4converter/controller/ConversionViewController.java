/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtx.ut4converter.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * FXML Controller class
 *
 * @author XtremeXp
 */
@SuppressWarnings("restriction")
public class ConversionViewController implements Initializable {

	@FXML
	private TableView<TableRowLog> convLogTableView;
	@FXML
	private TableColumn<TableRowLog, String> logTime;
	@FXML
	private TableColumn<TableRowLog, Level> logLevel;
	@FXML
	private TableColumn<TableRowLog, String> logMsg;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private ProgressBar progressBarDetail;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private ProgressIndicator progressIndicatorDetail;
	@FXML
	private Label progressMessageDetail;
	@FXML
	private Label progressMessage;

	/**
	 * Initializes the controller class.
	 * 
	 * @param url Url
	 * @param rb Resource Bundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		logTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		logLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
		logMsg.setCellValueFactory(new PropertyValueFactory<>("message"));

		logLevel.setCellFactory(column -> new TableCell<>() {

			@Override
			protected void updateItem(Level item, boolean empty) {

				if (item != null) {
					setText(item.getName());

					if (item == Level.WARNING) {
						setStyle("-fx-background-color: #ff9213"); // orange
					} else if (item == Level.SEVERE) {
						setStyle("-fx-background-color: #ff3333"); // red
					} else if (item == Level.FINE) {
						setStyle("-fx-background-color: #e8e8e8"); // white
					} else {
						setStyle(null);
					}
				}
			}
		});
	}

	public TableView<TableRowLog> getConvLogTableView() {
		return convLogTableView;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public ProgressBar getProgressBarDetail() {
		return progressBarDetail;
	}

	public ProgressIndicator getProgressIndicator() {
		return progressIndicator;
	}

	public ProgressIndicator getProgressIndicatorDetail() {
		return progressIndicatorDetail;
	}

	public Label getProgressMessageDetail() {
		return progressMessageDetail;
	}

	public Label getProgressMessage() {
		return progressMessage;
	}

}