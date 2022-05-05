package net.gamebacon.demo.login_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface LoginUserRepository extends JpaRepository<LoginUser, Long> {

    Optional<LoginUser> findByEmail(String email);


    @Transactional
    @Modifying
    @Query("UPDATE LoginUser l SET l.isVerified = 1 WHERE l.email = ?1")
    int enableLoginUser(String email);

}
