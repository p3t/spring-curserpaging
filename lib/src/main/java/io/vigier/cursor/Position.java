package io.vigier.cursor;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Getter
public class Position {

    private final List<Object> position;
    private final Sort.Order sortOrder;

    public Position( Order sortOrder, Object... position ) {
        this.position = List.of( position );
        this.sortOrder = sortOrder;
    }
}
