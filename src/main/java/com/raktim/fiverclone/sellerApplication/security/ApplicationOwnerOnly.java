package com.raktim.fiverclone.sellerApplication.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Put on a controller method whose path variable is named {@code id} (the application id).
 * Only the user who owns that application may call it. Use it for every change (create, update, delete, submit).
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("@sellerApplicationAccess.isOwner(#id, authentication)")
public @interface ApplicationOwnerOnly {
}
