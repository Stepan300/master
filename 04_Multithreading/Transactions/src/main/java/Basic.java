import java.text.DecimalFormat;

public class Basic
{
    private static final DecimalFormat DF = new DecimalFormat("00000000");
    private static final int TRANSFERS_NUMBER = 1000000;
    private static final int RANGE_GEN_ACCOUNTS = 100000;  // Range of the generation of accounts

    public static void main(String[] args) throws IllegalThreadStateException {

        Bank bank = new Bank();
        long allAccounts = bank.getSumAllAccounts();

        int cores = Runtime.getRuntime().availableProcessors();
        int threadAm = TRANSFERS_NUMBER / cores;
        int remainder = TRANSFERS_NUMBER % cores;
        int addition = 1;
        int remainAdd = remainder;
        int factor = 0;
        long start = System.currentTimeMillis();

        for (int j = 0; j < cores; j++) {
            if (remainder <= 0) {
                addition = 0;
                factor = 1;
            }
            int finalFactor = factor;
            int finalJ = j;
            int finalAddition = addition;
            Runnable task = () -> {
                for (int i = 1 + finalJ * (threadAm + finalAddition) + remainAdd * finalFactor;
                     i <= (finalJ + 1) * (threadAm + finalAddition) + remainAdd * finalFactor; i++) {

                    String fromAccountNum = DF.format((int) (Math.random() * (RANGE_GEN_ACCOUNTS - 1) + 1));
                    String toAccountNum = DF.format((int) (Math.random() * (RANGE_GEN_ACCOUNTS - 1) + 1));

                    long transferredMoney;
                    if (Math.random() <= 0.95) {
                        transferredMoney = (long) (Math.random() * (50000 - 100 + 1) + 100);
                    } else {
                        transferredMoney = (long) (Math.random() * (10000000 - 50001 + 1) + 50001);
                    }

                    long expBalFrom = bank.getBalance(fromAccountNum);
                    long expBalTo = bank.getBalance(toAccountNum);
                    System.out.println("\n\tAccount " + fromAccountNum + " before transfer " +
                            expBalFrom + " rub.");
                    System.out.println("\tAccount " + toAccountNum + " before transfer " +
                            expBalTo + " rub.");
                    expBalFrom -= transferredMoney;
                    expBalTo += transferredMoney;

                    try {
                        bank.transfer(fromAccountNum, toAccountNum, transferredMoney, i, allAccounts,
                                TRANSFERS_NUMBER, start);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        ex.printStackTrace();
                    }

                    long actBalFrom = bank.getBalance(fromAccountNum);
                    long actBalTo = bank.getBalance(toAccountNum);
                    System.out.println("\tAccount " + fromAccountNum + " after transfer Expected: " +
                            expBalFrom + " rub. Actual: " + actBalFrom + " rub.");
                    System.out.println("\tAccount " + toAccountNum + " after transfer Expected: " +
                            expBalTo + " rub. Actual: " + actBalTo + " rub.");
                }
            };
            Thread thread = new Thread(task);
            thread.start();
            remainder--;
        }
    }
}