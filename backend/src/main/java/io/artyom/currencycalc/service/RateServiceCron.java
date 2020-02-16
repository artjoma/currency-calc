package io.artyom.currencycalc.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
@Slf4j
public class RateServiceCron {

    private final RatesService rateService;

    @PostConstruct
    public void init(){
        rateService.refreshRates();
    }

    @Scheduled(cron = "0 * * * * *")
    public void periodicRateUpdate() {
        try {
            rateService.refreshRates();
        } catch (Exception err) {
            log.error("periodicRateUpdate err:{}", err.getMessage());
        }
    }

}
