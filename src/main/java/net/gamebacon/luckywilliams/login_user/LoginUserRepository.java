package net.gamebacon.luckywilliams.login_user;

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


    @Transactional
    @Modifying
    @Query("UPDATE LoginUser l SET l.password = ?2 WHERE l.id = ?1")
    void setPassword(Long id, String encodedPassword);


    @Query(value = "SELECT is_verified from login_user l  WHERE l.id = ?1", nativeQuery = true)
    boolean getVerified(Long id);

    @Query(value = "SELECT balance FROM login_user l WHERE l.id = ?1", nativeQuery = true)
    double getBalance(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE LoginUser u SET u.balance = (u.balance + ?2) WHERE u.id = ?1")
    void appendBalance(Long id, double append);


}
