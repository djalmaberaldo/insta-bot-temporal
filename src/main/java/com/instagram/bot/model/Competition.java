package com.instagram.bot.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = Competition.CompetitionBuilder.class)
public class Competition {

    public Map<String, List<Result>> results;

    @Data
    @Builder
    @JsonDeserialize(builder = Result.ResultBuilder.class)
    public static class Result implements Serializable {
        public String position;
        public String athlete;
        public String mark;
        public String country;
    }

}
