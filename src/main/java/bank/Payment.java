package bank;

import bank.exceptions.*;

/**
 *The payment class handles payments on which the bank can invoke interest 
 */
public class Payment extends Transaction{
	//Attribute
	/**
	 * bank interest on incoming payments
	 */
	private double incomingIntrest;
	/**
	 * bank interest on outgoing payments
	 */
	private double outgoingIntrest;
	
	/**
	 * returns interest on incoming payments
	 * @return incomingIntrest (double)
	 */
	public double getIncomingIntrest() {return incomingIntrest;}
	/**
	 * sets the incoming interest if its inbetween 0 and 1
	 * @param xincomingIntrest (double)
	 */
	public void setIncomingIntrest(double xincomingIntrest) throws IntrestException{
		if (xincomingIntrest >= 0 && xincomingIntrest <= 1) {incomingIntrest = xincomingIntrest;}
		else {throw new IntrestException();}
	}
	/**
	 * returns interest on outgoing payments
	 * @return outgoingIntrest (double)
	 */
	public double getOutgoingIntrest() {return outgoingIntrest;}
	/**
	 * sets the outgoing interest if its inbetween 0 and 1
	 * @param xoutgoingIntrest (double)
	 */
	public void setOutgoingIntrest(double xoutgoingIntrest) throws IntrestException{
		if (xoutgoingIntrest >= 0 && xoutgoingIntrest <= 1) {outgoingIntrest = xoutgoingIntrest;}
		else {throw new IntrestException();}
	}
	
	/**
	 * 3 value constructor
	 * @param xdate, Date in String form
	 * @param xamount, Amount as double
	 * @param xdescription, Description as string
	 * @throws TransactionAttributeException 
	 */
	public Payment(String xdate, double xamount, String xdescription) throws TransactionAttributeException
	{
		super(xdate, xamount, xdescription);
	}
	/**
	 * Full constructor
	 * @param xdate, Date in String form
	 * @param xamount, Amount as double
	 * @param xdescription, Description as string
	 * @param xincomingIntrest, Intrest on incoming payments as double
	 * @param xoutgoingIntrest, Intrest on outgoing payments as double
	 * @throws TransactionAttributeException 
	 */
	public Payment(String xdate, double xamount, String xdescription, double xincomingIntrest, double xoutgoingIntrest) throws TransactionAttributeException
	{
		super(xdate, xamount, xdescription);
		this.setIncomingIntrest(xincomingIntrest);
		this.setOutgoingIntrest(xoutgoingIntrest);
	}
	/**
	 * Copy-constructor
	 * @param xPayment, the to be copied Payment Object
	 */
	public Payment(Payment xPayment)
	{
		super();
		this.date = xPayment.date;
		this.amount = xPayment.amount;
		this.description = xPayment.description;
		this.incomingIntrest = xPayment.incomingIntrest;
		this.outgoingIntrest = xPayment.outgoingIntrest;
	}
	
	/**
	 * returns String as paragraph including all attributes as 'Name: Data'
	 *@return String as paragraph including all attributes as 'Name: Data'
	 */
	@Override
	public String toString() {
		return (super.toString()+("\n IncomingIntrest: " + this.incomingIntrest + "\n OutgoingIntrest: " + this.outgoingIntrest + "\n"));
	}
	/**
	 *Calculates the transaction amount by adding interests and the original amount
	 *@return the payment amount after considering interests (double)
	 */
	@Override
	public double calculate() {
		if (this.amount >= 0) {
			return this.amount-(this.amount*this.incomingIntrest);			
		}
		else {
			return this.amount+(this.amount*this.outgoingIntrest);
		}
	}
	/**
	 *checks if all attributes and the classtype match
	 *@param xObject, the to be checked object (Object)
	 *@return true if equal
	 */
	@Override
	public boolean equals(Object xObject) {
		if (xObject instanceof Payment) {
			Payment xPayment = (Payment) xObject;
			if ((super.equals(xObject))&&(this.incomingIntrest==xPayment.incomingIntrest)&&(this.outgoingIntrest==xPayment.outgoingIntrest)) {
				return true;
			}
		}
		return false;
	}	
}
