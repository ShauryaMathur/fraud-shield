package com.fraudplatform.user_account_service.controller;

import com.fraudplatform.user_account_service.annotations.CurrentUser;
import com.fraudplatform.user_account_service.dto.ApiResponse;
import com.fraudplatform.user_account_service.dto.UserPrincipal;
import com.fraudplatform.user_account_service.dto.accounts.AccountDetailsDTO;
import com.fraudplatform.user_account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_OWN_TRANSACTIONS')")
    public ApiResponse<List<AccountDetailsDTO>> getAccountDetails(@CurrentUser UserPrincipal userPrincipal) {
        log.info("getAccountDetails called for " + userPrincipal.getUserId());
        List<AccountDetailsDTO> accountDetails = accountService.findAllByUserId(userPrincipal.getUserId());
        return ApiResponse.success(accountDetails, "Accounts fetched successfully", HttpStatus.OK);
    }
}
