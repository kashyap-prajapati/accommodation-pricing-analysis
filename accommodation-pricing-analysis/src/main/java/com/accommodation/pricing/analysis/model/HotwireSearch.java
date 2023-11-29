package com.accommodation.pricing.analysis.model;

import java.util.List;

public class HotwireSearch {
	
	private String q;
	private String rid;
	private String rc;
	private List<HotwireSuggestion> sr;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getRc() {
		return rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	public List<HotwireSuggestion> getSr() {
		return sr;
	}

	public void setSr(List<HotwireSuggestion> sr) {
		this.sr = sr;
	}
	
	

	@Override
	public String toString() {
		return "HotwireSearch [q=" + q + ", rid=" + rid + ", rc=" + rc + ", sr=" + sr + "]";
	}



	public static class HotwireSuggestion{
		private String index;
		private String gaiaId;
		private String type;
		private RegionNames regionNames;
		
		public HotwireSuggestion() {
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getGaiaId() {
			return gaiaId;
		}

		public void setGaiaId(String gaiaId) {
			this.gaiaId = gaiaId;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public RegionNames getRegionNames() {
			return regionNames;
		}

		public void setRegionNames(RegionNames regionNames) {
			this.regionNames = regionNames;
		}

		@Override
		public String toString() {
			return "HotwireSuggestion [index=" + index + ", gaiaId=" + gaiaId + ", type=" + type + ", regionNames="
					+ regionNames + "]";
		}
		
		
		
	}
	
	public static class RegionNames{
		private String fullName;
		private String shortName;
		private String displayName;
		private String primaryDisplayName;
		private String secondaryDisplayName;
		private String lastSearchName;
	
		public RegionNames() {
		}

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getShortName() {
			return shortName;
		}

		public void setShortName(String shortName) {
			this.shortName = shortName;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getPrimaryDisplayName() {
			return primaryDisplayName;
		}

		public void setPrimaryDisplayName(String primaryDisplayName) {
			this.primaryDisplayName = primaryDisplayName;
		}

		public String getSecondaryDisplayName() {
			return secondaryDisplayName;
		}

		public void setSecondaryDisplayName(String secondaryDisplayName) {
			this.secondaryDisplayName = secondaryDisplayName;
		}

		public String getLastSearchName() {
			return lastSearchName;
		}

		public void setLastSearchName(String lastSearchName) {
			this.lastSearchName = lastSearchName;
		}

		@Override
		public String toString() {
			return "RegionNames [fullName=" + fullName + ", shortName=" + shortName + ", displayName=" + displayName
					+ ", primaryDisplayName=" + primaryDisplayName + ", secondaryDisplayName=" + secondaryDisplayName
					+ ", lastSearchName=" + lastSearchName + "]";
		}
		
		
	}
	
	
	
	

}
