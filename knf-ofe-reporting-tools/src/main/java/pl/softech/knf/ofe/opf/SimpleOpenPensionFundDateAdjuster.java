package pl.softech.knf.ofe.opf;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class SimpleOpenPensionFundDateAdjuster implements OpenPensionFundDateAdjuster {
    @Override
    public Date adjust(Date date) {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .toLocalDate()
                .with(TemporalAdjusters.firstDayOfMonth());
        instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
