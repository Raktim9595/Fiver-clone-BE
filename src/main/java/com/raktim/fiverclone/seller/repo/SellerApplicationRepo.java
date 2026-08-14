package com.raktim.fiverclone.seller.repo;

import com.raktim.fiverclone.seller.model.SellerApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerApplicationRepo extends JpaRepository<SellerApplicationEntity, UUID> {
}