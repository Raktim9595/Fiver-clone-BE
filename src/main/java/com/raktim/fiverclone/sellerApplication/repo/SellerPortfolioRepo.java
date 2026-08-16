package com.raktim.fiverclone.sellerApplication.repo;

import com.raktim.fiverclone.sellerApplication.model.SellerPortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerPortfolioRepo extends JpaRepository<SellerPortfolioEntity, UUID> {
}
