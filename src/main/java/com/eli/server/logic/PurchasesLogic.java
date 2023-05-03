package com.eli.server.logic;


import com.eli.server.constants.Consts;
import com.eli.server.dal.IPurchasesDal;
import com.eli.server.dto.PurchaseDto;
import com.eli.server.entities.*;
import com.eli.server.enums.ErrorType;
import com.eli.server.enums.UserType;
import com.eli.server.exceptions.ServerException;
import com.eli.server.utils.JWTUtils;
import com.eli.server.utils.ValidatorUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class PurchasesLogic {

    private IPurchasesDal iPurchasesDal;
    private CouponsLogic couponsLogic;
    private CustomersLogic customersLogic;
    private UsersLogic usersLogic;

    @Autowired
    public PurchasesLogic(IPurchasesDal iPurchasesDal, CouponsLogic couponsLogic, CustomersLogic customersLogic, UsersLogic usersLogic) {
        this.iPurchasesDal = iPurchasesDal;
        this.couponsLogic = couponsLogic;
        this.customersLogic = customersLogic;
        this.usersLogic = usersLogic;
    }


    public void add(String authorization, Purchase purchase) throws ServerException {

        Long id = ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.CUSTOMER);

        Customer customer = customersLogic.findById(id);
        purchase.setCustomer(customer);
        purchaseValidation(purchase);
        purchase.setTimestamp(Date.from(Instant.now()));
        try {
            iPurchasesDal.save(purchase);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public PurchaseDto getById(String authorization, long purchaseId) throws ServerException {

        validateRequest(authorization, purchaseId);
        try {
            return iPurchasesDal.getDtoById(purchaseId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    ///???
    public void updatePurchase(String authorization, Purchase purchase) throws ServerException {

        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);

        purchaseValidation(purchase);
        purchaseExistValidation(purchase.getId());
        purchase.setTimestamp(Date.from(Instant.now()));
        try {
            iPurchasesDal.save(purchase);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public void removePurchase(String authorization, long purchaseId) throws ServerException {

        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        purchaseExistValidation(purchaseId);
        try {
            iPurchasesDal.deleteById(purchaseId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<PurchaseDto> getAllByCustomer(String authorization, int pageNumber) throws ServerException {

        Long customerId = ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.CUSTOMER);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            return iPurchasesDal.getAllByCustomer(customerId, paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<PurchaseDto> getMyPurchasesInDateRange(String authorization, String startDate, String endDate, int pageNumber) throws ServerException {

        Long customerId = ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.CUSTOMER);
        Date startD = Date.valueOf(startDate);
        Date endD = Date.valueOf(endDate);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            return iPurchasesDal.findAllByCustomerInDateRange(customerId, startD, endD, paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<PurchaseDto> getAllByCompany(String authorization, int pageNumber) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.COMPANY);

        Claims claims = JWTUtils.decodeJWT(authorization);
        String strCompanyId = claims.getAudience();

        long companyId = Long.parseLong(strCompanyId);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            return iPurchasesDal.findAllByCompany(companyId, paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<PurchaseDto> getAllByCategory(String authorization, long categoryId, int pageNumber) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            return iPurchasesDal.findAllByCategory(categoryId, paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<PurchaseDto> getAllByCompanyUser(String authorization, int pageNumber) throws ServerException {

        Long userId = ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.COMPANY);

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            return iPurchasesDal.findAllByCompanyUser(userId, paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<PurchaseDto> getAllByCompanyInDateRange(String authorization, String startDate, String endDate,
                                                        int pageNumber) throws ServerException {
        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.COMPANY);

        Claims claims = JWTUtils.decodeJWT(authorization);

        String strCompanyId = claims.getAudience();
        long companyId = Long.parseLong(strCompanyId);
        Date startD = Date.valueOf(startDate);
        Date endD = Date.valueOf(endDate);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);

        try {
            return iPurchasesDal.findAllByCompanyInDateRange(companyId, startD, endD, paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public List<PurchaseDto> getAll(String authorization, int pageNumber) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        try {
            return iPurchasesDal.getAll(paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public List<PurchaseDto> getAllInDateRange(String authorization, String startDate, String endDate, int pageNumber) throws
            ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        Date start = Date.valueOf(startDate);
        Date end = Date.valueOf(endDate);
        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);

        try {
            return iPurchasesDal.findAllInDateRange(start, end, paging);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }
//
//    public List<PurchaseDto> getAllPurchasesByCustomerAndMaxPricePaged(int customerId, float maxPrice, int pageNumber) throws ServerException {
//        List<PurchaseDto> purchasesDto = purchasesDal.getAllPurchasesByCustomerAndMaxPricePaged(customerId, maxPrice, pageNumber);
//        return purchasesDto;
//    }

    private void purchaseValidation(Purchase purchase) throws ServerException {

        long customerId = purchase.getCustomer().getId();
        customersLogic.validateCustomerExist(customerId);

        long couponId = purchase.getCoupon().getId();
        Coupon coupon = couponsLogic.findById(couponId);

        if (Date.valueOf(LocalDate.now()).after(coupon.getEndDate())) {
            throw new ServerException(ErrorType.EXPIRED_COUPON, " end date: " + coupon.getEndDate());
        }
    }

    private void purchaseExistValidation(long purchaseId) throws ServerException {

        if (!iPurchasesDal.existsById(purchaseId)) {
            throw new ServerException(ErrorType.UNSUPPORTED_REQUEST, "purchaseId: " + purchaseId);
        }
    }

    private void validateRequest(String authorization, long purchaseId) throws ServerException {

        purchaseExistValidation(purchaseId);
        Purchase purchase = iPurchasesDal.getById(purchaseId);

        long id = ValidatorUtils.validateToken(authorization);
        User user = usersLogic.getById(id);

        if (user.getUserType() == UserType.COMPANY) {
            String strCompanyId = JWTUtils.decodeJWT(authorization).getAudience();
            long companyId = Long.parseLong(strCompanyId);

            if (purchase.getCoupon().getCompany().getId() != companyId) {
                throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
            }
            if (purchase.getCoupon().getUser().getId() != id) {
                throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
            }
        } else if (user.getUserType() == UserType.CUSTOMER) {
            if (purchase.getCustomer().getId() != id) {
                throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
            }
        }
    }

}