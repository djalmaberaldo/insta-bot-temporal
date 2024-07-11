package com.instagram.bot.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.bson.codecs.pojo.annotations.BsonId;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data
@Builder
@JsonDeserialize(builder = Competition.CompetitionBuilder.class)
public class Competition {

    @BsonId
    private UUID uuid;
    public String name;
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
