import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerStorage
{
    private HashMap<String, Customer> storage;
    private final Pattern PATTERN_MAIL = Pattern.compile("(^[a-zA-Z0-9]+"+"((\\.|_|-?)" +
            "[a-zA-Z0-9]+)*)"+"(@)"+"([a-zA-Z0-9]+"+"((\\.|_|-?)[a-zA-Z0-9]+)*)"+
            "(\\.[a-zA-Z]{2,}$)", Pattern.CASE_INSENSITIVE);
    private final Pattern PATTERN_NAME = Pattern.compile("(^[a-zA-Zа-яА-Я]+)"+"(\\s+)"+
            "([a-zA-Zа-яА-Я]+$)");

    public CustomerStorage()
    {
        storage = new HashMap<>();
    }

    public void addCustomer(String data)
    {
        String[] components = data.split("\\s+");
        if (components.length != 4) {
            throw new IllegalArgumentException("Wrong format. Correct format:\n" +
                    "add Василий Петров vasily.petrov@gmail.com +79215637722");
        }
        emailValidate(components[2]);
        components[3] = phoneValidate(components[3]);
        String name = components[0] + " " + components[1];
        name = nameValidate(name);
        storage.put(name, new Customer(name, components[3], components[2]));
        System.out.println("Customer was added!");
    }

    public void emailValidate(String email) {
        Matcher m = PATTERN_MAIL.matcher(email);
        if (m.find()) {
            System.out.println("Email validate! ");
        }
        else {
            throw new IllegalArgumentException("Wrong format Email. Correct format:\n" +
                    "vasily.petrov@gmail.com");
        }
    }

    public String nameValidate(String name) {
        Matcher m = PATTERN_NAME.matcher(name);
        if (m.find()) {
            name = m.group(1).substring(0, 1).toUpperCase() + m.group(1).substring(1).toLowerCase() +
                    " " + m.group(3).substring(0, 1).toUpperCase() +
                    m.group(3).substring(1).toLowerCase();
        }
        else {
            throw new IllegalArgumentException("Wrong format Name. Correct format:\n" +
                    "Василий Петров");
        }
        return name;
    }

    public String phoneValidate(String phoneNum) {
        int countSim = 0;
        String phone = null;
        String clPhNum = phoneNum.trim().replaceAll("[^\\d]", "");
        StringBuilder builder = new StringBuilder(clPhNum);
        for (int i = 0; i < clPhNum.length(); i++) {
            countSim += 1;
        }
        if (countSim != 10 && countSim != 11) {
            throw new IllegalArgumentException("Wrong phone format. Correct format:\n" +
                    "+79215637722");
        }
        if (countSim == 10) {
            builder = builder.replace(0, 0, "+7");
            phone = builder.toString();
        }
        if (countSim == 11) {
            if ("7".equals(clPhNum.substring(0, 1)) || "8".equals(clPhNum.substring(0, 1))) {
                builder = builder.replace(0, 1, "+7");
                phone = builder.toString();
            }
        }
        return phone;
    }

    public void listCustomers()
    {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) throws NonexistentComponentException
    {
        if (storage.containsKey(name)) {
            storage.remove(name);
            System.out.println(name + " successfully deleted");
        }
        else {
            throw new NonexistentComponentException("Nonexistent collection item! " + name +
            "\nAdvice: To get acquainted with the scroll, type a 'list'");
        }
    }

    public int getCount()
    {
        return storage.size();
    }
}