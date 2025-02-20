package com.soundbrew.soundbrew.statistic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.sound.SoundTotalStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.tag.TagsTotalStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.user.UserStatisticDTO;
import com.soundbrew.soundbrew.dto.statistics.usersubscription.SubscriptionTotalStatisticDTO;
import com.soundbrew.soundbrew.service.sound.SoundsStatisticService;
import com.soundbrew.soundbrew.service.tag.TagsStatisticService;
import com.soundbrew.soundbrew.service.user.UserStatisticService;
import com.soundbrew.soundbrew.service.userSubscription.UserSubscriptionStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@Transactional
public class StatisticIntegrationTest {
    @Autowired private UserStatisticService userStatisticService;
    @Autowired private TagsStatisticService tagsStatisticService;
    @Autowired private UserSubscriptionStatisticService userSubscriptionStatisticService;
    @Autowired private SoundsStatisticService soundsStatisticService;

    @Test
    public void testUserStatistics() {
        ResponseDTO<UserStatisticDTO> response = userStatisticService.getUsersStatsForAdmin();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String prettyResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            log.info("User Statistics:\n{}", prettyResponse);
        } catch (Exception e) {
            log.error("Error converting response to JSON", e);
        }

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getDto(), "UserStatisticDTO should not be null");
    }

    @Test
    public void testTagsStatistics() {
        ResponseDTO<TagsTotalStatisticDTO> response = tagsStatisticService.getTagsWithTopUsage();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String prettyResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            log.info("Tags Statistics:\n{}", prettyResponse);
        } catch (Exception e) {
            log.error("Error converting tags statistics response to JSON", e);
        }

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getDto(), "TagsTotalStatisticDTO should not be null");
    }

    @Test
    public void testUserSubscriptionStatistics() {
        ResponseDTO<SubscriptionTotalStatisticDTO> response = userSubscriptionStatisticService.getUserSubscriptionStatsForAdmin();
        log.info("User Subscription Statistics: {}", response);
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getDto(), "SubscriptionTotalStatisticDTO should not be null");
    }

    @Test
    public void testSoundStatistics() {
        // RequestDTO 생성 (필요한 값이 있다면 설정)
        RequestDTO requestDTO = new RequestDTO();
        ResponseDTO<SoundTotalStatisticDTO> response = soundsStatisticService.getSoundStats(requestDTO);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String prettyResponse = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            log.info("Sound Statistics:\n{}", prettyResponse);
        } catch (Exception e) {
            log.error("Error converting sound statistics response to JSON", e);
        }

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getDto(), "SoundTotalStatisticDTO should not be null");
    }

}
