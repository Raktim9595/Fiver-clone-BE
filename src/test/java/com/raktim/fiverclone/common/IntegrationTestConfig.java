package com.raktim.fiverclone.common;

import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({
        UserTestDataSeeder.class,
        SellerApplicationTestDataSeeder.class
})
public class IntegrationTestConfig {
}