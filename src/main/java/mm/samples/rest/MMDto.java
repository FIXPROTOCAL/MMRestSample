package mm.samples.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class TokenData {
    public String access_token;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class InstrumentDto {
    public String algo_key = "MM";
    public Long algo_id;
    public String instrument;
    public String exchange;
    public String source_exchange;
    public boolean running;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class PricerDto {
    public String algo_key = "MM";
    public Long algo_id;
    public String buy_quote_sizes;
    public String sell_quote_sizes;
    public String buy_margins;
    public String sell_margins;
    public String aggregation_method;
    public Double min_price_change;
    public String min_spread;
    public boolean running;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class HedgerDto {
    public String algo_key = "MM";
    public Long algo_id;
    public String position_max_norm_size;
    public String hedge_strategy;
    public String execution_style;
    public String venues_list;
    public double max_order_size;
    public Long resend_time;
    public String hedge_instrument;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class RiskLimitsDto {
    public String algo_key = "MM";
    public Long algo_id;
    public double max_long_exposure;
    public double max_short_exposure;
}
