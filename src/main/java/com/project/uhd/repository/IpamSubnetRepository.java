package com.project.uhd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhd.entity.IpamSubnet;

public interface IpamSubnetRepository extends JpaRepository<IpamSubnet, Long> {
}
