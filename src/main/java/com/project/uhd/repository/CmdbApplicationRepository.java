package com.project.uhd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.CmdbApplication;

public interface CmdbApplicationRepository extends JpaRepository<CmdbApplication, Long> {
	Optional<CmdbApplication> findByApplicationCodeAndEnvironment(String applicationCode, String environment);

	Optional<CmdbApplication> findByApplicationId(String applicationId);
}
