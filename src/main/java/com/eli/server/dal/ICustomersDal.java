package com.eli.server.dal;

import com.eli.server.dto.CustomerDto;
import com.eli.server.entities.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICustomersDal extends CrudRepository<Customer,Long> {

    @Query("SELECT NEW com.eli.server.dto.CustomerDto(u.userName,u.userType,c.address,c.phoneNumber,c.childrenAmount)" +
            "FROM User u JOIN Customer c ON u.id= c.user.id")
    List<CustomerDto> getAllDto(Pageable paging);
//
//    @Query("SELECT NEW com.eli.server.dto.CustomerDto(u.userName,u.password,c.address,c.phoneNumber,c.childrenAmount)" +
//            "FROM Users u join Customers c on u.id= c.id")
//    List<CustomerDto> getAll(Pageable paging);
}
