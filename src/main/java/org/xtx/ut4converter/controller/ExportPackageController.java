package org.xtx.ut4converter.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.xtx.ut4converter.config.ApplicationConfig;
import org.xtx.ut4converter.tools.PackageExporterService;
import org.xtx.ut4converter.tools.UIUtils;
import org.xtx.ut4converter.ucore.UnrealGame;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.xtx.ut4converter.config.ApplicationConfig.loadApplicationConfig;

public class ExportPackageController implements Initializable {

    public static final String EXPORTER_EPIC_GAMES = "Epic Games";

    public static final String EXPORTER_UMODEL = "Umodel";

    @FXML
    public TextField exportFolder;
    public ComboBox<String> pkgExtractorCbBox;
    public TextArea logContentTxtArea;

    @FXML
    public Button stopExportBtn;
    /**
     * Package to export ressources
     */
    private File unrealPakFile;

    private File outputFolder;

    @FXML
    private TextField unrealPakPath;

    @FXML
    private Button convertBtn;

    private ApplicationConfig applicationConfig;

    @FXML
    private ComboBox<UnrealGame> unrealGamesList;

    @FXML
    private Label progressIndicatorLbl;

    private PackageExporterService pkgExporterService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            stopExportBtn.setVisible(false);

            this.pkgExtractorCbBox.getItems().add(EXPORTER_EPIC_GAMES);
            this.pkgExtractorCbBox.getItems().add(EXPORTER_UMODEL);

            this.pkgExtractorCbBox.getSelectionModel().select(EXPORTER_UMODEL);
            this.applicationConfig = loadApplicationConfig();

            this.applicationConfig.getGames().forEach(g -> {
                if (!g.isDisabled()) {
                    unrealGamesList.getItems().add(g);
                }
            });

            unrealGamesList.setConverter(new StringConverter<>() {

                @Override
                public String toString(UnrealGame object) {
                    if (object == null) return null;
                    return object.getName();
                }

                @Override
                public UnrealGame fromString(String string) {
                    if (string != null) {
                        return applicationConfig.getGames().stream().filter(g -> g.getShortName().equals(string)).findFirst().orElse(null);
                    } else {
                        return null;
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectPackage() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select unreal package");

        final UnrealGame selectedGame = this.unrealGamesList.getSelectionModel().getSelectedItem();

        if (selectedGame != null) {
            chooser.setInitialDirectory(selectedGame.getPath());

            final Set<String> extList = new LinkedHashSet<>();
            extList.add("*." + selectedGame.getMapExt());
            extList.add("*." + selectedGame.getSoundExt());
            extList.add("*." + selectedGame.getTexExt());
            extList.add("*." + selectedGame.getMusicExt());

            // staticmeshes - ut2003/4
            if (selectedGame.getUeVersion() == 2) {
                extList.add("*.usx");
            }

            // Prefab - unreal 2
            if (selectedGame.getUeVersion() == 2) {
                extList.add("*.upx");
            }

            if (selectedGame.getUeVersion() <= 3) {
                extList.add("*.u");
            }

            if (selectedGame.getUeVersion() >= 4) {
                extList.add("*.pak");
            }

            extList.remove("*.null");

            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Unreal package", extList.toArray(new String[0])));
        } else {
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Unreal package", "*.unr", "*.un2", "*.ut2", "*.ut3", "*.utx", "*.uax", "*.usx", "*.dtx", "*.umx", "*.upx"));
        }


        this.unrealPakFile = chooser.showOpenDialog(new Stage());

        if (this.unrealPakFile != null) {
            this.unrealPakPath.setText(this.unrealPakFile.getAbsolutePath());
            this.convertBtn.setDisable(this.outputFolder == null);
        }
    }

    public void exportPackage() {

        final UnrealGame selectedGame = this.unrealGamesList.getSelectionModel().getSelectedItem();

        if (selectedGame.getPath() == null || !selectedGame.getPath().exists()) {
            showError("Game path for " + selectedGame.getName() + " is not set in settings.");
            return;
        }

        if (this.outputFolder == null) {
            showError("Select output folder.");
            return;
        }

        // .pak files can only be extracted with UCC
        if (this.unrealPakFile.getName().endsWith(".pak")) {
            this.pkgExtractorCbBox.getSelectionModel().select(EXPORTER_EPIC_GAMES);
        }
        // uasset files can only be extracted with umodel
        else if (this.unrealPakFile.getName().endsWith(".uasset")) {
            this.pkgExtractorCbBox.getSelectionModel().select(EXPORTER_UMODEL);
        }

        this.pkgExporterService = new PackageExporterService(pkgExtractorCbBox.getSelectionModel().getSelectedItem(), selectedGame, this.outputFolder, this.unrealPakFile);

        pkgExporterService.setOnSucceeded(t -> {
            pkgExporterService.reset();
            displayLogs(pkgExporterService);
            progressIndicatorLbl.setText("All done !");
            UIUtils.openExplorer(this.outputFolder);
            convertBtn.setDisable(false);
            stopExportBtn.setVisible(false);
        });

        pkgExporterService.setOnFailed(t -> {
            pkgExporterService.reset();
            displayLogs(pkgExporterService);
            progressIndicatorLbl.setText("Error !");
            convertBtn.setDisable(false);
            stopExportBtn.setVisible(false);
        });

        pkgExporterService.setOnCancelled(t -> {
            pkgExporterService.reset();
            progressIndicatorLbl.setText("Cancelled");
            convertBtn.setDisable(false);
            stopExportBtn.setVisible(false);
        });

        this.logContentTxtArea.setText("");
        stopExportBtn.setVisible(true);
        convertBtn.setDisable(true);
        pkgExporterService.start();
        progressIndicatorLbl.setText("Please wait ...");
    }

    private void displayLogs(PackageExporterService pkgExporterService) {
        StringBuilder sb = new StringBuilder();

        try {
            for (String log : pkgExporterService.getTask().get()) {
                sb.append(log).append("\n");
            }

            this.logContentTxtArea.setText(sb.toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public void selectFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select output folder");

        this.outputFolder = chooser.showDialog(new Stage());

        if (this.outputFolder != null) {
            exportFolder.setText(this.outputFolder.getAbsolutePath());
            this.convertBtn.setDisable(this.unrealPakFile == null);
        }
    }

    public void stopExport() {
        this.pkgExporterService.cancel();
    }
}
