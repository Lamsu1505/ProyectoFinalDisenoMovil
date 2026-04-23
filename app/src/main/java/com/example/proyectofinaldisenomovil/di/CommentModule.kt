package com.example.proyectofinaldisenomovil.di

import com.example.proyectofinaldisenomovil.data.repository.CommentRepository
import com.example.proyectofinaldisenomovil.data.repository.firebase.FirestoreCommentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommentModule {
    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        impl: FirestoreCommentRepository
    ): CommentRepository
}

