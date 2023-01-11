import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class Main
{
    private static final String CSV_FILE = "files/movementList.csv";
    private static final String DATA_FORMAT = "dd.MM.yy";
    private static ArrayList<Movements> movements;
    public static String move;
    public static String nameOfCompany;
    public static int index;
    public static Double income;
    public static Double expense;
    public static Date date;

    public static void main(String[] args)
    {
        movements = parsingFile();
        System.out.println("\tList:");
        Movements movement = new Movements(move);
        DecimalFormat dF = new DecimalFormat("###,##0.00");
        for (int i = 0; i < movements.size(); i++) {
            System.out.printf("%3d) %-35s%12s%12s%10s%n", (i + 1), movement.getNameOfCompany(i),
                dF.format(movement.getIncome(i)), dF.format(movement.getExpense(i)),
                movement.getDate(i));
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.printf("%n%-20s%12s%4s%n", "Total of  incomes:", dF.format(movement
                        .getIncomeSum()), "RUR");
        System.out.printf("%-20s%12s%4s%n%n", "Total of expenses:", dF.format(movement
                        .getExpenseSum()), "RUR");

        System.out.println("====== Costs by organizations ==========================================");
        for (int i = 0; i < movements.size(); i++) {
            System.out.printf("%3d) %-35s%10s%4s%n", (i+1), movement.getNameOfCompany(i),
                    dF.format(movement.getExpense(i)), "RUR");
        }
        System.out.println("==================================================================\n");
        movement.getExpensesByOrganizations(movements);
    }

    // ===== Parsing file csv ========
    private static ArrayList<Movements> parsingFile()
    {
        movements = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
            int counter = 0;
            for (String line : lines) {
                counter++;
                if (counter > 1) {
                    String[] fragments = line.split(",");
                    if (fragments.length == 9) {
                        fragments[7] = fragments[7].replaceAll("[^\\d]", "") +
                                "." + fragments[8].replaceAll("[^\\d]", "");
                    }
                    if (fragments.length != 8 && fragments.length != 9) {
                        System.out.println("\nWrong line: " + line + "\n");
                        continue;
                    }
                    System.out.println((counter-1) + ") Income - " + fragments[6] +
                            "; Expense - " + fragments[7] + "; Data - " + fragments[3]);
                    String[] parts = fragments[5].split("\s{4,}");
                    String[] pieces = parts[1].split("[/|\\\\]");
                    String nameOfCompany = null;
                    for (int i = 1; i < pieces.length; i++) {
                        if (i == 1) {
                            nameOfCompany = pieces[i];
                        }
                        else {
                            nameOfCompany = nameOfCompany + " " + pieces[i];
                        }
                    }
                    System.out.println("Name of company: " + nameOfCompany);
                    movements.add(new Movements(nameOfCompany + ";" + fragments[6] + ";" +
                            fragments[7] + ";" + fragments[3]));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Size 1: " + movements.size());
        return movements;
    }

    public static ArrayList<Movements> getList() throws NullPointerException {
        return movements;
    }
}

