package com.marin.PaymentService.entities;

public enum OrderStatus {
    CONFIRMED {
        @Override
        public String value() {
            return "CONFIRMED";
        }
    },
    PROCESSING {
        @Override
        public String value() {
            return "PROCESSING";
        }
    },
    CANCELLED {
        @Override
        public String value() {
            return "CANCELLED";
        }
    };

    public abstract String value();
}
