package com.springboot.web.common.application.mediator;

public interface RequestHandler<T extends Request<R>, R> {

    R handle(T request);

    Class<T> getRequestType();

}
