package ml.fli.db.repositories;

import ml.fli.db.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
}
