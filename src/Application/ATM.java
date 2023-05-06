package Application;

import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        //Crear el objeto banco
        Bank thebank=new Bank("Banco de Credito del Peru");
        //Agregar usuario al banco
        User aUser1=thebank.addUser("Jose","Ochoa","1234", "Ahorro Dolares");
        User aUser2=thebank.addUser("Diego","Ochoa","1234", "Ahorro Soles");
        //Agregar una cuenta para nuestros usuarios
        Account newAccount=new Account("Plazo Fijo",aUser1,thebank);
        aUser1.addAccount(newAccount);
        thebank.addAccount(newAccount);

        Account newAccount2=new Account("Cuenta Sueldo",aUser2,thebank);
        aUser2.addAccount(newAccount2);
        thebank.addAccount(newAccount2);

        User curUser;
        while (true){
            thebank.getAccounts().forEach(elem -> System.out.println(elem.getAccountID()));
            curUser=ATM.mainMenuPrompt(thebank,sc);
            ATM.printUserMenu(curUser,sc,thebank);
        }
    }
    /**
     * Imprime el menu Login del ATM
     * @param theBank el objeto Bank
     * @param sc el objeto Scanner que se usa para la entrada del usuario
     * */
    public static User mainMenuPrompt(Bank theBank, Scanner sc){
        String userID;
        String pin;
        User authUser;

        do {
            System.out.printf("\nBienvenido a %s\n",theBank.getName());
            System.out.print("Introduce el ID de usuario: ");
            userID=sc.nextLine();
            System.out.print("Introduce el pin: ");
            pin=sc.nextLine();
            authUser=theBank.userLogin(userID,pin);
            if(authUser==null){
                System.out.println("Combinacion de ID/pin incorrecta. Por favor inténtalo de nuevo.");
            }
        }while(authUser==null);
        return authUser;
    }
    /**
     * Mostrar el mensaje de bienvenida luego de iniciar sesion
     * @param theUser  el objeto User logeado
     * @param sc el objeto Scanner usado para la entrada del usuario
     * @param thebank el objeto Bank, correspondiente al banco del usuario
     * */
    public static void printUserMenu(User theUser,Scanner sc, Bank thebank){
        theUser.printAccountsSummary();
        int choice;
        do {
            System.out.printf("Bienvenido %s, ¿Que te gustaria hacer?\n",theUser.getFirstName());
            System.out.println("    1) Historial de transacciones");
            System.out.println("    2) Retirar");
            System.out.println("    3) Depositar");
            System.out.println("    4) Transferir");
            System.out.println("    5) Salir");
            System.out.println();
            System.out.println("Introduce una option: ");
            choice=sc.nextInt();
            if(choice<1 || choice>5){
                System.out.println("Opcion no válida. Por favor elije entre 1-5");
            }
        }while(choice<1 || choice>5);

        switch (choice) {
            case 1 -> ATM.showTransHistory(theUser, sc);
            case 2 -> ATM.withdrawFunds(theUser, sc);
            case 3 -> ATM.depositFunds(theUser, sc);
            case 4 -> ATM.transferOptions(theUser, sc, thebank);
            case 5 -> sc.nextLine();
        }

        if(choice!=5){
            ATM.printUserMenu(theUser,sc,thebank);
        }
    }
    /**
     * Mostrar el historial de transacciones para una cuenta
     * @param theUser  el objeto User logeado
     * @param sc el objeto Scanner usado para la entrada del usuario
     * */
    public static void showTransHistory(User theUser, Scanner sc){
        int theAcct;
        do {
            System.out.printf("Introduce el numero (1-%d) de la cuenta cuyas transacciones desea ver: ",theUser.numAccounts());
            theAcct= sc.nextInt()-1;
            if(theAcct<0 || theAcct>= theUser.numAccounts()){
                System.out.println("Cuenta no válida. Por favor inténtalo de nuevo.");
            }
        }while(theAcct<0 || theAcct>= theUser.numAccounts());

        theUser.printAcctTransHistory(theAcct);
    }
    /**
     * Mostrar el menu de eleccion del tipo de transferencia
     * @param theUser el usuario logeado
     * @param sc el objeto Scanner
     * @param thebank el objeto Bank, correspondiente al banco del usuario
     * */
    public static void transferOptions(User theUser, Scanner sc, Bank thebank){
        int userChoice;
        do{
            System.out.println("Elige el tipo de transferencia");
            System.out.println("    1) Entre mis cuentas");
            System.out.println("    2) A terceros");
            System.out.println("\nIntroduce una option: ");
            userChoice=sc.nextInt();
        }while(userChoice<1 || userChoice>2);
        if(userChoice==1){
            transferMyFunds(theUser,sc);
        }else {
            transferFundsToThird(theUser,sc,thebank);
        }
    }
    /**
     * Proceso de transaccion de fondos entre las cuentas del usuario
     * @param theUser el objeto User logeado
     * @param sc  el objeto Scanner usado para la entrada del usuario
     * */
    public static void transferMyFunds(User theUser, Scanner sc){
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do {
            System.out.printf("Introduce el numero (1-%d) de la cuenta desde la cual deseas transferir: ",theUser.numAccounts());
            fromAcct=sc.nextInt()-1;
            if(fromAcct<0 || fromAcct>= theUser.numAccounts()){
                System.out.println("Cuenta no válida. Por favor inténtalo de nuevo.");
            }
        }while(fromAcct<0 || fromAcct>= theUser.numAccounts());

        acctBal=theUser.getAcctBalance(fromAcct);

        do {
            System.out.printf("Introduce el numero (1-%d) de la cuenta a la cual deseas transferir: ",theUser.numAccounts());
            toAcct=sc.nextInt()-1;
            if(toAcct<0 || toAcct>= theUser.numAccounts()){
                System.out.println("Cuenta no válida. Por favor inténtalo de nuevo.");
            }
        }while(toAcct<0 || toAcct>= theUser.numAccounts());

        do{
            System.out.printf("Introduce el monto a transferir (max $%.02f): $",acctBal);
            amount= sc.nextDouble();
            if(amount<0){
                System.out.println("La cantidad debe ser mayor a cero");
            } else if (amount>acctBal) {
                System.out.printf("El monto no debe ser mayor que su saldo: $%.02f\n",acctBal);
            }
        }while(amount<0 || amount>acctBal);

        theUser.addAcctTransaction(fromAcct,-1*amount,String.format("Transferencia a la cuenta %s : Titular -> %s %s",theUser.getAcctUserID(toAcct),theUser.getFirstName(),theUser.getLastName()));
        theUser.addAcctTransaction(toAcct, amount,String.format("Transferencia a la cuenta %s : Titular -> %s %s",theUser.getAcctUserID(fromAcct),theUser.getFirstName(),theUser.getLastName()));
    }
    /**
     * Proceso de transaccion entre la cuenta del usuario y la de un tercero
     * @param theUser el usuario logeado
     * @param sc el objeto Scanner
     * @param thebank el objeto Bank, correspondiente al banco del usuario
     * */
    public static void transferFundsToThird(User theUser, Scanner sc, Bank thebank){
        int fromAcct;
        String toAcct;
        User thirdUser;
        double amount;
        double acctBal;

        do {
            System.out.printf("Introduce el numero (1-%d) de la cuenta desde la cual deseas transferir: ",theUser.numAccounts());
            fromAcct=sc.nextInt()-1;
            if(fromAcct<0 || fromAcct>= theUser.numAccounts()){
                System.out.println("Cuenta no válida. Por favor inténtalo de nuevo.");
            }
        }while(fromAcct<0 || fromAcct>= theUser.numAccounts());

        acctBal=theUser.getAcctBalance(fromAcct);

        do {
            System.out.println("Introduce el numero de cuenta a la cual deseas transferir: ");
            toAcct=sc.next();
            thirdUser=thebank.findUserByAccountNumber(toAcct);
            if(thirdUser==null){
                System.out.println("Cuenta no válida. Por favor inténtalo de nuevo.");
            }
        }while(thirdUser==null);

        do{
            System.out.printf("Introduce el monto a transferir (max $%.02f): $",acctBal);
            amount= sc.nextDouble();
            if(amount<0){
                System.out.println("La cantidad debe ser mayor a cero");
            } else if (amount>acctBal) {
                System.out.printf("El monto no debe ser mayor que su saldo: $%.02f\n",acctBal);
            }
        }while(amount<0 || amount>acctBal);

        theUser.addAcctTransaction(fromAcct,-1*amount,String.format("Transferencia a la cuenta %s : Titular -> %s %s",thirdUser.getAcctUserID(thirdUser.findAccountIndex(toAcct)),thirdUser.getFirstName(),thirdUser.getLastName()));
        thirdUser.addAcctTransaction(thirdUser.findAccountIndex(toAcct),amount,String.format("Transferencia de la cuenta %s : Titular -> %s %s",theUser.getAcctUserID(fromAcct),theUser.getFirstName(),theUser.getLastName()));

    }
    /**
     * Proceso para retirar los fondos de una cuenta
     * @param theUser el objeto User logeado
     * @param sc  el objeto Scanner usado para la entrada del usuario
     * */
    public static void withdrawFunds(User theUser, Scanner sc){
        int fromAcct;
        double amount;
        double acctBal;
        String message;

        do {
            System.out.printf("Introduce el numero (1-%d) de la cuenta de la cual deseas retirar: ",theUser.numAccounts());
            fromAcct=sc.nextInt()-1;
            if(fromAcct<0 || fromAcct>= theUser.numAccounts()){
                System.out.println("Cuenta no válida. Por favor inténtalo de nuevo.");
            }
        }while(fromAcct<0 || fromAcct>= theUser.numAccounts());
        acctBal=theUser.getAcctBalance(fromAcct);

        do{
            System.out.printf("Introduce el monto a retirar (max $%.02f): $",acctBal);
            amount= sc.nextDouble();
            if(amount<0){
                System.out.println("La cantidad debe ser mayor a cero");
            } else if (amount>acctBal) {
                System.out.printf("El monto no debe ser mayor que su saldo: $%02f.\n",acctBal);
            }
        }while(amount<0 || amount>acctBal);

        sc.nextLine();
        System.out.print("Introduce un mensaje: ");
        message=sc.nextLine();

        theUser.addAcctTransaction(fromAcct,-1*amount,message);
    }
    /**
     * Proceso para depositar fondos a una cuenta
     * @param theUser el obejto usuario logeado
     * @param sc el objeto Scanner usado para la entrada del usuario
     * */
    public static void depositFunds(User theUser, Scanner sc){
        int toAcct;
        double amount;
        String message;

        do {
            System.out.printf("Introduce el numero (1-%d) de la cuenta a la que deseas depositar: ",theUser.numAccounts());
            toAcct=sc.nextInt()-1;
            if(toAcct<0 || toAcct>= theUser.numAccounts()){
                System.out.println("Cuenta no válida. Por favor inténtalo de nuevo.");
            }
        }while(toAcct<0 || toAcct>= theUser.numAccounts());

        do{
            System.out.print("Introduce el monto a depositar: $");
            amount= sc.nextDouble();
            if(amount<0){
                System.out.println("La cantidad debe ser mayor a cero.");
            }
        }while(amount<0);

        sc.nextLine();
        System.out.print("Introduce un mensaje: ");
        message=sc.nextLine();

        theUser.addAcctTransaction(toAcct,amount,message);
    }
}
