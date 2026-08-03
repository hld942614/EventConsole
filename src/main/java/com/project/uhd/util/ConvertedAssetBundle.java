package com.project.uhd.util;

import java.util.List;

import com.project.uhd.entity.CmdbAsset;
import com.project.uhd.entity.CmdbAssetHardware;
import com.project.uhd.entity.CmdbAssetNetwork;
import com.project.uhd.entity.CmdbAssetOs;

/** CmdbAssetExcelConverter 轉換單一 Excel 列後的輸出，對應四張表各自要存的一份資料。 */
public class ConvertedAssetBundle {

	private final CmdbAsset asset;
	private final CmdbAssetHardware hardware;
	private final CmdbAssetOs os;
	private final List<CmdbAssetNetwork> networks;

	public ConvertedAssetBundle(CmdbAsset asset, CmdbAssetHardware hardware, CmdbAssetOs os,
			List<CmdbAssetNetwork> networks) {
		this.asset = asset;
		this.hardware = hardware;
		this.os = os;
		this.networks = networks;
	}

	public CmdbAsset getAsset() {
		return asset;
	}

	public CmdbAssetHardware getHardware() {
		return hardware;
	}

	public CmdbAssetOs getOs() {
		return os;
	}

	public List<CmdbAssetNetwork> getNetworks() {
		return networks;
	}
}
