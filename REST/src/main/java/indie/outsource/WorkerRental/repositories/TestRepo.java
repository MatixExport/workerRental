package indie.outsource.WorkerRental.repositories;

import indie.outsource.WorkerRental.repositories.mongoConnection.MongoConnection;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


@AllArgsConstructor
public class TestRepo {
    MongoConnection mongoConnection;

    public void test(){
        System.out.println(mongoConnection);
    }

}
