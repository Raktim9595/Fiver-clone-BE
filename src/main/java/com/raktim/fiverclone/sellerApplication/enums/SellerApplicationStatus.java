package com.raktim.fiverclone.sellerApplication.enums;

public enum SellerApplicationStatus {
    DRAFT, // when the application is not submitted yet so user can make changes
    SUBMITTED, // application is submitted now, and waiting for review and user can't make any changes now
    UNDER_REVIEW, // admin just started reviewing the application
    CHANGES_REQUIRED, // admin asked for changes needed or additional information
    APPROVED, // admin approved the application
    REJECTED, // admin rejected the application
    WITHDRAWN, // user themselves withdrawn the application
}
