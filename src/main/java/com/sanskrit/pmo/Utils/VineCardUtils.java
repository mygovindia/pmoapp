package com.sanskrit.pmo.utils;


public class VineCardUtils {
    public static final String PLAYER_CARD = "player";
    public static final String VINE_CARD = "vine";
    public static final long VINE_USER_ID = 586671909;

    private VineCardUtils() {
    }

    public static boolean isVine(Card card) {
        if (("player".equals(card.name) || "vine".equals(card.name)) && isVineUser(card)) {
            return true;
        }
        return false;
    }

    private static boolean isVineUser(Card card) {
        UserValue user = (UserValue) card.bindingValues.get("site");
        if (user == null) {
            return false;
        }
        try {
            if (Long.parseLong(user.idStr) == 586671909) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getPublisherId(Card card) {
        return ((UserValue) card.bindingValues.get("site")).idStr;
    }

    public static String getStreamUrl(Card card) {
        return (String) card.bindingValues.get("player_stream_url");
    }

    public static String getCallToActionUrl(Card card) {
        return (String) card.bindingValues.get("card_url");
    }

    public static ImageValue getImageValue(Card card) {
        return (ImageValue) card.bindingValues.get("player_image");
    }
}
