package com.githerb.domain.plant.controller;
import com.githerb.domain.plant.dto.request.RequestEnrollPlant;
import com.githerb.domain.plant.dto.response.ResponseEnrollPlant;
import com.githerb.domain.plant.dto.response.ResponsePlantInfo;
import com.githerb.domain.plant.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plants")
public class PlantController {
    private final PlantService plantService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEnrollPlant enrollPlant(@RequestBody RequestEnrollPlant requestEnrollPlant,
                                           @AuthenticationPrincipal OAuth2User oAuth2User) {
        return plantService.enrollPlant(requestEnrollPlant, oAuth2User.getName());
    }

    @GetMapping("/{id}")
    public ResponsePlantInfo getPlant(@AuthenticationPrincipal OAuth2User oAuth2User,
                                      @PathVariable Long id,
                                      HttpSession session) throws IOException {
        return plantService.getPlant(session.getAttribute("oAuthToken").toString(), oAuth2User.getName(), id);
    }
}
