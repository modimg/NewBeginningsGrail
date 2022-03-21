package com.grail.nb.utils;

import org.apache.commons.lang3.RandomStringUtils;


public class ParticipantUtils {
    public static String getParticipantId(){
        String id = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        id = id.substring(0,3) + "-" + id.substring(3,6);
        return id;
    }

    public static String standardizePhoneNo(String phoneno) {
        if(null == phoneno)
            return null;
        String numberOnly = phoneno.replaceAll("[^0-9]", "");
        return numberOnly.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
    }
}
