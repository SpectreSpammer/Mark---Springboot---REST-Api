package com.onepieceofjava.SpringRestApiDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

}
