package myproject.demo.Episode;

import myproject.demo.Episode.domain.Episode;
import myproject.demo.Episode.domain.EpisodeId;
import myproject.demo.Episode.domain.EpisodeRepository;
import myproject.demo.Episode.service.EpisodeService;
import myproject.demo.Novel.domain.NovelRepository;
import myproject.demo.Novel.service.NovelService;
import myproject.demo.User.service.UserDto;
import myproject.demo.User.service.UserService;
import myproject.demo.category.domain.CategoryRepository;
import myproject.demo.category_novel.domain.CategoryNovelRelationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DeleteTest {

    @Autowired
    CategoryNovelRelationRepository relationRepository;

    @Autowired
    NovelRepository novelRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EpisodeRepository episodeRepository;

    @Mock
    UserService userService;

    @DisplayName("Delete test 1. Normal Condition")
    @Test
    public void test1() {
        EpisodeService sut
                = new EpisodeService(
                new NovelService(userService, novelRepository),
                userService,
                episodeRepository
        );

        Long testNovelId = 1L; // author : 1L
        int testEpisodeNum = 1;
        UserDto userDto = new UserDto(1L, "user1");


        //요청한 유저
        when(userService.findLoggedUser()).thenReturn(userDto);
        // 소설 작가
        when(userService.findUserByUserId(any())).thenReturn(userDto);

        sut.delete(testNovelId, testEpisodeNum);


        Optional<Episode> sutObject
                = episodeRepository.findById(EpisodeId.create(testNovelId, testEpisodeNum));
        assertFalse(sutObject.isEmpty());
        assertFalse(sutObject.get().checkDeleted());

        sutObject.get().delete();

        assertTrue(sutObject.get().checkDeleted());

    }

    @DisplayName("Delete test 2. abnormal Condition: novel or episode doesn't exist or already deleted ")
    @Test
    public void test2() {
        EpisodeService sut
                = new EpisodeService(
                new NovelService(userService, novelRepository),
                userService,
                episodeRepository
        );

        Long testNovelId = 1L; // author : 1L
        int testEpisodeNum = 1;
        UserDto userDto = new UserDto(1L, "user1");

        //요청한 유저
        when(userService.findLoggedUser()).thenReturn(userDto);
        // 소설 작가
        when(userService.findUserByUserId(any())).thenReturn(userDto);

        Long nonExistNovelId = -1L;
        Long deletedNovelId = 3L;
        int nonExistEpisodeNum = -1;

        Long existNovelId = 1L;
        int deletedEpisodeNum = 2;


        // novel : x
        assertThrows(IllegalArgumentException.class, ()->sut.delete(nonExistNovelId, testEpisodeNum));

        // novel : deleted
        assertThrows(IllegalArgumentException.class, ()->sut.delete(deletedNovelId, testEpisodeNum));

        //episode: x
        assertThrows(IllegalArgumentException.class, ()->sut.delete(existNovelId, nonExistEpisodeNum));

        //episode: deleted
        assertThrows(IllegalArgumentException.class, ()->sut.delete(existNovelId, deletedEpisodeNum));

    }

}
