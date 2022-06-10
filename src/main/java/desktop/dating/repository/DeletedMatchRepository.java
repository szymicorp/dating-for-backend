package desktop.dating.repository;

import desktop.dating.model.DeletedMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedMatchRepository extends JpaRepository<DeletedMatch, Long> {
}
