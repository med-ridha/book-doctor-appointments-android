package tn.ridha.Dao;

import org.bson.BsonValue;
import org.bson.Document;

import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.result.DeleteResult;
import io.realm.mongodb.mongo.result.InsertOneResult;
import io.realm.mongodb.mongo.result.UpdateResult;
import tn.ridha.main.MainActivity;

public class AppointmentsDao {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    public AppointmentsDao(){
        User user = MainActivity.app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Doctor_appointments");
         mongoCollection = mongoDatabase.getCollection("appointments");
    }
    public Document getOne(String key){
        Document filter = new Document();
        filter.append("_id", key);
        return mongoCollection.findOne(filter).get();
    }

    public FindIterable<Document> getMany(String field, String key){
        Document filter = new Document();
        filter.append(field, key);
        return mongoCollection.find(filter);
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

    public boolean update (Document filter, Document document){
        UpdateResult result = mongoCollection.updateOne(filter, document).get();
        if (result.getModifiedCount() > 0){
            return true;
        }else{
            return false;
        }
    }
}
