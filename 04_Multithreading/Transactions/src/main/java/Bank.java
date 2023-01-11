import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Bank
{
    private final HashMap<String, Account> accounts;
    private final AtomicInteger COUNTER = new AtomicInteger(0);
    // =====================================================

    public Bank() {
        this.accounts = new HashMap<>();
        initializeBank();
    }

    public void initializeBank() {
        long money;
        int accountsNumber = 100000;
        DecimalFormat dF = new DecimalFormat("00000000");
        for (int i = 1; i <= accountsNumber; i++) {
            String key = dF.format(i);
            if (i <= (accountsNumber / 20)) {
                money = (long) (Math.random() * (100000000 - 5000 + 1) + 5000);
            }
            else {
                money = (long) (Math.random() * (300000 - 25000 + 1) + 25000);
            }
            accounts.put(key, new Account(key, money, true));
        }
    }

    public void isFraud(String fromAccountNum, String toAccountNum) throws InterruptedException {
        Random random = new Random();
        boolean verdict = random.nextBoolean();
        if (verdict) {
            synchronized (accounts.get(fromAccountNum)) {
                synchronized (accounts.get(toAccountNum)) {
                        accounts.put(fromAccountNum, new Account(fromAccountNum, accounts.get(fromAccountNum)
                                .getMoney(), false));
                        accounts.put(toAccountNum, new Account(toAccountNum, accounts.get(toAccountNum)
                                .getMoney(), false));
                }
            }
        }
        System.out.println("Fraud verdict: " + verdict);
        Thread.sleep(1000);
    }

    public void transfer(String fromAccountNum, String toAccountNum, long amount, int number,
                         long allAccounts, int numOfTransfers, long time) throws InterruptedException
    {
        if (!accounts.containsKey(fromAccountNum) || !accounts.containsKey(toAccountNum)) {
            COUNTER.getAndIncrement();         // Если счета не найдены
            System.out.println("\t\tCounter: " + COUNTER);
            System.out.println("\tBank account " + fromAccountNum + " or " + toAccountNum + " not found!");
        }
        else if (!accounts.get(fromAccountNum).getLegal() || !accounts.get(toAccountNum).getLegal()) {
            COUNTER.getAndIncrement();         // Если счета заблокированы
            System.out.println("\t\tCounter: " + COUNTER);
            System.out.println("\t\t\t\t\t\t\t\t\tAttention!!! Transfer " + number + " is not possible - " +
                    "accounts are blocked.");
        }
        else if (accounts.get(fromAccountNum).getMoney() < amount) {
            COUNTER.getAndIncrement();         // Если на счету недостаточно средств
            System.out.println("\t\tCounter: " + COUNTER);
            System.out.println("\tTransfer " + number + " between " + fromAccountNum + " and " +
                    toAccountNum + " to amount: " + amount + " is not possible - insufficient funds");
        }
        else {                                 // Если всё нормально и на счету достаточно средств
            int accountFrom = Integer.parseInt(fromAccountNum);
            int accountTo = Integer.parseInt(toAccountNum);
            if (accountFrom > accountTo) {
                synchronized (accounts.get(fromAccountNum)) {
                    synchronized (accounts.get(toAccountNum)) {
                        COUNTER.getAndIncrement();
                        System.out.println("\t\tCounter: " + COUNTER);
                        accounts.put(fromAccountNum, new Account(fromAccountNum, accounts.get(fromAccountNum)
                                .writeMoneyOff(amount), true));
                        accounts.put(toAccountNum, new Account(toAccountNum, accounts.get(toAccountNum)
                                .addMoneyTo(amount), true));
                        System.out.println("Transfer " + number + " from " + fromAccountNum + " to " +
                                toAccountNum + " was successful to amount: " + amount + " rub.");
                    }
                }
            }
            else if (accountFrom < accountTo){
                synchronized (accounts.get(toAccountNum)) {
                    synchronized (accounts.get(fromAccountNum)) {
                        COUNTER.getAndIncrement();
                        System.out.println("\t\tCounter: " + COUNTER);
                        accounts.put(fromAccountNum, new Account(fromAccountNum, accounts.get(fromAccountNum)
                                .writeMoneyOff(amount), true));
                        accounts.put(toAccountNum, new Account(toAccountNum, accounts.get(toAccountNum)
                                .addMoneyTo(amount), true));
                        System.out.println("Transfer " + number + " from " + fromAccountNum + " to " +
                                toAccountNum + " was successful to amount: " + amount + " rub.");
                    }
                }
            }
            else {                              // Если сгенерировано два одинаковых счёта
                COUNTER.getAndIncrement();
                System.out.println("\t\tCounter: " + COUNTER);
                System.out.println("\tError! recipient account is missing");
            }
            if (amount > 50000) {                    // Если сумма нуждается в проверке
                isFraud(fromAccountNum, toAccountNum);
            }
        }
        if (COUNTER.get() == numOfTransfers) {
            System.out.printf("%n%-35s%15d%5s%n%-35s%20d%5s%n", "\tSum of all accounts from the beginning:",
                allAccounts, "rub.", "\tSumma of all accounts in the end:", getSumAllAccounts(), "rub.");
            System.out.println("Duration: " + (System.currentTimeMillis() - time));
        }
    }

    public long getBalance(String accountNum) {return this.accounts.get(accountNum).getMoney();}

    public long getSumAllAccounts() {
        return accounts.values().stream().mapToLong(Account::getMoney).sum();
    }
}