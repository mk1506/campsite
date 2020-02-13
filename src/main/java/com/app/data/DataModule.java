package com.app.data;

import com.app.data.repositories.ReservationsRepository;
import com.app.data.repositories.inMemoryRepository.InMemoryReservationsRepository;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class DataModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(InMemoryReservationsRepository.class).in(Scopes.SINGLETON);
        bind(ReservationsRepository.class).to(InMemoryReservationsRepository.class);
    }
}
