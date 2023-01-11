package main.model;

public enum PriorityTaskType {
    HIGH,
    NORMAL,
    LOW;

    public static String getRandomPriorityType() {
        return values()[(int) (Math.random() * values().length)].toString();
    }
}
