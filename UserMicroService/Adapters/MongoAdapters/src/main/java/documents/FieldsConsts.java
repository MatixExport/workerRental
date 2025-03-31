package documents;

public class FieldsConsts {

    private FieldsConsts() {
        throw new IllegalStateException("Utility class");
    }


    //collection names
    public static final String COLLECTION_ACCOUNT = "user";


    // AbstractEntity Fields
    public static final String ENTITY_ID = "_id";

    //Account Fields
    public static final String ACCOUNT_LOGIN = "login";
    public static final String ACCOUNT_PASSWORD = "password";
    public static final String ACCOUNT_ACTIVE = "active";



}
