package bank;

import bank.exceptions.*;

/**
 *The abstract transaction class is a blueprint for transactions
 */
public abstract class Transaction implements CalculateBill{
	/**
	 * The date of the transactions as string (no further format checking
	 */
	protected String date;
	/**
	 * The transaction amount as double
	 */
	protected double amount;
	/**
	 * The description of the transaction as String
	 */
	protected String description;
	
	/**
	 * sets the date attribute
	 * @param xdate (String)
	 */
	public void setdate(String xdate) {date = xdate;}
	/**
	 * returns date attribute
	 * @return date (String)
	 */
	public String getdate() {return date;}
	/**
	 * sets the attribute amount the given double
	 * @param xamount (double)
	 */
	public void setamount(double xamount)	throws  TransferAmountAttributeException {amount = xamount;}
	/**
	 * returns the amount attribute
	 * @return amount (double)
	 */
	public double getamount() {return amount;}
	/**
	 * sets the description attribute
	 * @param xdescription (String)
	 */
	public void setdescription(String xdescription) {description = xdescription;}
	/**
	 * returns description attribute 
	 * @return description (String)
	 */
	public String getdescription() {return description;}
	
	/**
	 * standart-constructor
	 */
	public Transaction() {}
	/**
	 * 3 value constructor
	 * @param xdate, Date in String form
	 * @param xamount, Amount as double
	 * @param xdescription, Description as string
	 * @throws TransactionAttributeException 
	 */
	public Transaction(String xdate, double xamount, String xdescription) throws TransactionAttributeException
	{
		setdate(xdate);
		setamount(xamount);
		setdescription(xdescription);
	}
	/**
	 * returns String as paragraph including all attributes as 'Name: Data'
	 *@return String as paragraph including all attributes as 'Name: Data'
	 */
	@Override
	public String toString() {
		return("\n Date: " + this.date + "\n Amount: " + this.calculate() + "\n Description: " + this.description);
	}
	/**
	 *Checks if classtype and attributes of the given object match
	 *@return true for match, false for no match
	 */
	@Override
	public boolean equals(Object xObject) {
		if (xObject instanceof Transaction) {
			Transaction xTransaction = (Transaction) xObject;
			if ((this.date.equals(xTransaction.date))&&(this.amount == xTransaction.amount)&&(this.description.equals(xTransaction.description))) {
				return true;
			}
		}
		return false;
	}
}
