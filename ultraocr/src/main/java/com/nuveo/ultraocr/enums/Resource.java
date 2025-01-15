package com.nuveo.ultraocr.enums;

public enum Resource {
    JOB {
        @Override
        public String toString() {
            return "job";
        }
    },
    BATCH {
        @Override
        public String toString() {
            return "batch";
        }
    }
}

