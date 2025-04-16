/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package io.github.eduardout.converter.currency;

/**
 * Enum representation of a currency code from the availableCurrencies static
 * method from Java API Currency class.
 *
 * @see java.util.Currency
 * @author eduar
 */
public enum ISO4217Currency {

    ADP("ADP"), AED("AED"), AFA("AFA"), AFN("AFN"), ALL("ALL"), AMD("AMD"),
    ANG("ANG"), AOA("AOA"), ARS("ARS"), ATS("ATS"), AUD("AUD"), AWG("AWG"),
    AYM("AYM"), AZM("AZM"), AZN("AZN"), BAM("BAM"), BBD("BBD"), BDT("BDT"),
    BEF("BEF"), BGL("BGL"), BGN("BGN"), BHD("BHD"), BIF("BIF"), BMD("BMD"),
    BND("BND"), BOB("BOB"), BOV("BOV"), BRL("BRL"), BSD("BSD"), BTN("BTN"),
    BWP("BWP"), BYB("BYB"), BYN("BYN"), BYR("BYR"), BZD("BZD"), CAD("CAD"),
    CDF("CDF"), CHE("CHE"), CHF("CHF"), CHW("CHW"), CLF("CLF"), CLP("CLP"),
    CNY("CNY"), COP("COP"), COU("COU"), CRC("CRC"), CSD("CSD"), CUC("CUC"),
    CUP("CUP"), CVE("CVE"), CYP("CYP"), CZK("CZK"), DEM("DEM"), DJF("DJF"),
    DKK("DKK"), DOP("DOP"), DZD("DZD"), EEK("EEK"), EGP("EGP"), ERN("ERN"),
    ESP("ESP"), ETB("ETB"), EUR("EUR"), FIM("FIM"), FJD("FJD"), FKP("FKP"),
    FRF("FRF"), GBP("GBP"), GEL("GEL"), GHC("GHC"), GHS("GHS"), GIP("GIP"),
    GMD("GMD"), GNF("GNF"), GRD("GRD"), GTQ("GTQ"), GWP("GWP"), GYD("GYD"),
    HKD("HKD"), HNL("HNL"), HRK("HRK"), HTG("HTG"), HUF("HUF"), IDR("IDR"),
    IEP("IEP"), ILS("ILS"), INR("INR"), IQD("IQD"), IRR("IRR"), ISK("ISK"),
    ITL("ITL"), JMD("JMD"), JOD("JOD"), JPY("JPY"), KES("KES"), KGS("KGS"),
    KHR("KHR"), KMF("KMF"), KPW("KPW"), KRW("KRW"), KWD("KWD"), KYD("KYD"),
    KZT("KZT"), LAK("LAK"), LBP("LBP"), LKR("LKR"), LRD("LRD"), LSL("LSL"),
    LTL("LTL"), LUF("LUF"), LVL("LVL"), LYD("LYD"), MAD("MAD"), MDL("MDL"),
    MGA("MGA"), MGF("MGF"), MKD("MKD"), MMK("MMK"), MNT("MNT"), MOP("MOP"),
    MRO("MRO"), MRU("MRU"), MTL("MTL"), MUR("MUR"), MVR("MVR"), MWK("MWK"),
    MXN("MXN"), MXV("MXV"), MYR("MYR"), MZM("MZM"), MZN("MZN"), NAD("NAD"),
    NGN("NGN"), NIO("NIO"), NLG("NLG"), NOK("NOK"), NPR("NPR"), NZD("NZD"),
    OMR("OMR"), PAB("PAB"), PEN("PEN"), PGK("PGK"), PHP("PHP"), PKR("PKR"),
    PLN("PLN"), PTE("PTE"), PYG("PYG"), QAR("QAR"), ROL("ROL"), RON("RON"),
    RSD("RSD"), RUB("RUB"), RUR("RUR"), RWF("RWF"), SAR("SAR"), SBD("SBD"),
    SCR("SCR"), SDD("SDD"), SDG("SDG"), SEK("SEK"), SGD("SGD"), SHP("SHP"),
    SIT("SIT"), SKK("SKK"), SLE("SLE"), SLL("SLL"), SOS("SOS"), SRD("SRD"),
    SRG("SRG"), SSP("SSP"), STD("STD"), STN("STN"), SVC("SVC"), SYP("SYP"),
    SZL("SZL"), THB("THB"), TJS("TJS"), TMM("TMM"), TMT("TMT"), TND("TND"),
    TOP("TOP"), TPE("TPE"), TRL("TRL"), TRY("TRY"), TTD("TTD"), TWD("TWD"),
    TZS("TZS"), UAH("UAH"), UGX("UGX"), USD("USD"), USN("USN"), USS("USS"),
    UYI("UYI"), UYU("UYU"), UZS("UZS"), VEB("VEB"), VED("VED"), VEF("VEF"),
    VES("VES"), VND("VND"), VUV("VUV"), WST("WST"), XAF("XAF"), XAG("XAG"),
    XAU("XAU"), XBA("XBA"), XBB("XBB"), XBC("XBC"), XBD("XBD"), XCD("XCD"),
    XDR("XDR"), XFO("XFO"), XFU("XFU"), XOF("XOF"), XPD("XPD"), XPF("XPF"),
    XPT("XPT"), XSU("XSU"), XTS("XTS"), XUA("XUA"), XXX("XXX"), YER("YER"),
    YUM("YUM"), ZAR("ZAR"), ZMK("ZMK"), ZMW("ZMW"), ZWD("ZWD"), ZWL("ZWL"),
    ZWN("ZWN"), ZWR("ZWR");

    private final String currencyCode;

    private ISO4217Currency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
