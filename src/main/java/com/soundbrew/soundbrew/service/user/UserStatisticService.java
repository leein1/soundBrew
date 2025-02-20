package com.soundbrew.soundbrew.service.user;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.statistics.user.UserStatisticDTO;

public interface UserStatisticService {
    ResponseDTO<UserStatisticDTO> getUsersStatsForAdmin();
}
