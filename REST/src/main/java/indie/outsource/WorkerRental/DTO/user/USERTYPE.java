package indie.outsource.WorkerRental.DTO.user;

import java.util.Objects;

public enum USERTYPE {
    ADMIN("Admin"), CLIENT("Client"), MANAGER("Manager");

    private final String className;


    USERTYPE(String className) {
        this.className = className;
    }

    public static USERTYPE getByClassname(String className) {
        for (USERTYPE status : values()) {
            if (Objects.equals(status.className, className)) {
                return status;
            }
        }
        return null;
    }
}
