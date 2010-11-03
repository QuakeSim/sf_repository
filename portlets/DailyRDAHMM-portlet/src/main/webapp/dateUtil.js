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

