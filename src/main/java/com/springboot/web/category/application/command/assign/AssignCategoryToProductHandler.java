package com.springboot.web.category.application.command.assign;

import com.springboot.web.category.domain.exception.CategoryNotFoundException;
import com.springboot.web.category.domain.port.CategoryRepository;
import com.springboot.web.common.application.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignCategoryToProductHandler implements RequestHandler<AssignCategoryToProductRequest, Void> {

    private final CategoryRepository categoryRepository;

    @Override
    public Void handle(AssignCategoryToProductRequest request) {
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new CategoryNotFoundException(request.getCategoryId());
        }
        categoryRepository.assignCategoryToProduct(request.getCategoryId(), request.getProductId());
        log.info("Category {} assigned to product {}", request.getCategoryId(), request.getProductId());
        return null;
    }

    @Override
    public Class<AssignCategoryToProductRequest> getRequestType() {
        return AssignCategoryToProductRequest.class;
    }
}