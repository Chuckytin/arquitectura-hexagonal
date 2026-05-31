package com.springboot.web.category.application.query.getAll;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.domain.port.CategoryRepository;
import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.common.domain.PaginationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllCategoryHandler implements RequestHandler<GetAllCategoryRequest, GetAllCategoryResponse> {

    private final CategoryRepository categoryRepository;

    @Override
    public GetAllCategoryResponse handle(GetAllCategoryRequest request) {
        log.info("GetAllCategoryHandler start");

        PaginationResult<Category> categories = categoryRepository.findAll(request.getPaginationQuery());
        
        return new GetAllCategoryResponse(categories);
    }

    @Override
    public Class<GetAllCategoryRequest> getRequestType() {
        return GetAllCategoryRequest.class;
    }
}