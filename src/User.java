public class User {
    private double balance;

    public User(double balance) {
        this.balance = balance;
    }

    public void addToBalance(double amount) {
        balance += amount;
    }

    public void subtractFromBalance(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}
