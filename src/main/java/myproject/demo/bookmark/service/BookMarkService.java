package myproject.demo.bookmark.service;


import lombok.RequiredArgsConstructor;
import myproject.demo.Novel.service.NovelService;
import myproject.demo.User.service.UserService;
import myproject.demo.bookmark.domain.BookMark;
import myproject.demo.bookmark.domain.BookMarkId;
import myproject.demo.bookmark.domain.BookMarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final UserService userService;
    private final NovelService novelService;
    private final BookMarkRepository bookMarkRepository;

    public void create(Long novelId){
        Long userId = userService.findLoggedUser().getUserId();
        novelService.checkExistenceById(novelId);
        checkAlreadyExistence(userId, novelId);
        makeOrResurrect(novelId, userId);
    }

    public void makeOrResurrect(Long novelId, Long userId) {

        if (bookMarkRepository.existsById(BookMarkId.create(userId, novelId))){
            BookMark bookMark = bookMarkRepository.findById(BookMarkId.create(userId, novelId)).get();
            bookMark.resurrect();
            bookMarkRepository.save(bookMark);
        }else {
            bookMarkRepository.saveAndFlush(BookMark.create(userId, novelId));
        }
    }


    public void delete(Long novelId){
        Long userId = userService.findLoggedUser().getUserId();
        novelService.checkExistenceById(novelId);
        checkExist(userId,novelId);

        BookMark bookMark = bookMarkRepository.findById(BookMarkId.create(userId, novelId)).get();
        bookMark.delete();
        bookMarkRepository.save(bookMark);
    }


    private void checkExist(Long userId, Long novelId) {
        if(bookMarkRepository.findById(BookMarkId.create(userId, novelId)).isEmpty()){
            throw new IllegalArgumentException();
        }
    }

    private void checkAlreadyExistence(Long userId, Long novelId) {
        Optional<BookMark> bookMark = bookMarkRepository.findById(BookMarkId.create(userId, novelId));
        if(bookMark.isPresent() && !bookMark.get().isDeleted()){
            throw new IllegalArgumentException();
        }
    }

    public boolean checkUserAlreadyBookmark(Long userId, Long novelId) {
        return bookMarkRepository.findById(BookMarkId.create(userId, novelId)).isPresent();
    }
}
