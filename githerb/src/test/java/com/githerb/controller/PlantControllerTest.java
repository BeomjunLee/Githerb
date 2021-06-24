package com.githerb.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.githerb.domain.plant.controller.PlantController;
import com.githerb.domain.plant.dto.PlantDto;
import com.githerb.domain.plant.dto.request.RequestEnrollPlant;
import com.githerb.domain.plant.dto.response.ResponseEnrollPlant;
import com.githerb.global.security.CustomOAuth2UserService;
import com.githerb.global.security.handler.CustomAuthenticationEntryPoint;
import com.githerb.domain.plant.service.PlantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.time.LocalDateTime;
import static com.githerb.domain.plant.type.PlantStatus.IN_PROGRESS;
import static com.githerb.global.type.ResultType.RESULT_ENROLL_PLANT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    @DisplayName("식물 등록 테스트")
    public void enrollPlant() throws Exception{
        //given
        ResponseEnrollPlant responseEnrollPlant = ResponseEnrollPlant.builder()
                .message(RESULT_ENROLL_PLANT.getMessage())
                .data(PlantDto.builder()
                        .id(2L)
                        .frontName("푸릇한")
                        .backName("새싹이")
                        .plantStatus(IN_PROGRESS)
                        .repoName("BeomjunLee/TableOrder")
                        .repoDesc("주문 시스템입니다")
                        .startDate(LocalDateTime.of(2021, 06, 24, 22, 00, 00))
                        .deadLine(LocalDateTime.of(2021, 07, 23, 18, 00, 00))
                        .commitCycle(3)
                        .build())
                .build();

        given(plantService.enrollPlant(any(RequestEnrollPlant.class), anyString())).willReturn(responseEnrollPlant);

        //when
        RequestEnrollPlant requestEnrollPlant = RequestEnrollPlant.builder()
                .repoName("BeomjunLee/TableOrder")
                .plantName("새싹이")
                .repoDesc("주문 시스템입니다")
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
                                fieldWithPath("repoName").type(STRING).description("레포 경로 (아이디/레포이름)"),
                                fieldWithPath("plantName").type(STRING).description("식물 이름"),
                                fieldWithPath("repoDesc").type(STRING).description("식물 설명"),
                                fieldWithPath("deadLine").type(STRING).description("마감 날짜 시간 (yyyy-MM-dd HH:ss:mm) (띄어쓰기 주의)"),
                                fieldWithPath("commitCycle").type(NUMBER).description("커밋 주기")
                        ),
                        responseFields(
                                fieldWithPath("message").type(STRING).description("상태 메세지"),
                                fieldWithPath("data.id").type(NUMBER).description("식물 id"),
                                fieldWithPath("data.frontName").type(STRING).description("랜덤한 식물 앞쪽 명칭"),
                                fieldWithPath("data.backName").type(STRING).description("식물 이름"),
                                fieldWithPath("data.plantStatus").type(STRING).description("식물 개발 진행 상태"),
                                fieldWithPath("data.repoName").type(STRING).description("식물 것허브 레포 이름"),
                                fieldWithPath("data.repoDesc").type(STRING).description("식물 설명"),
                                fieldWithPath("data.startDate").type(STRING).description("식물 만든 날짜"),
                                fieldWithPath("data.deadLine").type(STRING).description("식물 키우기 종료 날짜"),
                                fieldWithPath("data.commitCycle").type(NUMBER).description("커밋 주기")
                        )
                ));
    }
}