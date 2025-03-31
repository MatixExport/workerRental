package connection;

import com.mongodb.client.model.ValidationOptions;
import documents.RentMgd;
import documents.WorkerMgd;
import documents.users.UserMgd;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class MongoSchema {


    private MongoSchema() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final Map<String, ValidationOptions> schemaMap = new HashMap<>() {{
        put(UserMgd.class.getSimpleName(), new ValidationOptions().validator(
                Document.parse("""
                {
                  $jsonSchema:{
                      "bsonType": "object",
                      "required": ["login","password"],
                      "properties":{
                          "login":{
                              "bsonType": "string",
                              "minLength": 3,
                              "maxLength": 30
                          },
                         "password": {
                              "bsonType": "string",
                              "minLength": 3,
                              "maxLength": 300
                         },
                      }
                  }
                }
                """
                )
        ));
        put(RentMgd.class.getSimpleName(), new ValidationOptions().validator(
                Document.parse("""
                                {
                                  "$jsonSchema": {
                                    "bsonType": "object",
                                    "required": [
                                      "startDate",
                                      "worker",
                                      "user"
                                    ],
                                    "properties": {
                                      "startDate": {
                                        "bsonType": "date"
                                      },
                                      "endDate": {
                                        "bsonType": "date"
                                      },
                                       "user": {
                                      "bsonType": "object",
                                      "required": [
                                        "login"
                                      ],
                                      "properties": {
                                        "login": {
                                          "bsonType": "string",
                                          "minLength": 3,
                                          "maxLength": 30
                                        }
                                      }
                                    },
                                      "worker": {
                                        "bsonType": "object",
                                        "required": [
                                          "name"
                                        ],
                                        "properties": {
                                          "name": {
                                            "bsonType": "string",
                                            "minLength": 3,
                                            "maxLength": 30
                                          },
                                          "isRented": {
                                            "bsonType": "int",
                                            "minimum": 0,
                                            "maximum": 1
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
                      "required": ["name"],
                      "properties":{
                      "name":{
                          "bsonType": "string",
                           "minLength": 3,
                           "maxLength": 30
                      },
                      "isRented":{
                         "bsonType": "int",
                         "minimum":0,
                         "maximum":1}
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
