package cn.smilex.vueblog.common.entity.other;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/11:33
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_other")
public class Other {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("`order`")
    private Short order;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;
}
