package Entities;

import Entities.user.UserEnt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RentEnt extends AbstractEntity {
    public RentEnt(LocalDateTime startDate, WorkerEnt worker, UserEnt user) {
        this.startDate = startDate;
        this.worker = worker;
        this.user = user;
    }

    LocalDateTime startDate;

    LocalDateTime endDate;

    WorkerEnt worker;

    UserEnt user;
}
