package org.fxtracker.datafetcher;

import org.fxtracker.datafetcher.exception.RateDataFetcherException;
import org.fxtracker.domain.Currency;
import org.fxtracker.domain.Rate;

import java.util.List;

public interface RateDataFetcher {
    List<Rate> getRates() throws RateDataFetcherException;

    Rate getRate(Currency currencyFrom, Currency currencyTo) throws RateDataFetcherException;
}
