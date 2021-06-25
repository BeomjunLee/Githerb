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
    @DisplayName("회원 정보 보기 테스트")
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
                .name("이범준")
                .picture("https://avatars.githubusercontent.com/u/69130921?v=4")
                .userJob("서버 개발자")
                .userDesc("서버 개발을 좋아합니다")
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
                                parameterWithName("id").description("회원 고유 id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 고유 id"),
                                fieldWithPath("username").type(STRING).description("회원 아이디"),
                                fieldWithPath("name").type(STRING).description("회원 이름"),
                                fieldWithPath("picture").type(STRING).description("회원 프로필 사진"),
                                fieldWithPath("userJob").type(STRING).description("회원 직업"),
                                fieldWithPath("userDesc").type(STRING).description("회원 소개"),
                                fieldWithPath("userTier").type(STRING).description("회원 티어"),
                                fieldWithPath("following").type(NUMBER).description("팔로잉 수"),
                                fieldWithPath("follower").type(NUMBER).description("팔로워 수"),
                                fieldWithPath("plantAchieve.all").type(NUMBER).description("식물 키우기 종료 날짜"),
                                fieldWithPath("plantAchieve.inProgress").type(NUMBER).description("식물 개발 종료 D-day"),
                                fieldWithPath("plantAchieve.done").type(NUMBER).description("커밋 주기"),
                                fieldWithPath("plantAchieve.failed").type(NUMBER).description("총 해야할 커밋수")
                        )
                ));
    }
}