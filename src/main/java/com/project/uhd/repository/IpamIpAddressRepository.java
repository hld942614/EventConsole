package com.project.uhd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.IpamIpAddress;

public interface IpamIpAddressRepository extends JpaRepository<IpamIpAddress, Long> {
	Optional<IpamIpAddress> findByIpAddress(String ipAddress);

	Optional<IpamIpAddress> findByIpId(String ipId);
}
