package com.openbox.realcomm2.utilities.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.openbox.realcomm2.database.models.BoothModel;
import com.openbox.realcomm2.database.models.BoothViewModel;
import com.openbox.realcomm2.utilities.enums.BoothDataFragmentStatus;
import com.openbox.realcomm2.utilities.enums.BoothSortMode;
import com.openbox.realcomm2.utilities.enums.ProximityRegion;
import com.radiusnetworks.ibeacon.IBeacon;

public interface BoothDataInterface
{
	BoothDataFragmentStatus getDataFragmentStatus();
	
	void refreshData();

	BoothViewModel getBoothViewModel(int boothId);

	List<BoothViewModel> getBooothViewModels();

	List<BoothViewModel> getBoothViewModels(BoothSortMode boothSortMode);

	void updateBoothDistances(Collection<IBeacon> beaconList);
	
	void resetBoothDistances();

	List<Integer> getClosestBoothIds(int numberOfDisplayBooths);

	List<Integer> getRandomBoothIds(int numberOfDisplayBooths);
	
	Map<ProximityRegion, Integer> getBoothProximityCounts();
	
	// TODO: Will be removed
	BoothModel getBooth(int index);
}
