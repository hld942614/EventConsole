package com.project.uhdbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.IpamIpAddress;

public interface IpamIpAddressRepository extends JpaRepository<IpamIpAddress, Long> {
	Optional<IpamIpAddress> findByIpAddress(String ipAddress);

	Optional<IpamIpAddress> findByIpId(String ipId);
}
