package ws.task.tasklist.Service.mappers;

import java.util.List;

public interface Mappable<E, D> {

    D toDto(
            E entity
    );

    List<D> toDto(
            List<E> entity
    );

    E toEntity(
            D dto
    );

}
