package Application;

import java.util.Date;

public class Transaction {
    private final double amount;
    private final Date time;
    private String message;
    private Account account;
    /**
     * Creacion de una nueva transaccion
     * @param amount el monto de la transaccion
     * @param account la cuenta a la cual pertenece la transaccion
     * */
    public Transaction(double amount, Account account) {
        this.amount = amount;
        this.account = account;
        this.time=new Date();
        this.message="";
    }
    /**
     * Creacion de una nueva transaccion
     * @param amount el monto de la transaccion
     * @param message el mensaje para la transaccion
     * @param account la cuenta a la cual pertenece la transaccion
     * */
    public Transaction(double amount, String message, Account account) {
        this(amount, account);
        this.message = message;

    }
    /**
     * Obtener la cantidad de la transaccion
     * @return la cantidad
     * */
    public double getAmount() {
        return this.amount;
    }
    /**
     * Obtener una cadena resumiendo la transaccion
     * @return la cadena resumida
     * */
    public String getSummaryLine(){
        if(this.amount>=0){
            return String.format("%s : $%.02f : %s", this.time.toString(),this.amount,this.message);
        }
        else{
            return String.format("%s : $(%.02f) : %s", this.time.toString(),-this.amount,this.message);
        }
    }
}
