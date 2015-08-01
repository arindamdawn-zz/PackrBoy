package com.packrboy.extras;

/**
 * Created by arindam.paaltao on 22-Feb-15.
 */
public interface Keys {
    public interface GoogleMaps{

        public static String KEY_RESULTS = "results";
        public static String KEY_ADDRESS_COMPONENTS = "address_components";
        public static String KEY_FORMATTED_ADDRESS = "formatted_address";
        public static String KEY_GEOMETRY = "geometry";
        public static String KEY_LOCATION = "location";
        public static String KEY_TYPES = "types";
        public static String KEY_POSTAL_CODE = "postal_code";
        public static String KEY_STATE = "administrative_area_level_1";
        public static String KEY_PREMISE = "premise";
        public static String KEY_CITY = "locality";
        public static String KEY_COUNTRY = "country";
        public static String KEY_LATITUDE = "lat";
        public static String KEY_LONGITUDE = "lng";
        public static String KEY_LONG_NAME = "long_name";

    }

    public interface ServiceKeys{
        public static String KEY_LOGIN_STATUS = "logged";
        public static String KEY_REDIRECT_URL = "redirectUrl";
        public static String KEY_ERROR_MESSAGE = "error_msg";
        public static String KEY_LOGGED_IN_USER = "loggedinUser";
        public static String KEY_USER_TYPE_OBJECT = "user_type_object";
        public static String KEY_FIRST_NAME = "fname";
        public static String KEY_ID = "id";
        public static String KEY_LAST_NAME = "lname";
        public static String KEY_GENDER = "sex";
        public static String KEY_PROFILE_PIC = "profile_pic";
        public static String KEY_EMAIL = "email";
        public static String KEY_PHONE_NO = "phone_no";
        public static String KEY_USER_TYPE = "user_type";
        public static String KEY_EMAIL_VERIFIED = "email_verified";
        public static String KEY_PHONE_NO_VERIFIED = "phone_no_verified";
        public static String KEY_ERROR_CODE = "error_code";
    }

    public interface Shipment{
        public static String KEY_TYPE = "type";
        public static String KEY_IN_TRANSIT_STATUS = "in_transit_status";
        public static String KEY_PICKUP_STREET_NO = "pickup_street_number";
        public static String KEY_SHIPMENT_ARRAY = "shipments";
        public static String KEY_SHIPMENT = "shipment";
        public static String KEY_ITEM_IMAGE = "item_image";
        public static String KEY_ID = "id";
        public static String KEY_PICKUP_ROUTE = "pickup_route";
        public static String KEY_PICKUP_CITY = "pickup_locality";
        public static String KEY_PICKUP_STATE = "pickup_city";
        public static String KEY_PICKUP_LATITUDE = "pickup_latitude";
        public static String KEY_PICKUP_LONGITUDE = "pickup_longitude";
        public static String KEY_PICKUP_POSTAL_CODE = "pickup_postal_code";
        public static String KEY_ITEM_QUANTITY = "item_qty";
        public static String KEY_CREATED_AT = "created_at";
        public static String KEY_DELIVERY_TYPE_ID = "delivery_type_id";
        public static String KEY_ITEM_TYPE_ID = "item_type_id";
    }
}
