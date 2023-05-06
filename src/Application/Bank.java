package Application;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    /**
     * Nombre del banco
     * */
    private final String name;
    /**
     * Lista de usuarios del banco
     * */
    private final ArrayList<User> users;
    /**
     * Lista de las cuentas del banco
     * */
    private final ArrayList<Account> accounts;

    /**
     * Crea un nuevo objeto Bank con una lista vacia de usuarios y cuentas
     * @param name nombre del banco
     * */
    public Bank(String name) {
        this.name = name;
        this.users= new ArrayList<>();
        this.accounts= new ArrayList<>();
    }
    /**
     * Generar un nuevo ID unico universal para un usuario
     * @return  el userID
     * */
    public String getNewUserID(){
        StringBuilder userID;
        Random rng=new Random();
        int len=6;
        boolean nonUnique;

        do {
            userID = new StringBuilder();
            for (int i = 0; i < len; i++) {
                userID.append(((Integer) rng.nextInt(10)));
            }
            nonUnique=false;
            for (User u: this.users) {
                if(userID.toString().compareTo(u.getUserID())==0){
                    nonUnique=true;
                    break;
                }
            }
        }while(nonUnique);

        return userID.toString();
    }
    /**
     * Generar un nuevo ID unico universal para una cuenta
     * @return  el accountID
     * */
    public String getNewAccountID(){
        StringBuilder accountID;
        Random rng=new Random();
        int len=10;
        boolean nonUnique;

        do {
            accountID = new StringBuilder();
            for (int i = 0; i < len; i++) {
                accountID.append(((Integer) rng.nextInt(10)));
            }
            nonUnique=false;
            for (Account ac: this.accounts) {
                if(accountID.toString().compareTo(ac.getAccountID())==0){
                    nonUnique=true;
                    break;
                }
            }
        }while(nonUnique);

        return accountID.toString();
    }
    /**
     * Agregar cuenta
     * @param account la cuenta que se agrega
     * */
    public void addAccount(Account account){
        this.accounts.add(account);
    }
    /**
     * Creacion de un nuevo usuario del banco
     * @param firstName  el nombre del usuario
     * @param lastName el apellido del usuario
     * @param pin  el pin del usuario
     * @return el nuevo objeto User*/
    public User addUser(String firstName,String lastName,String pin, String typeAccount){
        User newUser=new User(firstName,lastName,pin,this);
        this.users.add(newUser);

        Account newAccount=new Account(typeAccount,newUser,this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);
        return newUser;
    }
    /**
     * Obtener el obejto User asociado al userId y el pin, si estos son validos
     * @param userId el userID del usuario para iniciar sesion
     * @param pin wl pin del usuario
     * @return el obejeto User, si el inicio de session es exitoso, o null si no lo es
     * */
    public User userLogin(String userId,String pin){
        for (User u: this.users) {
            if(u.getUserID().compareTo(userId)==0 && u.validatePIN(pin)){
                return u;
            }
        }
        return null;
    }
    /**
     * Obtener un usuario por medio del numero de cuenta
     * @param accountNumber el numero de la cuenta
     * @return el objeto User que corresponde a la cuenta
     * */
    public User findUserByAccountNumber(String accountNumber){
        for (Account account: accounts) {
            if(account.getAccountID().equals(accountNumber)){
                return account.getAccountHolder();
            }
        }
        return null;
    }
    /**
     * Obtener el nombre del banco
     * @return  el nombre del banco
     * */
    public String getName() {
        return this.name;
    }
    /**
     * Obtener la lista de cuentas del banco
     * @return  la lista de cuentas
     * */
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    /**
     * Obtener la lista de usuarios del banco
     * @return  la lista de usuarios
     * */
    public ArrayList<User> getUsers() {
        return users;
    }
}
