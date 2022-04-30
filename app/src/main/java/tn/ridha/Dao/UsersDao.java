package tn.ridha.Dao;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import org.bson.BsonValue;
import org.bson.Document;

import java.util.concurrent.Callable;

import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.result.DeleteResult;
import io.realm.mongodb.mongo.result.InsertOneResult;
import io.realm.mongodb.mongo.result.UpdateResult;
import tn.ridha.main.MainActivity;
import tn.ridha.main.ProfileActivity;

public class UsersDao {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;
    public UsersDao(){
        User user = MainActivity.app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Doctor_appointments");
        mongoCollection = mongoDatabase.getCollection("users");
    }
    public Document get(String key){
        Document filter = new Document();
        filter.append("_id", key);
        return mongoCollection.findOne(filter).get();
    }

    public boolean insert (Document document){
        InsertOneResult result = mongoCollection.insertOne(document).get();
        BsonValue bsonValue = result.getInsertedId();
        if (bsonValue != null){
            return true;
        }else{
            return false;
        }
    }
    public boolean delete (Document document){
        DeleteResult result = mongoCollection.deleteOne(document).get();
        long count  = result.getDeletedCount();
        if (count > 0){
            return true;
        }else{
            return false;
        }
    }

    public void update (Document filter, Document document, Context context){
        System.out.println(filter);
        System.out.println(document);
        mongoCollection.updateOne(filter, document).getAsync(result ->{
            if (result.isSuccess()){
                MainActivity.costumeToast("done", context);
                MainActivity.userData.setName(document.get("name").toString());
                MainActivity.userData.setEmail(document.get("email").toString());
                MainActivity.userData.setBirthdate(document.get("birthdate").toString());
                MainActivity.userData.setCity(document.get("city").toString());
                MainActivity.userData.setPhone(document.get("phone").toString());
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            }else{
                System.out.println(result.getError());
            }
        });

    }
}
