package com.ads.tad.Helpers;

public class Normalizer {
    public static String normalize(String value) {
        return value != null ? value.replaceAll("\\\\n", "\n") : "";
    }
}
