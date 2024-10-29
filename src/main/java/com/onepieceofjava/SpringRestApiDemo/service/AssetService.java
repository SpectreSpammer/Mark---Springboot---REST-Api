package com.onepieceofjava.SpringRestApiDemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.model.Employee;

@Service
public class AssetService {
	
	List<Asset> assets = new ArrayList<>();
	
	private Long assetId = 201L;
	
	//Get All
	public List<Asset> getAllAsset(){
		return assets;
	}
	
	//Get by Id
	public Optional<Asset> getAssetById(Long id){
		
		return assets.stream().filter(asset -> asset.getId().equals(id)).findFirst();
	}
	
	
	//Add Assets
	public Asset addAsset(Asset asset) {
		asset.setId(assetId++);
		assets.add(asset);
		
		return asset;
	}
	
	
	//Update Assets
	public Asset updatedAsset(Long id, Asset updatedAsset) {
		
		Optional<Asset> assetOptional = getAssetById(id);
		if(assetOptional.isPresent()) {
			updatedAsset.setId(id);
			assets.set(assets.indexOf(assetOptional), updatedAsset);
			return updatedAsset;
		}
		
		return null;
	}
	
	//Delete Asset by id
	public void deleteAssetById(Long id) {
		assets.removeIf(asset -> asset.getId().equals(id));
	}

}
