package org.fxtracker.domain;

public class Rate {
    private Currency currencyFrom;
    private Currency currencyTo;
    private Double rate;

    public Rate(Currency currencyFrom, Currency currencyTo, Double rate) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
    }

    public Currency getCurrencyFrom() { return currencyFrom; }
    public Currency getCurrencyTo() { return currencyTo; }
    public Double getRate() { return rate; }
}
