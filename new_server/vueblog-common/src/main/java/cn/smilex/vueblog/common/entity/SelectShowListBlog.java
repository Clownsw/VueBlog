package cn.smilex.vueblog.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/18:37
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectShowListBlog {
    private Long id;
    private Long userId;
    private Integer sortId;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created;
    private Short status;
    private List<SelectSortWithBlog> sort;
    private List<SelectBlogOther> tags;
}
