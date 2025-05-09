package connection;

import com.mongodb.client.model.ValidationOptions;
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
    }};

    public static ValidationOptions getSchema(String key) {
        return schemaMap.get(key);
    }
}
