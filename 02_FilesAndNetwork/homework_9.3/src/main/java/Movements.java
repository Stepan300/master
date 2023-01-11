import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Movements
{
    public String move;
    public int    index;
    public ArrayList<Movements> movements;

    public Movements(String move) {
        this.move = move;
        movements = new ArrayList<>();
        this.movements = getMovementsList();;
    }

// ========Getters of parameters================================================================
    public String getNameOfCompany(int index) throws NullPointerException {
        String nameOfCompany;
        if (index > -1 && index < movements.size()) {
            Movements movement = movements.get(index);
            String[] fragment = movement.toString().split(";");
            nameOfCompany = fragment[0];
        }
        else {
            System.out.println("Incorrect index!");
            return null;
        }
        return nameOfCompany;
    }

    public Double getIncome(int index) throws NullPointerException {
        double income;
        if (index > -1 && index < movements.size()) {
            Movements movement = movements.get(index);
            String[] fragment = movement.toString().split(";");
            income = Double.parseDouble(fragment[1]);
        }
        else {
            System.out.println("Incorrect index!!");
            return null;
        }
        return income;
    }

    public Double getExpense(int index) throws NullPointerException {
        double expense;
        if (index > -1 && index < movements.size()) {
            Movements movement = movements.get(index);
            String[] fragment = movement.toString().split(";");
            expense = Double.parseDouble(fragment[2]);
        }
        else {
            System.out.println("Incorrect index!!!");
            return null;
        }
        return expense;
    }

    public String getDate(int index) throws NullPointerException {
        String day;
        if (index > -1 && index < movements.size()) {
            Movements movement = movements.get(index);
            String[] fragment = movement.toString().split(";");
            day = fragment[3];
        }
        else {
            System.out.println("Incorrect index!!!");
            return null;
        }
        return day;
    }

// ========Sums of incomes and expenses=========================================================

    public Double getIncomeSum() {
        double incomeSum = 0.0;
        try {
            if (index > -1 && index < movements.size()) {
                for (int i = 0; i < movements.size(); i++) {
                    incomeSum += movements.get(i).getIncome(i);
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return incomeSum;
    }

    public Double getExpenseSum() {
        double expenseSum = 0.0;
        try {
            if (index > -1 && index < movements.size()) {
                for (int i = 0; i < movements.size(); i++) {
                    expenseSum += movements.get(i).getExpense(i);
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return expenseSum;
    }

//=========List filling and method toString==============================================

    public ArrayList<Movements> getMovementsList() throws NullPointerException {
        this.movements = Main.getList();
        return movements;
    }

@Override
    public String toString() {
        return move;
    }

//=========Expenses by organizations=======================================================

    public void getExpensesByOrganizations(ArrayList<Movements> movements) {
        HashMap<String, Double> expensesBy = new HashMap<>();
        for (int i = 0; i < movements.size(); i++) {
            String keyName = movements.get(i).getNameOfCompany(i);
            Double valueExp = movements.get(i).getExpense(i);
            if (!expensesBy.containsKey(keyName)) {
                expensesBy.putIfAbsent(keyName, valueExp);
            }
            expensesBy.merge(keyName, valueExp, Double::sum);
        }
        DecimalFormat dF = new DecimalFormat("###,###,##0.00");
        expensesBy.forEach((k, v) -> System.out.printf("%-35s%12s%4s%n", k,
                dF.format(v), "RUR"));
    }
}