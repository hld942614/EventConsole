package com.project.uhdbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
