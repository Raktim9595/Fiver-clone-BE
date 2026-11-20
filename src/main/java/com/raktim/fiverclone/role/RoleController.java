package com.raktim.fiverclone.role;

import com.raktim.fiverclone.role.model.RoleEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "List of endpoints for everything related to roles")
public class RoleController {
    private final RoleService service;

    @GetMapping
    @Operation(
            summary = "End point to fetch the list of all available roles"
    )
    public ResponseEntity<List<RoleEntity>> getAll() {
        List<RoleEntity> roleEntities = service.findAll();
        return ResponseEntity.ok(roleEntities);
    }
}
