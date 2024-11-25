package bank;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import bank.exceptions.*;

/**
 * a private bank
 */
public class PrivateBankAlt implements Bank{
	/**
	 * name of the private bank
	 */
	private String name;
	/**
	 * bank interest on incoming payments (between 0 and 1)
	 */
	private double incomingInterest;
	/**
	 * bank interest on outgoing payments (between 0 and 1)
	 */
	private double outgoingInterest;
	/**
	 * a map of style <String, list<Transaktions>> which links transactions to an account
	 */	
	private Map<String, List<Transaction>> accountToTransaction = new HashMap<String, List<Transaction>>();
	
	/**
	 * gets the name of the bank
	 * @return name (String)
	 */
	public String getname(){
		return this.name;
	}
	/**
	 * sets the name of the bank
	 * @param name (String)
	 */
	public void setname(String xname) {
		this.name = xname;
	}
	/**
	 * gets incoming interest
	 * @return incomingInterest (double)
	 */
	public double getIncomingInterest() {
		return this.incomingInterest;
	}
	/**
	 * sets incoming interest (when in between 0 and 1)
	 * @param incomingInterest (double)
	 */
	public void setIncomingInterest(double xincomingIntrest) {
		if (xincomingIntrest >= 0 && xincomingIntrest <= 1) {this.incomingInterest = xincomingIntrest;}
		else {System.out.print("Intrest rate is not in allowed range");}
	}
	/**
	 * gets outgoing interest
	 * @return outgoingInterest (double)
	 */
	public double getOutgoingInterest() {
		return this.outgoingInterest;
	}
	/**
	 * sets outgoing interest (when in between 0 and 1)
	 * @param outgoingInterest (double)
	 */
	public void setOutgoingInterest(double xoutgoingIntrest) {
		if (xoutgoingIntrest >= 0 && xoutgoingIntrest <= 1) {this.outgoingInterest = xoutgoingIntrest;}
		else {System.out.print("Intrest rate is not in allowed range");}
	}
	
	/**
	 * constructor
	 * @param name (String)
	 * @param incomingIntrest(double between 0 an 1)
	 * @param outgoingIntrest(double between 0 and 1)
	 * @throws TransactionAttributeException 
	 */
	public PrivateBankAlt(String xname, double xincomingInterest, double xoutgoingInterest) throws TransactionAttributeException {
		this.setname(xname);
		this.setIncomingInterest(xincomingInterest);
		this.setOutgoingInterest(xoutgoingInterest);
	}
	/**
	 * Copy-constructor (copies name, incoming interest and outgoing interest)
	 * @param xPrivateBank, the to be copied PrivateBank Object
	 */
	public PrivateBankAlt(PrivateBankAlt xPrivateBank)
	{
		this.name = xPrivateBank.name;
		this.incomingInterest = xPrivateBank.incomingInterest;
		this.outgoingInterest = xPrivateBank.outgoingInterest;
	}
	
	/**
	 * returns String as paragraph including all attributes as 'Name: Data'
	 *@return String as paragraph including all attributes as 'Name: Data'
	 */
	@Override
	public String toString() {
		return ("Name: " + this.name + "\n IncomingIntrest: " + this.incomingInterest + "\n OutgoingIntrest: " + this.outgoingInterest + "\n");
	}
	/**
	 *checks if all attributes and the classtype match
	 *@param xObject, the to be checked object (Object)
	 *@return true if equal
	 */
	@Override
	public boolean equals(Object xObject) {
		if (xObject instanceof PrivateBankAlt) {
			PrivateBankAlt xPrivateBank = (PrivateBankAlt) xObject;
			if ((this.name == xPrivateBank.name)&&(this.incomingInterest==xPrivateBank.incomingInterest)&&(this.outgoingInterest==xPrivateBank.outgoingInterest)&&(this.accountToTransaction == xPrivateBank.accountToTransaction)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * creates an account with the given name and stores it in the bank if an account with this name doesn't already exists
	 * @param account name (String)
	 */
	@Override
	public void createAccount(String account) throws AccountAlreadyExistsException {
		if(this.accountToTransaction.containsKey(account)) {
			throw new AccountAlreadyExistsException();
		}
		else {
			this.accountToTransaction.put(account, null);
		}
	}
	/**
	 * creates an account with given name and transactionlist (if both are valid and not already in Bank)
	 * @param account name (String)
	 * @param transactionslist (List<Transaction>)
	 */
	@Override
	public void createAccount(String account, List<Transaction> transactions)
			throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
		if(this.accountToTransaction.containsKey(account)) {
			throw new AccountAlreadyExistsException();
		}
		else if(this.accountToTransaction.containsValue(transactions)) {
			throw new TransactionAlreadyExistException();
		}
		else	{
			this.accountToTransaction.put(account, transactions);
		}
		//TransactionAttributeException ?		
	}
	/**
	 * add an transaction to an account (if the account exists and the transaction doesn't)
	 * @param account name (String)
	 * @param transaction (Transaction)
	 */
	@Override
	public void addTransaction(String account, Transaction transaction)
			throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
		if(!this.accountToTransaction.containsKey(account)) {
			throw new AccountDoesNotExistException();
		}
		else if (this.accountToTransaction.get(account).contains(transaction)) {
			throw new TransactionAlreadyExistException();
		}
		else {
			if (transaction instanceof Payment) {
				((Payment) transaction).setIncomingIntrest(this.incomingInterest);
				((Payment) transaction).setOutgoingIntrest(this.outgoingInterest);
			}
			this.accountToTransaction.get(account).add(transaction);
		}
		//TransactionAttributeException ?
	}
	/**
	 * removes a transaction from account (if account and transaction exist)
	 * @param account name (String)
	 * @param transaction (Transaction)
	 */
	@Override
	public void removeTransaction(String account, Transaction transaction)
			throws AccountDoesNotExistException, TransactionDoesNotExistException {
		if (!this.accountToTransaction.containsKey(account)) {
			throw new AccountDoesNotExistException();
		}
		else if (!this.accountToTransaction.get(account).contains(transaction)){
			throw new TransactionDoesNotExistException();
		}
		else {
			this.accountToTransaction.get(account).remove(transaction);
		}
	}
	/**
	 * checks if transaction exists in bankaccount
	 * @param account name (String)
	 * @param transaction (Transaction)
	 * @return true if exists otherwise false
	 */
	@Override
	public boolean containsTransaction(String account, Transaction transaction) {
		if (this.accountToTransaction.containsKey(account)) {
			if(this.accountToTransaction.get(account).contains(transaction)) {
				return true;
			}
		}
		return false;
	}
	/**
     * Calculates and returns the current account balance
     * @param account name (String)
     * @return the current account balance (double)
     */
	@Override
    public double getAccountBalance(String account)
    {
        double balance = 0.0;
        if (this.accountToTransaction.containsKey(account)) {
        	for (Transaction transaction: this.accountToTransaction.get(account)) {
        		if(transaction instanceof Transfer){
                    if(((Transfer) transaction).getrecipient() == account)
                    {
                        balance-= ((Transfer) transaction).amount;
                    }
                    else if(((Transfer) transaction).getsender() == account)
                    {
                        balance+= ((Transfer) transaction).amount;
                    }
                }
        		else if(transaction instanceof Payment){
                    balance+=transaction.amount;
                }
        	}
        }
        return balance;
    }

	/**
	 * gets Transaction list of bankaccount (if bankaccount exists)
	 * @param account name (String)
	 * @return transactions (List<Transaction>) if account exists, otherwise null
	 */
	@Override
	public List<Transaction> getTransactions(String account) {
		if (this.accountToTransaction.containsKey(account)) {
			return this.accountToTransaction.get(account);
		}
		return null;
	}
	/**
	 * gets a sorted list of transactions (if account exists) in ascending or descending order
	 * @param account name (String)
	 * @param asc(true when ascending order, false when descending) (boolean)
	 * @returnm sorted List (List<Transaction>)
	 */
	@Override
	public List<Transaction> getTransactionsSorted(String account, boolean asc) {
		if (this.accountToTransaction.containsKey(account)) {
			List<Transaction> transactionList = this.accountToTransaction.get(account);
	        transactionList.sort(Comparator.comparing(Transaction::getamount));
	        if(!asc){
	            Collections.reverse(transactionList);
	        }

	        return transactionList;

		}
		return null;
	}
	
	/**
     * returns a list of either positive or negative transactions (-> calculated amounts).
     * @param account name (String)
     * @param positive selects if positive or negative transactions are listed (boolean)
     * @return the list of transactions (List<Transaction>) or null if account doesn't exist
     */
	@Override
    public List<Transaction> getTransactionsByType(String account, boolean positive)
    {
		if (this.accountToTransaction.containsKey(account)) {
			List<Transaction> transactionLinkedList= new LinkedList<Transaction>();
		    for(Transaction entry: this.accountToTransaction.get(account)){
		        if(entry.getamount()>0 && positive){
		            transactionLinkedList.add(entry);
		        }
		        if(entry.getamount()<=0 && !positive){
		            transactionLinkedList.add(entry);
		        }
		    }
		    return transactionLinkedList;
		}
		return null;        
    }
}
