package main.model;

public enum PositionExecutorType {

    MANAGER,
    HEAD_DEPARTMENT,
    LAWYER,
    DISPATCHER,
    SECRETARY,
    ACCOUNTANT;

    public static String getRandomPositionType() {
        return values()[(int) (Math.random() * values().length)].toString();
    }
}
