package spring.security;

public final class Roles {
    private Roles() {
        throw new RuntimeException("Utility class");
    }

    public static final String ADMIN = "ADMIN";
    public static final String CLIENT = "CLIENT";
    public static final String MANAGER = "MANAGER";


}
