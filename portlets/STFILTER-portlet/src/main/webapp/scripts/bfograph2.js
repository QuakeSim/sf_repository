var isFirst = true;

function Update(seriesx)
{
	if (isFirst) {
		document.forms.myStationCurrentParam['myStationCurrentParam:startDate'].value = bfgRound(bfgToDate2(seriesx),2);
		isFirst = false;
	} else {
		document.forms.myStationCurrentParam['myStationCurrentParam:endDate'].value = bfgRound(bfgToDate2(seriesx),2);
		isFirst = true;
	}
}

function bfgToDate2(date)
{
    if (date!=date) return null;
    curr = new Date((date+788940000)*1000);
    var y = curr.getFullYear();

    thisYear = new Date();
    nextYear = new Date();
    thisYear.setUTCFullYear(y,0,1);
	thisYear.setUTCHours(0,0,0,0);
    nextYear.setUTCFullYear((y+1),0,1);
	nextYear.setUTCHours(0,0,0,0);
	var result = y + (curr.valueOf() - thisYear.valueOf())/(nextYear.valueOf() - thisYear.valueOf());

    return result;
}

