package Application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    /**
     * El nombre del usuario
     * */
    private final String firstName;
    /**
     * El apellido del usuario
     * */
    private String lastName;
    /**
     * El ID del usuario
     * */
    private final String userID;
    /**
     * El hash MD5 del PIN del usuario
     * */
    private byte userPIN[];
    /**
     * La lista de las cuentas de este usuario
     * */
    private final ArrayList<Account> accounts;
    /**
     * Creacion de un nuevo usuario
     * @param firstName el nombre del usuario
     * @param lastName el apellido del usuario
     * @param userPIN el PIN de la cuenta del usuario
     * @param theBank el objeto Bank del cual es usuario es cliente
     * */
    public User(String firstName, String lastName, String userPIN, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            this.userPIN=md.digest(userPIN.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error");
            e.printStackTrace();
            System.exit(1);
        }
        this.userID=theBank.getNewUserID();
        this.accounts= new ArrayList<>();
        System.out.printf("Nuevo usuario creado %s, %s con ID %s.\n",lastName,firstName,this.userID);
    }
    /**
     * Agregar una cuenta para el usuario
     * @param account la cuenta que sera agregada
     * */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    /**
     * Obtener el ID del usuario
     * @return la variable userID
     * */
    public String getUserID(){
        return this.userID;
    }

    public boolean validatePIN( String pin){
        try {
            MessageDigest md=MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.userPIN);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
    /**
     * Retorna el primer nombre del usuario
     * @return el primer nombre
     * */
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public void printAccountsSummary(){
        System.out.printf("\nResumen de cuentas de: %s %s\n",this.firstName,this.lastName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf("    %d) %s\n",i+1,this.accounts.get(i).getSummaryLine());
        }
        System.out.println();
    }
    /**
     * Obtener la cantidad de cuentas del usuario
     * @return cantidad de cuentas
     * */
    public int numAccounts(){
        return this.accounts.size();
    }
    /**
     * Imprime la historia de transacciones para una cuenta en particular
     * @param acctIdx el indice de la cuenta a usar
     * */
    public void printAcctTransHistory(int acctIdx){
        this.accounts.get(acctIdx).printTransHistory();
    }
    /**
     * Obtener el balance de una cuenta en particular
     * @param acctIdx el indice de la cuenta a usar
     * @return el balance de la cuenta
     * */
    public double getAcctBalance(int acctIdx){
        return this.accounts.get(acctIdx).getBalance();
    }
    /**
     * Obtener el accountId de una cuenta en particular
     * @param acctIdx el indice de la cuenta a usar
     * @return el accountID de la cuenta
     * */
    public String getAcctUserID( int acctIdx){
        return this.accounts.get(acctIdx).getAccountID();
    }
    /**
     * Agrega una transaccion a una cuenta en particular
     * @param acctIdx  el indice de la cuenta
     * @param amount  la cantidad de la transaccion
     * @param message  el mensaje de la transaccion
     * */
    public void addAcctTransaction(int acctIdx, double amount , String message){
        this.accounts.get(acctIdx).addTransaction(amount,message);
    }
    /***
     * Busca en el usuario, el indice de la cuenta ingresada
     * @param accountNumber numero de cuenta ingresado
     * @return el inidice de la cuenta
     */
    public int findAccountIndex(String accountNumber){
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountID().equals(accountNumber)){
                return i;
            }
        }
        return -1;
    }
}
