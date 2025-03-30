package spring.security;

public final class Roles {
    private Roles() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String ADMIN = "ADMIN";
    public static final String CLIENT = "CLIENT";
    public static final String MANAGER = "MANAGER";


}
