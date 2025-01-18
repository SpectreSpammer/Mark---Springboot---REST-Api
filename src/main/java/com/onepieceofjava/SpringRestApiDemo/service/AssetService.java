package com.onepieceofjava.SpringRestApiDemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.repository.AssetRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetService {

	@Autowired
	private AssetRepository assetRepository;
	
	//getAll
	public List<Asset> getAllAsset(){
		return assetRepository.findAll();
	}
	
	//get by id
	public Optional<Asset> getAssetById(Long id){
		
		return assetRepository.findById(id);
	}
	
	
	//add 
	public Asset addAsset(Asset asset) {
		return assetRepository.save(asset);
	}
	
	 @Transactional
	 public Asset saveAsset(Asset asset) {
	    return assetRepository.save(asset);
	 }
	
	//update
	public Asset updatedAsset(Long id, Asset updatedAsset) {
		if(assetRepository.existsById(id)) {
			updatedAsset.setId(id);
			return  assetRepository.save(updatedAsset);
		}
		
		return null;
	}
	
	//delete
	public void deleteAssetById(Long id) {
		assetRepository.deleteById(id);
	}
}
