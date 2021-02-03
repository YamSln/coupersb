package local.coupersb.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import local.coupersb.dao.CouponRepository;

/**
 * The coupon expiration daily job deletes expired coupons from the coupons system
 * database on a daily schedule
 * 
 * @author YAM
 *
 */

@Component
public class CouponExpirationDailyJob
{	
	private CouponRepository couponRepository;
	
	@Autowired
	public CouponExpirationDailyJob(CouponRepository couponRepository)
	{
		this.couponRepository = couponRepository;
	}
	
	@PostConstruct
	public void dailyJobOnStartup()
	{ // Performs the daily job upon starting up the system
		performDailyJob();
	}
	
	@Scheduled(cron = "${daily-job.scheduled.time}")
	public void initDailyJob()
	{ // Performs the daily job as a scheduled task,
		// defined by the daily-job.scheduled.time expression (application properties)
		performDailyJob();
	}
	
	private void performDailyJob()
	{
		this.couponRepository.deleteExpieredCoupons();
	}
	
}