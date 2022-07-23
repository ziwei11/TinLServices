package org.V.TinL.transform_function;

// Hashcode Way
// Reference: https://gosunaina.medium.com/march-leetcoding-challenge-2021-encode-and-decode-tinyurl-step-by-step-6b39beb0671c

public class HashCode {
    public static String getShortUrlByHashCode(String longUrl){
        return String.valueOf(longUrl.hashCode());
    }
}
