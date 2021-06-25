package com.githerb.domain.plant.service;
import com.githerb.domain.plant.entity.Plant;
import com.githerb.domain.user.entity.User;
import com.githerb.domain.plant.dto.CommitDto;
import com.githerb.domain.plant.dto.CommitLanguageDto;
import com.githerb.domain.plant.dto.PlantDto;
import com.githerb.domain.plant.dto.request.RequestEnrollPlant;
import com.githerb.domain.plant.dto.response.ResponseEnrollPlant;
import com.githerb.domain.plant.dto.response.ResponsePlantInfo;
import com.githerb.domain.plant.repository.PlantQueryRepository;
import com.githerb.domain.plant.repository.PlantRepository;
import com.githerb.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import static com.githerb.global.type.ResultType.RESULT_ENROLL_PLANT;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantService {

    private final PlantRepository plantRepository;
    private final PlantQueryRepository plantQueryRepository;
    private final UserRepository userRepository;

    /**
     * 식물 등록
     * @param requestEnrollPlant 식물 등록 form
     * @param username 사용자 아이디
     * @return 응답 dto
     */
    public ResponseEnrollPlant enrollPlant(RequestEnrollPlant requestEnrollPlant, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        Plant plant = Plant.createPlant(requestEnrollPlant, user);
        Plant savedPlant = plantRepository.save(plant);

        return ResponseEnrollPlant.builder()
                .message(RESULT_ENROLL_PLANT.getMessage())
                .data(PlantDto.builder()
                        .id(savedPlant.getId())
                        .frontName(savedPlant.getFrontName())
                        .backName(savedPlant.getBackName())
                        .plantStatus(savedPlant.getPlantStatus())
                        .repoName(savedPlant.getRepoName())
                        .repoDesc(savedPlant.getDesc())
                        .startDate(savedPlant.getStartDate())
                        .deadLine(savedPlant.getDeadLine())
                        .commitCycle(savedPlant.getCommitCycle())
                        .build())
                .build();
    }

    /**
     * 식물 조회
     * @param token access_token
     * @param username 사용자 아이디
     * @param id 조회할 식물 id
     * @return 응답 dto
     */
    public ResponsePlantInfo getPlant(String token, String username, Long id) throws IOException {
        GitHub gitHub = new GitHubBuilder()
                .withOAuthToken(token, username)
                .build();

        Plant plant = plantQueryRepository.findByIdJoinUser(id, username).orElseThrow(() -> new IllegalArgumentException("식물 레포를 찾을 수 없습니다"));

        GHRepository repository = gitHub.getRepository(plant.getRepoName());
        List<CommitDto> commitDtos = transferCommitDto(repository);

        List<CommitLanguageDto> comLang = getCommitLanguages(repository.listLanguages());

        double seconds = Duration.between(plant.getStartDate(), plant.getDeadLine()).getSeconds();
        int decimalDay = (int) Math.ceil(seconds / 3600 / 24);
        int commitCount = decimalDay / plant.getCommitCycle();

        return ResponsePlantInfo.builder()
                .id(plant.getId())
                .userId(plant.getUser().getId())
                .frontName(plant.getFrontName())
                .backName(plant.getBackName())
                //TODO 레벨 체크 추가해야됨
                .plantLevel(null)
                .plantStatus(plant.getPlantStatus())
                .repoName(plant.getRepoName())
                .repoDesc(plant.getDesc())
                .startDate(plant.getStartDate())
                .deadLine(plant.getDeadLine())
                .decimalDay(decimalDay)
                .commitCycle(plant.getCommitCycle())
                .commitCount(commitCount)
                .totalCommit(commitDtos.size())
                .commit(commitDtos)
                .comLang(comLang)
                .build();
    }

    /**
     * 커밋 언어 추출 후 퍼센트 계산
     * @param languages 추출된 언어 Map
     * @return 커밋 언어 dto 리스트
     */
    private List<CommitLanguageDto> getCommitLanguages(Map<String, Long> languages) {
        List<CommitLanguageDto> comLang = new ArrayList<>();

        double total = 0;
        for (String key : languages.keySet()) {
            CommitLanguageDto commitLanguage = CommitLanguageDto.builder()
                    .language(key)
                    .usedLine(languages.get(key))
                    .build();
            total += languages.get(key);
            comLang.add(commitLanguage);
        }

        double finalTotal = total;
        Comparator<CommitLanguageDto> compareByPercentage = Comparator.comparing((CommitLanguageDto coLang) -> coLang.getPercentage()).reversed();
        LinkedList<CommitLanguageDto> commitLanguageDtoLinkedList = comLang.stream().peek(lan -> lan.calculatePercentage(finalTotal)).sorted(compareByPercentage)
                .collect(Collectors.toCollection(LinkedList::new));

        return extractOtherLanguage(commitLanguageDtoLinkedList);
    }

    /**
     * 커밋 언어 그 외 필드 추가 및 변환
     * @param comLang 커밋 언어 dto 리스트 (추가 삭제를 위해 링크드 리스트)
     * @return 작업 완료된 커밋 언어 dto 리스트 (arrayList)
     */
    private List<CommitLanguageDto> extractOtherLanguage(LinkedList<CommitLanguageDto> comLang) {
        double percentSum = 0;
        long usedLineSum = 0;

        int languageVisibleCount = 4;
        int count = 0;
        int originalCommitLanguageDtoSize = comLang.size();
        for (int i = 0; i < originalCommitLanguageDtoSize; i++) {
            count++;
            if (count > languageVisibleCount) {
                percentSum += comLang.get(languageVisibleCount).getPercentage();
                usedLineSum += comLang.get(languageVisibleCount).getUsedLine();
                comLang.remove(languageVisibleCount);
            }

            if (count == originalCommitLanguageDtoSize && percentSum != 0 && usedLineSum != 0) {
                CommitLanguageDto commitLanguageDto = CommitLanguageDto.builder()
                        .language("others")
                        .percentage(percentSum)
                        .usedLine(usedLineSum)
                        .build();
                comLang.add(commitLanguageDto);
            }
        }
        return comLang;
    }

    /**
     * 커밋들 추출해서 사용할 dto 로 변환
     * @param repository github api repo
     * @return 커밋 dto 리스트
     */
    private List<CommitDto> transferCommitDto(GHRepository repository) throws IOException {
        return repository.listCommits().toList().stream().map(commit -> {
            try {
                CommitDto commitDto = CommitDto.builder()
                        .date(LocalDateTime.ofInstant(commit.getCommitShortInfo().getCommitDate().toInstant(), ZoneId.systemDefault()))
                        .committer(commit.getAuthor() == null ? "can't extract committer" : commit.getAuthor().getLogin())
                        .message(commit.getCommitShortInfo().getMessage())
                        .build();
                return commitDto;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }
}
