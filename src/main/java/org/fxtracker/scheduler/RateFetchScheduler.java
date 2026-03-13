package org.fxtracker.scheduler;

import org.fxtracker.datafetcher.RateDataFetcher;
import org.fxtracker.datafetcher.exception.RateDataFetcherException;
import org.fxtracker.domain.Rate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RateFetchScheduler {

    private final List<RateDataFetcher> rateFetchers;

    public RateFetchScheduler(List<RateDataFetcher> rateFetchers) {
        this.rateFetchers = rateFetchers;
    }

    @Scheduled(fixedRate = 60000)
    public void fetchRates() {
        rateFetchers.forEach(fetcher -> {
            try {
                List<Rate> rates = fetcher.getRates();
                rates.forEach(rate -> System.out.println(
                        rate.getCurrencyFrom() + " -> " + rate.getCurrencyTo() + " : " + rate.getRate()
                ));
            } catch (RateDataFetcherException e) {
                System.err.println("Failed to fetch rates from " + fetcher.getClass().getSimpleName() + ": " + e.getMessage());
            }
        });
    }
}
