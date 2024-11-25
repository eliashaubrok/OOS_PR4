package tests;

import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {

    Transfer t1;
    Transfer t2;
    @BeforeEach
    void iniMethode() throws TransactionAttributeException {
        t1 = new Transfer("18.11.2023", 100.0, "t1", "u1", "u2");
        t2 = new Transfer("31.12.2022", 10.0, "t2", "u1", "u2");
    }
    @Test
    void testKonstruktor(){
        assertEquals("18.11.2023", t1.getdate());
        assertEquals(100.0, t1.getamount());
        assertEquals("t1", t1.getdescription());
        assertEquals("u1", t1.getsender());
        assertEquals("u2", t1.getrecipient());
    }
    @Test
    void testCopyKonstruktor(){
        Transfer t3 = new Transfer(t1);
        assertEquals(t1,t3);
        assertNotSame(t1,t3);
    }
    @Test
    void testCalculateIncoming(){
        IncomingTransfer i1 = new IncomingTransfer(t1);
        assertEquals(100.0, i1.calculate());
    }
    @Test
    void testCalculateOutgoin(){
        OutgoingTransfer o1 = new OutgoingTransfer(t2);
        assertEquals(-10.0, o1.calculate());
    }
    @Test
    void testEquals() throws TransactionAttributeException{

        Transfer t3 = new Transfer("18.11.2023", 100.0, "t1", "u1", "u2");
        assertTrue(t1.equals(t3));
    }
    @Test
    void testToString(){
        String expectedString = "\n Date: 18.11.2023\n Amount: 100.0\n Description: t1\n Sender: u1\n Recepient: u2\n";
        assertEquals(expectedString,t1.toString());
    }
}
