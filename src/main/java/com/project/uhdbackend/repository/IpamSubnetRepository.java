package com.project.uhdbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.uhdbackend.entity.IpamSubnet;

public interface IpamSubnetRepository extends JpaRepository<IpamSubnet, Long> {
}
