package com.eli.server.dal;

import com.eli.server.dto.SuccessfulLoginDetails;
import com.eli.server.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUsersDal extends CrudRepository<User, Long> {

    User findIdByUserName(String userName);

    List<User> findAll(Pageable paging);

    boolean existsByUserName(String userName);

    @Query(value = "SELECT id, password, user_name, user_type, company_id FROM users WHERE user_type= :userType", nativeQuery = true)
    List<User> findAllByUserType(@Param("userType") String userType, Pageable paging);

    @Query(value = "SELECT id, password, user_name, user_type, company_id FROM users WHERE company_id= :companyId", nativeQuery = true)
    List<User> findAllByCompany(@Param("companyId") long companyId, Pageable paging);

    @Query("SELECT new com.eli.server.dto.SuccessfulLoginDetails(u.id, u.company.id, u.userType) " +
            "FROM User u WHERE u.userName= :userName AND u.password= :password")
    SuccessfulLoginDetails login(@Param("userName") String userName, @Param("password") String password);


}
