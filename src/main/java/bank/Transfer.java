package bank;

import bank.exceptions.*;

/**
 *the Transfer class handles transactions between two party's with no interest
 */
public class Transfer extends Transaction{	
	/**
	 * the sender of the transfer
	 */
	protected String sender;
	/**
	 * the recipient of the transfer
	 */
	protected String recipient;

	/**
	 *sets amount if positive or 0, otherwise there is a message on the console
	 *@param xamount (double)
	 */
	@Override
	public void setamount(double xamount) throws  TransferAmountAttributeException{
		if (xamount >= 0) {
			amount = xamount;
		}
		else {
			throw new TransferAmountAttributeException();
		}
	}
	/**
	 * returns sender attribute
	 * @return sender (String)
	 */
	public String getsender() {return sender;	}
	/**
	 * sets sender attribute
	 * @param xsender (String)
	 */
	public void setsender(String xsender) {sender = xsender;}
	/**
	 * returns recipient attribute
	 * @return recipient (String)
	 */
	public String getrecipient() {return recipient;}
	/**sets recipient attribute
	 * @param xrecipient (String)
	 */
	public void setrecipient(String xrecipient) {recipient = xrecipient;}
	
	/**
	 * 3 value constructor
	 * @param xdate, Date in String form
	 * @param xamount, Amount as double
	 * @param xdescription, Description as string
	 * @throws TransactionAttributeException 
	 */
	public Transfer(String xdate, double xamount, String xdescription) throws TransactionAttributeException
	{
		super(xdate, xamount, xdescription);
	}
	/**
	 * Full constructor
	 * @param xdate, Date in String form
	 * @param xamount, Amount as double
	 * @param xdescription, Description as string
	 * @param xsender, Sender as String
	 * @param xrecipient, Recipient as String
	 * @throws TransactionAttributeException 
	 */
	public Transfer(String xdate, double xamount, String xdescription, String xsender, String xrecipient) throws TransactionAttributeException
	{
		super(xdate, xamount, xdescription);
		this.setsender(xsender);
		this.setrecipient(xrecipient);
	}
	/**
	 * copy-constructor
	 * @param xTransfer, Transfer object that is to be copied
	 */
	public Transfer(Transfer xTransfer)
	{
		super();
		this.date = xTransfer.date;
		this.amount = xTransfer.amount;
		this.description = xTransfer.description;
		this.sender = xTransfer.sender;
		this.recipient = xTransfer.recipient;
	}
	
	/**
	 * standart constrctor
	 */
	public Transfer() {
		super();
	}
	/**
	 * returns String as paragraph including all attributes as 'Name: Data'
	 *@return String as paragraph including all attributes as 'Name: Data'
	 */
	@Override
	public String toString() { //Gibt als Paragraph die Atribbute nach Schema 'Name: Daten' aus
		return (super.toString() + ("\n Sender: " + this.sender + "\n Recepient: " + this.recipient + "\n"));
	}
	/**
	 *returns the amount attribute
	 *@return amount attribute (double)
	 */
	@Override
	public double calculate() {
		return this.amount;
	}
	/**
	 * Checks if classtype and attributes of the given object match
	 *@return true for match, false for no match
	 */
	@Override
	public boolean equals(Object xObject) {
		if (xObject instanceof Transfer) {
			Transfer xTransfer = (Transfer) xObject;
			if ((super.equals(xObject))&&(this.sender.equals(xTransfer.sender))&&(this.recipient.equals(xTransfer.recipient))) {
				return true;
			}
		}
		return false;
	}
}
