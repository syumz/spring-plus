package org.example.expert.domain.manager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.config.CustomUserDetails;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos/{todoId}/managers")
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ManagerSaveResponse> saveManager(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable long todoId,
            @Valid @RequestBody ManagerSaveRequest managerSaveRequest
    ) {
        return ResponseEntity.ok(managerService.saveManager(authUser, todoId, managerSaveRequest));
    }

    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getMembers(@PathVariable long todoId) {
        return ResponseEntity.ok(managerService.getManagers(todoId));
    }

    @DeleteMapping("/{managerId}")
    public void deleteManager(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable long todoId,
            @PathVariable long managerId
    ) {
        managerService.deleteManager(authUser, todoId, managerId);
    }
}
