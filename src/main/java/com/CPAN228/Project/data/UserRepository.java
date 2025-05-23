package com.CPAN228.Project.data;

import com.CPAN228.Project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    Page<User> findAll(Pageable pageable);
}
