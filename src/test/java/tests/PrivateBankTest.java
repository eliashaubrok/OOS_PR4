package tests;

import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrivateBankTest {
    private PrivateBank bank;
    private OutgoingTransfer o1;
    private IncomingTransfer i1;
    private OutgoingTransfer o2;

    List<Transaction> transactions;

    @BeforeEach
    void init() throws TransactionAttributeException, IOException, AccountAlreadyExistsException {
            bank = new PrivateBank("B1", 0.1, 0.2, "C:\\Users\\elias\\OneDrive\\Dokumente\\FH Aachen-DESKTOP-DA0PU66\\OOS\\PR4\\OOS_PR4\\BankTest/");
            o1 = new OutgoingTransfer("18.11.2023", 100.0, "o1", "u2", "u1");
            i1 = new IncomingTransfer("22.11.2023", 250.0, "i1", "u1", "u2");
            o2 = new OutgoingTransfer("23.10.2023", 1000.0,"o2", "u1", "u3");
            transactions= new ArrayList<>();
            transactions.add(o1);
            transactions.add(i1);
            transactions.add(o2);
    }
    @Test
    @Order(1)
    void testKonstruktor(){
        assertEquals("B1",bank.getname());
        assertEquals(0.1,bank.getIncomingInterest());
        assertEquals(0.2,bank.getOutgoingInterest());
        assertEquals("C:\\Users\\elias\\OneDrive\\Dokumente\\FH Aachen-DESKTOP-DA0PU66\\OOS\\PR4\\OOS_PR4\\BankTest/",bank.getDirectoryName());
    }
    @Test
    @Order(2)
    void testCopyKonstruktor() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {
        PrivateBank banktest = new PrivateBank(bank);
        assertEquals(bank.getname(),banktest.getname());
        assertEquals(bank.getIncomingInterest(),banktest.getIncomingInterest());
        assertEquals(bank.getOutgoingInterest(),banktest.getOutgoingInterest());
        assertEquals(bank.getDirectoryName(),banktest.getDirectoryName());
        assertNotSame(bank, banktest);
    }
    @Test
    @Order(3)
    void testEquals() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, TransactionAttributeException, IOException {
        PrivateBank banktest = new PrivateBank("B1", 0.1, 0.2, "C:\\Users\\elias\\OneDrive\\Dokumente\\FH Aachen-DESKTOP-DA0PU66\\OOS\\PR4\\OOS_PR4\\BankTest/");
        assertTrue(bank.equals(banktest));

    }
    @Test
    @Order(4)
    void testCreateAccount() throws AccountAlreadyExistsException {
        assertThrows(AccountAlreadyExistsException.class,() -> bank.createAccount("u1"));
    }

    //@Test
    //@Order(5)
    //void testCreateAccountWith() throws AccountAlreadyExistsException, TransactionAlreadyExistException, IOException, TransactionAttributeException {
        //bank.createAccount("u2", transactions);
    //    assertTrue(bank.getAccountToTransaction().containsKey("u2"));
    //    assertEquals(transactions, bank.getAccountToTransaction().get("u2"));
    //}

    @Test
    @Order(6)
    void testAddAndContainsTransaction() throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        bank.addTransaction("u5", o1);
        assertTrue(bank.getAccountToTransaction().get("u5").contains(o1));
    }
    @Test
    @Order(7)
    void testRemoveTransaction() throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        bank.removeTransaction("u5",o1);
        assertFalse(bank.getAccountToTransaction().get("u5").contains(o1));
    }
    @Test
    @Order(8)
    void testGetAccountBalance(){
        assertEquals(-850.0,bank.getAccountBalance("u2"));
    }

    @Test
    @Order(9)
    void testGetTransactions(){
        assertEquals(transactions,bank.getTransactions("u2"));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    @Order(10)
    void testGetTransactionsSorted(boolean sorted){
        List<Transaction> tempList = transactions;
        tempList.sort(Comparator.comparing(Transaction::getamount));
        if (!sorted){
            Collections.reverse(tempList);
        }
        assertEquals(tempList, bank.getTransactionsSorted("u2",sorted));
    }
    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    @Order(11)
    void testGetTransactionsByType(boolean positive){
        List<Transaction> tempList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getamount() > 0.0 && positive) {
                tempList.add(transaction);
            } else if (transaction.getamount() <= 0 && !positive) {
                tempList.add(transaction);
            }
        }
        assertEquals(tempList,bank.getTransactionsByType("u2", positive));
    }
}
