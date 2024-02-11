package com.example.clublink;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DateTime {


    public static String getTime(String dateTime){

        String timeAgo="";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(dateTime);

            long currentTimeMillis = System.currentTimeMillis();
            long dateTimeMillis = date.getTime();
            long timeDifferenceMillis = currentTimeMillis - dateTimeMillis;

            long seconds = timeDifferenceMillis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                timeAgo = days + " days ago";
            } else if (hours > 0) {
                timeAgo = hours + " hours ago";
            } else if (minutes > 1) {
                timeAgo = minutes + " minutes ago";
            } else {
                timeAgo = "just now";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeAgo;


    }


}