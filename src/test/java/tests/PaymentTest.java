package tests;

import bank.Payment;
import bank.exceptions.IntrestException;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    private Payment p1;
    private Payment p2;

    @BeforeEach
    void iniMethode() throws TransactionAttributeException {
        p1 = new Payment("26.11.2023", 100.0, "p1", 0.1, 0.2);
        p2 = new Payment("27.11.2023", 50.0, "p2", 0.12, 0.21);
    }

    @Test
    void testKonstruktor() {
        assertEquals("26.11.2023", p1.getdate());
        assertEquals(100.0, p1.getamount());
        assertEquals("p1", p1.getdescription());
        assertEquals(0.1, p1.getIncomingIntrest());
        assertEquals(0.2, p1.getOutgoingIntrest());
    }

    @Test
    void testCopyKonstruktor() throws TransactionAttributeException {
        Payment p3 = new Payment(p1);
        assertEquals(p1,p3);
        // Verify that the copied object is a new object and not the same reference
        assertNotSame(p1, p3);
    }

    @Test
    void testCalculate() {
        assertEquals(90.0, p1.calculate());
    }

    @Test
    void testEquals() throws TransactionAttributeException {
        Payment p3 = new Payment("26.11.2023", 100.0, "p3", 0.1, 0.2);
        Payment p4 = new Payment("26.11.2023", 100.0, "p3", 0.1, 0.2);

        assertTrue(p3.equals(p4));
    }

    @Test
    void testToString() {
        String expectedString = "\n Date: 26.11.2023\n Amount: 90.0\n Description: p1\n IncomingIntrest: 0.1\n OutgoingIntrest: 0.2\n";
        assertEquals(expectedString, p1.toString());
    }
}


