package com.raktim.fiverclone.sellerApplication.repo;

import com.raktim.fiverclone.sellerApplication.model.SellerPersonalProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerPersonalProfileRepo extends JpaRepository<SellerPersonalProfileEntity, UUID> {
}
