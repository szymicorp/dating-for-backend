package desktop.dating.service;

import desktop.dating.model.Like;
import org.springframework.stereotype.Service;
import desktop.dating.repository.LikeRepository;

@Service
public class LikeService {
    public final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void addLike(Like like) {
        this.likeRepository.save(like);
    }

    public void removeLike(Like like) {
        this.likeRepository.delete(like);
    }
}
