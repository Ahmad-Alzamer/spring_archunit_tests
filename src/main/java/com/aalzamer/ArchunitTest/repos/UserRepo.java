package com.aalzamer.ArchunitTest.repos;

import com.aalzamer.ArchunitTest.models.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserModel, Long> {
}
