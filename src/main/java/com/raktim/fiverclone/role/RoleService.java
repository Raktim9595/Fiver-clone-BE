package com.raktim.fiverclone.role;

import com.raktim.fiverclone.role.model.RoleEntity;
import com.raktim.fiverclone.role.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepo repo;

    private static Logger log = LoggerFactory.getLogger(RoleService.class);

    public List<RoleEntity> findAll() {
        log.info("Fetching all roles");
        return repo.findAll();
    }
}
