package indie.outsource.WorkerRental.repositories.mongoConnection;

import com.mongodb.client.model.ValidationOptions;
import indie.outsource.WorkerRental.documents.RentMgd;
import indie.outsource.WorkerRental.documents.WorkerMgd;
import indie.outsource.WorkerRental.documents.user.UserMgd;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class MongoSchema {
    private static final Map<String, ValidationOptions> schemaMap = new HashMap<String, ValidationOptions>() {{
        put(UserMgd.class.getSimpleName(), new ValidationOptions().validator(
                Document.parse("""
                {
                  $jsonSchema:{
                      "bsonType": "object",
                      "required": ["product","productInfo"],
                      "properties":{
                      "productInfo":{
                          "bsonType": "object",
                          "required": ["quantity"],
                          "properties":{
                              "quantity": {
                                  "bsonType": "int",
                                  "minimum":0
                              }
                          }
            
                      }
                      }
                  }
                }
                """
                )
        ));
        put(RentMgd.class.getSimpleName(), new ValidationOptions().validator(
                Document.parse("""
                {
                  $jsonSchema:{
                      "bsonType": "object",
                      "required": ["product","productInfo"],
                      "properties":{
                      "productInfo":{
                          "bsonType": "object",
                          "required": ["quantity"],
                          "properties":{
                              "quantity": {
                                  "bsonType": "int",
                                  "minimum":0
                              }
                          }
            
                      }
                      }
                  }
                }
                """
                )
        ));
        put(WorkerMgd.class.getSimpleName(), new ValidationOptions().validator(
                Document.parse("""
                {
                  $jsonSchema:{
                      "bsonType": "object",
                      "required": ["product","productInfo"],
                      "properties":{
                      "productInfo":{
                          "bsonType": "object",
                          "required": ["quantity"],
                          "properties":{
                              "quantity": {
                                  "bsonType": "int",
                                  "minimum":0
                              }
                          }
            
                      }
                      }
                  }
                }
                """
                )
        ));
    }};

    public static ValidationOptions getSchema(String key) {
        return schemaMap.get(key);
    }
}
