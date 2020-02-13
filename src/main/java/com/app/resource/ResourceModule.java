package com.app.resource;

import com.app.exceptions.mappers.CampSiteExceptionMapper;
import com.app.exceptions.mappers.UnknownExceptionMapper;
import com.app.resource.resources.CampSiteResource;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class ResourceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UnknownExceptionMapper.class);
        bind(CampSiteExceptionMapper.class);
        bind(CampSiteResource.class).in(Scopes.SINGLETON);
    }
}