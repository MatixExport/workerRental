package indie.outsource.event.user;

import java.util.UUID;

public record UserEvent(
        String login,
        UUID id
) {
}
