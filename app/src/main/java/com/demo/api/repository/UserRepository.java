package com.demo.api.repository;

import com.demo.api.domain.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Profile("!test")
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u ORDER BY id")
    Page<User> findAllUsersWithPagination(Pageable pageable);

    @Query(value = "SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> searchByNameLike(@Param("name") String name);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByCpf(String cpf);
}
