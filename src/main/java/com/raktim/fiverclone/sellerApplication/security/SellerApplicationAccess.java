package com.raktim.fiverclone.sellerApplication.security;

import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import com.raktim.fiverclone.user.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * The single place that decides who may touch a seller application.
 * It is called from {@link ApplicationOwnerOnly} and {@link ApplicationOwnerOrAdmin}
 * through the bean name "sellerApplicationAccess".
 *
 * Returning false makes Spring Security answer 403. If the application does not exist,
 * findByIdOrThrow raises the usual 404, so the caller gets the same error as before.
 */
@Component("sellerApplicationAccess")
@RequiredArgsConstructor
public class SellerApplicationAccess {
    private final SellerApplicationService sellerApplicationService;

    public boolean isOwner(UUID applicationId, Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return false;
        }
        SellerApplicationEntity application = sellerApplicationService.findByIdOrThrow(applicationId);
        return application.getUser().getId().equals(principal.getId());
    }

    public boolean isOwnerOrAdmin(UUID applicationId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        return isAdmin || isOwner(applicationId, authentication);
    }
}
