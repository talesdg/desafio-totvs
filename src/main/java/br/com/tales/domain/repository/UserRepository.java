package br.com.tales.domain.repository;

import br.com.tales.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByLogin (String login);
}
