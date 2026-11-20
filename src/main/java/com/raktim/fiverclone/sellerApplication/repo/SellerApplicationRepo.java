package com.raktim.fiverclone.sellerApplication.repo;

import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SellerApplicationRepo extends JpaRepository<SellerApplicationEntity, UUID> {
    List<SellerApplicationEntity> findByUserIdAndStatus(UUID userId, SellerApplicationStatus status);
}