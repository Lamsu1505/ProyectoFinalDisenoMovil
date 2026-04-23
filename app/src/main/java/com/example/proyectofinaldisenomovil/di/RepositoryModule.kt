package com.example.proyectofinaldisenomovil.di

import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
import com.example.proyectofinaldisenomovil.data.repository.firebase.FirestoreUserRepository
import com.example.proyectofinaldisenomovil.data.repository.firebase.FirestoreAttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.firebase.FirestoreEventRepository
import com.example.proyectofinaldisenomovil.data.repository.firebase.FirestoreVoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindEventRepository(
        impl: FirestoreEventRepository
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: FirestoreUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindVoteRepository(
        impl: FirestoreVoteRepository
    ): VoteRepository

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(
        impl: FirestoreAttendanceRepository
    ): AttendanceRepository
}
