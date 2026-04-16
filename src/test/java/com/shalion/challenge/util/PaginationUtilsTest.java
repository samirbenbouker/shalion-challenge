package com.shalion.challenge.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationUtilsTest {

    @Test
    void toPageableParsesDirectionAndField() {
        Pageable pageable = PaginationUtils.toPageable(2, 15, "name,desc", "id");

        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(15);
        assertThat(pageable.getSort().getOrderFor("name").isDescending()).isTrue();
    }

    @Test
    void toPageableNormalizesArrayLikeSortInput() {
        Pageable pageable = PaginationUtils.toPageable(0, 20, "[\"id\"]", "id");

        assertThat(pageable.getSort().getOrderFor("id")).isNotNull();
        assertThat(pageable.getSort().getOrderFor("id").isAscending()).isTrue();
    }
}
