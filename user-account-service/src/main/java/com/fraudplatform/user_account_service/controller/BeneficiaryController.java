package com.fraudplatform.user_account_service.controller;

import com.fraudplatform.user_account_service.annotations.CurrentUser;
import com.fraudplatform.user_account_service.dto.ApiResponse;
import com.fraudplatform.user_account_service.dto.UserPrincipal;
import com.fraudplatform.user_account_service.dto.accounts.BeneficiaryDetailsDTO;
import com.fraudplatform.user_account_service.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficiaries")
@Slf4j
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping
    public ApiResponse<List<BeneficiaryDetailsDTO>> getBeneficiaryDetails(@CurrentUser UserPrincipal userPrincipal) {
        log.info("getBeneficiaryDetails called for " + userPrincipal.getUserId());
        List<BeneficiaryDetailsDTO> beneficiaryDetails = beneficiaryService.getAllBeneficiariesForUser(userPrincipal.getUserId());
        return ApiResponse.success(beneficiaryDetails, "Beneficiary Accounts fetched successfully", HttpStatus.OK);
    }

}
