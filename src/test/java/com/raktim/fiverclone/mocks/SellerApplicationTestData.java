package com.raktim.fiverclone.mocks;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevel;
import com.raktim.fiverclone.sellerApplication.dto.*;
import com.raktim.fiverclone.sellerApplication.enums.PortfolioLinkType;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class SellerApplicationTestData {
    public static SellerPersonalProfileRequestDto.SellerPersonalProfileRequestDtoBuilder
        validSellerPersonalProfileRequestDto() {
            Set<UUID> languages = Set.of(UUID.randomUUID(), UUID.randomUUID());
            return SellerPersonalProfileRequestDto.builder()
                    .displayName( "alanwalker")
                    .country("Nepal")
                    .phoneNumber("0406055522")
                    .professionalHeadline("I am very excited")
                    .description("I am motivated software engineer")
                    .languages(languages);
        }

    public static SellerEducationRequestDto.SellerEducationRequestDtoBuilder
        validSellerEducationRequestDto() {
            return SellerEducationRequestDto
                    .builder()
                    .current(false)
                    .degree("Bachelors")
                    .startYear(2018)
                    .endYear(2023)
                    .fieldOfStudy("Computer Science")
                    .country("Nepal")
                    .institutionName("Western Regional Campus");

        }

    public static SellerPortfolioRequestDto.SellerPortfolioRequestDtoBuilder
        validSellerPortfolioRequestDto() {
        return SellerPortfolioRequestDto.builder()
                .url("https://www.portfolio.com")
                .title("Linkedin link")
                .linkType(PortfolioLinkType.LINKEDIN);
    }

    public static SellerCertificationRequestDto.SellerCertificationRequestDtoBuilder
        validSellerCertificationRequestDto() {
        return SellerCertificationRequestDto.builder()
                .certificationName("AWS cloud hero")
                .issuingOrganization("Amazon Web Services")
                .issueDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusDays(1))
                .credentialId("aws-id")
                .credentialUrl("https://www.amazon.com");
    }

    public static SellerProfessionalProfileRequestDto.SellerProfessionalProfileRequestDtoBuilder
        validSellerProfessionalProfileRequestDto() {
        return SellerProfessionalProfileRequestDto.builder()
                .professionalLevel(ExperienceLevel.PRO)
                .occupationId(UUID.fromString("eaec867b-eba1-418a-a1f5-cddb1dcb17a6"))
                .yearsOfExperience(3);
    }
}