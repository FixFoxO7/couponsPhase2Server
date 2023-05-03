package com.eli.server.controllers;

import com.eli.server.dto.PurchaseDto;
import com.eli.server.entities.Purchase;
import com.eli.server.exceptions.ServerException;
import com.eli.server.logic.PurchasesLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    private PurchasesLogic purchasesLogic;

    @Autowired
    public PurchasesController(PurchasesLogic purchasesLogic) {
        this.purchasesLogic = purchasesLogic;
    }

    @PostMapping
    public void add(@RequestHeader String authorization, @RequestBody Purchase purchase) throws ServerException {
        purchasesLogic.add(authorization,purchase);
    }

    @GetMapping("{purchaseId}")
    public PurchaseDto getById(@RequestHeader String authorization,@PathVariable("purchaseId") long purchaseId) throws ServerException {
        return purchasesLogic.getById(authorization ,purchaseId);
    }

    //???
    @PutMapping
    public void update(@RequestHeader String authorization,@RequestBody Purchase purchase) throws ServerException {
        purchasesLogic.updatePurchase(authorization , purchase);
    }

    @DeleteMapping("{purchaseId}")
    public void delete(@RequestHeader String authorization,@PathVariable("purchaseId") long purchaseId) throws ServerException {
        purchasesLogic.removePurchase(authorization , purchaseId);
    }

    @GetMapping
    public List<PurchaseDto> getAll(@RequestHeader String authorization,@RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getAll(authorization,pageNumber);
    }

    @GetMapping("/byCustomer")
    public List<PurchaseDto> getAllByCustomer(@RequestHeader String authorization, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getAllByCustomer(authorization, pageNumber);
    }

    @GetMapping("/byCompany")
    public List<PurchaseDto> getAllByCompany(@RequestHeader String authorization, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getAllByCompany(authorization, pageNumber);
    }
    @GetMapping("/byCategory/{categoryId}")
    public List<PurchaseDto> getAllByCategory(@RequestHeader String authorization,@PathVariable("categoryId")  long categoryId ,@RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getAllByCategory(authorization,categoryId, pageNumber);
    }

    @GetMapping("/byCompanyUser")
    public List<PurchaseDto> getAllByCompanyUser(@RequestHeader String authorization, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getAllByCompanyUser(authorization, pageNumber);
    }

    @GetMapping("/byCompanyInDateRange")
    public List<PurchaseDto> getAllByCompanyInDateRange(@RequestHeader String authorization, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getAllByCompanyInDateRange(authorization, startDate, endDate, pageNumber);
    }

    @GetMapping("/byDateRange")
    public List<PurchaseDto> getAllInDateRange(@RequestHeader String authorization ,@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getAllInDateRange(authorization,startDate, endDate, pageNumber);
    }

    @GetMapping("/byCustomerInDateRange")
    public List<PurchaseDto> getMyPurchasesInDateRange(@RequestHeader String authorization, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("pageNumber") int pageNumber) throws ServerException {
        return purchasesLogic.getMyPurchasesInDateRange(authorization,startDate, endDate, pageNumber);
    }
//    @GetMapping("/byCustomer&Price")
//    //http://localhost:8080/purchases/byCustomer&Price?customerId=num&maxPrice=num&pageNumber=num
//    public List<PurchaseDto> getPurchasesByCustomerAndMaxPrice(@RequestParam("customerId") int customerId, @RequestParam("maxPrice") int maxPrice, @RequestParam("pageNumber") int pageNumber) throws ServerException {
//        return purchasesLogic.getAllPurchasesByCustomerAndMaxPricePaged(customerId, maxPrice, pageNumber);
//    }
}
