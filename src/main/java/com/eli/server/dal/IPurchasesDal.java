package com.eli.server.dal;

import com.eli.server.dto.PurchaseDto;
import com.eli.server.entities.Purchase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IPurchasesDal extends CrudRepository<Purchase, Long> {



    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name,p.timestamp) " +
            "FROM Purchase p ")
    List<PurchaseDto> getAll(Pageable paging);



    Purchase getById(@Param("purchaseId") long purchaseId);

    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name, p.timestamp) " +
            "FROM Purchase p " +
            "WHERE p.customer.id= :customerId")
    List<PurchaseDto> getAllByCustomer(@Param("customerId") long customerId, Pageable paging);

    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name, p.timestamp) " +
            "FROM Purchase p " +
            "WHERE p.coupon.company.id= :companyId")
    List<PurchaseDto> findAllByCompany(@Param("companyId") long companyId, Pageable paging);

    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name, p.timestamp) " +
            "FROM Purchase p " +
            "WHERE p.coupon.company.id= :companyId AND timestamp>= :startD AND timestamp<= :endD")
    List<PurchaseDto> findAllByCompanyInDateRange(@Param("companyId") long companyId, @Param("startD") Date startD, @Param("endD") Date endD, Pageable paging);

    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name, p.timestamp) " +
            "FROM Purchase p " +
            "WHERE p.coupon.user.id= :userId")
    List<PurchaseDto> findAllByCompanyUser(@Param("userId") long userId, Pageable paging);


    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name, p.timestamp) " +
            "FROM Purchase p " +
            "WHERE timestamp>= :startDate AND timestamp<= :endDate")
    List<PurchaseDto> findAllInDateRange(@Param("startDate")Date startDate,@Param("endDate") Date endDate, Pageable paging);

    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name, p.timestamp) " +
            "FROM Purchase p " +
            "WHERE p.customer.id= :customerId AND timestamp>= :startDate AND timestamp< :endDate")
    List<PurchaseDto> findAllByCustomerInDateRange(@Param("customerId")long customerId,@Param("startDate") Date startDate,@Param("endDate")  Date endDate, Pageable paging);

    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name ,p.timestamp) " +
            "FROM Purchase p " +
            "WHERE p.id= :purchaseId")
    PurchaseDto getDtoById(long purchaseId);


    @Query("SELECT NEW com.eli.server.dto.PurchaseDto(p.id, p.coupon.name, p.customer.user.userName, p.coupon.company.name, " +
            "p.coupon.user.userName, p.coupon.price,p.amount, p.coupon.category.name ,p.timestamp) " +
            "FROM Purchase p " +
            "WHERE p.coupon.category.id= :categoryId")
    List<PurchaseDto> findAllByCategory(long categoryId, Pageable paging);
}
