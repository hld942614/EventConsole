package com.project.uhd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
