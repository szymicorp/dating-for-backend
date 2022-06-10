package desktop.dating.service;

import desktop.dating.model.DeletedMatch;
import desktop.dating.repository.DeletedMatchRepository;
import org.springframework.stereotype.Service;

@Service
public class DeletedMatchService {
    private final DeletedMatchRepository deletedMatchRepository;

    public DeletedMatchService(DeletedMatchRepository deletedMatchRepository) {
        this.deletedMatchRepository = deletedMatchRepository;
    }

    public void addDeletedMatch(DeletedMatch deletedMatch) {
        deletedMatchRepository.save(deletedMatch);
    }
}
