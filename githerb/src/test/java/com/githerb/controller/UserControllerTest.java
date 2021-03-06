package com.githerb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.githerb.domain.plant.dto.CommitDto;
import com.githerb.domain.plant.dto.CommitLanguageDto;
import com.githerb.domain.plant.dto.PlantAchieveDto;
import com.githerb.domain.plant.dto.response.ResponsePlantInfo;
import com.githerb.domain.plant.service.PlantService;
import com.githerb.domain.user.controller.UserController;
import com.githerb.domain.user.dto.response.ResponseUserInfo;
import com.githerb.domain.user.service.UserService;
import com.githerb.global.security.CustomOAuth2UserService;
import com.githerb.global.security.handler.CustomAuthenticationEntryPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.githerb.domain.plant.type.PlantStatus.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    @MockBean
    CustomOAuth2UserService customOAuth2UserService;
    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    public void getUserInfo() throws Exception{
        //given
        PlantAchieveDto plantAchieveDto = PlantAchieveDto.builder()
                .all(4)
                .inProgress(2)
                .done(1)
                .failed(1)
                .build();

        ResponseUserInfo responseUserInfo = ResponseUserInfo.builder()
                .id(1L)
                .username("BeomjunLee")
                .name("?????????")
                .picture("https://avatars.githubusercontent.com/u/69130921?v=4")
                .userJob("?????? ?????????")
                .userDesc("?????? ????????? ???????????????")
                .userTier("GOLD")
                .following(30)
                .follower(20)
                .plantAchieve(plantAchieveDto)
                .build();

        given(userService.getUserInfo(anyLong())).willReturn(responseUserInfo);
        //when
        ResultActions result = mockMvc.perform(
                get("/users/{id}", 1L).with(oidcLogin())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk())
                .andDo(document("getUserInfo",
                        pathParameters(
                                parameterWithName("id").description("?????? ?????? id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????? id"),
                                fieldWithPath("username").type(STRING).description("?????? ?????????"),
                                fieldWithPath("name").type(STRING).description("?????? ??????"),
                                fieldWithPath("picture").type(STRING).description("?????? ????????? ??????"),
                                fieldWithPath("userJob").type(STRING).description("?????? ??????"),
                                fieldWithPath("userDesc").type(STRING).description("?????? ??????"),
                                fieldWithPath("userTier").type(STRING).description("?????? ??????"),
                                fieldWithPath("following").type(NUMBER).description("????????? ???"),
                                fieldWithPath("follower").type(NUMBER).description("????????? ???"),
                                fieldWithPath("plantAchieve.all").type(NUMBER).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("plantAchieve.inProgress").type(NUMBER).description("?????? ?????? ?????? D-day"),
                                fieldWithPath("plantAchieve.done").type(NUMBER).description("?????? ??????"),
                                fieldWithPath("plantAchieve.failed").type(NUMBER).description("??? ????????? ?????????")
                        )
                ));
    }
}