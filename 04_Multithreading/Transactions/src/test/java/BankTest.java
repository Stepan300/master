import junit.framework.TestCase;
import java.text.DecimalFormat;

public class BankTest extends TestCase
{
    private final DecimalFormat DF = new DecimalFormat("00000000");
    private final int TRANSFERS_NUMBER = 10000;
    private final int RANGE_GEN_ACCOUNTS = 100000;  // Range of the generation of accounts

    @Override
    protected void setUp() {
//
    }

    public void testTransfer()
    {
        Bank bank = new Bank();
        long allAccounts = bank.getSumAllAccounts();
        System.out.println("Summation of all accounts: " + allAccounts);
        long start = System.currentTimeMillis();

        int cores = Runtime.getRuntime().availableProcessors();
        int threadAm = TRANSFERS_NUMBER / cores;
        int remainder = TRANSFERS_NUMBER % cores;
        int addition = 1;
        int remainAdd = remainder;
        int factor = 0;

        for (int j = 0; j < cores; j++) {
            if (remainder <= 0) {
                addition = 0;
                factor = 1;
            }
            int finalFactor = factor;
            int finalJ = j;
            int finalAddition = addition;
            new Thread(() -> {
                for (int i = 1 + finalJ * (threadAm + finalAddition) + remainAdd * finalFactor;
                        i <= (finalJ + 1) * (threadAm + finalAddition) + remainAdd * finalFactor; i++) {
                    String fromAccountNum = DF.format((int) (Math.random() * (RANGE_GEN_ACCOUNTS - 1) + 1));
                    String toAccountNum = DF.format((int) (Math.random() * (RANGE_GEN_ACCOUNTS - 1) + 1));
                    long transferredAmount;
                    if (Math.random() <= 0.95) {
                        transferredAmount = (long) (Math.random() * (50000 - 100 + 1) + 100);
                    }
                    else {
                        transferredAmount = (long) (Math.random() * (10000000 - 50001 + 1) + 50001);
                    }
                    long expectedFromMon = bank.getBalance(fromAccountNum);
                    long expectedToMon = bank.getBalance(toAccountNum);
                    if (expectedFromMon >= transferredAmount) {
                            expectedFromMon -= transferredAmount;
                            expectedToMon += transferredAmount;
                    }
                    try {
                            bank.transfer(fromAccountNum, toAccountNum, transferredAmount, i, allAccounts,
                                    TRANSFERS_NUMBER, start);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        ex.printStackTrace();
                    }
                    long actualFromMon = bank.getBalance(fromAccountNum);
                    long actualToMon = bank.getBalance(toAccountNum);

                    synchronized (System.out) {
                        System.out.println("ExpectedFrom: " + expectedFromMon + "; ActualFrom: " + actualFromMon
                                + "\nExpectedTo: " + expectedToMon + "; ActualTo: " + actualToMon +
                                "\ntransferredAmount: " + transferredAmount);
                    }

                    assertEquals(expectedFromMon, actualFromMon);
                    assertEquals(expectedToMon, actualToMon);
                }
            }).start();
            remainder--;
        }
//        if (COUNTER.get() == numOfTransfers) {
//            System.out.printf("%n%-35s%15d%5s%n%-35s%20d%5s%n", "\tSum of all accounts from the beginning:",
//                    allAccounts, "rub.", "\tSum of all accounts in the end:", getSumAllAccounts(), "rub.");
        long allAccounts2 = bank.getSumAllAccounts();
        long expected = 0L;
        long actual = allAccounts - allAccounts2;
        assertEquals(expected, actual);
    }

//    @Override
//    protected void tearDown() throws Exception {
//
//    }
}
