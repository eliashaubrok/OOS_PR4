package bank;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * a private bank
 */
public class PrivateBank implements Bank{
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
	protected Map<String, List<Transaction>> accountToTransaction = new HashMap<>();
	/**
	 * the path for the directory name
	 */
	private String directoryName = "";
	/**
	 * gets the name of the bank
	 * @return name (String)
	 */
	public String getname(){
		return this.name;
	}
	/**
	 * sets the name of the bank
	 * @param xname (String)
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
	 * @param xincomingIntrest (double)
	 */
	public void setIncomingInterest(double xincomingIntrest) throws IntrestException{
		if (xincomingIntrest >= 0 && xincomingIntrest <= 1) {this.incomingInterest = xincomingIntrest;}
		else {throw new IntrestException();}
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
	 * @param xoutgoingIntrest (double)
	 */
	public void setOutgoingInterest(double xoutgoingIntrest) throws IntrestException{
		if (xoutgoingIntrest >= 0 && xoutgoingIntrest <= 1) {this.outgoingInterest = xoutgoingIntrest;}
		else {throw new IntrestException();}
	}

	/**
	 * gets the directory name for accounts
	 * @return Directory name (String)
	 */
	public String getDirectoryName() {
		return directoryName;
	}
	/**
	 * sets the directory name for accounts
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	/**
	 * constructor
	 * @param xname (String)
	 * @param xincomingInterest (double between 0 an 1)
	 * @param xoutgoingInterest (double between 0 and 1)
	 * @throws TransactionAttributeException 
	 */

	/**
	 * returns list of transactions
	 * @return list of transactions
	 */
	public Map<String, List<Transaction>> getAccountToTransaction() {
		return this.accountToTransaction;
	}

	public PrivateBank(String xname, double xincomingInterest, double xoutgoingInterest, String xdN) throws TransactionAttributeException, IOException, AccountAlreadyExistsException {
		this.setname(xname);
		this.setIncomingInterest(xincomingInterest);
		this.setOutgoingInterest(xoutgoingInterest);
		this.setDirectoryName(xdN);

		try {
			this.readAccounts();
		} catch (IOException | AccountAlreadyExistsException | TransactionAlreadyExistException |
				 TransactionAttributeException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Copy-constructor (copies name, incoming interest and outgoing interest)
	 * @param xPrivateBank, the to be copied PrivateBank Object
	 */
	public PrivateBank(PrivateBank xPrivateBank)
	{
		this.name = xPrivateBank.name;
		this.incomingInterest = xPrivateBank.incomingInterest;
		this.outgoingInterest = xPrivateBank.outgoingInterest;
		this.directoryName = xPrivateBank.directoryName;
		this.accountToTransaction = xPrivateBank.accountToTransaction;
	}
	
	/**
	 * returns String as paragraph including all attributes as 'Name: Data'
	 *@return String as paragraph including all attributes as 'Name: Data'
	 */
	@Override
	public String toString() {
		return ("Name: " + this.name + "\n IncomingIntrest: " + this.incomingInterest + "\n OutgoingIntrest: " + this.outgoingInterest + "\n" + " Accounts To Transactions: " + this.accountToTransaction + "\n");
	}
	/**
	 *checks if all attributes and the classtype match
	 *@param xObject, the to be checked object (Object)
	 *@return true if equal
	 */
	@Override
	public boolean equals(Object xObject) {
		if (xObject instanceof PrivateBank) {
			PrivateBank xPrivateBank = (PrivateBank) xObject;
			if ((this.name.equals(xPrivateBank.name))&&(this.incomingInterest==xPrivateBank.incomingInterest)&&(this.outgoingInterest==xPrivateBank.outgoingInterest)&&(this.directoryName.equals(xPrivateBank.directoryName))) {
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
	public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
		if(this.accountToTransaction.containsKey(account)) {
			throw new AccountAlreadyExistsException();
		}
		else {
			this.accountToTransaction.put(account, new ArrayList<Transaction> ());
			try {
				this.writeAccount(account);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	public void deleteAccounts ()  {
		this.accountToTransaction.clear();
	}
	/**
	 * creates an account with given name and transactionlist (if both are valid and not already in Bank)
	 * @param account name (String)
	 * @param transactions (List<Transaction>)
	 */
	@Override
	public void createAccount(String account, List<Transaction> transactions)
			throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {
		if(this.accountToTransaction.containsKey(account)) {
			throw new AccountAlreadyExistsException();
		}
		//else if(this.accountToTransaction.containsValue(transactions)) {
		//	throw new TransactionAlreadyExistException();
		//}
		else	{
			this.accountToTransaction.put(account, transactions);
			try {
				this.writeAccount(account);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
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
			throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {
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
			try {
				this.writeAccount(account);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
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
			throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
		if (!this.accountToTransaction.containsKey(account)) {
			throw new AccountDoesNotExistException();
		}
		//else if (!this.accountToTransaction.get(account).contains(transaction)){
		//	throw new TransactionDoesNotExistException();
		//}
		else {
			this.accountToTransaction.get(account).remove(transaction);
			try {
				this.writeAccount(account);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
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
     * Calculates and returns the current account balance (if account exists)
     * @param account name (String)
     * @return the current account balance or 0 if account doesn't exist (double)
     */
	@Override
    public double getAccountBalance(String account)
    {
        if (this.accountToTransaction.containsKey(account)) {
        	double balance = 0.0;
				for (Transaction transaction : this.accountToTransaction.get(account)) {
					balance += transaction.calculate();
				}
        	return balance;
        }
        return 0;
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

	/**
	 * reads in the accounts out of a jason file and creates the accounts in the class with the transactions
	 * @throws IOException
	 * @throws AccountAlreadyExistsException
	 * @throws TransactionAlreadyExistException
	 * @throws TransactionAttributeException
	 */
	private void readAccounts() throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeHierarchyAdapter(Transaction.class, new CustomJson());
		File file = new File(this.directoryName);
		if (file.listFiles() != null) {
			File[] filelist = file.listFiles();
			for(int i = 0; i < filelist.length; ++i) {
				File file1 = filelist[i];
				// read in all as string
				String json = new String(Files.readAllBytes(Paths.get(this.directoryName + "/" + file1.getName())));
				// define type for json deserialization
				Type type = (new TypeToken<List<Transaction>>() {}).getType();
				// deserialize jason data in transaction list
				List<Transaction> transactionList = gson.create().fromJson(json, type);
				// create account(with file name) with transactions
				this.createAccount(file1.getName().replaceAll(".json", ""), transactionList);
			}
		}
	}

	/**
	 * Writes the account data in a jason file
	 * @param account (String)
	 * @throws IOException
	 */
	private void writeAccount(String account) throws IOException {
		CustomJson customserialisieren = new CustomJson();
		List<JsonElement> jsonElementList = new ArrayList();
		Iterator JELI = ((List)this.accountToTransaction.get(account)).iterator();
		//adds transactions to JEList
		while(JELI.hasNext()) {
			Transaction t = (Transaction)JELI.next();
			jsonElementList.add(customserialisieren.serialize(t, null, null));
		}
		//write JEList into file
		Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
		FileWriter writer = new FileWriter(this.directoryName + account + ".json");
		gson.toJson(jsonElementList, writer);
		writer.close();
	}

	@Override
	public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
		try {
			if(this.accountToTransaction.containsKey(account)) {
				this.accountToTransaction.remove(account);
			}
			else {
				throw new AccountDoesNotExistException();
			}
		}
		catch (AccountDoesNotExistException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public List<String> getAllAccounts() {
		return (new ArrayList<>(this.accountToTransaction.keySet()));
	}
}
