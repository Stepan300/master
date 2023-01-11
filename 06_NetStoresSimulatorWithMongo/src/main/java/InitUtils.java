import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class InitUtils {

    private static final String STORES = "NetStoresSimulatorWithMongo/Files/Stores.txt";
    private static final String PRODUCTS = "NetStoresSimulatorWithMongo/Files/Products.txt";
    private static final Pattern PATTERN_PRODUCT = Pattern.compile("([а-яА-Я]+)(\\d+)([\\-])(\\d+)");
    private static final String DOCUMENTS_IN_STORE = "NetStoresSimulatorWithMongo/Files/ProdInStores.txt";
    private static final Pattern PATTERN_PRODUCT_IN_STORE = Pattern.compile("([а-я]+)(\\d+)([\\-])(\\d+)([а-яА-Я]+)");

    public static void parsingStoresFile(MongoCollection<Document> colS) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(STORES));
            for (String line : lines) {
                StoresUtils.addDocument(colS, line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parsingProductsFile(MongoCollection<Document> colP) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PRODUCTS));
            for (String line : lines) {
                Matcher m = PATTERN_PRODUCT.matcher(line);
                if (m.find()) {
                    String productName = m.group(1);
                    int price = Integer.parseInt(m.group(2));
                    int number = Integer.parseInt(m.group(4));
                    ProductsUtils.addDocument(colP, productName, price, number);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateDocumentsInStore(MongoCollection<Document> colS, String storeName, String productName,
                                               int price, int number) {
        Bson filter = eq("store_name", storeName);
        Document product = new Document().append("product_name", productName)
                                         .append("price", price)
                                         .append("number", number);
        Bson update = Updates.push("products", product);
        UpdateOptions options = new UpdateOptions().upsert(true);

        colS.updateOne(filter, update, options);
    }

    public static void parsingDocumentsForUpdateInStores(MongoCollection<Document> colS) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(DOCUMENTS_IN_STORE));
            for (String line : lines) {
                Matcher m = PATTERN_PRODUCT_IN_STORE.matcher(line);
                if (m.find()) {
                    String productName = m.group(1);
                    int price = Integer.parseInt(m.group(2));
                    int number = Integer.parseInt(m.group(4));
                    String storeName = m.group(5);
                    updateDocumentsInStore(colS, storeName, productName, price, number);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
