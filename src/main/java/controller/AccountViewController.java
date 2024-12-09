package controller;

import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import bank.PrivateBank;
import bank.Transaction;

import java.io.IOException;

public class AccountViewController {

    @FXML
    private Label accountNameLabel; // Label für den Accountnamen
    @FXML
    private Label balanceLabel; // Label für den Kontostand
    @FXML
    private ListView<Transaction> transactionListView; // ListView für Transaktionen
    @FXML
    private Button backButton; // Button zum Zurückwechseln

    private PrivateBank privateBank; // Instanz der Bank
    private String currentAccount; // Der aktuelle Account
    private ObservableList<Transaction> transactions; // Datenbindung für Transaktionen

    public void initialize(String accountName, PrivateBank bank) {
        // Initialisierung der View
        this.currentAccount = accountName;
        this.privateBank = bank;

        accountNameLabel.setText(accountName);
        updateBalance();
        transactions = FXCollections.observableArrayList(privateBank.getTransactions(accountName));
        transactionListView.setItems(transactions);

        // Kontextmenü für Transaktionen
        transactionListView.setCellFactory(lv -> {
            ListCell<Transaction> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteMenuItem = new MenuItem("Löschen");
            deleteMenuItem.setOnAction(e -> handleDeleteTransaction(cell.getItem()));

            contextMenu.getItems().add(deleteMenuItem);
            cell.textProperty().bind(cell.itemProperty().asString());
            cell.setContextMenu(contextMenu);
            return cell;
        });
    }

    @FXML
    private void handleBackButton() {
        // Szenenwechsel zurück zur MainView
        System.out.println("Zurück zur Hauptansicht");
    }

    private void handleDeleteTransaction(Transaction transaction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Transaktion löschen");
        alert.setHeaderText("Möchten Sie diese Transaktion wirklich löschen?");
        alert.setContentText(transaction.toString());

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                privateBank.removeTransaction(currentAccount, transaction);
                transactions.remove(transaction); // Aktualisiere die ListView
                updateBalance(); // Aktualisiere den Kontostand
            } catch (IOException | AccountDoesNotExistException | TransactionDoesNotExistException e) {
                showError("Fehler beim Löschen", "Transaktion konnte nicht gelöscht werden.");
            }
        }
    }

    private void updateBalance() {
        double balance = privateBank.getAccountBalance(currentAccount);
        balanceLabel.setText(String.format("%.2f €", balance));
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

