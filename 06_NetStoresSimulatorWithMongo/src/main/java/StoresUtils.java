import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Updates.*;

public class StoresUtils {

    private static final DecimalFormat dF = new DecimalFormat("###,##0.00");

    public static void addDocument(MongoCollection<Document> collection, String storeName) {
        Bson filter = eq("store_name", storeName);
        Bson update = set("products", List.of());
        FindOneAndUpdateOptions optionUpsert = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
        Document element = collection.findOneAndUpdate(filter, update, optionUpsert);
        assert element != null;
        System.out.println("Store: " + element.toJson());
    }

    protected static void displayProduct(MongoCollection<Document> colS, MongoCollection<Document> colP, String storeName, String productName, int price, int number) {
        StoresUtils sU = new StoresUtils();
        if (sU.findDocument(colS, storeName)) {
            ProductsUtils pU = new ProductsUtils();
            if (pU.findAndUpdateDocument(colP, productName, price, number)) {
                Bson filter1 = eq("store_name", storeName);
                Bson filter2 = Projections.elemMatch("products", and(eq("product_name", productName), eq("price", price)));
                Bson filters = combine(filter1, filter2);

                Document docPr = colS.find(filters).first();
                System.out.println("It's result of finding document in the store: " + docPr);

                if (docPr != null) {
                    Bson update = inc("products.$.number", number);
                    FindOneAndUpdateOptions optionUp = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER);

                    Document addProd = colS.findOneAndUpdate(filters, update, optionUp);
                    assert addProd != null;
                    System.out.println("Product in the store had incremented! " + addProd.toJson());
                }
                else {
                    Bson update = addToSet("products", new Document("product_name", productName)
                                                                     .append("price", price)
                                                                     .append("number", number));
                    FindOneAndUpdateOptions optionAft = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER);

                    Document newProd = colS.findOneAndUpdate(filter1, update, optionAft);
                    assert newProd != null;
                    System.out.println("Display product: " + newProd.toJson());
                }
            }
            else {
                System.out.println("Add product to the 'products' database!");
            }
        } else {
            System.out.println("The store not found! Add new store.");
        }
    }

    public static void getNumberOfProductsInTheStores(MongoCollection<Document> colS) {
        AggregateIterable<Document> productsInStore = colS.aggregate(List.of(unwind("$products"),
                project(fields(include("store_name"), include("products.price"), excludeId())),
                group("$store_name", sum("count", 1), avg("avgPrice", "$products.price"),
                Accumulators.max("maxPrice", "$products.price"), Accumulators.min("minPrice",
                "$products.price")), sort(ascending("count"))));
        System.out.println("\n===================== Total number, average price, minimum and maximum of named items in " +
                "each store ==================================");
        for (Document doc : productsInStore) {
            System.out.printf("%29s%10s%7s%4s;%11s%9s;%11s%7s;%11s%7s;%n", "\tNumber of total products in", doc.get("_id"), "store:",
                    doc.get("count"), "Average:", dF.format(doc.get("avgPrice")), "Maximum:", doc.get("maxPrice"),
                    "Minimum:", doc.get("minPrice"));
        }
    }

    public static void numberOfProductsCheaper100rubInEveryStore(MongoCollection<Document> colS) {
        AggregateIterable<Document> numberMinus100 = colS.aggregate(List.of(unwind("$products"), project(fields(
                include("store_name"), include("products.price"), excludeId())), match(lt("products.price", 100)),
                group("$store_name", sum("count", 1)), sort(ascending("count"))));
        System.out.println("\n================== Number of named items in each store cheaper then 100 rubles ====================");
        for (Document doc : numberMinus100) {
            System.out.printf("%40s%10s%7s%4s;%n", "\tNumber of items cheaper then 100 rubles in", doc.get("_id"),
                    "store:", doc.get("count"));
        }
    }

    private boolean findDocument(MongoCollection<Document> collection, String storeName) {
        Bson filter = eq("store_name", storeName);
        return collection.find(filter).first() != null;
    }

    public static void getAllCollection(MongoCollection<Document> collection) {
        List<Document> stores = collection.find().into(new ArrayList<>());
        System.out.println("====================================================================");
        System.out.println("\tCollection of stores:");
        for (Document store : stores) {
            System.out.println(store.toJson());
        }
    }
}