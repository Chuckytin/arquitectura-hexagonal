package com.springboot.web.category.application.command.delete;

import com.springboot.web.category.domain.exception.CategoryNotFoundException;
import com.springboot.web.category.domain.port.CategoryRepository;
import com.springboot.web.common.application.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteCategoryHandler implements RequestHandler<DeleteCategoryRequest, Void> {

    private final CategoryRepository categoryRepository;

    @Override
    public Void handle(DeleteCategoryRequest request) {
        if (!categoryRepository.existsById(request.getId())) {
            throw new CategoryNotFoundException(request.getId());
        }
        categoryRepository.deleteById(request.getId());
        log.info("Category deleted with id {}", request.getId());
        return null;
    }

    @Override
    public Class<DeleteCategoryRequest> getRequestType() {
        return DeleteCategoryRequest.class;
    }
}