package controller;

import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAttributeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import bank.PrivateBank;

import java.io.IOException;

public class MainViewController {

    @FXML
    private ListView<String> accountListView; // ListView für Accounts
    @FXML
    private Button createAccountButton; // Button zum Erstellen neuer Accounts

    private PrivateBank privateBank; // Instanz der PrivateBank
    private ObservableList<String> accounts; // Datenbindung für ListView

    public void initialize() throws AccountAlreadyExistsException, TransactionAttributeException, IOException {
        // Initialisierung der Bank und der ListView
        privateBank = new PrivateBank("B1", 0.1, 0.2, "C:\\Users\\elias\\OneDrive\\Dokumente\\FH Aachen-DESKTOP-DA0PU66\\OOS\\PR4\\OOS_PR4\\BankTest/");
        privateBank.createAccount("u1");
        accounts = FXCollections.observableArrayList(privateBank.getAllAccounts());
        accountListView.setItems(accounts);

        // Hinzufügen des Kontextmenüs für die ListView
        accountListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem selectMenuItem = new MenuItem("Auswählen");
            selectMenuItem.setOnAction(e -> handleSelectAccount(cell.getItem()));

            MenuItem deleteMenuItem = new MenuItem("Löschen");
            deleteMenuItem.setOnAction(e -> handleDeleteAccount(cell.getItem()));

            contextMenu.getItems().addAll(selectMenuItem, deleteMenuItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.setContextMenu(contextMenu);
            return cell;
        });
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Neuen Account erstellen");
        dialog.setHeaderText("Geben Sie den Namen des neuen Accounts ein:");
        dialog.setContentText("Accountname:");

        dialog.showAndWait().ifPresent(accountName -> {
            try {
                privateBank.createAccount(accountName);
                accounts.add(accountName); // Aktualisiere die ListView
            } catch (IOException | AccountAlreadyExistsException e) {
                showError("Fehler beim Erstellen", "Account konnte nicht erstellt werden.");
            }
        });
    }

    private void handleSelectAccount(String accountName) {
        // Szenenwechsel zu AccountView
        // Szenenwechsel-Logik wird hier implementiert
        System.out.println("Account ausgewählt: " + accountName);
    }

    private void handleDeleteAccount(String accountName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Account löschen");
        alert.setHeaderText("Möchten Sie diesen Account wirklich löschen?");
        alert.setContentText("Account: " + accountName);

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                privateBank.deleteAccount(accountName);
                accounts.remove(accountName); // Aktualisiere die ListView
            } catch (IOException | AccountDoesNotExistException e) {
                showError("Fehler beim Löschen", "Account konnte nicht gelöscht werden.");
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

