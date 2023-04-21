package ru.practicum.ExploreWithMe.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.service.UserEventService;
import ru.practicum.ExploreWithMe.exception.DuplicateException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserEventService eventService, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CategoryDto add(CategoryDto categoryDto) {
        try {
            return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategoryWithoutId(categoryDto)));
        } catch (Exception e) {
            throw new DuplicateException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public CategoryDto update(long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NullPointerException("Category with id=" + id + " was not found."));
        category.setName(categoryDto.getName());
        try {
            return CategoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (Exception e) {
            throw new DuplicateException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public CategoryDto delete(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NullPointerException("Category with id=" + id + " was not found."));
        /*if (eventRepository.findAllByCategory(id) != null) {
            throw new WrongConditionException("The category is not empty");
        }*/
        categoryRepository.deleteById(id);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(() -> new NullPointerException("Category with id=" + id + " was not found.")));
    }
}
