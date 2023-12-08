package cn.smilex.vueblog.common.entity.music;

import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.util.CommonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @author smilex
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicSearchResult {
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer id;
    private String name;
    private String artist;
    private String cover;

    public static MusicSearchResult parse(JsonNode song) {
        return new MusicSearchResult(
                song.get("id").asInt(),
                song.get("name").asText(),
                CommonUtils.collectionToStrNotLast(
                        CommonUtils.<List<String>, Throwable>tryRun(
                                () -> {
                                    JsonNode ar = song.get("ar");
                                    return CommonUtils.mapJsonNode(
                                            ar,
                                            v -> v.get("name").asText()
                                    );
                                },
                                Collections::emptyList,
                                e -> log.error("", e)
                        ),
                        v -> v,
                        ", "
                ),
                CommonUtils.tryRun(
                        () -> {
                            JsonNode al = song.get("al");
                            return al.get("picUrl").asText();
                        },
                        () -> CommonConfig.EMPTY_STRING,
                        e -> log.error("", e)
                )
        );
    }
}
