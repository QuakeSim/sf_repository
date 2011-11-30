// utility functions for maniputlating dates	

	// get the date that is n days before thisDate
        function nDaysBefore(n, thisDate) {
                var before = new Date();
                before.setTime(thisDate.getTime() - n * 86400 * 1000);
                return before;
        }

        // get the string representation for a date
        function getDateString(d) {
                return d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate();
        }

        // get the UTC string representation for a date
        function getUTCDateString(d) {
                return d.getUTCFullYear() + "-" + (d.getUTCMonth()+1) + "-" + d.getUTCDate();
        }

		// get the string representation for a date and time
		function getDateTimeString(dt) {
			return dt.getFullYear() + "-" + (dt.getMonth()+1) + "-" + dt.getDate()
					+ "T" + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
		}

		// get date from a string like 2007-03-08
		function getDateFromString(str) {
			if (str.indexOf("-") >= 0) {
				var ret = new Date();
				setDateBySpecialString(ret, str);
				return ret;
			} else {
				return new Date(str);
			}
        }

		function setDateBySpecialString(theDate, str) {
			var year, month, day, i1, i2;
			i1 = str.indexOf("-");
			i2 = str.indexOf("-", i1+1);
			
			if (i1 < 0 || i2 < 0) {
				theDate.setDate("NaN");
				return;
			}
				
			year = str.substring(0, i1);
			month = str.substring(i1+1, i2);
			day = str.substring(i2+1);

			theDate.setFullYear(parseInt(year, 10));
			theDate.setMonth(parseInt(month, 10)-1);
			theDate.setDate(parseInt(day, 10));
			theDate.setHours(12);
			theDate.setMinutes(0);
			theDate.setSeconds(0);
			theDate.setMilliseconds(0);
        }
		
		function setDateByString(theDate, str) {
			if (str.indexOf("-") >= 0) {
				setDateBySpecialString(theDate, str);
			} else {
				var tmpDate = new Date(str);
				if (isNaN(tmpDate.getDate())) {
					theDate.setDate("NaN");
				} else {
					theDate.setTime(tmpDate.getTime());
				}
			}
		}

		// get date and time from a string like 2007-03-08T03:30:00
		function getDateTimeFromString(str) {
			var ret = new Date();
			setDateTimeByString(ret, str);
			return ret;
		}

		function setDateTimeByString(dt, str) {
			var idx = str.indexOf("T");
			if (idx < 0) {
				dt.setDate("NaN");
				return;
			}
			var strDate = str.substring(0, idx);
			var strTime = str.substring(idx+1);
			if (strDate.indexOf("-") >= 0) {
				setDateBySpecialString(dt, strDate);
				if ( isNaN(dt.getDate()) ) {
					return;
				}
			} else {
				var tmpDate = new Date(strDate);
				if ( isNaN(tmpDate.getDate()) ) {
					dt.setDate("NaN");
					return;
				}
				dt.setTime(tmpDate.getTime());
			}

			idx = strTime.indexOf(":");
			if (idx < 0) {
				dt.setDate("NaN");
				return;
			}
			var idx2 = strTime.indexOf(":", idx+1);
			dt.setHours( parseInt(strTime.substring(0, idx)) );
			dt.setMinutes( parseInt(strTime.substring(idx+1, idx2)) );
			dt.setSeconds( parseInt(strTime.substring(idx2+1)) );
		}

