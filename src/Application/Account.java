package Application;

import java.util.ArrayList;

public class Account {
    /**
     * El nombre de la cuenta
     * */
    private final String accountName;
    /**
     * El numero ID de la cuenta
     * */
    private final String accountID;
    /**
     * El objeto User que posee esta cuenta
     * */
    private User accountHolder;
    /**
     * La lista de transacciones para esta cuenta*/
    private final ArrayList<Transaction> transactions;
    /**
     * Creacion de una nueva cuenta
     * @param accountName el numero de la cuenta
     * @param accountHolder el objeto User que es titular de esta cuenta
     * @param theBank el banco que emite esta cuenta
     * */
    public Account(String accountName, User accountHolder, Bank theBank) {
        this.accountName = accountName;
        this.accountHolder=accountHolder;
        this.accountID = theBank.getNewAccountID();
        this.transactions= new ArrayList<>();
    }
    /**
     * Obtener el ID de la cuenta
     * @return la variable accountID
     * */
    public String getAccountID() {
        return this.accountID;
    }
    /**
     * Obtener el propietario de la cuenta
     * @return el objeto tipo User
     * */
    public User getAccountHolder() {
        return accountHolder;
    }

    /**
     * obtener resumen para la cuenta
     * @return el String del resumen
     * */
    public String getSummaryLine(){
        double balance=this.getBalance();
        if(balance>=0){
            return String.format("%s : $%.02f : %s",this.accountID,balance,this.accountName);
        }else{
            return String.format("%s : $(%.02f) : %s",this.accountID,balance,this.accountName);
        }
    }
    /**
     * Obtener el balance de esta cuenta agregando las cantidad de las transacciones
     * @return el valor del saldo
     * */
    public double getBalance(){
        double balance=0;
        for (Transaction t:this.transactions){
            balance += t.getAmount();
        }
        return balance;
    }
    /**
     * Imprime el historial de transacciones de la cuenta
     * */
    public void printTransHistory(){
        System.out.printf("\nHistorial de transacciones de la cuenta %s\n",this.accountID);
        for (int i =this.transactions.size()-1; i>=0;i--) {
            System.out.println(this.transactions.get(i).getSummaryLine());
        }
        System.out.println();
    }
    /**
     * Agrega una nueva tranasaccion a esta cuenta
     * @param amount  la cantidad de la transaccion
     * @param message  el mensaje de la transaccion
     * */
    public void addTransaction(double amount, String message){
        Transaction newTrans=new Transaction(amount,message,this);
        this.transactions.add(newTrans);
    }
}
