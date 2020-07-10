package tg.bot.rssgo.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    private String hashId;

    private String rawId;

    private String rawLink;

    private String title;

    private String description;

    private String telegraphUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
