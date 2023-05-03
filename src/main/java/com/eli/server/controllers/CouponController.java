package com.eli.server.controllers;

import com.eli.server.dto.BasicCouponDto;
import com.eli.server.dto.DetailedCouponDto;
import com.eli.server.entities.Coupon;
import com.eli.server.exceptions.ServerException;
import com.eli.server.logic.CouponsLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private CouponsLogic couponsLogic;

    @Autowired
    public CouponController(CouponsLogic couponsLogic) {
        this.couponsLogic = couponsLogic;
    }


    @PostMapping
    public void add(@RequestHeader String Authorization, @RequestBody Coupon coupon) throws ServerException {
        couponsLogic.add(Authorization, coupon);
    }

    @PutMapping
    public void update(@RequestHeader String Authorization,@RequestBody Coupon coupon) throws ServerException {
        couponsLogic.update(Authorization,coupon);
    }

    @DeleteMapping("{id}")
    public void delete(@RequestHeader String Authorization,@PathVariable("id") long id) throws ServerException {
        couponsLogic.remove(Authorization,id);
    }

    @GetMapping("/basic/{couponId}")
    public BasicCouponDto getBasicById(@PathVariable("couponId") long couponId) throws ServerException {
        return couponsLogic.getBasicById(couponId);
    }

    @GetMapping("/detailed/{couponId}")
    public DetailedCouponDto getDetailedById(@PathVariable("couponId") long couponId) throws ServerException {
        return couponsLogic.getDetailedById(couponId);
    }

    @GetMapping
    public List<BasicCouponDto> getAllBasic(@RequestParam("pageNumber") int pageNumber) throws ServerException {
        return couponsLogic.getAllBasic(pageNumber);
    }

    @GetMapping("/basicByCompany")
    public List<BasicCouponDto> getAllBasicByCompany(@RequestHeader String Authorization, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return couponsLogic.getAllBasicByCompany(Authorization, pageNumber);
    }

    @GetMapping("/basicByCompanyUser")
    public List<BasicCouponDto> getAllBasicByCompanyUser(@RequestHeader String Authorization, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return couponsLogic.getAllBasicByCompanyUser(Authorization, pageNumber);
    }

    @GetMapping("/basicByCategory/{categoryId}")
    public List<BasicCouponDto> getAllBasicByCategory(@PathVariable("categoryId") long categoryId, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return couponsLogic.getAllBasicByCategory(categoryId, pageNumber);
    }

    @GetMapping("/basicInDateRange")
    public List<BasicCouponDto> getAllBasicInDateRange(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return couponsLogic.getAllBasicInDateRange(startDate, endDate, pageNumber);
    }
    @GetMapping("/basicInPriceRange")
    public List<BasicCouponDto> getAllBasicInPriceRange(@RequestParam("minPrice") String minPrice, @RequestParam("maxPrice") String maxPrice, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return couponsLogic.getAllBasicInPriceRange(minPrice,maxPrice,pageNumber);
    }
}
