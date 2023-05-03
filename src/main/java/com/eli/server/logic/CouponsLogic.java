package com.eli.server.logic;


import com.eli.server.constants.Consts;
import com.eli.server.dal.ICouponsDal;
import com.eli.server.dto.BasicCouponDto;
import com.eli.server.dto.DetailedCouponDto;
import com.eli.server.entities.Company;
import com.eli.server.entities.Coupon;
import com.eli.server.entities.User;
import com.eli.server.enums.ErrorType;
import com.eli.server.enums.UserType;
import com.eli.server.exceptions.ServerException;
import com.eli.server.utils.JWTUtils;
import com.eli.server.utils.ValidatorUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponsLogic {

    private ICouponsDal iCouponsDal;
    private CategoriesLogic categoriesLogic;
    private CompaniesLogic companiesLogic;
    private UsersLogic usersLogic;

    @Autowired
    public CouponsLogic(ICouponsDal iCouponsDal, CategoriesLogic categoriesLogic, CompaniesLogic companiesLogic, UsersLogic usersLogic) {
        this.iCouponsDal = iCouponsDal;
        this.categoriesLogic = categoriesLogic;
        this.companiesLogic = companiesLogic;
        this.usersLogic = usersLogic;
    }


    public void add(String authorization, Coupon coupon) throws ServerException {

        Long id = ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.COMPANY);

        User user = usersLogic.getById(id);
        Company company = user.getCompany();
        coupon.setUser(user);
        coupon.setCompany(company);

        validateCoupon(coupon);
        validateNameForCreate(coupon);

        try {
            iCouponsDal.save(coupon);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public void update(String authorization, Coupon coupon) throws ServerException {

        Long id = ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.COMPANY);

        User user = usersLogic.getById(id);
        Coupon coupon1 = findById(coupon.getId());
        validateCouponPublisher(user, coupon1);

        validateCoupon(coupon);
        validateNameForUpdate(coupon);
        try {
            iCouponsDal.save(coupon);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public void remove(String authorization, long couponId) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        validateCouponExist(couponId);
        validatePermission(authorization,couponId);

        try {
            iCouponsDal.deleteById(couponId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public BasicCouponDto getBasicById(long couponId) throws ServerException {

        validateCouponExist(couponId);
        try {
            Coupon coupon = iCouponsDal.findById(couponId).get();
            return extractBasicDto(coupon);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public DetailedCouponDto getDetailedById(long couponId) throws ServerException {

        validateCouponExist(couponId);
        try {
            Coupon coupon = iCouponsDal.findById(couponId).get();
            return extractDetailedDto(coupon);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<BasicCouponDto> getAllBasic(int pageNumber) throws ServerException {

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            List<Coupon> coupons = iCouponsDal.findAll(paging);
            return extractBasicDtoToList(coupons);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);

        }
    }

    public List<BasicCouponDto> getAllBasicByCompany(String authorization, int pageNumber) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.COMPANY);

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        Claims claims = JWTUtils.decodeJWT(authorization);
        String strCompanyId = claims.getAudience();
        long companyId = Long.parseLong(strCompanyId);
        try {
            List<Coupon> coupons = iCouponsDal.findAllByCompany(companyId, paging);
            return extractBasicDtoToList(coupons);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<BasicCouponDto> getAllBasicByCategory(long categoryId, int pageNumber) throws ServerException {

        categoriesLogic.validateCategoryExist(categoryId);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            List<Coupon> coupons = iCouponsDal.findAllByCategory(categoryId, paging);
            return extractBasicDtoToList(coupons);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<BasicCouponDto> getAllBasicInDateRange(String startDate, String endDate, int pageNumber) throws ServerException {

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            List<Coupon> coupons = iCouponsDal.findAllInDateRange(startDate, endDate, paging);
            return extractBasicDtoToList(coupons);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<BasicCouponDto> getAllBasicInPriceRange(String minPrice, String maxPrice, int pageNumber) throws ServerException {

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            List<Coupon> coupons = iCouponsDal.findAllInPriceRange(minPrice, maxPrice, paging);
            return extractBasicDtoToList(coupons);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);

        }
    }

    public List<BasicCouponDto> getAllBasicByCompanyUser(String authorization, int pageNumber) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.COMPANY);


        Claims claims = JWTUtils.decodeJWT(authorization);
        String strUserId = claims.getIssuer();
        long userId = Long.parseLong(strUserId);

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            List<Coupon> coupons = iCouponsDal.findAllByCompanyUser(userId, paging);
            return extractBasicDtoToList(coupons);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    Coupon findById(long couponId) throws ServerException {

        validateCouponExist(couponId);
        try {
            return iCouponsDal.findById(couponId).get();
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    private BasicCouponDto extractBasicDto(Coupon coupon) {

        long id = coupon.getId();
        String name = coupon.getName();
        float price = coupon.getPrice();
        String endDate = String.valueOf(coupon.getEndDate());
        String imgSrc = coupon.getImgSrc();
        return new BasicCouponDto(id,name, price, endDate,imgSrc);
    }

    private List<BasicCouponDto> extractBasicDtoToList(List<Coupon> coupons) {

        List<BasicCouponDto> couponsDto = new ArrayList<>();
        coupons.forEach(coupon -> couponsDto.add(extractBasicDto(coupon)));
        return couponsDto;
    }

    private DetailedCouponDto extractDetailedDto(Coupon coupon) {

        long id = coupon.getId();
        String name = coupon.getName();
        String description = coupon.getDescription();
        String companyName = coupon.getCompany().getName();
        String categoryName = coupon.getCategory().getName();
        float price = coupon.getPrice();
        String startDate = String.valueOf(coupon.getStartDate());
        String endDate = String.valueOf(coupon.getEndDate());
        String companyEmployee = coupon.getUser().getUserName();
        String imgSrc = coupon.getImgSrc();
        return new DetailedCouponDto(id,categoryName, name, price, endDate, startDate, description, companyName, companyEmployee,imgSrc);
    }

    private void validateCoupon(Coupon coupon) throws ServerException {

        long companyId = coupon.getCompany().getId();
        companiesLogic.validateCompanyExist(companyId);

        long categoryId = coupon.getCategory().getId();
        categoriesLogic.validateCategoryExist(categoryId);

        if (!ValidatorUtils.isNameValid(coupon.getName())) {
            throw new ServerException(ErrorType.INVALID_NAME_LENGTH, " name=" + coupon.getName());
        }
        if (!(coupon.getPrice() > 0)) {
            throw new ServerException(ErrorType.INVALID_VALUE_INPUT, " price=" + coupon.getPrice());
        }
        if (!isDescriptionValid(coupon.getDescription())) {
            throw new ServerException(ErrorType.INVALID_DESCRIPTION, coupon.getDescription());
        }
        if (coupon.getStartDate().after(coupon.getEndDate())) {
            throw new ServerException(ErrorType.INVALID_DATE_INPUT, " start date=" + coupon.getStartDate());
        }
        if (Date.valueOf(LocalDate.now()).after(coupon.getEndDate())) {
            throw new ServerException(ErrorType.INVALID_DATE_INPUT, " end date=" + coupon.getEndDate());
        }
    }

    private boolean isDescriptionValid(String description) {

        if (description == null) {
            return false;
        }
        if (description.isBlank()) {
            return false;
        }
        if (description.length() > 200) {
            return false;
        }
        return true;
    }

    private void validateNameForCreate(Coupon coupon) throws ServerException {

        String name = coupon.getName();
        if (findByName(name) != null) {
            throw new ServerException(ErrorType.COUPON_NAME_EXISTS, "name=" + coupon.getName());
        }
    }

    private void validateNameForUpdate(Coupon coupon) throws ServerException {

        String name = coupon.getName();
        Coupon coupon1 = findByName(name);
        if (coupon1 == null) {
            return;
        }
        long id = coupon.getId();
        if (id != coupon1.getId()) {
            throw new ServerException(ErrorType.COUPON_NAME_EXISTS, "name=" + coupon.getName());
        }
    }

    private Coupon findByName(String name) throws ServerException {
        try {
            return iCouponsDal.findByName(name);
        } catch (Exception e) {
            throw new ServerException(ErrorType.GENERAL_ERROR);
        }
    }

    private void validateCouponExist(long couponId) throws ServerException {

        try {
            if (iCouponsDal.findById(couponId).isEmpty()) {
                throw new ServerException(ErrorType.UNSUPPORTED_REQUEST, " couponId=" + couponId);
            }
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    private void validateCouponPublisher(User user, Coupon coupon) throws ServerException {
        if (coupon.getUser().getId() != user.getId()) {
            throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED, "User is not the employee that posted coupon with id=" + coupon.getId());
        }
    }

    private void validatePermission(String authorization, long couponId) throws ServerException {

        long id = ValidatorUtils.validateToken(authorization);
        User user = usersLogic.getById(id);
        Coupon coupon = findById(couponId);
        if (user.getUserType()==UserType.CUSTOMER){
            throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
        }
        if (user.getUserType()==UserType.COMPANY){
            if (coupon.getUser().getId()!=id){
                throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
            }
        }

    }


}
