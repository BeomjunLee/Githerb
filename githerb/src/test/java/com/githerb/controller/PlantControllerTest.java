package com.githerb.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.githerb.domain.plant.controller.PlantController;
import com.githerb.domain.plant.dto.CommitDto;
import com.githerb.domain.plant.dto.CommitLanguageDto;
import com.githerb.domain.plant.dto.PlantDto;
import com.githerb.domain.plant.dto.request.RequestEnrollPlant;
import com.githerb.domain.plant.dto.response.ResponseEnrollPlant;
import com.githerb.domain.plant.dto.response.ResponsePlantInfo;
import com.githerb.domain.plant.type.PlantStatus;
import com.githerb.global.security.CustomOAuth2UserService;
import com.githerb.global.security.handler.CustomAuthenticationEntryPoint;
import com.githerb.domain.plant.service.PlantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.githerb.domain.plant.type.PlantStatus.IN_PROGRESS;
import static com.githerb.global.type.ResultType.RESULT_ENROLL_PLANT;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlantController.class)
@AutoConfigureRestDocs
class PlantControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    PlantService plantService;
    @MockBean
    CustomOAuth2UserService customOAuth2UserService;
    @MockBean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    @DisplayName("?????? ?????? ?????????")
    public void enrollPlant() throws Exception{
        //given
        ResponseEnrollPlant responseEnrollPlant = ResponseEnrollPlant.builder()
                .message(RESULT_ENROLL_PLANT.getMessage())
                .data(PlantDto.builder()
                        .id(2L)
                        .frontName("?????????")
                        .backName("?????????")
                        .plantStatus(IN_PROGRESS)
                        .repoName("BeomjunLee/TableOrder")
                        .repoDesc("?????? ??????????????????")
                        .startDate(LocalDateTime.of(2021, 06, 24, 22, 00, 00, 545915))
                        .deadLine(LocalDateTime.of(2021, 07, 23, 18, 00, 00))
                        .commitCycle(3)
                        .build())
                .build();

        given(plantService.enrollPlant(any(RequestEnrollPlant.class), anyString())).willReturn(responseEnrollPlant);

        //when
        RequestEnrollPlant requestEnrollPlant = RequestEnrollPlant.builder()
                .repoName("BeomjunLee/TableOrder")
                .plantName("?????????")
                .repoDesc("?????? ??????????????????")
                .deadLine("2021-07-23 18:00:00")
                .commitCycle(3)
                .build();

        ResultActions result = mockMvc.perform(
                post("/plants").with(oidcLogin())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestEnrollPlant))
                        .accept(APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isCreated())
                .andDo(document("enrollPlant",
                        requestFields(
                                fieldWithPath("repoName").type(STRING).description("?????? ?????? (?????????/????????????)"),
                                fieldWithPath("plantName").type(STRING).description("?????? ??????"),
                                fieldWithPath("repoDesc").type(STRING).description("?????? ??????"),
                                fieldWithPath("deadLine").type(STRING).description("?????? ?????? ?????? (yyyy-MM-dd HH:ss:mm) (???????????? ??????)"),
                                fieldWithPath("commitCycle").type(NUMBER).description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("message").type(STRING).description("?????? ?????????"),
                                fieldWithPath("data.id").type(NUMBER).description("?????? ?????? id"),
                                fieldWithPath("data.frontName").type(STRING).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("data.backName").type(STRING).description("?????? ??????"),
                                fieldWithPath("data.plantStatus").type(STRING).description("?????? ?????? ?????? ??????"),
                                fieldWithPath("data.repoName").type(STRING).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("data.repoDesc").type(STRING).description("?????? ??????"),
                                fieldWithPath("data.startDate").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("data.deadLine").type(STRING).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("data.commitCycle").type(NUMBER).description("?????? ??????")
                        )
                ));
    }
    
    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    public void getPlantInfo() throws Exception{
        //given
        List<CommitDto> commitDtos = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            CommitDto commitDto = CommitDto.builder()
                    .date(LocalDateTime.of(2021, 06, i, 12, 00, 00))
                    .committer("?????????"+i)
                    .message("?????????"+i)
                    .build();
            commitDtos.add(commitDto);
        }

        List<CommitLanguageDto> commitLanguageDtos = makeCommitLanguageDtos();

        ResponsePlantInfo responsePlantInfo = ResponsePlantInfo.builder()
                .id(2L)
                .userId(1L)
                .frontName("?????????")
                .backName("?????????")
                .plantLevel(1)
                .plantStatus(IN_PROGRESS)
                .repoName("BeomjunLee/TableOrder")
                .repoDesc("?????? ??????????????????")
                .startDate(LocalDateTime.of(2021, 06, 24, 22, 00, 00, 545915))
                .deadLine(LocalDateTime.of(2021, 07, 23, 18, 00, 00))
                .decimalDay(29)
                .commitCycle(3)
                .commitCount(9)
                .totalCommit(5)
                .commit(commitDtos)
                .comLang(commitLanguageDtos)
                .build();

        given(plantService.getPlant(anyString(), anyString(), anyLong())).willReturn(responsePlantInfo);
        //when
        ResultActions result = mockMvc.perform(
                get("/plants/{id}", 2L).with(oidcLogin())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print());
    
        //then
        result.andExpect(status().isOk())
                .andDo(document("getPlantInfo",
                        pathParameters(
                                parameterWithName("id").description("?????? ?????? id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????? id"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ?????? id"),
                                fieldWithPath("frontName").type(STRING).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("backName").type(STRING).description("?????? ??????"),
                                fieldWithPath("plantLevel").type(NUMBER).description("?????? ??????"),
                                fieldWithPath("plantStatus").type(STRING).description("?????? ?????? ?????? ??????"),
                                fieldWithPath("repoName").type(STRING).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("repoDesc").type(STRING).description("?????? ??????"),
                                fieldWithPath("startDate").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("deadLine").type(STRING).description("?????? ????????? ?????? ??????"),
                                fieldWithPath("decimalDay").type(NUMBER).description("?????? ?????? ?????? D-day"),
                                fieldWithPath("commitCycle").type(NUMBER).description("?????? ??????"),
                                fieldWithPath("commitCount").type(NUMBER).description("??? ????????? ?????????"),
                                fieldWithPath("totalCommit").type(NUMBER).description("??? ?????????"),
                                fieldWithPath("commit.[].date").type(STRING).description("????????? ??????"),
                                fieldWithPath("commit.[].committer").type(STRING).description("????????? ?????? ?????????"),
                                fieldWithPath("commit.[].message").type(STRING).description("?????? ?????????"),
                                fieldWithPath("comLang.[].language").type(STRING).description("?????? ??????"),
                                fieldWithPath("comLang.[].percentage").type(NUMBER).description("?????? ?????? ?????????"),
                                fieldWithPath("comLang.[].usedLine").type(NUMBER).description("?????? ?????? ??????")


                        )
                ));
    }

    private List<CommitLanguageDto> makeCommitLanguageDtos() {
        List<CommitLanguageDto> commitLanguageDtos = new ArrayList<>();
        CommitLanguageDto commitLanguageDto1 = CommitLanguageDto.builder()
                .language("Kotlin")
                .percentage(35.1)
                .usedLine(36741L)
                .build();
        commitLanguageDtos.add(commitLanguageDto1);
        CommitLanguageDto commitLanguageDto2 = CommitLanguageDto.builder()
                .language("JavaScript")
                .percentage(33.2)
                .usedLine(34811L)
                .build();
        commitLanguageDtos.add(commitLanguageDto2);
        CommitLanguageDto commitLanguageDto3 = CommitLanguageDto.builder()
                .language("CSS")
                .percentage(15.6)
                .usedLine(16307L)
                .build();
        commitLanguageDtos.add(commitLanguageDto3);
        CommitLanguageDto commitLanguageDto4 = CommitLanguageDto.builder()
                .language("HTML")
                .percentage(14.5)
                .usedLine(15227L)
                .build();
        commitLanguageDtos.add(commitLanguageDto4);
        CommitLanguageDto commitLanguageDto5 = CommitLanguageDto.builder()
                .language("others")
                .percentage(1.5)
                .usedLine(1649L)
                .build();
        commitLanguageDtos.add(commitLanguageDto5);
        return commitLanguageDtos;
    }
}