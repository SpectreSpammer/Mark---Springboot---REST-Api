package com.onepieceofjava.SpringRestApiDemo.oldCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class OldAssetService {
	
	List<OldAsset> assets = new ArrayList<>();
	
	private Long assetId = 201L;
	
	//Get All
	public List<OldAsset> getAllAsset(){
		return assets;
	}
	
	//Get by Id
	public Optional<OldAsset> getAssetById(Long id){
		
		return assets.stream().filter(asset -> asset.getId().equals(id)).findFirst();
	}
	
	
	//Add Assets
	public OldAsset addAsset(OldAsset asset) {
		asset.setId(assetId++);
		assets.add(asset);
		
		return asset;
	}
	
	
	//Update Assets
	public OldAsset updatedAsset(Long id, OldAsset updatedAsset) {
		
		Optional<OldAsset> assetOptional = getAssetById(id);
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
