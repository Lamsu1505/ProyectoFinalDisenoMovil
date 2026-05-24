package com.example.proyectofinaldisenomovil.di

import com.example.proyectofinaldisenomovil.data.repository.AttendanceRepository
import com.example.proyectofinaldisenomovil.data.repository.CommentRepository
import com.example.proyectofinaldisenomovil.data.repository.EventRepository
import com.example.proyectofinaldisenomovil.data.repository.ImageRepository
import com.example.proyectofinaldisenomovil.data.repository.NotificationRepository
import com.example.proyectofinaldisenomovil.data.repository.Remote.AttendanceRepositoryImpl
import com.example.proyectofinaldisenomovil.data.repository.Remote.FirebaseCommentRepository
import com.example.proyectofinaldisenomovil.data.repository.Remote.EventRepositoryImpl
import com.example.proyectofinaldisenomovil.data.repository.Remote.ImageRepositoryImpl
import com.example.proyectofinaldisenomovil.data.repository.Remote.FirebaseNotificationRepository
import com.example.proyectofinaldisenomovil.data.repository.Remote.UserRepositoryImpl
import com.example.proyectofinaldisenomovil.data.repository.Remote.VoteRepositoryImpl
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.data.repository.VoteRepository
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
        impl: EventRepositoryImpl
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindVoteRepository(
        impl: VoteRepositoryImpl
    ): VoteRepository

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(
        impl: AttendanceRepositoryImpl
    ): AttendanceRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(
        impl: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: FirebaseNotificationRepository
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        impl: FirebaseCommentRepository
    ): CommentRepository
}
