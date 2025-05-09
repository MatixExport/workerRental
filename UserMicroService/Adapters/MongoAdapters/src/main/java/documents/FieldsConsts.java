package documents;

public class FieldsConsts {

    private FieldsConsts() {
        throw new IllegalStateException("Utility class");
    }


    //collection names
    public static final String COLLECTION_ACCOUNT = "user";
    public static final String COLLECTION_WORKER = "worker";
    public static final String COLLECTION_RENT = "rent";


    // AbstractEntity Fields
    public static final String ENTITY_ID = "_id";

    //Account Fields
    public static final String ACCOUNT_LOGIN = "login";
    public static final String ACCOUNT_PASSWORD = "password";
    public static final String ACCOUNT_ACTIVE = "active";

    //Worker Fields
    public static final String WORKER_NAME = "name";
    public static final String WORKER_IS_RENTED = "isRented";

    //Rent Fields
    public static final String RENT_START_DATE = "startDate";
    public static final String RENT_END_DATE = "endDate";
    public static final String RENT_WORKER = "worker";
    public static final String RENT_USER = "user";


}
