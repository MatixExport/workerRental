package indie.outsource.ApplicationCore.model;

import indie.outsource.ApplicationCore.model.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rent extends AbstractEntity {
    public Rent(LocalDateTime startDate, Worker worker, User user) {
        this.startDate = startDate;
        this.worker = worker;
        this.user = user;
    }

    LocalDateTime startDate;

    LocalDateTime endDate;

    Worker worker;

    User user;
}
