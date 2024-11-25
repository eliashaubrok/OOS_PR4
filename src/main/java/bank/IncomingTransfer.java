/**
 * 
 */
package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * 
 */
public class IncomingTransfer extends Transfer {

	/**
	 * Full constructor
	 * @param xdate, Date in String form
	 * @param xamount, Amount as double
	 * @param xdescription, Description as string
	 * @param xsender, Sender as String
	 * @param xrecipient, Recipient as String
	 * @throws TransactionAttributeException 
	 */
	public IncomingTransfer(String xdate, double xamount, String xdescription, String xsender, String xrecipient) throws TransactionAttributeException
	{
		super(xdate, xamount, xdescription, xsender, xrecipient);
	}
	/**
	 * copy-constructor
	 * @param xTransfer, Transfer object that is to be copied
	 */
	public IncomingTransfer(Transfer xTransfer)
	{
		super();
		this.date = xTransfer.date;
		this.amount = xTransfer.amount;
		this.description = xTransfer.description;
		this.sender = xTransfer.sender;
		this.recipient = xTransfer.recipient;
	}
	/**
	 *returns the amount attribute
	 *@return amount attribute (double)
	 */
	@Override
	public double calculate() {
		return this.amount;
	}
}
