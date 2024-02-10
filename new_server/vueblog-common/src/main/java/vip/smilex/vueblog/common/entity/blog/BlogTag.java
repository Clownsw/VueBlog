package vip.smilex.vueblog.common.entity.blog;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/12/11:30
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_blogtag")
public class BlogTag {
    @TableField("blogId")
    private Long blogId;

    @TableField("tagId")
    private Long tagId;

    @TableField("sort")
    private Long sort;
}
