
public class Account {

    private String accNumber;
    private long money;
    private boolean legal;
 // ============================================================

    public Account(String accNumber, long money, boolean legal) {
        this.accNumber = accNumber;
        this.money = money;
        this.legal = legal;
    }

    public Long addMoneyTo(Long amount) {
        setMoney(getMoney() + amount);
        return this.money;
    }

    public Long writeMoneyOff(Long amount) {
        setMoney(getMoney() - amount);
        return this.money;
    }

    public Long getMoney() {return this.money;}

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return this.accNumber;
    }

    public void setAccNumber(String accNumber) {this.accNumber = accNumber;}

    public boolean getLegal() {return this.legal;}

    public void setLegal(boolean legal) {this.legal = legal;}

    @Override
    public String toString() {
        return "\n\tAccount number: " + getAccNumber() + "; Money: " + getMoney() + "; Legal: " + getLegal();
    }
}
