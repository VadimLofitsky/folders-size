package ru.lofitsky.foldersSize.util;

import java.util.Arrays;

public class PrettyPrint {
    public final static String[] defaultPrefixesEn = {"y", "z", "a", "f", "p", "n", "mk", "m",
            "", "k", "M", "G", "T", "P", "E", "Z", "Y"};

    public final static String[] defaultPrefixesRu = {"и", "з", "а", "ф", "п", "н", "мк", "м",
            "", "к", "М", "Г", "Т", "П", "Э", "З", "И"};

    private final String[] prefixes;
    private String unitName;

    private final double base;
    private static final double defaultBase = 1000L;
    private final double log_base;
    private final int prettyValueIntPartDigitsMaxCount;

    private final static int defaultMinPow = -24;
    private final int minPow;

    private int prettyValueFloatPartPrecision;
    private final static int defaultFloatPrecision = 2;


    public PrettyPrint(String[] prefixes, String unitName, double base, int minPow, int precision) {
        this.prefixes = Arrays.copyOf(prefixes, prefixes.length);
        this.unitName = unitName;

        this.minPow = minPow;
        this.prettyValueFloatPartPrecision = precision;

        this.base = base;
        log_base = Math.log(base);
        prettyValueIntPartDigitsMaxCount = (int) Math.log10(base);

        this.prettyValueFloatPartPrecision = precision;
    }

    public PrettyPrint(String[] prefixes, String unitName, double base, int precision) {
        this(prefixes, unitName, base, defaultMinPow, precision);
    }

    public PrettyPrint(String unitName, double base, int precision) {
        this(defaultPrefixesEn, unitName, base, defaultMinPow, precision);
    }

    public PrettyPrint(String unitName, double base) {
        this(defaultPrefixesEn, unitName, base, defaultMinPow, defaultFloatPrecision);
    }

    public PrettyPrint(String unitName, int precision) {
        this(defaultPrefixesEn, unitName, defaultBase, defaultMinPow, precision);
    }

    public PrettyPrint(String unitName) {
        this(defaultPrefixesEn, unitName, defaultBase, defaultMinPow, defaultFloatPrecision);
    }

    public String print(double value) {
        return print(value, defaultFloatPrecision);
    }

    public String print(double value, int floatPrecision) {
        double sign = Math.signum(value);
        value = Math.abs(value);

        int pow = (value != 0) ? (int) (Math.log(value) / log_base) : 0;
        if(pow < 1) {
            pow -= 1;
        }

        int prefixIndex = pow - minPow / prettyValueIntPartDigitsMaxCount;
        double prettyValue = sign * value / Math.pow(base, pow);

        String prettyFormat = "%" + prettyValueIntPartDigitsMaxCount +
                "." + floatPrecision + "f %s" + unitName;

        return String.format(prettyFormat, prettyValue, prefixes[prefixIndex]);
    }

    public String printExp(double value, int floatPrecision) {
        return String.format("%."+ floatPrecision + "e %s", value, unitName);
    }
}