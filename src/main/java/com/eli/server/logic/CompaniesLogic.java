package com.eli.server.logic;


import com.eli.server.constants.Consts;
import com.eli.server.dal.ICompaniesDal;
import com.eli.server.dto.CompanyDto;
import com.eli.server.entities.Company;
import com.eli.server.entities.User;
import com.eli.server.enums.ErrorType;
import com.eli.server.enums.UserType;
import com.eli.server.exceptions.ServerException;
import com.eli.server.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CompaniesLogic {

    private ICompaniesDal iCompaniesDal;
    @Autowired
    private UsersLogic usersLogic;

    @Autowired
    public CompaniesLogic(ICompaniesDal iCompaniesDal) {
        this.iCompaniesDal = iCompaniesDal;
    }


    public void add(String authorization, Company company) throws ServerException {

        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);

        companyValidation(company);
        validateNameForCreate(company);

        try {
            iCompaniesDal.save(company);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public void update(String authorization,Company company) throws ServerException {

        companyValidation(company);
        validateNameForUpdate(company);
        validateCompanyExist(company.getId());
        long id = ValidatorUtils.validateToken(authorization);
        validatePermission(company.getId(),id);
        try {
            iCompaniesDal.save(company);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public void remove(String authorization,long companyId) throws ServerException {

        validateCompanyExist(companyId);
        ValidatorUtils.validateToken(authorization);
        ValidatorUtils.validateUserPermission(authorization, UserType.ADMIN);
        try {
            iCompaniesDal.deleteById(companyId);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    public CompanyDto getById(long companyId) throws ServerException {

        validateCompanyExist(companyId);
        try {
            Company company = iCompaniesDal.findById(companyId).get();
            return extractDto(company);
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }


    public List<CompanyDto> getAll(int pageNumber) throws ServerException {

        Pageable paging = PageRequest.of(pageNumber, Consts.AMOUNT_OF_ITEMS_PER_PAGE);
        List<CompanyDto> companiesDto = new ArrayList<>();

        try {
            List<Company> companies = iCompaniesDal.findAll(paging);
            companies.forEach(company -> companiesDto.add(extractDto(company)));
            return companiesDto;
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    private void validatePermission(long companyId, long id) throws ServerException {

        User user = usersLogic.getById(id);
        if (user.getUserType()==UserType.CUSTOMER){
            throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
        }
        if (user.getUserType()==UserType.COMPANY){
            if (user.getCompany().getId()!=companyId){
                throw new ServerException(ErrorType.PERMISSION_NOT_GRANTED);
            }
        }
    }

    private CompanyDto extractDto(Company company) {

        long id = company.getId();
        String name = company.getName();
        String phoneNumber = company.getPhoneNumber();
        String address = company.getAddress();
        return new CompanyDto(id,name, phoneNumber, address);
    }

    private void companyValidation(Company company) throws ServerException {

        String name = company.getName();
        String address = company.getAddress();
        String phoneNumber = company.getPhoneNumber();

        if (!ValidatorUtils.isNameValid(name)) {
            throw new ServerException(ErrorType.INVALID_NAME_LENGTH, name);
        }
        if (!isAddressValid(address)) {
            throw new ServerException(ErrorType.INVALID_ADDRESS_ENTERED, address);
        }
        if (!ValidatorUtils.isPhoneNumberValid(phoneNumber)) {
            throw new ServerException(ErrorType.INVALID_PHONE_NUMBER, phoneNumber);
        }
    }

    private boolean isAddressValid(String address) {

        if (!ValidatorUtils.isNameValid(address)) {
            return false;
        }
        if (!isAddressPatternValid(address)) {
            return false;
        }
        return true;
    }

    private boolean isAddressPatternValid(String address) {

        int commaCount = 0;
        int digitCount = 0;

        for (int i = 0; i < address.length(); i++) {
            if (address.charAt(i) == ',') {
                commaCount++;
            }
            if (address.charAt(i) >= '0' && address.charAt(i) <= '9') {
                digitCount++;
            }
        }
        if (commaCount < 1) {
            return false;
        }
        if (digitCount > address.length() / 2) {
            return false;
        }
        return true;
    }


    void validateCompanyExist(long companyId) throws ServerException {

        try {
            if (!iCompaniesDal.existsById(companyId)) {
                throw new ServerException(ErrorType.UNSUPPORTED_REQUEST, "Resource does NOT exist,id=" + companyId);
            }
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    private void validateNameForCreate(Company company) throws ServerException {

        String name = company.getName();
        try {
            if (iCompaniesDal.findByName(name) != null) {
                throw new ServerException(ErrorType.COMPANY_NAME_EXISTS, "name=" + name);
            }
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }

    private void validateNameForUpdate(Company company) throws ServerException {

        String name = company.getName();
        long id = company.getId();

        try {
            Company company1 = iCompaniesDal.findByName(name);
            if (company1 != null && company1.getId() != id) {
                throw new ServerException(ErrorType.COMPANY_NAME_EXISTS, "name=" + name);
            }
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.GENERAL_ERROR);
        }
    }
}
