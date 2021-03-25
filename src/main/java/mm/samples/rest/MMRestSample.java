package mm.samples.rest;

public class MMRestSample {

    private static final String URL = "http://localhost:8990";
    private static final String USER = "admin";
    private static final String PASSWORD = "********";

    public static void main(String[] args) throws Exception {
        //---------------------------------------------
        // Login
        String accessToken = HTTPUtils.login(URL, USER, PASSWORD);

        //---------------------------------------------
        // Create instrument
        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.instrument = "LTCBTC";
        instrumentDto.exchange = "DLTXMM";
        instrumentDto.source_exchange = "BINANCE KRAKEN";
        long algo_id = HTTPUtils.post(URL, "/api/v0/config/instrument/save", accessToken, instrumentDto, InstrumentDto.class).algo_id;
        System.out.println("Created Bot#" + algo_id);

        // Create pricer
        PricerDto pricerDto = new PricerDto();
        pricerDto.algo_id = algo_id;
        pricerDto.buy_quote_sizes = "2";
        pricerDto.sell_quote_sizes = "2";
        pricerDto.buy_margins = "15";
        pricerDto.sell_margins = "15";
        pricerDto.min_spread = "1 bps";
        pricerDto.min_price_change = 3.0d;
        pricerDto.aggregation_method = "BEST_SINGLE";
        HTTPUtils.post(URL, "/api/v0/config/pricer/save", accessToken, pricerDto);
        System.out.println("Created pricer for Bot#" + algo_id);

        // Create Hedger
        HedgerDto hedgerDto = new HedgerDto();
        hedgerDto.algo_id = algo_id;
        hedgerDto.position_max_norm_size = "2 0";
        hedgerDto.hedge_instrument = "LTCBTC";
        hedgerDto.hedge_strategy = "LIMIT";
        hedgerDto.execution_style = "LEAN";
        hedgerDto.venues_list = "BINANCE";
        hedgerDto.max_order_size = 1.0d;
        hedgerDto.resend_time = 500L;
        HTTPUtils.post(URL, "/api/v0/config/hedger/save", accessToken, hedgerDto);
        System.out.println("Created hedger for Bot#" + algo_id);

        // Create risk limits
        RiskLimitsDto riskLimitsDto = new RiskLimitsDto();
        riskLimitsDto.algo_id = algo_id;
        riskLimitsDto.max_long_exposure = 5;
        riskLimitsDto.max_short_exposure = 5;
        HTTPUtils.post(URL, "/api/v0/config/riskLimits/save", accessToken, riskLimitsDto);
        System.out.println("Created risk limits for Bot#" + algo_id);

        // Start quoter
        boolean quoter_running = HTTPUtils.post(URL, "/api/v0/config/pricer/start/" + algo_id, accessToken, PricerDto.class).running;
        System.out.println("Started pricer for Bot#" + algo_id + "; running: " + quoter_running);

        // Start hedger
        boolean hedger_running = HTTPUtils.post(URL, "/api/v0/config/hedger/start/" + algo_id, accessToken, PricerDto.class).running;
        System.out.println("Started hedger for Bot#" + algo_id + "; running: " + hedger_running);

        // Run bot
        boolean running = HTTPUtils.post(URL, "/api/v0/config/instrument/start/" + algo_id, accessToken, InstrumentDto.class).running;
        System.out.println("Started the Bot#" + algo_id + "; running: " + running);
    }


}
