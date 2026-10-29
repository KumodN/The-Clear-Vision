package com.example.theclearvision.common.util;

public class Constants {

    private Constants() {
        // Utility class
    }

    public static class Security {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String STAFF = "ROLE_STAFF";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
    }

    public static class Pagination {
        public static final String DEFAULT_PAGE = "0";
        public static final String DEFAULT_SIZE = "20";
        public static final String DEFAULT_SORT_BY = "createdAt";
        public static final String DEFAULT_SORT_DIRECTION = "desc";
    }
}
