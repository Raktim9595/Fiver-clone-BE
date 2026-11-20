package com.raktim.fiverclone.sellerApplication.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Same as {@link ApplicationOwnerOnly}, but an ADMIN may also call it.
 * Use it for read-only endpoints that the review team needs.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("@sellerApplicationAccess.isOwnerOrAdmin(#id, authentication)")
public @interface ApplicationOwnerOrAdmin {
}
