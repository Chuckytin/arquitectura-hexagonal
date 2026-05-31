package com.springboot.web.category.application.command.create;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.domain.port.CategoryRepository;
import com.springboot.web.common.application.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCategoryHandler implements RequestHandler<CreateCategoryRequest, CreateCategoryResponse> {

    private final CategoryRepository categoryRepository;

    @Override
    public CreateCategoryResponse handle(CreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category stored = categoryRepository.upsert(category);
        log.info("Category created with id {}", stored.getId());
        return new CreateCategoryResponse(stored);
    }

    @Override
    public Class<CreateCategoryRequest> getRequestType() {
        return CreateCategoryRequest.class;
    }
}