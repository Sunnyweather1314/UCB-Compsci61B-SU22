
//"this" keyword can be called on static variables so that no static methods can also access static fields.

public class Account {

    private String name;
    private static int amount;

    public Account(String name) { this.name = name; amount = 0; }

    public Account(String name, int amount){ this.name = name; this.amount = amount; }

    public void deposit(int amount) { this.amount += amount; }

    public boolean withdraw(int amount) {
        if (amount > this.amount) {
        return false;
        } else{
        this.amount -= amount; return true;
        }

    }

    public static void main(String[] args) {
        Account aliceBank = new Account("Alice", 200);
        Account billBank = new Account("Bill", 25);

        aliceBank.withdraw(10);
        System.out.print(aliceBank.getAmount()) ;
        aliceBank.withdraw(1);

        billBank.deposit(61);
    }


    public int getAmount() { return this.amount; }

    public String getName() { return this.name; }



}
