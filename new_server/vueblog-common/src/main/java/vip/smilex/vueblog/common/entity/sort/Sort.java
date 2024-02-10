package vip.smilex.vueblog.common.entity.sort;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/11:34
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_sort")
public class Sort {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("`order`")
    private Integer order;

    @TableField("name")
    private String name;
}
