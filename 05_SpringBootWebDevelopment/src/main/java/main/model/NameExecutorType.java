package main.model;

public enum NameExecutorType {

    DAVID,
    EMILY,
    HARRY,
    JORDAN,
    SAMUEL,
    JACKSON,
    LORA;

    public static String getRandomNameType() {
        return values()[(int) (Math.random() * values().length)].toString();
    }
}
