package com.app.service;

import com.app.exceptions.mappers.CampSiteExceptionMapper;
import com.app.service.services.CampSiteService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CampSiteExceptionMapper.class);
        bind(CampSiteService.class).in(Scopes.SINGLETON);
    }
}