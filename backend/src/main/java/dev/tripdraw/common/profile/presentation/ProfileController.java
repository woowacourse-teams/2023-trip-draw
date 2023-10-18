package dev.tripdraw.common.profile.presentation;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/profile")
@RestController
public class ProfileController {

    private final Environment environment;

    @GetMapping
    public String readProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .findFirst()
                .orElse("");
    }
}
