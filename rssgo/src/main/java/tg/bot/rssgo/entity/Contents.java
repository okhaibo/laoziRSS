package tg.bot.rssgo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author HIBO
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Contents implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer sourceId;

    @TableId
    private String hashId;

    private String rawId;

    private String rawLink;

    private String title;

    private String description;

    private String telegraphUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
