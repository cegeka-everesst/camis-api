package com.cegeka.horizon.camis.timesheet.api.get;

import org.junit.jupiter.api.Test;
import org.threeten.extra.LocalDateRange;

import java.util.List;

import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

class LocalDateRangeSplitterTest {

    @Test
    public void givenLessThanWeek_whenSplitByWeek_thenThatWeek(){
        List<LocalDateRange> localDateRanges = LocalDateRangeSplitter.splitByWeek(LocalDateRange.of(
                of(2022, 12, 19),
                of(2022, 12, 23)));

        assertThat(localDateRanges).hasSize(1);
        assertThat(localDateRanges.get(0).getStart()).isEqualTo(of(2022, 12, 19));
        assertThat(localDateRanges.get(0).getEnd()).isEqualTo(of(2022, 12, 23));

    }

    @Test
    public void givenExactlyAWeek_whenSplitByWeek_thenThatWeek(){
        List<LocalDateRange> localDateRanges = LocalDateRangeSplitter.splitByWeek(LocalDateRange.of(
                of(2022, 12, 19),
                of(2022, 12, 25)));

        assertThat(localDateRanges).hasSize(1);
        assertThat(localDateRanges.get(0).getStart()).isEqualTo(of(2022, 12, 19));
        assertThat(localDateRanges.get(0).getEnd()).isEqualTo(of(2022, 12, 25));
    }

    @Test
    public void givenMoreThanWeek_whenSplitByWeek_thenThatWeek(){
        List<LocalDateRange> localDateRanges = LocalDateRangeSplitter.splitByWeek(LocalDateRange.of(
                of(2022, 12, 19),
                of(2022, 12, 29)));

        assertThat(localDateRanges).hasSize(2);
        assertThat(localDateRanges.get(0).getStart()).isEqualTo(of(2022, 12, 19));
        assertThat(localDateRanges.get(0).getEnd()).isEqualTo(of(2022, 12, 25));
        assertThat(localDateRanges.get(1).getStart()).isEqualTo(of(2022, 12, 26));
        assertThat(localDateRanges.get(1).getEnd()).isEqualTo(of(2022, 12, 29));
    }

    @Test
    public void givenCrossAMonth_whenSplitByWeek_thenThatWeek(){
        List<LocalDateRange> localDateRanges = LocalDateRangeSplitter.splitByWeek(LocalDateRange.of(
                of(2022, 12, 19),
                of(2023, 1, 1)));

        assertThat(localDateRanges).hasSize(2);
        assertThat(localDateRanges.get(0).getStart()).isEqualTo(of(2022, 12, 19));
        assertThat(localDateRanges.get(0).getEnd()).isEqualTo(of(2022, 12, 25));
        assertThat(localDateRanges.get(1).getStart()).isEqualTo(of(2022, 12, 26));
        assertThat(localDateRanges.get(1).getEnd()).isEqualTo(of(2023, 1, 1));
    }

    @Test
    public void givenExactlyAWeekWithAMonday_whenSplitByWeek_thenThatWeek(){
        List<LocalDateRange> localDateRanges = LocalDateRangeSplitter.splitByWeek(LocalDateRange.of(
                of(2022, 12, 19),
                of(2023, 1, 2)));

        assertThat(localDateRanges).hasSize(3);
        assertThat(localDateRanges.get(2).getStart()).isEqualTo(of(2023, 1, 2));
        assertThat(localDateRanges.get(2).getEnd()).isEqualTo(of(2023, 1, 2));
    }

}