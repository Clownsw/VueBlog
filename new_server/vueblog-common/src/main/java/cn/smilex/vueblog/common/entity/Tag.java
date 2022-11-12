package cn.smilex.vueblog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/11:37
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("name")
    private String name;
}
