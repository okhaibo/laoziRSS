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
public class Sources implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String link;

    private String title;

    private Integer errorCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
