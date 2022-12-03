package cn.smilex.vueblog.common.entity.other;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author smilex
 * @date 2022/11/14/21:56
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AboutMe {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String title;
    private String content;

    public static AboutMe fromOther(Other other) {
        if (other == null) {
            return new AboutMe();
        }
        final String content = StringUtils.isBlank(other.getContent()) ? "这个家伙很懒, 什么都没写..." : other.getContent();
        return new AboutMe(other.getId(), other.getTitle(), content);
    }
}
