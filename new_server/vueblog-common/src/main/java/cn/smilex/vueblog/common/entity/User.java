package cn.smilex.vueblog.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author smilex
 * @date 2022/11/12/11:38
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("username")
    private String userName;

    @TableField("avatar")
    private String avatar;

    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    @TableField("status")
    private Integer status;

    @TableField("created")
    private LocalDateTime created;

    @TableField("last_login")
    private LocalDateTime lastLogin;
}
