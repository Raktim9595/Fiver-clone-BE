package com.raktim.fiverclone.sellerApplication.repo;

import com.raktim.fiverclone.sellerApplication.model.SellerProfessionalProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerProfessionalProfileRepo extends JpaRepository<SellerProfessionalProfileEntity, UUID> {
}
