package desktop.dating.service;

import desktop.dating.model.Match;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import desktop.dating.repository.MatchRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    public MatchService(
            MatchRepository matchRepository
    ) {
        this.matchRepository = matchRepository;
    }

    public Match getMatch(long id) {
        return matchRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Match> getMatchesForUser(long userId) {
        return this.matchRepository.findAllByUser1_IdOrUser2_Id(userId, userId);
    }

    public void addMatch(Match match) {
        this.matchRepository.save(match);
    }

    public void updateMatch(Match match) {
        this.matchRepository.save(match);
    }

    public void removeMatch(Match match) {
        this.matchRepository.delete(match);
    }
}
