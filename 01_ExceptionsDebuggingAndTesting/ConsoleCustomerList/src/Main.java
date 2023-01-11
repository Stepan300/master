import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
    private static String addCommand = "add Василий Петров " +
            "vasily.petrov@gmail.com +79215637722";
    private static String commandExamples = "\t" + addCommand + "\n" +
            "\tlist\n\tcount\n\tremove Василий Петров";
    private static String commandError = "Wrong command! Available command examples: \n" +
            commandExamples;
    private static String helpText = "Command examples:\n" + commandExamples;
    private static final Pattern PATTERN_COMMAND = Pattern.compile("^[a-zA-Z]{3,6}$",
            Pattern.CASE_INSENSITIVE);

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        CustomerStorage executor = new CustomerStorage();
        for (; ; ) {
            try {
                String command = scanner.nextLine();
                String[] tokens = command.split("\\s+", 2);
                tokens[0] = commandValidate(tokens[0]);
                if (tokens[0].equals("add")) {
                    executor.addCustomer(tokens[1]);
                } else if (tokens[0].equals("list")) {
                    executor.listCustomers();
                } else if (tokens[0].equals("remove")) {
                    executor.removeCustomer(tokens[1]);
                } else if (tokens[0].equals("count")) {
                    System.out.println("There are " + executor.getCount() + " customers");
                } else if (tokens[0].equals("help")) {
                    System.out.println(helpText);
                } else {
                    System.out.println(commandError);
                }
            }
            catch (IllegalArgumentException | NonexistentComponentException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static String commandValidate(String command) {
        Matcher m = PATTERN_COMMAND.matcher(command);
        if (m.find()) {
            command = command.toLowerCase();
        }
        else {
            throw new IllegalArgumentException("Wrong format command. Correct command: " +
                    commandExamples);
        }
        return command;
    }
}
