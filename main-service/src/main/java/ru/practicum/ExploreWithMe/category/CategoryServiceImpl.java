package ru.practicum.ExploreWithMe.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.category.dto.NewCategoryDto;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.exception.DuplicateException;
import ru.practicum.ExploreWithMe.exception.WrongConditionException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CategoryDto add(NewCategoryDto categoryDto) {
        Category cat = categoryRepository.findByName(categoryDto.getName());
        if (cat != null && !Objects.equals(cat.getName(), categoryDto.getName())) throw new DuplicateException("Not unique name");
        try {
            return CategoryMapper.toCategoryDto(categoryRepository.save(new Category(null, categoryDto.getName())));
        } catch (Exception e) {
            throw new DuplicateException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public CategoryDto update(long id, CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()) != null && categoryRepository.findByName(categoryDto.getName()).getId() != id)
            throw new DuplicateException("Not unique");
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
        List<Event> events = eventRepository.findAllByCategory(id);
        if (events != null && events.size() > 0) {
            throw new WrongConditionException("The category is not empty");
        }
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
