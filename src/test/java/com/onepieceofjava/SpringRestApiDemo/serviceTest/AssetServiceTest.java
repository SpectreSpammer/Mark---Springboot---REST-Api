package com.onepieceofjava.SpringRestApiDemo.serviceTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.onepieceofjava.SpringRestApiDemo.model.Asset;
import com.onepieceofjava.SpringRestApiDemo.repository.AssetRepository;
import com.onepieceofjava.SpringRestApiDemo.service.AssetService;

import jakarta.inject.Inject;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {
	
	
	@Mock
	private AssetRepository assetRepository;
	
	@InjectMocks
	private AssetService assetService;
	
	
	private Asset testAsset;
	private Asset updatedAsset;
	
	
	@BeforeEach
	void setUp() {
		testAsset = new Asset( 1L, "Dell Laptop", "Backend Developer Laptop","DLL12345");
		updatedAsset = new Asset( 1L, "Dell Laptop Updated", "Backend Developer Laptop - Updated","DLL123456789");
		
	}
	
	
	//Get All Asset
	   @Test
	   void getAllAsset_ShouldReturnAllAssets() {
	   // Arrange
	   List<Asset> expectedAssets = Arrays.asList(
	          testAsset,
	          new Asset( 2L, "Lenovo Laptop", "Frontend Developer Laptop","LNV98765")
	   );
	   when(assetRepository.findAll()).thenReturn(expectedAssets);

	   // Act
	    List<Asset> actualAssets = assetService.getAllAsset();

	    // Assert
	    assertEquals(expectedAssets.size(), actualAssets.size());
	    verify(assetRepository).findAll();
	    }
	
	//Get Asset by Id
	   @Test
	   void getAssetById_ShouldReturnAssetById_WhenAssetIsExist() {
		   
		   //Arrange
		   when(assetRepository.findById(1L)).thenReturn(Optional.of(testAsset));
		   
		   //Act
		   Optional <Asset> result = assetService.getAssetById(1L);
		   
		   //Assert
		   assertTrue(result.isPresent());
		   assertEquals(testAsset.getName(), result.get().getName());
		   
	   }
	   
	
	//Add
	@Test
	void addAsset_ShouldReturnSavedAsset() {
		//Arrange
		when(assetRepository.save(testAsset)).thenReturn(testAsset);
		
		//Act
		Asset savedAsset = assetService.addAsset(testAsset);
		
		//Assert
		assertNotNull(savedAsset);
		assertEquals(testAsset.getName(), savedAsset.getName());
		verify(assetRepository).save(testAsset);
	}
	
	
	//Update
	@Test
	void updateAssetById_ShouldReturnUpdatedAsset_WhenAssetIsExist() {
		
		//Arrange
		when(assetRepository.existsById(1L)).thenReturn(true);
		when(assetRepository.save(updatedAsset)).thenReturn(updatedAsset);
		
		//Act
		Asset result = assetService.updatedAsset(1L, updatedAsset);
		
		//Assert
		assertNotNull(result);
		assertEquals(updatedAsset.getName(), result.getName());
		verify(assetRepository).existsById(1L);
		verify(assetRepository).save(updatedAsset);
		
		
	}
	
	@Test
	void updateAssetById_ShouldReturnNull_WhenAssetDoesNotExist() {
		
		//Arrange
		when(assetRepository.existsById(1L)).thenReturn(false);
	
		
		//Act
		Asset result = assetService.updatedAsset(1L, updatedAsset);
		
		//Assert
		assertNull(result);
		verify(assetRepository).existsById(1L);
		verify(assetRepository,never()).save(updatedAsset);
		
		
	}
	
	//Delete
	@Test
	void deleteAsset_ShouldCallRepository() {
		
		//Arrange
		Long assetId = 1L;
		
		//Act
		assetService.deleteAssetById(assetId);
		
		//Assert
		verify(assetRepository).deleteById(assetId);
	}

}
