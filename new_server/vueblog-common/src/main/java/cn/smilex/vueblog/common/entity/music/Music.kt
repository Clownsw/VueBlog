package cn.smilex.vueblog.common.entity.music

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

/**
 * @author smilex
 * @date 2023/12/8 13:55:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("m_music")
data class Music(
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    var id: Long,

    @TableField(value = "name")
    var name: String,

    @TableField(value = "artist")
    var artist: String,

    @TableField(value = "url")
    var url: String,

    @TableField(value = "cover")
    var cover: String,

    @TableField(value = "lrc")
    var lrc: String,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @TableField("created")
    var created: LocalDateTime
)