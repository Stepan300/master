import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.inc;

public class ProductsUtils {

    public static void addDocument(MongoCollection<Document> collection, String name, int price, int number) {
        Bson filter = and(eq("product_name", name), eq("price", price));
        Bson update = inc("number", number);

        FindOneAndUpdateOptions optionUpsert = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
        Document doc = collection.findOneAndUpdate(filter, update, optionUpsert);

        assert doc != null;
        System.out.println("Add product: " + doc.toJson());
    }

    public boolean findAndUpdateDocument(MongoCollection<Document> colP, String name, int price, int number) {
        Bson filter = and(eq("product_name", name), eq("price", price), gt("number", number));
        Bson filter1 = and(eq("product_name", name), eq("price", price), eq("number", number));
        Bson update = inc("number", -number);

        Document docGt = colP.findOneAndUpdate(filter, update);
        Document docEq = colP.find(filter1).first();
        if (docGt != null) return true;
        else if (docEq != null) {
            colP.findOneAndDelete(filter1);
            return true;
        }
        else return false;
    }

    public static void getAllCollection(MongoCollection<Document> collection) {
        List<Document> products = collection.find().into(new ArrayList<>());
        System.out.println("==========================================================================");
        System.out.println("\tCollection of products:");
        for (Document product : products) {
            System.out.println(product.toJson());
        }
    }
}
