package controller;

import bank.*;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
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
    @FXML
    private Button newTransactionButton; // Button zum Erstellen einer neuen Transaction
    @FXML
    private Button aufsteigendButton; // Button zur aufsteigenden Sortierung der Transactions
    @FXML
    private Button absteigendButton; // Button zur absteigenden Sortierung der Transactions
    @FXML
    private Button positiveButton; // Button zum Anzeigen von nur positiven Transactions
    @FXML
    private Button negativeButton; // Button zum Anzeigen von nur negativen Transactions

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
    private void handleBackButton() throws IOException, AccountAlreadyExistsException, TransactionAttributeException {
        // Szenenwechsel zurück zur MainView
        // Lade die FXML-Datei für die MainView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        Parent MainView = loader.load();

        // Holt den MainViewController und initalisiert ihn
        MainViewController controller = loader.getController();
        controller.initialize();

        // Erstelle eine neue Szene mit der MainView
        Stage stage = (Stage) transactionListView.getScene().getWindow(); // Aktuelle Stage
        stage.setScene(new Scene(MainView));
        System.out.println("Wechsel zur Main View");
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

    @FXML
    private void handleAscendingSort() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsSorted(currentAccount, true));
        transactionListView.setItems(transactions);
    }

    @FXML
    private void handleDescendingSort() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsSorted(currentAccount, false));
        transactionListView.setItems(transactions);
    }

    @FXML
    private void handlePositiveAmounts() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsByType(currentAccount, true));
        transactionListView.setItems(transactions);
    }

    @FXML
    private void handleNegativeAmounts() {
        transactions = FXCollections.observableArrayList(privateBank.getTransactionsByType(currentAccount, false));
        transactionListView.setItems(transactions);
    }
    @FXML
    private void handleAddTransaction() {
        Optional<Transaction> result = TransactionDialog.show(currentAccount);

        result.ifPresent(transaction -> {
            try {
                privateBank.addTransaction(currentAccount, transaction);
                transactionListView.getItems().add(transaction); // Aktualisiere die ListView
                updateBalance(); // Kontostand aktualisieren
            } catch (Exception e) {
                showError("Fehler beim Hinzufügen", "Die Transaktion konnte nicht hinzugefügt werden.");
            }
        });
    }

    public class TransactionDialog {

        public static Optional<Transaction> show(String currentAcc) {
            // Dialog erstellen
            Dialog<Transaction> dialog = new Dialog<>();
            dialog.setTitle("Neue Transaktion");
            dialog.setHeaderText("Erstellen Sie eine neue Transaktion");
            // Buttons hinzufügen
            ButtonType addButtonType = new ButtonType("Hinzufügen", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            // Layout erstellen
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            // Eingabefelder
            ComboBox<String> typeBox = new ComboBox<>();
            typeBox.getItems().addAll("Payment", "Incoming Transfer", "Outgoing Transfer");

            TextField dateField = new TextField();
            dateField.setPromptText("Datum (DD.MM.YYYY)");

            TextField amountField = new TextField();
            amountField.setPromptText("Betrag (z. B. 100 oder -50)");

            TextField descriptionField = new TextField();
            descriptionField.setPromptText("Name der Transaktion");

            TextField senderField = new TextField();
            senderField.setPromptText("Sender (nur für Transfer)");

            TextField receiverField = new TextField();
            receiverField.setPromptText("Receiver (nur für Transfer)");

            // Layout befüllen
            grid.add(new Label("Typ:"), 0, 0);
            grid.add(typeBox, 1, 0);
            grid.add(new Label("Datum:"), 0, 1);
            grid.add(dateField, 1, 1);
            grid.add(new Label("Betrag:"), 0, 2);
            grid.add(amountField, 1, 2);
            grid.add(new Label("Beschreibung:"), 0, 3);
            grid.add(descriptionField, 1, 3);
            grid.add(new Label("Sender:"), 0, 4);
            grid.add(senderField, 1, 4);
            grid.add(new Label("Receiver:"), 0, 5);
            grid.add(receiverField, 1, 5);

            // Dynamische Aktivierung der Felder für Transfers
            typeBox.valueProperty().addListener((obs, oldValue, newValue) -> {
                if ("Payment".equals(newValue)) {
                    senderField.setDisable(true);
                    receiverField.setDisable(true);
                }
                else {
                    if ("Outgoing Transfer".equals(newValue)) {
                        senderField.setText(currentAcc);
                        senderField.setDisable(true);
                    }
                    else {
                        receiverField.setText(currentAcc);
                        receiverField.setDisable(true);
                    }
                }
            });

            dialog.getDialogPane().setContent(grid);

            // Ergebnisverarbeitung
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    if (typeBox.getValue() == null || dateField.getText().isEmpty() || amountField.getText().isEmpty() || descriptionField.getText().isEmpty()){
                        showError("Ungültige Eingabe", "Es müssen alle Felder ausgefüllt werden");
                    }
                    else {
                        try {
                            String date = dateField.getText();
                            double amount = Double.parseDouble(amountField.getText());
                            String description = descriptionField.getText();

                            if (typeBox.getValue().equals("Payment")) {
                                return new Payment(date, amount, description);
                            } else {
                                if (senderField.getText().isEmpty() || receiverField.getText().isEmpty()) {
                                    showError("Ungültige Eingabe", "Es müssen alle Felder ausgefüllt werden");
                                }
                                else {
                                    String sender = senderField.getText();
                                    String receiver = receiverField.getText();
                                    if (typeBox.getValue().equals("Outgoing Transfer")) {
                                        return new OutgoingTransfer(date, amount, description, sender, receiver);
                                    } else {
                                        return new IncomingTransfer(date, amount, description, sender, receiver);
                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {
                            showError("Ungültiger Betrag", "Der Betrag muss eine Zahl sein.");
                        } catch (TransactionAttributeException e) {
                            showError("Ungültige Eingabe", e.getMessage());
                        }
                    }
                }
                return null;
            });
            return dialog.showAndWait();
        }

        private static void showError(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
}

