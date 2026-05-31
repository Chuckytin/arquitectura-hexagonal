package com.springboot.web.category.application.query.getById;

import com.springboot.web.category.domain.exception.CategoryNotFoundException;
import com.springboot.web.category.domain.port.CategoryRepository;
import com.springboot.web.common.application.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCategoryByIdHandler implements RequestHandler<GetCategoryByIdRequest, GetCategoryByIdResponse> {

    private final CategoryRepository categoryRepository;

    @Override
    public GetCategoryByIdResponse handle(GetCategoryByIdRequest request) {
        log.info("GetCategoryByIdHandler start for id {}", request.getId());
        return categoryRepository.findById(request.getId())
                .map(GetCategoryByIdResponse::new)
                .orElseThrow(() -> new CategoryNotFoundException(request.getId()));
    }

    @Override
    public Class<GetCategoryByIdRequest> getRequestType() {
        return GetCategoryByIdRequest.class;
    }
}