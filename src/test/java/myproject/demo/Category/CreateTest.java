package myproject.demo.Category;


import myproject.demo.category.domain.CategoryRepository;
import myproject.demo.category.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTest {

    @Mock
    CategoryRepository categoryRepository;


    @DisplayName("Create Test 1. Normal Condition")
    @Test
    public void test1(){
        CategoryService sut = new CategoryService(categoryRepository);

        List<String> names = Arrays.asList("one", "two", "three");

        sut.createAll(names);
        verify(categoryRepository, times(3 )).saveAndFlush(any());

    }

    @DisplayName("Create Test 2. Abnormal Condition : Duplicate Title")
    @Test
    public void test2(){
        CategoryService sut = new CategoryService(categoryRepository);

        List<String> names = Arrays.asList("one", "two", "three");

        when(categoryRepository.existsByCategoryName(any())).thenReturn(true);
        when(categoryRepository.existsByCategoryName(any())).thenReturn(true);
        when(categoryRepository.existsByCategoryName(any())).thenReturn(true);
        sut.createAll(names);
        verify(categoryRepository, times(0 )).saveAndFlush(any());

    }


}
