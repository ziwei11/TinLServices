package org.V.TinL.validate;

import org.apache.commons.validator.routines.UrlValidator;

public class UrlValidate {
    public static boolean isValidUrl(String Url){
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(Url);
    }
}
