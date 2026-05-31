package com.springboot.web.category.application.command.update;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.domain.exception.CategoryNotFoundException;
import com.springboot.web.category.domain.port.CategoryRepository;
import com.springboot.web.common.application.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCategoryHandler implements RequestHandler<UpdateCategoryRequest, Void> {

    private final CategoryRepository categoryRepository;

    @Override
    public Void handle(UpdateCategoryRequest request) {
        if (!categoryRepository.existsById(request.getId())) {
            throw new CategoryNotFoundException(request.getId());
        }

        Category category = Category.builder()
                .id(request.getId())
                .name(request.getName())
                .build();

        categoryRepository.upsert(category);
        log.info("Category updated with id {}", request.getId());
        return null;
    }

    @Override
    public Class<UpdateCategoryRequest> getRequestType() {
        return UpdateCategoryRequest.class;
    }
}