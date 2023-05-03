package com.eli.server.dal;

import com.eli.server.dto.BasicCouponDto;
import com.eli.server.entities.Coupon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ICouponsDal extends CrudRepository<Coupon, Long> {


    void deleteByEndDateBefore(@Param("todayDate") java.sql.Date todayDate);

    Coupon findByName(String name);

    List<Coupon> findAll(Pageable paging);

    @Query(value = "SELECT id, description, end_date,img_src, name, price, start_date, category_id, company_id, user_id" +
            " FROM coupons WHERE company_id= :companyId", nativeQuery = true)
    List<Coupon> findAllByCompany(@Param("companyId") long companyId, Pageable paging);

    List<Coupon> findAllByCategory(long categoryId, Pageable paging);

    @Query(value = "SELECT id, description, end_date,img_src, name, price, start_date, category_id, company_id, user_id" +
            " FROM coupons WHERE start_date>= :startDate AND end_date<= :endDate", nativeQuery = true)
    List<Coupon> findAllInDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate, Pageable paging);

    @Query(value = "SELECT id, description, end_date,img_src, name, price, start_date, category_id, company_id, user_id" +
            " FROM coupons WHERE price>= :minPrice AND price<= :maxPrice", nativeQuery = true)
    List<Coupon> findAllInPriceRange(@Param("minPrice") String minPrice, @Param("maxPrice") String maxPrice, Pageable paging);

    @Query(value = "SELECT id, description, end_date,img_src, name, price, start_date, category_id, company_id, user_id" +
            " FROM coupons WHERE user_id= :userId", nativeQuery = true)
    List<Coupon> findAllByCompanyUser(@Param("userId")long userId, Pageable paging);

//    @Query("SELECT NEW com.eli.server.dto.CouponDto(ca.name as category,co.name,co.price,co.endDate)" +
//            "FROM Coupons co JOIN Categories ca ON co.categoryId= ca.id WHERE co.id= :id")
//    CouponDto getBasicDtoById(@Param("id") long couponId);
}
