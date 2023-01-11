package main.model;

public enum StatusTaskType {
    PLANNING,
    APPROVED,
    PENDING_REVIEW;

    public static String getRandomStatusType() {
        return values()[(int) (Math.random() * values().length)].toString();
    }
}
