package com.eli.server.tasks;

import com.eli.server.dal.ICouponsDal;
import com.eli.server.enums.ErrorType;
import com.eli.server.exceptions.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Component
public class ScheduledTaskRemoveExpiredCoupons {
    private ICouponsDal iCouponsDal;


    @Autowired
    public ScheduledTaskRemoveExpiredCoupons(ICouponsDal iCouponsDal) {
        this.iCouponsDal = iCouponsDal;
    }

    @Transactional
    @Scheduled(cron = "0 01 0 * * *")
    public void execute() throws ServerException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

        long now = Calendar.getInstance().getTimeInMillis();
        java.sql.Date todayDate = new java.sql.Date(now);

        try {
            iCouponsDal.deleteByEndDateBefore(todayDate);
            System.out.println("Deleting Expired Coupons... Time: " + formatter.format(LocalDateTime.now()));
        } catch (Exception e) {
            throw new ServerException(e, ErrorType.ERROR_DELETING_EXPIRED_COUPONS);
        }
    }
}
