import bank.*;
import bank.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws TransactionAttributeException, AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        PrivateBank bank;
        bank = new PrivateBank("B1", 0.1, 0.2, "C:\\Users\\elias\\OneDrive\\Dokumente\\FH Aachen-DESKTOP-DA0PU66\\OOS\\PR4\\OOS_PR4\\BankTest/");
        bank.createAccount("u1");
    }
}