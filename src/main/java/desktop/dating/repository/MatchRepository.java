package desktop.dating.repository;

import desktop.dating.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllByUser1_IdOrUser2_Id(long user1Id, long user2Id);
}
