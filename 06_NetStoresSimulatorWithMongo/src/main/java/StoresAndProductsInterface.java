import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoresAndProductsInterface {

    private static final Pattern PATTERN_COMMAND = Pattern.compile("([a-z]+)([_]?)([a-z]*)([а-яА-Я]*)(\\d*)" +
            "([\\-]?)(\\d*)([а-яА-Я]*)", Pattern.CASE_INSENSITIVE);

    private static void printHelpMenu() {
        System.out.println("Наберите 'add_store' + название магазина без пробела (ru) - добавить магазин в базу;\n" +
                "Наберите 'add_product' + название товара (ru) + цена-количество через тире всё без пробелов" +
                " - добавить товар в базу;\nНаберите 'display_product' + название магазина где будет " +
                "продаваться товар (ru) + цена-количество через тире + название товара (ru) всё без пробелов " +
                "- добавление товара в магазин;\nНаберите 'statistic' - статистика по магазинам;" +
                "\nНазвание магазинов и товаров на русском языке;\nДля вывода данного сообщения - 'help';\n" +
                "Для выхода из меню - 'exit';\n\t==========================================================");
    }
    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
        MongoDatabase db = mongoClient.getDatabase("NetStores");
        MongoCollection<Document> colS = db.getCollection("stores");
        MongoCollection<Document> colP = db.getCollection("products");
        colS.drop();
        colP.drop();

        InitUtils.parsingStoresFile(colS);
        InitUtils.parsingProductsFile(colP);
        InitUtils.parsingDocumentsForUpdateInStores(colS);

        printHelpMenu();
        System.out.println("Database: " + db.getName() + "; ColS: " + colS.getNamespace() + "; ColP: " + colP.getNamespace());

        Scanner sc = new Scanner(System.in);
        boolean breaker = true;
        while (breaker) {
            String input = sc.nextLine();
            Matcher m = PATTERN_COMMAND.matcher(input);
            String command = getCommand(m);
            switch (command) {
                case "ADD_STORE" -> {
                    String nameOfStore = getNameOfStoreOrProductName(m);
                    System.out.println("Name of Store: " + nameOfStore);

                    StoresUtils.addDocument(colS, nameOfStore);
                }
                case "ADD_PRODUCT" -> {
                    String prodName = getNameOfStoreOrProductName(m);
                    int price = getPrice(m);
                    int numberProduct = getNumberOfProduct(m);
                    System.out.println("Name of product: " + prodName + "; Price: " + price + "; Number of product: " + numberProduct);

                    ProductsUtils.addDocument(colP, prodName, price, numberProduct);
                }
                case "DISPLAY_PRODUCT" -> {
                    String prodName = getNameOfStoreOrProductName(m);
                    int price = getPrice(m);
                    int numberOfProduct = getNumberOfProduct(m);
                    String nameOfStore = getNameOfStoreWhereDisplayProduct(m);
                    System.out.println("Name of product: " + prodName + "; Price: " + price + "; Number : "
                            + numberOfProduct + "; Name of store: " + nameOfStore);

                    StoresUtils.displayProduct(colS, colP, nameOfStore, prodName, price, numberOfProduct);
                }
                case "STATISTIC" -> {
                    ProductsUtils.getAllCollection(colP);
                    StoresUtils.getAllCollection(colS);
                    StoresUtils.getNumberOfProductsInTheStores(colS);
                    StoresUtils.numberOfProductsCheaper100rubInEveryStore(colS);
                }
                case "HELP" -> printHelpMenu();
                case "EXIT" -> {
                    breaker = false;
                    System.out.println("Exit has successful done!");
                }
                default -> System.out.println("Uncorrected command!  Repeat please...");
            }
        }
        db.drop();
    }

    private static String getCommand(Matcher m) {
        String command = null;
        if (m.find()) {
            if (m.group(1).length() != 0 && m.group(2).length() == 0) {
                command = m.group(1).trim().toUpperCase();
               System.out.println("Command: " + command);
            }
            else if (m.group(1).length() != 0 && m.group(2).length() != 0 && m.group(3).length() != 0) {
                command = m.group(1).toUpperCase() + m.group(2).toUpperCase() + m.group(3).toUpperCase();
            }
            else {
                System.out.println("Attention! It's a wrong command. Look at the HelpMenu for correct query.");
                command = "HELP";
            }
        }
        else {
            System.out.println("Incorrect command format!!");
            command = "DEFAULT";
        }
        return command;
    }

    private static String getNameOfStoreOrProductName(Matcher m) {
        return m.group(4);
    }

    private static int getPrice(Matcher m) {
        return Integer.parseInt(m.group(5));
    }

    private static int getNumberOfProduct(Matcher m) {
        return Integer.parseInt(m.group(7));
    }

    private static String getNameOfStoreWhereDisplayProduct(Matcher m) {
        return m.group(8);
    }
}
