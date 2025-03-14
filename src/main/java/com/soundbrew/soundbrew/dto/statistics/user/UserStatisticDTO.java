package com.soundbrew.soundbrew.dto.statistics.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticDTO {
    private int totalUsers;
    private int newUsersToday;
    private int newUsersWeek;
    private int newUsersMonth;
    private int subscribedUsers;
    private int verifiedUsers;
}
