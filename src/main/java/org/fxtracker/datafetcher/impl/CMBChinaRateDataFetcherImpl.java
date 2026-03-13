package org.fxtracker.datafetcher.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.fxtracker.datafetcher.RateDataFetcher;
import org.fxtracker.datafetcher.exception.RateDataFetcherException;
import org.fxtracker.domain.Currency;
import org.fxtracker.domain.Rate;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CMBChinaRateDataFetcherImpl implements RateDataFetcher {

    private static final String CMB_CHINA_GET_RATE_ENDPOINT = "https://m.cmbchina.com/api/rate/fx-rate";
    private static final String SUC_CODE = "SUC0000";

    private static final Map<String, Currency> CURRENCY_NAME_MAP = Map.of(
            "港币", Currency.HKD,
            "美元", Currency.USD,
            "澳大利亚元", Currency.AUD,
            "人民币", Currency.CNY
    );

    private final RestClient restClient = RestClient.create();

    @Override
    public List<Rate> getRates() throws RateDataFetcherException {
        BeeResponseEntity response = restClient.get()
                .uri(CMB_CHINA_GET_RATE_ENDPOINT)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .body(BeeResponseEntity.class);

        if (response == null || !response.returnCode.equals("SUC0000")) {
            throw new RateDataFetcherException("Failed to fetch rate from CMB");
        }

        return convertToRates(response);
    }

    @Override
    public Rate getRate(Currency currencyFrom, Currency currencyTo) throws RateDataFetcherException {
        List<Rate> rates = getRates();
        for (Rate rate : rates) {
            if (rate.getCurrencyFrom().equals(currencyFrom) && rate.getCurrencyTo().equals(currencyTo)) {
                return rate;
            }
        }
        return null;
    }

    private List<Rate> convertToRates(BeeResponseEntity response) {
        return response.getBody().getData().stream()
                .map(data -> {
                    Currency currency = CURRENCY_NAME_MAP.get(data.getCcyNbr());
                    if (currency == null) return null;
                    return new Rate(currency, Currency.CNY, data.getRthOfr().doubleValue());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @JacksonXmlRootElement(localName = "BeeResponseEntity")
    static class BeeResponseEntity {
        private String returnCode;
        private String errorMsg;
        private BeeResponseBody body;

        public String getReturnCode() { return returnCode; }
        public void setReturnCode(String returnCode) { this.returnCode = returnCode; }

        public String getErrorMsg() { return errorMsg; }
        public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }

        public BeeResponseBody getBody() { return body; }
        public void setBody(BeeResponseBody body) { this.body = body; }
    }

    static class BeeResponseBody {
        @JacksonXmlElementWrapper(localName = "data")
        @JacksonXmlProperty(localName = "data")
        private List<CmbRateData> data;

        private String time;

        public List<CmbRateData> getData() { return data; }
        public void setData(List<CmbRateData> data) { this.data = data; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
    }

    static class CmbRateData {
        private String ccyNbr;
        private BigDecimal rtbBid;
        private BigDecimal rthOfr;
        private BigDecimal rtcOfr;
        private BigDecimal rthBid;
        private BigDecimal rtcBid;
        private String ratTim;
        private String ratDat;
        private String ccyExc;

        public String getCcyNbr() { return ccyNbr; }
        public void setCcyNbr(String ccyNbr) { this.ccyNbr = ccyNbr; }

        public BigDecimal getRtbBid() { return rtbBid; }
        public void setRtbBid(BigDecimal rtbBid) { this.rtbBid = rtbBid; }

        public BigDecimal getRthOfr() { return rthOfr; }
        public void setRthOfr(BigDecimal rthOfr) { this.rthOfr = rthOfr; }

        public BigDecimal getRtcOfr() { return rtcOfr; }
        public void setRtcOfr(BigDecimal rtcOfr) { this.rtcOfr = rtcOfr; }

        public BigDecimal getRthBid() { return rthBid; }
        public void setRthBid(BigDecimal rthBid) { this.rthBid = rthBid; }

        public BigDecimal getRtcBid() { return rtcBid; }
        public void setRtcBid(BigDecimal rtcBid) { this.rtcBid = rtcBid; }

        public String getRatTim() { return ratTim; }
        public void setRatTim(String ratTim) { this.ratTim = ratTim; }

        public String getRatDat() { return ratDat; }
        public void setRatDat(String ratDat) { this.ratDat = ratDat; }

        public String getCcyExc() { return ccyExc; }
        public void setCcyExc(String ccyExc) { this.ccyExc = ccyExc; }
    }
}
