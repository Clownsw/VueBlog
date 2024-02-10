package vip.smilex.vueblog.common.entity.other;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/11:36
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_system")
public class System {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("welcome")
    private String welcome;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;
}
