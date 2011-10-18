<h:inputHidden id="unavcoRedJsonStations" value="#{SimplexBean.selectedGPSJSONValues}"/>
<h:panelGrid id="unavcoMapncrap" 
				 rendered="#{SimplexBean.currentEditProjectForm.renderUnavcoGPSStationMap}">

  <f:verbatim>

	 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" 
				type="text/javascript"></script>


    <script src='@host.base.url@@artifactId@/cookies.js' type='text/javascript'></script> 
    <script src='@host.base.url@@artifactId@/dragzoom.js' type='text/javascript'></script> 
	 <script type='text/javascript'>  
		//<![CDATA[
		//These are needed for the search area.
		var searcharea;
		var marker_NE;
		var marker_SW;
		var border;
		var icon_NE;
		var icon_SW;
		var icon_move;

		var map;       
		var redStationsJson={};
		var greenIcon="http://labs.google.com/ridefinder/images/mm_20_green.png";
		var yellowIcon="http://labs.google.com/ridefinder/images/mm_20_yellow.png";
		var redIcon="http://labs.google.com/ridefinder/images/mm_20_red.png";
		var usecookies=0;       
		var center=new GLatLng(33.036, -117.24); 
		var zoomfactor=8;          
		var declutter=1;          
		var speedToDgr=0.0024;     
		var vectorpointcolor='#00FF00'; 
		var vectorstemcolor='#000000'; 
		var earthquakecolor  ='#000000'; 
		var earthquakecolor3 ='#9966cc'; 
		var volcanocolor2 ='#000000'; 
		var volcanocolor  ='#000000'; 
		var drawellipses=1;        
		var drawvertellipses=1;    
		var drawvectors=1;         
		var drawmarkers=1;         
		var drawverticals=1;       
		var drawplates=0;
		var drawvolcanoes=0;
		var draweqs=0;
		var latmax,lngmax,latmin,lngmin,deltalat,deltalng;    
		var vn,ve,vu,x1,y1,dx,dy,dcf, stddevN, stddevE,stddevU, corrNE,term1,term2,alpha;  
		var id=''; 
		var gpsStationMarker=new Array();
		var ids = new Array();    
		var sts = new Array();    
		var lats = new Array();    
		var lngs = new Array();    
		var ves  = new Array();    
		var vns  = new Array();    
		var vus  = new Array();    
		var stddevNs= new Array();    
		var stddevEs= new Array();    
		var stddevUs= new Array();    
		var corrNEs = new Array();    
		var iconColors=new Array();

		//These two objects store the markers of selected (yellow) and 
		//imported (red) stations. 
		var selectedStations={};
		var importedStations={};

		var staname; 
		var eq_lats = new Array();    
		var glatlngs = new Array();    
		var eq_lngs = new Array();    
		var eq_mags = new Array();    
		var eq_deps = new Array();    
		var vol_lats = new Array();    
		var vol_lngs = new Array();    
		var vol_locs = new Array();    
		var vol_names = new Array();    
		var vol_elevs = new Array();    
		var vol_types = new Array();    
		var vol_times = new Array();    
		var multiplate_verticesARRAY = new Array();  
		var f3CookieName ='GPSviewerstate';    
		var f3CookieValue ='';    
		var cookie ='';    
		var mapsizer;    
		var ellipseSizer = 1.000;    
		var generatedImages= 'http://facdev.unavco.org/data/gnss/dai-globals/lib/images/generated-icons';
		function submitallform()  { document.submitallform.submit(); } 
		function submitrestartform()  { document.submitrestartform.submit(); } 
		var load_plate_data_here = ''; 
		var term; 
		
		var pbpoints= new Array(); 
		ids[0]='AB01';    sts[0]='AtkaIslandAK2007';    lats[0]=52.2095049455;    lngs[0]=185.7952432959;    ves[0]=-7.76;    vns[0]=1.17;    vus[0]=2.36;    stddevNs[0]=1.2;    stddevEs[0]=0.94;    stddevUs[0]=1.18;    corrNEs[0]=0.001; 
   ids[1]='AB02';    sts[1]='Nikolski__AK2007';    lats[1]=52.9706058271;    lngs[1]=191.1453295852;    ves[1]=-3.91;    vns[1]=-2.12;    vus[1]=2.13;    stddevNs[1]=0.98;    stddevEs[1]=0.89;    stddevUs[1]=1.64;    corrNEs[1]=0.001; 
   ids[2]='AB04';    sts[2]='Savoonga__AK2007';    lats[2]=63.6568649874;    lngs[2]=189.4325568280;    ves[2]=0.63;    vns[2]=-2.99;    vus[2]=-1.17;    stddevNs[2]=0.76;    stddevEs[2]=0.49;    stddevUs[2]=1.15;    corrNEs[2]=0.003; 
   ids[3]='AB06';    sts[3]='FalsePass_AK2005';    lats[3]=54.8853227965;    lngs[3]=196.5765460345;    ves[3]=-7.2;    vns[3]=-0.88;    vus[3]=3.87;    stddevNs[3]=0.71;    stddevEs[3]=0.56;    stddevUs[3]=1.81;    corrNEs[3]=-0.012; 
   ids[4]='AB06';    sts[4]='FalsePass_AK2005';    lats[4]=54.8853227944;    lngs[4]=196.5765459613;    ves[4]=-7.2;    vns[4]=-0.88;    vus[4]=3.87;    stddevNs[4]=0.71;    stddevEs[4]=0.56;    stddevUs[4]=1.81;    corrNEs[4]=-0.012; 
   ids[5]='AB07';    sts[5]='SandPoint_AK2004';    lats[5]=55.3492766169;    lngs[5]=199.5232425751;    ves[5]=-8.5;    vns[5]=4.75;    vus[5]=-0.12;    stddevNs[5]=0.34;    stddevEs[5]=0.34;    stddevUs[5]=0.86;    corrNEs[5]=0.003; 
   ids[6]='AB08';    sts[6]='Mekoryuk__AK2008';    lats[6]=60.3848326825;    lngs[6]=193.7991431719;    ves[6]=-15.99;    vns[6]=9.55;    vus[6]=-1.91;    stddevNs[6]=2.49;    stddevEs[6]=2.3;    stddevUs[6]=4.75;    corrNEs[6]=0.005; 
   ids[7]='AB09';    sts[7]='Razorback_AK2007';    lats[7]=65.6149818611;    lngs[7]=191.9378748979;    ves[7]=1.94;    vns[7]=-3.56;    vus[7]=0.14;    stddevNs[7]=1.63;    stddevEs[7]=1.27;    stddevUs[7]=2.68;    corrNEs[7]=0.001; 
   ids[8]='AB11';    sts[8]='Nome_AnvilAK2006';    lats[8]=64.5644966174;    lngs[8]=194.6265415975;    ves[8]=1.74;    vns[8]=-3;    vus[8]=-0.58;    stddevNs[8]=1.01;    stddevEs[8]=0.85;    stddevUs[8]=1.24;    corrNEs[8]=0.001; 
   ids[9]='AB12';    sts[9]='Platinum__AK2007';    lats[9]=58.9507955991;    lngs[9]=198.2537385513;    ves[9]=-3.6;    vns[9]=-2.33;    vus[9]=1.03;    stddevNs[9]=1.82;    stddevEs[9]=1.94;    stddevUs[9]=1.71;    corrNEs[9]=0.000; 
   ids[10]='AB13';    sts[10]='ChignikLgnAK2006';    lats[10]=56.3073266928;    lngs[10]=201.4962060302;    ves[10]=-7.56;    vns[10]=5.51;    vus[10]=0.51;    stddevNs[10]=0.57;    stddevEs[10]=0.67;    stddevUs[10]=1.05;    corrNEs[10]=0.001; 
   ids[11]='AB14';    sts[11]='DillinghamAK2007';    lats[11]=59.1081667172;    lngs[11]=200.9084687372;    ves[11]=-1.7;    vns[11]=-1.71;    vus[11]=1.57;    stddevNs[11]=0.62;    stddevEs[11]=0.39;    stddevUs[11]=1.17;    corrNEs[11]=0.001; 
   ids[12]='AB15';    sts[12]='Nyac_Gold_AK2006';    lats[12]=61.0397532481;    lngs[12]=200.1216388977;    ves[12]=1.93;    vns[12]=-3.87;    vus[12]=0.55;    stddevNs[12]=0.48;    stddevEs[12]=1.31;    stddevUs[12]=1.22;    corrNEs[12]=0.001; 
   ids[13]='AB17';    sts[13]='UnalakleetAK2008';    lats[13]=63.8863600407;    lngs[13]=199.3052692636;    ves[13]=-11.09;    vns[13]=-4.55;    vus[13]=5.15;    stddevNs[13]=2.03;    stddevEs[13]=1.91;    stddevUs[13]=3.48;    corrNEs[13]=0.002; 
   ids[14]='AB18';    sts[14]='Kotzebue__AK2007';    lats[14]=66.8583638820;    lngs[14]=197.3864927194;    ves[14]=-0.86;    vns[14]=-3.87;    vus[14]=-1.37;    stddevNs[14]=2.82;    stddevEs[14]=3.65;    stddevUs[14]=1.15;    corrNEs[14]=0.000; 
   ids[15]='AB21';    sts[15]='AdakIAleutAK2006';    lats[15]=51.8641501642;    lngs[15]=183.3373524541;    ves[15]=-11.1;    vns[15]=8.59;    vus[15]=1.42;    stddevNs[15]=2.2;    stddevEs[15]=1.63;    stddevUs[15]=2.53;    corrNEs[15]=0.000; 
   ids[16]='AB21';    sts[16]='AdakIAleutAK2006';    lats[16]=51.8641501450;    lngs[16]=183.3373524685;    ves[16]=-11.1;    vns[16]=8.59;    vus[16]=1.42;    stddevNs[16]=2.2;    stddevEs[16]=1.63;    stddevUs[16]=2.53;    corrNEs[16]=0.000; 
   ids[17]='AB22';    sts[17]='IlliamnaHPAK2006';    lats[17]=59.8993206414;    lngs[17]=205.3017417603;    ves[17]=3.82;    vns[17]=-9;    vus[17]=2.92;    stddevNs[17]=0.33;    stddevEs[17]=0.65;    stddevUs[17]=0.85;    corrNEs[17]=0.002; 
   ids[18]='AB25';    sts[18]='Tatalina__AK2008';    lats[18]=62.9293110910;    lngs[18]=203.9766095254;    ves[18]=1.02;    vns[18]=-6.11;    vus[18]=2.76;    stddevNs[18]=3.03;    stddevEs[18]=2.22;    stddevUs[18]=2.7;    corrNEs[18]=-0.000; 
   ids[19]='AB27';    sts[19]='Kobuk_VallAK2007';    lats[19]=67.0558867894;    lngs[19]=203.0951110841;    ves[19]=0.85;    vns[19]=-3.32;    vus[19]=-0.37;    stddevNs[19]=0.55;    stddevEs[19]=0.83;    stddevUs[19]=1.27;    corrNEs[19]=0.001; 
   ids[20]='AB28';    sts[20]='Rainy_PassAK2006';    lats[20]=62.0938158822;    lngs[20]=207.1850828596;    ves[20]=0.29;    vns[20]=-9.05;    vus[20]=5.22;    stddevNs[20]=0.37;    stddevEs[20]=0.54;    stddevUs[20]=0.98;    corrNEs[20]=0.002; 
   ids[21]='AB33';    sts[21]='Coldfoot__AK2006';    lats[21]=67.2510130459;    lngs[21]=209.8274566208;    ves[21]=1.57;    vns[21]=-4.09;    vus[21]=-1.11;    stddevNs[21]=0.38;    stddevEs[21]=0.64;    stddevUs[21]=0.98;    corrNEs[21]=0.001; 
   ids[22]='AB35';    sts[22]='Yakataga__AK2007';    lats[22]=60.0790666351;    lngs[22]=217.6101286895;    ves[22]=-20.67;    vns[22]=30.41;    vus[22]=7.77;    stddevNs[22]=0.77;    stddevEs[22]=0.59;    stddevUs[22]=1.22;    corrNEs[22]=-0.000; 
   ids[23]='AB36';    sts[23]='Manley_HotAK2006';    lats[23]=65.0303992258;    lngs[23]=209.2560161896;    ves[23]=0.62;    vns[23]=-11.79;    vus[23]=2.94;    stddevNs[23]=1.24;    stddevEs[23]=1.72;    stddevUs[23]=1.4;    corrNEs[23]=-0.001; 
   ids[24]='AB37';    sts[24]='Paxson2___AK2004';    lats[24]=62.9673234507;    lngs[24]=214.5481218676;    ves[24]=-23.03;    vns[24]=13.37;    vus[24]=5.73;    stddevNs[24]=0.28;    stddevEs[24]=0.75;    stddevUs[24]=0.78;    corrNEs[24]=0.001; 
   ids[25]='AB39';    sts[25]='FortYukon_AK2008';    lats[25]=66.5593467392;    lngs[25]=214.7873700101;    ves[25]=1.51;    vns[25]=-4.44;    vus[25]=-4.27;    stddevNs[25]=1.72;    stddevEs[25]=2.48;    stddevUs[25]=2.73;    corrNEs[25]=-0.000; 
   ids[26]='AB41';    sts[26]='EagleAirptAK2006';    lats[26]=64.7773263199;    lngs[26]=218.8419339625;    ves[26]=0.81;    vns[26]=-1.33;    vus[26]=-2.47;    stddevNs[26]=0.47;    stddevEs[26]=0.57;    stddevUs[26]=0.89;    corrNEs[26]=0.000; 
   ids[27]='AB42';    sts[27]='Akwe_Peak_AK2006';    lats[27]=59.3403761463;    lngs[27]=221.1011881519;    ves[27]=-22.78;    vns[27]=34.75;    vus[27]=17.44;    stddevNs[27]=0.74;    stddevEs[27]=0.81;    stddevUs[27]=2.08;    corrNEs[27]=-0.001; 
   ids[28]='AB42';    sts[28]='Akwe_Peak_AK2006';    lats[28]=59.3403761338;    lngs[28]=221.1011882116;    ves[28]=-22.78;    vns[28]=34.75;    vus[28]=17.44;    stddevNs[28]=0.74;    stddevEs[28]=0.81;    stddevUs[28]=2.08;    corrNEs[28]=-0.001; 
   ids[29]='AB43';    sts[29]='CapeSpenceAK2007';    lats[29]=58.1988425986;    lngs[29]=223.3591923866;    ves[29]=-7.11;    vns[29]=11.38;    vus[29]=16.98;    stddevNs[29]=0.34;    stddevEs[29]=0.58;    stddevUs[29]=1.15;    corrNEs[29]=0.002; 
   ids[30]='AB44';    sts[30]='SkagwayUSCAK2005';    lats[30]=59.5280392127;    lngs[30]=224.7717026192;    ves[30]=1.02;    vns[30]=6.07;    vus[30]=15.87;    stddevNs[30]=0.25;    stddevEs[30]=0.39;    stddevUs[30]=1.04;    corrNEs[30]=-0.006; 
   ids[31]='AB45';    sts[31]='SagRivrDOTAK2007';    lats[31]=68.7604953319;    lngs[31]=211.1288489979;    ves[31]=-0.39;    vns[31]=-2.51;    vus[31]=-0.87;    stddevNs[31]=0.71;    stddevEs[31]=0.87;    stddevUs[31]=1.37;    corrNEs[31]=0.000; 
   ids[32]='AB46';    sts[32]='ArcticVilgAK2007';    lats[32]=68.1206721927;    lngs[32]=214.4320994989;    ves[32]=1.93;    vns[32]=-2.78;    vus[32]=-4.15;    stddevNs[32]=1.27;    stddevEs[32]=0.68;    stddevUs[32]=1.79;    corrNEs[32]=0.001; 
   ids[33]='AB48';    sts[33]='PortAlexanAK2005';    lats[33]=56.2450630930;    lngs[33]=225.3530039740;    ves[33]=-2.32;    vns[33]=7.77;    vus[33]=0.98;    stddevNs[33]=0.23;    stddevEs[33]=0.35;    stddevUs[33]=0.83;    corrNEs[33]=0.002; 
   ids[34]='AB49';    sts[34]='KlawockAirAK2006';    lats[34]=55.5801020701;    lngs[34]=226.9315134561;    ves[34]=-2.57;    vns[34]=3.82;    vus[34]=4.17;    stddevNs[34]=0.23;    stddevEs[34]=0.37;    stddevUs[34]=0.96;    corrNEs[34]=-0.001; 
   ids[35]='AB50';    sts[35]='MendenhallAK2005';    lats[35]=58.4167758623;    lngs[35]=225.4546995925;    ves[35]=-0.88;    vns[35]=2.46;    vus[35]=15.14;    stddevNs[35]=0.27;    stddevEs[35]=0.34;    stddevUs[35]=0.93;    corrNEs[35]=0.001; 
   ids[36]='AB51';    sts[36]='PetersburgAK2005';    lats[36]=56.7976235124;    lngs[36]=227.0864397972;    ves[36]=-4.46;    vns[36]=3.04;    vus[36]=9.09;    stddevNs[36]=0.22;    stddevEs[36]=0.35;    stddevUs[36]=0.82;    corrNEs[36]=-0.001; 
   ids[37]='AC02';    sts[37]='AkhiokCorpAK2005';    lats[37]=56.9505865355;    lngs[37]=205.8169551171;    ves[37]=-14.21;    vns[37]=17.34;    vus[37]=0.14;    stddevNs[37]=0.44;    stddevEs[37]=0.52;    stddevUs[37]=0.78;    corrNEs[37]=0.001; 
   ids[38]='AC03';    sts[38]='AnchorPnt_AK2007';    lats[38]=59.7706384756;    lngs[38]=208.1354669935;    ves[38]=-2.29;    vns[38]=-7.01;    vus[38]=15.91;    stddevNs[38]=0.48;    stddevEs[38]=0.68;    stddevUs[38]=1.43;    corrNEs[38]=0.002; 
   ids[39]='AC06';    sts[39]='BradleyLakAK2005';    lats[39]=59.7636379016;    lngs[39]=209.1094417931;    ves[39]=-4.74;    vns[39]=2.39;    vus[39]=7.67;    stddevNs[39]=0.47;    stddevEs[39]=0.5;    stddevUs[39]=1.07;    corrNEs[39]=0.001; 
   ids[40]='AC07';    sts[40]='Buckland__AK2007';    lats[40]=65.9612936220;    lngs[40]=198.7133843319;    ves[40]=1.21;    vns[40]=-3.21;    vus[40]=0.15;    stddevNs[40]=0.54;    stddevEs[40]=0.88;    stddevUs[40]=1.34;    corrNEs[40]=0.002; 
   ids[41]='AC08';    sts[41]='CapDouglasAK2007';    lats[41]=58.9287755737;    lngs[41]=206.3553004515;    ves[41]=-1.62;    vns[41]=-3.49;    vus[41]=3.25;    stddevNs[41]=1.16;    stddevEs[41]=1.24;    stddevUs[41]=2.09;    corrNEs[41]=-0.005; 
   ids[42]='AC08';    sts[42]='CapDouglasAK2007';    lats[42]=58.9287756200;    lngs[42]=206.3553003817;    ves[42]=-1.62;    vns[42]=-3.49;    vus[42]=3.25;    stddevNs[42]=1.16;    stddevEs[42]=1.24;    stddevUs[42]=2.09;    corrNEs[42]=-0.005; 
   ids[43]='AC08';    sts[43]='CapDouglasAK2007';    lats[43]=58.9287755644;    lngs[43]=206.3553003430;    ves[43]=-1.62;    vns[43]=-3.49;    vus[43]=3.25;    stddevNs[43]=1.16;    stddevEs[43]=1.24;    stddevUs[43]=2.09;    corrNEs[43]=-0.005; 
   ids[44]='AC09';    sts[44]='KayakIsl__AK2007';    lats[44]=59.8684788204;    lngs[44]=215.4761531043;    ves[44]=-17.29;    vns[44]=33.47;    vus[44]=2.24;    stddevNs[44]=0.56;    stddevEs[44]=0.63;    stddevUs[44]=1.28;    corrNEs[44]=-0.000; 
   ids[45]='AC10';    sts[45]='CpSarichefAK2008';    lats[45]=54.5225821767;    lngs[45]=195.1132717470;    ves[45]=-7.96;    vns[45]=-0.03;    vus[45]=1.89;    stddevNs[45]=2.36;    stddevEs[45]=3.12;    stddevUs[45]=3.16;    corrNEs[45]=-0.000; 
   ids[46]='AC10';    sts[46]='CpSarichefAK2008';    lats[46]=54.5225822267;    lngs[46]=195.1132717122;    ves[46]=-7.96;    vns[46]=-0.03;    vus[46]=1.89;    stddevNs[46]=2.36;    stddevEs[46]=3.12;    stddevUs[46]=3.16;    corrNEs[46]=-0.000; 
   ids[47]='AC11';    sts[47]='ChickaloonAK2005';    lats[47]=61.8070792684;    lngs[47]=211.6682516306;    ves[47]=-14.05;    vns[47]=10.74;    vus[47]=7.03;    stddevNs[47]=0.27;    stddevEs[47]=0.35;    stddevUs[47]=1.09;    corrNEs[47]=0.002; 
   ids[48]='AC12';    sts[48]='ChernaburaAK2008';    lats[48]=54.8309638154;    lngs[48]=200.4104384174;    ves[48]=-10.65;    vns[48]=9.47;    vus[48]=-5.32;    stddevNs[48]=38.88;    stddevEs[48]=52.57;    stddevUs[48]=9.14;    corrNEs[48]=-0.000; 
   ids[49]='AC13';    sts[49]='ChirikofIsAK2008';    lats[49]=55.8218972141;    lngs[49]=204.3776001282;    ves[49]=-19.14;    vns[49]=32.41;    vus[49]=-8.58;    stddevNs[49]=12.18;    stddevEs[49]=11.61;    stddevUs[49]=4.41;    corrNEs[49]=-0.000; 
   ids[50]='AC14';    sts[50]='EstherIsleAK2007';    lats[50]=60.8487016955;    lngs[50]=212.0004197411;    ves[50]=-17.97;    vns[50]=36.9;    vus[50]=-0.67;    stddevNs[50]=0.45;    stddevEs[50]=1.6;    stddevUs[50]=1.85;    corrNEs[50]=0.000; 
   ids[51]='AC15';    sts[51]='CooperLndgAK2005';    lats[51]=60.4813334323;    lngs[51]=210.2759935639;    ves[51]=-10.77;    vns[51]=16.36;    vus[51]=7.88;    stddevNs[51]=0.32;    stddevEs[51]=0.46;    stddevUs[51]=1.04;    corrNEs[51]=0.000; 
   ids[52]='AC16';    sts[52]='DeepWater_AK2007';    lats[52]=60.5182051987;    lngs[52]=211.9067658058;    ves[52]=-18.99;    vns[52]=43.53;    vus[52]=-0.5;    stddevNs[52]=1.87;    stddevEs[52]=3.39;    stddevUs[52]=3.48;    corrNEs[52]=-0.000; 
   ids[53]='AC17';    sts[53]='DriftRiverAK2006';    lats[53]=60.6639031551;    lngs[53]=207.5961543259;    ves[53]=1.73;    vns[53]=-13.22;    vus[53]=8.13;    stddevNs[53]=0.54;    stddevEs[53]=0.45;    stddevUs[53]=0.85;    corrNEs[53]=0.002; 
   ids[54]='AC18';    sts[54]='Ushagat_IsAK2008';    lats[54]=58.9259588098;    lngs[54]=207.7504692902;    ves[54]=0.67;    vns[54]=-4.94;    vus[54]=10.76;    stddevNs[54]=1.15;    stddevEs[54]=1.21;    stddevUs[54]=2.05;    corrNEs[54]=-0.000; 
   ids[55]='AC19';    sts[55]='FarewellMTAK2008';    lats[55]=62.5192143341;    lngs[55]=206.3926677109;    ves[55]=-0.19;    vns[55]=-5.01;    vus[55]=5.78;    stddevNs[55]=0.94;    stddevEs[55]=1.24;    stddevUs[55]=1.96;    corrNEs[55]=0.001; 
   ids[56]='AC20';    sts[56]='Girdwood__AK2005';    lats[56]=60.9292081637;    lngs[56]=210.6474966924;    ves[56]=-11.9;    vns[56]=14.68;    vus[56]=3.96;    stddevNs[56]=0.49;    stddevEs[56]=0.29;    stddevUs[56]=1.09;    corrNEs[56]=0.002; 
   ids[57]='AC21';    sts[57]='PerryvilleAK2006';    lats[57]=55.9210905693;    lngs[57]=200.8722778229;    ves[57]=-9.7;    vns[57]=6.37;    vus[57]=-0.39;    stddevNs[57]=0.48;    stddevEs[57]=0.62;    stddevUs[57]=1;    corrNEs[57]=0.002; 
   ids[58]='AC23';    sts[58]='Soldotna__AK2007';    lats[58]=60.4750934702;    lngs[58]=209.1220465423;    ves[58]=-3.19;    vns[58]=-4.44;    vus[58]=12.13;    stddevNs[58]=0.68;    stddevEs[58]=0.73;    stddevUs[58]=1.25;    corrNEs[58]=0.001; 
   ids[59]='AC24';    sts[59]='KingsalmonAK2006';    lats[59]=58.6815731370;    lngs[59]=203.3472453272;    ves[59]=-0.9;    vns[59]=-2.11;    vus[59]=2.38;    stddevNs[59]=0.48;    stddevEs[59]=0.65;    stddevUs[59]=0.95;    corrNEs[59]=0.002; 
   ids[60]='AC25';    sts[60]='King_Cove_AK2005';    lats[60]=55.0889686620;    lngs[60]=197.6859478718;    ves[60]=-6.59;    vns[60]=-1.11;    vus[60]=1.42;    stddevNs[60]=0.67;    stddevEs[60]=0.65;    stddevUs[60]=1.06;    corrNEs[60]=0.001; 
   ids[61]='AC26';    sts[61]='Cape_Gull_AK2008';    lats[61]=58.2145559366;    lngs[61]=205.8497086218;    ves[61]=1.75;    vns[61]=-3.55;    vus[61]=3.27;    stddevNs[61]=0.87;    stddevEs[61]=0.74;    stddevUs[61]=1.71;    corrNEs[61]=0.001; 
   ids[62]='AC27';    sts[62]='AC27MNEIL_AK2004';    lats[62]=59.2525072622;    lngs[62]=205.8371197265;    ves[62]=1.52;    vns[62]=-9.16;    vus[62]=2.31;    stddevNs[62]=0.44;    stddevEs[62]=0.39;    stddevUs[62]=0.86;    corrNEs[62]=0.002; 
   ids[63]='AC27';    sts[63]='AC27MNEIL_AK2004';    lats[63]=59.2525072642;    lngs[63]=205.8371197186;    ves[63]=1.52;    vns[63]=-9.16;    vus[63]=2.31;    stddevNs[63]=0.44;    stddevEs[63]=0.39;    stddevUs[63]=0.86;    corrNEs[63]=0.002; 
   ids[64]='AC27';    sts[64]='AC27MNEIL_AK2004';    lats[64]=59.2525072759;    lngs[64]=205.8371197192;    ves[64]=1.52;    vns[64]=-9.16;    vus[64]=2.31;    stddevNs[64]=0.44;    stddevEs[64]=0.39;    stddevUs[64]=0.86;    corrNEs[64]=0.002; 
   ids[65]='AC28';    sts[65]='NagaiIslndAK2008';    lats[65]=55.0784915207;    lngs[65]=199.9508397038;    ves[65]=-10.62;    vns[65]=6.42;    vus[65]=-1.87;    stddevNs[65]=0.82;    stddevEs[65]=1.29;    stddevUs[65]=1.58;    corrNEs[65]=0.001; 
   ids[66]='AC30';    sts[66]='MontaguIslAK2007';    lats[66]=59.8558202608;    lngs[66]=212.2607682632;    ves[66]=18.05;    vns[66]=115.03;    vus[66]=217.36;    stddevNs[66]=9.21;    stddevEs[66]=8.78;    stddevUs[66]=6.2;    corrNEs[66]=-0.001; 
   ids[67]='AC31';    sts[67]='Bald_Head_AK2006';    lats[67]=64.6379766361;    lngs[67]=197.7608741228;    ves[67]=1.33;    vns[67]=-3.46;    vus[67]=1.72;    stddevNs[67]=0.41;    stddevEs[67]=0.47;    stddevUs[67]=0.96;    corrNEs[67]=0.003; 
   ids[68]='AC32';    sts[68]='Mt_SusitnaAK2006';    lats[68]=61.4731214298;    lngs[68]=209.2630831423;    ves[68]=-6.25;    vns[68]=3;    vus[68]=2.15;    stddevNs[68]=6.17;    stddevEs[68]=6.47;    stddevUs[68]=17.39;    corrNEs[68]=-0.001; 
   ids[69]='AC32';    sts[69]='Mt_SusitnaAK2006';    lats[69]=61.4731214049;    lngs[69]=209.2630831295;    ves[69]=-6.25;    vns[69]=3;    vus[69]=2.15;    stddevNs[69]=6.17;    stddevEs[69]=6.47;    stddevUs[69]=17.39;    corrNEs[69]=-0.001; 
   ids[70]='AC33';    sts[70]='TokoDenaliAK2007';    lats[70]=62.6711785633;    lngs[70]=209.3149272324;    ves[70]=-2.69;    vns[70]=30.25;    vus[70]=-23.74;    stddevNs[70]=21.5;    stddevEs[70]=4.56;    stddevUs[70]=5.18;    corrNEs[70]=-0.000; 
   ids[71]='AC34';    sts[71]='OldHarbor_AK2006';    lats[71]=57.2200272632;    lngs[71]=206.7208224434;    ves[71]=-13.78;    vns[71]=15.61;    vus[71]=3.09;    stddevNs[71]=0.56;    stddevEs[71]=0.98;    stddevUs[71]=0.93;    corrNEs[71]=0.001; 
   ids[72]='AC35';    sts[72]='PetrofLakeAK2006';    lats[72]=59.3758117743;    lngs[72]=209.2067549149;    ves[72]=-9.17;    vns[72]=9.57;    vus[72]=6.14;    stddevNs[72]=0.35;    stddevEs[72]=0.51;    stddevUs[72]=1.17;    corrNEs[72]=0.001; 
   ids[73]='AC35';    sts[73]='PetrofLakeAK2006';    lats[73]=59.3758117773;    lngs[73]=209.2067548704;    ves[73]=-9.17;    vns[73]=9.57;    vus[73]=6.14;    stddevNs[73]=0.35;    stddevEs[73]=0.51;    stddevUs[73]=1.17;    corrNEs[73]=0.001; 
   ids[74]='AC36';    sts[74]='MoosePointAK2008';    lats[74]=60.9553178301;    lngs[74]=209.3916523459;    ves[74]=-2.96;    vns[74]=-5.97;    vus[74]=8.17;    stddevNs[74]=0.71;    stddevEs[74]=1.04;    stddevUs[74]=1.92;    corrNEs[74]=0.000; 
   ids[75]='AC37';    sts[75]='LakeClark_AK2007';    lats[75]=60.4396879793;    lngs[75]=206.1346171311;    ves[75]=7.6;    vns[75]=-8.46;    vus[75]=-2.8;    stddevNs[75]=4.24;    stddevEs[75]=4.7;    stddevUs[75]=4.27;    corrNEs[75]=-0.000; 
   ids[76]='AC38';    sts[76]='Quartz_CrkAK2005';    lats[76]=57.7536855790;    lngs[76]=206.6581280695;    ves[76]=-5.99;    vns[76]=3.89;    vus[76]=10.21;    stddevNs[76]=0.51;    stddevEs[76]=0.6;    stddevUs[76]=1.12;    corrNEs[76]=0.001; 
   ids[77]='AC39';    sts[77]='ShuyakIsSPAK2006';    lats[77]=58.6097199797;    lngs[77]=207.6059273054;    ves[77]=0.69;    vns[77]=-5.12;    vus[77]=9.61;    stddevNs[77]=0.31;    stddevEs[77]=0.61;    stddevUs[77]=0.96;    corrNEs[77]=0.002; 
   ids[78]='AC40';    sts[78]='PortHeidenAK2007';    lats[78]=56.9303531996;    lngs[78]=201.3814294815;    ves[78]=-8.24;    vns[78]=2.44;    vus[78]=5.68;    stddevNs[78]=0.62;    stddevEs[78]=0.79;    stddevUs[78]=1.27;    corrNEs[78]=0.000; 
   ids[79]='AC41';    sts[79]='PortMollerAK2006';    lats[79]=55.9086693363;    lngs[79]=199.5926954116;    ves[79]=-7.62;    vns[79]=3.07;    vus[79]=0.89;    stddevNs[79]=0.62;    stddevEs[79]=0.61;    stddevUs[79]=1.04;    corrNEs[79]=0.001; 
   ids[80]='AC42';    sts[80]='SanakIslndAK2007';    lats[80]=54.4717770556;    lngs[80]=197.2163484973;    ves[80]=-6.35;    vns[80]=-2.23;    vus[80]=5.19;    stddevNs[80]=1.62;    stddevEs[80]=1.72;    stddevUs[80]=2.17;    corrNEs[80]=-0.001; 
   ids[81]='AC42';    sts[81]='SanakIslndAK2007';    lats[81]=54.4717769916;    lngs[81]=197.2163485889;    ves[81]=-6.35;    vns[81]=-2.23;    vus[81]=5.19;    stddevNs[81]=1.62;    stddevEs[81]=1.72;    stddevUs[81]=2.17;    corrNEs[81]=-0.001; 
   ids[82]='AC43';    sts[82]='Seal_RocksAK2007';    lats[82]=59.5212800199;    lngs[82]=210.3712646692;    ves[82]=-12.97;    vns[82]=34.86;    vus[82]=2.07;    stddevNs[82]=0.44;    stddevEs[82]=0.73;    stddevUs[82]=1.19;    corrNEs[82]=0.000; 
   ids[83]='AC44';    sts[83]='ArcticVly_AK2008';    lats[83]=61.2421735579;    lngs[83]=210.4328754683;    ves[83]=-12.83;    vns[83]=8.83;    vus[83]=15.25;    stddevNs[83]=1.85;    stddevEs[83]=1.67;    stddevUs[83]=3.02;    corrNEs[83]=-0.001; 
   ids[84]='AC45';    sts[84]='SitkinakIsAK2006';    lats[84]=56.5644541693;    lngs[84]=205.8190387984;    ves[84]=-15.69;    vns[84]=26.97;    vus[84]=-4.58;    stddevNs[84]=0.43;    stddevEs[84]=0.69;    stddevUs[84]=1.04;    corrNEs[84]=0.001; 
   ids[85]='AC46';    sts[85]='Skwentna_RAK2006';    lats[85]=61.9862696643;    lngs[85]=208.4759989791;    ves[85]=-1.76;    vns[85]=-8.57;    vus[85]=5.21;    stddevNs[85]=0.3;    stddevEs[85]=0.59;    stddevUs[85]=0.86;    corrNEs[85]=0.002; 
   ids[86]='AC47';    sts[86]='SlopeMtn__AK2007';    lats[86]=60.0814517436;    lngs[86]=207.3760556464;    ves[86]=5.11;    vns[86]=-10.06;    vus[86]=27.09;    stddevNs[86]=3.13;    stddevEs[86]=4.06;    stddevUs[86]=6.46;    corrNEs[86]=-0.007; 
   ids[87]='AC47';    sts[87]='SlopeMtn__AK2007';    lats[87]=60.0814516739;    lngs[87]=207.3760557591;    ves[87]=5.11;    vns[87]=-10.06;    vus[87]=27.09;    stddevNs[87]=3.13;    stddevEs[87]=4.06;    stddevUs[87]=6.46;    corrNEs[87]=-0.007; 
   ids[88]='AC48';    sts[88]='NakedIsl__AK2007';    lats[88]=60.6458627323;    lngs[88]=212.6569837751;    ves[88]=-19.08;    vns[88]=41.38;    vus[88]=-4.48;    stddevNs[88]=0.52;    stddevEs[88]=0.61;    stddevUs[88]=1.72;    corrNEs[88]=-0.003; 
   ids[89]='AC48';    sts[89]='NakedIsl__AK2007';    lats[89]=60.6458627254;    lngs[89]=212.6569837557;    ves[89]=-19.08;    vns[89]=41.38;    vus[89]=-4.48;    stddevNs[89]=0.52;    stddevEs[89]=0.61;    stddevUs[89]=1.72;    corrNEs[89]=-0.003; 
   ids[90]='AC48';    sts[90]='NakedIsl__AK2007';    lats[90]=60.6458626789;    lngs[90]=212.6569837483;    ves[90]=-19.08;    vns[90]=41.38;    vus[90]=-4.48;    stddevNs[90]=0.52;    stddevEs[90]=0.61;    stddevUs[90]=1.72;    corrNEs[90]=-0.003; 
   ids[91]='AC50';    sts[91]='BaldyMtn__AK2007';    lats[91]=65.5538497279;    lngs[91]=195.4334306426;    ves[91]=2.37;    vns[91]=-3;    vus[91]=-3.64;    stddevNs[91]=1.31;    stddevEs[91]=1.01;    stddevUs[91]=3;    corrNEs[91]=-0.007; 
   ids[92]='AC50';    sts[92]='BaldyMtn__AK2007';    lats[92]=65.5538497244;    lngs[92]=195.4334306974;    ves[92]=2.37;    vns[92]=-3;    vus[92]=-3.64;    stddevNs[92]=1.31;    stddevEs[92]=1.01;    stddevUs[92]=3;    corrNEs[92]=-0.007; 
   ids[93]='AC50';    sts[93]='BaldyMtn__AK2007';    lats[93]=65.5538497308;    lngs[93]=195.4334306502;    ves[93]=2.37;    vns[93]=-3;    vus[93]=-3.64;    stddevNs[93]=1.31;    stddevEs[93]=1.01;    stddevUs[93]=3;    corrNEs[93]=-0.007; 
   ids[94]='AC51';    sts[94]='StrandlineAK2007';    lats[94]=61.4980837470;    lngs[94]=208.1646524631;    ves[94]=2.35;    vns[94]=-9.82;    vus[94]=5.55;    stddevNs[94]=1.22;    stddevEs[94]=2.31;    stddevUs[94]=2.2;    corrNEs[94]=-0.001; 
   ids[95]='AC51';    sts[95]='StrandlineAK2007';    lats[95]=61.4980837485;    lngs[95]=208.1646524785;    ves[95]=2.35;    vns[95]=-9.82;    vus[95]=5.55;    stddevNs[95]=1.22;    stddevEs[95]=2.31;    stddevUs[95]=2.2;    corrNEs[95]=-0.001; 
   ids[96]='AC52';    sts[96]='PilotPointAK2007';    lats[96]=57.5672505184;    lngs[96]=202.4257838283;    ves[96]=-8.18;    vns[96]=7.24;    vus[96]=2.2;    stddevNs[96]=0.62;    stddevEs[96]=0.92;    stddevUs[96]=1.19;    corrNEs[96]=0.001; 
   ids[97]='AC53';    sts[97]='Willow_CrkAK2006';    lats[97]=61.7689732796;    lngs[97]=209.9310478986;    ves[97]=-5.65;    vns[97]=-0.91;    vus[97]=4.46;    stddevNs[97]=0.32;    stddevEs[97]=0.63;    stddevUs[97]=1.32;    corrNEs[97]=0.002; 
   ids[98]='AC55';    sts[98]='Yentna_RvrAK2006';    lats[98]=62.3844447740;    lngs[98]=208.2354131141;    ves[98]=60.66;    vns[98]=7.04;    vus[98]=-25.81;    stddevNs[98]=0.43;    stddevEs[98]=1.35;    stddevUs[98]=1;    corrNEs[98]=0.001; 
   ids[99]='AC57';    sts[99]='ThompsonPaAK2006';    lats[99]=61.1385974296;    lngs[99]=214.2572967951;    ves[99]=-13.87;    vns[99]=28.55;    vus[99]=10.45;    stddevNs[99]=0.29;    stddevEs[99]=0.54;    stddevUs[99]=1.17;    corrNEs[99]=0.000; 
   ids[100]='AC58';    sts[100]='StPaulIsldAK2008';    lats[100]=57.1560889853;    lngs[100]=189.7821800676;    ves[100]=-2.61;    vns[100]=-2.18;    vus[100]=-2.01;    stddevNs[100]=2.29;    stddevEs[100]=4.19;    stddevUs[100]=4.68;    corrNEs[100]=-0.003; 
   ids[101]='AC58';    sts[101]='StPaulIsldAK2008';    lats[101]=57.1560890145;    lngs[101]=189.7821800838;    ves[101]=-2.61;    vns[101]=-2.18;    vus[101]=-2.01;    stddevNs[101]=2.29;    stddevEs[101]=4.19;    stddevUs[101]=4.68;    corrNEs[101]=-0.003; 
   ids[102]='AC58';    sts[102]='StPaulIsldAK2008';    lats[102]=57.1560889774;    lngs[102]=189.7821801301;    ves[102]=-2.61;    vns[102]=-2.18;    vus[102]=-2.01;    stddevNs[102]=2.29;    stddevEs[102]=4.19;    stddevUs[102]=4.68;    corrNEs[102]=-0.003; 
   ids[103]='AC59';    sts[103]='AC59URSUS_AK2004';    lats[103]=59.5671982280;    lngs[103]=206.4147998555;    ves[103]=2.09;    vns[103]=-11.88;    vus[103]=5.3;    stddevNs[103]=0.5;    stddevEs[103]=0.34;    stddevUs[103]=0.86;    corrNEs[103]=0.001; 
   ids[104]='AC60';    sts[104]='Westeast__AK2008';    lats[104]=52.7146202072;    lngs[104]=174.0762683284;    ves[104]=-26.13;    vns[104]=18.28;    vus[104]=-8.95;    stddevNs[104]=9.69;    stddevEs[104]=9.28;    stddevUs[104]=6.76;    corrNEs[104]=0.001; 
   ids[105]='AC61';    sts[105]='ChickenM61AK2006';    lats[105]=64.0292602268;    lngs[105]=217.9241534138;    ves[105]=3.49;    vns[105]=-2.48;    vus[105]=-4.43;    stddevNs[105]=0.5;    stddevEs[105]=0.53;    stddevUs[105]=1.63;    corrNEs[105]=0.000; 
   ids[106]='AC61';    sts[106]='ChickenM61AK2006';    lats[106]=64.0292602619;    lngs[106]=217.9241533522;    ves[106]=3.49;    vns[106]=-2.48;    vus[106]=-4.43;    stddevNs[106]=0.5;    stddevEs[106]=0.53;    stddevUs[106]=1.63;    corrNEs[106]=0.000; 
   ids[107]='AC62';    sts[107]='DenliHwy32AK2004';    lats[107]=63.0836069599;    lngs[107]=213.6873040735;    ves[107]=-20.47;    vns[107]=7.34;    vus[107]=3.84;    stddevNs[107]=0.27;    stddevEs[107]=0.49;    stddevUs[107]=0.68;    corrNEs[107]=0.001; 
   ids[108]='AC63';    sts[108]='ATTPS_____AK2004';    lats[108]=63.5024265829;    lngs[108]=214.1527553381;    ves[108]=5.09;    vns[108]=-2.5;    vus[108]=8.34;    stddevNs[108]=0.61;    stddevEs[108]=0.6;    stddevUs[108]=0.79;    corrNEs[108]=0.000; 
   ids[109]='AC64';    sts[109]='MtnDrumVP_AK2004';    lats[109]=62.7140168912;    lngs[109]=215.6960118863;    ves[109]=-16.9;    vns[109]=29.04;    vus[109]=13.18;    stddevNs[109]=1.08;    stddevEs[109]=0.5;    stddevUs[109]=0.85;    corrNEs[109]=-0.003; 
   ids[110]='AC64';    sts[110]='MtnDrumVP_AK2004';    lats[110]=62.7140165530;    lngs[110]=215.6960116947;    ves[110]=-16.9;    vns[110]=29.04;    vus[110]=13.18;    stddevNs[110]=1.08;    stddevEs[110]=0.5;    stddevUs[110]=0.85;    corrNEs[110]=-0.003; 
   ids[111]='AC65';    sts[111]='Mentasta__AK2004';    lats[111]=62.8315113595;    lngs[111]=216.2957824420;    ves[111]=-11.86;    vns[111]=23.24;    vus[111]=13.32;    stddevNs[111]=0.5;    stddevEs[111]=0.4;    stddevUs[111]=0.87;    corrNEs[111]=-0.005; 
   ids[112]='AC66';    sts[112]='AmchitkaIsAK2006';    lats[112]=51.3781294311;    lngs[112]=179.3013258390;    ves[112]=-14.36;    vns[112]=-5.74;    vus[112]=7.98;    stddevNs[112]=2.04;    stddevEs[112]=1.48;    stddevUs[112]=2.38;    corrNEs[112]=0.000; 
   ids[113]='AC66';    sts[113]='AmchitkaIsAK2006';    lats[113]=51.3781294069;    lngs[113]=179.3013258271;    ves[113]=-14.36;    vns[113]=-5.74;    vus[113]=7.98;    stddevNs[113]=2.04;    stddevEs[113]=1.48;    stddevUs[113]=2.38;    corrNEs[113]=0.000; 
   ids[114]='AC67';    sts[114]='PillarMtn_AK2006';    lats[114]=57.7907180818;    lngs[114]=207.5745674627;    ves[114]=-9.27;    vns[114]=13.4;    vus[114]=8.11;    stddevNs[114]=0.52;    stddevEs[114]=0.71;    stddevUs[114]=1.02;    corrNEs[114]=0.001; 
   ids[115]='AC69';    sts[115]='Tetlin____AK2006';    lats[115]=63.1126141438;    lngs[115]=217.9734654215;    ves[115]=6.46;    vns[115]=-1.95;    vus[115]=-4.97;    stddevNs[115]=2.26;    stddevEs[115]=2.84;    stddevUs[115]=3.38;    corrNEs[115]=-0.001; 
   ids[116]='AC70';    sts[116]='Brokebits_AK2003';    lats[116]=63.3047116157;    lngs[116]=211.8116795356;    ves[116]=-11.04;    vns[116]=0.04;    vus[116]=3.38;    stddevNs[116]=0.45;    stddevEs[116]=0.91;    stddevUs[116]=1.51;    corrNEs[116]=0.001; 
   ids[117]='AC71';    sts[117]='DeltaJunc_AK2003';    lats[117]=64.0493058108;    lngs[117]=214.2863767485;    ves[117]=3.09;    vns[117]=-4.33;    vus[117]=2.36;    stddevNs[117]=0.53;    stddevEs[117]=0.58;    stddevUs[117]=1.56;    corrNEs[117]=-0.002; 
   ids[118]='AC72';    sts[118]='DonnellyC_AK2002';    lats[118]=63.6950590330;    lngs[118]=214.1123039352;    ves[118]=5.23;    vns[118]=-4.38;    vus[118]=5.1;    stddevNs[118]=0.58;    stddevEs[118]=0.64;    stddevUs[118]=1.93;    corrNEs[118]=-0.013; 
   ids[119]='AC74';    sts[119]='Cantwello_AK2002';    lats[119]=63.4643571504;    lngs[119]=211.1927413257;    ves[119]=-5.94;    vns[119]=-0.31;    vus[119]=2.06;    stddevNs[119]=0.53;    stddevEs[119]=0.63;    stddevUs[119]=1.33;    corrNEs[119]=0.002; 
   ids[120]='AC75';    sts[120]='Hurricane_AK2002';    lats[120]=62.9993051665;    lngs[120]=210.3911798002;    ves[120]=-7.98;    vns[120]=-1.53;    vus[120]=2.62;    stddevNs[120]=0.63;    stddevEs[120]=0.75;    stddevUs[120]=1.38;    corrNEs[120]=-0.000; 
   ids[121]='AC76';    sts[121]='LogCabin__AK2008';    lats[121]=63.0399988465;    lngs[121]=216.7411849310;    ves[121]=7.69;    vns[121]=6.81;    vus[121]=5.02;    stddevNs[121]=0.79;    stddevEs[121]=1.66;    stddevUs[121]=2.25;    corrNEs[121]=-0.003; 
   ids[122]='AC76';    sts[122]='LogCabin__AK2008';    lats[122]=63.0399988380;    lngs[122]=216.7411849040;    ves[122]=7.69;    vns[122]=6.81;    vus[122]=5.02;    stddevNs[122]=0.79;    stddevEs[122]=1.66;    stddevUs[122]=2.25;    corrNEs[122]=-0.003; 
   ids[123]='AC77';    sts[123]='Sourdough_AK2003';    lats[123]=62.6880327497;    lngs[123]=214.5737434190;    ves[123]=-16.81;    vns[123]=12.78;    vus[123]=7.35;    stddevNs[123]=0.48;    stddevEs[123]=0.77;    stddevUs[123]=1.47;    corrNEs[123]=0.000; 
   ids[124]='ACSO';    sts[124]='AlumCreek_OH2006';    lats[124]=40.2323605615;    lngs[124]=277.0184691488;    ves[124]=-0.55;    vns[124]=-0.44;    vus[124]=-3.95;    stddevNs[124]=0.4;    stddevEs[124]=0.37;    stddevUs[124]=1.19;    corrNEs[124]=-0.000; 
   ids[125]='AGMT';    sts[125]='AGMT_SCGN_CS1999';    lats[125]=34.5942821177;    lngs[125]=243.5706212404;    ves[125]=-8.03;    vns[125]=7.13;    vus[125]=-1.3;    stddevNs[125]=0.13;    stddevEs[125]=0.13;    stddevUs[125]=0.54;    corrNEs[125]=-0.016; 
   ids[126]='AHID';    sts[126]='AHID_EBRY_ID2000';    lats[126]=42.7731192970;    lngs[126]=248.9362598475;    ves[126]=-1.44;    vns[126]=-0.73;    vus[126]=-0.57;    stddevNs[126]=0.16;    stddevEs[126]=0.15;    stddevUs[126]=1.04;    corrNEs[126]=-0.005; 
   ids[127]='AIS1';    sts[127]='Annette_Island_1';    lats[127]=55.0690723838;    lngs[127]=228.4004644415;    ves[127]=0.93;    vns[127]=3.28;    vus[127]=1.65;    stddevNs[127]=0.44;    stddevEs[127]=0.55;    stddevUs[127]=1.22;    corrNEs[127]=0.001; 
   ids[128]='AIS1';    sts[128]='Annette_Island_1';    lats[128]=55.0690723788;    lngs[128]=228.4004645009;    ves[128]=0.93;    vns[128]=3.28;    vus[128]=1.65;    stddevNs[128]=0.44;    stddevEs[128]=0.55;    stddevUs[128]=1.22;    corrNEs[128]=0.001; 
   ids[129]='ALBH';    sts[129]='Albert_Head';    lats[129]=4745791.33122;    lngs[129]=48.3897812878;    ves[129]=4.24;    vns[129]=-745;    vus[129]=5.03;    stddevNs[129]=0.32;    stddevEs[129]=0.21;    stddevUs[129]=0.23;    corrNEs[129]=0.00061; 
   ids[130]='ALGO';    sts[130]='Algonquin';    lats[130]=4561977.85523;    lngs[130]=45.9558002115;    ves[130]=-1.33;    vns[130]=-856;    vus[130]=0.53;    stddevNs[130]=3.59;    stddevEs[130]=0.22;    stddevUs[130]=0.15;    corrNEs[130]=0.00079; 
   ids[131]='ALRT';    sts[131]='Alert';    lats[131]=-740382.39638;    lngs[131]=6302001.87867;    ves[131]=-383;    vns[131]=151;    vus[131]=-2.05;    stddevNs[131]=-1.59;    stddevEs[131]=2.89;    stddevUs[131]=0.31;    corrNEs[131]=0.00046; 
   ids[132]='ANA1';    sts[132]='ANA1_SCGN_CS2002';    lats[132]=34.0150060337;    lngs[132]=240.6365302473;    ves[132]=-31.22;    vns[132]=33.76;    vus[132]=1.72;    stddevNs[132]=0.41;    stddevEs[132]=0.26;    stddevUs[132]=0.87;    corrNEs[132]=-0.003; 
   ids[133]='ATW2';    sts[133]='ATW2_AKDA_AK2000';    lats[133]=61.5977545930;    lngs[133]=210.8677078858;    ves[133]=-11.67;    vns[133]=9.48;    vus[133]=1.65;    stddevNs[133]=0.27;    stddevEs[133]=0.37;    stddevUs[133]=1.08;    corrNEs[133]=0.000; 
   ids[134]='ATW2';    sts[134]='ATW2_AKDA_AK2000';    lats[134]=61.5977545215;    lngs[134]=210.8677081094;    ves[134]=-11.67;    vns[134]=9.48;    vus[134]=1.65;    stddevNs[134]=0.27;    stddevEs[134]=0.37;    stddevUs[134]=1.08;    corrNEs[134]=0.000; 
   ids[135]='AUGL';    sts[135]='AUGLAugst_AK2006';    lats[135]=59.3702983106;    lngs[135]=206.6460902022;    ves[135]=1.92;    vns[135]=-12.87;    vus[135]=8.43;    stddevNs[135]=0.29;    stddevEs[135]=0.59;    stddevUs[135]=0.84;    corrNEs[135]=0.000; 
   ids[136]='AUGL';    sts[136]='AUGLAugst_AK2006';    lats[136]=59.3702982492;    lngs[136]=206.6460901728;    ves[136]=1.92;    vns[136]=-12.87;    vus[136]=8.43;    stddevNs[136]=0.29;    stddevEs[136]=0.59;    stddevUs[136]=0.84;    corrNEs[136]=0.000; 
   ids[137]='AV01';    sts[137]='AV01AUGST_AK2004';    lats[137]=59.3585307397;    lngs[137]=206.5391994507;    ves[137]=-0.68;    vns[137]=-12.14;    vus[137]=1.63;    stddevNs[137]=0.56;    stddevEs[137]=0.72;    stddevUs[137]=0.99;    corrNEs[137]=0.001; 
   ids[138]='AV01';    sts[138]='AV01AUGST_AK2004';    lats[138]=59.3585307508;    lngs[138]=206.5391994271;    ves[138]=-0.68;    vns[138]=-12.14;    vus[138]=1.63;    stddevNs[138]=0.56;    stddevEs[138]=0.72;    stddevUs[138]=0.99;    corrNEs[138]=0.001; 
   ids[139]='AV01';    sts[139]='AV01AUGST_AK2004';    lats[139]=59.3585307152;    lngs[139]=206.5391993064;    ves[139]=-0.68;    vns[139]=-12.14;    vus[139]=1.63;    stddevNs[139]=0.56;    stddevEs[139]=0.72;    stddevUs[139]=0.99;    corrNEs[139]=0.001; 
   ids[140]='AV02';    sts[140]='AV02AUGST_AK2004';    lats[140]=59.3329745194;    lngs[140]=206.5716094598;    ves[140]=0.85;    vns[140]=-12.48;    vus[140]=3.97;    stddevNs[140]=0.61;    stddevEs[140]=0.37;    stddevUs[140]=0.97;    corrNEs[140]=0.000; 
   ids[141]='AV02';    sts[141]='AV02AUGST_AK2004';    lats[141]=59.3329745042;    lngs[141]=206.5716094418;    ves[141]=0.85;    vns[141]=-12.48;    vus[141]=3.97;    stddevNs[141]=0.61;    stddevEs[141]=0.37;    stddevUs[141]=0.97;    corrNEs[141]=0.000; 
   ids[142]='AV02';    sts[142]='AV02AUGST_AK2004';    lats[142]=59.3329744764;    lngs[142]=206.5716094422;    ves[142]=0.85;    vns[142]=-12.48;    vus[142]=3.97;    stddevNs[142]=0.61;    stddevEs[142]=0.37;    stddevUs[142]=0.97;    corrNEs[142]=0.000; 
   ids[143]='AV03';    sts[143]='AV03AUGST_AK2004';    lats[143]=59.3812960683;    lngs[143]=206.5622216275;    ves[143]=-2.53;    vns[143]=-12.2;    vus[143]=6.23;    stddevNs[143]=0.88;    stddevEs[143]=0.65;    stddevUs[143]=1.93;    corrNEs[143]=0.000; 
   ids[144]='AV03';    sts[144]='AV03AUGST_AK2004';    lats[144]=59.3812960485;    lngs[144]=206.5622214651;    ves[144]=-2.53;    vns[144]=-12.2;    vus[144]=6.23;    stddevNs[144]=0.88;    stddevEs[144]=0.65;    stddevUs[144]=1.93;    corrNEs[144]=0.000; 
   ids[145]='AV03';    sts[145]='AV03AUGST_AK2004';    lats[145]=59.3812960240;    lngs[145]=206.5622214527;    ves[145]=-2.53;    vns[145]=-12.2;    vus[145]=6.23;    stddevNs[145]=0.88;    stddevEs[145]=0.65;    stddevUs[145]=1.93;    corrNEs[145]=0.000; 
   ids[146]='AV03';    sts[146]='AV03AUGST_AK2004';    lats[146]=59.3812960146;    lngs[146]=206.5622213810;    ves[146]=-2.53;    vns[146]=-12.2;    vus[146]=6.23;    stddevNs[146]=0.88;    stddevEs[146]=0.65;    stddevUs[146]=1.93;    corrNEs[146]=0.000; 
   ids[147]='AV04';    sts[147]='AV04AUGST_AK2004';    lats[147]=59.3625833177;    lngs[147]=206.5553271796;    ves[147]=-1.81;    vns[147]=-15.33;    vus[147]=0.07;    stddevNs[147]=2.02;    stddevEs[147]=6.91;    stddevUs[147]=2.61;    corrNEs[147]=-0.000; 
   ids[148]='AV04';    sts[148]='AV04AUGST_AK2004';    lats[148]=59.3625831465;    lngs[148]=206.5553269829;    ves[148]=-1.81;    vns[148]=-15.33;    vus[148]=0.07;    stddevNs[148]=2.02;    stddevEs[148]=6.91;    stddevUs[148]=2.61;    corrNEs[148]=-0.000; 
   ids[149]='AV04';    sts[149]='AV04AUGST_AK2004';    lats[149]=59.3625831196;    lngs[149]=206.5553267330;    ves[149]=-1.81;    vns[149]=-15.33;    vus[149]=0.07;    stddevNs[149]=2.02;    stddevEs[149]=6.91;    stddevUs[149]=2.61;    corrNEs[149]=-0.000; 
   ids[150]='AV05';    sts[150]='AV05AUGST_AK2004';    lats[150]=59.3629353500;    lngs[150]=206.5773525211;    ves[150]=100.53;    vns[150]=48.51;    vus[150]=35.41;    stddevNs[150]=18.5;    stddevEs[150]=24.47;    stddevUs[150]=2.27;    corrNEs[150]=0.000; 
   ids[151]='AV06';    sts[151]='AKU_AkutPtAK2005';    lats[151]=54.1471797089;    lngs[151]=194.2342794899;    ves[151]=-3.1;    vns[151]=-2.7;    vus[151]=3.02;    stddevNs[151]=0.48;    stddevEs[151]=0.43;    stddevUs[151]=1.06;    corrNEs[151]=0.002; 
   ids[152]='AV07';    sts[152]='AKU_NWSlopAK2005';    lats[152]=54.1629374133;    lngs[152]=193.9614633757;    ves[152]=-6.53;    vns[152]=-2.58;    vus[152]=4.5;    stddevNs[152]=0.46;    stddevEs[152]=1.13;    stddevUs[152]=1.23;    corrNEs[152]=0.001; 
   ids[153]='AV08';    sts[153]='AKU_WSlopeAK2005';    lats[153]=54.1362996599;    lngs[153]=193.9717286446;    ves[153]=-5.95;    vns[153]=-2.34;    vus[153]=4.07;    stddevNs[153]=0.65;    stddevEs[153]=0.78;    stddevUs[153]=1.15;    corrNEs[153]=0.001; 
   ids[154]='AV09';    sts[154]='Haystack__AK2004';    lats[154]=53.8756350955;    lngs[154]=193.4581637846;    ves[154]=-5.54;    vns[154]=-2.25;    vus[154]=3.49;    stddevNs[154]=0.39;    stddevEs[154]=0.35;    stddevUs[154]=0.79;    corrNEs[154]=0.004; 
   ids[155]='AV10';    sts[155]='AKU_SESlopAK2005';    lats[155]=54.0976099290;    lngs[155]=194.0662355033;    ves[155]=-4.13;    vns[155]=-4.43;    vus[155]=4.29;    stddevNs[155]=0.88;    stddevEs[155]=0.48;    stddevUs[155]=1.19;    corrNEs[155]=0.001; 
   ids[156]='AV11';    sts[156]='Augs_MoundAK2006';    lats[156]=59.3706279375;    lngs[156]=206.6453147059;    ves[156]=2.12;    vns[156]=-11.42;    vus[156]=6.66;    stddevNs[156]=0.36;    stddevEs[156]=0.58;    stddevUs[156]=0.89;    corrNEs[156]=0.002; 
   ids[157]='AV12';    sts[157]='AKU_OpenBtAK2005';    lats[157]=54.2107997082;    lngs[157]=194.1024541734;    ves[157]=-1.75;    vns[157]=1.52;    vus[157]=5.52;    stddevNs[157]=0.53;    stddevEs[157]=0.45;    stddevUs[157]=1.18;    corrNEs[157]=0.002; 
   ids[158]='AV13';    sts[158]='AKUFumarolAK2005';    lats[158]=54.1530817223;    lngs[158]=194.1021848497;    ves[158]=-0.48;    vns[158]=-2.89;    vus[158]=8.03;    stddevNs[158]=0.54;    stddevEs[158]=0.87;    stddevUs[158]=1.1;    corrNEs[158]=0.001; 
   ids[159]='AV14';    sts[159]='AKU_HarborAK2005';    lats[159]=54.1188951756;    lngs[159]=194.1584315812;    ves[159]=-2.34;    vns[159]=-3.64;    vus[159]=3.31;    stddevNs[159]=0.59;    stddevEs[159]=0.52;    stddevUs[159]=1.17;    corrNEs[159]=0.002; 
   ids[160]='AV15';    sts[160]='AKU_EastPtAK2005';    lats[160]=54.1001942517;    lngs[160]=194.2898872144;    ves[160]=-6.53;    vns[160]=-2.5;    vus[160]=3.67;    stddevNs[160]=0.49;    stddevEs[160]=0.48;    stddevUs[160]=0.88;    corrNEs[160]=0.002; 
   ids[161]='AV16';    sts[161]='AugvLagoonAK2006';    lats[161]=59.3859111571;    lngs[161]=206.4649552895;    ves[161]=1.75;    vns[161]=-10.78;    vus[161]=5.75;    stddevNs[161]=0.34;    stddevEs[161]=0.55;    stddevUs[161]=0.8;    corrNEs[161]=0.002; 
   ids[162]='AV17';    sts[162]='AugustinNWAK2006';    lats[162]=59.4039467987;    lngs[162]=206.5485550990;    ves[162]=1.32;    vns[162]=-10.09;    vus[162]=3.9;    stddevNs[162]=0.45;    stddevEs[162]=1;    stddevUs[162]=1.03;    corrNEs[162]=0.001; 
   ids[163]='AV18';    sts[163]='AugvNorth_AK2006';    lats[163]=59.3804339718;    lngs[163]=206.5631655592;    ves[163]=7.73;    vns[163]=-9.97;    vus[163]=-2.65;    stddevNs[163]=0.38;    stddevEs[163]=0.61;    stddevUs[163]=0.96;    corrNEs[163]=0.002; 
   ids[164]='AV19';    sts[164]='AugustinSEAK2006';    lats[164]=59.3549081119;    lngs[164]=206.5856720635;    ves[164]=3.8;    vns[164]=-13.28;    vus[164]=-0.74;    stddevNs[164]=0.37;    stddevEs[164]=0.48;    stddevUs[164]=1.11;    corrNEs[164]=-0.002; 
   ids[165]='AV20';    sts[165]='Augs_SouthAK2006';    lats[165]=59.3473817026;    lngs[165]=206.5717946026;    ves[165]=2.98;    vns[165]=-11.42;    vus[165]=2.68;    stddevNs[165]=2.94;    stddevEs[165]=1.97;    stddevUs[165]=2.5;    corrNEs[165]=0.000; 
   ids[166]='AV21';    sts[166]='AV21Augst_AK2006';    lats[166]=59.3702982492;    lngs[166]=206.6460901816;    ves[166]=1.92;    vns[166]=-12.87;    vus[166]=8.43;    stddevNs[166]=0.29;    stddevEs[166]=0.59;    stddevUs[166]=0.84;    corrNEs[166]=0.000; 
   ids[167]='AV24';    sts[167]='WestdahlNWAK2008';    lats[167]=54.5899847666;    lngs[167]=195.2451969573;    ves[167]=-9.17;    vns[167]=3.44;    vus[167]=3.48;    stddevNs[167]=2.11;    stddevEs[167]=2.4;    stddevUs[167]=3.13;    corrNEs[167]=0.000; 
   ids[168]='AV24';    sts[168]='WestdahlNWAK2008';    lats[168]=54.5899848034;    lngs[168]=195.2451969239;    ves[168]=-9.17;    vns[168]=3.44;    vus[168]=3.48;    stddevNs[168]=2.11;    stddevEs[168]=2.4;    stddevUs[168]=3.13;    corrNEs[168]=0.000; 
   ids[169]='AV25';    sts[169]='WestdahlW_AK2007';    lats[169]=54.5300035952;    lngs[169]=195.2205019216;    ves[169]=-27.59;    vns[169]=-0.38;    vus[169]=7.16;    stddevNs[169]=226.49;    stddevEs[169]=185.09;    stddevUs[169]=33.44;    corrNEs[169]=0.000; 
   ids[170]='AV25';    sts[170]='WestdahlW_AK2007';    lats[170]=54.5300035222;    lngs[170]=195.2205022802;    ves[170]=-27.59;    vns[170]=-0.38;    vus[170]=7.16;    stddevNs[170]=226.49;    stddevEs[170]=185.09;    stddevUs[170]=33.44;    corrNEs[170]=0.000; 
   ids[171]='AV26';    sts[171]='WestdahlNEAK2008';    lats[171]=54.5716548355;    lngs[171]=195.4195354653;    ves[171]=-0.59;    vns[171]=5.82;    vus[171]=9.08;    stddevNs[171]=2.94;    stddevEs[171]=4.54;    stddevUs[171]=3.18;    corrNEs[171]=-0.000; 
   ids[172]='AV26';    sts[172]='WestdahlNEAK2008';    lats[172]=54.5716548753;    lngs[172]=195.4195354096;    ves[172]=-0.59;    vns[172]=5.82;    vus[172]=9.08;    stddevNs[172]=2.94;    stddevEs[172]=4.54;    stddevUs[172]=3.18;    corrNEs[172]=-0.000; 
   ids[173]='AV27';    sts[173]='WestdahlSWAK2008';    lats[173]=54.4923514538;    lngs[173]=195.2768376803;    ves[173]=-44.93;    vns[173]=-25.01;    vus[173]=110.52;    stddevNs[173]=25.42;    stddevEs[173]=10.39;    stddevUs[173]=11.15;    corrNEs[173]=0.001; 
   ids[174]='AV27';    sts[174]='WestdahlSWAK2008';    lats[174]=54.4923510619;    lngs[174]=195.2768371704;    ves[174]=-44.93;    vns[174]=-25.01;    vus[174]=110.52;    stddevNs[174]=25.42;    stddevEs[174]=10.39;    stddevUs[174]=11.15;    corrNEs[174]=0.001; 
   ids[175]='AV29';    sts[175]='WestdahlSEAK2008';    lats[175]=54.4723540460;    lngs[175]=195.4138505264;    ves[175]=10.73;    vns[175]=2.33;    vus[175]=10.71;    stddevNs[175]=15.69;    stddevEs[175]=13.17;    stddevUs[175]=5.26;    corrNEs[175]=-0.000; 
   ids[176]='AV34';    sts[176]='IsanotskiSAK2008';    lats[176]=54.7249098736;    lngs[176]=196.2870813493;    ves[176]=-5.65;    vns[176]=-0.48;    vus[176]=9.12;    stddevNs[176]=2.62;    stddevEs[176]=14.86;    stddevUs[176]=4.6;    corrNEs[176]=-0.000; 
   ids[177]='AV34';    sts[177]='IsanotskiSAK2008';    lats[177]=54.7249099237;    lngs[177]=196.2870812946;    ves[177]=-5.65;    vns[177]=-0.48;    vus[177]=9.12;    stddevNs[177]=2.62;    stddevEs[177]=14.86;    stddevUs[177]=4.6;    corrNEs[177]=-0.000; 
   ids[178]='AV35';    sts[178]='TugamakRngAK2007';    lats[178]=54.8466956950;    lngs[178]=195.6130837624;    ves[178]=-1.62;    vns[178]=-15.88;    vus[178]=-6.01;    stddevNs[178]=36.62;    stddevEs[178]=25.81;    stddevUs[178]=13.23;    corrNEs[178]=0.001; 
   ids[179]='AV35';    sts[179]='TugamakRngAK2007';    lats[179]=54.8466953381;    lngs[179]=195.6130838396;    ves[179]=-1.62;    vns[179]=-15.88;    vus[179]=-6.01;    stddevNs[179]=36.62;    stddevEs[179]=25.81;    stddevUs[179]=13.23;    corrNEs[179]=0.001; 
   ids[180]='AV36';    sts[180]='ShshldinNWAK2008';    lats[180]=54.7717951206;    lngs[180]=195.8731965457;    ves[180]=-7.45;    vns[180]=2.68;    vus[180]=11.42;    stddevNs[180]=4.71;    stddevEs[180]=3.14;    stddevUs[180]=2.84;    corrNEs[180]=0.000; 
   ids[181]='AV36';    sts[181]='ShshldinNWAK2008';    lats[181]=54.7717950481;    lngs[181]=195.8731965581;    ves[181]=-7.45;    vns[181]=2.68;    vus[181]=11.42;    stddevNs[181]=4.71;    stddevEs[181]=3.14;    stddevUs[181]=2.84;    corrNEs[181]=0.000; 
   ids[182]='AV37';    sts[182]='ShshldinSWAK2008';    lats[182]=54.7093805642;    lngs[182]=196.0027657499;    ves[182]=-16.49;    vns[182]=-2.56;    vus[182]=8.19;    stddevNs[182]=5.24;    stddevEs[182]=6.51;    stddevUs[182]=11.08;    corrNEs[182]=-0.067; 
   ids[183]='AV37';    sts[183]='ShshldinSWAK2008';    lats[183]=54.7093806317;    lngs[183]=196.0027658552;    ves[183]=-16.49;    vns[183]=-2.56;    vus[183]=8.19;    stddevNs[183]=5.24;    stddevEs[183]=6.51;    stddevUs[183]=11.08;    corrNEs[183]=-0.067; 
   ids[184]='AV38';    sts[184]='IsanotskiNAK2008';    lats[184]=54.8314696015;    lngs[184]=196.2191268495;    ves[184]=-6.52;    vns[184]=4.14;    vus[184]=1.45;    stddevNs[184]=3.54;    stddevEs[184]=3.33;    stddevUs[184]=3.16;    corrNEs[184]=-0.000; 
   ids[185]='AV38';    sts[185]='IsanotskiNAK2008';    lats[185]=54.8314696218;    lngs[185]=196.2191268366;    ves[185]=-6.52;    vns[185]=4.14;    vus[185]=1.45;    stddevNs[185]=3.54;    stddevEs[185]=3.33;    stddevUs[185]=3.16;    corrNEs[185]=-0.000; 
   ids[186]='AV39';    sts[186]='ShishaldinAK2008';    lats[186]=54.8113425804;    lngs[186]=196.0015341568;    ves[186]=-3.34;    vns[186]=6.31;    vus[186]=10.78;    stddevNs[186]=4.76;    stddevEs[186]=3.47;    stddevUs[186]=3.36;    corrNEs[186]=-0.000; 
   ids[187]='AV39';    sts[187]='ShishaldinAK2008';    lats[187]=54.8113426251;    lngs[187]=196.0015341354;    ves[187]=-3.34;    vns[187]=6.31;    vus[187]=10.78;    stddevNs[187]=4.76;    stddevEs[187]=3.47;    stddevUs[187]=3.36;    corrNEs[187]=-0.000; 
   ids[188]='AV40';    sts[188]='BrownPeak_AK2008';    lats[188]=54.6445375190;    lngs[188]=196.2570328590;    ves[188]=-6.95;    vns[188]=-1.94;    vus[188]=8.37;    stddevNs[188]=1.74;    stddevEs[188]=2.3;    stddevUs[188]=2.77;    corrNEs[188]=0.000; 
   ids[189]='AV40';    sts[189]='BrownPeak_AK2008';    lats[189]=54.6445375673;    lngs[189]=196.2570328252;    ves[189]=-6.95;    vns[189]=-1.94;    vus[189]=8.37;    stddevNs[189]=1.74;    stddevEs[189]=2.3;    stddevUs[189]=2.77;    corrNEs[189]=0.000; 
   ids[190]='AVRY';    sts[190]='Apple_ValleSCIGN';    lats[190]=34.4683016261;    lngs[190]=242.8460279635;    ves[190]=-13.82;    vns[190]=14.52;    vus[190]=-0.44;    stddevNs[190]=0.24;    stddevEs[190]=0.68;    stddevUs[190]=1.05;    corrNEs[190]=-0.001; 
   ids[191]='AVRY';    sts[191]='Apple_ValleSCIGN';    lats[191]=34.4683016175;    lngs[191]=242.8460279473;    ves[191]=-13.82;    vns[191]=14.52;    vus[191]=-0.44;    stddevNs[191]=0.24;    stddevEs[191]=0.68;    stddevUs[191]=1.05;    corrNEs[191]=-0.001; 
   ids[192]='AVRY';    sts[192]='Apple_ValleSCIGN';    lats[192]=34.4683014923;    lngs[192]=242.8460277055;    ves[192]=-13.82;    vns[192]=14.52;    vus[192]=-0.44;    stddevNs[192]=0.24;    stddevEs[192]=0.68;    stddevUs[192]=1.05;    corrNEs[192]=-0.001; 
   ids[193]='AZU1';    sts[193]='AZU1_SCGN_CS1996';    lats[193]=34.1260190841;    lngs[193]=242.1035126761;    ves[193]=-25.07;    vns[193]=22.27;    vus[193]=-2.79;    stddevNs[193]=0.51;    stddevEs[193]=0.4;    stddevUs[193]=0.7;    corrNEs[193]=-0.002; 
   ids[194]='AZU1';    sts[194]='AZU1_SCGN_CS1996';    lats[194]=34.1260190585;    lngs[194]=242.1035126444;    ves[194]=-25.07;    vns[194]=22.27;    vus[194]=-2.79;    stddevNs[194]=0.51;    stddevEs[194]=0.4;    stddevUs[194]=0.7;    corrNEs[194]=-0.002; 
   ids[195]='BAMO';    sts[195]='BAMO_BRGN_NV2003';    lats[195]=40.4127504692;    lngs[195]=242.7954340666;    ves[195]=-3.99;    vns[195]=0.97;    vus[195]=-0.74;    stddevNs[195]=0.17;    stddevEs[195]=0.13;    stddevUs[195]=0.6;    corrNEs[195]=-0.009; 
   ids[196]='BAR1';    sts[196]='BAR1_SCGN_CS2002';    lats[196]=33.4804523565;    lngs[196]=240.9702637173;    ves[196]=-30.76;    vns[196]=32.95;    vus[196]=-2.49;    stddevNs[196]=0.15;    stddevEs[196]=0.19;    stddevUs[196]=0.58;    corrNEs[196]=-0.012; 
   ids[197]='BAY2';    sts[197]='Cold_Bay_2__CORS';    lats[197]=55.1904181056;    lngs[197]=197.2932354419;    ves[197]=-5.97;    vns[197]=-2.12;    vus[197]=1.92;    stddevNs[197]=0.36;    stddevEs[197]=0.38;    stddevUs[197]=1.52;    corrNEs[197]=0.003; 
   ids[198]='BBDM';    sts[198]='BBDM_SCGN_CS2000';    lats[198]=34.5821989201;    lngs[198]=240.0184864628;    ves[198]=-32.09;    vns[198]=31.46;    vus[198]=-0.74;    stddevNs[198]=0.28;    stddevEs[198]=0.6;    stddevUs[198]=0.75;    corrNEs[198]=-0.002; 
   ids[199]='BBID';    sts[199]='BBID_EBRY_ID2002';    lats[199]=44.1850447464;    lngs[199]=248.4738737092;    ves[199]=-2.26;    vns[199]=-2.23;    vus[199]=-3.49;    stddevNs[199]=0.17;    stddevEs[199]=0.15;    stddevUs[199]=0.73;    corrNEs[199]=-0.010; 
   ids[200]='BBRY';    sts[200]='BBRY_SCGN_CS1998';    lats[200]=34.2642791656;    lngs[200]=243.1157582246;    ves[200]=-12.79;    vns[200]=14.21;    vus[200]=0.05;    stddevNs[200]=0.18;    stddevEs[200]=0.13;    stddevUs[200]=0.41;    corrNEs[200]=-0.010; 
   ids[201]='BEMT';    sts[201]='BEMT_SCGN_CS2001';    lats[201]=34.0005342859;    lngs[201]=244.0018092823;    ves[201]=-5.97;    vns[201]=4.96;    vus[201]=-1.22;    stddevNs[201]=0.13;    stddevEs[201]=0.13;    stddevUs[201]=0.45;    corrNEs[201]=-0.015; 
   ids[202]='BEPK';    sts[202]='BEPK_SCGN_CS2000';    lats[202]=35.8783882981;    lngs[202]=241.9259075944;    ves[202]=-10.53;    vns[202]=11.31;    vus[202]=-0.43;    stddevNs[202]=0.43;    stddevEs[202]=0.2;    stddevUs[202]=0.48;    corrNEs[202]=-0.003; 
   ids[203]='BILI';    sts[203]='Bilibino';    lats[203]=560096.84300;    lngs[203]=5894691.78931;    ves[203]=440;    vns[203]=-713;    vus[203]=-1.16;    stddevNs[203]=2.66;    stddevEs[203]=0.64;    stddevUs[203]=0.21;    corrNEs[203]=0.00020; 
   ids[204]='BILL';    sts[204]='BILL_SCGN_CS1997';    lats[204]=33.5782437316;    lngs[204]=242.9353997241;    ves[204]=-25.05;    vns[204]=24.62;    vus[204]=-1.24;    stddevNs[204]=0.19;    stddevEs[204]=0.16;    stddevUs[204]=0.55;    corrNEs[204]=-0.009; 
   ids[205]='BILL';    sts[205]='BILL_SCGN_CS1997';    lats[205]=33.5782437318;    lngs[205]=242.9353997262;    ves[205]=-25.05;    vns[205]=24.62;    vus[205]=-1.24;    stddevNs[205]=0.19;    stddevEs[205]=0.16;    stddevUs[205]=0.55;    corrNEs[205]=-0.009; 
   ids[206]='BIS1';    sts[206]='Biorka_IslanCORS';    lats[206]=56.8544928711;    lngs[206]=224.4607036594;    ves[206]=-3.87;    vns[206]=7.47;    vus[206]=3.48;    stddevNs[206]=0.52;    stddevEs[206]=0.32;    stddevUs[206]=1.33;    corrNEs[206]=-0.003; 
   ids[207]='BIS1';    sts[207]='Biorka_IslanCORS';    lats[207]=56.8544928476;    lngs[207]=224.4607036097;    ves[207]=-3.87;    vns[207]=7.47;    vus[207]=3.48;    stddevNs[207]=0.52;    stddevEs[207]=0.32;    stddevUs[207]=1.33;    corrNEs[207]=-0.003; 
   ids[208]='BKAP';    sts[208]='BKAP_SCGN_CS2000';    lats[208]=35.2870477158;    lngs[208]=243.9195755962;    ves[208]=-4.64;    vns[208]=0.72;    vus[208]=-0.22;    stddevNs[208]=0.17;    stddevEs[208]=0.15;    stddevUs[208]=0.51;    corrNEs[208]=-0.014; 
   ids[209]='BKAP';    sts[209]='BKAP_SCGN_CS2000';    lats[209]=35.2870477225;    lngs[209]=243.9195755942;    ves[209]=-4.64;    vns[209]=0.72;    vus[209]=-0.22;    stddevNs[209]=0.17;    stddevEs[209]=0.15;    stddevUs[209]=0.51;    corrNEs[209]=-0.014; 
   ids[210]='BKAP';    sts[210]='BKAP_SCGN_CS2000';    lats[210]=35.2870477133;    lngs[210]=243.9195756011;    ves[210]=-4.64;    vns[210]=0.72;    vus[210]=-0.22;    stddevNs[210]=0.17;    stddevEs[210]=0.15;    stddevUs[210]=0.51;    corrNEs[210]=-0.014; 
   ids[211]='BKMS';    sts[211]='BKMS_SCGN_CS1998';    lats[211]=33.9622587129;    lngs[211]=241.9053013569;    ves[211]=-29.54;    vns[211]=29.25;    vus[211]=-1.49;    stddevNs[211]=0.18;    stddevEs[211]=0.32;    stddevUs[211]=1.02;    corrNEs[211]=-0.005; 
   ids[212]='BLA1';    sts[212]='BlacksburgVA2006';    lats[212]=37.2113482607;    lngs[212]=279.5794930104;    ves[212]=1.43;    vns[212]=-0.72;    vus[212]=-2.25;    stddevNs[212]=0.56;    stddevEs[212]=0.65;    stddevUs[212]=1.21;    corrNEs[212]=0.000; 
   ids[213]='BLA1';    sts[213]='BlacksburgVA2006';    lats[213]=37.2113482511;    lngs[213]=279.5794930297;    ves[213]=1.43;    vns[213]=-0.72;    vus[213]=-2.25;    stddevNs[213]=0.56;    stddevEs[213]=0.65;    stddevUs[213]=1.21;    corrNEs[213]=0.000; 
   ids[214]='BLW2';    sts[214]='BLW2_EBRY_WY2003';    lats[214]=42.7671270890;    lngs[214]=250.4421989584;    ves[214]=-1.03;    vns[214]=0.04;    vus[214]=-1.02;    stddevNs[214]=0.23;    stddevEs[214]=0.18;    stddevUs[214]=0.98;    corrNEs[214]=-0.002; 
   ids[215]='BLYN';    sts[215]='BLYN_PNGA_WA2001';    lats[215]=48.0160577909;    lngs[215]=237.0724603221;    ves[215]=4.62;    vns[215]=3.96;    vus[215]=-4.46;    stddevNs[215]=0.29;    stddevEs[215]=0.46;    stddevUs[215]=0.97;    corrNEs[215]=-0.001; 
   ids[216]='BMHL';    sts[216]='BMHL_SCGN_CS1999';    lats[216]=34.2514452663;    lngs[216]=243.9470221393;    ves[216]=-3.84;    vns[216]=3.82;    vus[216]=-0.82;    stddevNs[216]=0.13;    stddevEs[216]=0.14;    stddevUs[216]=0.46;    corrNEs[216]=-0.014; 
   ids[217]='BRMU';    sts[217]='Bermuda';    lats[217]=-4874817.20391;    lngs[217]=3395186.93304;    ves[217]=-806;    vns[217]=486;    vus[217]=-2.31;    stddevNs[217]=-0.28;    stddevEs[217]=-0.5;    stddevUs[217]=0.29;    corrNEs[217]=0.00059; 
   ids[218]='BSRY';    sts[218]='BSRY_SCGN_CS1998';    lats[218]=34.9186124867;    lngs[218]=242.9880069354;    ves[218]=-9.83;    vns[218]=10.19;    vus[218]=-1.97;    stddevNs[218]=0.15;    stddevEs[218]=0.17;    stddevUs[218]=0.56;    corrNEs[218]=-0.011; 
   ids[219]='BTDM';    sts[219]='BTDM_SCGN_CS1999';    lats[219]=34.2928063961;    lngs[219]=241.8117735240;    ves[219]=-25.11;    vns[219]=22.31;    vus[219]=-2.04;    stddevNs[219]=0.21;    stddevEs[219]=0.18;    stddevUs[219]=0.53;    corrNEs[219]=-0.012; 
   ids[220]='BURN';    sts[220]='BURN_PNGA_OR1998';    lats[220]=42.7795015579;    lngs[220]=242.1564707912;    ves[220]=-2.26;    vns[220]=1.39;    vus[220]=-2.4;    stddevNs[220]=0.21;    stddevEs[220]=0.22;    stddevUs[220]=0.73;    corrNEs[220]=-0.004; 
   ids[221]='BURN';    sts[221]='BURN_PNGA_OR1998';    lats[221]=42.7795015592;    lngs[221]=242.1564707761;    ves[221]=-2.26;    vns[221]=1.39;    vus[221]=-2.4;    stddevNs[221]=0.21;    stddevEs[221]=0.22;    stddevUs[221]=0.73;    corrNEs[221]=-0.004; 
   ids[222]='BVPP';    sts[222]='BVPP_SCGN_CS2000';    lats[222]=35.1572766011;    lngs[222]=240.6524931811;    ves[222]=-17.94;    vns[222]=14.33;    vus[222]=-1.92;    stddevNs[222]=0.17;    stddevEs[222]=0.18;    stddevUs[222]=0.63;    corrNEs[222]=-0.011; 
   ids[223]='CABL';    sts[223]='CABL_PNGA_OR1997';    lats[223]=42.8360992571;    lngs[223]=235.4366547124;    ves[223]=4.85;    vns[223]=13.63;    vus[223]=-0.71;    stddevNs[223]=0.18;    stddevEs[223]=0.18;    stddevUs[223]=0.67;    corrNEs[223]=-0.008; 
   ids[224]='CABL';    sts[224]='CABL_PNGA_OR1997';    lats[224]=42.8360992207;    lngs[224]=235.4366546935;    ves[224]=4.85;    vns[224]=13.63;    vus[224]=-0.71;    stddevNs[224]=0.18;    stddevEs[224]=0.18;    stddevUs[224]=0.67;    corrNEs[224]=-0.008; 
   ids[225]='CABL';    sts[225]='CABL_PNGA_OR1997';    lats[225]=42.8360992111;    lngs[225]=235.4366546980;    ves[225]=4.85;    vns[225]=13.63;    vus[225]=-0.71;    stddevNs[225]=0.18;    stddevEs[225]=0.18;    stddevUs[225]=0.67;    corrNEs[225]=-0.008; 
   ids[226]='CABL';    sts[226]='CABL_PNGA_OR1997';    lats[226]=42.8360992167;    lngs[226]=235.4366547105;    ves[226]=4.85;    vns[226]=13.63;    vus[226]=-0.71;    stddevNs[226]=0.18;    stddevEs[226]=0.18;    stddevUs[226]=0.67;    corrNEs[226]=-0.008; 
   ids[227]='CAND';    sts[227]='CAND_SCGN_CN1999';    lats[227]=35.9393524554;    lngs[227]=239.5663036664;    ves[227]=-13.85;    vns[227]=18.73;    vus[227]=-1.11;    stddevNs[227]=0.22;    stddevEs[227]=0.36;    stddevUs[227]=1.03;    corrNEs[227]=-0.004; 
   ids[228]='CAND';    sts[228]='CAND_SCGN_CN1999';    lats[228]=35.9393533830;    lngs[228]=239.5663027960;    ves[228]=-13.85;    vns[228]=18.73;    vus[228]=-1.11;    stddevNs[228]=0.22;    stddevEs[228]=0.36;    stddevUs[228]=1.03;    corrNEs[228]=-0.004; 
   ids[229]='CARH';    sts[229]='CARH_SCGN_CN2001';    lats[229]=35.8883843696;    lngs[229]=239.5691788716;    ves[229]=-22.11;    vns[229]=25.71;    vus[229]=-3.11;    stddevNs[229]=0.49;    stddevEs[229]=0.33;    stddevUs[229]=1.27;    corrNEs[229]=-0.002; 
   ids[230]='CARH';    sts[230]='CARH_SCGN_CN2001';    lats[230]=35.8883849901;    lngs[230]=239.5691782859;    ves[230]=-22.11;    vns[230]=25.71;    vus[230]=-3.11;    stddevNs[230]=0.49;    stddevEs[230]=0.33;    stddevUs[230]=1.27;    corrNEs[230]=-0.002; 
   ids[231]='CAST';    sts[231]='CAST_BRGN_UT1996';    lats[231]=39.1910221724;    lngs[231]=249.3226868783;    ves[231]=-1.21;    vns[231]=-0.23;    vus[231]=-0.41;    stddevNs[231]=0.15;    stddevEs[231]=0.15;    stddevUs[231]=0.85;    corrNEs[231]=-0.006; 
   ids[232]='CAT1';    sts[232]='CAT1_SCGN_CS1995';    lats[232]=33.4457728229;    lngs[232]=241.5169918777;    ves[232]=-30.35;    vns[232]=31.53;    vus[232]=-1.58;    stddevNs[232]=0.16;    stddevEs[232]=0.15;    stddevUs[232]=0.6;    corrNEs[232]=-0.015; 
   ids[233]='CAT2';    sts[233]='CAT2_SCGN_CS2000';    lats[233]=33.3116171372;    lngs[233]=241.6661839260;    ves[233]=-30.03;    vns[233]=31.36;    vus[233]=-2.9;    stddevNs[233]=0.13;    stddevEs[233]=0.13;    stddevUs[233]=0.58;    corrNEs[233]=-0.017; 
   ids[234]='CAT3';    sts[234]='CAT3_SCGN_CS2008';    lats[234]=33.4457619371;    lngs[234]=241.5169943627;    ves[234]=-29.84;    vns[234]=31.41;    vus[234]=-3.06;    stddevNs[234]=0.27;    stddevEs[234]=0.27;    stddevUs[234]=1.14;    corrNEs[234]=-0.005; 
   ids[235]='CBHS';    sts[235]='CBHS_SCGN_CS1998';    lats[235]=34.1385617675;    lngs[235]=241.3701945472;    ves[235]=-28.54;    vns[235]=28.19;    vus[235]=-1.1;    stddevNs[235]=0.2;    stddevEs[235]=0.14;    stddevUs[235]=0.59;    corrNEs[235]=-0.011; 
   ids[236]='CCCC';    sts[236]='CCCC_SCGN_CS2000';    lats[236]=35.5653125310;    lngs[236]=242.3288279308;    ves[236]=-9.93;    vns[236]=9.63;    vus[236]=-1.77;    stddevNs[236]=0.14;    stddevEs[236]=0.23;    stddevUs[236]=0.51;    corrNEs[236]=-0.009; 
   ids[237]='CCCC';    sts[237]='CCCC_SCGN_CS2000';    lats[237]=35.5653125233;    lngs[237]=242.3288279158;    ves[237]=-9.93;    vns[237]=9.63;    vus[237]=-1.77;    stddevNs[237]=0.14;    stddevEs[237]=0.23;    stddevUs[237]=0.51;    corrNEs[237]=-0.009; 
   ids[238]='CDMT';    sts[238]='CDMT_SCGN_CS2000';    lats[238]=34.8294676331;    lngs[238]=243.6640688613;    ves[238]=-7.07;    vns[238]=2.49;    vus[238]=-0.6;    stddevNs[238]=0.13;    stddevEs[238]=0.14;    stddevUs[238]=0.53;    corrNEs[238]=-0.013; 
   ids[239]='CEDA';    sts[239]='CEDA_BRGN_UT1996';    lats[239]=40.6806955950;    lngs[239]=247.1395188168;    ves[239]=-3.61;    vns[239]=-0.27;    vus[239]=0.29;    stddevNs[239]=0.14;    stddevEs[239]=0.12;    stddevUs[239]=0.89;    corrNEs[239]=-0.009; 
   ids[240]='CGDM';    sts[240]='CGDM_SCGN_CS2000';    lats[240]=34.2439931399;    lngs[240]=242.0350542137;    ves[240]=-24.9;    vns[240]=21.34;    vus[240]=-1.33;    stddevNs[240]=0.51;    stddevEs[240]=0.4;    stddevUs[240]=0.56;    corrNEs[240]=-0.002; 
   ids[241]='CHAB';    sts[241]='CHAB_BARD_CN1991';    lats[241]=37.7241163377;    lngs[241]=237.8806893874;    ves[241]=-17.42;    vns[241]=20.12;    vus[241]=0.07;    stddevNs[241]=0.32;    stddevEs[241]=0.4;    stddevUs[241]=1.02;    corrNEs[241]=-0.003; 
   ids[242]='CHAB';    sts[242]='CHAB_BARD_CN1991';    lats[242]=37.7241163335;    lngs[242]=237.8806893868;    ves[242]=-17.42;    vns[242]=20.12;    vus[242]=0.07;    stddevNs[242]=0.32;    stddevEs[242]=0.4;    stddevUs[242]=1.02;    corrNEs[242]=-0.003; 
   ids[243]='CHAB';    sts[243]='CHAB_BARD_CN1991';    lats[243]=37.7241163237;    lngs[243]=237.8806894582;    ves[243]=-17.42;    vns[243]=20.12;    vus[243]=0.07;    stddevNs[243]=0.32;    stddevEs[243]=0.4;    stddevUs[243]=1.02;    corrNEs[243]=-0.003; 
   ids[244]='CHAB';    sts[244]='CHAB_BARD_CN1991';    lats[244]=37.7241162909;    lngs[244]=237.8806894634;    ves[244]=-17.42;    vns[244]=20.12;    vus[244]=0.07;    stddevNs[244]=0.32;    stddevEs[244]=0.4;    stddevUs[244]=1.02;    corrNEs[244]=-0.003; 
   ids[245]='CHAB';    sts[245]='CHAB_BARD_CN1991';    lats[245]=37.7241162944;    lngs[245]=237.8806894682;    ves[245]=-17.42;    vns[245]=20.12;    vus[245]=0.07;    stddevNs[245]=0.32;    stddevEs[245]=0.4;    stddevUs[245]=1.02;    corrNEs[245]=-0.003; 
   ids[246]='CHAB';    sts[246]='CHAB_BARD_CN1991';    lats[246]=37.7241163169;    lngs[246]=237.8806893975;    ves[246]=-17.42;    vns[246]=20.12;    vus[246]=0.07;    stddevNs[246]=0.32;    stddevEs[246]=0.4;    stddevUs[246]=1.02;    corrNEs[246]=-0.003; 
   ids[247]='CHAB';    sts[247]='CHAB_BARD_CN1991';    lats[247]=37.7241163019;    lngs[247]=237.8806894296;    ves[247]=-17.42;    vns[247]=20.12;    vus[247]=0.07;    stddevNs[247]=0.32;    stddevEs[247]=0.4;    stddevUs[247]=1.02;    corrNEs[247]=-0.003; 
   ids[248]='CHMS';    sts[248]='CHMS_SCGN_CS1999';    lats[248]=34.6404624983;    lngs[248]=242.1722985987;    ves[248]=-12.75;    vns[248]=14.01;    vus[248]=1.96;    stddevNs[248]=0.21;    stddevEs[248]=0.29;    stddevUs[248]=0.89;    corrNEs[248]=-0.007; 
   ids[249]='CHUR';    sts[249]='Churchill';    lats[249]=5430049.26579;    lngs[249]=58.7590777165;    ves[249]=-0.34;    vns[249]=-733;    vus[249]=2.96;    stddevNs[249]=10.47;    stddevEs[249]=0.36;    stddevUs[249]=0.36;    corrNEs[249]=0.00099; 
   ids[250]='CHUR';    sts[250]='Churchill';    lats[250]=5430049.26684;    lngs[250]=58.7590776911;    ves[250]=-0.34;    vns[250]=-733;    vus[250]=2.96;    stddevNs[250]=10.47;    stddevEs[250]=0.36;    stddevUs[250]=0.36;    corrNEs[250]=0.00099; 
   ids[251]='CHUR';    sts[251]='Churchill';    lats[251]=5430049.26376;    lngs[251]=58.7590776500;    ves[251]=-0.34;    vns[251]=-733;    vus[251]=2.96;    stddevNs[251]=10.47;    stddevEs[251]=0.36;    stddevUs[251]=0.36;    corrNEs[251]=0.00099; 
   ids[252]='CHUR';    sts[252]='Churchill';    lats[252]=5430049.26283;    lngs[252]=58.7590777471;    ves[252]=-0.34;    vns[252]=-733;    vus[252]=2.96;    stddevNs[252]=10.47;    stddevEs[252]=0.36;    stddevUs[252]=0.36;    corrNEs[252]=0.00099; 
   ids[253]='CHUR';    sts[253]='Churchill';    lats[253]=5430049.26087;    lngs[253]=58.7590777120;    ves[253]=-0.34;    vns[253]=-733;    vus[253]=2.96;    stddevNs[253]=10.47;    stddevEs[253]=0.36;    stddevUs[253]=0.36;    corrNEs[253]=0.00099; 
   ids[254]='CHUR';    sts[254]='Churchill';    lats[254]=5430049.26233;    lngs[254]=58.7590777001;    ves[254]=-0.34;    vns[254]=-733;    vus[254]=2.96;    stddevNs[254]=10.47;    stddevEs[254]=0.36;    stddevUs[254]=0.36;    corrNEs[254]=0.00099; 
   ids[255]='CHZZ';    sts[255]='CHZZ_PNGA_OR1999';    lats[255]=45.4865155709;    lngs[255]=236.0218769907;    ves[255]=6.87;    vns[255]=9.58;    vus[255]=-1.28;    stddevNs[255]=0.15;    stddevEs[255]=0.25;    stddevUs[255]=0.51;    corrNEs[255]=-0.002; 
   ids[256]='CIRX';    sts[256]='CIRX_SCGN_CS2000';    lats[256]=34.1095540210;    lngs[256]=241.0627042404;    ves[256]=-29.52;    vns[256]=29.64;    vus[256]=-1.69;    stddevNs[256]=0.13;    stddevEs[256]=0.14;    stddevUs[256]=0.53;    corrNEs[256]=-0.018; 
   ids[257]='CLGO';    sts[257]='CLGO_AKDA_AK1998';    lats[257]=64.8737742433;    lngs[257]=212.1395132200;    ves[257]=2.79;    vns[257]=-4.88;    vus[257]=6.2;    stddevNs[257]=0.64;    stddevEs[257]=0.39;    stddevUs[257]=1.08;    corrNEs[257]=0.000; 
   ids[258]='CLGO';    sts[258]='CLGO_AKDA_AK1998';    lats[258]=64.8737742164;    lngs[258]=212.1395132378;    ves[258]=2.79;    vns[258]=-4.88;    vus[258]=6.2;    stddevNs[258]=0.64;    stddevEs[258]=0.39;    stddevUs[258]=1.08;    corrNEs[258]=0.000; 
   ids[259]='CLOV';    sts[259]='CLOV_BRGN_NV2006';    lats[259]=40.5583846019;    lngs[259]=245.1258115329;    ves[259]=-3.77;    vns[259]=-0.41;    vus[259]=-2.43;    stddevNs[259]=0.26;    stddevEs[259]=0.26;    stddevUs[259]=1.23;    corrNEs[259]=-0.002; 
   ids[260]='CNPP';    sts[260]='CNPP_SCGN_CS1999';    lats[260]=33.8576326900;    lngs[260]=242.3910804473;    ves[260]=-24.75;    vns[260]=24.96;    vus[260]=-1.65;    stddevNs[260]=0.14;    stddevEs[260]=0.14;    stddevUs[260]=0.47;    corrNEs[260]=-0.014; 
   ids[261]='COON';    sts[261]='COON_BRGN_UT1996';    lats[261]=40.6525914564;    lngs[261]=247.8789889854;    ves[261]=-2.67;    vns[261]=-0.12;    vus[261]=0.43;    stddevNs[261]=0.14;    stddevEs[261]=0.13;    stddevUs[261]=0.96;    corrNEs[261]=-0.008; 
   ids[262]='COPR';    sts[262]='COPR_SCGN_CS2001';    lats[262]=34.4149079290;    lngs[262]=240.1204744516;    ves[262]=-31.02;    vns[262]=29.06;    vus[262]=-0.41;    stddevNs[262]=0.18;    stddevEs[262]=0.17;    stddevUs[262]=0.59;    corrNEs[262]=-0.012; 
   ids[263]='CORV';    sts[263]='CORV_PNGA_OR1996';    lats[263]=44.5855355847;    lngs[263]=236.6953958811;    ves[263]=2.59;    vns[263]=6.8;    vus[263]=-1.27;    stddevNs[263]=0.18;    stddevEs[263]=0.2;    stddevUs[263]=0.6;    corrNEs[263]=-0.007; 
   ids[264]='CPBN';    sts[264]='CPBN_SCGN_CS2000';    lats[264]=35.0717347652;    lngs[264]=242.4269769728;    ves[264]=-9.48;    vns[264]=12.19;    vus[264]=-1.34;    stddevNs[264]=0.17;    stddevEs[264]=0.17;    stddevUs[264]=0.54;    corrNEs[264]=-0.009; 
   ids[265]='CPXF';    sts[265]='PackForestWA2001';    lats[265]=46.8400854467;    lngs[265]=237.7434815203;    ves[265]=3.04;    vns[265]=4.06;    vus[265]=-0.5;    stddevNs[265]=0.18;    stddevEs[265]=0.2;    stddevUs[265]=0.96;    corrNEs[265]=-0.006; 
   ids[266]='CPXX';    sts[266]='CPXX_PNGA_WA2001';    lats[266]=46.8400854591;    lngs[266]=237.7434814990;    ves[266]=3.04;    vns[266]=4.06;    vus[266]=-0.5;    stddevNs[266]=0.18;    stddevEs[266]=0.2;    stddevUs[266]=0.96;    corrNEs[266]=-0.006; 
   ids[267]='CPXX';    sts[267]='CPXX_PNGA_WA2001';    lats[267]=46.8400854560;    lngs[267]=237.7434814758;    ves[267]=3.04;    vns[267]=4.06;    vus[267]=-0.5;    stddevNs[267]=0.18;    stddevEs[267]=0.2;    stddevUs[267]=0.96;    corrNEs[267]=-0.006; 
   ids[268]='CPXX';    sts[268]='CPXX_PNGA_WA2001';    lats[268]=46.8400854972;    lngs[268]=237.7434814659;    ves[268]=3.04;    vns[268]=4.06;    vus[268]=-0.5;    stddevNs[268]=0.18;    stddevEs[268]=0.2;    stddevUs[268]=0.96;    corrNEs[268]=-0.006; 
   ids[269]='CRBT';    sts[269]='CRBT_SCGN_CN2001';    lats[269]=35.7916117510;    lngs[269]=239.2492457416;    ves[269]=-27.09;    vns[269]=32.63;    vus[269]=0.85;    stddevNs[269]=0.17;    stddevEs[269]=0.17;    stddevUs[269]=1.13;    corrNEs[269]=-0.012; 
   ids[270]='CRBT';    sts[270]='CRBT_SCGN_CN2001';    lats[270]=35.7916118126;    lngs[270]=239.2492455832;    ves[270]=-27.09;    vns[270]=32.63;    vus[270]=0.85;    stddevNs[270]=0.17;    stddevEs[270]=0.17;    stddevUs[270]=1.13;    corrNEs[270]=-0.012; 
   ids[271]='CRHS';    sts[271]='CRHS_SCGN_CS1999';    lats[271]=33.8235046382;    lngs[271]=241.7272338235;    ves[271]=-29.14;    vns[271]=28.05;    vus[271]=-0.8;    stddevNs[271]=0.27;    stddevEs[271]=0.22;    stddevUs[271]=0.62;    corrNEs[271]=-0.005; 
   ids[272]='CRRS';    sts[272]='CRRS_SCGN_CS1998';    lats[272]=33.0698083039;    lngs[272]=244.2649588218;    ves[272]=-14.19;    vns[272]=18.21;    vus[272]=-2.23;    stddevNs[272]=0.15;    stddevEs[272]=0.14;    stddevUs[272]=0.63;    corrNEs[272]=-0.011; 
   ids[273]='CRRS';    sts[273]='CRRS_SCGN_CS1998';    lats[273]=33.0698082950;    lngs[273]=244.2649588265;    ves[273]=-14.19;    vns[273]=18.21;    vus[273]=-2.23;    stddevNs[273]=0.15;    stddevEs[273]=0.14;    stddevUs[273]=0.63;    corrNEs[273]=-0.011; 
   ids[274]='CRRS';    sts[274]='CRRS_SCGN_CS1998';    lats[274]=33.0698083000;    lngs[274]=244.2649588224;    ves[274]=-14.19;    vns[274]=18.21;    vus[274]=-2.23;    stddevNs[274]=0.15;    stddevEs[274]=0.14;    stddevUs[274]=0.63;    corrNEs[274]=-0.011; 
   ids[275]='CRU1';    sts[275]='CRU1_SCGN_CS2000';    lats[275]=34.0292604095;    lngs[275]=240.2151886077;    ves[275]=-31.25;    vns[275]=33.76;    vus[275]=-2.3;    stddevNs[275]=0.17;    stddevEs[275]=0.14;    stddevUs[275]=0.74;    corrNEs[275]=-0.014; 
   ids[276]='CSCI';    sts[276]='CSCI_SCGN_CS2000';    lats[276]=34.1684034639;    lngs[276]=240.9610192515;    ves[276]=-29.71;    vns[276]=30.13;    vus[276]=-2.1;    stddevNs[276]=0.14;    stddevEs[276]=0.14;    stddevUs[276]=0.53;    corrNEs[276]=-0.016; 
   ids[277]='CSHR';    sts[277]='MR_CSCHRMNWA2007';    lats[277]=46.8707191114;    lngs[277]=238.2675784031;    ves[277]=2.62;    vns[277]=2.65;    vus[277]=-3.12;    stddevNs[277]=0.67;    stddevEs[277]=1.43;    stddevUs[277]=1.47;    corrNEs[277]=0.003; 
   ids[278]='CSST';    sts[278]='CSST_SGCN_CS2000';    lats[278]=34.4080907817;    lngs[278]=240.6287502034;    ves[278]=-29.94;    vns[278]=26.31;    vus[278]=-2.46;    stddevNs[278]=0.14;    stddevEs[278]=0.15;    stddevUs[278]=0.57;    corrNEs[278]=-0.016; 
   ids[279]='CTDM';    sts[279]='CTDM_SCGN_CS2001';    lats[279]=34.5165498143;    lngs[279]=241.3867894582;    ves[279]=-24.49;    vns[279]=21.18;    vus[279]=-0.78;    stddevNs[279]=0.17;    stddevEs[279]=0.15;    stddevUs[279]=0.49;    corrNEs[279]=-0.012; 
   ids[280]='CTMS';    sts[280]='CTMS_SCGN_CS1998';    lats[280]=34.1241010664;    lngs[280]=243.6295741991;    ves[280]=-7.13;    vns[280]=10.88;    vus[280]=-1.22;    stddevNs[280]=0.14;    stddevEs[280]=0.13;    stddevUs[280]=0.49;    corrNEs[280]=-0.013; 
   ids[281]='DDSN';    sts[281]='DDSN_PNGA_OR1999';    lats[281]=43.1187911971;    lngs[281]=236.7557587154;    ves[281]=-0.37;    vns[281]=7.19;    vus[281]=-0.97;    stddevNs[281]=0.16;    stddevEs[281]=0.25;    stddevUs[281]=0.68;    corrNEs[281]=-0.006; 
   ids[282]='DRAO';    sts[282]='Dominion_Radio_A';    lats[282]=49.3226188902;    lngs[282]=240.3750177605;    ves[282]=0.66;    vns[282]=0.59;    vus[282]=0.16;    stddevNs[282]=0.16;    stddevEs[282]=0.11;    stddevUs[282]=0.45;    corrNEs[282]=-0.010; 
   ids[283]='DSHS';    sts[283]='DSHS_SCGN_CS1999';    lats[283]=34.0239323820;    lngs[283]=241.6514581931;    ves[283]=-27.16;    vns[283]=25.8;    vus[283]=0.1;    stddevNs[283]=0.24;    stddevEs[283]=0.19;    stddevUs[283]=0.78;    corrNEs[283]=-0.008; 
   ids[284]='DSHS';    sts[284]='DSHS_SCGN_CS1999';    lats[284]=34.0239323538;    lngs[284]=241.6514582102;    ves[284]=-27.16;    vns[284]=25.8;    vus[284]=0.1;    stddevNs[284]=0.24;    stddevEs[284]=0.19;    stddevUs[284]=0.78;    corrNEs[284]=-0.008; 
   ids[285]='DSSC';    sts[285]='DSSC_SCGN_CS1998';    lats[285]=33.7333316004;    lngs[285]=243.2879104072;    ves[285]=-17.95;    vns[285]=19.2;    vus[285]=-1.12;    stddevNs[285]=0.14;    stddevEs[285]=0.16;    stddevUs[285]=0.46;    corrNEs[285]=-0.013; 
   ids[286]='DUBO';    sts[286]='Lac_du_Bonnet';    lats[286]=50.2588088794;    lngs[286]=264.1338193998;    ves[286]=-0.47;    vns[286]=-1.35;    vus[286]=0.41;    stddevNs[286]=0.17;    stddevEs[286]=0.13;    stddevUs[286]=0.7;    corrNEs[286]=0.003; 
   ids[287]='ECCO';    sts[287]='ECCO_SCGN_CS1999';    lats[287]=33.8867531851;    lngs[287]=241.6709735857;    ves[287]=-28.43;    vns[287]=28.41;    vus[287]=1.69;    stddevNs[287]=0.23;    stddevEs[287]=0.26;    stddevUs[287]=0.81;    corrNEs[287]=-0.008; 
   ids[288]='ECFS';    sts[288]='ECFS_SCGN_CS2001';    lats[288]=33.6476845341;    lngs[288]=242.5883055074;    ves[288]=-25.94;    vns[288]=26.16;    vus[288]=-1.94;    stddevNs[288]=0.14;    stddevEs[288]=0.14;    stddevUs[288]=0.44;    corrNEs[288]=-0.015; 
   ids[289]='ECHO';    sts[289]='ECHO_BRGN_NV1999';    lats[289]=37.9155337433;    lngs[289]=245.7357524914;    ves[289]=-3.85;    vns[289]=-0.12;    vus[289]=-1;    stddevNs[289]=0.16;    stddevEs[289]=0.14;    stddevUs[289]=0.97;    corrNEs[289]=-0.009; 
   ids[290]='ECSD';    sts[290]='EROS_USGS_SD2006';    lats[290]=43.7336996281;    lngs[290]=263.3859936065;    ves[290]=-0.99;    vns[290]=-0.6;    vus[290]=-5.8;    stddevNs[290]=0.24;    stddevEs[290]=0.23;    stddevUs[290]=0.92;    corrNEs[290]=-0.000; 
   ids[291]='EDPP';    sts[291]='EDPP_SCGN_CS1999';    lats[291]=34.9461947280;    lngs[291]=241.1695914260;    ves[291]=-17.27;    vns[291]=16.06;    vus[291]=-0.22;    stddevNs[291]=0.16;    stddevEs[291]=0.13;    stddevUs[291]=0.57;    corrNEs[291]=-0.015; 
   ids[292]='EGAN';    sts[292]='EGAN_BRGN_NV1996';    lats[292]=39.3452437786;    lngs[292]=245.0611455072;    ves[292]=-4.92;    vns[292]=-0.13;    vus[292]=-1.81;    stddevNs[292]=0.18;    stddevEs[292]=0.13;    stddevUs[292]=0.96;    corrNEs[292]=-0.009; 
   ids[293]='ELKO';    sts[293]='ELKO_BRGN_NV1997';    lats[293]=40.9146905076;    lngs[293]=244.1828018879;    ves[293]=-4.46;    vns[293]=0.2;    vus[293]=-0.48;    stddevNs[293]=0.14;    stddevEs[293]=0.14;    stddevUs[293]=0.87;    corrNEs[293]=-0.010; 
   ids[294]='EOUT';    sts[294]='EOUT_EBRY_UT1998';    lats[294]=41.2531966600;    lngs[294]=248.0710915584;    ves[294]=-2;    vns[294]=-0.32;    vus[294]=-0.38;    stddevNs[294]=0.13;    stddevEs[294]=0.16;    stddevUs[294]=0.54;    corrNEs[294]=-0.006; 
   ids[295]='EWPP';    sts[295]='EWPP_SCGN_CS1999';    lats[295]=34.1041986689;    lngs[295]=242.4744148664;    ves[295]=-23.68;    vns[295]=22.24;    vus[295]=-2.11;    stddevNs[295]=0.14;    stddevEs[295]=0.15;    stddevUs[295]=0.54;    corrNEs[295]=-0.014; 
   ids[296]='EYAC';    sts[296]='EyacSkiAr_AK2005';    lats[296]=60.5487049852;    lngs[296]=214.2501412316;    ves[296]=-12.9;    vns[296]=28.12;    vus[296]=1.97;    stddevNs[296]=0.63;    stddevEs[296]=0.75;    stddevUs[296]=1.87;    corrNEs[296]=-0.005; 
   ids[297]='EYAC';    sts[297]='EyacSkiAr_AK2005';    lats[297]=60.5487049730;    lngs[297]=214.2501412469;    ves[297]=-12.9;    vns[297]=28.12;    vus[297]=1.97;    stddevNs[297]=0.63;    stddevEs[297]=0.75;    stddevUs[297]=1.87;    corrNEs[297]=-0.005; 
   ids[298]='EYAC';    sts[298]='EyacSkiAr_AK2005';    lats[298]=60.5487049620;    lngs[298]=214.2501412161;    ves[298]=-12.9;    vns[298]=28.12;    vus[298]=1.97;    stddevNs[298]=0.63;    stddevEs[298]=0.75;    stddevUs[298]=1.87;    corrNEs[298]=-0.005; 
   ids[299]='FAIR';    sts[299]='Gilmore_Creek_Ob';    lats[299]=64.9779987054;    lngs[299]=212.5007609282;    ves[299]=1.14;    vns[299]=-5.19;    vus[299]=2.79;    stddevNs[299]=0.41;    stddevEs[299]=0.44;    stddevUs[299]=0.83;    corrNEs[299]=0.001; 
   ids[300]='FERN';    sts[300]='FERN_BRGN_AZ1999';    lats[300]=35.3418721215;    lngs[300]=247.5452631756;    ves[300]=-2.43;    vns[300]=0.27;    vus[300]=-1.37;    stddevNs[300]=0.15;    stddevEs[300]=0.15;    stddevUs[300]=0.88;    corrNEs[300]=-0.008; 
   ids[301]='FGST';    sts[301]='FGST_SCGN_CS2000';    lats[301]=34.7330100158;    lngs[301]=239.9906185769;    ves[301]=-31.04;    vns[301]=30.72;    vus[301]=-1.41;    stddevNs[301]=0.19;    stddevEs[301]=0.18;    stddevUs[301]=0.73;    corrNEs[301]=-0.010; 
   ids[302]='FLIN';    sts[302]='Flin_Flon';    lats[302]=5184056.22589;    lngs[302]=54.7255839748;    ves[302]=-1.07;    vns[302]=-891;    vus[302]=-0.44;    stddevNs[302]=1.07;    stddevEs[302]=0.13;    stddevUs[302]=0.13;    corrNEs[302]=0.00059; 
   ids[303]='FMVT';    sts[303]='FMVT_SCGN_CS2000';    lats[303]=34.3563367133;    lngs[303]=241.1159684010;    ves[303]=-28.65;    vns[303]=28.11;    vus[303]=-2.98;    stddevNs[303]=0.16;    stddevEs[303]=0.16;    stddevUs[303]=0.45;    corrNEs[303]=-0.012; 
   ids[304]='FOOT';    sts[304]='FOOT_BRGN_UT1996';    lats[304]=39.3693969002;    lngs[304]=246.1946270991;    ves[304]=-3.8;    vns[304]=-0.15;    vus[304]=-0.62;    stddevNs[304]=0.16;    stddevEs[304]=0.13;    stddevUs[304]=0.88;    corrNEs[304]=-0.010; 
   ids[305]='FORE';    sts[305]='FORE_BRGN_UT2007';    lats[305]=40.5118966604;    lngs[305]=248.6196721934;    ves[305]=-1.51;    vns[305]=0.15;    vus[305]=-2.57;    stddevNs[305]=0.2;    stddevEs[305]=0.19;    stddevUs[305]=0.69;    corrNEs[305]=-0.003; 
   ids[306]='FRED';    sts[306]='FRED_BRGN_AZ1999';    lats[306]=36.9883320319;    lngs[306]=247.5007829381;    ves[306]=-2.06;    vns[306]=-0.07;    vus[306]=-1.77;    stddevNs[306]=0.14;    stddevEs[306]=0.14;    stddevUs[306]=0.96;    corrNEs[306]=-0.009; 
   ids[307]='GARL';    sts[307]='GARL_BRGN_NV1996';    lats[307]=40.4165267276;    lngs[307]=240.6445430738;    ves[307]=-5.3;    vns[307]=2.73;    vus[307]=-2.14;    stddevNs[307]=0.23;    stddevEs[307]=0.25;    stddevUs[307]=1.35;    corrNEs[307]=-0.005; 
   ids[308]='GARL';    sts[308]='GARL_BRGN_NV1996';    lats[308]=40.4165267382;    lngs[308]=240.6445430779;    ves[308]=-5.3;    vns[308]=2.73;    vus[308]=-2.14;    stddevNs[308]=0.23;    stddevEs[308]=0.25;    stddevUs[308]=1.35;    corrNEs[308]=-0.005; 
   ids[309]='GLRS';    sts[309]='GLRS_SCGN_CS1999';    lats[309]=33.2748126631;    lngs[309]=244.4786287330;    ves[309]=-6.74;    vns[309]=1.65;    vus[309]=-1.41;    stddevNs[309]=0.26;    stddevEs[309]=0.14;    stddevUs[309]=0.54;    corrNEs[309]=-0.007; 
   ids[310]='GMPK';    sts[310]='GMPK_SCGN_CS2000';    lats[310]=33.0510853562;    lngs[310]=245.1726548427;    ves[310]=-3.32;    vns[310]=2.14;    vus[310]=-2;    stddevNs[310]=0.15;    stddevEs[310]=0.13;    stddevUs[310]=0.53;    corrNEs[310]=-0.012; 
   ids[311]='GMRC';    sts[311]='GMRC_SCGN_CS1999';    lats[311]=34.7839977123;    lngs[311]=244.3397710179;    ves[311]=-3.86;    vns[311]=0.63;    vus[311]=-1.02;    stddevNs[311]=0.13;    stddevEs[311]=0.13;    stddevUs[311]=0.47;    corrNEs[311]=-0.015; 
   ids[312]='GNPS';    sts[312]='GNPS_SCGN_CS1999';    lats[312]=34.3085564545;    lngs[312]=245.8105502725;    ves[312]=-2.63;    vns[312]=1.93;    vus[312]=-0.46;    stddevNs[312]=0.15;    stddevEs[312]=0.15;    stddevUs[312]=0.52;    corrNEs[312]=-0.009; 
   ids[313]='GOBS';    sts[313]='GOBS_PNGA_WA1997';    lats[313]=45.8388188050;    lngs[313]=239.1853240630;    ves[313]=0.53;    vns[313]=2.72;    vus[313]=-1.32;    stddevNs[313]=0.18;    stddevEs[313]=0.19;    stddevUs[313]=0.61;    corrNEs[313]=-0.005; 
   ids[314]='GODE';    sts[314]='GGAO_Greenbelt';    lats[314]=39.0217274692;    lngs[314]=283.1731698483;    ves[314]=0.89;    vns[314]=-0.53;    vus[314]=1.76;    stddevNs[314]=0.18;    stddevEs[314]=0.19;    stddevUs[314]=0.99;    corrNEs[314]=0.003; 
   ids[315]='GOGA';    sts[315]='Godfrey___GA2006';    lats[315]=33.4147473447;    lngs[315]=276.5267767336;    ves[315]=-0.59;    vns[315]=-1.14;    vus[315]=-3.68;    stddevNs[315]=0.45;    stddevEs[315]=0.58;    stddevUs[315]=1;    corrNEs[315]=-0.000; 
   ids[316]='GOLD';    sts[316]='GoldstoneDSSCIGN';    lats[316]=35.4251561589;    lngs[316]=243.1107495800;    ves[316]=-6.32;    vns[316]=6.08;    vus[316]=-1.22;    stddevNs[316]=0.11;    stddevEs[316]=0.11;    stddevUs[316]=0.46;    corrNEs[316]=-0.019; 
   ids[317]='GOSH';    sts[317]='GOSH_BRGN_NV1996';    lats[317]=40.6401687410;    lngs[317]=245.8203023147;    ves[317]=-3.74;    vns[317]=-0.37;    vus[317]=-0.63;    stddevNs[317]=0.14;    stddevEs[317]=0.13;    stddevUs[317]=0.9;    corrNEs[317]=-0.010; 
   ids[318]='GR8R';    sts[318]='GR8R_BARD_CN2008';    lats[318]=36.3990154924;    lngs[318]=239.5842692987;    ves[318]=-9.07;    vns[318]=11.65;    vus[318]=1.87;    stddevNs[318]=0.43;    stddevEs[318]=0.44;    stddevUs[318]=1.38;    corrNEs[318]=-0.002; 
   ids[319]='GR8V';    sts[319]='GR8V_BARD_CN2003';    lats[319]=36.3990088408;    lngs[319]=239.5842278350;    ves[319]=-8.33;    vns[319]=10.08;    vus[319]=-1.39;    stddevNs[319]=0.38;    stddevEs[319]=1.5;    stddevUs[319]=1.06;    corrNEs[319]=-0.001; 
   ids[320]='GR8V';    sts[320]='GR8V_BARD_CN2003';    lats[320]=36.3990087760;    lngs[320]=239.5842277266;    ves[320]=-8.33;    vns[320]=10.08;    vus[320]=-1.39;    stddevNs[320]=0.38;    stddevEs[320]=1.5;    stddevUs[320]=1.06;    corrNEs[320]=-0.001; 
   ids[321]='GRNR';    sts[321]='Garner_Permanent';    lats[321]=63.8357588850;    lngs[321]=211.0216618392;    ves[321]=-1.28;    vns[321]=-1.64;    vus[321]=1.79;    stddevNs[321]=0.41;    stddevEs[321]=0.2;    stddevUs[321]=0.88;    corrNEs[321]=0.001; 
   ids[322]='GRNX';    sts[322]='GRNX_AKDA_AK2004';    lats[322]=63.8354998148;    lngs[322]=211.0218078654;    ves[322]=-1.48;    vns[322]=-1.49;    vus[322]=4.26;    stddevNs[322]=0.33;    stddevEs[322]=0.3;    stddevUs[322]=0.78;    corrNEs[322]=0.001; 
   ids[323]='GTRG';    sts[323]='GTRG_EBRY_ID1999';    lats[323]=43.2440917948;    lngs[323]=246.7587871944;    ves[323]=-2.62;    vns[323]=-0.61;    vus[323]=-1.42;    stddevNs[323]=0.13;    stddevEs[323]=0.12;    stddevUs[323]=0.47;    corrNEs[323]=-0.010; 
   ids[324]='GUS2';    sts[324]='Gustavus_2__CORS';    lats[324]=58.4177572031;    lngs[324]=224.3029490804;    ves[324]=-0.29;    vns[324]=2.06;    vus[324]=20.41;    stddevNs[324]=0.28;    stddevEs[324]=0.19;    stddevUs[324]=1;    corrNEs[324]=-0.001; 
   ids[325]='HCES';    sts[325]='HCES_GAMA_TN2000';    lats[325]=3758032.70197;    lngs[325]=36.3326046946;    ves[325]=-1.24;    vns[325]=-456;    vus[325]=-0.59;    stddevNs[325]=-7.8;    stddevEs[325]=1.27;    stddevUs[325]=1.74;    corrNEs[325]=0.00213; 
   ids[326]='HCES';    sts[326]='HCES_GAMA_TN2000';    lats[326]=3758032.70488;    lngs[326]=36.3326047212;    ves[326]=-1.24;    vns[326]=-456;    vus[326]=-0.59;    stddevNs[326]=-7.8;    stddevEs[326]=1.27;    stddevUs[326]=1.74;    corrNEs[326]=0.00213; 
   ids[327]='HCMN';    sts[327]='HCMN_SCGN_CS1999';    lats[327]=34.7547740200;    lngs[327]=243.5699250298;    ves[327]=-8.17;    vns[327]=4.36;    vus[327]=-1.52;    stddevNs[327]=0.18;    stddevEs[327]=0.14;    stddevUs[327]=0.41;    corrNEs[327]=-0.010; 
   ids[328]='HCRO';    sts[328]='HCRO_BARD_CN2003';    lats[328]=40.8159175753;    lngs[328]=238.5301011394;    ves[328]=-7.19;    vns[328]=5.71;    vus[328]=-1.97;    stddevNs[328]=0.18;    stddevEs[328]=0.3;    stddevUs[328]=0.98;    corrNEs[328]=-0.005; 
   ids[329]='HDIL';    sts[329]='MackinawFWIL2005';    lats[329]=4125206.85585;    lngs[329]=40.5558857312;    ves[329]=-0.36;    vns[329]=-855;    vus[329]=-0.61;    stddevNs[329]=-5;    stddevEs[329]=0.38;    stddevUs[329]=0.48;    corrNEs[329]=0.00136; 
   ids[330]='HEBE';    sts[330]='HEBE_BRGN_UT1996';    lats[330]=40.5140990947;    lngs[330]=248.6272824124;    ves[330]=-1.59;    vns[330]=-0.34;    vus[330]=0.43;    stddevNs[330]=0.26;    stddevEs[330]=0.23;    stddevUs[330]=1.59;    corrNEs[330]=0.001; 
   ids[331]='HIVI';    sts[331]='HighVistaCCSCIGN';    lats[331]=34.7599201164;    lngs[331]=242.2005461492;    ves[331]=-14.5;    vns[331]=15.08;    vus[331]=-1.24;    stddevNs[331]=0.38;    stddevEs[331]=0.59;    stddevUs[331]=1.44;    corrNEs[331]=-0.003; 
   ids[332]='HLID';    sts[332]='HLID_EBRY_ID1996';    lats[332]=43.5625805459;    lngs[332]=245.5859550538;    ves[332]=-2.27;    vns[332]=-0.4;    vus[332]=-2.23;    stddevNs[332]=0.13;    stddevEs[332]=0.12;    stddevUs[332]=0.53;    corrNEs[332]=-0.013; 
   ids[333]='HNPS';    sts[333]='HNPS_SCGN_CS1999';    lats[333]=33.7050061920;    lngs[333]=244.3646648616;    ves[333]=-6.01;    vns[333]=2.26;    vus[333]=-2.19;    stddevNs[333]=0.13;    stddevEs[333]=0.13;    stddevUs[333]=0.47;    corrNEs[333]=-0.014; 
   ids[334]='HOGS';    sts[334]='HOGS_SCGN_CN2001';    lats[334]=35.8667148398;    lngs[334]=239.5205039307;    ves[334]=-23.68;    vns[334]=27.98;    vus[334]=2.82;    stddevNs[334]=0.27;    stddevEs[334]=0.22;    stddevUs[334]=1.13;    corrNEs[334]=-0.006; 
   ids[335]='HOGS';    sts[335]='HOGS_SCGN_CN2001';    lats[335]=35.8667156230;    lngs[335]=239.5205032034;    ves[335]=-23.68;    vns[335]=27.98;    vus[335]=2.82;    stddevNs[335]=0.27;    stddevEs[335]=0.22;    stddevUs[335]=1.13;    corrNEs[335]=-0.006; 
   ids[336]='HOLM';    sts[336]='Holman';    lats[336]=-1867623.65279;    lngs[336]=5998639.54909;    ves[336]=-643;    vns[336]=-500;    vus[336]=-0.05;    stddevNs[336]=0.89;    stddevEs[336]=2.25;    stddevUs[336]=0.29;    corrNEs[336]=0.00017; 
   ids[337]='HUNT';    sts[337]='HUNT_SCGN_CN2001';    lats[337]=35.8808118484;    lngs[337]=239.5976172325;    ves[337]=-17.02;    vns[337]=20.25;    vus[337]=1.09;    stddevNs[337]=0.15;    stddevEs[337]=0.28;    stddevUs[337]=1.01;    corrNEs[337]=-0.007; 
   ids[338]='HUNT';    sts[338]='HUNT_SCGN_CN2001';    lats[338]=35.8808127990;    lngs[338]=239.5976160473;    ves[338]=-17.02;    vns[338]=20.25;    vus[338]=1.09;    stddevNs[338]=0.15;    stddevEs[338]=0.28;    stddevUs[338]=1.01;    corrNEs[338]=-0.007; 
   ids[339]='HVWY';    sts[339]='HVWY_EBRY_WY1999';    lats[339]=44.6136014512;    lngs[339]=249.4640376208;    ves[339]=-10.18;    vns[339]=5.06;    vus[339]=18.24;    stddevNs[339]=0.69;    stddevEs[339]=1.13;    stddevUs[339]=1.58;    corrNEs[339]=0.000; 
   ids[340]='HVYS';    sts[340]='HVYS_SCGN_CS2000';    lats[340]=34.4412313643;    lngs[340]=240.8124587437;    ves[340]=-28.96;    vns[340]=24.12;    vus[340]=-2.79;    stddevNs[340]=0.15;    stddevEs[340]=0.21;    stddevUs[340]=0.52;    corrNEs[340]=-0.010; 
   ids[341]='HWUT';    sts[341]='Hardware_RaSCIGN';    lats[341]=41.6072408525;    lngs[341]=248.4348845013;    ves[341]=-1.16;    vns[341]=-0.07;    vus[341]=-2.55;    stddevNs[341]=0.16;    stddevEs[341]=0.17;    stddevUs[341]=0.64;    corrNEs[341]=-0.004; 
   ids[342]='I40A';    sts[342]='I40A_SCGN_CS2000';    lats[342]=34.7273249374;    lngs[342]=244.0885514374;    ves[342]=-3.89;    vns[342]=1;    vus[342]=-0.64;    stddevNs[342]=0.15;    stddevEs[342]=0.15;    stddevUs[342]=0.5;    corrNEs[342]=-0.010; 
   ids[343]='IID2';    sts[343]='IID2_SCGN_CS1998';    lats[343]=32.7061692317;    lngs[343]=244.9681972509;    ves[343]=-3.71;    vns[343]=2.47;    vus[343]=-3.86;    stddevNs[343]=0.14;    stddevEs[343]=0.26;    stddevUs[343]=0.69;    corrNEs[343]=-0.006; 
   ids[344]='IMPS';    sts[344]='IMPS_SCGN_CS1999';    lats[344]=34.1575634500;    lngs[344]=244.8549003136;    ves[344]=-3.77;    vns[344]=1.05;    vus[344]=-1.91;    stddevNs[344]=0.13;    stddevEs[344]=0.14;    stddevUs[344]=0.47;    corrNEs[344]=-0.013; 
   ids[345]='INVK';    sts[345]='Inuvik';    lats[345]=-1714393.05250;    lngs[345]=5903844.67149;    ves[345]=-522;    vns[345]=-490;    vus[345]=-0.29;    stddevNs[345]=1.04;    stddevEs[345]=-1.09;    stddevUs[345]=0.33;    corrNEs[345]=0.00021; 
   ids[346]='INVK';    sts[346]='Inuvik';    lats[346]=-1714393.05217;    lngs[346]=5903844.68561;    ves[346]=-522;    vns[346]=-490;    vus[346]=-0.29;    stddevNs[346]=1.04;    stddevEs[346]=-1.09;    stddevUs[346]=0.33;    corrNEs[346]=0.00021; 
   ids[347]='INVK';    sts[347]='Inuvik';    lats[347]=-1714393.04622;    lngs[347]=5903844.68507;    ves[347]=-522;    vns[347]=-490;    vus[347]=-0.29;    stddevNs[347]=1.04;    stddevEs[347]=-1.09;    stddevUs[347]=0.33;    corrNEs[347]=0.00021; 
   ids[348]='ISLK';    sts[348]='ISLK_SCGN_CS1999';    lats[348]=35.6622711685;    lngs[348]=241.5256991581;    ves[348]=-11.47;    vns[348]=10.13;    vus[348]=0.18;    stddevNs[348]=0.13;    stddevEs[348]=0.18;    stddevUs[348]=0.7;    corrNEs[348]=-0.014; 
   ids[349]='IVCO';    sts[349]='IVCO_SCGN_CS2000';    lats[349]=32.8293824861;    lngs[349]=244.4932102413;    ves[349]=-15.31;    vns[349]=20.11;    vus[349]=-4.17;    stddevNs[349]=0.37;    stddevEs[349]=0.37;    stddevUs[349]=0.75;    corrNEs[349]=-0.002; 
   ids[350]='JCT1';    sts[350]='Junction__TX2005';    lats[350]=30.4794075071;    lngs[350]=260.1989253742;    ves[350]=-1.35;    vns[350]=-0.57;    vus[350]=-0.48;    stddevNs[350]=0.28;    stddevEs[350]=0.29;    stddevUs[350]=0.95;    corrNEs[350]=0.003; 
   ids[351]='JFWS';    sts[351]='JewelFarm_WI2006';    lats[351]=42.9142713916;    lngs[351]=269.7518941438;    ves[351]=-1.8;    vns[351]=-2.29;    vus[351]=-6.34;    stddevNs[351]=0.28;    stddevEs[351]=0.47;    stddevUs[351]=0.86;    corrNEs[351]=0.000; 
   ids[352]='JNPR';    sts[352]='JNPR_BARD_CN1997';    lats[352]=37.7717441420;    lngs[352]=240.9152636604;    ves[352]=-9.95;    vns[352]=9.25;    vus[352]=-2.32;    stddevNs[352]=0.51;    stddevEs[352]=0.48;    stddevUs[352]=0.96;    corrNEs[352]=-0.010; 
   ids[353]='JNPR';    sts[353]='JNPR_BARD_CN1997';    lats[353]=37.7717441653;    lngs[353]=240.9152636653;    ves[353]=-9.95;    vns[353]=9.25;    vus[353]=-2.32;    stddevNs[353]=0.51;    stddevEs[353]=0.48;    stddevUs[353]=0.96;    corrNEs[353]=-0.010; 
   ids[354]='JNPX';    sts[354]='JNPX_GPS';    lats[354]=-4413040.10242;    lngs[354]=3886862.99870;    ves[354]=-526;    vns[354]=-389;    vus[354]=9.25;    stddevNs[354]=-9.95;    stddevEs[354]=-2.32;    stddevUs[354]=0.51;    corrNEs[354]=0.00048; 
   ids[355]='KAGA';    sts[355]='KangiaNorthGrenL';    lats[355]=69.2223004673;    lngs[355]=310.1853742045;    ves[355]=-2.74;    vns[355]=-0.36;    vus[355]=15.24;    stddevNs[355]=0.69;    stddevEs[355]=0.79;    stddevUs[355]=1.44;    corrNEs[355]=0.006; 
   ids[356]='KAGA';    sts[356]='KangiaNorthGrenL';    lats[356]=69.2223004664;    lngs[356]=310.1853742945;    ves[356]=-2.74;    vns[356]=-0.36;    vus[356]=15.24;    stddevNs[356]=0.69;    stddevEs[356]=0.79;    stddevUs[356]=1.44;    corrNEs[356]=0.006; 
   ids[357]='KAGA';    sts[357]='KangiaNorthGrenL';    lats[357]=69.2223004537;    lngs[357]=310.1853742945;    ves[357]=-2.74;    vns[357]=-0.36;    vus[357]=15.24;    stddevNs[357]=0.69;    stddevEs[357]=0.79;    stddevUs[357]=1.44;    corrNEs[357]=0.006; 
   ids[358]='KBRC';    sts[358]='KBRC_SCGN_CS2000';    lats[358]=34.3985282045;    lngs[358]=240.9918021104;    ves[358]=-27.43;    vns[358]=27.89;    vus[358]=-3.51;    stddevNs[358]=0.54;    stddevEs[358]=0.32;    stddevUs[358]=0.56;    corrNEs[358]=-0.002; 
   ids[359]='KBRC';    sts[359]='KBRC_SCGN_CS2000';    lats[359]=34.3985281381;    lngs[359]=240.9918020909;    ves[359]=-27.43;    vns[359]=27.89;    vus[359]=-3.51;    stddevNs[359]=0.54;    stddevEs[359]=0.32;    stddevUs[359]=0.56;    corrNEs[359]=-0.002; 
   ids[360]='KBRC';    sts[360]='KBRC_SCGN_CS2000';    lats[360]=34.3985281395;    lngs[360]=240.9918020901;    ves[360]=-27.43;    vns[360]=27.89;    vus[360]=-3.51;    stddevNs[360]=0.54;    stddevEs[360]=0.32;    stddevUs[360]=0.56;    corrNEs[360]=-0.002; 
   ids[361]='KELS';    sts[361]='KELS_PNGA_WA1997';    lats[361]=46.1181776589;    lngs[361]=237.1039312505;    ves[361]=3.78;    vns[361]=5.92;    vus[361]=-0.65;    stddevNs[361]=0.26;    stddevEs[361]=0.28;    stddevUs[361]=0.77;    corrNEs[361]=-0.002; 
   ids[362]='KELY';    sts[362]='Kangerlussuaq';    lats[362]=66.9874183856;    lngs[362]=309.0551617381;    ves[362]=-0.35;    vns[362]=-0.58;    vus[362]=1.17;    stddevNs[362]=0.21;    stddevEs[362]=0.18;    stddevUs[362]=0.68;    corrNEs[362]=0.012; 
   ids[363]='KOD1';    sts[363]='Kodiak_1____CORS';    lats[363]=57.6176927941;    lngs[363]=207.8065701976;    ves[363]=-9.71;    vns[363]=19.91;    vus[363]=6.87;    stddevNs[363]=0.49;    stddevEs[363]=0.45;    stddevUs[363]=1.07;    corrNEs[363]=-0.005; 
   ids[364]='KOD1';    sts[364]='Kodiak_1____CORS';    lats[364]=57.6176928163;    lngs[364]=207.8065701888;    ves[364]=-9.71;    vns[364]=19.91;    vus[364]=6.87;    stddevNs[364]=0.49;    stddevEs[364]=0.45;    stddevUs[364]=1.07;    corrNEs[364]=-0.005; 
   ids[365]='KOD5';    sts[365]='Kodiak_5____CORS';    lats[365]=57.6176928126;    lngs[365]=207.8065702247;    ves[365]=-9.71;    vns[365]=19.91;    vus[365]=6.87;    stddevNs[365]=0.49;    stddevEs[365]=0.45;    stddevUs[365]=1.07;    corrNEs[365]=-0.005; 
   ids[366]='KOKB';    sts[366]='Kokee_Park_Geoph';    lats[366]=22.1262648828;    lngs[366]=200.3350693554;    ves[366]=-59.9;    vns[366]=53;    vus[366]=-0.66;    stddevNs[366]=1.3;    stddevEs[366]=0.77;    stddevUs[366]=1.08;    corrNEs[366]=-0.004; 
   ids[367]='KOKB';    sts[367]='Kokee_Park_Geoph';    lats[367]=22.1262644838;    lngs[367]=200.3350691577;    ves[367]=-59.9;    vns[367]=53;    vus[367]=-0.66;    stddevNs[367]=1.3;    stddevEs[367]=0.77;    stddevUs[367]=1.08;    corrNEs[367]=-0.004; 
   ids[368]='KSU1';    sts[368]='KSU1_KSUN_KS2006';    lats[368]=39.1007502036;    lngs[368]=263.3905199875;    ves[368]=-0.65;    vns[368]=-0.01;    vus[368]=-6.63;    stddevNs[368]=0.32;    stddevEs[368]=0.32;    stddevUs[368]=0.94;    corrNEs[368]=-0.001; 
   ids[369]='KSU1';    sts[369]='KSU1_KSUN_KS2006';    lats[369]=39.1007502184;    lngs[369]=263.3905199821;    ves[369]=-0.65;    vns[369]=-0.01;    vus[369]=-6.63;    stddevNs[369]=0.32;    stddevEs[369]=0.32;    stddevUs[369]=0.94;    corrNEs[369]=-0.001; 
   ids[370]='KTBW';    sts[370]='KTBW_PNGA_WA2001';    lats[370]=47.5473198104;    lngs[370]=237.2045826473;    ves[370]=4.24;    vns[370]=4.19;    vus[370]=-1.49;    stddevNs[370]=0.15;    stddevEs[370]=0.19;    stddevUs[370]=0.74;    corrNEs[370]=-0.007; 
   ids[371]='KVTX';    sts[371]='KingsvilleTX2006';    lats[371]=27.5459506164;    lngs[371]=262.1071177432;    ves[371]=-1.96;    vns[371]=-1.46;    vus[371]=-3.12;    stddevNs[371]=0.48;    stddevEs[371]=0.29;    stddevUs[371]=0.92;    corrNEs[371]=0.003; 
   ids[372]='LACR';    sts[372]='LACR_BRGN_NV2006';    lats[372]=40.8508047540;    lngs[372]=244.2962608712;    ves[372]=-4.63;    vns[372]=0.31;    vus[372]=-2.17;    stddevNs[372]=0.26;    stddevEs[372]=0.27;    stddevUs[372]=1.09;    corrNEs[372]=-0.002; 
   ids[373]='LAND';    sts[373]='LAND_SCGN_CN1999';    lats[373]=35.8997906383;    lngs[373]=239.5267165660;    ves[373]=-22.88;    vns[373]=28.03;    vus[373]=0.06;    stddevNs[373]=0.22;    stddevEs[373]=0.19;    stddevUs[373]=0.98;    corrNEs[373]=-0.008; 
   ids[374]='LAND';    sts[374]='LAND_SCGN_CN1999';    lats[374]=35.8997915478;    lngs[374]=239.5267155500;    ves[374]=-22.88;    vns[374]=28.03;    vus[374]=0.06;    stddevNs[374]=0.22;    stddevEs[374]=0.19;    stddevUs[374]=0.98;    corrNEs[374]=-0.008; 
   ids[375]='LAPC';    sts[375]='LAPC_SCGN_CS1999';    lats[375]=34.1819224260;    lngs[375]=241.4253491856;    ves[375]=-27.91;    vns[375]=27.71;    vus[375]=-0.9;    stddevNs[375]=0.14;    stddevEs[375]=0.13;    stddevUs[375]=0.5;    corrNEs[375]=-0.017; 
   ids[376]='LASC';    sts[376]='LASC_SCGN_CS1998';    lats[376]=33.9279425272;    lngs[376]=241.6934948072;    ves[376]=-27.19;    vns[376]=27.4;    vus[376]=-2.28;    stddevNs[376]=0.2;    stddevEs[376]=0.37;    stddevUs[376]=0.84;    corrNEs[376]=-0.004; 
   ids[377]='LDES';    sts[377]='LDES_SCGN_CS1998';    lats[377]=34.2673399287;    lngs[377]=243.5671965461;    ves[377]=-6.59;    vns[377]=10.76;    vus[377]=0.69;    stddevNs[377]=0.14;    stddevEs[377]=0.14;    stddevUs[377]=0.44;    corrNEs[377]=-0.013; 
   ids[378]='LDSW';    sts[378]='LDSW_SCGN_CS1999';    lats[378]=34.6994268688;    lngs[378]=243.7908580254;    ves[378]=-5.15;    vns[378]=0.67;    vus[378]=-0.43;    stddevNs[378]=1.1;    stddevEs[378]=0.48;    stddevUs[378]=0.6;    corrNEs[378]=-0.000; 
   ids[379]='LDSW';    sts[379]='LDSW_SCGN_CS1999';    lats[379]=34.6994269276;    lngs[379]=243.7908579920;    ves[379]=-5.15;    vns[379]=0.67;    vus[379]=-0.43;    stddevNs[379]=1.1;    stddevEs[379]=0.48;    stddevUs[379]=0.6;    corrNEs[379]=-0.000; 
   ids[380]='LEV1';    sts[380]='LevelIsland1CORS';    lats[380]=56.4656806870;    lngs[380]=226.9072185739;    ves[380]=-2.08;    vns[380]=1.05;    vus[380]=6.98;    stddevNs[380]=0.4;    stddevEs[380]=0.33;    stddevUs[380]=1.35;    corrNEs[380]=-0.002; 
   ids[381]='LEV1';    sts[381]='LevelIsland1CORS';    lats[381]=56.4656807670;    lngs[381]=226.9072185545;    ves[381]=-2.08;    vns[381]=1.05;    vus[381]=6.98;    stddevNs[381]=0.4;    stddevEs[381]=0.33;    stddevUs[381]=1.35;    corrNEs[381]=-0.002; 
   ids[382]='LEWI';    sts[382]='LEWI_BRGN_NV1997';    lats[382]=40.4035126996;    lngs[382]=243.1381272310;    ves[382]=-3.09;    vns[382]=-0.78;    vus[382]=-0.6;    stddevNs[382]=0.15;    stddevEs[382]=0.13;    stddevUs[382]=0.78;    corrNEs[382]=-0.011; 
   ids[383]='LFRS';    sts[383]='LFRS_SCGN_CS1998';    lats[383]=34.0950708717;    lngs[383]=241.5871744357;    ves[383]=-28.04;    vns[383]=25.29;    vus[383]=-1.8;    stddevNs[383]=0.14;    stddevEs[383]=0.14;    stddevUs[383]=0.61;    corrNEs[383]=-0.016; 
   ids[384]='LKCP';    sts[384]='LKCP_PNGA_WA2001';    lats[384]=47.9443798021;    lngs[384]=238.1691475642;    ves[384]=2.14;    vns[384]=2.56;    vus[384]=-1.84;    stddevNs[384]=0.14;    stddevEs[384]=0.14;    stddevUs[384]=0.56;    corrNEs[384]=-0.005; 
   ids[385]='LKWY';    sts[385]='LKWY_EBRY_WY1996';    lats[385]=44.5650760593;    lngs[385]=249.5997803856;    ves[385]=0.71;    vns[385]=-7.71;    vus[385]=32.25;    stddevNs[385]=0.34;    stddevEs[385]=0.44;    stddevUs[385]=1.64;    corrNEs[385]=0.001; 
   ids[386]='LKWY';    sts[386]='LKWY_EBRY_WY1996';    lats[386]=44.5650760768;    lngs[386]=249.5997803150;    ves[386]=0.71;    vns[386]=-7.71;    vus[386]=32.25;    stddevNs[386]=0.34;    stddevEs[386]=0.44;    stddevUs[386]=1.64;    corrNEs[386]=0.001; 
   ids[387]='LMNL';    sts[387]='Limonal___CR2007';    lats[387]=10.2675085034;    lngs[387]=274.9466690271;    ves[387]=16.5;    vns[387]=16.78;    vus[387]=-1.9;    stddevNs[387]=2.31;    stddevEs[387]=1.66;    stddevUs[387]=2.29;    corrNEs[387]=-0.001; 
   ids[388]='LMUT';    sts[388]='LakeMountaUT1997';    lats[388]=40.2614203259;    lngs[388]=248.0716858618;    ves[388]=-2.86;    vns[388]=-0.65;    vus[388]=-0.39;    stddevNs[388]=0.16;    stddevEs[388]=0.14;    stddevUs[388]=0.74;    corrNEs[388]=-0.007; 
   ids[389]='LNMT';    sts[389]='LNMT_SCGN_CS2000';    lats[389]=35.0902020826;    lngs[389]=243.0603479260;    ves[389]=-8.26;    vns[389]=8.22;    vus[389]=-1.67;    stddevNs[389]=0.32;    stddevEs[389]=0.31;    stddevUs[389]=0.49;    corrNEs[389]=-0.003; 
   ids[390]='LORS';    sts[390]='LORS_SCGN_CS1998';    lats[390]=34.1333274669;    lngs[390]=242.2459299167;    ves[390]=-24.79;    vns[390]=22.04;    vus[390]=-1.98;    stddevNs[390]=0.13;    stddevEs[390]=0.14;    stddevUs[390]=0.42;    corrNEs[390]=-0.016; 
   ids[391]='LOWS';    sts[391]='LOWS_SCGN_CN2001';    lats[391]=35.8287099342;    lngs[391]=239.4057153045;    ves[391]=-26.04;    vns[391]=31.36;    vus[391]=-0.32;    stddevNs[391]=0.18;    stddevEs[391]=0.28;    stddevUs[391]=0.77;    corrNEs[391]=-0.007; 
   ids[392]='LOWS';    sts[392]='LOWS_SCGN_CN2001';    lats[392]=35.8287101499;    lngs[392]=239.4057150096;    ves[392]=-26.04;    vns[392]=31.36;    vus[392]=-0.32;    stddevNs[392]=0.18;    stddevEs[392]=0.28;    stddevUs[392]=0.77;    corrNEs[392]=-0.007; 
   ids[393]='LOZ1';    sts[393]='LakeOzoniaNY2005';    lats[393]=44.6197073283;    lngs[393]=285.4170683579;    ves[393]=0.72;    vns[393]=-2.67;    vus[393]=3.56;    stddevNs[393]=0.37;    stddevEs[393]=0.67;    stddevUs[393]=1.82;    corrNEs[393]=0.007; 
   ids[394]='LST1';    sts[394]='LSU_test1_LA2006';    lats[394]=30.4074256881;    lngs[394]=268.8197421633;    ves[394]=110.57;    vns[394]=42.09;    vus[394]=-5.46;    stddevNs[394]=180.95;    stddevEs[394]=131.62;    stddevUs[394]=23.65;    corrNEs[394]=-0.000; 
   ids[395]='LTUT';    sts[395]='LTUT_EBRY_UT2002';    lats[395]=41.5920985880;    lngs[395]=247.7531623596;    ves[395]=-1.77;    vns[395]=-2.64;    vus[395]=-2.04;    stddevNs[395]=1.17;    stddevEs[395]=1.77;    stddevUs[395]=0.86;    corrNEs[395]=0.002; 
   ids[396]='LTUT';    sts[396]='LTUT_EBRY_UT2002';    lats[396]=41.5920985125;    lngs[396]=247.7531623633;    ves[396]=-1.77;    vns[396]=-2.64;    vus[396]=-2.04;    stddevNs[396]=1.17;    stddevEs[396]=1.77;    stddevUs[396]=0.86;    corrNEs[396]=0.002; 
   ids[397]='MASW';    sts[397]='MASW_SCGN_CN2001';    lats[397]=35.8325970838;    lngs[397]=239.5569406455;    ves[397]=-23.88;    vns[397]=27.65;    vus[397]=0.45;    stddevNs[397]=0.18;    stddevEs[397]=0.17;    stddevUs[397]=0.87;    corrNEs[397]=-0.011; 
   ids[398]='MASW';    sts[398]='MASW_SCGN_CN2001';    lats[398]=35.8325979026;    lngs[398]=239.5569400713;    ves[398]=-23.88;    vns[398]=27.65;    vus[398]=0.45;    stddevNs[398]=0.18;    stddevEs[398]=0.17;    stddevUs[398]=0.87;    corrNEs[398]=-0.011; 
   ids[399]='MAT2';    sts[399]='MAT2_SCGN_CS2000';    lats[399]=33.8567610070;    lngs[399]=242.5633012943;    ves[399]=-24.33;    vns[399]=24.54;    vus[399]=-1.9;    stddevNs[399]=0.15;    stddevEs[399]=0.13;    stddevUs[399]=0.43;    corrNEs[399]=-0.014; 
   ids[400]='MAUI';    sts[400]='Haleakala';    lats[400]=2242127.65989;    lngs[400]=20.7066565567;    ves[400]=53.59;    vns[400]=-412;    vus[400]=-59.92;    stddevNs[400]=-0.64;    stddevEs[400]=0.55;    stddevUs[400]=0.6;    corrNEs[400]=0.00137; 
   ids[401]='MAUI';    sts[401]='Haleakala';    lats[401]=2242127.65994;    lngs[401]=20.7066565616;    ves[401]=53.59;    vns[401]=-412;    vus[401]=-59.92;    stddevNs[401]=-0.64;    stddevEs[401]=0.55;    stddevUs[401]=0.6;    corrNEs[401]=0.00137; 
   ids[402]='MAWY';    sts[402]='MAWY_EBRY_WY1998';    lats[402]=44.9734278243;    lngs[402]=249.3106990519;    ves[402]=0.17;    vns[402]=-0.41;    vus[402]=-2.09;    stddevNs[402]=0.14;    stddevEs[402]=0.14;    stddevUs[402]=0.72;    corrNEs[402]=-0.007; 
   ids[403]='MDMT';    sts[403]='MDMT_PNGA_OR1999';    lats[403]=42.4183414929;    lngs[403]=238.7783992195;    ves[403]=-3.33;    vns[403]=4.43;    vus[403]=-1.18;    stddevNs[403]=0.16;    stddevEs[403]=0.16;    stddevUs[403]=0.65;    corrNEs[403]=-0.011; 
   ids[404]='MDO1';    sts[404]='McDonald_Observa';    lats[404]=30.6805110413;    lngs[404]=255.9850068972;    ves[404]=-1.21;    vns[404]=-0.3;    vus[404]=-0.33;    stddevNs[404]=0.14;    stddevEs[404]=0.14;    stddevUs[404]=0.63;    corrNEs[404]=0.002; 
   ids[405]='MEE1';    sts[405]='MEE1_BARD_CN2003';    lats[405]=36.1869037954;    lngs[405]=239.2413980438;    ves[405]=-11.35;    vns[405]=10.55;    vus[405]=1.72;    stddevNs[405]=0.3;    stddevEs[405]=0.22;    stddevUs[405]=0.87;    corrNEs[405]=-0.004; 
   ids[406]='MEE1';    sts[406]='MEE1_BARD_CN2003';    lats[406]=36.1869038063;    lngs[406]=239.2413980729;    ves[406]=-11.35;    vns[406]=10.55;    vus[406]=1.72;    stddevNs[406]=0.3;    stddevEs[406]=0.22;    stddevUs[406]=0.87;    corrNEs[406]=-0.004; 
   ids[407]='MEE2';    sts[407]='MEE2_BARD_CN2003';    lats[407]=36.1805244891;    lngs[407]=239.2331537960;    ves[407]=-29.62;    vns[407]=31.64;    vus[407]=3.83;    stddevNs[407]=0.21;    stddevEs[407]=0.2;    stddevUs[407]=0.92;    corrNEs[407]=-0.008; 
   ids[408]='MEE2';    sts[408]='MEE2_BARD_CN2003';    lats[408]=36.1805244300;    lngs[408]=239.2331538733;    ves[408]=-29.62;    vns[408]=31.64;    vus[408]=3.83;    stddevNs[408]=0.21;    stddevEs[408]=0.2;    stddevUs[408]=0.92;    corrNEs[408]=-0.008; 
   ids[409]='MFP0';    sts[409]='MarshalZerCO2007';    lats[409]=39.9496049251;    lngs[409]=254.8055967251;    ves[409]=0.59;    vns[409]=-1.21;    vus[409]=-3.41;    stddevNs[409]=0.41;    stddevEs[409]=0.64;    stddevUs[409]=1.57;    corrNEs[409]=0.005; 
   ids[410]='MFP0';    sts[410]='MarshalZerCO2007';    lats[410]=39.9496049107;    lngs[410]=254.8055967397;    ves[410]=0.59;    vns[410]=-1.21;    vus[410]=-3.41;    stddevNs[410]=0.41;    stddevEs[410]=0.64;    stddevUs[410]=1.57;    corrNEs[410]=0.005; 
   ids[411]='MFP0';    sts[411]='MarshalZerCO2007';    lats[411]=39.9496049004;    lngs[411]=254.8055967125;    ves[411]=0.59;    vns[411]=-1.21;    vus[411]=-3.41;    stddevNs[411]=0.41;    stddevEs[411]=0.64;    stddevUs[411]=1.57;    corrNEs[411]=0.005; 
   ids[412]='MFTC';    sts[412]='MarshalCenCO2007';    lats[412]=39.9492838832;    lngs[412]=254.8057342658;    ves[412]=0.61;    vns[412]=-2.39;    vus[412]=-4.12;    stddevNs[412]=2.75;    stddevEs[412]=2.66;    stddevUs[412]=4.92;    corrNEs[412]=0.002; 
   ids[413]='MFTN';    sts[413]='MarshalNorCO2007';    lats[413]=39.9492938828;    lngs[413]=254.8057361827;    ves[413]=-3.02;    vns[413]=-1.34;    vus[413]=-0.87;    stddevNs[413]=0.74;    stddevEs[413]=1.71;    stddevUs[413]=1.73;    corrNEs[413]=0.003; 
   ids[414]='MFTN';    sts[414]='MarshalNorCO2007';    lats[414]=39.9492938763;    lngs[414]=254.8057361771;    ves[414]=-3.02;    vns[414]=-1.34;    vus[414]=-0.87;    stddevNs[414]=0.74;    stddevEs[414]=1.71;    stddevUs[414]=1.73;    corrNEs[414]=0.003; 
   ids[415]='MFTS';    sts[415]='MarshalSouCO2007';    lats[415]=39.9492779124;    lngs[415]=254.8057450830;    ves[415]=-0.42;    vns[415]=-0.85;    vus[415]=-1.36;    stddevNs[415]=0.51;    stddevEs[415]=0.46;    stddevUs[415]=1.63;    corrNEs[415]=0.003; 
   ids[416]='MFTW';    sts[416]='MarshalWesCO2007';    lats[416]=39.9492799072;    lngs[416]=254.8057226986;    ves[416]=0.21;    vns[416]=1.02;    vus[416]=4.74;    stddevNs[416]=1.23;    stddevEs[416]=1.64;    stddevUs[416]=2.46;    corrNEs[416]=0.008; 
   ids[417]='MFTW';    sts[417]='MarshalWesCO2007';    lats[417]=39.9492799145;    lngs[417]=254.8057226878;    ves[417]=0.21;    vns[417]=1.02;    vus[417]=4.74;    stddevNs[417]=1.23;    stddevEs[417]=1.64;    stddevUs[417]=2.46;    corrNEs[417]=0.008; 
   ids[418]='MFTW';    sts[418]='MarshalWesCO2007';    lats[418]=39.9492799466;    lngs[418]=254.8057226855;    ves[418]=0.21;    vns[418]=1.02;    vus[418]=4.74;    stddevNs[418]=1.23;    stddevEs[418]=1.64;    stddevUs[418]=2.46;    corrNEs[418]=0.008; 
   ids[419]='MFTW';    sts[419]='MarshalWesCO2007';    lats[419]=39.9492799197;    lngs[419]=254.8057226563;    ves[419]=0.21;    vns[419]=1.02;    vus[419]=4.74;    stddevNs[419]=1.23;    stddevEs[419]=1.64;    stddevUs[419]=2.46;    corrNEs[419]=0.008; 
   ids[420]='MFTW';    sts[420]='MarshalWesCO2007';    lats[420]=39.9492799353;    lngs[420]=254.8057226302;    ves[420]=0.21;    vns[420]=1.02;    vus[420]=4.74;    stddevNs[420]=1.23;    stddevEs[420]=1.64;    stddevUs[420]=2.46;    corrNEs[420]=0.008; 
   ids[421]='MIDA';    sts[421]='MIDA_SCGN_CN1999';    lats[421]=35.9219124751;    lngs[421]=239.5411701809;    ves[421]=-14.75;    vns[421]=21.06;    vus[421]=-4.61;    stddevNs[421]=0.5;    stddevEs[421]=0.43;    stddevUs[421]=1.42;    corrNEs[421]=-0.003; 
   ids[422]='MIDA';    sts[422]='MIDA_SCGN_CN1999';    lats[422]=35.9219136899;    lngs[422]=239.5411691432;    ves[422]=-14.75;    vns[422]=21.06;    vus[422]=-4.61;    stddevNs[422]=0.5;    stddevEs[422]=0.43;    stddevUs[422]=1.42;    corrNEs[422]=-0.003; 
   ids[423]='MIG1';    sts[423]='MIG1_SCGN_CS2000';    lats[423]=34.0382594369;    lngs[423]=239.6486085087;    ves[423]=-31.7;    vns[423]=35.68;    vus[423]=-2.88;    stddevNs[423]=0.16;    stddevEs[423]=0.14;    stddevUs[423]=0.72;    corrNEs[423]=-0.016; 
   ids[424]='MINE';    sts[424]='MINE_BRGN_NV1996';    lats[424]=40.1483658344;    lngs[424]=243.9044879503;    ves[424]=-3.76;    vns[424]=0.48;    vus[424]=-1.21;    stddevNs[424]=0.19;    stddevEs[424]=0.35;    stddevUs[424]=0.79;    corrNEs[424]=-0.006; 
   ids[425]='MINE';    sts[425]='MINE_BRGN_NV1996';    lats[425]=40.1483658343;    lngs[425]=243.9044878884;    ves[425]=-3.76;    vns[425]=0.48;    vus[425]=-1.21;    stddevNs[425]=0.19;    stddevEs[425]=0.35;    stddevUs[425]=0.79;    corrNEs[425]=-0.006; 
   ids[426]='MKEA';    sts[426]='Mauna_Kea';    lats[426]=2148291.41595;    lngs[426]=19.8013559722;    ves[426]=52.71;    vns[426]=-358;    vus[426]=-59.44;    stddevNs[426]=-2.14;    stddevEs[426]=0.46;    stddevUs[426]=0.46;    corrNEs[426]=0.00119; 
   ids[427]='MKEA';    sts[427]='Mauna_Kea';    lats[427]=2148291.41538;    lngs[427]=19.8013559625;    ves[427]=52.71;    vns[427]=-358;    vus[427]=-59.44;    stddevNs[427]=-2.14;    stddevEs[427]=0.46;    stddevUs[427]=0.46;    corrNEs[427]=0.00119; 
   ids[428]='MLFP';    sts[428]='MLFP_SCGN_CS1998';    lats[428]=33.9184040746;    lngs[428]=242.6820417840;    ves[428]=-23.33;    vns[428]=23.64;    vus[428]=-1.49;    stddevNs[428]=0.15;    stddevEs[428]=0.13;    stddevUs[428]=0.43;    corrNEs[428]=-0.014; 
   ids[429]='MNMC';    sts[429]='MNMC_SCGN_CN2001';    lats[429]=35.9694716030;    lngs[429]=239.5659483034;    ves[429]=-11.64;    vns[429]=16.7;    vus[429]=0.49;    stddevNs[429]=0.22;    stddevEs[429]=0.91;    stddevUs[429]=0.95;    corrNEs[429]=-0.002; 
   ids[430]='MNMC';    sts[430]='MNMC_SCGN_CN2001';    lats[430]=35.9694716067;    lngs[430]=239.5659482462;    ves[430]=-11.64;    vns[430]=16.7;    vus[430]=0.49;    stddevNs[430]=0.22;    stddevEs[430]=0.91;    stddevUs[430]=0.95;    corrNEs[430]=-0.002; 
   ids[431]='MNMC';    sts[431]='MNMC_SCGN_CN2001';    lats[431]=35.9694723609;    lngs[431]=239.5659478030;    ves[431]=-11.64;    vns[431]=16.7;    vus[431]=0.49;    stddevNs[431]=0.22;    stddevEs[431]=0.91;    stddevUs[431]=0.95;    corrNEs[431]=-0.002; 
   ids[432]='MOIL';    sts[432]='MOIL_BRGN_NV2006';    lats[432]=40.7106828626;    lngs[432]=244.5878867442;    ves[432]=-4.17;    vns[432]=-0.03;    vus[432]=-2.1;    stddevNs[432]=0.28;    stddevEs[432]=0.27;    stddevUs[432]=1.07;    corrNEs[432]=-0.002; 
   ids[433]='MPUT';    sts[433]='MPUT_EBRY_UT2003';    lats[433]=40.0155994629;    lngs[433]=248.3663704201;    ves[433]=-2.07;    vns[433]=0.67;    vus[433]=-1.32;    stddevNs[433]=0.18;    stddevEs[433]=0.16;    stddevUs[433]=0.77;    corrNEs[433]=-0.002; 
   ids[434]='MPWD';    sts[434]='MPWD_SCGN_CS1998';    lats[434]=34.2955119175;    lngs[434]=241.1219834322;    ves[434]=-28.99;    vns[434]=28.79;    vus[434]=-1.77;    stddevNs[434]=0.2;    stddevEs[434]=0.13;    stddevUs[434]=0.47;    corrNEs[434]=-0.012; 
   ids[435]='MUSB';    sts[435]='MUSB_BARD_CN1997';    lats[435]=37.1699412651;    lngs[435]=240.6906494672;    ves[435]=-11.08;    vns[435]=8.29;    vus[435]=0.21;    stddevNs[435]=0.13;    stddevEs[435]=0.13;    stddevUs[435]=0.53;    corrNEs[435]=-0.017; 
   ids[436]='NBPS';    sts[436]='NBPS_SCGN_CS1999';    lats[436]=34.5086223926;    lngs[436]=243.8518278981;    ves[436]=-3.51;    vns[436]=2.75;    vus[436]=-1.18;    stddevNs[436]=0.15;    stddevEs[436]=0.15;    stddevUs[436]=0.61;    corrNEs[436]=-0.011; 
   ids[437]='NDAP';    sts[437]='NDAP_SCGN_CS2000';    lats[437]=34.7676880906;    lngs[437]=245.3813707322;    ves[437]=-4.26;    vns[437]=3.2;    vus[437]=-2.09;    stddevNs[437]=1.04;    stddevEs[437]=0.37;    stddevUs[437]=0.67;    corrNEs[437]=0.002; 
   ids[438]='NDAP';    sts[438]='NDAP_SCGN_CS2000';    lats[438]=34.7676881711;    lngs[438]=245.3813707060;    ves[438]=-4.26;    vns[438]=3.2;    vus[438]=-2.09;    stddevNs[438]=1.04;    stddevEs[438]=0.37;    stddevUs[438]=0.67;    corrNEs[438]=0.002; 
   ids[439]='NEAH';    sts[439]='NEAH_PNGA_WA2004';    lats[439]=48.2978552176;    lngs[439]=235.3750937166;    ves[439]=9.85;    vns[439]=7.88;    vus[439]=3.51;    stddevNs[439]=0.19;    stddevEs[439]=0.23;    stddevUs[439]=0.7;    corrNEs[439]=-0.004; 
   ids[440]='NEWP';    sts[440]='NEWP_PNGA_OR1996';    lats[440]=44.5850250400;    lngs[440]=235.9381135050;    ves[440]=6.56;    vns[440]=9.73;    vus[440]=0.99;    stddevNs[440]=0.31;    stddevEs[440]=0.57;    stddevUs[440]=0.88;    corrNEs[440]=-0.001; 
   ids[441]='NEWS';    sts[441]='NEWS_BRGN_NV1996';    lats[441]=39.6856166885;    lngs[441]=242.4910640360;    ves[441]=-4.75;    vns[441]=0.59;    vus[441]=0.44;    stddevNs[441]=0.22;    stddevEs[441]=0.14;    stddevUs[441]=0.81;    corrNEs[441]=-0.008; 
   ids[442]='NHRG';    sts[442]='NHRG_SCGN_CS2000';    lats[442]=34.4986579414;    lngs[442]=240.8587405744;    ves[442]=-27.97;    vns[442]=22.88;    vus[442]=-1.74;    stddevNs[442]=0.2;    stddevEs[442]=0.16;    stddevUs[442]=0.53;    corrNEs[442]=-0.010; 
   ids[443]='NLIB';    sts[443]='North_Liberty_VL';    lats[443]=41.7715908573;    lngs[443]=268.4251047129;    ves[443]=0.36;    vns[443]=-0.11;    vus[443]=-1.98;    stddevNs[443]=0.18;    stddevEs[443]=0.16;    stddevUs[443]=0.75;    corrNEs[443]=0.002; 
   ids[444]='NOMT';    sts[444]='NOMT_EBRY_MT1999';    lats[444]=45.5969192604;    lngs[444]=248.3701789648;    ves[444]=-0;    vns[444]=-0.43;    vus[444]=-1.18;    stddevNs[444]=0.15;    stddevEs[444]=0.15;    stddevUs[444]=0.58;    corrNEs[444]=-0.003; 
   ids[445]='NRC1';    sts[445]='OTTAWA_NRC';    lats[445]=4522955.81212;    lngs[445]=45.4541624073;    ves[445]=-0.78;    vns[445]=-865;    vus[445]=0.33;    stddevNs[445]=3.27;    stddevEs[445]=0.22;    stddevUs[445]=0.32;    corrNEs[445]=0.00083; 
   ids[446]='NRC1';    sts[446]='OTTAWA_NRC';    lats[446]=4522955.81766;    lngs[446]=45.4541624091;    ves[446]=-0.78;    vns[446]=-865;    vus[446]=0.33;    stddevNs[446]=3.27;    stddevEs[446]=0.22;    stddevUs[446]=0.32;    corrNEs[446]=0.00083; 
   ids[447]='NRC1';    sts[447]='OTTAWA_NRC';    lats[447]=4522955.81883;    lngs[447]=45.4541623717;    ves[447]=-0.78;    vns[447]=-865;    vus[447]=0.33;    stddevNs[447]=3.27;    stddevEs[447]=0.22;    stddevUs[447]=0.32;    corrNEs[447]=0.00083; 
   ids[448]='NRWY';    sts[448]='NRWY_EBRY_WY2003';    lats[448]=44.7148478989;    lngs[448]=249.3222827136;    ves[448]=-3.37;    vns[448]=-2.11;    vus[448]=-7.76;    stddevNs[448]=0.39;    stddevEs[448]=0.52;    stddevUs[448]=1.49;    corrNEs[448]=-0.000; 
   ids[449]='NRWY';    sts[449]='NRWY_EBRY_WY2003';    lats[449]=44.7148479437;    lngs[449]=249.3222826491;    ves[449]=-3.37;    vns[449]=-2.11;    vus[449]=-7.76;    stddevNs[449]=0.39;    stddevEs[449]=0.52;    stddevUs[449]=1.49;    corrNEs[449]=-0.000; 
   ids[450]='OAES';    sts[450]='OAES_SCGN_CS1999';    lats[450]=34.1410053090;    lngs[450]=243.9322640195;    ves[450]=-2.34;    vns[450]=6.71;    vus[450]=-2.02;    stddevNs[450]=1.07;    stddevEs[450]=0.62;    stddevUs[450]=0.61;    corrNEs[450]=-0.000; 
   ids[451]='OAES';    sts[451]='OAES_SCGN_CS1999';    lats[451]=34.1410053219;    lngs[451]=243.9322638761;    ves[451]=-2.34;    vns[451]=6.71;    vus[451]=-2.02;    stddevNs[451]=1.07;    stddevEs[451]=0.62;    stddevUs[451]=0.61;    corrNEs[451]=-0.000; 
   ids[452]='OAES';    sts[452]='OAES_SCGN_CS1999';    lats[452]=34.1410053209;    lngs[452]=243.9322638752;    ves[452]=-2.34;    vns[452]=6.71;    vus[452]=-2.02;    stddevNs[452]=1.07;    stddevEs[452]=0.62;    stddevUs[452]=0.61;    corrNEs[452]=-0.000; 
   ids[453]='OFW2';    sts[453]='OFW2_EBRY_WY2003';    lats[453]=44.4511072049;    lngs[453]=249.1688494449;    ves[453]=-5.23;    vns[453]=0.28;    vus[453]=22.97;    stddevNs[453]=0.33;    stddevEs[453]=0.23;    stddevUs[453]=1.6;    corrNEs[453]=-0.001; 
   ids[454]='OPBL';    sts[454]='OPBL_SCGN_CS1999';    lats[454]=34.3698825355;    lngs[454]=244.0819431507;    ves[454]=-3.03;    vns[454]=2.17;    vus[454]=-1.59;    stddevNs[454]=0.13;    stddevEs[454]=0.14;    stddevUs[454]=0.5;    corrNEs[454]=-0.013; 
   ids[455]='OPCL';    sts[455]='OPCL_SCGN_CS1999';    lats[455]=34.4277165804;    lngs[455]=243.6945431046;    ves[455]=-5.51;    vns[455]=6.97;    vus[455]=0.08;    stddevNs[455]=0.16;    stddevEs[455]=0.14;    stddevUs[455]=0.47;    corrNEs[455]=-0.011; 
   ids[456]='OPCP';    sts[456]='OPCP_SCGN_CS1999';    lats[456]=34.3671295435;    lngs[456]=243.9166280250;    ves[456]=-3.29;    vns[456]=3.97;    vus[456]=-0.96;    stddevNs[456]=0.14;    stddevEs[456]=0.2;    stddevUs[456]=0.45;    corrNEs[456]=-0.009; 
   ids[457]='OPCX';    sts[457]='OPCX_SCGN_CS1999';    lats[457]=34.4300824737;    lngs[457]=243.8505163064;    ves[457]=-3.12;    vns[457]=3.63;    vus[457]=-0.8;    stddevNs[457]=0.13;    stddevEs[457]=0.14;    stddevUs[457]=0.4;    corrNEs[457]=-0.014; 
   ids[458]='OPRD';    sts[458]='OPRD_SCGN_CS1999';    lats[458]=34.5330318230;    lngs[458]=243.7077209991;    ves[458]=-6.15;    vns[458]=5.24;    vus[458]=1.03;    stddevNs[458]=0.13;    stddevEs[458]=0.13;    stddevUs[458]=0.6;    corrNEs[458]=-0.015; 
   ids[459]='ORES';    sts[459]='ORES_SCGN_CS1999';    lats[459]=34.7390605047;    lngs[459]=239.7214413084;    ves[459]=-30.3;    vns[459]=33.35;    vus[459]=-8.45;    stddevNs[459]=0.27;    stddevEs[459]=0.25;    stddevUs[459]=0.75;    corrNEs[459]=-0.006; 
   ids[460]='ORMT';    sts[460]='ORMT_SCGN_CS2000';    lats[460]=34.6749042460;    lngs[460]=243.1849082763;    ves[460]=-11.18;    vns[460]=10.2;    vus[460]=-1.6;    stddevNs[460]=0.13;    stddevEs[460]=0.14;    stddevUs[460]=0.39;    corrNEs[460]=-0.014; 
   ids[461]='OVLS';    sts[461]='OVLS_SCGN_CS1999';    lats[461]=34.3273600837;    lngs[461]=240.8580528335;    ves[461]=-29.08;    vns[461]=27.83;    vus[461]=-2.33;    stddevNs[461]=0.22;    stddevEs[461]=0.16;    stddevUs[461]=0.74;    corrNEs[461]=-0.010; 
   ids[462]='OZST';    sts[462]='OZST_SCGN_CS2000';    lats[462]=34.6833715634;    lngs[462]=240.6465924711;    ves[462]=-28.07;    vns[462]=23.62;    vus[462]=-0.3;    stddevNs[462]=0.17;    stddevEs[462]=0.14;    stddevUs[462]=0.6;    corrNEs[462]=-0.015; 
   ids[463]='P001';    sts[463]='Organ_PipeAZ2007';    lats[463]=31.9493769530;    lngs[463]=247.1982976381;    ves[463]=-2.95;    vns[463]=0.04;    vus[463]=-0.09;    stddevNs[463]=0.23;    stddevEs[463]=0.25;    stddevUs[463]=1.06;    corrNEs[463]=0.000; 
   ids[464]='P002';    sts[464]='VigusButteNV2006';    lats[464]=39.5211386586;    lngs[464]=242.8134691454;    ves[464]=-4.21;    vns[464]=0.75;    vus[464]=-1.8;    stddevNs[464]=0.17;    stddevEs[464]=0.17;    stddevUs[464]=0.69;    corrNEs[464]=-0.008; 
   ids[465]='P003';    sts[465]='MohawkvallAZ2006';    lats[465]=32.7225446689;    lngs[465]=245.9950483223;    ves[465]=-3.85;    vns[465]=0.52;    vus[465]=-1.61;    stddevNs[465]=0.18;    stddevEs[465]=0.17;    stddevUs[465]=0.78;    corrNEs[465]=-0.006; 
   ids[466]='P003';    sts[466]='MohawkvallAZ2006';    lats[466]=32.7225446689;    lngs[466]=245.9950483160;    ves[466]=-3.85;    vns[466]=0.52;    vus[466]=-1.61;    stddevNs[466]=0.18;    stddevEs[466]=0.17;    stddevUs[466]=0.78;    corrNEs[466]=-0.006; 
   ids[467]='P004';    sts[467]='JeromeNFS_AZ2007';    lats[467]=34.7843821725;    lngs[467]=247.8480194726;    ves[467]=-2.89;    vns[467]=-0.28;    vus[467]=-3.32;    stddevNs[467]=0.19;    stddevEs[467]=0.22;    stddevUs[467]=0.73;    corrNEs[467]=-0.004; 
   ids[468]='P005';    sts[468]='PonyExpresNV2006';    lats[468]=39.9101715614;    lngs[468]=244.7213726339;    ves[468]=-4.03;    vns[468]=-0.18;    vus[468]=-1.59;    stddevNs[468]=0.18;    stddevEs[468]=0.17;    stddevUs[468]=0.66;    corrNEs[468]=-0.007; 
   ids[469]='P006';    sts[469]='Lake_Mead_NV2007';    lats[469]=36.1541878965;    lngs[469]=245.5430882313;    ves[469]=-2.86;    vns[469]=0.75;    vus[469]=1.57;    stddevNs[469]=0.22;    stddevEs[469]=0.22;    stddevUs[469]=0.99;    corrNEs[469]=-0.001; 
   ids[470]='P007';    sts[470]='SalmonFallNV2007';    lats[470]=41.7242011133;    lngs[470]=245.1802876641;    ves[470]=-4.05;    vns[470]=-0.04;    vus[470]=-1.93;    stddevNs[470]=0.26;    stddevEs[470]=0.25;    stddevUs[470]=0.87;    corrNEs[470]=-0.001; 
   ids[471]='P008';    sts[471]='Tuba_City_AZ2007';    lats[471]=36.1428213192;    lngs[471]=248.8699526038;    ves[471]=-2.17;    vns[471]=0.14;    vus[471]=-2.02;    stddevNs[471]=0.2;    stddevEs[471]=0.23;    stddevUs[471]=0.95;    corrNEs[471]=-0.002; 
   ids[472]='P009';    sts[472]='Marysvale_UT2006';    lats[472]=38.4799306399;    lngs[472]=247.7772854568;    ves[472]=-3.19;    vns[472]=-0.7;    vus[472]=-1.47;    stddevNs[472]=0.31;    stddevEs[472]=0.29;    stddevUs[472]=0.65;    corrNEs[472]=-0.001; 
   ids[473]='P010';    sts[473]='AubreyPeakAZ2007';    lats[473]=34.6672624417;    lngs[473]=246.2686575315;    ves[473]=-3.49;    vns[473]=0.5;    vus[473]=-1.91;    stddevNs[473]=0.18;    stddevEs[473]=0.19;    stddevUs[473]=0.72;    corrNEs[473]=-0.006; 
   ids[474]='P011';    sts[474]='SpiderRockAZ2005';    lats[474]=36.1498280981;    lngs[474]=250.4807725028;    ves[474]=-1.62;    vns[474]=0.17;    vus[474]=-2.61;    stddevNs[474]=0.23;    stddevEs[474]=0.19;    stddevUs[474]=0.53;    corrNEs[474]=-0.001; 
   ids[475]='P012';    sts[475]='MonticelloUT2006';    lats[475]=38.0974329882;    lngs[475]=250.6661645799;    ves[475]=-1.48;    vns[475]=-0.17;    vus[475]=-2.72;    stddevNs[475]=0.16;    stddevEs[475]=0.17;    stddevUs[475]=0.52;    corrNEs[475]=-0.003; 
   ids[476]='P013';    sts[476]='SpringCrekNV2007';    lats[476]=41.4287416555;    lngs[476]=242.6700061973;    ves[476]=-3.97;    vns[476]=0.8;    vus[476]=-0.93;    stddevNs[476]=0.19;    stddevEs[476]=0.19;    stddevUs[476]=0.69;    corrNEs[476]=-0.006; 
   ids[477]='P014';    sts[477]='Sahuarita_AZ2007';    lats[477]=31.9728946654;    lngs[477]=248.9014041460;    ves[477]=-2.17;    vns[477]=-0.09;    vus[477]=-1.22;    stddevNs[477]=0.28;    stddevEs[477]=0.34;    stddevUs[477]=0.9;    corrNEs[477]=0.001; 
   ids[478]='P015';    sts[478]='DueceClubSAZ2005';    lats[478]=34.2638108646;    lngs[478]=249.9905151246;    ves[478]=-2.02;    vns[478]=-0.12;    vus[478]=-1.68;    stddevNs[478]=0.15;    stddevEs[478]=0.17;    stddevUs[478]=0.47;    corrNEs[478]=-0.004; 
   ids[479]='P016';    sts[479]='VernonHillUT2006';    lats[479]=40.0781221987;    lngs[479]=247.6385982385;    ves[479]=-3.6;    vns[479]=-0.09;    vus[479]=-0.91;    stddevNs[479]=0.17;    stddevEs[479]=0.15;    stddevUs[479]=0.78;    corrNEs[479]=-0.006; 
   ids[480]='P017';    sts[480]='HaysPeak__NV2006';    lats[480]=41.2758964033;    lngs[480]=240.0644901233;    ves[480]=-5.72;    vns[480]=3.52;    vus[480]=-2.11;    stddevNs[480]=0.17;    stddevEs[480]=0.2;    stddevUs[480]=0.75;    corrNEs[480]=-0.007; 
   ids[481]='P018';    sts[481]='Pharmacy__OR2008';    lats[481]=42.9817067838;    lngs[481]=242.9354424469;    ves[481]=-2.62;    vns[481]=0.68;    vus[481]=-2.28;    stddevNs[481]=0.39;    stddevEs[481]=0.39;    stddevUs[481]=1.43;    corrNEs[481]=-0.002; 
   ids[482]='P019';    sts[482]='Cat_Creek_ID2007';    lats[482]=43.3001962922;    lngs[482]=244.6883385968;    ves[482]=-2.8;    vns[482]=-0.24;    vus[482]=-3.63;    stddevNs[482]=0.21;    stddevEs[482]=0.19;    stddevUs[482]=0.8;    corrNEs[482]=-0.004; 
   ids[483]='P020';    sts[483]='DRYLNDRSH_WA2004';    lats[483]=47.0022087612;    lngs[483]=241.4342304753;    ves[483]=0.11;    vns[483]=0.67;    vus[483]=-0.91;    stddevNs[483]=0.16;    stddevEs[483]=0.13;    stddevUs[483]=0.56;    corrNEs[483]=-0.008; 
   ids[484]='P021';    sts[484]='RadioHill_WA2005';    lats[484]=48.6746927682;    lngs[484]=241.2697474660;    ves[484]=0.73;    vns[484]=0.58;    vus[484]=-0.38;    stddevNs[484]=0.19;    stddevEs[484]=0.16;    stddevUs[484]=0.66;    corrNEs[484]=-0.003; 
   ids[485]='P022';    sts[485]='I84E_E269_OR2005';    lats[485]=45.2317862028;    lngs[485]=241.9862275327;    ves[485]=-1;    vns[485]=0.8;    vus[485]=-0.89;    stddevNs[485]=0.16;    stddevEs[485]=0.15;    stddevUs[485]=0.64;    corrNEs[485]=-0.007; 
   ids[486]='P023';    sts[486]='McCallAir_ID2007';    lats[486]=44.8984335844;    lngs[486]=243.8969950138;    ves[486]=-0.27;    vns[486]=0.51;    vus[486]=-2.31;    stddevNs[486]=0.2;    stddevEs[486]=0.24;    stddevUs[486]=0.93;    corrNEs[486]=-0.004; 
   ids[487]='P024';    sts[487]='GoosePeak_ID2008';    lats[487]=47.5622023779;    lngs[487]=244.1575663182;    ves[487]=-4.9;    vns[487]=1.73;    vus[487]=24.02;    stddevNs[487]=2.82;    stddevEs[487]=2.57;    stddevUs[487]=16.64;    corrNEs[487]=-0.059; 
   ids[488]='P025';    sts[488]='Bndry_Air_ID2007';    lats[488]=48.7310143241;    lngs[488]=243.7124887017;    ves[488]=0.74;    vns[488]=-0.26;    vus[488]=-0.24;    stddevNs[488]=0.26;    stddevEs[488]=0.22;    stddevUs[488]=0.87;    corrNEs[488]=-0.002; 
   ids[489]='P026';    sts[489]='HATCHARPT_NM2004';    lats[489]=32.6589612362;    lngs[489]=252.8051274116;    ves[489]=-1.66;    vns[489]=-0.56;    vus[489]=-0.58;    stddevNs[489]=0.45;    stddevEs[489]=0.2;    stddevUs[489]=0.97;    corrNEs[489]=-0.001; 
   ids[490]='P027';    sts[490]='ApachePnt_NM2007';    lats[490]=32.8018587809;    lngs[490]=254.1958404510;    ves[490]=-1.5;    vns[490]=0.16;    vus[490]=-0.53;    stddevNs[490]=0.22;    stddevEs[490]=0.3;    stddevUs[490]=0.83;    corrNEs[490]=-0.000; 
   ids[491]='P028';    sts[491]='ChacoCnHP_NM2005';    lats[491]=36.0316851597;    lngs[491]=252.0915917479;    ves[491]=-1.43;    vns[491]=-0.14;    vus[491]=-2.22;    stddevNs[491]=0.18;    stddevEs[491]=0.18;    stddevUs[491]=0.49;    corrNEs[491]=-0.002; 
   ids[492]='P029';    sts[492]='MontroseCSCO2007';    lats[492]=38.4391910718;    lngs[492]=252.3619574426;    ves[492]=-2.39;    vns[492]=-3.21;    vus[492]=0.92;    stddevNs[492]=0.21;    stddevEs[492]=0.3;    stddevUs[492]=0.73;    corrNEs[492]=-0.000; 
   ids[493]='P030';    sts[493]='KemmererLFWY2005';    lats[493]=41.7498152284;    lngs[493]=249.4871904244;    ves[493]=-0.85;    vns[493]=-0.33;    vus[493]=-0.67;    stddevNs[493]=0.25;    stddevEs[493]=0.15;    stddevUs[493]=0.8;    corrNEs[493]=-0.002; 
   ids[494]='P031';    sts[494]='Rifle_GCL_CO2006';    lats[494]=39.5154969356;    lngs[494]=252.0913290206;    ves[494]=1.39;    vns[494]=-0.4;    vus[494]=-4.45;    stddevNs[494]=0.23;    stddevEs[494]=0.6;    stddevUs[494]=0.63;    corrNEs[494]=-0.001; 
   ids[495]='P032';    sts[495]='RawlinsWWTWY2005';    lats[495]=41.7416733990;    lngs[495]=252.7440766725;    ves[495]=-0.54;    vns[495]=-0.05;    vus[495]=-2.26;    stddevNs[495]=0.24;    stddevEs[495]=0.22;    stddevUs[495]=0.83;    corrNEs[495]=-0.001; 
   ids[496]='P033';    sts[496]='TenSleepTRWY2005';    lats[496]=43.9528993881;    lngs[496]=252.6124531669;    ves[496]=-0.48;    vns[496]=-0.72;    vus[496]=-2.44;    stddevNs[496]=0.37;    stddevEs[496]=0.34;    stddevUs[496]=1.02;    corrNEs[496]=0.000; 
   ids[497]='P034';    sts[497]='SANDIA_ASLNM2004';    lats[497]=34.9456197567;    lngs[497]=253.5407336244;    ves[497]=-1.15;    vns[497]=-0.13;    vus[497]=-1.45;    stddevNs[497]=0.13;    stddevEs[497]=0.17;    stddevUs[497]=0.57;    corrNEs[497]=-0.005; 
   ids[498]='P035';    sts[498]='VaughnPtrlNM2005';    lats[498]=34.6013932164;    lngs[498]=254.8163981349;    ves[498]=-1.02;    vns[498]=0.04;    vus[498]=-1.64;    stddevNs[498]=0.15;    stddevEs[498]=0.23;    stddevUs[498]=0.67;    corrNEs[498]=-0.001; 
   ids[499]='P036';    sts[499]='ANGELFIRE_NM2004';    lats[499]=36.4202746200;    lngs[499]=254.7063471189;    ves[499]=-0.88;    vns[499]=-0.1;    vus[499]=-1.67;    stddevNs[499]=0.4;    stddevEs[499]=0.22;    stddevUs[499]=0.63;    corrNEs[499]=-0.001; 
   ids[500]='P036';    sts[500]='ANGELFIRE_NM2004';    lats[500]=36.4202746710;    lngs[500]=254.7063471059;    ves[500]=-0.88;    vns[500]=-0.1;    vus[500]=-1.67;    stddevNs[500]=0.4;    stddevEs[500]=0.22;    stddevUs[500]=0.63;    corrNEs[500]=-0.001; 
   ids[501]='P036';    sts[501]='ANGELFIRE_NM2004';    lats[501]=36.4202746463;    lngs[501]=254.7063470909;    ves[501]=-0.88;    vns[501]=-0.1;    vus[501]=-1.67;    stddevNs[501]=0.4;    stddevEs[501]=0.22;    stddevUs[501]=0.63;    corrNEs[501]=-0.001; 
   ids[502]='P037';    sts[502]='FreemontAPCO2004';    lats[502]=38.4217539234;    lngs[502]=254.8953165535;    ves[502]=-0.84;    vns[502]=-0.12;    vus[502]=-1.43;    stddevNs[502]=0.14;    stddevEs[502]=0.2;    stddevUs[502]=0.61;    corrNEs[502]=-0.003; 
   ids[503]='P038';    sts[503]='PortalesApNM2005';    lats[503]=34.1472552863;    lngs[503]=256.5926617802;    ves[503]=-1.03;    vns[503]=-0.36;    vus[503]=-0.56;    stddevNs[503]=0.16;    stddevEs[503]=0.23;    stddevUs[503]=0.67;    corrNEs[503]=-0.001; 
   ids[504]='P039';    sts[504]='ClaytonArpNM2005';    lats[504]=36.4481131816;    lngs[504]=256.8460463783;    ves[504]=-0.81;    vns[504]=0.18;    vus[504]=-0.82;    stddevNs[504]=0.16;    stddevEs[504]=0.24;    stddevUs[504]=0.74;    corrNEs[504]=-0.001; 
   ids[505]='P040';    sts[505]='LamarArpt_CO2004';    lats[505]=38.0714990819;    lngs[505]=257.3130211700;    ves[505]=-0.95;    vns[505]=-0.13;    vus[505]=-1.77;    stddevNs[505]=0.32;    stddevEs[505]=0.3;    stddevUs[505]=0.78;    corrNEs[505]=0.000; 
   ids[506]='P040';    sts[506]='LamarArpt_CO2004';    lats[506]=38.0714990748;    lngs[506]=257.3130211577;    ves[506]=-0.95;    vns[506]=-0.13;    vus[506]=-1.77;    stddevNs[506]=0.32;    stddevEs[506]=0.3;    stddevUs[506]=0.78;    corrNEs[506]=0.000; 
   ids[507]='P041';    sts[507]='Marshall__CO2004';    lats[507]=39.9494919647;    lngs[507]=254.8057336362;    ves[507]=-0.55;    vns[507]=-0.24;    vus[507]=-1.89;    stddevNs[507]=0.16;    stddevEs[507]=0.17;    stddevUs[507]=0.57;    corrNEs[507]=-0.003; 
   ids[508]='P042';    sts[508]='Wheatland_WY2004';    lats[508]=42.0515062326;    lngs[508]=255.0894056495;    ves[508]=-0.68;    vns[508]=-0.17;    vus[508]=-2.38;    stddevNs[508]=0.16;    stddevEs[508]=0.21;    stddevUs[508]=0.77;    corrNEs[508]=-0.002; 
   ids[509]='P043';    sts[509]='Newcastle_WY2006';    lats[509]=43.8811457514;    lngs[509]=255.8142976275;    ves[509]=-0.52;    vns[509]=-0.29;    vus[509]=-2.71;    stddevNs[509]=0.29;    stddevEs[509]=0.38;    stddevUs[509]=1.1;    corrNEs[509]=0.000; 
   ids[510]='P044';    sts[510]='AkronArpt_CO2004';    lats[510]=40.1717907022;    lngs[510]=256.7775163104;    ves[510]=-0.45;    vns[510]=-0.34;    vus[510]=-2.03;    stddevNs[510]=0.16;    stddevEs[510]=0.24;    stddevUs[510]=0.82;    corrNEs[510]=-0.001; 
   ids[511]='P045';    sts[511]='BirchCreekMT2006';    lats[511]=45.3828739078;    lngs[511]=247.3828109437;    ves[511]=-0.58;    vns[511]=-0.95;    vus[511]=-2.06;    stddevNs[511]=0.3;    stddevEs[511]=0.17;    stddevUs[511]=0.87;    corrNEs[511]=-0.002; 
   ids[512]='P046';    sts[512]='ClearwaterMT2006';    lats[512]=47.0295955542;    lngs[512]=246.6682668692;    ves[512]=0.14;    vns[512]=-0.78;    vus[512]=-2.86;    stddevNs[512]=0.2;    stddevEs[512]=0.17;    stddevUs[512]=0.66;    corrNEs[512]=-0.004; 
   ids[513]='P047';    sts[513]='E_Glacier_MT2007';    lats[513]=48.4213361702;    lngs[513]=246.7802550143;    ves[513]=0.73;    vns[513]=-0.67;    vus[513]=-2.66;    stddevNs[513]=0.27;    stddevEs[513]=0.23;    stddevUs[513]=1;    corrNEs[513]=-0.001; 
   ids[514]='P048';    sts[514]='BozemanRR_MT2007';    lats[514]=45.6530442870;    lngs[514]=248.7957900004;    ves[514]=0.18;    vns[514]=-0.1;    vus[514]=-2.85;    stddevNs[514]=0.29;    stddevEs[514]=0.19;    stddevUs[514]=0.94;    corrNEs[514]=-0.001; 
   ids[515]='P049';    sts[515]='Armington_MT2006';    lats[515]=47.3499644982;    lngs[515]=249.0937678901;    ves[515]=0.23;    vns[515]=-0.64;    vus[515]=-2.48;    stddevNs[515]=0.19;    stddevEs[515]=0.2;    stddevUs[515]=0.79;    corrNEs[515]=-0.002; 
   ids[516]='P050';    sts[516]='WickumRnchMT2006';    lats[516]=48.8094774563;    lngs[516]=248.7515690837;    ves[516]=0.55;    vns[516]=-0.89;    vus[516]=-1.51;    stddevNs[516]=0.23;    stddevEs[516]=0.17;    stddevUs[516]=1.04;    corrNEs[516]=-0.001; 
   ids[517]='P051';    sts[517]='BillingsAPMT2005';    lats[517]=45.8066011298;    lngs[517]=251.4538555229;    ves[517]=0.16;    vns[517]=-0.86;    vus[517]=-2.62;    stddevNs[517]=0.25;    stddevEs[517]=0.23;    stddevUs[517]=0.97;    corrNEs[517]=-0.000; 
   ids[518]='P052';    sts[518]='LRRnchJrdnMT2006';    lats[518]=47.3747361929;    lngs[518]=252.9813236220;    ves[518]=0.16;    vns[518]=-0.41;    vus[518]=-2.97;    stddevNs[518]=0.22;    stddevEs[518]=0.3;    stddevUs[518]=1.08;    corrNEs[518]=0.000; 
   ids[519]='P053';    sts[519]='WhitewaterMT2007';    lats[519]=48.7260802347;    lngs[519]=252.2745810422;    ves[519]=-0.02;    vns[519]=-1.07;    vus[519]=-4.32;    stddevNs[519]=0.22;    stddevEs[519]=0.21;    stddevUs[519]=1.02;    corrNEs[519]=0.002; 
   ids[520]='P054';    sts[520]='TEREkalakaMT2006';    lats[520]=45.8463491808;    lngs[520]=255.5585809961;    ves[520]=-0.09;    vns[520]=-0.44;    vus[520]=-2.8;    stddevNs[520]=0.18;    stddevEs[520]=0.39;    stddevUs[520]=1.2;    corrNEs[520]=0.000; 
   ids[521]='P055';    sts[521]='GlendiveWTMT2006';    lats[521]=47.1167136806;    lngs[521]=255.3148772603;    ves[521]=0.26;    vns[521]=-0.77;    vus[521]=-3.54;    stddevNs[521]=0.19;    stddevEs[521]=0.35;    stddevUs[521]=1.04;    corrNEs[521]=0.001; 
   ids[522]='P056';    sts[522]='PotervilleCS2005';    lats[522]=36.0274376795;    lngs[522]=240.9371300982;    ves[522]=-12.67;    vns[522]=8.86;    vus[522]=-28.89;    stddevNs[522]=0.35;    stddevEs[522]=0.18;    stddevUs[522]=0.79;    corrNEs[522]=-0.005; 
   ids[523]='P057';    sts[523]='Hill72____UT2004';    lats[523]=41.7565712295;    lngs[523]=247.3768611333;    ves[523]=-2.84;    vns[523]=-0.63;    vus[523]=-1.58;    stddevNs[523]=0.15;    stddevEs[523]=0.13;    stddevUs[523]=0.59;    corrNEs[523]=-0.008; 
   ids[524]='P058';    sts[524]='HumboldtStCN2005';    lats[524]=40.8763061913;    lngs[524]=235.9246306455;    ves[524]=0.52;    vns[524]=16.81;    vus[524]=-0.1;    stddevNs[524]=0.17;    stddevEs[524]=0.29;    stddevUs[524]=0.79;    corrNEs[524]=-0.007; 
   ids[525]='P059';    sts[525]='PointArenaCN2006';    lats[525]=38.9283465080;    lngs[525]=236.2738028546;    ves[525]=-26.51;    vns[525]=35.4;    vus[525]=-1.87;    stddevNs[525]=0.24;    stddevEs[525]=0.22;    stddevUs[525]=0.75;    corrNEs[525]=-0.007; 
   ids[526]='P060';    sts[526]='PollardFltCN2005';    lats[526]=40.9976325683;    lngs[526]=237.5851159123;    ves[526]=-7.04;    vns[526]=6.34;    vus[526]=1.39;    stddevNs[526]=0.18;    stddevEs[526]=0.29;    stddevUs[526]=0.62;    corrNEs[526]=-0.007; 
   ids[527]='P061';    sts[527]='Myrtle_CrkOR2006';    lats[527]=42.9673783289;    lngs[527]=235.9859854107;    ves[527]=-0.08;    vns[527]=9.49;    vus[527]=-0.41;    stddevNs[527]=0.27;    stddevEs[527]=0.6;    stddevUs[527]=0.68;    corrNEs[527]=-0.001; 
   ids[528]='P062';    sts[528]='SilverLakeOR2007';    lats[528]=43.1123873514;    lngs[528]=238.9092866888;    ves[528]=-2.14;    vns[528]=3.8;    vus[528]=-0.94;    stddevNs[528]=0.27;    stddevEs[528]=0.25;    stddevUs[528]=0.75;    corrNEs[528]=-0.003; 
   ids[529]='P063';    sts[529]='Shaniko___OR2005';    lats[529]=44.9227224799;    lngs[529]=239.0538472916;    ves[529]=-0.33;    vns[529]=2.46;    vus[529]=-1.54;    stddevNs[529]=0.17;    stddevEs[529]=0.18;    stddevUs[529]=0.57;    corrNEs[529]=-0.007; 
   ids[530]='P064';    sts[530]='HricaneRg_WA2007';    lats[530]=47.9698465829;    lngs[530]=236.5122954515;    ves[530]=6.48;    vns[530]=4.19;    vus[530]=-0.41;    stddevNs[530]=69.67;    stddevEs[530]=62.06;    stddevUs[530]=10.99;    corrNEs[530]=-0.000; 
   ids[531]='P065';    sts[531]='Naches_Rg_WA2006';    lats[531]=46.8439599461;    lngs[531]=239.0669084670;    ves[531]=1.5;    vns[531]=2.11;    vus[531]=-2.74;    stddevNs[531]=0.22;    stddevEs[531]=0.25;    stddevUs[531]=0.71;    corrNEs[531]=-0.004; 
   ids[532]='P066';    sts[532]='JacumbaAp_CS2004';    lats[532]=32.6165248264;    lngs[532]=243.8302135055;    ves[532]=-28.38;    vns[532]=26.84;    vus[532]=-2.22;    stddevNs[532]=0.14;    stddevEs[532]=0.16;    stddevUs[532]=0.54;    corrNEs[532]=-0.011; 
   ids[533]='P067';    sts[533]='CleggRanchCS2004';    lats[533]=35.5517531528;    lngs[533]=238.9970397550;    ves[533]=-29.56;    vns[533]=34.47;    vus[533]=-1.18;    stddevNs[533]=0.2;    stddevEs[533]=0.18;    stddevUs[533]=0.78;    corrNEs[533]=-0.010; 
   ids[534]='P067';    sts[534]='CleggRanchCS2004';    lats[534]=35.5517531674;    lngs[534]=238.9970397464;    ves[534]=-29.56;    vns[534]=34.47;    vus[534]=-1.18;    stddevNs[534]=0.2;    stddevEs[534]=0.18;    stddevUs[534]=0.78;    corrNEs[534]=-0.010; 
   ids[535]='P068';    sts[535]='MiddlegateNV2006';    lats[535]=39.3056436091;    lngs[535]=242.0152116704;    ves[535]=-4.71;    vns[535]=1.47;    vus[535]=-1.06;    stddevNs[535]=0.17;    stddevEs[535]=0.17;    stddevUs[535]=0.67;    corrNEs[535]=-0.009; 
   ids[536]='P069';    sts[536]='IndianPeakNV2006';    lats[536]=39.2879409862;    lngs[536]=242.3951053436;    ves[536]=-4.44;    vns[536]=0.95;    vus[536]=-1.18;    stddevNs[536]=0.17;    stddevEs[536]=0.17;    stddevUs[536]=0.62;    corrNEs[536]=-0.008; 
   ids[537]='P070';    sts[537]='WagonMoundNM2007';    lats[537]=36.0447802196;    lngs[537]=255.3020004941;    ves[537]=-0.77;    vns[537]=-0.25;    vus[537]=-3.79;    stddevNs[537]=0.24;    stddevEs[537]=0.31;    stddevUs[537]=1.39;    corrNEs[537]=0.001; 
   ids[538]='P070';    sts[538]='WagonMoundNM2007';    lats[538]=36.0447802167;    lngs[538]=255.3020004832;    ves[538]=-0.77;    vns[538]=-0.25;    vus[538]=-3.79;    stddevNs[538]=0.24;    stddevEs[538]=0.31;    stddevUs[538]=1.39;    corrNEs[538]=0.001; 
   ids[539]='P071';    sts[539]='RailroadPsNV2006';    lats[539]=39.3465477726;    lngs[539]=242.5988193844;    ves[539]=-4.38;    vns[539]=0.67;    vus[539]=-1.12;    stddevNs[539]=0.17;    stddevEs[539]=0.17;    stddevUs[539]=0.65;    corrNEs[539]=-0.011; 
   ids[540]='P072';    sts[540]='DryCreekRnNV2005';    lats[540]=39.5214555433;    lngs[540]=243.2591039062;    ves[540]=-4.12;    vns[540]=0.56;    vus[540]=-1.33;    stddevNs[540]=0.16;    stddevEs[540]=0.16;    stddevUs[540]=0.49;    corrNEs[540]=-0.009; 
   ids[541]='P073';    sts[541]='TwinSpringNV2006';    lats[541]=39.5005247932;    lngs[541]=243.5755299793;    ves[541]=-4.04;    vns[541]=0.42;    vus[541]=-1.97;    stddevNs[541]=0.16;    stddevEs[541]=0.17;    stddevUs[541]=0.54;    corrNEs[541]=-0.008; 
   ids[542]='P074';    sts[542]='BigReilleyNV2006';    lats[542]=39.5464275500;    lngs[542]=243.9504030563;    ves[542]=-3.96;    vns[542]=0.15;    vus[542]=-1.58;    stddevNs[542]=0.17;    stddevEs[542]=0.17;    stddevUs[542]=0.55;    corrNEs[542]=-0.007; 
   ids[543]='P075';    sts[543]='Southgate_NV2006';    lats[543]=39.3742439265;    lngs[543]=244.1111826105;    ves[543]=-3.41;    vns[543]=-0.79;    vus[543]=-2.36;    stddevNs[543]=0.4;    stddevEs[543]=0.27;    stddevUs[543]=0.53;    corrNEs[543]=-0.002; 
   ids[544]='P076';    sts[544]='DryMountaiNV2006';    lats[544]=39.5360629195;    lngs[544]=244.4874017667;    ves[544]=-4.02;    vns[544]=0.05;    vus[544]=-2.34;    stddevNs[544]=0.17;    stddevEs[544]=0.17;    stddevUs[544]=0.56;    corrNEs[544]=-0.008; 
   ids[545]='P077';    sts[545]='RavenLoft_NV2008';    lats[545]=39.3885615158;    lngs[545]=244.5681759349;    ves[545]=-4.84;    vns[545]=-0.31;    vus[545]=-2.09;    stddevNs[545]=0.25;    stddevEs[545]=0.29;    stddevUs[545]=1.08;    corrNEs[545]=-0.000; 
   ids[546]='P078';    sts[546]='LonePineCnNV2007';    lats[546]=40.4334172475;    lngs[546]=242.1201954985;    ves[546]=-5.47;    vns[546]=1.4;    vus[546]=-0.45;    stddevNs[546]=0.22;    stddevEs[546]=0.2;    stddevUs[546]=0.75;    corrNEs[546]=-0.005; 
   ids[547]='P079';    sts[547]='SuccessSmtNV2007';    lats[547]=39.2552096359;    lngs[547]=245.3083427504;    ves[547]=-3.07;    vns[547]=0.78;    vus[547]=-2.37;    stddevNs[547]=0.28;    stddevEs[547]=0.31;    stddevUs[547]=1.01;    corrNEs[547]=0.000; 
   ids[548]='P080';    sts[548]='BlackhorseNV2006';    lats[548]=39.1194392716;    lngs[548]=245.7227849271;    ves[548]=-3.88;    vns[548]=-0.05;    vus[548]=-1.95;    stddevNs[548]=0.18;    stddevEs[548]=0.17;    stddevUs[548]=0.6;    corrNEs[548]=-0.006; 
   ids[549]='P081';    sts[549]='Buckskins_UT2006';    lats[549]=39.0673072671;    lngs[549]=246.1286727495;    ves[549]=-4.11;    vns[549]=0.05;    vus[549]=-2.12;    stddevNs[549]=0.18;    stddevEs[549]=0.16;    stddevUs[549]=0.7;    corrNEs[549]=-0.006; 
   ids[550]='P082';    sts[550]='TuleValleyUT2006';    lats[550]=39.2689350775;    lngs[550]=246.4947922348;    ves[550]=-3.83;    vns[550]=-0.2;    vus[550]=-2.01;    stddevNs[550]=0.19;    stddevEs[550]=0.16;    stddevUs[550]=0.69;    corrNEs[550]=-0.006; 
   ids[551]='P083';    sts[551]='NeedlePeakNV2007';    lats[551]=40.3205815278;    lngs[551]=242.5323636754;    ves[551]=-4.48;    vns[551]=0.67;    vus[551]=-0.51;    stddevNs[551]=0.2;    stddevEs[551]=0.21;    stddevUs[551]=0.79;    corrNEs[551]=-0.006; 
   ids[552]='P084';    sts[552]='CedarMountUT2006';    lats[552]=40.4939528055;    lngs[552]=246.9459969857;    ves[552]=-3.91;    vns[552]=-0.45;    vus[552]=-1.68;    stddevNs[552]=0.17;    stddevEs[552]=0.15;    stddevUs[552]=0.76;    corrNEs[552]=-0.006; 
   ids[553]='P085';    sts[553]='SlavenCyn_NV2006';    lats[553]=40.4954616575;    lngs[553]=243.2637878418;    ves[553]=-3.98;    vns[553]=-0.12;    vus[553]=-0.75;    stddevNs[553]=0.17;    stddevEs[553]=0.16;    stddevUs[553]=0.69;    corrNEs[553]=-0.008; 
   ids[554]='P086';    sts[554]='Kennecott1UT2005';    lats[554]=40.6487949826;    lngs[554]=247.7179276915;    ves[554]=-2.95;    vns[554]=-0.05;    vus[554]=-0.04;    stddevNs[554]=0.23;    stddevEs[554]=0.15;    stddevUs[554]=0.65;    corrNEs[554]=-0.004; 
   ids[555]='P087';    sts[555]='CortezRangNV2006';    lats[555]=40.3632059896;    lngs[555]=243.7207361714;    ves[555]=-4.01;    vns[555]=0.23;    vus[555]=-1.74;    stddevNs[555]=0.19;    stddevEs[555]=0.17;    stddevUs[555]=0.66;    corrNEs[555]=-0.006; 
   ids[556]='P088';    sts[556]='LittleMtStUT2006';    lats[556]=40.7717695162;    lngs[556]=248.2771089623;    ves[556]=-1.47;    vns[556]=-0.42;    vus[556]=-1.46;    stddevNs[556]=0.23;    stddevEs[556]=0.16;    stddevUs[556]=0.74;    corrNEs[556]=-0.004; 
   ids[557]='P089';    sts[557]='Wanship___UT2004';    lats[557]=40.8070870113;    lngs[557]=248.5847110517;    ves[557]=-0.79;    vns[557]=0.28;    vus[557]=-2.08;    stddevNs[557]=0.17;    stddevEs[557]=0.15;    stddevUs[557]=0.69;    corrNEs[557]=-0.005; 
   ids[558]='P090';    sts[558]='DRI_Reno__NV2007';    lats[558]=39.5728037657;    lngs[558]=240.2001471529;    ves[558]=-6.42;    vns[558]=4.93;    vus[558]=-0.12;    stddevNs[558]=0.24;    stddevEs[558]=0.35;    stddevUs[558]=0.9;    corrNEs[558]=-0.002; 
   ids[559]='P091';    sts[559]='Hunter_MtnCS2007';    lats[559]=36.6143244969;    lngs[559]=242.4684589605;    ves[559]=-5.97;    vns[559]=3.85;    vus[559]=-2.29;    stddevNs[559]=0.18;    stddevEs[559]=0.18;    stddevUs[559]=0.78;    corrNEs[559]=-0.008; 
   ids[560]='P092';    sts[560]='White__TopCS2006';    lats[560]=36.8041946434;    lngs[560]=242.5931981523;    ves[560]=-5.41;    vns[560]=3.15;    vus[560]=-1.27;    stddevNs[560]=0.2;    stddevEs[560]=0.2;    stddevUs[560]=0.85;    corrNEs[560]=-0.008; 
   ids[561]='P093';    sts[561]='HaystackHiCS2007';    lats[561]=36.6060253415;    lngs[561]=242.0058556726;    ves[561]=-8.15;    vns[561]=6.34;    vus[561]=-1.78;    stddevNs[561]=0.19;    stddevEs[561]=0.19;    stddevUs[561]=0.79;    corrNEs[561]=-0.006; 
   ids[562]='P094';    sts[562]='LastChanceCS2006';    lats[562]=37.2008361219;    lngs[562]=242.2958061619;    ves[562]=-5.95;    vns[562]=2.91;    vus[562]=-1.48;    stddevNs[562]=0.17;    stddevEs[562]=0.18;    stddevUs[562]=0.7;    corrNEs[562]=-0.009; 
   ids[563]='P095';    sts[563]='SpanishSpgNV2005';    lats[563]=39.6984309419;    lngs[563]=240.4630953042;    ves[563]=-7.41;    vns[563]=4.4;    vus[563]=-0.5;    stddevNs[563]=0.15;    stddevEs[563]=0.18;    stddevUs[563]=0.57;    corrNEs[563]=-0.009; 
   ids[564]='P096';    sts[564]='LittleVallNV2007';    lats[564]=39.7955653208;    lngs[564]=240.6943577262;    ves[564]=-7.07;    vns[564]=3.5;    vus[564]=-0.91;    stddevNs[564]=0.23;    stddevEs[564]=0.25;    stddevUs[564]=0.99;    corrNEs[564]=-0.002; 
   ids[565]='P097';    sts[565]='HotSpringsNV2007';    lats[565]=39.8388063659;    lngs[565]=241.1261397325;    ves[565]=-6.51;    vns[565]=2.89;    vus[565]=-1.35;    stddevNs[565]=0.2;    stddevEs[565]=0.22;    stddevUs[565]=1.04;    corrNEs[565]=-0.007; 
   ids[566]='P098';    sts[566]='TopogPeak_NV2007';    lats[566]=39.8888823809;    lngs[566]=241.3155860379;    ves[566]=-6.45;    vns[566]=2.55;    vus[566]=-1.03;    stddevNs[566]=0.2;    stddevEs[566]=0.2;    stddevUs[566]=1;    corrNEs[566]=-0.007; 
   ids[567]='P099';    sts[567]='FairviewPkNV2006';    lats[567]=39.2123205172;    lngs[567]=241.8406494670;    ves[567]=-4.99;    vns[567]=2.1;    vus[567]=-0.71;    stddevNs[567]=0.18;    stddevEs[567]=0.17;    stddevUs[567]=0.62;    corrNEs[567]=-0.008; 
   ids[568]='P100';    sts[568]='ParkValleyUT2007';    lats[568]=41.8568013936;    lngs[568]=246.7057850294;    ves[568]=-3.86;    vns[568]=-0.05;    vus[568]=-2.23;    stddevNs[568]=0.2;    stddevEs[568]=0.19;    stddevUs[568]=0.72;    corrNEs[568]=-0.003; 
   ids[569]='P101';    sts[569]='RandolphLLUT2005';    lats[569]=41.6922737242;    lngs[569]=248.7639842503;    ves[569]=-0.89;    vns[569]=-0.28;    vus[569]=-1.65;    stddevNs[569]=0.19;    stddevEs[569]=0.15;    stddevUs[569]=0.73;    corrNEs[569]=-0.004; 
   ids[570]='P102';    sts[570]='LittleBaldNV2006';    lats[570]=39.9249217786;    lngs[570]=244.4439932900;    ves[570]=-4.01;    vns[570]=0.31;    vus[570]=-1.77;    stddevNs[570]=0.18;    stddevEs[570]=0.17;    stddevUs[570]=0.65;    corrNEs[570]=-0.007; 
   ids[571]='P103';    sts[571]='redknolls_ut2008';    lats[571]=39.3451406852;    lngs[571]=246.9579233063;    ves[571]=-4.03;    vns[571]=0.2;    vus[571]=-1.57;    stddevNs[571]=0.33;    stddevEs[571]=0.32;    stddevUs[571]=1.45;    corrNEs[571]=0.003; 
   ids[572]='P104';    sts[572]='BlackRock_UT2006';    lats[572]=39.1860834485;    lngs[572]=247.2828800715;    ves[572]=-3.33;    vns[572]=-0.01;    vus[572]=-1.85;    stddevNs[572]=0.26;    stddevEs[572]=0.16;    stddevUs[572]=0.66;    corrNEs[572]=-0.004; 
   ids[573]='P104';    sts[573]='BlackRock_UT2006';    lats[573]=39.1860834465;    lngs[573]=247.2828800657;    ves[573]=-3.33;    vns[573]=-0.01;    vus[573]=-1.85;    stddevNs[573]=0.26;    stddevEs[573]=0.16;    stddevUs[573]=0.66;    corrNEs[573]=-0.004; 
   ids[574]='P105';    sts[574]='DeltaMuni_UT2004';    lats[574]=39.3875438950;    lngs[574]=247.4959120854;    ves[574]=-3.23;    vns[574]=-0.13;    vus[574]=-2.5;    stddevNs[574]=0.27;    stddevEs[574]=0.14;    stddevUs[574]=1.47;    corrNEs[574]=-0.004; 
   ids[575]='P106';    sts[575]='FoolCreek_UT2006';    lats[575]=39.4589569458;    lngs[575]=247.7376550183;    ves[575]=-3.48;    vns[575]=-0.31;    vus[575]=-2.13;    stddevNs[575]=0.18;    stddevEs[575]=0.15;    stddevUs[575]=0.62;    corrNEs[575]=-0.005; 
   ids[576]='P107';    sts[576]='Grants____NM2006';    lats[576]=35.1321805777;    lngs[576]=252.1199802508;    ves[576]=-1.51;    vns[576]=0.08;    vus[576]=-2.85;    stddevNs[576]=0.16;    stddevEs[576]=0.2;    stddevUs[576]=0.64;    corrNEs[576]=-0.002; 
   ids[577]='P108';    sts[577]='SageValleyUT2005';    lats[577]=39.5888788587;    lngs[577]=248.0554492485;    ves[577]=-2.35;    vns[577]=-0.23;    vus[577]=0.49;    stddevNs[577]=0.2;    stddevEs[577]=0.14;    stddevUs[577]=0.56;    corrNEs[577]=-0.004; 
   ids[578]='P109';    sts[578]='FountGreenUT2007';    lats[578]=39.5974859056;    lngs[578]=248.3491673959;    ves[578]=-1.61;    vns[578]=-0.27;    vus[578]=-2.89;    stddevNs[578]=0.22;    stddevEs[578]=0.25;    stddevUs[578]=0.75;    corrNEs[578]=-0.002; 
   ids[579]='P110';    sts[579]='SanPeteValUT2005';    lats[579]=39.7152278119;    lngs[579]=248.4288583572;    ves[579]=-1.67;    vns[579]=3.95;    vus[579]=-0;    stddevNs[579]=1.75;    stddevEs[579]=0.48;    stddevUs[579]=0.99;    corrNEs[579]=0.000; 
   ids[580]='P110';    sts[580]='SanPeteValUT2005';    lats[580]=39.7152278333;    lngs[580]=248.4288583580;    ves[580]=-1.67;    vns[580]=3.95;    vus[580]=-0;    stddevNs[580]=1.75;    stddevEs[580]=0.48;    stddevUs[580]=0.99;    corrNEs[580]=0.000; 
   ids[581]='P110';    sts[581]='SanPeteValUT2005';    lats[581]=39.7152278309;    lngs[581]=248.4288583475;    ves[581]=-1.67;    vns[581]=3.95;    vus[581]=-0;    stddevNs[581]=1.75;    stddevEs[581]=0.48;    stddevUs[581]=0.99;    corrNEs[581]=0.000; 
   ids[582]='P111';    sts[582]='WildcatHlsUT2006';    lats[582]=41.8173204422;    lngs[582]=246.9878438015;    ves[582]=-3.57;    vns[582]=-0.69;    vus[582]=-1.37;    stddevNs[582]=0.2;    stddevEs[582]=0.15;    stddevUs[582]=0.72;    corrNEs[582]=-0.005; 
   ids[583]='P112';    sts[583]='Indianola_UT2005';    lats[583]=39.8169061128;    lngs[583]=248.5500039859;    ves[583]=-1.39;    vns[583]=-0.23;    vus[583]=-1.3;    stddevNs[583]=0.18;    stddevEs[583]=0.14;    stddevUs[583]=0.68;    corrNEs[583]=-0.005; 
   ids[584]='P113';    sts[584]='KnollsWestUT2006';    lats[584]=40.6713047611;    lngs[584]=246.7219664422;    ves[584]=-3.94;    vns[584]=-0.52;    vus[584]=-1.03;    stddevNs[584]=0.18;    stddevEs[584]=0.15;    stddevUs[584]=0.79;    corrNEs[584]=-0.006; 
   ids[585]='P114';    sts[585]='BurgessRchUT2005';    lats[585]=40.6339857004;    lngs[585]=247.4723707963;    ves[585]=-3.27;    vns[585]=-0.53;    vus[585]=-0.81;    stddevNs[585]=0.16;    stddevEs[585]=0.15;    stddevUs[585]=0.6;    corrNEs[585]=-0.006; 
   ids[586]='P115';    sts[586]='GrantsVillUT2007';    lats[586]=40.4744232730;    lngs[586]=247.5720030886;    ves[586]=-3.61;    vns[586]=-0.35;    vus[586]=-0.82;    stddevNs[586]=0.21;    stddevEs[586]=0.18;    stddevUs[586]=0.71;    corrNEs[586]=-0.005; 
   ids[587]='P116';    sts[587]='LatimerPntUT2007';    lats[587]=40.4340248891;    lngs[587]=247.9857711503;    ves[587]=-3.19;    vns[587]=-0.41;    vus[587]=-1.78;    stddevNs[587]=0.19;    stddevEs[587]=0.19;    stddevUs[587]=0.89;    corrNEs[587]=-0.003; 
   ids[588]='P117';    sts[588]='AmericanFCUT2006';    lats[588]=40.4351642402;    lngs[588]=248.2485765824;    ves[588]=-3.46;    vns[588]=-1.29;    vus[588]=-0.13;    stddevNs[588]=0.23;    stddevEs[588]=0.17;    stddevUs[588]=0.95;    corrNEs[588]=0.002; 
   ids[589]='P118';    sts[589]='KamasLloydUT2005';    lats[589]=40.6354892015;    lngs[589]=248.6501316995;    ves[589]=-1.13;    vns[589]=-0.33;    vus[589]=-0.96;    stddevNs[589]=0.2;    stddevEs[589]=0.16;    stddevUs[589]=0.76;    corrNEs[589]=-0.003; 
   ids[590]='P119';    sts[590]='MahoganyHRUT2005';    lats[590]=40.7317857461;    lngs[590]=248.7422693875;    ves[590]=-0.98;    vns[590]=-0.31;    vus[590]=-1.64;    stddevNs[590]=0.2;    stddevEs[590]=0.15;    stddevUs[590]=0.6;    corrNEs[590]=-0.006; 
   ids[591]='P120';    sts[591]='ClinesCornNM2007';    lats[591]=35.0074559831;    lngs[591]=254.3739244684;    ves[591]=-1.26;    vns[591]=-0.18;    vus[591]=-1.49;    stddevNs[591]=0.21;    stddevEs[591]=0.33;    stddevUs[591]=0.85;    corrNEs[591]=-0.001; 
   ids[592]='P121';    sts[592]='HNSLVALLY_UT2004';    lats[592]=41.8033883943;    lngs[592]=247.3017098060;    ves[592]=-3.26;    vns[592]=-0.6;    vus[592]=-1.12;    stddevNs[592]=0.18;    stddevEs[592]=0.13;    stddevUs[592]=0.56;    corrNEs[592]=-0.007; 
   ids[593]='P122';    sts[593]='CNRSPRNGS_UT2004';    lats[593]=41.6353635742;    lngs[593]=247.6680839050;    ves[593]=-2.44;    vns[593]=-0.74;    vus[593]=-0.98;    stddevNs[593]=0.17;    stddevEs[593]=0.13;    stddevUs[593]=0.67;    corrNEs[593]=-0.007; 
   ids[594]='P122';    sts[594]='CNRSPRNGS_UT2004';    lats[594]=41.6353635647;    lngs[594]=247.6680839073;    ves[594]=-2.44;    vns[594]=-0.74;    vus[594]=-0.98;    stddevNs[594]=0.17;    stddevEs[594]=0.13;    stddevUs[594]=0.67;    corrNEs[594]=-0.007; 
   ids[595]='P122';    sts[595]='CNRSPRNGS_UT2004';    lats[595]=41.6353635626;    lngs[595]=247.6680839087;    ves[595]=-2.44;    vns[595]=-0.74;    vus[595]=-0.98;    stddevNs[595]=0.17;    stddevEs[595]=0.13;    stddevUs[595]=0.67;    corrNEs[595]=-0.007; 
   ids[596]='P123';    sts[596]='TresPiedraNM2006';    lats[596]=36.6351716210;    lngs[596]=254.0891535488;    ves[596]=-1.27;    vns[596]=-0.14;    vus[596]=-3.85;    stddevNs[596]=0.18;    stddevEs[596]=0.27;    stddevUs[596]=0.64;    corrNEs[596]=-0.001; 
   ids[597]='P124';    sts[597]='WellsvilleUT2007';    lats[597]=41.5575912270;    lngs[597]=248.0426329940;    ves[597]=-1.35;    vns[597]=-0.25;    vus[597]=-2.45;    stddevNs[597]=0.43;    stddevEs[597]=0.25;    stddevUs[597]=0.77;    corrNEs[597]=-0.001; 
   ids[598]='P125';    sts[598]='BankheadWlUT2007';    lats[598]=41.5890277688;    lngs[598]=248.1010748429;    ves[598]=-2.15;    vns[598]=-0.39;    vus[598]=-3.13;    stddevNs[598]=0.21;    stddevEs[598]=0.28;    stddevUs[598]=0.85;    corrNEs[598]=-0.002; 
   ids[599]='P126';    sts[599]='Coldwater_UT2007';    lats[599]=41.5832447199;    lngs[599]=248.2194596323;    ves[599]=-1.91;    vns[599]=-0.31;    vus[599]=-4.34;    stddevNs[599]=0.21;    stddevEs[599]=0.24;    stddevUs[599]=0.95;    corrNEs[599]=0.007; 
   ids[600]='P127';    sts[600]='LockwoodRLNV2005';    lats[600]=39.4992140765;    lngs[600]=240.4000756680;    ves[600]=-7.73;    vns[600]=4.83;    vus[600]=-0.54;    stddevNs[600]=0.17;    stddevEs[600]=0.16;    stddevUs[600]=0.55;    corrNEs[600]=-0.010; 
   ids[601]='P128';    sts[601]='BangoJunctNV2006';    lats[601]=39.4860986851;    lngs[601]=240.9313597839;    ves[601]=-6.02;    vns[601]=3.85;    vus[601]=-0.95;    stddevNs[601]=0.2;    stddevEs[601]=0.2;    stddevUs[601]=0.86;    corrNEs[601]=-0.006; 
   ids[602]='P128';    sts[602]='BangoJunctNV2006';    lats[602]=39.4860986834;    lngs[602]=240.9313597817;    ves[602]=-6.02;    vns[602]=3.85;    vus[602]=-0.95;    stddevNs[602]=0.2;    stddevEs[602]=0.2;    stddevUs[602]=0.86;    corrNEs[602]=-0.006; 
   ids[603]='P129';    sts[603]='DeadCowPt_NV2007';    lats[603]=39.1338935227;    lngs[603]=241.5503973048;    ves[603]=-5.35;    vns[603]=3.2;    vus[603]=-0.57;    stddevNs[603]=0.23;    stddevEs[603]=0.23;    stddevUs[603]=0.72;    corrNEs[603]=-0.003; 
   ids[604]='P130';    sts[604]='CamelHump_NV2006';    lats[604]=39.2680354422;    lngs[604]=241.0622411157;    ves[604]=-6.28;    vns[604]=4.02;    vus[604]=-1.06;    stddevNs[604]=0.18;    stddevEs[604]=0.17;    stddevUs[604]=0.58;    corrNEs[604]=-0.009; 
   ids[605]='P131';    sts[605]='LaPlataCynNV2007';    lats[605]=39.4546859205;    lngs[605]=241.6487321498;    ves[605]=-5.93;    vns[605]=2.56;    vus[605]=-0.58;    stddevNs[605]=0.24;    stddevEs[605]=0.23;    stddevUs[605]=0.75;    corrNEs[605]=-0.003; 
   ids[606]='P132';    sts[606]='ParadiseRdNV2006';    lats[606]=38.7288440133;    lngs[606]=241.9951315968;    ves[606]=-5.02;    vns[606]=2.12;    vus[606]=-1.76;    stddevNs[606]=0.17;    stddevEs[606]=0.18;    stddevUs[606]=0.56;    corrNEs[606]=-0.009; 
   ids[607]='P133';    sts[607]='BuckleyFltNV2006';    lats[607]=38.7246023798;    lngs[607]=241.5397878909;    ves[607]=-5.61;    vns[607]=3.77;    vus[607]=-0.77;    stddevNs[607]=0.17;    stddevEs[607]=0.17;    stddevUs[607]=0.57;    corrNEs[607]=-0.009; 
   ids[608]='P134';    sts[608]='MinersRidgNV2006';    lats[608]=38.9808758330;    lngs[608]=241.0695870882;    ves[608]=-6.9;    vns[608]=4.85;    vus[608]=-0.98;    stddevNs[608]=0.17;    stddevEs[608]=0.17;    stddevUs[608]=0.6;    corrNEs[608]=-0.009; 
   ids[609]='P135';    sts[609]='Cambridge_NV2006';    lats[609]=38.7046219152;    lngs[609]=240.9846290646;    ves[609]=-7.28;    vns[609]=5.96;    vus[609]=-0.8;    stddevNs[609]=0.24;    stddevEs[609]=0.2;    stddevUs[609]=0.8;    corrNEs[609]=-0.005; 
   ids[610]='P135';    sts[610]='Cambridge_NV2006';    lats[610]=38.7046219151;    lngs[610]=240.9846290655;    ves[610]=-7.28;    vns[610]=5.96;    vus[610]=-0.8;    stddevNs[610]=0.24;    stddevEs[610]=0.2;    stddevUs[610]=0.8;    corrNEs[610]=-0.005; 
   ids[611]='P136';    sts[611]='S_Camp_CynNV2006';    lats[611]=38.7613672319;    lngs[611]=240.5414740232;    ves[611]=-8.49;    vns[611]=6.44;    vus[611]=-1.02;    stddevNs[611]=0.18;    stddevEs[611]=0.18;    stddevUs[611]=0.65;    corrNEs[611]=-0.010; 
   ids[612]='P137';    sts[612]='Granitepk_NV2007';    lats[612]=40.7797956852;    lngs[612]=240.5232333554;    ves[612]=-5.69;    vns[612]=3.1;    vus[612]=-1.05;    stddevNs[612]=0.26;    stddevEs[612]=0.33;    stddevUs[612]=0.93;    corrNEs[612]=-0.000; 
   ids[613]='P138';    sts[613]='BluewingFtNV2007';    lats[613]=40.2421785603;    lngs[613]=240.9898355340;    ves[613]=-6.07;    vns[613]=3.05;    vus[613]=-0.8;    stddevNs[613]=0.19;    stddevEs[613]=0.21;    stddevUs[613]=1.06;    corrNEs[613]=-0.008; 
   ids[614]='P139';    sts[614]='PaiuteCyn_NV2006';    lats[614]=39.9081994438;    lngs[614]=240.2775486327;    ves[614]=-7.6;    vns[614]=4.33;    vus[614]=-0.48;    stddevNs[614]=0.17;    stddevEs[614]=0.17;    stddevUs[614]=0.72;    corrNEs[614]=-0.008; 
   ids[615]='P140';    sts[615]='SlateMtn__CN2006';    lats[615]=38.8292351907;    lngs[615]=239.3068059106;    ves[615]=-10.93;    vns[615]=7.38;    vus[615]=-0.63;    stddevNs[615]=0.21;    stddevEs[615]=0.19;    stddevUs[615]=0.8;    corrNEs[615]=-0.003; 
   ids[616]='P141';    sts[616]='BunkerhillCN2006';    lats[616]=39.0466328997;    lngs[616]=239.6144164460;    ves[616]=-10.59;    vns[616]=7.34;    vus[616]=0.41;    stddevNs[616]=0.2;    stddevEs[616]=0.22;    stddevUs[616]=0.87;    corrNEs[616]=-0.011; 
   ids[617]='P142';    sts[617]='voltairecnnv2008';    lats[617]=39.1235332711;    lngs[617]=240.1892831615;    ves[617]=-6.53;    vns[617]=10.55;    vus[617]=21.86;    stddevNs[617]=0.81;    stddevEs[617]=0.81;    stddevUs[617]=2.99;    corrNEs[617]=-0.020; 
   ids[618]='P142';    sts[618]='voltairecnnv2008';    lats[618]=39.1235332630;    lngs[618]=240.1892831309;    ves[618]=-6.53;    vns[618]=10.55;    vus[618]=21.86;    stddevNs[618]=0.81;    stddevEs[618]=0.81;    stddevUs[618]=2.99;    corrNEs[618]=-0.020; 
   ids[619]='P143';    sts[619]='IndianCrk_CN2007';    lats[619]=38.7601669570;    lngs[619]=240.2351641308;    ves[619]=-10.35;    vns[619]=7.19;    vus[619]=-0.66;    stddevNs[619]=0.29;    stddevEs[619]=0.3;    stddevUs[619]=0.97;    corrNEs[619]=-0.003; 
   ids[620]='P144';    sts[620]='PliocenRdgCN2007';    lats[620]=39.4667040389;    lngs[620]=239.1070144591;    ves[620]=-10.64;    vns[620]=6.73;    vus[620]=0.72;    stddevNs[620]=0.24;    stddevEs[620]=0.25;    stddevUs[620]=1.19;    corrNEs[620]=0.001; 
   ids[621]='P145';    sts[621]='Fern_PointNV2005';    lats[621]=41.3576885132;    lngs[621]=240.3756741097;    ves[621]=-5.16;    vns[621]=3.12;    vus[621]=-0.02;    stddevNs[621]=0.16;    stddevEs[621]=0.17;    stddevUs[621]=0.51;    corrNEs[621]=-0.009; 
   ids[622]='P146';    sts[622]='SignalPeakCN2006';    lats[622]=39.3374638771;    lngs[622]=239.4627004431;    ves[622]=-10.68;    vns[622]=7.02;    vus[622]=-0.73;    stddevNs[622]=0.24;    stddevEs[622]=0.23;    stddevUs[622]=0.81;    corrNEs[622]=-0.005; 
   ids[623]='P147';    sts[623]='DixieMtn__CN2007';    lats[623]=39.9373866774;    lngs[623]=239.7155867205;    ves[623]=-8.75;    vns[623]=5.16;    vus[623]=0.23;    stddevNs[623]=0.22;    stddevEs[623]=0.21;    stddevUs[623]=0.9;    corrNEs[623]=-0.005; 
   ids[624]='P148';    sts[624]='GoatMtn___CN2006';    lats[624]=40.4185882511;    lngs[624]=239.1940389907;    ves[624]=-7.44;    vns[624]=5.03;    vus[624]=-0.09;    stddevNs[624]=0.22;    stddevEs[624]=0.2;    stddevUs[624]=0.89;    corrNEs[624]=-0.006; 
   ids[625]='P149';    sts[625]='BabbittPk_CN2008';    lats[625]=39.6021301604;    lngs[625]=239.8950253474;    ves[625]=-9.37;    vns[625]=5.33;    vus[625]=0.13;    stddevNs[625]=1.68;    stddevEs[625]=1.73;    stddevUs[625]=3.05;    corrNEs[625]=0.001; 
   ids[626]='P150';    sts[626]='MartisPeakCN2008';    lats[626]=39.2923805265;    lngs[626]=239.9661467951;    ves[626]=-10.63;    vns[626]=6.53;    vus[626]=-2.14;    stddevNs[626]=0.77;    stddevEs[626]=1.22;    stddevUs[626]=2.18;    corrNEs[626]=-0.001; 
   ids[627]='P151';    sts[627]='SierraArmyCN2008';    lats[627]=40.2889017997;    lngs[627]=239.9227368469;    ves[627]=-7.62;    vns[627]=4.37;    vus[627]=-1.3;    stddevNs[627]=0.96;    stddevEs[627]=0.59;    stddevUs[627]=2.54;    corrNEs[627]=0.000; 
   ids[628]='P154';    sts[628]='IshkeshRchCN2007';    lats[628]=41.8070829136;    lngs[628]=236.6399501408;    ves[628]=-2.66;    vns[628]=8.49;    vus[628]=0.62;    stddevNs[628]=0.25;    stddevEs[628]=0.24;    stddevUs[628]=1.08;    corrNEs[628]=-0.007; 
   ids[629]='P155';    sts[629]='BlueRidge_CN2007';    lats[629]=41.2724351324;    lngs[629]=236.8112142042;    ves[629]=-4.71;    vns[629]=8.27;    vus[629]=-0.27;    stddevNs[629]=1.26;    stddevEs[629]=1.56;    stddevUs[629]=2.71;    corrNEs[629]=0.002; 
   ids[630]='P156';    sts[630]='GibsonRdg_CN2006';    lats[630]=40.0244426847;    lngs[630]=236.0938833202;    ves[630]=-21.31;    vns[630]=23.59;    vus[630]=-0.53;    stddevNs[630]=0.23;    stddevEs[630]=0.29;    stddevUs[630]=1;    corrNEs[630]=-0.009; 
   ids[631]='P157';    sts[631]='GordaTwo__CN2006';    lats[631]=40.2475467667;    lngs[631]=235.6919267677;    ves[631]=-20.51;    vns[631]=32.12;    vus[631]=-1.1;    stddevNs[631]=0.18;    stddevEs[631]=0.27;    stddevUs[631]=0.75;    corrNEs[631]=-0.008; 
   ids[632]='P158';    sts[632]='MonumntRdgCN2004';    lats[632]=40.4224907214;    lngs[632]=235.8927922247;    ves[632]=-6.8;    vns[632]=22.96;    vus[632]=1.09;    stddevNs[632]=0.17;    stddevEs[632]=0.19;    stddevUs[632]=0.66;    corrNEs[632]=-0.011; 
   ids[633]='P158';    sts[633]='MonumntRdgCN2004';    lats[633]=40.4224907110;    lngs[633]=235.8927921985;    ves[633]=-6.8;    vns[633]=22.96;    vus[633]=1.09;    stddevNs[633]=0.17;    stddevEs[633]=0.19;    stddevUs[633]=0.66;    corrNEs[633]=-0.011; 
   ids[634]='P159';    sts[634]='BearRvrRdgCN2006';    lats[634]=40.5047870494;    lngs[634]=235.7172181927;    ves[634]=-4.08;    vns[634]=26.03;    vus[634]=-1.61;    stddevNs[634]=0.22;    stddevEs[634]=0.23;    stddevUs[634]=0.75;    corrNEs[634]=-0.007; 
   ids[635]='P160';    sts[635]='RohnerAir_CN2005';    lats[635]=40.5512521869;    lngs[635]=235.8667292814;    ves[635]=-3.61;    vns[635]=20.62;    vus[635]=-0.24;    stddevNs[635]=0.17;    stddevEs[635]=0.22;    stddevUs[635]=0.7;    corrNEs[635]=-0.009; 
   ids[636]='P160';    sts[636]='RohnerAir_CN2005';    lats[636]=40.5512521892;    lngs[636]=235.8667292583;    ves[636]=-3.61;    vns[636]=20.62;    vus[636]=-0.24;    stddevNs[636]=0.17;    stddevEs[636]=0.22;    stddevUs[636]=0.7;    corrNEs[636]=-0.009; 
   ids[637]='P161';    sts[637]='OswaldPropCN2005';    lats[637]=40.6373632621;    lngs[637]=235.7869145747;    ves[637]=-0.14;    vns[637]=21.52;    vus[637]=-2.29;    stddevNs[637]=0.18;    stddevEs[637]=0.19;    stddevUs[637]=0.79;    corrNEs[637]=-0.011; 
   ids[638]='P161';    sts[638]='OswaldPropCN2005';    lats[638]=40.6373632628;    lngs[638]=235.7869145455;    ves[638]=-0.14;    vns[638]=21.52;    vus[638]=-2.29;    stddevNs[638]=0.18;    stddevEs[638]=0.19;    stddevUs[638]=0.79;    corrNEs[638]=-0.011; 
   ids[639]='P162';    sts[639]='RedwoodsCCCN2004';    lats[639]=40.6910950124;    lngs[639]=235.7629637470;    ves[639]=1.86;    vns[639]=20.89;    vus[639]=-1.77;    stddevNs[639]=0.15;    stddevEs[639]=0.28;    stddevUs[639]=0.72;    corrNEs[639]=-0.008; 
   ids[640]='P162';    sts[640]='RedwoodsCCCN2004';    lats[640]=40.6910950063;    lngs[640]=235.7629637099;    ves[640]=1.86;    vns[640]=20.89;    vus[640]=-1.77;    stddevNs[640]=0.15;    stddevEs[640]=0.28;    stddevUs[640]=0.72;    corrNEs[640]=-0.008; 
   ids[641]='P163';    sts[641]='PringleRdgCN2006';    lats[641]=40.2195717036;    lngs[641]=235.9427105459;    ves[641]=-16.05;    vns[641]=23.85;    vus[641]=0.87;    stddevNs[641]=0.19;    stddevEs[641]=0.22;    stddevUs[641]=0.81;    corrNEs[641]=-0.009; 
   ids[642]='P164';    sts[642]='PrattMtn__CN2004';    lats[642]=40.1192565971;    lngs[642]=236.3066599207;    ves[642]=-15.92;    vns[642]=15.35;    vus[642]=-0.73;    stddevNs[642]=0.16;    stddevEs[642]=0.25;    stddevUs[642]=0.68;    corrNEs[642]=-0.009; 
   ids[643]='P164';    sts[643]='PrattMtn__CN2004';    lats[643]=40.1192565969;    lngs[643]=236.3066599155;    ves[643]=-15.92;    vns[643]=15.35;    vus[643]=-0.73;    stddevNs[643]=0.16;    stddevEs[643]=0.25;    stddevUs[643]=0.68;    corrNEs[643]=-0.009; 
   ids[644]='P165';    sts[644]='BurghRanchCN2006';    lats[644]=40.2455489766;    lngs[644]=236.1467357102;    ves[644]=-13.2;    vns[644]=17.79;    vus[644]=1.12;    stddevNs[644]=0.23;    stddevEs[644]=0.24;    stddevUs[644]=0.86;    corrNEs[644]=-0.006; 
   ids[645]='P166';    sts[645]='ChalkMtn__CN2005';    lats[645]=40.4351835327;    lngs[645]=236.1371720525;    ves[645]=-7.52;    vns[645]=16.13;    vus[645]=0.19;    stddevNs[645]=0.19;    stddevEs[645]=0.22;    stddevUs[645]=0.69;    corrNEs[645]=-0.006; 
   ids[646]='P167';    sts[646]='BaldJesse_CN2005';    lats[646]=40.5437045638;    lngs[646]=236.1198230515;    ves[646]=-5.18;    vns[646]=15.59;    vus[646]=-1.12;    stddevNs[646]=0.21;    stddevEs[646]=0.27;    stddevUs[646]=0.62;    corrNEs[646]=-0.006; 
   ids[647]='P168';    sts[647]='IaquaButteCN2005';    lats[647]=40.6686377651;    lngs[647]=236.1185404196;    ves[647]=-2.59;    vns[647]=14.7;    vus[647]=0.24;    stddevNs[647]=0.19;    stddevEs[647]=0.28;    stddevUs[647]=0.62;    corrNEs[647]=-0.006; 
   ids[648]='P169';    sts[648]='FickleHillCA2004';    lats[648]=40.7911450683;    lngs[648]=236.0323477223;    ves[648]=-0.47;    vns[648]=15.44;    vus[648]=1.02;    stddevNs[648]=0.15;    stddevEs[648]=0.27;    stddevUs[648]=0.6;    corrNEs[648]=-0.009; 
   ids[649]='P169';    sts[649]='FickleHillCA2004';    lats[649]=40.7911450761;    lngs[649]=236.0323476956;    ves[649]=-0.47;    vns[649]=15.44;    vus[649]=1.02;    stddevNs[649]=0.15;    stddevEs[649]=0.27;    stddevUs[649]=0.6;    corrNEs[649]=-0.009; 
   ids[650]='P170';    sts[650]='BALDMTN___CA2004';    lats[650]=40.8802328915;    lngs[650]=236.1367191965;    ves[650]=-2.62;    vns[650]=12.17;    vus[650]=1.74;    stddevNs[650]=0.15;    stddevEs[650]=0.26;    stddevUs[650]=0.6;    corrNEs[650]=-0.008; 
   ids[651]='P170';    sts[651]='BALDMTN___CA2004';    lats[651]=40.8802328943;    lngs[651]=236.1367191670;    ves[651]=-2.62;    vns[651]=12.17;    vus[651]=1.74;    stddevNs[651]=0.15;    stddevEs[651]=0.26;    stddevUs[651]=0.6;    corrNEs[651]=-0.008; 
   ids[652]='P171';    sts[652]='SantaLuciaCN2004';    lats[652]=36.4855245567;    lngs[652]=238.2074814789;    ves[652]=-29.97;    vns[652]=35.73;    vus[652]=-2.03;    stddevNs[652]=0.21;    stddevEs[652]=0.19;    stddevUs[652]=0.5;    corrNEs[652]=-0.010; 
   ids[653]='P172';    sts[653]='PostRanch_CN2008';    lats[653]=36.2280745948;    lngs[653]=238.2327554323;    ves[653]=-30.65;    vns[653]=35.72;    vus[653]=-2.09;    stddevNs[653]=0.38;    stddevEs[653]=0.48;    stddevUs[653]=1.46;    corrNEs[653]=-0.003; 
   ids[654]='P173';    sts[654]='FortHunterCN2008';    lats[654]=35.9457167349;    lngs[654]=238.7096663804;    ves[654]=-29.62;    vns[654]=34.73;    vus[654]=-1.48;    stddevNs[654]=0.46;    stddevEs[654]=0.67;    stddevUs[654]=1.91;    corrNEs[654]=-0.002; 
   ids[655]='P174';    sts[655]='LlanoGrandCN2007';    lats[655]=36.3021517029;    lngs[655]=238.9491012108;    ves[655]=-29.42;    vns[655]=33.46;    vus[655]=-0.12;    stddevNs[655]=1.13;    stddevEs[655]=2.28;    stddevUs[655]=0.99;    corrNEs[655]=-0.000; 
   ids[656]='P174';    sts[656]='LlanoGrandCN2007';    lats[656]=36.3021517018;    lngs[656]=238.9491012171;    ves[656]=-29.42;    vns[656]=33.46;    vus[656]=-0.12;    stddevNs[656]=1.13;    stddevEs[656]=2.28;    stddevUs[656]=0.99;    corrNEs[656]=-0.000; 
   ids[657]='P175';    sts[657]='RosasCyn__CN2006';    lats[657]=36.4259027277;    lngs[657]=238.8651428592;    ves[657]=-29.4;    vns[657]=33.03;    vus[657]=1.17;    stddevNs[657]=0.64;    stddevEs[657]=0.95;    stddevUs[657]=0.9;    corrNEs[657]=-0.001; 
   ids[658]='P176';    sts[658]='MillsCreekCN2007';    lats[658]=37.4717727297;    lngs[658]=237.6428603205;    ves[658]=-25.63;    vns[658]=31.02;    vus[658]=-0.48;    stddevNs[658]=0.33;    stddevEs[658]=0.32;    stddevUs[658]=1.12;    corrNEs[658]=-0.002; 
   ids[659]='P177';    sts[659]='CoDeTierraCN2008';    lats[659]=37.5281676914;    lngs[659]=237.5049481709;    ves[659]=-24.58;    vns[659]=34.12;    vus[659]=-2.57;    stddevNs[659]=1.28;    stddevEs[659]=7.41;    stddevUs[659]=1.28;    corrNEs[659]=-0.000; 
   ids[660]='P177';    sts[660]='CoDeTierraCN2008';    lats[660]=37.5281676771;    lngs[660]=237.5049481575;    ves[660]=-24.58;    vns[660]=34.12;    vus[660]=-2.57;    stddevNs[660]=1.28;    stddevEs[660]=7.41;    stddevUs[660]=1.28;    corrNEs[660]=-0.000; 
   ids[661]='P178';    sts[661]='SanMateoCCCN2007';    lats[661]=37.5345191486;    lngs[661]=237.6676360254;    ves[661]=-24.21;    vns[661]=27.76;    vus[661]=-2.04;    stddevNs[661]=0.24;    stddevEs[661]=0.23;    stddevUs[661]=0.98;    corrNEs[661]=-0.009; 
   ids[662]='P179';    sts[662]='IllinoisAPOR2007';    lats[662]=42.0989721003;    lngs[662]=236.3144291229;    ves[662]=-1.65;    vns[662]=8.92;    vus[662]=-1.03;    stddevNs[662]=0.45;    stddevEs[662]=0.51;    stddevUs[662]=0.95;    corrNEs[662]=-0.002; 
   ids[663]='P180';    sts[663]='JuanFiestaCN2007';    lats[663]=36.2928324274;    lngs[663]=238.5967615350;    ves[663]=-28.16;    vns[663]=35.11;    vus[663]=-2.5;    stddevNs[663]=0.88;    stddevEs[663]=0.69;    stddevUs[663]=1.27;    corrNEs[663]=-0.001; 
   ids[664]='P180';    sts[664]='JuanFiestaCN2007';    lats[664]=36.2928324491;    lngs[664]=238.5967615067;    ves[664]=-28.16;    vns[664]=35.11;    vus[664]=-2.5;    stddevNs[664]=0.88;    stddevEs[664]=0.69;    stddevUs[664]=1.27;    corrNEs[664]=-0.001; 
   ids[665]='P181';    sts[665]='MillerKnoxCN2005';    lats[665]=37.9145453996;    lngs[665]=237.6232444697;    ves[665]=-20.36;    vns[665]=24.08;    vus[665]=-4.54;    stddevNs[665]=0.18;    stddevEs[665]=0.26;    stddevUs[665]=0.81;    corrNEs[665]=-0.008; 
   ids[666]='P181';    sts[666]='MillerKnoxCN2005';    lats[666]=37.9145453985;    lngs[666]=237.6232444760;    ves[666]=-20.36;    vns[666]=24.08;    vus[666]=-4.54;    stddevNs[666]=0.18;    stddevEs[666]=0.26;    stddevUs[666]=0.81;    corrNEs[666]=-0.008; 
   ids[667]='P182';    sts[667]='MeyersGradCN2006';    lats[667]=38.4950149506;    lngs[667]=236.8187545109;    ves[667]=-22.81;    vns[667]=31.82;    vus[667]=-2.53;    stddevNs[667]=0.19;    stddevEs[667]=0.21;    stddevUs[667]=0.84;    corrNEs[667]=-0.013; 
   ids[668]='P183';    sts[668]='BodegaHeadCN2006';    lats[668]=38.3136633016;    lngs[668]=236.9311127844;    ves[668]=-23.87;    vns[668]=32.81;    vus[668]=-2.13;    stddevNs[668]=0.18;    stddevEs[668]=0.21;    stddevUs[668]=0.8;    corrNEs[668]=-0.010; 
   ids[669]='P184';    sts[669]='Greenwood_CN2007';    lats[669]=39.1171652229;    lngs[669]=236.2910626083;    ves[669]=-24.27;    vns[669]=30.36;    vus[669]=-2.95;    stddevNs[669]=0.27;    stddevEs[669]=0.27;    stddevUs[669]=1.07;    corrNEs[669]=-0.006; 
   ids[670]='P185';    sts[670]='LilRvrAir_CN2006';    lats[670]=39.2613036789;    lngs[670]=236.2506643313;    ves[670]=-24.39;    vns[670]=28.97;    vus[670]=-2.27;    stddevNs[670]=0.2;    stddevEs[670]=0.22;    stddevUs[670]=0.89;    corrNEs[670]=-0.008; 
   ids[671]='P186';    sts[671]='HolmesRch_CN2007';    lats[671]=39.1501787305;    lngs[671]=236.4818478845;    ves[671]=-22.7;    vns[671]=25.83;    vus[671]=-2.74;    stddevNs[671]=0.48;    stddevEs[671]=0.29;    stddevUs[671]=1.31;    corrNEs[671]=-0.010; 
   ids[672]='P186';    sts[672]='HolmesRch_CN2007';    lats[672]=39.1501787330;    lngs[672]=236.4818478816;    ves[672]=-22.7;    vns[672]=25.83;    vus[672]=-2.74;    stddevNs[672]=0.48;    stddevEs[672]=0.29;    stddevUs[672]=1.31;    corrNEs[672]=-0.010; 
   ids[673]='P187';    sts[673]='ThreeChop_CN2005';    lats[673]=39.3524774844;    lngs[673]=236.3974587650;    ves[673]=-23.09;    vns[673]=23.83;    vus[673]=-1.73;    stddevNs[673]=0.19;    stddevEs[673]=0.26;    stddevUs[673]=0.98;    corrNEs[673]=-0.005; 
   ids[674]='P188';    sts[674]='BurntRidgeCN2006';    lats[674]=38.6678566065;    lngs[674]=236.7704365968;    ves[674]=-21.89;    vns[674]=29.16;    vus[674]=-1.84;    stddevNs[674]=0.23;    stddevEs[674]=0.23;    stddevUs[674]=1.04;    corrNEs[674]=-0.011; 
   ids[675]='P189';    sts[675]='Bradford__CN2005';    lats[675]=38.9874489741;    lngs[675]=236.6515711933;    ves[675]=-22.03;    vns[675]=26.43;    vus[675]=-0.57;    stddevNs[675]=0.2;    stddevEs[675]=0.19;    stddevUs[675]=0.82;    corrNEs[675]=-0.008; 
   ids[676]='P190';    sts[676]='UkiahNorthCN2005';    lats[676]=39.2419554568;    lngs[676]=236.7959523757;    ves[676]=-18.83;    vns[676]=18.02;    vus[676]=0.45;    stddevNs[676]=0.16;    stddevEs[676]=0.17;    stddevUs[676]=0.85;    corrNEs[676]=-0.014; 
   ids[677]='P191';    sts[677]='SiskiyouRCOR2007';    lats[677]=42.2753560367;    lngs[677]=236.3677372624;    ves[677]=-1.5;    vns[677]=8.66;    vus[677]=-1.13;    stddevNs[677]=0.32;    stddevEs[677]=0.34;    stddevUs[677]=0.82;    corrNEs[677]=-0.003; 
   ids[678]='P192';    sts[678]='PotrVlySchCN2005';    lats[678]=39.3197280754;    lngs[678]=236.8948108403;    ves[678]=-16.54;    vns[678]=14.98;    vus[678]=-0.16;    stddevNs[678]=0.19;    stddevEs[678]=0.17;    stddevUs[678]=0.79;    corrNEs[678]=-0.011; 
   ids[679]='P193';    sts[679]='PointReyesCN2007';    lats[679]=38.1229368437;    lngs[679]=237.0918589003;    ves[679]=-24.02;    vns[679]=32.98;    vus[679]=-2.42;    stddevNs[679]=0.24;    stddevEs[679]=0.28;    stddevUs[679]=0.86;    corrNEs[679]=-0.006; 
   ids[680]='P194';    sts[680]='WalkerCrk_CN2007';    lats[680]=38.1857162586;    lngs[680]=237.1837427631;    ves[680]=-21.04;    vns[680]=28.6;    vus[680]=-1.64;    stddevNs[680]=0.3;    stddevEs[680]=0.26;    stddevUs[680]=1.18;    corrNEs[680]=-0.005; 
   ids[681]='P195';    sts[681]='WineCreek_CN2007';    lats[681]=38.6646654363;    lngs[681]=237.0406921203;    ves[681]=-17.5;    vns[681]=25.68;    vus[681]=-0.85;    stddevNs[681]=0.24;    stddevEs[681]=0.27;    stddevUs[681]=1.16;    corrNEs[681]=-0.004; 
   ids[682]='P196';    sts[682]='MeachumLflCN2006';    lats[682]=38.2981438386;    lngs[682]=237.2573584360;    ves[682]=-18.75;    vns[682]=25.23;    vus[682]=-1.98;    stddevNs[682]=0.18;    stddevEs[682]=0.23;    stddevUs[682]=0.7;    corrNEs[682]=-0.009; 
   ids[683]='P197';    sts[683]='SantaRosa_CN2005';    lats[683]=38.4285612591;    lngs[683]=237.2326140858;    ves[683]=-18.61;    vns[683]=24.6;    vus[683]=-2.73;    stddevNs[683]=0.22;    stddevEs[683]=0.46;    stddevUs[683]=0.69;    corrNEs[683]=-0.003; 
   ids[684]='P198';    sts[684]='PetalumAirCN2004';    lats[684]=38.2598745957;    lngs[684]=237.3925497601;    ves[684]=-19.11;    vns[684]=22.61;    vus[684]=-6.37;    stddevNs[684]=0.2;    stddevEs[684]=0.27;    stddevUs[684]=0.74;    corrNEs[684]=-0.006; 
   ids[685]='P198';    sts[685]='PetalumAirCN2004';    lats[685]=38.2598745866;    lngs[685]=237.3925497842;    ves[685]=-19.11;    vns[685]=22.61;    vus[685]=-6.37;    stddevNs[685]=0.2;    stddevEs[685]=0.27;    stddevUs[685]=0.74;    corrNEs[685]=-0.006; 
   ids[686]='P199';    sts[686]='RodgersCrkCN2005';    lats[686]=38.2636921817;    lngs[686]=237.4965618170;    ves[686]=-15.04;    vns[686]=20;    vus[686]=-4.26;    stddevNs[686]=0.52;    stddevEs[686]=0.26;    stddevUs[686]=0.83;    corrNEs[686]=-0.003; 
   ids[687]='P199';    sts[687]='RodgersCrkCN2005';    lats[687]=38.2636921796;    lngs[687]=237.4965617978;    ves[687]=-15.04;    vns[687]=20;    vus[687]=-4.26;    stddevNs[687]=0.52;    stddevEs[687]=0.26;    stddevUs[687]=0.83;    corrNEs[687]=-0.003; 
   ids[688]='P200';    sts[688]='SonomaCrk_CN2005';    lats[688]=38.2398313875;    lngs[688]=237.5482981066;    ves[688]=-15.86;    vns[688]=19.25;    vus[688]=-4.54;    stddevNs[688]=0.23;    stddevEs[688]=0.27;    stddevUs[688]=0.61;    corrNEs[688]=-0.006; 
   ids[689]='P201';    sts[689]='MarkWstQryCN2008';    lats[689]=38.5598060704;    lngs[689]=237.3415657409;    ves[689]=-14.51;    vns[689]=18.28;    vus[689]=-0.02;    stddevNs[689]=0.26;    stddevEs[689]=0.28;    stddevUs[689]=1.34;    corrNEs[689]=-0.006; 
   ids[690]='P202';    sts[690]='NunnsCyn__CN2007';    lats[690]=38.4235808240;    lngs[690]=237.5040000423;    ves[690]=-15.06;    vns[690]=17.81;    vus[690]=-0.96;    stddevNs[690]=0.28;    stddevEs[690]=0.27;    stddevUs[690]=1.13;    corrNEs[690]=-0.007; 
   ids[691]='P203';    sts[691]='Mayacmas__CN2007';    lats[691]=38.8661126578;    lngs[691]=237.0829953799;    ves[691]=-12.69;    vns[691]=16.02;    vus[691]=-3.42;    stddevNs[691]=0.24;    stddevEs[691]=0.33;    stddevUs[691]=1.08;    corrNEs[691]=-0.006; 
   ids[692]='P203';    sts[692]='Mayacmas__CN2007';    lats[692]=38.8661126546;    lngs[692]=237.0829953748;    ves[692]=-12.69;    vns[692]=16.02;    vus[692]=-3.42;    stddevNs[692]=0.24;    stddevEs[692]=0.33;    stddevUs[692]=1.08;    corrNEs[692]=-0.006; 
   ids[693]='P204';    sts[693]='MaacamaCrkCN2007';    lats[693]=38.6664998797;    lngs[693]=237.2894664973;    ves[693]=-13.82;    vns[693]=19.31;    vus[693]=-2.35;    stddevNs[693]=0.26;    stddevEs[693]=0.24;    stddevUs[693]=1.02;    corrNEs[693]=-0.006; 
   ids[694]='P205';    sts[694]='LkPilsburyCN2007';    lats[694]=39.3981238043;    lngs[694]=237.0369356574;    ves[694]=-15.24;    vns[694]=14.13;    vus[694]=-0.25;    stddevNs[694]=0.39;    stddevEs[694]=0.42;    stddevUs[694]=0.96;    corrNEs[694]=-0.003; 
   ids[695]='P206';    sts[695]='CrazyCreekCN2006';    lats[695]=38.7778172195;    lngs[695]=237.4242065993;    ves[695]=-13.64;    vns[695]=15;    vus[695]=-1.34;    stddevNs[695]=0.22;    stddevEs[695]=0.18;    stddevUs[695]=0.8;    corrNEs[695]=-0.009; 
   ids[696]='P207';    sts[696]='GoatMtnMNFCN2008';    lats[696]=39.2600450204;    lngs[696]=237.2814325863;    ves[696]=-12.39;    vns[696]=7.8;    vus[696]=-0.9;    stddevNs[696]=0.5;    stddevEs[696]=0.68;    stddevUs[696]=1.51;    corrNEs[696]=-0.002; 
   ids[697]='P208';    sts[697]='SaltCanyonCN2006';    lats[697]=39.1093020167;    lngs[697]=237.6961310127;    ves[697]=-11.51;    vns[697]=6.68;    vus[697]=-0.05;    stddevNs[697]=0.2;    stddevEs[697]=0.28;    stddevUs[697]=0.76;    corrNEs[697]=-0.006; 
   ids[698]='P209';    sts[698]='BonnyDoon_CN2007';    lats[698]=37.0692546951;    lngs[698]=237.8732853102;    ves[698]=-27.78;    vns[698]=33.18;    vus[698]=-2.58;    stddevNs[698]=0.32;    stddevEs[698]=0.22;    stddevUs[698]=1.29;    corrNEs[698]=-0.001; 
   ids[699]='P210';    sts[699]='ElkhrnSlghCN2005';    lats[699]=36.8161376971;    lngs[699]=238.2681580356;    ves[699]=-29.06;    vns[699]=32.46;    vus[699]=-1.83;    stddevNs[699]=0.18;    stddevEs[699]=0.16;    stddevUs[699]=0.56;    corrNEs[699]=-0.013; 
   ids[700]='P211';    sts[700]='LewisRdLflCN2007';    lats[700]=36.8791760657;    lngs[700]=238.3019622909;    ves[700]=-28.6;    vns[700]=31.51;    vus[700]=1.31;    stddevNs[700]=0.31;    stddevEs[700]=0.37;    stddevUs[700]=1.22;    corrNEs[700]=-0.005; 
   ids[701]='P211';    sts[701]='LewisRdLflCN2007';    lats[701]=36.8791760615;    lngs[701]=238.3019623038;    ves[701]=-28.6;    vns[701]=31.51;    vus[701]=1.31;    stddevNs[701]=0.31;    stddevEs[701]=0.37;    stddevUs[701]=1.22;    corrNEs[701]=-0.005; 
   ids[702]='P211';    sts[702]='LewisRdLflCN2007';    lats[702]=36.8791760524;    lngs[702]=238.3019622715;    ves[702]=-28.6;    vns[702]=31.51;    vus[702]=1.31;    stddevNs[702]=0.31;    stddevEs[702]=0.37;    stddevUs[702]=1.22;    corrNEs[702]=-0.005; 
   ids[703]='P212';    sts[703]='LarkinVly_CN2006';    lats[703]=36.9620105967;    lngs[703]=238.1372665129;    ves[703]=-27.54;    vns[703]=32.02;    vus[703]=-0.92;    stddevNs[703]=0.29;    stddevEs[703]=0.18;    stddevUs[703]=0.71;    corrNEs[703]=-0.007; 
   ids[704]='P213';    sts[704]='LenihanDamCN2005';    lats[704]=37.2017128301;    lngs[704]=238.0091634728;    ves[704]=-23.09;    vns[704]=27.65;    vus[704]=-1.19;    stddevNs[704]=0.29;    stddevEs[704]=0.16;    stddevUs[704]=0.65;    corrNEs[704]=-0.009; 
   ids[705]='P214';    sts[705]='CorralitosCN2007';    lats[705]=37.0010201452;    lngs[705]=238.2034313606;    ves[705]=-26.26;    vns[705]=30.63;    vus[705]=-0.46;    stddevNs[705]=0.53;    stddevEs[705]=0.38;    stddevUs[705]=2.25;    corrNEs[705]=0.005; 
   ids[706]='P214';    sts[706]='CorralitosCN2007';    lats[706]=37.0010201299;    lngs[706]=238.2034313436;    ves[706]=-26.26;    vns[706]=30.63;    vus[706]=-0.46;    stddevNs[706]=0.53;    stddevEs[706]=0.38;    stddevUs[706]=2.25;    corrNEs[706]=0.005; 
   ids[707]='P215';    sts[707]='SalsipuedeCN2007';    lats[707]=37.0487852167;    lngs[707]=238.2370504027;    ves[707]=-23.62;    vns[707]=27.61;    vus[707]=-2.93;    stddevNs[707]=0.28;    stddevEs[707]=0.23;    stddevUs[707]=0.91;    corrNEs[707]=-0.006; 
   ids[708]='P216';    sts[708]='MtMadonna_CN2007';    lats[708]=37.0024260319;    lngs[708]=238.2737927710;    ves[708]=-24.42;    vns[708]=27.84;    vus[708]=-2.13;    stddevNs[708]=0.32;    stddevEs[708]=0.22;    stddevUs[708]=0.81;    corrNEs[708]=-0.007; 
   ids[709]='P217';    sts[709]='LacrosseDrCN2005';    lats[709]=37.1044974147;    lngs[709]=238.3493676976;    ves[709]=-21.61;    vns[709]=22.95;    vus[709]=-1.35;    stddevNs[709]=0.27;    stddevEs[709]=0.16;    stddevUs[709]=0.77;    corrNEs[709]=-0.009; 
   ids[710]='P218';    sts[710]='CoyoteCrk_CN2005';    lats[710]=37.2035147218;    lngs[710]=238.2860399652;    ves[710]=-22.01;    vns[710]=22.43;    vus[710]=-2.76;    stddevNs[710]=0.33;    stddevEs[710]=0.34;    stddevUs[710]=0.86;    corrNEs[710]=-0.003; 
   ids[711]='P218';    sts[711]='CoyoteCrk_CN2005';    lats[711]=37.2035147163;    lngs[711]=238.2860399657;    ves[711]=-22.01;    vns[711]=22.43;    vus[711]=-2.76;    stddevNs[711]=0.33;    stddevEs[711]=0.34;    stddevUs[711]=0.86;    corrNEs[711]=-0.003; 
   ids[712]='P219';    sts[712]='LaHondaCrkCN2008';    lats[712]=37.3424899847;    lngs[712]=237.7151794371;    ves[712]=-20.89;    vns[712]=30.28;    vus[712]=-0.13;    stddevNs[712]=0.59;    stddevEs[712]=2.98;    stddevUs[712]=2.33;    corrNEs[712]=-0.000; 
   ids[713]='P220';    sts[713]='RussianRdgCN2007';    lats[713]=37.3298874072;    lngs[713]=237.7857160531;    ves[713]=-25.38;    vns[713]=30.53;    vus[713]=-0.44;    stddevNs[713]=0.3;    stddevEs[713]=0.31;    stddevUs[713]=1.21;    corrNEs[713]=-0.004; 
   ids[714]='P221';    sts[714]='SanAntonioCN2007';    lats[714]=37.3369521724;    lngs[714]=237.9009508433;    ves[714]=-23.19;    vns[714]=27.33;    vus[714]=-1.09;    stddevNs[714]=0.25;    stddevEs[714]=0.39;    stddevUs[714]=1.19;    corrNEs[714]=-0.004; 
   ids[715]='P222';    sts[715]='CoyotHillsCN2004';    lats[715]=37.5392398959;    lngs[715]=237.9167360365;    ves[715]=-21.94;    vns[715]=23.96;    vus[715]=-4.09;    stddevNs[715]=0.17;    stddevEs[715]=0.36;    stddevUs[715]=1.18;    corrNEs[715]=-0.006; 
   ids[716]='P223';    sts[716]='ChabotParkCN2007';    lats[716]=37.7220541764;    lngs[716]=237.9002211036;    ves[716]=-17.61;    vns[716]=18.39;    vus[716]=-1.43;    stddevNs[716]=0.27;    stddevEs[716]=0.24;    stddevUs[716]=1.02;    corrNEs[716]=-0.006; 
   ids[717]='P223';    sts[717]='ChabotParkCN2007';    lats[717]=37.7220541892;    lngs[717]=237.9002211102;    ves[717]=-17.61;    vns[717]=18.39;    vus[717]=-1.43;    stddevNs[717]=0.27;    stddevEs[717]=0.24;    stddevUs[717]=1.02;    corrNEs[717]=-0.006; 
   ids[718]='P224';    sts[718]='SibleyVolcCN2005';    lats[718]=37.8638966428;    lngs[718]=237.7809400671;    ves[718]=-16.84;    vns[718]=18.37;    vus[718]=-1.33;    stddevNs[718]=0.2;    stddevEs[718]=0.19;    stddevUs[718]=0.58;    corrNEs[718]=-0.009; 
   ids[719]='P225';    sts[719]='CullCanyonCN2005';    lats[719]=37.7138660477;    lngs[719]=237.9416720003;    ves[719]=-17.47;    vns[719]=17.23;    vus[719]=-1.88;    stddevNs[719]=0.18;    stddevEs[719]=0.45;    stddevUs[719]=0.7;    corrNEs[719]=-0.005; 
   ids[720]='P225';    sts[720]='CullCanyonCN2005';    lats[720]=37.7138660482;    lngs[720]=237.9416719969;    ves[720]=-17.47;    vns[720]=17.23;    vus[720]=-1.88;    stddevNs[720]=0.18;    stddevEs[720]=0.45;    stddevUs[720]=0.7;    corrNEs[720]=-0.005; 
   ids[721]='P226';    sts[721]='ReidHillVWCN2006';    lats[721]=37.3367758727;    lngs[721]=238.1744145717;    ves[721]=-23.25;    vns[721]=22.6;    vus[721]=-7.06;    stddevNs[721]=0.96;    stddevEs[721]=0.5;    stddevUs[721]=0.98;    corrNEs[721]=-0.001; 
   ids[722]='P226';    sts[722]='ReidHillVWCN2006';    lats[722]=37.3367758311;    lngs[722]=238.1744145404;    ves[722]=-23.25;    vns[722]=22.6;    vus[722]=-7.06;    stddevNs[722]=0.96;    stddevEs[722]=0.5;    stddevUs[722]=0.98;    corrNEs[722]=-0.001; 
   ids[723]='P227';    sts[723]='SunolWildrCN2006';    lats[723]=37.5329746744;    lngs[723]=238.2104026900;    ves[723]=-13.91;    vns[723]=11.36;    vus[723]=-1.06;    stddevNs[723]=0.56;    stddevEs[723]=0.28;    stddevUs[723]=1.08;    corrNEs[723]=-0.002; 
   ids[724]='P227';    sts[724]='SunolWildrCN2006';    lats[724]=37.5329746799;    lngs[724]=238.2104026737;    ves[724]=-13.91;    vns[724]=11.36;    vus[724]=-1.06;    stddevNs[724]=0.56;    stddevEs[724]=0.28;    stddevUs[724]=1.08;    corrNEs[724]=-0.002; 
   ids[725]='P228';    sts[725]='DelValle__CN2005';    lats[725]=37.6018361001;    lngs[725]=238.3130636517;    ves[725]=-12.37;    vns[725]=10.65;    vus[725]=-0.06;    stddevNs[725]=0.3;    stddevEs[725]=0.29;    stddevUs[725]=1.06;    corrNEs[725]=-0.002; 
   ids[726]='P228';    sts[726]='DelValle__CN2005';    lats[726]=37.6018361038;    lngs[726]=238.3130636434;    ves[726]=-12.37;    vns[726]=10.65;    vus[726]=-0.06;    stddevNs[726]=0.3;    stddevEs[726]=0.29;    stddevUs[726]=1.06;    corrNEs[726]=-0.002; 
   ids[727]='P228';    sts[727]='DelValle__CN2005';    lats[727]=37.6018361029;    lngs[727]=238.3130636411;    ves[727]=-12.37;    vns[727]=10.65;    vus[727]=-0.06;    stddevNs[727]=0.3;    stddevEs[727]=0.29;    stddevUs[727]=1.06;    corrNEs[727]=-0.002; 
   ids[728]='P229';    sts[728]='BishopRnchCN2005';    lats[728]=37.7494354451;    lngs[728]=238.0220438752;    ves[728]=-15.93;    vns[728]=15.11;    vus[728]=-0.23;    stddevNs[728]=0.27;    stddevEs[728]=0.25;    stddevUs[728]=0.57;    corrNEs[728]=-0.005; 
   ids[729]='P230';    sts[729]='MorganTrtyCN2005';    lats[729]=37.8189646869;    lngs[729]=238.2136004327;    ves[729]=-11.74;    vns[729]=9.76;    vus[729]=-3.82;    stddevNs[729]=0.29;    stddevEs[729]=0.47;    stddevUs[729]=1.14;    corrNEs[729]=-0.002; 
   ids[730]='P231';    sts[730]='HopkinsStnCN2006';    lats[730]=36.6216768862;    lngs[730]=238.0945901217;    ves[730]=-30.47;    vns[730]=36.12;    vus[730]=2.43;    stddevNs[730]=0.34;    stddevEs[730]=0.21;    stddevUs[730]=0.8;    corrNEs[730]=-0.006; 
   ids[731]='P232';    sts[731]='BengardRchCN2007';    lats[731]=36.7240200980;    lngs[731]=238.4209587450;    ves[731]=-28.89;    vns[731]=34;    vus[731]=-0.57;    stddevNs[731]=0.21;    stddevEs[731]=0.2;    stddevUs[731]=0.77;    corrNEs[731]=-0.010; 
   ids[732]='P233';    sts[732]='Hollister_CN2006';    lats[732]=36.8004262640;    lngs[732]=238.5797161578;    ves[732]=-18.99;    vns[732]=24.69;    vus[732]=-1.23;    stddevNs[732]=0.19;    stddevEs[732]=0.19;    stddevUs[732]=0.63;    corrNEs[732]=-0.010; 
   ids[733]='P234';    sts[733]='LasAromitaCN2006';    lats[733]=36.8585261773;    lngs[733]=238.4087744977;    ves[733]=-27.94;    vns[733]=31.39;    vus[733]=-0.3;    stddevNs[733]=0.21;    stddevEs[733]=0.17;    stddevUs[733]=0.74;    corrNEs[733]=-0.010; 
   ids[734]='P235';    sts[734]='SJBautistaCN2007';    lats[734]=36.8142721439;    lngs[734]=238.4584592611;    ves[734]=-27.89;    vns[734]=33.86;    vus[734]=-4.97;    stddevNs[734]=0.28;    stddevEs[734]=0.23;    stddevUs[734]=0.97;    corrNEs[734]=-0.006; 
   ids[735]='P236';    sts[735]='Lomerias__CN2005';    lats[735]=36.9035451078;    lngs[735]=238.4455394866;    ves[735]=-22.87;    vns[735]=25.16;    vus[735]=-0.05;    stddevNs[735]=0.22;    stddevEs[735]=0.16;    stddevUs[735]=0.75;    corrNEs[735]=-0.010; 
   ids[736]='P237';    sts[736]='MountOlds-CN2007';    lats[736]=36.6370236759;    lngs[736]=238.6131704036;    ves[736]=-28.82;    vns[736]=33.48;    vus[736]=-0.29;    stddevNs[736]=0.2;    stddevEs[736]=0.22;    stddevUs[736]=0.89;    corrNEs[736]=-0.009; 
   ids[737]='P238';    sts[737]='FlintHillsCN2006';    lats[737]=36.8490798495;    lngs[737]=238.5472317372;    ves[737]=-20.45;    vns[737]=25.02;    vus[737]=-1.64;    stddevNs[737]=0.42;    stddevEs[737]=0.5;    stddevUs[737]=0.73;    corrNEs[737]=-0.002; 
   ids[738]='P238';    sts[738]='FlintHillsCN2006';    lats[738]=36.8490798389;    lngs[738]=238.5472317527;    ves[738]=-20.45;    vns[738]=25.02;    vus[738]=-1.64;    stddevNs[738]=0.42;    stddevEs[738]=0.5;    stddevUs[738]=0.73;    corrNEs[738]=-0.002; 
   ids[739]='P239';    sts[739]='UvasCrk___CN2008';    lats[739]=36.9624622534;    lngs[739]=238.4522132278;    ves[739]=-20.14;    vns[739]=23.69;    vus[739]=-2.51;    stddevNs[739]=0.39;    stddevEs[739]=0.92;    stddevUs[739]=1.56;    corrNEs[739]=-0.001; 
   ids[740]='P239';    sts[740]='UvasCrk___CN2008';    lats[740]=36.9624622585;    lngs[740]=238.4522132360;    ves[740]=-20.14;    vns[740]=23.69;    vus[740]=-2.51;    stddevNs[740]=0.39;    stddevEs[740]=0.92;    stddevUs[740]=1.56;    corrNEs[740]=-0.001; 
   ids[741]='P240';    sts[741]='MillerSlghCN2005';    lats[741]=37.0078070009;    lngs[741]=238.4579670528;    ves[741]=-20.39;    vns[741]=23.55;    vus[741]=-3.18;    stddevNs[741]=0.25;    stddevEs[741]=0.18;    stddevUs[741]=0.75;    corrNEs[741]=-0.008; 
   ids[742]='P241';    sts[742]='RodeoFlat_CN2007';    lats[742]=37.2130111570;    lngs[742]=238.4257404515;    ves[742]=-12.04;    vns[742]=11.78;    vus[742]=-0.84;    stddevNs[742]=0.23;    stddevEs[742]=0.37;    stddevUs[742]=1.08;    corrNEs[742]=-0.005; 
   ids[743]='P242';    sts[743]='FrazierAirCN2004';    lats[743]=36.9539319397;    lngs[743]=238.5368169438;    ves[743]=-20.28;    vns[743]=20.69;    vus[743]=-7.73;    stddevNs[743]=0.34;    stddevEs[743]=0.21;    stddevUs[743]=0.89;    corrNEs[743]=-0.005; 
   ids[744]='P243';    sts[744]='HollisterECN2007';    lats[744]=36.9181890785;    lngs[744]=238.6648477620;    ves[744]=-13.66;    vns[744]=10.33;    vus[744]=0.62;    stddevNs[744]=0.43;    stddevEs[744]=0.42;    stddevUs[744]=0.94;    corrNEs[744]=-0.003; 
   ids[745]='P243';    sts[745]='HollisterECN2007';    lats[745]=36.9181890810;    lngs[745]=238.6648477530;    ves[745]=-13.66;    vns[745]=10.33;    vus[745]=0.62;    stddevNs[745]=0.43;    stddevEs[745]=0.42;    stddevUs[745]=0.94;    corrNEs[745]=-0.003; 
   ids[746]='P244';    sts[746]='PachecoCrkCN2005';    lats[746]=37.0108237522;    lngs[746]=238.6454828714;    ves[746]=-14.09;    vns[746]=9.27;    vus[746]=0.38;    stddevNs[746]=0.2;    stddevEs[746]=0.25;    stddevUs[746]=0.69;    corrNEs[746]=-0.007; 
   ids[747]='P245';    sts[747]='YOSTurtleDCN2008';    lats[747]=37.7131163522;    lngs[747]=240.2938787079;    ves[747]=-11.98;    vns[747]=7.16;    vus[747]=0.82;    stddevNs[747]=0.43;    stddevEs[747]=0.37;    stddevUs[747]=1.57;    corrNEs[747]=-0.003; 
   ids[748]='P245';    sts[748]='YOSTurtleDCN2008';    lats[748]=37.7131163514;    lngs[748]=240.2938787048;    ves[748]=-11.98;    vns[748]=7.16;    vus[748]=0.82;    stddevNs[748]=0.43;    stddevEs[748]=0.37;    stddevUs[748]=1.57;    corrNEs[748]=-0.003; 
   ids[749]='P247';    sts[749]='SpencerRchCN2006';    lats[749]=36.5595179731;    lngs[749]=238.8115674236;    ves[749]=-28.73;    vns[749]=33.63;    vus[749]=-0.34;    stddevNs[749]=0.86;    stddevEs[749]=1.08;    stddevUs[749]=0.9;    corrNEs[749]=-0.000; 
   ids[750]='P248';    sts[750]='BlkDiamondCN2007';    lats[750]=37.9756086090;    lngs[750]=238.1313028084;    ves[750]=-11.56;    vns[750]=8.95;    vus[750]=-0.49;    stddevNs[750]=0.25;    stddevEs[750]=0.25;    stddevUs[750]=1.2;    corrNEs[750]=-0.005; 
   ids[751]='P249';    sts[751]='CallMtn___CN2005';    lats[751]=36.6116453223;    lngs[751]=238.9355889240;    ves[751]=-10.38;    vns[751]=8.82;    vus[751]=1.59;    stddevNs[751]=0.34;    stddevEs[751]=0.43;    stddevUs[751]=0.78;    corrNEs[751]=-0.002; 
   ids[752]='P250';    sts[752]='SparlinRchCN2007';    lats[752]=36.9500354678;    lngs[752]=238.7315622636;    ves[752]=-12.88;    vns[752]=10.08;    vus[752]=0.07;    stddevNs[752]=0.23;    stddevEs[752]=0.26;    stddevUs[752]=0.95;    corrNEs[752]=-0.007; 
   ids[753]='P251';    sts[753]='TrePinoCrkCN2006';    lats[753]=36.8114497760;    lngs[753]=238.6520501274;    ves[753]=-13.65;    vns[753]=12.97;    vus[753]=1.68;    stddevNs[753]=0.22;    stddevEs[753]=0.26;    stddevUs[753]=0.73;    corrNEs[753]=-0.006; 
   ids[754]='P252';    sts[754]='QuintoCrk_CN2005';    lats[754]=37.1695558019;    lngs[754]=238.9422707989;    ves[754]=-11.64;    vns[754]=8.01;    vus[754]=1.05;    stddevNs[754]=0.22;    stddevEs[754]=0.17;    stddevUs[754]=0.66;    corrNEs[754]=-0.009; 
   ids[755]='P253';    sts[755]='ValpeRidgeCN2007';    lats[755]=37.4784499677;    lngs[755]=238.3469904652;    ves[755]=-13.07;    vns[755]=10.04;    vus[755]=-0.58;    stddevNs[755]=0.35;    stddevEs[755]=0.39;    stddevUs[755]=1.45;    corrNEs[755]=-0.005; 
   ids[756]='P253';    sts[756]='ValpeRidgeCN2007';    lats[756]=37.4784499661;    lngs[756]=238.3469904578;    ves[756]=-13.07;    vns[756]=10.04;    vus[756]=-0.58;    stddevNs[756]=0.35;    stddevEs[756]=0.39;    stddevUs[756]=1.45;    corrNEs[756]=-0.005; 
   ids[757]='P254';    sts[757]='MtBoardmanCN2007';    lats[757]=37.4896326144;    lngs[757]=238.5308046054;    ves[757]=-11.6;    vns[757]=9.22;    vus[757]=-0.76;    stddevNs[757]=0.25;    stddevEs[757]=0.31;    stddevUs[757]=0.98;    corrNEs[757]=-0.005; 
   ids[758]='P255';    sts[758]='ArkansaCrkCN2006';    lats[758]=37.5818852260;    lngs[758]=238.6751472280;    ves[758]=-11.79;    vns[758]=8.44;    vus[758]=0.38;    stddevNs[758]=0.18;    stddevEs[758]=0.21;    stddevUs[758]=0.75;    corrNEs[758]=-0.009; 
   ids[759]='P256';    sts[759]='FallmanPrpCN2005';    lats[759]=37.9319645391;    lngs[759]=238.3951606414;    ves[759]=-12.54;    vns[759]=7.86;    vus[759]=-4.3;    stddevNs[759]=0.16;    stddevEs[759]=0.17;    stddevUs[759]=0.69;    corrNEs[759]=-0.012; 
   ids[760]='P257';    sts[760]='TomPainSlgCN2005';    lats[760]=37.7552907973;    lngs[760]=238.5359651095;    ves[760]=-11.88;    vns[760]=7.58;    vus[760]=-1.56;    stddevNs[760]=0.17;    stddevEs[760]=0.25;    stddevUs[760]=0.69;    corrNEs[760]=-0.008; 
   ids[761]='P258';    sts[761]='DiabloGranCN2007';    lats[761]=37.3853893334;    lngs[761]=238.7167281901;    ves[761]=-11.72;    vns[761]=8.36;    vus[761]=-1.83;    stddevNs[761]=0.33;    stddevEs[761]=0.39;    stddevUs[761]=1.17;    corrNEs[761]=-0.004; 
   ids[762]='P258';    sts[762]='DiabloGranCN2007';    lats[762]=37.3853893319;    lngs[762]=238.7167281798;    ves[762]=-11.72;    vns[762]=8.36;    vus[762]=-1.83;    stddevNs[762]=0.33;    stddevEs[762]=0.39;    stddevUs[762]=1.17;    corrNEs[762]=-0.004; 
   ids[763]='P259';    sts[763]='Patterson_CN2005';    lats[763]=37.4330201152;    lngs[763]=238.8994202109;    ves[763]=-11.67;    vns[763]=7.99;    vus[763]=-5.27;    stddevNs[763]=0.72;    stddevEs[763]=0.26;    stddevUs[763]=0.77;    corrNEs[763]=-0.002; 
   ids[764]='P260';    sts[764]='ModestoCnlCN2005';    lats[764]=37.7112212790;    lngs[764]=238.9160479402;    ves[764]=-11.98;    vns[764]=7.45;    vus[764]=-0.57;    stddevNs[764]=0.21;    stddevEs[764]=0.21;    stddevUs[764]=0.63;    corrNEs[764]=-0.007; 
   ids[765]='P261';    sts[765]='HunterHillCA2004';    lats[765]=38.1529605244;    lngs[765]=237.7824604712;    ves[765]=-14.5;    vns[765]=14.75;    vus[765]=-0.73;    stddevNs[765]=0.53;    stddevEs[765]=0.81;    stddevUs[765]=1.02;    corrNEs[765]=-0.001; 
   ids[766]='P261';    sts[766]='HunterHillCA2004';    lats[766]=38.1529605431;    lngs[766]=237.7824604121;    ves[766]=-14.5;    vns[766]=14.75;    vus[766]=-0.73;    stddevNs[766]=0.53;    stddevEs[766]=0.81;    stddevUs[766]=1.02;    corrNEs[766]=-0.001; 
   ids[767]='P262';    sts[767]='Waterbird_CN2005';    lats[767]=38.0251486107;    lngs[767]=237.9038561120;    ves[767]=-15.33;    vns[767]=13.82;    vus[767]=-1.71;    stddevNs[767]=0.3;    stddevEs[767]=0.73;    stddevUs[767]=0.65;    corrNEs[767]=-0.002; 
   ids[768]='P263';    sts[768]='AngwinObs_CN2008';    lats[768]=38.5776943763;    lngs[768]=237.5708241784;    ves[768]=-13.65;    vns[768]=14.58;    vus[768]=-2.89;    stddevNs[768]=0.37;    stddevEs[768]=0.32;    stddevUs[768]=1.62;    corrNEs[768]=-0.005; 
   ids[769]='P264';    sts[769]='Marshall__CO2004';    lats[769]=38.4442150149;    lngs[769]=237.8046704267;    ves[769]=-12.54;    vns[769]=10.63;    vus[769]=0.56;    stddevNs[769]=0.15;    stddevEs[769]=0.21;    stddevUs[769]=0.6;    corrNEs[769]=-0.011; 
   ids[770]='P265';    sts[770]='PutahCreekCN2005';    lats[770]=38.5301856236;    lngs[770]=238.0458054222;    ves[770]=-10.78;    vns[770]=7.9;    vus[770]=-5.63;    stddevNs[770]=0.24;    stddevEs[770]=0.21;    stddevUs[770]=0.74;    corrNEs[770]=-0.006; 
   ids[771]='P266';    sts[771]='LilHonker_CN2005';    lats[771]=38.1839682932;    lngs[771]=238.1564735472;    ves[771]=-11.91;    vns[771]=7.81;    vus[771]=-1.84;    stddevNs[771]=0.22;    stddevEs[771]=0.18;    stddevUs[771]=0.65;    corrNEs[771]=-0.009; 
   ids[772]='P267';    sts[772]='DixonAviatCN2005';    lats[772]=38.3803356594;    lngs[772]=238.1767656569;    ves[772]=-10.03;    vns[772]=8.54;    vus[772]=-5.05;    stddevNs[772]=0.2;    stddevEs[772]=0.19;    stddevUs[772]=0.69;    corrNEs[772]=-0.009; 
   ids[773]='P268';    sts[773]='FinchFarmsCN2005';    lats[773]=38.4735264294;    lngs[773]=238.3535888965;    ves[773]=-13.24;    vns[773]=7.28;    vus[773]=-5.57;    stddevNs[773]=0.16;    stddevEs[773]=0.27;    stddevUs[773]=0.61;    corrNEs[773]=-0.007; 
   ids[774]='P269';    sts[774]='CowboyCampCN2008';    lats[774]=38.9995254163;    lngs[774]=237.6454464720;    ves[774]=-9.72;    vns[774]=7.66;    vus[774]=0.36;    stddevNs[774]=0.32;    stddevEs[774]=0.31;    stddevUs[774]=1.71;    corrNEs[774]=0.003; 
   ids[775]='P270';    sts[775]='HopkinSlghCN2005';    lats[775]=39.2437701760;    lngs[775]=237.9447870214;    ves[775]=-11.33;    vns[775]=6.29;    vus[775]=-2.19;    stddevNs[775]=0.16;    stddevEs[775]=0.29;    stddevUs[775]=0.7;    corrNEs[775]=-0.007; 
   ids[776]='P271';    sts[776]='Woodland1_CA2004';    lats[776]=38.6573506484;    lngs[776]=238.2854503001;    ves[776]=-12.59;    vns[776]=6.46;    vus[776]=-4.23;    stddevNs[776]=0.14;    stddevEs[776]=0.16;    stddevUs[776]=0.71;    corrNEs[776]=-0.015; 
   ids[777]='P272';    sts[777]='SycamorSlgCN2005';    lats[777]=39.1454819221;    lngs[777]=238.0569375279;    ves[777]=-11.13;    vns[777]=5.56;    vus[777]=-3.44;    stddevNs[777]=0.16;    stddevEs[777]=0.2;    stddevUs[777]=0.66;    corrNEs[777]=-0.010; 
   ids[778]='P273';    sts[778]='Kettleman_CN2005';    lats[778]=38.1158125954;    lngs[778]=238.6119222920;    ves[778]=-11.8;    vns[778]=9.92;    vus[778]=-3.2;    stddevNs[778]=1.04;    stddevEs[778]=0.51;    stddevUs[778]=1.38;    corrNEs[778]=-0.000; 
   ids[779]='P274';    sts[779]='SnodgraSlgCN2005';    lats[779]=38.2831235067;    lngs[779]=238.5394877850;    ves[779]=-14.86;    vns[779]=7.25;    vus[779]=-3.22;    stddevNs[779]=0.78;    stddevEs[779]=0.44;    stddevUs[779]=0.74;    corrNEs[779]=-0.001; 
   ids[780]='P275';    sts[780]='KenefikRchCN2006';    lats[780]=38.3215292132;    lngs[780]=238.7854138615;    ves[780]=-11.88;    vns[780]=7.17;    vus[780]=-1.84;    stddevNs[780]=0.2;    stddevEs[780]=0.19;    stddevUs[780]=0.76;    corrNEs[780]=-0.008; 
   ids[781]='P276';    sts[781]='LDoradoHilCN2005';    lats[781]=38.6453079827;    lngs[781]=238.9048350476;    ves[781]=-11.34;    vns[781]=7.2;    vus[781]=0.15;    stddevNs[781]=0.21;    stddevEs[781]=0.18;    stddevUs[781]=0.6;    corrNEs[781]=-0.008; 
   ids[782]='P277';    sts[782]='PigeonPt__CN2007';    lats[782]=37.1923724777;    lngs[782]=237.6331169829;    ves[782]=-28.27;    vns[782]=35.4;    vus[782]=-1.73;    stddevNs[782]=0.28;    stddevEs[782]=0.26;    stddevUs[782]=1.03;    corrNEs[782]=-0.005; 
   ids[783]='P278';    sts[783]='ClarkeMtn_CS2004';    lats[783]=35.7112522542;    lngs[783]=238.9392459991;    ves[783]=-30.04;    vns[783]=31.76;    vus[783]=1.44;    stddevNs[783]=0.37;    stddevEs[783]=0.56;    stddevUs[783]=1.11;    corrNEs[783]=-0.002; 
   ids[784]='P278';    sts[784]='ClarkeMtn_CS2004';    lats[784]=35.7112521222;    lngs[784]=238.9392460012;    ves[784]=-30.04;    vns[784]=31.76;    vus[784]=1.44;    stddevNs[784]=0.37;    stddevEs[784]=0.56;    stddevUs[784]=1.11;    corrNEs[784]=-0.002; 
   ids[785]='P278';    sts[785]='ClarkeMtn_CS2004';    lats[785]=35.7112520998;    lngs[785]=238.9392459509;    ves[785]=-30.04;    vns[785]=31.76;    vus[785]=1.44;    stddevNs[785]=0.37;    stddevEs[785]=0.56;    stddevUs[785]=1.11;    corrNEs[785]=-0.002; 
   ids[786]='P279';    sts[786]='GulchHouseCN2007';    lats[786]=35.7906474535;    lngs[786]=238.9379840672;    ves[786]=-29.66;    vns[786]=33.59;    vus[786]=0.4;    stddevNs[786]=0.41;    stddevEs[786]=0.65;    stddevUs[786]=0.98;    corrNEs[786]=-0.001; 
   ids[787]='P280';    sts[787]='CamattaCynCS2006';    lats[787]=35.5440543860;    lngs[787]=239.6523946113;    ves[787]=-25.77;    vns[787]=29.69;    vus[787]=-0.18;    stddevNs[787]=0.69;    stddevEs[787]=0.83;    stddevUs[787]=0.95;    corrNEs[787]=-0.001; 
   ids[788]='P281';    sts[788]='CholameCrkCN2004';    lats[788]=35.8410590212;    lngs[788]=239.6105402027;    ves[788]=-26.1;    vns[788]=35.22;    vus[788]=-1.09;    stddevNs[788]=0.41;    stddevEs[788]=0.77;    stddevUs[788]=0.88;    corrNEs[788]=-0.001; 
   ids[789]='P282';    sts[789]='GoldHill__CN2004';    lats[789]=35.8378088474;    lngs[789]=239.6547865439;    ves[789]=-10.93;    vns[789]=15.82;    vus[789]=0.58;    stddevNs[789]=0.34;    stddevEs[789]=0.37;    stddevUs[789]=0.93;    corrNEs[789]=-0.003; 
   ids[790]='P283';    sts[790]='StonCorralCN2004';    lats[790]=35.8066695265;    lngs[790]=239.7147444126;    ves[790]=-11.68;    vns[790]=17.85;    vus[790]=0.91;    stddevNs[790]=0.56;    stddevEs[790]=0.4;    stddevUs[790]=0.92;    corrNEs[790]=-0.002; 
   ids[791]='P284';    sts[791]='AvilaRanchCN2005';    lats[791]=35.9332605443;    lngs[791]=239.0931560400;    ves[791]=-28.9;    vns[791]=31.49;    vus[791]=-2.6;    stddevNs[791]=0.94;    stddevEs[791]=0.41;    stddevUs[791]=1.18;    corrNEs[791]=-0.001; 
   ids[792]='P285';    sts[792]='BuckRidge_CN2006';    lats[792]=36.4171796064;    lngs[792]=239.0185188151;    ves[792]=-12.55;    vns[792]=12.62;    vus[792]=1.45;    stddevNs[792]=0.62;    stddevEs[792]=0.67;    stddevUs[792]=1.16;    corrNEs[792]=-0.001; 
   ids[793]='P286';    sts[793]='BabiesGch_CN2007';    lats[793]=36.5159089160;    lngs[793]=239.1469440936;    ves[793]=-10.83;    vns[793]=10.51;    vus[793]=-0.95;    stddevNs[793]=0.41;    stddevEs[793]=0.47;    stddevUs[793]=1;    corrNEs[793]=-0.001; 
   ids[794]='P287';    sts[794]='EmeryRanchCN2005';    lats[794]=36.0248352723;    lngs[794]=239.3021686903;    ves[794]=-30.76;    vns[794]=31.77;    vus[794]=0.43;    stddevNs[794]=0.84;    stddevEs[794]=0.44;    stddevUs[794]=0.94;    corrNEs[794]=-0.001; 
   ids[795]='P288';    sts[795]='MooneyCyn_CN2006';    lats[795]=36.1402070042;    lngs[795]=239.1210988248;    ves[795]=-30.01;    vns[795]=32.7;    vus[795]=-0.93;    stddevNs[795]=0.45;    stddevEs[795]=0.58;    stddevUs[795]=0.92;    corrNEs[795]=-0.001; 
   ids[796]='P289';    sts[796]='Peachtree_CN2007';    lats[796]=36.1068419750;    lngs[796]=239.2511844170;    ves[796]=-29.17;    vns[796]=32.72;    vus[796]=0.26;    stddevNs[796]=0.44;    stddevEs[796]=0.56;    stddevUs[796]=0.97;    corrNEs[796]=-0.002; 
   ids[797]='P290';    sts[797]='MustangRdgCN2006';    lats[797]=36.1789393103;    lngs[797]=239.2717477784;    ves[797]=-10.17;    vns[797]=10.42;    vus[797]=1.32;    stddevNs[797]=0.4;    stddevEs[797]=0.41;    stddevUs[797]=1.03;    corrNEs[797]=-0.002; 
   ids[798]='P291';    sts[798]='DeerVlyRchCN2007';    lats[798]=35.9227773153;    lngs[798]=239.3552294563;    ves[798]=-26.97;    vns[798]=32.63;    vus[798]=0.1;    stddevNs[798]=0.42;    stddevEs[798]=0.59;    stddevUs[798]=1.08;    corrNEs[798]=-0.002; 
   ids[799]='P292';    sts[799]='LowGastro_CN2008';    lats[799]=36.0074641375;    lngs[799]=239.5247244249;    ves[799]=-7.51;    vns[799]=17.26;    vus[799]=-0.61;    stddevNs[799]=0.63;    stddevEs[799]=1.06;    stddevUs[799]=1.29;    corrNEs[799]=-0.000; 
   ids[800]='P293';    sts[800]='SulphurCrkCN2006';    lats[800]=36.0893643376;    lngs[800]=239.4570068792;    ves[800]=-9.86;    vns[800]=13.33;    vus[800]=1.01;    stddevNs[800]=0.65;    stddevEs[800]=0.56;    stddevUs[800]=1.04;    corrNEs[800]=-0.001; 
   ids[801]='P294';    sts[801]='AlcaldeHilCN2006';    lats[801]=36.1232363928;    lngs[801]=239.5601180320;    ves[801]=-10.06;    vns[801]=11.96;    vus[801]=1.02;    stddevNs[801]=0.5;    stddevEs[801]=0.73;    stddevUs[801]=1.08;    corrNEs[801]=-0.001; 
   ids[802]='P295';    sts[802]='ChimneyRR_CS2004';    lats[802]=35.6970774032;    lngs[802]=239.1576171962;    ves[802]=-28.14;    vns[802]=32.69;    vus[802]=0.27;    stddevNs[802]=0.18;    stddevEs[802]=0.18;    stddevUs[802]=0.97;    corrNEs[802]=-0.008; 
   ids[803]='P295';    sts[803]='ChimneyRR_CS2004';    lats[803]=35.6970774053;    lngs[803]=239.1576171910;    ves[803]=-28.14;    vns[803]=32.69;    vus[803]=0.27;    stddevNs[803]=0.18;    stddevEs[803]=0.18;    stddevUs[803]=0.97;    corrNEs[803]=-0.008; 
   ids[804]='P295';    sts[804]='ChimneyRR_CS2004';    lats[804]=35.6970773446;    lngs[804]=239.1576172290;    ves[804]=-28.14;    vns[804]=32.69;    vus[804]=0.27;    stddevNs[804]=0.18;    stddevEs[804]=0.18;    stddevUs[804]=0.97;    corrNEs[804]=-0.008; 
   ids[805]='P295';    sts[805]='ChimneyRR_CS2004';    lats[805]=35.6970773416;    lngs[805]=239.1576172199;    ves[805]=-28.14;    vns[805]=32.69;    vus[805]=0.27;    stddevNs[805]=0.18;    stddevEs[805]=0.18;    stddevUs[805]=0.97;    corrNEs[805]=-0.008; 
   ids[806]='P295';    sts[806]='ChimneyRR_CS2004';    lats[806]=35.6970773427;    lngs[806]=239.1576172628;    ves[806]=-28.14;    vns[806]=32.69;    vus[806]=0.27;    stddevNs[806]=0.18;    stddevEs[806]=0.18;    stddevUs[806]=0.97;    corrNEs[806]=-0.008; 
   ids[807]='P296';    sts[807]='Jacalitos_CN2006';    lats[807]=36.0516551242;    lngs[807]=239.6364388651;    ves[807]=-9.66;    vns[807]=13.95;    vus[807]=0.74;    stddevNs[807]=0.54;    stddevEs[807]=0.42;    stddevUs[807]=0.92;    corrNEs[807]=-0.001; 
   ids[808]='P296';    sts[808]='Jacalitos_CN2006';    lats[808]=36.0516551265;    lngs[808]=239.6364388637;    ves[808]=-9.66;    vns[808]=13.95;    vus[808]=0.74;    stddevNs[808]=0.54;    stddevEs[808]=0.42;    stddevUs[808]=0.92;    corrNEs[808]=-0.001; 
   ids[809]='P297';    sts[809]='SAFOD_GPS_CN2005';    lats[809]=35.9740861843;    lngs[809]=239.4481422663;    ves[809]=-28.07;    vns[809]=33.39;    vus[809]=-1.04;    stddevNs[809]=0.49;    stddevEs[809]=0.42;    stddevUs[809]=1;    corrNEs[809]=-0.002; 
   ids[810]='P298';    sts[810]='CedarCyn__CN2005';    lats[810]=36.0158423471;    lngs[810]=239.7059411845;    ves[810]=-10.25;    vns[810]=13.38;    vus[810]=0.32;    stddevNs[810]=0.51;    stddevEs[810]=0.34;    stddevUs[810]=1.07;    corrNEs[810]=-0.002; 
   ids[811]='P299';    sts[811]='Duckworth_CN2007';    lats[811]=36.2568193726;    lngs[811]=239.2898023314;    ves[811]=-6.02;    vns[811]=12.19;    vus[811]=0.75;    stddevNs[811]=8.03;    stddevEs[811]=6.8;    stddevUs[811]=1.86;    corrNEs[811]=0.000; 
   ids[812]='P300';    sts[812]='HarrisRnchCN2004';    lats[812]=36.3044275199;    lngs[812]=239.7230196867;    ves[812]=-9.76;    vns[812]=10.96;    vus[812]=0.1;    stddevNs[812]=0.89;    stddevEs[812]=1.46;    stddevUs[812]=0.72;    corrNEs[812]=-0.000; 
   ids[813]='P301';    sts[813]='LilPanocheCN2004';    lats[813]=36.8062914290;    lngs[813]=239.2569472198;    ves[813]=-10.65;    vns[813]=8.71;    vus[813]=-0.48;    stddevNs[813]=0.17;    stddevEs[813]=0.19;    stddevUs[813]=0.52;    corrNEs[813]=-0.011; 
   ids[814]='P302';    sts[814]='PanocheCrkCN2004';    lats[814]=36.6347276394;    lngs[814]=239.3814247578;    ves[814]=-3.36;    vns[814]=11.44;    vus[814]=-4.05;    stddevNs[814]=0.52;    stddevEs[814]=0.62;    stddevUs[814]=0.86;    corrNEs[814]=-0.001; 
   ids[815]='P303';    sts[815]='LosBanos__CN2005';    lats[815]=37.0543834543;    lngs[815]=239.2947044411;    ves[815]=-11.37;    vns[815]=8.69;    vus[815]=-19.59;    stddevNs[815]=0.2;    stddevEs[815]=0.21;    stddevUs[815]=1.36;    corrNEs[815]=-0.008; 
   ids[816]='P304';    sts[816]='Mendota___CA2004';    lats[816]=36.7390045516;    lngs[816]=239.6434013491;    ves[816]=-12.03;    vns[816]=7.56;    vus[816]=-13.18;    stddevNs[816]=0.29;    stddevEs[816]=0.2;    stddevUs[816]=1.64;    corrNEs[816]=-0.006; 
   ids[817]='P305';    sts[817]='Planada___CN2005';    lats[817]=37.3522143716;    lngs[817]=239.8032414204;    ves[817]=-11.46;    vns[817]=8.12;    vus[817]=-0.2;    stddevNs[817]=0.22;    stddevEs[817]=0.15;    stddevUs[817]=0.62;    corrNEs[817]=-0.009; 
   ids[818]='P306';    sts[818]='WildcatCrkCN2006';    lats[818]=37.7951673439;    lngs[818]=239.3555417524;    ves[818]=-11.91;    vns[818]=7.74;    vus[818]=0.52;    stddevNs[818]=0.25;    stddevEs[818]=0.18;    stddevUs[818]=0.71;    corrNEs[818]=-0.007; 
   ids[819]='P307';    sts[819]='MaderaDOT_CN2005';    lats[819]=36.9472703367;    lngs[819]=239.9420805728;    ves[819]=-13.99;    vns[819]=9.63;    vus[819]=-21.16;    stddevNs[819]=0.19;    stddevEs[819]=0.17;    stddevUs[819]=0.73;    corrNEs[819]=-0.009; 
   ids[820]='P308';    sts[820]='YOS_HetchyCN2008';    lats[820]=37.9011509334;    lngs[820]=240.1598015928;    ves[820]=-11.4;    vns[820]=7.43;    vus[820]=-0.23;    stddevNs[820]=0.86;    stddevEs[820]=0.68;    stddevUs[820]=2.25;    corrNEs[820]=-0.004; 
   ids[821]='P309';    sts[821]='Calaveras_CN2005';    lats[821]=38.0899905468;    lngs[821]=239.0487626857;    ves[821]=-11.31;    vns[821]=7.57;    vus[821]=1.48;    stddevNs[821]=0.17;    stddevEs[821]=0.29;    stddevUs[821]=0.7;    corrNEs[821]=-0.006; 
   ids[822]='P310';    sts[822]='AlderRidgeCN2006';    lats[822]=38.7356073504;    lngs[822]=239.6656630044;    ves[822]=-10.74;    vns[822]=7.36;    vus[822]=-0.49;    stddevNs[822]=0.2;    stddevEs[822]=0.18;    stddevUs[822]=0.72;    corrNEs[822]=-0.008; 
   ids[823]='P311';    sts[823]='Hunchback_CS2008';    lats[823]=37.1775631289;    lngs[823]=241.4801990308;    ves[823]=-9.09;    vns[823]=9.79;    vus[823]=-2.69;    stddevNs[823]=0.56;    stddevEs[823]=0.78;    stddevUs[823]=1.99;    corrNEs[823]=0.001; 
   ids[824]='P312';    sts[824]='SmithRidgeCN2006';    lats[824]=39.5291788532;    lngs[824]=236.3016253325;    ves[824]=-23.96;    vns[824]=24.03;    vus[824]=-2.79;    stddevNs[824]=0.19;    stddevEs[824]=0.25;    stddevUs[824]=0.82;    corrNEs[824]=-0.011; 
   ids[825]='P313';    sts[825]='BuchaRidgeCN2006';    lats[825]=39.5542886824;    lngs[825]=236.4354551460;    ves[825]=-23.53;    vns[825]=21.28;    vus[825]=-2.31;    stddevNs[825]=0.2;    stddevEs[825]=0.21;    stddevUs[825]=0.71;    corrNEs[825]=-0.010; 
   ids[826]='P314';    sts[826]='CahtoPeak_CN2004';    lats[826]=39.6856642095;    lngs[826]=236.4181517254;    ves[826]=-23.54;    vns[826]=19.59;    vus[826]=-2.04;    stddevNs[826]=0.16;    stddevEs[826]=0.17;    stddevUs[826]=0.62;    corrNEs[826]=-0.013; 
   ids[827]='P314';    sts[827]='CahtoPeak_CN2004';    lats[827]=39.6856642082;    lngs[827]=236.4181517129;    ves[827]=-23.54;    vns[827]=19.59;    vus[827]=-2.04;    stddevNs[827]=0.16;    stddevEs[827]=0.17;    stddevUs[827]=0.62;    corrNEs[827]=-0.013; 
   ids[828]='P315';    sts[828]='LeggettSchCN2005';    lats[828]=39.8635832009;    lngs[828]=236.2831036749;    ves[828]=-22.96;    vns[828]=20.09;    vus[828]=-1.08;    stddevNs[828]=0.19;    stddevEs[828]=0.18;    stddevUs[828]=0.84;    corrNEs[828]=-0.012; 
   ids[829]='P316';    sts[829]='RequaYurokCN2006';    lats[829]=41.5591332984;    lngs[829]=235.9138562647;    ves[829]=-1.42;    vns[829]=9.91;    vus[829]=-3.82;    stddevNs[829]=0.2;    stddevEs[829]=0.2;    stddevUs[829]=0.78;    corrNEs[829]=-0.007; 
   ids[830]='P317';    sts[830]='AdanacRnchCN2006';    lats[830]=39.9056704313;    lngs[830]=236.4481498429;    ves[830]=-19.41;    vns[830]=14.07;    vus[830]=-2.18;    stddevNs[830]=0.2;    stddevEs[830]=0.28;    stddevUs[830]=0.86;    corrNEs[830]=-0.005; 
   ids[831]='P318';    sts[831]='WillitsAirCN2006';    lats[831]=39.4523695466;    lngs[831]=236.6281466332;    ves[831]=-19.47;    vns[831]=14.34;    vus[831]=-1.03;    stddevNs[831]=0.35;    stddevEs[831]=0.46;    stddevUs[831]=0.92;    corrNEs[831]=-0.006; 
   ids[832]='P318';    sts[832]='WillitsAirCN2006';    lats[832]=39.4523695470;    lngs[832]=236.6281466162;    ves[832]=-19.47;    vns[832]=14.34;    vus[832]=-1.03;    stddevNs[832]=0.35;    stddevEs[832]=0.46;    stddevUs[832]=0.92;    corrNEs[832]=-0.006; 
   ids[833]='P319';    sts[833]='HogHoleRdgCN2004';    lats[833]=39.7070971225;    lngs[833]=236.7049212897;    ves[833]=-18.04;    vns[833]=8.76;    vus[833]=3.01;    stddevNs[833]=0.24;    stddevEs[833]=0.35;    stddevUs[833]=0.67;    corrNEs[833]=-0.004; 
   ids[834]='P320';    sts[834]='EmandalFrmCN2008';    lats[834]=39.4928271521;    lngs[834]=236.8132148700;    ves[834]=-18.83;    vns[834]=14.52;    vus[834]=2.77;    stddevNs[834]=7.98;    stddevEs[834]=3.26;    stddevUs[834]=4.33;    corrNEs[834]=-0.000; 
   ids[835]='P321';    sts[835]='Updegraph_CN2008';    lats[835]=39.8972204823;    lngs[835]=236.6050531344;    ves[835]=-14.71;    vns[835]=16.89;    vus[835]=-6.07;    stddevNs[835]=0.77;    stddevEs[835]=1.3;    stddevUs[835]=2.03;    corrNEs[835]=-0.001; 
   ids[836]='P322';    sts[836]='LightnCampCN2008';    lats[836]=39.9621441741;    lngs[836]=236.8175188155;    ves[836]=-12.58;    vns[836]=8.08;    vus[836]=-1.19;    stddevNs[836]=0.62;    stddevEs[836]=0.83;    stddevUs[836]=2.02;    corrNEs[836]=-0.002; 
   ids[837]='P323';    sts[837]='AnthonyPk_CN2007';    lats[837]=39.8457484613;    lngs[837]=237.0365590631;    ves[837]=32.29;    vns[837]=31.1;    vus[837]=101.72;    stddevNs[837]=17.61;    stddevEs[837]=34.85;    stddevUs[837]=9.4;    corrNEs[837]=-0.000; 
   ids[838]='P324';    sts[838]='BlocksburgCN2006';    lats[838]=40.2568380570;    lngs[838]=236.3442629583;    ves[838]=-12.64;    vns[838]=13.64;    vus[838]=-0.59;    stddevNs[838]=0.18;    stddevEs[838]=0.25;    stddevUs[838]=0.77;    corrNEs[838]=-0.008; 
   ids[839]='P325';    sts[839]='SchoolHousCN2006';    lats[839]=41.1516703008;    lngs[839]=236.1173935394;    ves[839]=-1.23;    vns[839]=10.89;    vus[839]=1.44;    stddevNs[839]=0.16;    stddevEs[839]=0.24;    stddevUs[839]=0.58;    corrNEs[839]=-0.009; 
   ids[840]='P325';    sts[840]='SchoolHousCN2006';    lats[840]=41.1516703033;    lngs[840]=236.1173935331;    ves[840]=-1.23;    vns[840]=10.89;    vus[840]=1.44;    stddevNs[840]=0.16;    stddevEs[840]=0.24;    stddevUs[840]=0.58;    corrNEs[840]=-0.009; 
   ids[841]='P326';    sts[841]='ShowersMtnCN2006';    lats[841]=40.5753142143;    lngs[841]=236.3010699490;    ves[841]=-7.29;    vns[841]=9.41;    vus[841]=0.48;    stddevNs[841]=0.2;    stddevEs[841]=0.28;    stddevUs[841]=0.8;    corrNEs[841]=-0.004; 
   ids[842]='P327';    sts[842]='Dinsmore__CN2008';    lats[842]=40.4788607047;    lngs[842]=236.4269359778;    ves[842]=-9.12;    vns[842]=7.56;    vus[842]=1.46;    stddevNs[842]=0.43;    stddevEs[842]=0.76;    stddevUs[842]=1.68;    corrNEs[842]=0.002; 
   ids[843]='P329';    sts[843]='StewartRchCN2007';    lats[843]=40.0822146860;    lngs[843]=236.5479542330;    ves[843]=-15.47;    vns[843]=12.68;    vus[843]=0.09;    stddevNs[843]=0.33;    stddevEs[843]=0.44;    stddevUs[843]=1.11;    corrNEs[843]=-0.003; 
   ids[844]='P330';    sts[844]='RedMtnHeliCN2007';    lats[844]=40.3240691438;    lngs[844]=236.8644185511;    ves[844]=-10.37;    vns[844]=7.65;    vus[844]=-0.5;    stddevNs[844]=0.31;    stddevEs[844]=0.51;    stddevUs[844]=1.08;    corrNEs[844]=-0.003; 
   ids[845]='P331';    sts[845]='EagleRock_CN2007';    lats[845]=40.7329146236;    lngs[845]=236.6758574038;    ves[845]=-7.53;    vns[845]=7.92;    vus[845]=-0.43;    stddevNs[845]=0.34;    stddevEs[845]=0.55;    stddevUs[845]=0.97;    corrNEs[845]=-0.002; 
   ids[846]='P332';    sts[846]='HayforkAirCN2005';    lats[846]=40.5466507030;    lngs[846]=236.8254961386;    ves[846]=-8.69;    vns[846]=7.74;    vus[846]=-1.04;    stddevNs[846]=0.19;    stddevEs[846]=0.27;    stddevUs[846]=0.77;    corrNEs[846]=-0.006; 
   ids[847]='P333';    sts[847]='BaldMtnMNFCN2007';    lats[847]=39.6213813220;    lngs[847]=237.0242464305;    ves[847]=-13.96;    vns[847]=8.15;    vus[847]=-2.09;    stddevNs[847]=0.43;    stddevEs[847]=0.4;    stddevUs[847]=1.45;    corrNEs[847]=-0.002; 
   ids[848]='P333';    sts[848]='BaldMtnMNFCN2007';    lats[848]=39.6213813146;    lngs[848]=237.0242464173;    ves[848]=-13.96;    vns[848]=8.15;    vus[848]=-2.09;    stddevNs[848]=0.43;    stddevEs[848]=0.4;    stddevUs[848]=1.45;    corrNEs[848]=-0.002; 
   ids[849]='P334';    sts[849]='Sheetiron_CN2007';    lats[849]=39.4935922270;    lngs[849]=237.2641286018;    ves[849]=-11.87;    vns[849]=6.42;    vus[849]=1.32;    stddevNs[849]=10.13;    stddevEs[849]=20.22;    stddevUs[849]=4.8;    corrNEs[849]=-0.000; 
   ids[850]='P335';    sts[850]='BlactButteCN2008';    lats[850]=39.7261901999;    lngs[850]=237.1263631744;    ves[850]=-11.39;    vns[850]=7.69;    vus[850]=-2.38;    stddevNs[850]=0.95;    stddevEs[850]=0.75;    stddevUs[850]=1.64;    corrNEs[850]=-0.000; 
   ids[851]='P336';    sts[851]='HubbardRdgCN2007';    lats[851]=39.5280802861;    lngs[851]=237.5695164323;    ves[851]=-10.68;    vns[851]=6.57;    vus[851]=-0.19;    stddevNs[851]=0.24;    stddevEs[851]=0.33;    stddevUs[851]=0.99;    corrNEs[851]=-0.004; 
   ids[852]='P337';    sts[852]='PattymocusCN2007';    lats[852]=40.2961891248;    lngs[852]=237.1311955416;    ves[852]=-10.35;    vns[852]=6.65;    vus[852]=-2.26;    stddevNs[852]=0.5;    stddevEs[852]=0.47;    stddevUs[852]=1.21;    corrNEs[852]=-0.002; 
   ids[853]='P338';    sts[853]='WeavrvlAirCN2005';    lats[853]=40.7475532571;    lngs[853]=237.0769715296;    ves[853]=-8.21;    vns[853]=6.84;    vus[853]=1.74;    stddevNs[853]=0.19;    stddevEs[853]=0.23;    stddevUs[853]=0.77;    corrNEs[853]=-0.007; 
   ids[854]='P339';    sts[854]='Valentine_CN2007';    lats[854]=40.0341109110;    lngs[854]=237.3317505851;    ves[854]=-10.82;    vns[854]=7.01;    vus[854]=-1.16;    stddevNs[854]=0.33;    stddevEs[854]=0.29;    stddevUs[854]=0.96;    corrNEs[854]=-0.004; 
   ids[855]='P340';    sts[855]='DashielCrkCN2008';    lats[855]=39.4093393254;    lngs[855]=236.9501772156;    ves[855]=-19.72;    vns[855]=9.75;    vus[855]=-3.59;    stddevNs[855]=1.31;    stddevEs[855]=0.77;    stddevUs[855]=2.39;    corrNEs[855]=-0.009; 
   ids[856]='P340';    sts[856]='DashielCrkCN2008';    lats[856]=39.4093392781;    lngs[856]=236.9501772504;    ves[856]=-19.72;    vns[856]=9.75;    vus[856]=-3.59;    stddevNs[856]=1.31;    stddevEs[856]=0.77;    stddevUs[856]=2.39;    corrNEs[856]=-0.009; 
   ids[857]='P341';    sts[857]='WhiskytownCN2005';    lats[857]=40.6506565026;    lngs[857]=237.3930999928;    ves[857]=-8.55;    vns[857]=6.68;    vus[857]=-0.14;    stddevNs[857]=0.17;    stddevEs[857]=0.31;    stddevUs[857]=0.67;    corrNEs[857]=-0.007; 
   ids[858]='P343';    sts[858]='ChinaPeak_CN2007';    lats[858]=40.8871283882;    lngs[858]=236.6657899238;    ves[858]=-6.79;    vns[858]=8.08;    vus[858]=0.68;    stddevNs[858]=0.33;    stddevEs[858]=0.46;    stddevUs[858]=1.03;    corrNEs[858]=-0.003; 
   ids[859]='P344';    sts[859]='VinaHelitkCN2006';    lats[859]=39.9291233737;    lngs[859]=237.9720284843;    ves[859]=-11.14;    vns[859]=6.47;    vus[859]=-0.9;    stddevNs[859]=0.19;    stddevEs[859]=0.22;    stddevUs[859]=0.86;    corrNEs[859]=-0.007; 
   ids[860]='P345';    sts[860]='HookerDomeCN2005';    lats[860]=40.2712346922;    lngs[860]=237.7291937519;    ves[860]=-10.04;    vns[860]=6.6;    vus[860]=-1.22;    stddevNs[860]=0.2;    stddevEs[860]=0.25;    stddevUs[860]=0.8;    corrNEs[860]=-0.007; 
   ids[861]='P345';    sts[861]='HookerDomeCN2005';    lats[861]=40.2712346923;    lngs[861]=237.7291937388;    ves[861]=-10.04;    vns[861]=6.6;    vus[861]=-1.22;    stddevNs[861]=0.2;    stddevEs[861]=0.25;    stddevUs[861]=0.8;    corrNEs[861]=-0.007; 
   ids[862]='P346';    sts[862]='BuzzardRstCN2007';    lats[862]=39.7947101000;    lngs[862]=239.1325363500;    ves[862]=-10.73;    vns[862]=7.03;    vus[862]=-1.22;    stddevNs[862]=0.87;    stddevEs[862]=1.88;    stddevUs[862]=2.25;    corrNEs[862]=-0.000; 
   ids[863]='P347';    sts[863]='AdinCTYardCN2007';    lats[863]=41.1833435212;    lngs[863]=239.0515385424;    ves[863]=-6.51;    vns[863]=4.53;    vus[863]=-0.31;    stddevNs[863]=0.28;    stddevEs[863]=0.24;    stddevUs[863]=0.89;    corrNEs[863]=-0.003; 
   ids[864]='P348';    sts[864]='HatchetMtnCN2005';    lats[864]=40.9055461279;    lngs[864]=238.1719971884;    ves[864]=-7.81;    vns[864]=5.48;    vus[864]=-0.52;    stddevNs[864]=0.17;    stddevEs[864]=0.26;    stddevUs[864]=1;    corrNEs[864]=-0.006; 
   ids[865]='P349';    sts[865]='WonderlandCN2005';    lats[865]=40.7310857319;    lngs[865]=237.6806490711;    ves[865]=-8.14;    vns[865]=5.65;    vus[865]=2.25;    stddevNs[865]=0.23;    stddevEs[865]=0.25;    stddevUs[865]=0.76;    corrNEs[865]=-0.005; 
   ids[866]='P350';    sts[866]='SmokyDome_ID2008';    lats[866]=43.5327570163;    lngs[866]=245.1371615515;    ves[866]=-2.38;    vns[866]=-0.77;    vus[866]=-4.48;    stddevNs[866]=0.78;    stddevEs[866]=0.78;    stddevUs[866]=2.28;    corrNEs[866]=0.002; 
   ids[867]='P351';    sts[867]='Galena_Su_ID2008';    lats[867]=43.8744148222;    lngs[867]=245.2808377413;    ves[867]=-2.22;    vns[867]=-0.81;    vus[867]=-3.05;    stddevNs[867]=0.73;    stddevEs[867]=0.76;    stddevUs[867]=2.72;    corrNEs[867]=0.003; 
   ids[868]='P352';    sts[868]='Wildhorse_ID2008';    lats[868]=43.8490591921;    lngs[868]=245.9039251988;    ves[868]=-2.05;    vns[868]=-0.91;    vus[868]=-3.36;    stddevNs[868]=0.77;    stddevEs[868]=0.79;    stddevUs[868]=1.84;    corrNEs[868]=0.003; 
   ids[869]='P353';    sts[869]='Swensenbutid2008';    lats[869]=44.0545386745;    lngs[869]=246.0211992351;    ves[869]=-2.5;    vns[869]=-0.91;    vus[869]=-2.02;    stddevNs[869]=0.41;    stddevEs[869]=0.39;    stddevUs[869]=1.44;    corrNEs[869]=0.002; 
   ids[870]='P354';    sts[870]='SageCreek_ID2006';    lats[870]=44.1085114185;    lngs[870]=246.0210599032;    ves[870]=-2.22;    vns[870]=-0.69;    vus[870]=-1.92;    stddevNs[870]=0.22;    stddevEs[870]=0.16;    stddevUs[870]=0.69;    corrNEs[870]=-0.005; 
   ids[871]='P355';    sts[871]='BorahPeak_ID2008';    lats[871]=44.2179213698;    lngs[871]=246.2779133613;    ves[871]=-0.75;    vns[871]=0.32;    vus[871]=-3.86;    stddevNs[871]=0.4;    stddevEs[871]=0.5;    stddevUs[871]=1.68;    corrNEs[871]=0.001; 
   ids[872]='P356';    sts[872]='GTNPUhlHilWY2007';    lats[872]=43.8166370557;    lngs[872]=249.5108890504;    ves[872]=-2.16;    vns[872]=-1.12;    vus[872]=-5.08;    stddevNs[872]=0.23;    stddevEs[872]=0.21;    stddevUs[872]=0.92;    corrNEs[872]=0.000; 
   ids[873]='P357';    sts[873]='ElkhornCrkID2008';    lats[873]=44.2271053889;    lngs[873]=246.4181089884;    ves[873]=-2.01;    vns[873]=-0.18;    vus[873]=-3.36;    stddevNs[873]=0.44;    stddevEs[873]=0.41;    stddevUs[873]=1.69;    corrNEs[873]=0.001; 
   ids[874]='P358';    sts[874]='LemhiGulchID2007';    lats[874]=44.4018972546;    lngs[874]=246.7593359377;    ves[874]=-0.06;    vns[874]=0.13;    vus[874]=-2.63;    stddevNs[874]=0.24;    stddevEs[874]=0.24;    stddevUs[874]=0.96;    corrNEs[874]=-0.000; 
   ids[875]='P359';    sts[875]='SwanValleyID2007';    lats[875]=43.4824905177;    lngs[875]=248.4705002429;    ves[875]=-2.66;    vns[875]=-1.85;    vus[875]=-3.95;    stddevNs[875]=0.24;    stddevEs[875]=0.27;    stddevUs[875]=0.91;    corrNEs[875]=-0.000; 
   ids[876]='P360';    sts[876]='HLFIslParkID2005';    lats[876]=44.3178515189;    lngs[876]=248.5493228663;    ves[876]=-1.98;    vns[876]=-2.71;    vus[876]=-2.6;    stddevNs[876]=0.23;    stddevEs[876]=0.16;    stddevUs[876]=0.82;    corrNEs[876]=-0.003; 
   ids[877]='P361';    sts[877]='HLFSawtellID2007';    lats[877]=44.5603481342;    lngs[877]=248.5603112415;    ves[877]=-3.05;    vns[877]=-4.41;    vus[877]=-12.23;    stddevNs[877]=1.17;    stddevEs[877]=0.5;    stddevUs[877]=1.53;    corrNEs[877]=0.002; 
   ids[878]='P361';    sts[878]='HLFSawtellID2007';    lats[878]=44.5603480988;    lngs[878]=248.5603112416;    ves[878]=-3.05;    vns[878]=-4.41;    vus[878]=-12.23;    stddevNs[878]=1.17;    stddevEs[878]=0.5;    stddevUs[878]=1.53;    corrNEs[878]=0.002; 
   ids[879]='P361';    sts[879]='HLFSawtellID2007';    lats[879]=44.5603481046;    lngs[879]=248.5603112358;    ves[879]=-3.05;    vns[879]=-4.41;    vus[879]=-12.23;    stddevNs[879]=1.17;    stddevEs[879]=0.5;    stddevUs[879]=1.53;    corrNEs[879]=0.002; 
   ids[880]='P362';    sts[880]='BosleyButtOR2007';    lats[880]=42.2091042232;    lngs[880]=235.7742119773;    ves[880]=0.67;    vns[880]=10.62;    vus[880]=-0.21;    stddevNs[880]=0.24;    stddevEs[880]=0.36;    stddevUs[880]=0.78;    corrNEs[880]=-0.004; 
   ids[881]='P363';    sts[881]='DingbatFltOR2007';    lats[881]=42.8600639972;    lngs[881]=235.9459519562;    ves[881]=0.51;    vns[881]=9.63;    vus[881]=-1.03;    stddevNs[881]=0.36;    stddevEs[881]=0.33;    stddevUs[881]=0.73;    corrNEs[881]=-0.003; 
   ids[882]='P364';    sts[882]='BandonArptOR2007';    lats[882]=43.0903281678;    lngs[882]=235.5907104168;    ves[882]=4.9;    vns[882]=11.94;    vus[882]=1.13;    stddevNs[882]=0.41;    stddevEs[882]=0.26;    stddevUs[882]=0.97;    corrNEs[882]=-0.003; 
   ids[883]='P365';    sts[883]='SWOregnCC_OR2007';    lats[883]=43.3955186219;    lngs[883]=235.7465179886;    ves[883]=4.56;    vns[883]=10.54;    vus[883]=-1.59;    stddevNs[883]=0.41;    stddevEs[883]=0.41;    stddevUs[883]=0.89;    corrNEs[883]=-0.002; 
   ids[884]='P366';    sts[884]='Dean_Mtn__OR2008';    lats[884]=43.6143109955;    lngs[884]=236.0204067858;    ves[884]=4.22;    vns[884]=9.53;    vus[884]=-0.86;    stddevNs[884]=0.64;    stddevEs[884]=0.63;    stddevUs[884]=1.5;    corrNEs[884]=-0.001; 
   ids[885]='P367';    sts[885]='NewprtAir_OR2007';    lats[885]=44.5852466048;    lngs[885]=235.9384293821;    ves[885]=5.35;    vns[885]=9.56;    vus[885]=-1.81;    stddevNs[885]=0.26;    stddevEs[885]=0.45;    stddevUs[885]=0.74;    corrNEs[885]=-0.002; 
   ids[886]='P368';    sts[886]='Grants038GOR2006';    lats[886]=42.5035248534;    lngs[886]=236.6165802245;    ves[886]=-2.44;    vns[886]=8.14;    vus[886]=-0.79;    stddevNs[886]=0.2;    stddevEs[886]=0.26;    stddevUs[886]=0.88;    corrNEs[886]=-0.005; 
   ids[887]='P369';    sts[887]='WildSafariOR2005';    lats[887]=43.1401052287;    lngs[887]=236.5705405113;    ves[887]=-0.59;    vns[887]=7.7;    vus[887]=-1.76;    stddevNs[887]=0.17;    stddevEs[887]=0.25;    stddevUs[887]=0.81;    corrNEs[887]=-0.007; 
   ids[888]='P370';    sts[888]='AshlandAirOR2005';    lats[888]=42.1909873129;    lngs[888]=237.3436289260;    ves[888]=-2.69;    vns[888]=6.9;    vus[888]=-1.23;    stddevNs[888]=0.16;    stddevEs[888]=0.24;    stddevUs[888]=0.65;    corrNEs[888]=-0.007; 
   ids[889]='P371';    sts[889]='Lone_Rock_OR2005';    lats[889]=43.3632738749;    lngs[889]=236.9421010718;    ves[889]=-0.57;    vns[889]=6.71;    vus[889]=-1.51;    stddevNs[889]=0.19;    stddevEs[889]=0.29;    stddevUs[889]=0.53;    corrNEs[889]=-0.005; 
   ids[890]='P372';    sts[890]='EnterpriseOR2007';    lats[890]=45.4281377429;    lngs[890]=242.7483429895;    ves[890]=-0.13;    vns[890]=0.54;    vus[890]=-1.21;    stddevNs[890]=0.23;    stddevEs[890]=0.28;    stddevUs[890]=1;    corrNEs[890]=-0.003; 
   ids[891]='P373';    sts[891]='BaldyRanchOR2005';    lats[891]=43.6225193694;    lngs[891]=236.6667258820;    ves[891]=-0.18;    vns[891]=7.13;    vus[891]=-0.09;    stddevNs[891]=0.21;    stddevEs[891]=0.38;    stddevUs[891]=0.68;    corrNEs[891]=-0.004; 
   ids[892]='P373';    sts[892]='BaldyRanchOR2005';    lats[892]=43.6225193618;    lngs[892]=236.6667258612;    ves[892]=-0.18;    vns[892]=7.13;    vus[892]=-0.09;    stddevNs[892]=0.21;    stddevEs[892]=0.38;    stddevUs[892]=0.68;    corrNEs[892]=-0.004; 
   ids[893]='P374';    sts[893]='StoutAlseaOR2006';    lats[893]=44.3819260849;    lngs[893]=236.4093603164;    ves[893]=2.79;    vns[893]=7.64;    vus[893]=-1.13;    stddevNs[893]=0.31;    stddevEs[893]=0.3;    stddevUs[893]=0.74;    corrNEs[893]=-0.004; 
   ids[894]='P375';    sts[894]='SunriseFrmOR2007';    lats[894]=44.6892548328;    lngs[894]=236.5730136892;    ves[894]=2.66;    vns[894]=6.34;    vus[894]=0.24;    stddevNs[894]=0.76;    stddevEs[894]=0.52;    stddevUs[894]=1.45;    corrNEs[894]=-0.012; 
   ids[895]='P375';    sts[895]='SunriseFrmOR2007';    lats[895]=44.6892548320;    lngs[895]=236.5730136694;    ves[895]=2.66;    vns[895]=6.34;    vus[895]=0.24;    stddevNs[895]=0.76;    stddevEs[895]=0.52;    stddevUs[895]=1.45;    corrNEs[895]=-0.012; 
   ids[896]='P376';    sts[896]='EOLARESVR_OR2004';    lats[896]=44.9412030928;    lngs[896]=236.8977340597;    ves[896]=2.57;    vns[896]=6.67;    vus[896]=-0.4;    stddevNs[896]=0.21;    stddevEs[896]=0.26;    stddevUs[896]=0.54;    corrNEs[896]=-0.004; 
   ids[897]='P377';    sts[897]='MemorialPkOR2006';    lats[897]=44.0521189415;    lngs[897]=237.1129423609;    ves[897]=1.52;    vns[897]=5.94;    vus[897]=-1.87;    stddevNs[897]=0.22;    stddevEs[897]=0.39;    stddevUs[897]=0.63;    corrNEs[897]=-0.004; 
   ids[898]='P378';    sts[898]='LebanonAirOR2006';    lats[898]=44.5349826916;    lngs[898]=237.0691189499;    ves[898]=2.46;    vns[898]=6.26;    vus[898]=-1.59;    stddevNs[898]=0.33;    stddevEs[898]=0.63;    stddevUs[898]=0.87;    corrNEs[898]=-0.001; 
   ids[899]='P379';    sts[899]='BaldPeter_OR2006';    lats[899]=44.4965050168;    lngs[899]=237.4229564708;    ves[899]=1.39;    vns[899]=5.6;    vus[899]=-2.54;    stddevNs[899]=0.22;    stddevEs[899]=0.4;    stddevUs[899]=0.68;    corrNEs[899]=-0.003; 
   ids[900]='P380';    sts[900]='OITCampus_OR2005';    lats[900]=42.2596706585;    lngs[900]=238.2203027303;    ves[900]=-3.07;    vns[900]=5.27;    vus[900]=-2.2;    stddevNs[900]=0.15;    stddevEs[900]=0.18;    stddevUs[900]=0.74;    corrNEs[900]=-0.010; 
   ids[901]='P381';    sts[901]='AlkaliLakeOR2007';    lats[901]=43.0017768881;    lngs[901]=240.0481972067;    ves[901]=-0.67;    vns[901]=2.7;    vus[901]=-2.08;    stddevNs[901]=0.34;    stddevEs[901]=0.27;    stddevUs[901]=0.78;    corrNEs[901]=-0.002; 
   ids[902]='P382';    sts[902]='WalkerRim_OR2008';    lats[902]=43.1770569629;    lngs[902]=238.2304266646;    ves[902]=-1.6;    vns[902]=5.09;    vus[902]=-2.41;    stddevNs[902]=0.68;    stddevEs[902]=0.59;    stddevUs[902]=1.9;    corrNEs[902]=-0.001; 
   ids[903]='P383';    sts[903]='TwinButte_OR2007';    lats[903]=44.3421847912;    lngs[903]=237.7827800802;    ves[903]=0.55;    vns[903]=5.53;    vus[903]=-4.06;    stddevNs[903]=0.39;    stddevEs[903]=0.46;    stddevUs[903]=0.99;    corrNEs[903]=-0.004; 
   ids[904]='P383';    sts[904]='TwinButte_OR2007';    lats[904]=44.3421848005;    lngs[904]=237.7827800603;    ves[904]=0.55;    vns[904]=5.53;    vus[904]=-4.06;    stddevNs[904]=0.39;    stddevEs[904]=0.46;    stddevUs[904]=0.99;    corrNEs[904]=-0.004; 
   ids[905]='P384';    sts[905]='House_Mtn_OR2008';    lats[905]=44.8411308767;    lngs[905]=237.5171822317;    ves[905]=2.19;    vns[905]=6.19;    vus[905]=-1.03;    stddevNs[905]=0.71;    stddevEs[905]=0.51;    stddevUs[905]=2.03;    corrNEs[905]=-0.001; 
   ids[906]='P385';    sts[906]='SantiamJctOR2007';    lats[906]=44.4348485712;    lngs[906]=238.0541664897;    ves[906]=0.79;    vns[906]=4.05;    vus[906]=-1.79;    stddevNs[906]=1.14;    stddevEs[906]=0.62;    stddevUs[906]=2.95;    corrNEs[906]=-0.001; 
   ids[907]='P386';    sts[907]='GrantApt__OR2007';    lats[907]=44.4028278671;    lngs[907]=241.0321832765;    ves[907]=-0.84;    vns[907]=1.15;    vus[907]=-1.72;    stddevNs[907]=0.24;    stddevEs[907]=0.27;    stddevUs[907]=0.82;    corrNEs[907]=-0.003; 
   ids[908]='P387';    sts[908]='SistersHSLOR2006';    lats[908]=44.2967562687;    lngs[908]=238.4255234923;    ves[908]=1.58;    vns[908]=2.35;    vus[908]=-5.05;    stddevNs[908]=0.53;    stddevEs[908]=0.35;    stddevUs[908]=0.81;    corrNEs[908]=-0.001; 
   ids[909]='P387';    sts[909]='SistersHSLOR2006';    lats[909]=44.2967562628;    lngs[909]=238.4255234662;    ves[909]=1.58;    vns[909]=2.35;    vus[909]=-5.05;    stddevNs[909]=0.53;    stddevEs[909]=0.35;    stddevUs[909]=0.81;    corrNEs[909]=-0.001; 
   ids[910]='P388';    sts[910]='Willow_SprOR2005';    lats[910]=42.4687797988;    lngs[910]=239.6223898137;    ves[910]=-3.45;    vns[910]=3.43;    vus[910]=-1.51;    stddevNs[910]=0.18;    stddevEs[910]=0.2;    stddevUs[910]=0.56;    corrNEs[910]=-0.007; 
   ids[911]='P389';    sts[911]='BrothersDTOR2007';    lats[911]=43.8119925677;    lngs[911]=239.3966393598;    ves[911]=-1.1;    vns[911]=-5.35;    vus[911]=-2.04;    stddevNs[911]=3.45;    stddevEs[911]=0.24;    stddevUs[911]=0.83;    corrNEs[911]=-0.000; 
   ids[912]='P390';    sts[912]='JackassButOR2007';    lats[912]=43.0340079722;    lngs[912]=241.0715109339;    ves[912]=-2.68;    vns[912]=1.47;    vus[912]=-0.94;    stddevNs[912]=0.23;    stddevEs[912]=0.23;    stddevUs[912]=0.73;    corrNEs[912]=-0.002; 
   ids[913]='P391';    sts[913]='BuckskinMtOR2008';    lats[913]=42.2546294659;    lngs[913]=241.5875270042;    ves[913]=-5.67;    vns[913]=4.41;    vus[913]=-6.83;    stddevNs[913]=1.31;    stddevEs[913]=0.95;    stddevUs[913]=4.1;    corrNEs[913]=0.003; 
   ids[914]='P391';    sts[914]='BuckskinMtOR2008';    lats[914]=42.2546294584;    lngs[914]=241.5875270241;    ves[914]=-5.67;    vns[914]=4.41;    vus[914]=-6.83;    stddevNs[914]=1.31;    stddevEs[914]=0.95;    stddevUs[914]=4.1;    corrNEs[914]=0.003; 
   ids[915]='P392';    sts[915]='WrightsPt_OR2007';    lats[915]=43.4467591039;    lngs[915]=240.9989997624;    ves[915]=-2.18;    vns[915]=-0.01;    vus[915]=-2.19;    stddevNs[915]=0.42;    stddevEs[915]=0.25;    stddevUs[915]=0.74;    corrNEs[915]=-0.002; 
   ids[916]='P393';    sts[916]='Barren_VlyOR2007';    lats[916]=43.2344956614;    lngs[916]=242.1079621726;    ves[916]=-1.11;    vns[916]=-0.37;    vus[916]=-1.98;    stddevNs[916]=0.55;    stddevEs[916]=0.42;    stddevUs[916]=0.85;    corrNEs[916]=-0.000; 
   ids[917]='P394';    sts[917]='BakerCity_OR2007';    lats[917]=44.8348812379;    lngs[917]=242.2003625560;    ves[917]=-0.59;    vns[917]=0.66;    vus[917]=-2.07;    stddevNs[917]=0.22;    stddevEs[917]=0.24;    stddevUs[917]=0.9;    corrNEs[917]=-0.004; 
   ids[918]='P395';    sts[918]='Rose_LodgeOR2006';    lats[918]=45.0222795411;    lngs[918]=236.1424734537;    ves[918]=5.57;    vns[918]=9.24;    vus[918]=-2.6;    stddevNs[918]=0.27;    stddevEs[918]=0.3;    stddevUs[918]=0.67;    corrNEs[918]=-0.003; 
   ids[919]='P396';    sts[919]='RoosBC026gOR2007';    lats[919]=45.3095136380;    lngs[919]=236.1771049456;    ves[919]=5.82;    vns[919]=9.4;    vus[919]=-0.15;    stddevNs[919]=0.26;    stddevEs[919]=0.61;    stddevUs[919]=1.05;    corrNEs[919]=-0.002; 
   ids[920]='P396';    sts[920]='RoosBC026gOR2007';    lats[920]=45.3095136277;    lngs[920]=236.1771049123;    ves[920]=5.82;    vns[920]=9.4;    vus[920]=-0.15;    stddevNs[920]=0.26;    stddevEs[920]=0.61;    stddevUs[920]=1.05;    corrNEs[920]=-0.002; 
   ids[921]='P397';    sts[921]='RadarRidgeWA2007';    lats[921]=46.4216194685;    lngs[921]=236.2008400583;    ves[921]=6.85;    vns[921]=8.61;    vus[921]=-2.41;    stddevNs[921]=0.28;    stddevEs[921]=0.33;    stddevUs[921]=0.81;    corrNEs[921]=-0.002; 
   ids[922]='P397';    sts[922]='RadarRidgeWA2007';    lats[922]=46.4216194659;    lngs[922]=236.2008400430;    ves[922]=6.85;    vns[922]=8.61;    vus[922]=-2.41;    stddevNs[922]=0.28;    stddevEs[922]=0.33;    stddevUs[922]=0.81;    corrNEs[922]=-0.002; 
   ids[923]='P398';    sts[923]='StaffordCrWA2006';    lats[923]=46.9257850426;    lngs[923]=236.0838558244;    ves[923]=8.99;    vns[923]=8.1;    vus[923]=-0.5;    stddevNs[923]=0.2;    stddevEs[923]=0.43;    stddevUs[923]=0.72;    corrNEs[923]=-0.002; 
   ids[924]='P399';    sts[924]='Wynoochee_WA2008';    lats[924]=47.4339200435;    lngs[924]=236.3870396989;    ves[924]=8.58;    vns[924]=5.99;    vus[924]=2.64;    stddevNs[924]=0.49;    stddevEs[924]=0.75;    stddevUs[924]=2.01;    corrNEs[924]=-0.002; 
   ids[925]='P400';    sts[925]='Quinlt014gWA2008';    lats[925]=47.5133488006;    lngs[925]=236.1875498949;    ves[925]=5.73;    vns[925]=6.06;    vus[925]=16.11;    stddevNs[925]=0.74;    stddevEs[925]=1.3;    stddevUs[925]=2.41;    corrNEs[925]=-0.017; 
   ids[926]='P400';    sts[926]='Quinlt014gWA2008';    lats[926]=47.5133487893;    lngs[926]=236.1875498609;    ves[926]=5.73;    vns[926]=6.06;    vus[926]=16.11;    stddevNs[926]=0.74;    stddevEs[926]=1.3;    stddevUs[926]=2.41;    corrNEs[926]=-0.017; 
   ids[927]='P401';    sts[927]='Quillayut_WA2005';    lats[927]=47.9371880932;    lngs[927]=235.4429813226;    ves[927]=13.58;    vns[927]=9.56;    vus[927]=0.77;    stddevNs[927]=0.16;    stddevEs[927]=0.21;    stddevUs[927]=0.51;    corrNEs[927]=-0.006; 
   ids[928]='P402';    sts[928]='HohHumRch_WA2007';    lats[928]=47.7662215546;    lngs[928]=235.6941033313;    ves[928]=12.46;    vns[928]=9.8;    vus[928]=1.6;    stddevNs[928]=0.23;    stddevEs[928]=0.34;    stddevUs[928]=0.68;    corrNEs[928]=-0.003; 
   ids[929]='P403';    sts[929]='FloeQuaryGWA2005';    lats[929]=48.0623222376;    lngs[929]=235.8591255615;    ves[929]=10.36;    vns[929]=9.73;    vus[929]=0.78;    stddevNs[929]=0.87;    stddevEs[929]=0.65;    stddevUs[929]=0.74;    corrNEs[929]=-0.000; 
   ids[930]='P404';    sts[930]='PovHollow_OR2005';    lats[930]=45.1585341075;    lngs[930]=236.6096725308;    ves[930]=4.27;    vns[930]=7.05;    vus[930]=-1.47;    stddevNs[930]=0.34;    stddevEs[930]=0.23;    stddevUs[930]=0.56;    corrNEs[930]=-0.003; 
   ids[931]='P405';    sts[931]='Tri_Point_OR2006';    lats[931]=45.6293273025;    lngs[931]=236.3562433599;    ves[931]=5.27;    vns[931]=7.69;    vus[931]=-1.4;    stddevNs[931]=0.31;    stddevEs[931]=0.38;    stddevUs[931]=0.83;    corrNEs[931]=-0.002; 
   ids[932]='P406';    sts[932]='McMinvilleOR2005';    lats[932]=45.1903726221;    lngs[932]=236.8477437257;    ves[932]=3.47;    vns[932]=6.31;    vus[932]=-1.84;    stddevNs[932]=0.37;    stddevEs[932]=0.23;    stddevUs[932]=0.5;    corrNEs[932]=-0.003; 
   ids[933]='P407';    sts[933]='Seaside22GOR2006';    lats[933]=45.9546496765;    lngs[933]=236.0689946652;    ves[933]=11.87;    vns[933]=8.48;    vus[933]=-0.96;    stddevNs[933]=0.24;    stddevEs[933]=0.6;    stddevUs[933]=0.64;    corrNEs[933]=-0.000; 
   ids[934]='P408';    sts[934]='Whakiakum_WA2005';    lats[934]=46.2005020709;    lngs[934]=236.6234138745;    ves[934]=4.62;    vns[934]=6.94;    vus[934]=-0.83;    stddevNs[934]=0.53;    stddevEs[934]=0.24;    stddevUs[934]=0.8;    corrNEs[934]=-0.002; 
   ids[935]='P409';    sts[935]='VernoniaAPOR2005';    lats[935]=45.8512939114;    lngs[935]=236.7605229285;    ves[935]=4.28;    vns[935]=6.36;    vus[935]=-0.48;    stddevNs[935]=0.54;    stddevEs[935]=0.22;    stddevUs[935]=0.58;    corrNEs[935]=-0.002; 
   ids[936]='P410';    sts[936]='Cataln023GOR2008';    lats[936]=46.1111419822;    lngs[936]=236.9213949807;    ves[936]=0.14;    vns[936]=8.72;    vus[936]=8.31;    stddevNs[936]=2;    stddevEs[936]=2.82;    stddevUs[936]=2.61;    corrNEs[936]=-0.000; 
   ids[937]='P411';    sts[937]='ForestGrovOR2006';    lats[937]=45.5379931103;    lngs[937]=236.8425834084;    ves[937]=4.15;    vns[937]=6.51;    vus[937]=-0.85;    stddevNs[937]=0.55;    stddevEs[937]=0.37;    stddevUs[937]=0.78;    corrNEs[937]=-0.002; 
   ids[938]='P411';    sts[938]='ForestGrovOR2006';    lats[938]=45.5379931152;    lngs[938]=236.8425833816;    ves[938]=4.15;    vns[938]=6.51;    vus[938]=-0.85;    stddevNs[938]=0.55;    stddevEs[938]=0.37;    stddevUs[938]=0.78;    corrNEs[938]=-0.002; 
   ids[939]='P412';    sts[939]='MulinoAir_OR2006';    lats[939]=45.2211060752;    lngs[939]=237.4108991934;    ves[939]=2.36;    vns[939]=5.62;    vus[939]=-1.74;    stddevNs[939]=0.21;    stddevEs[939]=0.33;    stddevUs[939]=0.69;    corrNEs[939]=-0.004; 
   ids[940]='P413';    sts[940]='Methow_Ap_WA2008';    lats[940]=48.4265120348;    lngs[940]=239.8504165990;    ves[940]=1.09;    vns[940]=1.35;    vus[940]=-1.86;    stddevNs[940]=0.97;    stddevEs[940]=0.82;    stddevUs[940]=1.87;    corrNEs[940]=-0.000; 
   ids[941]='P414';    sts[941]='RidgefieldWA2006';    lats[941]=45.8348687877;    lngs[941]=237.3071629507;    ves[941]=3.46;    vns[941]=5.48;    vus[941]=-2.34;    stddevNs[941]=0.25;    stddevEs[941]=0.35;    stddevUs[941]=0.65;    corrNEs[941]=-0.003; 
   ids[942]='P415';    sts[942]='WHGOLFCRS_WA2004';    lats[942]=46.6559910710;    lngs[942]=236.2701348096;    ves[942]=7.36;    vns[942]=7.8;    vus[942]=-0.57;    stddevNs[942]=0.22;    stddevEs[942]=0.37;    stddevUs[942]=0.54;    corrNEs[942]=-0.002; 
   ids[943]='P416';    sts[943]='Suntop_LO_WA2008';    lats[943]=47.0399423989;    lngs[943]=238.4030722285;    ves[943]=1.53;    vns[943]=4.01;    vus[943]=-2.75;    stddevNs[943]=5.51;    stddevEs[943]=13.45;    stddevUs[943]=3.92;    corrNEs[943]=0.000; 
   ids[944]='P417';    sts[944]='Pe_Ell_HS_WA2005';    lats[944]=46.5747381580;    lngs[944]=236.7020707121;    ves[944]=4.57;    vns[944]=6.33;    vus[944]=-0.53;    stddevNs[944]=0.35;    stddevEs[944]=0.24;    stddevUs[944]=0.71;    corrNEs[944]=-0.003; 
   ids[945]='P418';    sts[945]='Matlock___WA2006';    lats[945]=47.2366453494;    lngs[945]=236.5921870709;    ves[945]=5.5;    vns[945]=5.63;    vus[945]=-0.62;    stddevNs[945]=0.28;    stddevEs[945]=0.51;    stddevUs[945]=0.7;    corrNEs[945]=-0.001; 
   ids[946]='P419';    sts[946]='Rock_Peak_WA2008';    lats[946]=47.4092989013;    lngs[946]=236.6334742153;    ves[946]=6.96;    vns[946]=4.16;    vus[946]=-0.03;    stddevNs[946]=1.1;    stddevEs[946]=1.03;    stddevUs[946]=2.87;    corrNEs[946]=-0.003; 
   ids[947]='P420';    sts[947]='FANTASYFD_WA2004';    lats[947]=46.5886023432;    lngs[947]=237.1336687148;    ves[947]=4.11;    vns[947]=5.57;    vus[947]=-1.08;    stddevNs[947]=0.32;    stddevEs[947]=0.27;    stddevUs[947]=0.59;    corrNEs[947]=-0.003; 
   ids[948]='P421';    sts[948]='MossyRock_WA2004';    lats[948]=46.5318562846;    lngs[948]=237.5707713745;    ves[948]=3.46;    vns[948]=4.34;    vus[948]=-0.83;    stddevNs[948]=0.39;    stddevEs[948]=0.31;    stddevUs[948]=1.11;    corrNEs[948]=-0.002; 
   ids[949]='P421';    sts[949]='MossyRock_WA2004';    lats[949]=46.5318562628;    lngs[949]=237.5707713477;    ves[949]=3.46;    vns[949]=4.34;    vus[949]=-0.83;    stddevNs[949]=0.39;    stddevEs[949]=0.31;    stddevUs[949]=1.11;    corrNEs[949]=-0.002; 
   ids[950]='P422';    sts[950]='Foot_Hill_ID2007';    lats[950]=46.7978658210;    lngs[950]=243.0203168524;    ves[950]=0.36;    vns[950]=-0.15;    vus[950]=-1.66;    stddevNs[950]=0.23;    stddevEs[950]=0.24;    stddevUs[950]=0.83;    corrNEs[950]=-0.003; 
   ids[951]='P423';    sts[951]='Grapeview_WA2005';    lats[951]=47.2879037478;    lngs[951]=237.0587903262;    ves[951]=4.5;    vns[951]=5.11;    vus[951]=-1.02;    stddevNs[951]=0.27;    stddevEs[951]=0.26;    stddevUs[951]=0.56;    corrNEs[951]=-0.003; 
   ids[952]='P424';    sts[952]='QuilcenehsWA2008';    lats[952]=47.8231841322;    lngs[952]=237.1252880752;    ves[952]=4.94;    vns[952]=3.83;    vus[952]=0.65;    stddevNs[952]=0.76;    stddevEs[952]=1.69;    stddevUs[952]=1.94;    corrNEs[952]=0.001; 
   ids[953]='P425';    sts[953]='Toledo_HS_WA2008';    lats[953]=46.4526941693;    lngs[953]=237.1546015489;    ves[953]=1.68;    vns[953]=4.98;    vus[953]=-1.79;    stddevNs[953]=0.79;    stddevEs[953]=0.76;    stddevUs[953]=1.73;    corrNEs[953]=-0.000; 
   ids[954]='P426';    sts[954]='GordonSch_WA2006';    lats[954]=47.8027250033;    lngs[954]=237.4854151205;    ves[954]=3.15;    vns[954]=2.81;    vus[954]=-3.79;    stddevNs[954]=0.23;    stddevEs[954]=0.4;    stddevUs[954]=0.75;    corrNEs[954]=-0.002; 
   ids[955]='P427';    sts[955]='US26BoringOR2006';    lats[955]=45.4301789191;    lngs[955]=237.6593692976;    ves[955]=2.11;    vns[955]=5.22;    vus[955]=-3.52;    stddevNs[955]=0.18;    stddevEs[955]=0.27;    stddevUs[955]=0.54;    corrNEs[955]=-0.005; 
   ids[956]='P428';    sts[956]='MtHoodSki_OR2005';    lats[956]=45.3445595698;    lngs[956]=238.3272884967;    ves[956]=26.89;    vns[956]=-14.32;    vus[956]=104.19;    stddevNs[956]=31.04;    stddevEs[956]=60.39;    stddevUs[956]=17.65;    corrNEs[956]=-0.000; 
   ids[957]='P429';    sts[957]='CascadeAirOR2006';    lats[957]=45.6761280055;    lngs[957]=238.1226404143;    ves[957]=1.6;    vns[957]=4.53;    vus[957]=-2.7;    stddevNs[957]=0.18;    stddevEs[957]=0.26;    stddevUs[957]=0.75;    corrNEs[957]=-0.006; 
   ids[958]='P430';    sts[958]='Elma_Elem_WA2005';    lats[958]=47.0038444479;    lngs[958]=236.5637782847;    ves[958]=5.45;    vns[958]=6.34;    vus[958]=-0.28;    stddevNs[958]=0.24;    stddevEs[958]=0.28;    stddevUs[958]=0.6;    corrNEs[958]=-0.003; 
   ids[959]='P431';    sts[959]='Watch_Mtn_WA2006';    lats[959]=46.5720817349;    lngs[959]=238.0115476271;    ves[959]=2.49;    vns[959]=4.08;    vus[959]=-2.73;    stddevNs[959]=0.29;    stddevEs[959]=0.57;    stddevUs[959]=0.86;    corrNEs[959]=-0.003; 
   ids[960]='P431';    sts[960]='Watch_Mtn_WA2006';    lats[960]=46.5720817331;    lngs[960]=238.0115475637;    ves[960]=2.49;    vns[960]=4.08;    vus[960]=-2.73;    stddevNs[960]=0.29;    stddevEs[960]=0.57;    stddevUs[960]=0.86;    corrNEs[960]=-0.003; 
   ids[961]='P432';    sts[961]='PackwoodF_WA2004';    lats[961]=46.6228559614;    lngs[961]=238.3167705408;    ves[961]=1.95;    vns[961]=2.97;    vus[961]=-0.86;    stddevNs[961]=0.15;    stddevEs[961]=0.17;    stddevUs[961]=0.62;    corrNEs[961]=-0.007; 
   ids[962]='P433';    sts[962]='Table_Mtn_OR2008';    lats[962]=44.5325223997;    lngs[962]=240.1279887213;    ves[962]=-0.67;    vns[962]=2.05;    vus[962]=0.04;    stddevNs[962]=0.44;    stddevEs[962]=0.37;    stddevUs[962]=1.62;    corrNEs[962]=-0.000; 
   ids[963]='P434';    sts[963]='Big_Chief_WA2008';    lats[963]=47.7402027655;    lngs[963]=238.9243917774;    ves[963]=2.1;    vns[963]=1.14;    vus[963]=-1.13;    stddevNs[963]=0.54;    stddevEs[963]=0.42;    stddevUs[963]=1.5;    corrNEs[963]=-0.001; 
   ids[964]='P435';    sts[964]='ShoresNW1GWA2005';    lats[964]=48.0595493196;    lngs[964]=236.4967219494;    ves[964]=6.3;    vns[964]=6.23;    vus[964]=-0.34;    stddevNs[964]=0.4;    stddevEs[964]=0.69;    stddevUs[964]=1.36;    corrNEs[964]=0.003; 
   ids[965]='P435';    sts[965]='ShoresNW1GWA2005';    lats[965]=48.0595493205;    lngs[965]=236.4967218821;    ves[965]=6.3;    vns[965]=6.23;    vus[965]=-0.34;    stddevNs[965]=0.4;    stddevEs[965]=0.69;    stddevUs[965]=1.36;    corrNEs[965]=0.003; 
   ids[966]='P435';    sts[966]='ShoresNW1GWA2005';    lats[966]=48.0595493094;    lngs[966]=236.4967218774;    ves[966]=6.3;    vns[966]=6.23;    vus[966]=-0.34;    stddevNs[966]=0.4;    stddevEs[966]=0.69;    stddevUs[966]=1.36;    corrNEs[966]=0.003; 
   ids[967]='P436';    sts[967]='Dungeness_WA2006';    lats[967]=48.0453027369;    lngs[967]=236.8656523776;    ves[967]=4.29;    vns[967]=3.93;    vus[967]=0.3;    stddevNs[967]=0.19;    stddevEs[967]=0.29;    stddevUs[967]=0.68;    corrNEs[967]=-0.003; 
   ids[968]='P437';    sts[968]='Whidbey_S_WA2007';    lats[968]=48.0018096713;    lngs[968]=237.5408429861;    ves[968]=3.31;    vns[968]=2.7;    vus[968]=-1.6;    stddevNs[968]=0.33;    stddevEs[968]=0.52;    stddevUs[968]=0.99;    corrNEs[968]=0.000; 
   ids[969]='P438';    sts[969]='NWIS_PNGA_WA2005';    lats[969]=48.4191480752;    lngs[969]=237.3297375318;    ves[969]=3.05;    vns[969]=2.66;    vus[969]=-3.33;    stddevNs[969]=0.22;    stddevEs[969]=0.5;    stddevUs[969]=0.92;    corrNEs[969]=-0.002; 
   ids[970]='P439';    sts[970]='OrcasAirptWA2005';    lats[970]=48.7081926908;    lngs[970]=237.0906969513;    ves[970]=4.2;    vns[970]=3.33;    vus[970]=-0.24;    stddevNs[970]=0.24;    stddevEs[970]=0.36;    stddevUs[970]=0.67;    corrNEs[970]=-0.002; 
   ids[971]='P440';    sts[971]='MeridianSDWA2006';    lats[971]=48.8561935182;    lngs[971]=237.5066593011;    ves[971]=2.32;    vns[971]=3.14;    vus[971]=1.22;    stddevNs[971]=1.04;    stddevEs[971]=0.52;    stddevUs[971]=1.49;    corrNEs[971]=-0.002; 
   ids[972]='P440';    sts[972]='MeridianSDWA2006';    lats[972]=48.8561935186;    lngs[972]=237.5066592985;    ves[972]=2.32;    vns[972]=3.14;    vus[972]=1.22;    stddevNs[972]=1.04;    stddevEs[972]=0.52;    stddevUs[972]=1.49;    corrNEs[972]=-0.002; 
   ids[973]='P441';    sts[973]='KendallEleWA2008';    lats[973]=48.9159790448;    lngs[973]=237.8603585633;    ves[973]=2.54;    vns[973]=1.39;    vus[973]=4.71;    stddevNs[973]=13.86;    stddevEs[973]=11.25;    stddevUs[973]=6.1;    corrNEs[973]=-0.000; 
   ids[974]='P442';    sts[974]='DarringtonWA2006';    lats[974]=48.2604802258;    lngs[974]=238.3844507726;    ves[974]=1.52;    vns[974]=1.72;    vus[974]=-2.89;    stddevNs[974]=0.25;    stddevEs[974]=0.22;    stddevUs[974]=0.78;    corrNEs[974]=-0.002; 
   ids[975]='P443';    sts[975]='La_Rush_LkWA2008';    lats[975]=48.5096479758;    lngs[975]=238.7144436808;    ves[975]=2.04;    vns[975]=2.7;    vus[975]=-0.31;    stddevNs[975]=0.74;    stddevEs[975]=0.63;    stddevUs[975]=1.54;    corrNEs[975]=-0.001; 
   ids[976]='P444';    sts[976]='RossLakeDmWA2006';    lats[976]=48.7302194576;    lngs[976]=238.9324526764;    ves[976]=1.63;    vns[976]=2.67;    vus[976]=-3.62;    stddevNs[976]=0.24;    stddevEs[976]=0.21;    stddevUs[976]=1.06;    corrNEs[976]=-0.014; 
   ids[977]='P445';    sts[977]='WascoAirptOR2005';    lats[977]=45.5901232577;    lngs[977]=239.3277542669;    ves[977]=0.21;    vns[977]=2.46;    vus[977]=-1.93;    stddevNs[977]=0.17;    stddevEs[977]=0.18;    stddevUs[977]=0.54;    corrNEs[977]=-0.006; 
   ids[978]='P446';    sts[978]='Kelso_Air_WA2007';    lats[978]=46.1156625168;    lngs[978]=237.1072127095;    ves[978]=4.61;    vns[978]=5.95;    vus[978]=-1.52;    stddevNs[978]=0.38;    stddevEs[978]=0.27;    stddevUs[978]=0.89;    corrNEs[978]=-0.001; 
   ids[979]='P447';    sts[979]='MorrowAir_OR2007';    lats[979]=45.4528214844;    lngs[979]=240.3098758383;    ves[979]=0.39;    vns[979]=1.51;    vus[979]=-1.62;    stddevNs[979]=0.27;    stddevEs[979]=0.3;    stddevUs[979]=0.86;    corrNEs[979]=-0.001; 
   ids[980]='P448';    sts[980]='HorsHeavenWA2005';    lats[980]=45.9105787390;    lngs[980]=239.9948000858;    ves[980]=0.39;    vns[980]=1.67;    vus[980]=-3.58;    stddevNs[980]=0.16;    stddevEs[980]=0.16;    stddevUs[980]=0.71;    corrNEs[980]=-0.007; 
   ids[981]='P449';    sts[981]='Olsen_BrosWA2006';    lats[981]=46.2597924981;    lngs[981]=240.3690037432;    ves[981]=0.17;    vns[981]=1.51;    vus[981]=-2.8;    stddevNs[981]=0.22;    stddevEs[981]=0.18;    stddevUs[981]=0.83;    corrNEs[981]=-0.004; 
   ids[982]='P450';    sts[982]='WattsBros_WA2005';    lats[982]=45.9533351967;    lngs[982]=240.4558060978;    ves[982]=0.19;    vns[982]=1.48;    vus[982]=-2.71;    stddevNs[982]=0.19;    stddevEs[982]=0.16;    stddevUs[982]=0.65;    corrNEs[982]=-0.006; 
   ids[983]='P451';    sts[983]='WSUOthelo_WA2004';    lats[983]=46.7927868152;    lngs[983]=240.9586389489;    ves[983]=0.33;    vns[983]=1.03;    vus[983]=-1.29;    stddevNs[983]=0.18;    stddevEs[983]=0.16;    stddevUs[983]=0.55;    corrNEs[983]=-0.006; 
   ids[984]='P451';    sts[984]='WSUOthelo_WA2004';    lats[984]=46.7927868101;    lngs[984]=240.9586389402;    ves[984]=0.33;    vns[984]=1.03;    vus[984]=-1.29;    stddevNs[984]=0.18;    stddevEs[984]=0.16;    stddevUs[984]=0.55;    corrNEs[984]=-0.006; 
   ids[985]='P451';    sts[985]='WSUOthelo_WA2004';    lats[985]=46.7927868159;    lngs[985]=240.9586389404;    ves[985]=0.33;    vns[985]=1.03;    vus[985]=-1.29;    stddevNs[985]=0.18;    stddevEs[985]=0.16;    stddevUs[985]=0.55;    corrNEs[985]=-0.006; 
   ids[986]='P452';    sts[986]='Soap_Lake_WA2005';    lats[986]=47.4035490186;    lngs[986]=240.5127181786;    ves[986]=0.77;    vns[986]=0.73;    vus[986]=-0.36;    stddevNs[986]=0.2;    stddevEs[986]=0.16;    stddevUs[986]=0.63;    corrNEs[986]=-0.003; 
   ids[987]='P453';    sts[987]='WilburLevaWA2005';    lats[987]=47.7591330629;    lngs[987]=241.2545438944;    ves[987]=0.55;    vns[987]=0.29;    vus[987]=-0.02;    stddevNs[987]=0.18;    stddevEs[987]=0.15;    stddevUs[987]=0.62;    corrNEs[987]=-0.006; 
   ids[988]='P454';    sts[988]='CouleeDam_WA2005';    lats[988]=47.9538161067;    lngs[988]=241.0074221258;    ves[988]=0.72;    vns[988]=0.56;    vus[988]=-0.8;    stddevNs[988]=0.27;    stddevEs[988]=0.26;    stddevUs[988]=0.57;    corrNEs[988]=-0.003; 
   ids[989]='P454';    sts[989]='CouleeDam_WA2005';    lats[989]=47.9538161079;    lngs[989]=241.0074221132;    ves[989]=0.72;    vns[989]=0.56;    vus[989]=-0.8;    stddevNs[989]=0.27;    stddevEs[989]=0.26;    stddevUs[989]=0.57;    corrNEs[989]=-0.003; 
   ids[990]='P454';    sts[990]='CouleeDam_WA2005';    lats[990]=47.9538161110;    lngs[990]=241.0074221128;    ves[990]=0.72;    vns[990]=0.56;    vus[990]=-0.8;    stddevNs[990]=0.27;    stddevEs[990]=0.26;    stddevUs[990]=0.57;    corrNEs[990]=-0.003; 
   ids[991]='P455';    sts[991]='BannackPasID2007';    lats[991]=44.4855682183;    lngs[991]=247.2714515540;    ves[991]=-1.84;    vns[991]=-1.32;    vus[991]=-4.8;    stddevNs[991]=0.25;    stddevEs[991]=0.21;    stddevUs[991]=0.94;    corrNEs[991]=0.000; 
   ids[992]='P456';    sts[992]='HLF_KirkwdMT2008';    lats[992]=44.8634868771;    lngs[992]=248.7748930868;    ves[992]=0.96;    vns[992]=0.2;    vus[992]=-0.22;    stddevNs[992]=0.72;    stddevEs[992]=0.52;    stddevUs[992]=1.68;    corrNEs[992]=0.002; 
   ids[993]='P457';    sts[993]='HLF_WapitiMT2007';    lats[993]=45.0412278315;    lngs[993]=248.7273674230;    ves[993]=-1.13;    vns[993]=1.54;    vus[993]=-0.03;    stddevNs[993]=0.38;    stddevEs[993]=0.49;    stddevUs[993]=0.92;    corrNEs[993]=-0.000; 
   ids[994]='P458';    sts[994]='HLFContourMT2007';    lats[994]=44.7656877647;    lngs[994]=248.6984903278;    ves[994]=-0.69;    vns[994]=-2.13;    vus[994]=-1.59;    stddevNs[994]=0.24;    stddevEs[994]=0.32;    stddevUs[994]=0.89;    corrNEs[994]=-0.001; 
   ids[995]='P459';    sts[995]='GTNPMoose_WY2007';    lats[995]=43.7483288915;    lngs[995]=249.2541902294;    ves[995]=-1.59;    vns[995]=-3.36;    vus[995]=-4.08;    stddevNs[995]=0.4;    stddevEs[995]=0.34;    stddevUs[995]=1.14;    corrNEs[995]=-0.005; 
   ids[996]='P460';    sts[996]='HLFBBarRchMT2005';    lats[996]=45.1399860148;    lngs[996]=248.9714061506;    ves[996]=0.45;    vns[996]=-0.15;    vus[996]=-0.98;    stddevNs[996]=0.28;    stddevEs[996]=0.15;    stddevUs[996]=0.94;    corrNEs[996]=-0.001; 
   ids[997]='P461';    sts[997]='HLFStoryRnMT2005';    lats[997]=45.3542500328;    lngs[997]=249.2413169071;    ves[997]=0.49;    vns[997]=-0.58;    vus[997]=-1.16;    stddevNs[997]=0.26;    stddevEs[997]=0.17;    stddevUs[997]=0.94;    corrNEs[997]=-0.001; 
   ids[998]='P462';    sts[998]='GoldValleyCS2006';    lats[998]=36.0713166758;    lngs[998]=243.3712919930;    ves[998]=-4.43;    vns[998]=2.03;    vus[998]=-1.45;    stddevNs[998]=0.18;    stddevEs[998]=0.19;    stddevUs[998]=0.7;    corrNEs[998]=-0.007; 
   ids[999]='P463';    sts[999]='Ballarat__CS2007';    lats[999]=36.0224656497;    lngs[999]=242.8353273279;    ves[999]=-6.06;    vns[999]=4.68;    vus[999]=-1.67;    stddevNs[999]=0.19;    stddevEs[999]=0.2;    stddevUs[999]=0.8;    corrNEs[999]=-0.008; 
   ids[1000]='P464';    sts[1000]='PanamintVQCS2006';    lats[1000]=36.1590461107;    lngs[1000]=242.5899998479;    ves[1000]=-7.01;    vns[1000]=5.17;    vus[1000]=-0.67;    stddevNs[1000]=0.23;    stddevEs[1000]=0.18;    stddevUs[1000]=0.57;    corrNEs[1000]=-0.004; 
   ids[1001]='P465';    sts[1001]='HorseshoeMCS2007';    lats[1001]=36.4668341586;    lngs[1001]=241.8675671029;    ves[1001]=-9.44;    vns[1001]=9.89;    vus[1001]=-1.08;    stddevNs[1001]=0.24;    stddevEs[1001]=0.27;    stddevUs[1001]=0.94;    corrNEs[1001]=-0.008; 
   ids[1002]='P466';    sts[1002]='CerroGordoCS2007';    lats[1002]=36.5312495481;    lngs[1002]=242.2105393817;    ves[1002]=-7.58;    vns[1002]=4.73;    vus[1002]=-2.04;    stddevNs[1002]=0.2;    stddevEs[1002]=0.21;    stddevUs[1002]=0.71;    corrNEs[1002]=-0.007; 
   ids[1003]='P467';    sts[1003]='AlabamaHilCS2006';    lats[1003]=36.5702021633;    lngs[1003]=241.9093772374;    ves[1003]=-7.09;    vns[1003]=8.11;    vus[1003]=-0.69;    stddevNs[1003]=0.6;    stddevEs[1003]=0.68;    stddevUs[1003]=0.74;    corrNEs[1003]=-0.004; 
   ids[1004]='P467';    sts[1004]='AlabamaHilCS2006';    lats[1004]=36.5702021456;    lngs[1004]=241.9093771467;    ves[1004]=-7.09;    vns[1004]=8.11;    vus[1004]=-0.69;    stddevNs[1004]=0.6;    stddevEs[1004]=0.68;    stddevUs[1004]=0.74;    corrNEs[1004]=-0.004; 
   ids[1005]='P468';    sts[1005]='MazourkaPkCS2006';    lats[1005]=36.9756823903;    lngs[1005]=241.8816374663;    ves[1005]=-7.89;    vns[1005]=5.56;    vus[1005]=-1.71;    stddevNs[1005]=0.19;    stddevEs[1005]=0.17;    stddevUs[1005]=0.73;    corrNEs[1005]=-0.009; 
   ids[1006]='P469';    sts[1006]='HarlisMineCS2007';    lats[1006]=37.2314251424;    lngs[1006]=242.0641905269;    ves[1006]=-6.39;    vns[1006]=3.72;    vus[1006]=-1.35;    stddevNs[1006]=0.2;    stddevEs[1006]=0.2;    stddevUs[1006]=0.79;    corrNEs[1006]=-0.006; 
   ids[1007]='P470';    sts[1007]='BaldyMesa_CS2004';    lats[1007]=34.4624048595;    lngs[1007]=242.6061111419;    ves[1007]=-14.61;    vns[1007]=14.79;    vus[1007]=-0.77;    stddevNs[1007]=0.15;    stddevEs[1007]=0.15;    stddevUs[1007]=0.52;    corrNEs[1007]=-0.012; 
   ids[1008]='P471';    sts[1008]='SanJuanCrkCS2005';    lats[1008]=33.5621250320;    lngs[1008]=242.4591348748;    ves[1008]=-26.58;    vns[1008]=26.46;    vus[1008]=-1.14;    stddevNs[1008]=0.21;    stddevEs[1008]=0.16;    stddevUs[1008]=0.52;    corrNEs[1008]=-0.008; 
   ids[1009]='P472';    sts[1009]='CampElliotCS2004';    lats[1009]=32.8892092067;    lngs[1009]=242.8953054296;    ves[1009]=-28.74;    vns[1009]=27.02;    vus[1009]=-1.73;    stddevNs[1009]=0.15;    stddevEs[1009]=0.14;    stddevUs[1009]=0.42;    corrNEs[1009]=-0.013; 
   ids[1010]='P473';    sts[1010]='Jamacha_LFCS2004';    lats[1010]=32.7337760982;    lngs[1010]=243.0504852866;    ves[1010]=-29.01;    vns[1010]=27.19;    vus[1010]=-2.8;    stddevNs[1010]=0.15;    stddevEs[1010]=0.14;    stddevUs[1010]=0.42;    corrNEs[1010]=-0.012; 
   ids[1011]='P474';    sts[1011]='Fallbrook_CS2004';    lats[1011]=33.3551929386;    lngs[1011]=242.7513086133;    ves[1011]=-27.32;    vns[1011]=26.69;    vus[1011]=-2.44;    stddevNs[1011]=0.15;    stddevEs[1011]=0.14;    stddevUs[1011]=0.49;    corrNEs[1011]=-0.013; 
   ids[1012]='P475';    sts[1012]='Point_LomaCS2007';    lats[1012]=32.6663957212;    lngs[1012]=242.7560655186;    ves[1012]=-29.75;    vns[1012]=29.04;    vus[1012]=-2.38;    stddevNs[1012]=0.22;    stddevEs[1012]=0.24;    stddevUs[1012]=0.86;    corrNEs[1012]=-0.003; 
   ids[1013]='P476';    sts[1013]='SantaMargaCS2005';    lats[1013]=33.4396491671;    lngs[1013]=242.8103238645;    ves[1013]=-26.94;    vns[1013]=26.2;    vus[1013]=-3.18;    stddevNs[1013]=0.16;    stddevEs[1013]=0.15;    stddevUs[1013]=0.67;    corrNEs[1013]=-0.011; 
   ids[1014]='P477';    sts[1014]='TemeculaVHCS2005';    lats[1014]=33.5028010480;    lngs[1014]=242.8866287932;    ves[1014]=-25.18;    vns[1014]=25.33;    vus[1014]=-6.34;    stddevNs[1014]=0.24;    stddevEs[1014]=0.19;    stddevUs[1014]=0.64;    corrNEs[1014]=-0.006; 
   ids[1015]='P478';    sts[1015]='ValleyCntrCS2004';    lats[1015]=33.2357150234;    lngs[1015]=242.9284099277;    ves[1015]=-27.61;    vns[1015]=26.5;    vus[1015]=-1.73;    stddevNs[1015]=0.22;    stddevEs[1015]=0.15;    stddevUs[1015]=0.51;    corrNEs[1015]=-0.009; 
   ids[1016]='P479';    sts[1016]='CowboyCtryCS2005';    lats[1016]=33.4933176838;    lngs[1016]=243.2170706445;    ves[1016]=-23.84;    vns[1016]=23.14;    vus[1016]=-0.86;    stddevNs[1016]=0.17;    stddevEs[1016]=0.16;    stddevUs[1016]=0.68;    corrNEs[1016]=-0.010; 
   ids[1017]='P480';    sts[1017]='Vallecito_CS2004';    lats[1017]=32.9759958363;    lngs[1017]=243.6514651899;    ves[1017]=-27.45;    vns[1017]=24.88;    vus[1017]=-1.58;    stddevNs[1017]=0.14;    stddevEs[1017]=0.14;    stddevUs[1017]=0.47;    corrNEs[1017]=-0.013; 
   ids[1018]='P481';    sts[1018]='CarrizoMtnCS2007';    lats[1018]=32.8222392741;    lngs[1018]=243.9892096785;    ves[1018]=-25.64;    vns[1018]=24.64;    vus[1018]=-0.36;    stddevNs[1018]=0.22;    stddevEs[1018]=0.23;    stddevUs[1018]=0.88;    corrNEs[1018]=-0.003; 
   ids[1019]='P482';    sts[1019]='LakHenshawCS2004';    lats[1019]=33.2401792589;    lngs[1019]=243.3285880927;    ves[1019]=-26.27;    vns[1019]=24.8;    vus[1019]=-1.58;    stddevNs[1019]=0.16;    stddevEs[1019]=0.14;    stddevUs[1019]=0.62;    corrNEs[1019]=-0.012; 
   ids[1020]='P483';    sts[1020]='JulianBin_CS2004';    lats[1020]=33.0591639153;    lngs[1020]=243.4306752892;    ves[1020]=-27.44;    vns[1020]=25.75;    vus[1020]=-1.67;    stddevNs[1020]=0.14;    stddevEs[1020]=0.15;    stddevUs[1020]=0.47;    corrNEs[1020]=-0.013; 
   ids[1021]='P484';    sts[1021]='SkyOaksBFSCS2005';    lats[1021]=33.3755924563;    lngs[1021]=243.3791628337;    ves[1021]=-24.53;    vns[1021]=22.98;    vus[1021]=-1.22;    stddevNs[1021]=0.17;    stddevEs[1021]=0.15;    stddevUs[1021]=0.59;    corrNEs[1021]=-0.010; 
   ids[1022]='P485';    sts[1022]='TubbCanyonCS2006';    lats[1022]=33.2102118050;    lngs[1022]=243.5909616086;    ves[1022]=-25.94;    vns[1022]=22.84;    vus[1022]=-2.22;    stddevNs[1022]=0.22;    stddevEs[1022]=0.21;    stddevUs[1022]=0.82;    corrNEs[1022]=-0.004; 
   ids[1023]='P485';    sts[1023]='TubbCanyonCS2006';    lats[1023]=33.2102118090;    lngs[1023]=243.5909616153;    ves[1023]=-25.94;    vns[1023]=22.84;    vus[1023]=-2.22;    stddevNs[1023]=0.22;    stddevEs[1023]=0.21;    stddevUs[1023]=0.82;    corrNEs[1023]=-0.004; 
   ids[1024]='P486';    sts[1024]='BorregoAirCS2004';    lats[1024]=33.2601914237;    lngs[1024]=243.6777145735;    ves[1024]=-23.83;    vns[1024]=21.8;    vus[1024]=-5.26;    stddevNs[1024]=0.15;    stddevEs[1024]=0.14;    stddevUs[1024]=0.44;    corrNEs[1024]=-0.011; 
   ids[1025]='P487';    sts[1025]='Cut_AcrossCS2007';    lats[1025]=33.2135314595;    lngs[1025]=243.8169281000;    ves[1025]=-22.43;    vns[1025]=19.99;    vus[1025]=-0.46;    stddevNs[1025]=0.23;    stddevEs[1025]=0.22;    stddevUs[1025]=0.76;    corrNEs[1025]=-0.003; 
   ids[1026]='P488';    sts[1026]='OcotilloWeCS2007';    lats[1026]=33.2010157161;    lngs[1026]=243.9211907801;    ves[1026]=-20.51;    vns[1026]=18.25;    vus[1026]=-1.25;    stddevNs[1026]=0.2;    stddevEs[1026]=0.19;    stddevUs[1026]=0.77;    corrNEs[1026]=-0.006; 
   ids[1027]='P489';    sts[1027]='CalciteMinCS2007';    lats[1027]=33.2962130948;    lngs[1027]=243.8883923855;    ves[1027]=-18.06;    vns[1027]=15.52;    vus[1027]=0.06;    stddevNs[1027]=0.23;    stddevEs[1027]=0.24;    stddevUs[1027]=0.97;    corrNEs[1027]=-0.004; 
   ids[1028]='P490';    sts[1028]='Toro_Peak_CS2006';    lats[1028]=33.5234623776;    lngs[1028]=243.5740958829;    ves[1028]=-18.24;    vns[1028]=17.56;    vus[1028]=-0.66;    stddevNs[1028]=0.19;    stddevEs[1028]=0.23;    stddevUs[1028]=0.67;    corrNEs[1028]=-0.005; 
   ids[1029]='P491';    sts[1029]='La_Quinta_CS2005';    lats[1029]=33.5746790044;    lngs[1029]=243.7731761649;    ves[1029]=-15.51;    vns[1029]=14.63;    vus[1029]=-0.65;    stddevNs[1029]=0.17;    stddevEs[1029]=0.18;    stddevUs[1029]=0.57;    corrNEs[1029]=-0.008; 
   ids[1030]='P492';    sts[1030]='CrzoBadlndCS2007';    lats[1030]=32.8870481529;    lngs[1030]=244.0305503147;    ves[1030]=-25.36;    vns[1030]=25.24;    vus[1030]=-0.58;    stddevNs[1030]=0.41;    stddevEs[1030]=0.38;    stddevUs[1030]=1.4;    corrNEs[1030]=-0.001; 
   ids[1031]='P492';    sts[1031]='CrzoBadlndCS2007';    lats[1031]=32.8870481582;    lngs[1031]=244.0305503148;    ves[1031]=-25.36;    vns[1031]=25.24;    vus[1031]=-0.58;    stddevNs[1031]=0.41;    stddevEs[1031]=0.38;    stddevUs[1031]=1.4;    corrNEs[1031]=-0.001; 
   ids[1032]='P493';    sts[1032]='Super_Mtn_CS2007';    lats[1032]=32.9547031493;    lngs[1032]=244.1751097989;    ves[1032]=-21.91;    vns[1032]=21.77;    vus[1032]=0.09;    stddevNs[1032]=0.22;    stddevEs[1032]=0.21;    stddevUs[1032]=0.78;    corrNEs[1032]=-0.003; 
   ids[1033]='P494';    sts[1033]='WestsideESCS2005';    lats[1033]=32.7596558678;    lngs[1033]=244.2679338962;    ves[1033]=-23.56;    vns[1033]=26.01;    vus[1033]=-4.97;    stddevNs[1033]=0.4;    stddevEs[1033]=0.45;    stddevUs[1033]=1.07;    corrNEs[1033]=-0.001; 
   ids[1034]='P495';    sts[1034]='WestmorlndCS2005';    lats[1034]=33.0449609921;    lngs[1034]=244.3716077519;    ves[1034]=-11.67;    vns[1034]=15.79;    vus[1034]=-6.01;    stddevNs[1034]=0.19;    stddevEs[1034]=0.17;    stddevUs[1034]=0.67;    corrNEs[1034]=-0.007; 
   ids[1035]='P496';    sts[1035]='McCabe_UESCS2005';    lats[1035]=32.7506269263;    lngs[1035]=244.4040473784;    ves[1035]=-25.55;    vns[1035]=29.35;    vus[1035]=2.1;    stddevNs[1035]=0.72;    stddevEs[1035]=0.65;    stddevUs[1035]=0.82;    corrNEs[1035]=-0.000; 
   ids[1036]='P497';    sts[1036]='ImperialApCS2005';    lats[1036]=32.8347356343;    lngs[1036]=244.4229695548;    ves[1036]=-16.84;    vns[1036]=23.03;    vus[1036]=-1.03;    stddevNs[1036]=0.25;    stddevEs[1036]=0.16;    stddevUs[1036]=0.58;    corrNEs[1036]=-0.005; 
   ids[1037]='P498';    sts[1037]='SprecklesSCS2005';    lats[1037]=32.8984312039;    lngs[1037]=244.4304349481;    ves[1037]=-14.6;    vns[1037]=20.69;    vus[1037]=-4.86;    stddevNs[1037]=0.17;    stddevEs[1037]=0.17;    stddevUs[1037]=0.88;    corrNEs[1037]=-0.007; 
   ids[1038]='P499';    sts[1038]='SDSUCenterCS2005';    lats[1038]=32.9796074420;    lngs[1038]=244.5120878031;    ves[1038]=-6.54;    vns[1038]=5.62;    vus[1038]=-4.96;    stddevNs[1038]=0.19;    stddevEs[1038]=0.17;    stddevUs[1038]=0.75;    corrNEs[1038]=-0.006; 
   ids[1039]='P500';    sts[1039]='AllAmericnCS2005';    lats[1039]=32.6900474534;    lngs[1039]=244.7000683635;    ves[1039]=-5.35;    vns[1039]=9.08;    vus[1039]=-1.63;    stddevNs[1039]=0.17;    stddevEs[1039]=0.28;    stddevUs[1039]=0.68;    corrNEs[1039]=-0.004; 
   ids[1040]='P501';    sts[1040]='HoltvilleCCS2005';    lats[1040]=32.8757572292;    lngs[1040]=244.6020881705;    ves[1040]=-3.81;    vns[1040]=6.02;    vus[1040]=-4.34;    stddevNs[1040]=0.27;    stddevEs[1040]=0.2;    stddevUs[1040]=1.07;    corrNEs[1040]=-0.004; 
   ids[1041]='P502';    sts[1041]='MagnoliaESCS2005';    lats[1041]=32.9824520987;    lngs[1041]=244.5780888732;    ves[1041]=-4.97;    vns[1041]=4.13;    vus[1041]=-2.93;    stddevNs[1041]=0.22;    stddevEs[1041]=0.15;    stddevUs[1041]=0.6;    corrNEs[1041]=-0.006; 
   ids[1042]='P503';    sts[1042]='SuperHillsCS2007';    lats[1042]=32.9489156006;    lngs[1042]=244.2798189688;    ves[1042]=-18.98;    vns[1042]=20.73;    vus[1042]=-1.53;    stddevNs[1042]=0.21;    stddevEs[1042]=0.2;    stddevUs[1042]=0.89;    corrNEs[1042]=-0.005; 
   ids[1043]='P504';    sts[1043]='SOrocopiaMCS2005';    lats[1043]=33.5164107086;    lngs[1043]=244.2341534839;    ves[1043]=-8.76;    vns[1043]=4.57;    vus[1043]=-1.24;    stddevNs[1043]=0.19;    stddevEs[1043]=0.15;    stddevUs[1043]=0.49;    corrNEs[1043]=-0.007; 
   ids[1044]='P505';    sts[1044]='ImperialSpCS2006';    lats[1044]=33.4238741066;    lngs[1044]=244.3130010031;    ves[1044]=-6.03;    vns[1044]=3.44;    vus[1044]=-2.34;    stddevNs[1044]=0.24;    stddevEs[1044]=0.18;    stddevUs[1044]=0.82;    corrNEs[1044]=-0.004; 
   ids[1045]='P505';    sts[1045]='ImperialSpCS2006';    lats[1045]=33.4238740993;    lngs[1045]=244.3130009908;    ves[1045]=-6.03;    vns[1045]=3.44;    vus[1045]=-2.34;    stddevNs[1045]=0.24;    stddevEs[1045]=0.18;    stddevUs[1045]=0.82;    corrNEs[1045]=-0.004; 
   ids[1046]='P506';    sts[1046]='Ramer_LakeCS2005';    lats[1046]=33.0814313917;    lngs[1046]=244.4898029335;    ves[1046]=-7.21;    vns[1046]=3.91;    vus[1046]=-3.31;    stddevNs[1046]=0.17;    stddevEs[1046]=0.17;    stddevUs[1046]=0.9;    corrNEs[1046]=-0.007; 
   ids[1047]='P506';    sts[1047]='Ramer_LakeCS2005';    lats[1047]=33.0814313879;    lngs[1047]=244.4898029351;    ves[1047]=-7.21;    vns[1047]=3.91;    vus[1047]=-3.31;    stddevNs[1047]=0.17;    stddevEs[1047]=0.17;    stddevUs[1047]=0.9;    corrNEs[1047]=-0.007; 
   ids[1048]='P506';    sts[1048]='Ramer_LakeCS2005';    lats[1048]=33.0814313818;    lngs[1048]=244.4898029175;    ves[1048]=-7.21;    vns[1048]=3.91;    vus[1048]=-3.31;    stddevNs[1048]=0.17;    stddevEs[1048]=0.17;    stddevUs[1048]=0.9;    corrNEs[1048]=-0.007; 
   ids[1049]='P506';    sts[1049]='Ramer_LakeCS2005';    lats[1049]=33.0814313646;    lngs[1049]=244.4898028754;    ves[1049]=-7.21;    vns[1049]=3.91;    vus[1049]=-3.31;    stddevNs[1049]=0.17;    stddevEs[1049]=0.17;    stddevUs[1049]=0.9;    corrNEs[1049]=-0.007; 
   ids[1050]='P507';    sts[1050]='RedIslandMCS2005';    lats[1050]=33.1999770448;    lngs[1050]=244.3875956326;    ves[1050]=-10.52;    vns[1050]=-1.5;    vus[1050]=-15.96;    stddevNs[1050]=0.2;    stddevEs[1050]=0.16;    stddevUs[1050]=0.62;    corrNEs[1050]=-0.006; 
   ids[1051]='P508';    sts[1051]='Angus_PropCS2005';    lats[1051]=33.2477809353;    lngs[1051]=244.5712954701;    ves[1051]=-4.9;    vns[1051]=2.21;    vus[1051]=-2.08;    stddevNs[1051]=0.21;    stddevEs[1051]=0.15;    stddevUs[1051]=0.66;    corrNEs[1051]=-0.007; 
   ids[1052]='P509';    sts[1052]='PansyLaterCS2005';    lats[1052]=32.8906637305;    lngs[1052]=244.7060831261;    ves[1052]=-1.93;    vns[1052]=3.22;    vus[1052]=-4.86;    stddevNs[1052]=0.17;    stddevEs[1052]=0.16;    stddevUs[1052]=0.74;    corrNEs[1052]=-0.007; 
   ids[1053]='P510';    sts[1053]='ScheuPropsCS2005';    lats[1053]=33.1435749950;    lngs[1053]=244.6566682044;    ves[1053]=-4.3;    vns[1053]=2.28;    vus[1053]=-2.2;    stddevNs[1053]=0.19;    stddevEs[1053]=0.16;    stddevUs[1053]=0.63;    corrNEs[1053]=-0.008; 
   ids[1054]='P511';    sts[1054]='CoxcombMtnCS2005';    lats[1054]=33.8869353959;    lngs[1054]=244.7038996613;    ves[1054]=-4.34;    vns[1054]=0.8;    vus[1054]=-1.27;    stddevNs[1054]=0.21;    stddevEs[1054]=0.15;    stddevUs[1054]=0.74;    corrNEs[1054]=-0.007; 
   ids[1055]='P512';    sts[1055]='YosWawona_CN2008';    lats[1055]=37.5626353176;    lngs[1055]=240.3055533625;    ves[1055]=-10.98;    vns[1055]=9.08;    vus[1055]=-2.04;    stddevNs[1055]=3.72;    stddevEs[1055]=1.5;    stddevUs[1055]=3.78;    corrNEs[1055]=0.001; 
   ids[1056]='P513';    sts[1056]='Point_Sal_CS2007';    lats[1056]=34.9072627110;    lngs[1056]=239.3498278147;    ves[1056]=-31.05;    vns[1056]=34.3;    vus[1056]=-1.77;    stddevNs[1056]=0.62;    stddevEs[1056]=0.52;    stddevUs[1056]=0.69;    corrNEs[1056]=-0.002; 
   ids[1057]='P514';    sts[1057]='Nipomo____CS2006';    lats[1057]=35.0107146573;    lngs[1057]=239.5902511203;    ves[1057]=-29.89;    vns[1057]=32.73;    vus[1057]=0.61;    stddevNs[1057]=0.47;    stddevEs[1057]=0.63;    stddevUs[1057]=0.93;    corrNEs[1057]=-0.001; 
   ids[1058]='P515';    sts[1058]='Tepusquet_CS2006';    lats[1058]=34.8705542682;    lngs[1058]=239.7601494980;    ves[1058]=-30.97;    vns[1058]=31.58;    vus[1058]=-0.36;    stddevNs[1058]=0.18;    stddevEs[1058]=0.17;    stddevUs[1058]=0.64;    corrNEs[1058]=-0.012; 
   ids[1059]='P516';    sts[1059]='JimJacksonCS2006';    lats[1059]=35.1062009728;    lngs[1059]=239.6166085497;    ves[1059]=-28.94;    vns[1059]=32.13;    vus[1059]=-0.59;    stddevNs[1059]=1.08;    stddevEs[1059]=0.63;    stddevUs[1059]=0.85;    corrNEs[1059]=-0.000; 
   ids[1060]='P516';    sts[1060]='JimJacksonCS2006';    lats[1060]=35.1062009686;    lngs[1060]=239.6166085465;    ves[1060]=-28.94;    vns[1060]=32.13;    vus[1060]=-0.59;    stddevNs[1060]=1.08;    stddevEs[1060]=0.63;    stddevUs[1060]=0.85;    corrNEs[1060]=-0.000; 
   ids[1061]='P517';    sts[1061]='Mt_GleasonCS2008';    lats[1061]=34.3763658248;    lngs[1061]=241.8224281410;    ves[1061]=-23.89;    vns[1061]=21.03;    vus[1061]=-1.26;    stddevNs[1061]=0.39;    stddevEs[1061]=0.42;    stddevUs[1061]=1.23;    corrNEs[1061]=0.000; 
   ids[1062]='P518';    sts[1062]='TrepletMtnCS2008';    lats[1062]=35.0200334088;    lngs[1062]=239.9246935730;    ves[1062]=-27.75;    vns[1062]=31.24;    vus[1062]=-2.22;    stddevNs[1062]=0.93;    stddevEs[1062]=0.37;    stddevUs[1062]=1.16;    corrNEs[1062]=-0.001; 
   ids[1063]='P519';    sts[1063]='PaintedCavCS2007';    lats[1063]=34.5078114323;    lngs[1063]=240.2075630836;    ves[1063]=-30.75;    vns[1063]=29.07;    vus[1063]=-0.86;    stddevNs[1063]=0.22;    stddevEs[1063]=0.29;    stddevUs[1063]=0.7;    corrNEs[1063]=-0.006; 
   ids[1064]='P520';    sts[1064]='LomaPelonaCS2008';    lats[1064]=34.6304838524;    lngs[1064]=240.3835948111;    ves[1064]=-31.21;    vns[1064]=26.69;    vus[1064]=-1.04;    stddevNs[1064]=0.34;    stddevEs[1064]=0.3;    stddevUs[1064]=1.3;    corrNEs[1064]=-0.004; 
   ids[1065]='P521';    sts[1065]='McPherson_CS2008';    lats[1065]=34.8885674798;    lngs[1065]=240.1846449894;    ves[1065]=-29.19;    vns[1065]=27.41;    vus[1065]=-3.2;    stddevNs[1065]=0.32;    stddevEs[1065]=0.5;    stddevUs[1065]=1.26;    corrNEs[1065]=-0.003; 
   ids[1066]='P522';    sts[1066]='Taft_MountCS2008';    lats[1066]=35.0866116127;    lngs[1066]=240.4639380354;    ves[1066]=-20.9;    vns[1066]=19.37;    vus[1066]=0.16;    stddevNs[1066]=0.74;    stddevEs[1066]=0.54;    stddevUs[1066]=2.36;    corrNEs[1066]=-0.000; 
   ids[1067]='P523';    sts[1067]='LosOsos___CS2006';    lats[1067]=35.3044497585;    lngs[1067]=239.1397275386;    ves[1067]=-30.15;    vns[1067]=35.2;    vus[1067]=-0.65;    stddevNs[1067]=0.37;    stddevEs[1067]=0.35;    stddevUs[1067]=0.76;    corrNEs[1067]=-0.003; 
   ids[1068]='P523';    sts[1068]='LosOsos___CS2006';    lats[1068]=35.3044497562;    lngs[1068]=239.1397275355;    ves[1068]=-30.15;    vns[1068]=35.2;    vus[1068]=-0.65;    stddevNs[1068]=0.37;    stddevEs[1068]=0.35;    stddevUs[1068]=0.76;    corrNEs[1068]=-0.003; 
   ids[1069]='P524';    sts[1069]='squirecnynCS2007';    lats[1069]=35.1660857215;    lngs[1069]=239.4091037748;    ves[1069]=-29.53;    vns[1069]=33.93;    vus[1069]=-1.36;    stddevNs[1069]=0.45;    stddevEs[1069]=0.5;    stddevUs[1069]=0.88;    corrNEs[1069]=-0.001; 
   ids[1070]='P525';    sts[1070]='MorroCreekCS2006';    lats[1070]=35.4257674676;    lngs[1070]=239.1918590241;    ves[1070]=-29.18;    vns[1070]=35.06;    vus[1070]=-1.35;    stddevNs[1070]=0.38;    stddevEs[1070]=0.84;    stddevUs[1070]=0.9;    corrNEs[1070]=-0.001; 
   ids[1071]='P526';    sts[1071]='RamageRnchCS2004';    lats[1071]=35.6359747773;    lngs[1071]=239.1302632203;    ves[1071]=-27.18;    vns[1071]=33.82;    vus[1071]=-4.51;    stddevNs[1071]=0.28;    stddevEs[1071]=0.4;    stddevUs[1071]=1.3;    corrNEs[1071]=-0.003; 
   ids[1072]='P526';    sts[1072]='RamageRnchCS2004';    lats[1072]=35.6359747854;    lngs[1072]=239.1302631212;    ves[1072]=-27.18;    vns[1072]=33.82;    vus[1072]=-4.51;    stddevNs[1072]=0.28;    stddevEs[1072]=0.4;    stddevUs[1072]=1.3;    corrNEs[1072]=-0.003; 
   ids[1073]='P527';    sts[1073]='RanchitaCnCS2006';    lats[1073]=35.7541441620;    lngs[1073]=239.3952461246;    ves[1073]=-27.28;    vns[1073]=31.27;    vus[1073]=-2.17;    stddevNs[1073]=0.55;    stddevEs[1073]=0.66;    stddevUs[1073]=0.83;    corrNEs[1073]=-0.001; 
   ids[1074]='P528';    sts[1074]='RinconadaRCS2006';    lats[1074]=35.3278112350;    lngs[1074]=239.4545519423;    ves[1074]=-27.9;    vns[1074]=33.29;    vus[1074]=0.04;    stddevNs[1074]=0.53;    stddevEs[1074]=0.65;    stddevUs[1074]=0.88;    corrNEs[1074]=-0.001; 
   ids[1075]='P529';    sts[1075]='ShellCreekCS2006';    lats[1075]=35.4404515792;    lngs[1075]=239.6461723388;    ves[1075]=-27.01;    vns[1075]=30.21;    vus[1075]=-0.06;    stddevNs[1075]=1.35;    stddevEs[1075]=0.72;    stddevUs[1075]=0.98;    corrNEs[1075]=-0.001; 
   ids[1076]='P529';    sts[1076]='ShellCreekCS2006';    lats[1076]=35.4404515554;    lngs[1076]=239.6461723205;    ves[1076]=-27.01;    vns[1076]=30.21;    vus[1076]=-0.06;    stddevNs[1076]=1.35;    stddevEs[1076]=0.72;    stddevUs[1076]=0.98;    corrNEs[1076]=-0.001; 
   ids[1077]='P530';    sts[1077]='HillmRanchCS2005';    lats[1077]=35.6248010970;    lngs[1077]=239.5195667126;    ves[1077]=-25.92;    vns[1077]=32.21;    vus[1077]=-3.7;    stddevNs[1077]=0.48;    stddevEs[1077]=0.46;    stddevUs[1077]=1.08;    corrNEs[1077]=-0.002; 
   ids[1078]='P531';    sts[1078]='Hog_CanyonCS2007';    lats[1078]=35.7926902565;    lngs[1078]=239.4634002552;    ves[1078]=-26.55;    vns[1078]=31.71;    vus[1078]=-0.24;    stddevNs[1078]=0.45;    stddevEs[1078]=0.47;    stddevUs[1078]=0.76;    corrNEs[1078]=-0.002; 
   ids[1079]='P532';    sts[1079]='WicksRanchCS2004';    lats[1079]=35.6338117268;    lngs[1079]=239.7329737582;    ves[1079]=-23.05;    vns[1079]=27.51;    vus[1079]=-0.29;    stddevNs[1079]=0.34;    stddevEs[1079]=0.23;    stddevUs[1079]=0.86;    corrNEs[1079]=-0.004; 
   ids[1080]='P533';    sts[1080]='SpringRancCS2006';    lats[1080]=35.7479301984;    lngs[1080]=239.6290470135;    ves[1080]=-23.19;    vns[1080]=28.97;    vus[1080]=-0.39;    stddevNs[1080]=0.71;    stddevEs[1080]=0.4;    stddevUs[1080]=1.18;    corrNEs[1080]=-0.001; 
   ids[1081]='P534';    sts[1081]='CookePeak_CN2007';    lats[1081]=37.0612314654;    lngs[1081]=237.7623898181;    ves[1081]=-28.47;    vns[1081]=34.86;    vus[1081]=-2.41;    stddevNs[1081]=0.27;    stddevEs[1081]=0.28;    stddevUs[1081]=0.99;    corrNEs[1081]=-0.005; 
   ids[1082]='P535';    sts[1082]='BuckhornRaCS2006';    lats[1082]=35.2351274441;    lngs[1082]=239.8985787920;    ves[1082]=-26.73;    vns[1082]=28.6;    vus[1082]=0.7;    stddevNs[1082]=0.49;    stddevEs[1082]=0.32;    stddevUs[1082]=0.89;    corrNEs[1082]=-0.002; 
   ids[1083]='P536';    sts[1083]='CarrizoRanCS2006';    lats[1083]=35.2797678576;    lngs[1083]=239.9749802227;    ves[1083]=-25.13;    vns[1083]=26.62;    vus[1083]=0.72;    stddevNs[1083]=0.33;    stddevEs[1083]=0.34;    stddevUs[1083]=0.89;    corrNEs[1083]=-0.003; 
   ids[1084]='P537';    sts[1084]='CaliValleyCS2006';    lats[1084]=35.3167961165;    lngs[1084]=240.0646597760;    ves[1084]=-22.91;    vns[1084]=24;    vus[1084]=1.17;    stddevNs[1084]=0.41;    stddevEs[1084]=0.37;    stddevUs[1084]=0.95;    corrNEs[1084]=-0.002; 
   ids[1085]='P538';    sts[1085]='BitterWatrCS2006';    lats[1085]=35.5341737073;    lngs[1085]=239.8875008124;    ves[1085]=-22.29;    vns[1085]=23.51;    vus[1085]=1.73;    stddevNs[1085]=0.72;    stddevEs[1085]=0.34;    stddevUs[1085]=1.16;    corrNEs[1085]=-0.001; 
   ids[1086]='P539';    sts[1086]='VogelRanchCS2004';    lats[1086]=35.7026737840;    lngs[1086]=239.8179499764;    ves[1086]=-15.95;    vns[1086]=18.93;    vus[1086]=0.32;    stddevNs[1086]=0.32;    stddevEs[1086]=0.36;    stddevUs[1086]=0.84;    corrNEs[1086]=-0.003; 
   ids[1087]='P540';    sts[1087]='AvenalRidgCS2006';    lats[1087]=35.8012795164;    lngs[1087]=239.8693641166;    ves[1087]=-13.44;    vns[1087]=14.85;    vus[1087]=-0.77;    stddevNs[1087]=0.58;    stddevEs[1087]=0.49;    stddevUs[1087]=1.1;    corrNEs[1087]=-0.001; 
   ids[1088]='P541';    sts[1088]='BlkwllFarmCS2005';    lats[1088]=35.6867317911;    lngs[1088]=239.9993221357;    ves[1088]=-13.86;    vns[1088]=15.06;    vus[1088]=1.24;    stddevNs[1088]=0.44;    stddevEs[1088]=0.26;    stddevUs[1088]=1.02;    corrNEs[1088]=-0.003; 
   ids[1089]='P542';    sts[1089]='BruceSpringCS200';    lats[1089]=35.6889101809;    lngs[1089]=239.7074005746;    ves[1089]=-22.99;    vns[1089]=27.13;    vus[1089]=1.67;    stddevNs[1089]=0.32;    stddevEs[1089]=0.36;    stddevUs[1089]=0.98;    corrNEs[1089]=-0.003; 
   ids[1090]='P543';    sts[1090]='Reward____CS2006';    lats[1090]=35.3189925516;    lngs[1090]=240.2867791303;    ves[1090]=-17.18;    vns[1090]=17.8;    vus[1090]=1.12;    stddevNs[1090]=0.81;    stddevEs[1090]=0.81;    stddevUs[1090]=0.71;    corrNEs[1090]=-0.001; 
   ids[1091]='P544';    sts[1091]='TwisselmanCS2005';    lats[1091]=35.7312680082;    lngs[1091]=240.2619657744;    ves[1091]=-10.73;    vns[1091]=11.03;    vus[1091]=-4.06;    stddevNs[1091]=0.49;    stddevEs[1091]=1.25;    stddevUs[1091]=1.24;    corrNEs[1091]=-0.001; 
   ids[1092]='P545';    sts[1092]='LerdoOvrpsCS2007';    lats[1092]=35.4998422879;    lngs[1092]=240.4641930282;    ves[1092]=-12.67;    vns[1092]=13.09;    vus[1092]=-13.25;    stddevNs[1092]=0.7;    stddevEs[1092]=0.67;    stddevUs[1092]=1.01;    corrNEs[1092]=-0.001; 
   ids[1093]='P546';    sts[1093]='FlattopPk_CS2006';    lats[1093]=35.9279040952;    lngs[1093]=239.8451159868;    ves[1093]=-10.47;    vns[1093]=14.13;    vus[1093]=0.93;    stddevNs[1093]=0.48;    stddevEs[1093]=0.53;    stddevUs[1093]=1.03;    corrNEs[1093]=-0.001; 
   ids[1094]='P547';    sts[1094]='UticaCaltnCS2005';    lats[1094]=35.9347037848;    lngs[1094]=240.0906396246;    ves[1094]=-10.29;    vns[1094]=13.27;    vus[1094]=1.46;    stddevNs[1094]=0.42;    stddevEs[1094]=0.38;    stddevUs[1094]=1.04;    corrNEs[1094]=-0.002; 
   ids[1095]='P547';    sts[1095]='UticaCaltnCS2005';    lats[1095]=35.9347037817;    lngs[1095]=240.0906396244;    ves[1095]=-10.29;    vns[1095]=13.27;    vus[1095]=1.46;    stddevNs[1095]=0.42;    stddevEs[1095]=0.38;    stddevUs[1095]=1.04;    corrNEs[1095]=-0.002; 
   ids[1096]='P548';    sts[1096]='Noon_Peak_CS2008';    lats[1096]=34.4668065061;    lngs[1096]=240.4960559224;    ves[1096]=-30.91;    vns[1096]=26.09;    vus[1096]=-2.52;    stddevNs[1096]=0.34;    stddevEs[1096]=0.27;    stddevUs[1096]=1.19;    corrNEs[1096]=-0.004; 
   ids[1097]='P549';    sts[1097]='SespeCreekCS2006';    lats[1097]=34.5996001903;    lngs[1097]=240.6736614718;    ves[1097]=-28.86;    vns[1097]=23.59;    vus[1097]=0.83;    stddevNs[1097]=0.2;    stddevEs[1097]=0.17;    stddevUs[1097]=0.68;    corrNEs[1097]=-0.008; 
   ids[1098]='P550';    sts[1098]='GradeVallyCS2007';    lats[1098]=34.6600833675;    lngs[1098]=240.8853806451;    ves[1098]=-26.39;    vns[1098]=21.26;    vus[1098]=0.25;    stddevNs[1098]=0.21;    stddevEs[1098]=0.21;    stddevUs[1098]=0.77;    corrNEs[1098]=-0.007; 
   ids[1099]='P551';    sts[1099]='PineMountnCS2006';    lats[1099]=34.8561990240;    lngs[1099]=240.8454301581;    ves[1099]=-22.9;    vns[1099]=19.14;    vus[1099]=1.98;    stddevNs[1099]=0.2;    stddevEs[1099]=0.2;    stddevUs[1099]=0.77;    corrNEs[1099]=-0.010; 
   ids[1100]='P552';    sts[1100]='Long_Lake_CS2008';    lats[1100]=35.6867255646;    lngs[1100]=239.7550398701;    ves[1100]=-22.14;    vns[1100]=26.05;    vus[1100]=1.9;    stddevNs[1100]=0.34;    stddevEs[1100]=0.53;    stddevUs[1100]=1;    corrNEs[1100]=-0.002; 
   ids[1101]='P553';    sts[1101]='Grapevine_CS2005';    lats[1101]=34.8350942886;    lngs[1101]=241.1210325123;    ves[1101]=-19.9;    vns[1101]=17.46;    vus[1101]=1.51;    stddevNs[1101]=0.16;    stddevEs[1101]=0.15;    stddevUs[1101]=0.51;    corrNEs[1101]=-0.012; 
   ids[1102]='P554';    sts[1102]='GormanCrk_CS2006';    lats[1102]=34.7922971430;    lngs[1102]=241.1519721062;    ves[1102]=-21.92;    vns[1102]=18.15;    vus[1102]=-0.25;    stddevNs[1102]=0.19;    stddevEs[1102]=0.19;    stddevUs[1102]=0.59;    corrNEs[1102]=-0.008; 
   ids[1103]='P555';    sts[1103]='CastaicCrkCS2008';    lats[1103]=34.6949905948;    lngs[1103]=241.3305773852;    ves[1103]=-20.71;    vns[1103]=19.27;    vus[1103]=-0.05;    stddevNs[1103]=0.44;    stddevEs[1103]=0.33;    stddevUs[1103]=1.17;    corrNEs[1103]=0.000; 
   ids[1104]='P556';    sts[1104]='BarnardProCS2005';    lats[1104]=34.7711087074;    lngs[1104]=241.4545470879;    ves[1104]=-16.15;    vns[1104]=16.7;    vus[1104]=0.14;    stddevNs[1104]=0.27;    stddevEs[1104]=0.31;    stddevUs[1104]=0.67;    corrNEs[1104]=-0.003; 
   ids[1105]='P557';    sts[1105]='EaglePerchCS2005';    lats[1105]=34.9443851199;    lngs[1105]=241.3444134967;    ves[1105]=-15.65;    vns[1105]=14.48;    vus[1105]=0.45;    stddevNs[1105]=0.17;    stddevEs[1105]=0.18;    stddevUs[1105]=0.66;    corrNEs[1105]=-0.010; 
   ids[1106]='P558';    sts[1106]='RavensRidgCS2006';    lats[1106]=35.1386056452;    lngs[1106]=241.3883457136;    ves[1106]=-13.58;    vns[1106]=12.5;    vus[1106]=0.69;    stddevNs[1106]=0.2;    stddevEs[1106]=0.16;    stddevUs[1106]=0.56;    corrNEs[1106]=-0.009; 
   ids[1107]='P559';    sts[1107]='Antelope__CS2005';    lats[1107]=34.8389099469;    lngs[1107]=241.3824023073;    ves[1107]=-15.7;    vns[1107]=16.43;    vus[1107]=-0.17;    stddevNs[1107]=0.16;    stddevEs[1107]=0.15;    stddevUs[1107]=0.51;    corrNEs[1107]=-0.012; 
   ids[1108]='P560';    sts[1108]='StokesPropCS2005';    lats[1108]=34.8218111216;    lngs[1108]=241.4591335704;    ves[1108]=-15.64;    vns[1108]=16.7;    vus[1108]=-2.11;    stddevNs[1108]=0.17;    stddevEs[1108]=0.17;    stddevUs[1108]=1.05;    corrNEs[1108]=-0.010; 
   ids[1109]='P561';    sts[1109]='GreenValleCS2005';    lats[1109]=34.6184595240;    lngs[1109]=241.6009452218;    ves[1109]=-18.28;    vns[1109]=21.03;    vus[1109]=-3.59;    stddevNs[1109]=0.19;    stddevEs[1109]=0.23;    stddevUs[1109]=0.81;    corrNEs[1109]=0.016; 
   ids[1110]='P562';    sts[1110]='SoledadMtnCS2004';    lats[1110]=34.9821332704;    lngs[1110]=241.8112558957;    ves[1110]=-11.38;    vns[1110]=14.13;    vus[1110]=-1.8;    stddevNs[1110]=0.15;    stddevEs[1110]=0.14;    stddevUs[1110]=0.48;    corrNEs[1110]=-0.012; 
   ids[1111]='P562';    sts[1111]='SoledadMtnCS2004';    lats[1111]=34.9821332747;    lngs[1111]=241.8112558971;    ves[1111]=-11.38;    vns[1111]=14.13;    vus[1111]=-1.8;    stddevNs[1111]=0.15;    stddevEs[1111]=0.14;    stddevUs[1111]=0.48;    corrNEs[1111]=-0.012; 
   ids[1112]='P562';    sts[1112]='SoledadMtnCS2004';    lats[1112]=34.9821332726;    lngs[1112]=241.8112558953;    ves[1112]=-11.38;    vns[1112]=14.13;    vus[1112]=-1.8;    stddevNs[1112]=0.15;    stddevEs[1112]=0.14;    stddevUs[1112]=0.48;    corrNEs[1112]=-0.012; 
   ids[1113]='P563';    sts[1113]='ButtonwillCS2005';    lats[1113]=35.4186695189;    lngs[1113]=240.5788346428;    ves[1113]=-12.17;    vns[1113]=12.89;    vus[1113]=-8.58;    stddevNs[1113]=1.22;    stddevEs[1113]=0.49;    stddevUs[1113]=1.4;    corrNEs[1113]=-0.001; 
   ids[1114]='P564';    sts[1114]='Posocreek_CS2006';    lats[1114]=35.6229115090;    lngs[1114]=240.6506102471;    ves[1114]=-14.63;    vns[1114]=16.05;    vus[1114]=-46.41;    stddevNs[1114]=3.86;    stddevEs[1114]=2.95;    stddevUs[1114]=2.94;    corrNEs[1114]=-0.000; 
   ids[1115]='P565';    sts[1115]='Delano_AirCS2005';    lats[1115]=35.7438938680;    lngs[1115]=240.7633475757;    ves[1115]=-15.61;    vns[1115]=10.17;    vus[1115]=-16.58;    stddevNs[1115]=0.28;    stddevEs[1115]=0.61;    stddevUs[1115]=1.67;    corrNEs[1115]=-0.002; 
   ids[1116]='P566';    sts[1116]='Visalia_MYCS2005';    lats[1116]=36.3244547884;    lngs[1116]=240.7707134187;    ves[1116]=-12.04;    vns[1116]=8.79;    vus[1116]=-12.07;    stddevNs[1116]=0.24;    stddevEs[1116]=0.16;    stddevUs[1116]=0.58;    corrNEs[1116]=-0.008; 
   ids[1117]='P567';    sts[1117]='RioBravo__CS2005';    lats[1117]=35.4209466606;    lngs[1117]=241.2464236048;    ves[1117]=-12.87;    vns[1117]=10.49;    vus[1117]=1.22;    stddevNs[1117]=0.19;    stddevEs[1117]=0.15;    stddevUs[1117]=0.61;    corrNEs[1117]=-0.010; 
   ids[1118]='P568';    sts[1118]='AntimonyFlCS2007';    lats[1118]=35.2543073129;    lngs[1118]=241.8734878217;    ves[1118]=-11.07;    vns[1118]=11.88;    vus[1118]=-1.3;    stddevNs[1118]=0.19;    stddevEs[1118]=0.48;    stddevUs[1118]=0.77;    corrNEs[1118]=-0.003; 
   ids[1119]='P569';    sts[1119]='ButterbredCS2007';    lats[1119]=35.3779687378;    lngs[1119]=241.8762333547;    ves[1119]=-10.99;    vns[1119]=10.91;    vus[1119]=-0.65;    stddevNs[1119]=0.2;    stddevEs[1119]=0.21;    stddevUs[1119]=0.72;    corrNEs[1119]=-0.007; 
   ids[1120]='P570';    sts[1120]='KelsoCreekCS2006';    lats[1120]=35.6673483433;    lngs[1120]=241.7399620018;    ves[1120]=-10.78;    vns[1120]=10.2;    vus[1120]=0.18;    stddevNs[1120]=0.2;    stddevEs[1120]=0.17;    stddevUs[1120]=0.61;    corrNEs[1120]=-0.008; 
   ids[1121]='P571';    sts[1121]='SpringvillCS2005';    lats[1121]=36.2313659102;    lngs[1121]=241.2332849568;    ves[1121]=-10.65;    vns[1121]=9.36;    vus[1121]=1.17;    stddevNs[1121]=0.18;    stddevEs[1121]=0.16;    stddevUs[1121]=0.56;    corrNEs[1121]=-0.010; 
   ids[1122]='P572';    sts[1122]='ShadequartCS2006';    lats[1122]=36.5855161375;    lngs[1122]=241.0454044731;    ves[1122]=-10.48;    vns[1122]=9.23;    vus[1122]=1.08;    stddevNs[1122]=0.17;    stddevEs[1122]=0.18;    stddevUs[1122]=0.64;    corrNEs[1122]=-0.009; 
   ids[1123]='P573';    sts[1123]='Blackrock_CS2008';    lats[1123]=36.0930945525;    lngs[1123]=241.7395019280;    ves[1123]=-10.08;    vns[1123]=9.41;    vus[1123]=1.02;    stddevNs[1123]=0.51;    stddevEs[1123]=0.7;    stddevUs[1123]=2.61;    corrNEs[1123]=0.001; 
   ids[1124]='P574';    sts[1124]='Mt_HarwoodCS2008';    lats[1124]=34.2867695819;    lngs[1124]=242.3661150953;    ves[1124]=-23;    vns[1124]=19.36;    vus[1124]=-2.99;    stddevNs[1124]=0.82;    stddevEs[1124]=1.09;    stddevUs[1124]=1.98;    corrNEs[1124]=-0.001; 
   ids[1125]='P574';    sts[1125]='Mt_HarwoodCS2008';    lats[1125]=34.2867695762;    lngs[1125]=242.3661150900;    ves[1125]=-23;    vns[1125]=19.36;    vus[1125]=-2.99;    stddevNs[1125]=0.82;    stddevEs[1125]=1.09;    stddevUs[1125]=1.98;    corrNEs[1125]=-0.001; 
   ids[1126]='P575';    sts[1126]='Cucamonga_CS2008';    lats[1126]=34.2155126187;    lngs[1126]=242.4578394830;    ves[1126]=-22.37;    vns[1126]=20.11;    vus[1126]=-1.96;    stddevNs[1126]=0.98;    stddevEs[1126]=0.66;    stddevUs[1126]=1.93;    corrNEs[1126]=0.000; 
   ids[1127]='P576';    sts[1127]='GallegosRnCS2004';    lats[1127]=35.6696620596;    lngs[1127]=239.0300004732;    ves[1127]=-28.27;    vns[1127]=35.68;    vus[1127]=-1.31;    stddevNs[1127]=0.74;    stddevEs[1127]=0.73;    stddevUs[1127]=1.15;    corrNEs[1127]=-0.000; 
   ids[1128]='P576';    sts[1128]='GallegosRnCS2004';    lats[1128]=35.6696620181;    lngs[1128]=239.0300003620;    ves[1128]=-28.27;    vns[1128]=35.68;    vus[1128]=-1.31;    stddevNs[1128]=0.74;    stddevEs[1128]=0.73;    stddevUs[1128]=1.15;    corrNEs[1128]=-0.000; 
   ids[1129]='P577';    sts[1129]='CdrSpgDam_CS2004';    lats[1129]=34.3046052842;    lngs[1129]=242.6810867700;    ves[1129]=-16.44;    vns[1129]=15.85;    vus[1129]=0.76;    stddevNs[1129]=0.16;    stddevEs[1129]=0.15;    stddevUs[1129]=0.59;    corrNEs[1129]=-0.011; 
   ids[1130]='P578';    sts[1130]='DavisBenchCS2008';    lats[1130]=35.6943311188;    lngs[1130]=239.7609696531;    ves[1130]=-21.96;    vns[1130]=25.17;    vus[1130]=0.06;    stddevNs[1130]=0.88;    stddevEs[1130]=0.72;    stddevUs[1130]=1.18;    corrNEs[1130]=-0.000; 
   ids[1131]='P579';    sts[1131]='FremontValCS2006';    lats[1131]=35.0387625376;    lngs[1131]=241.9942354030;    ves[1131]=-10.31;    vns[1131]=13.56;    vus[1131]=-1.25;    stddevNs[1131]=0.17;    stddevEs[1131]=0.16;    stddevUs[1131]=0.49;    corrNEs[1131]=-0.010; 
   ids[1132]='P580';    sts[1132]='Straw_PeakCS2007';    lats[1132]=35.6209476383;    lngs[1132]=242.8077730601;    ves[1132]=-7.58;    vns[1132]=7.35;    vus[1132]=-1.56;    stddevNs[1132]=0.19;    stddevEs[1132]=0.18;    stddevUs[1132]=0.77;    corrNEs[1132]=-0.008; 
   ids[1133]='P581';    sts[1133]='WoodsPropyCS2005';    lats[1133]=34.5097390838;    lngs[1133]=242.2709988372;    ves[1133]=-15.68;    vns[1133]=16.35;    vus[1133]=-0.84;    stddevNs[1133]=0.18;    stddevEs[1133]=0.18;    stddevUs[1133]=0.62;    corrNEs[1133]=-0.008; 
   ids[1134]='P582';    sts[1134]='El_Mirage_CS2008';    lats[1134]=34.6342587203;    lngs[1134]=242.4511406156;    ves[1134]=-13.52;    vns[1134]=14.23;    vus[1134]=-1.37;    stddevNs[1134]=0.28;    stddevEs[1134]=0.26;    stddevUs[1134]=0.92;    corrNEs[1134]=-0.003; 
   ids[1135]='P583';    sts[1135]='KramerJnctCS2005';    lats[1135]=34.9870039525;    lngs[1135]=242.4566075632;    ves[1135]=-9.83;    vns[1135]=12.5;    vus[1135]=-1.85;    stddevNs[1135]=0.16;    stddevEs[1135]=0.18;    stddevUs[1135]=0.62;    corrNEs[1135]=-0.009; 
   ids[1136]='P584';    sts[1136]='PotreroCrkCS2004';    lats[1136]=33.8926031111;    lngs[1136]=243.0483592200;    ves[1136]=-17.83;    vns[1136]=19.28;    vus[1136]=0.51;    stddevNs[1136]=0.16;    stddevEs[1136]=0.15;    stddevUs[1136]=0.48;    corrNEs[1136]=-0.012; 
   ids[1137]='P584';    sts[1137]='PotreroCrkCS2004';    lats[1137]=33.8926031146;    lngs[1137]=243.0483592112;    ves[1137]=-17.83;    vns[1137]=19.28;    vus[1137]=0.51;    stddevNs[1137]=0.16;    stddevEs[1137]=0.15;    stddevUs[1137]=0.48;    corrNEs[1137]=-0.012; 
   ids[1138]='P585';    sts[1138]='BigMorongoCS2007';    lats[1138]=34.0193480222;    lngs[1138]=243.4542540122;    ves[1138]=-11.19;    vns[1138]=14.71;    vus[1138]=0.32;    stddevNs[1138]=0.25;    stddevEs[1138]=0.31;    stddevUs[1138]=1.04;    corrNEs[1138]=-0.003; 
   ids[1139]='P586';    sts[1139]='LewisCentrCS2005';    lats[1139]=34.5348436626;    lngs[1139]=242.7194794503;    ves[1139]=-13.5;    vns[1139]=13.62;    vus[1139]=-1.07;    stddevNs[1139]=0.17;    stddevEs[1139]=0.15;    stddevUs[1139]=0.65;    corrNEs[1139]=-0.013; 
   ids[1140]='P587';    sts[1140]='ChileoFlatCS2008';    lats[1140]=34.3322334135;    lngs[1140]=241.9686631694;    ves[1140]=-23.89;    vns[1140]=20.71;    vus[1140]=-0.23;    stddevNs[1140]=0.28;    stddevEs[1140]=0.3;    stddevUs[1140]=1;    corrNEs[1140]=-0.003; 
   ids[1141]='P588';    sts[1141]='DeVriesRchCS2005';    lats[1141]=34.7854376125;    lngs[1141]=242.7324009047;    ves[1141]=-11.04;    vns[1141]=12.23;    vus[1141]=-1.8;    stddevNs[1141]=0.18;    stddevEs[1141]=0.33;    stddevUs[1141]=0.71;    corrNEs[1141]=-0.005; 
   ids[1142]='P588';    sts[1142]='DeVriesRchCS2005';    lats[1142]=34.7854376112;    lngs[1142]=242.7324009063;    ves[1142]=-11.04;    vns[1142]=12.23;    vus[1142]=-1.8;    stddevNs[1142]=0.18;    stddevEs[1142]=0.33;    stddevUs[1142]=0.71;    corrNEs[1142]=-0.005; 
   ids[1143]='P589';    sts[1143]='BlackMtQryCS2005';    lats[1143]=34.6206672884;    lngs[1143]=242.8898106065;    ves[1143]=-12.38;    vns[1143]=12.48;    vus[1143]=-2.71;    stddevNs[1143]=0.17;    stddevEs[1143]=0.16;    stddevUs[1143]=0.68;    corrNEs[1143]=-0.010; 
   ids[1144]='P590';    sts[1144]='McdnldWellCS2007';    lats[1144]=35.1167772914;    lngs[1144]=242.6352500265;    ves[1144]=-8.82;    vns[1144]=10.97;    vus[1144]=-2.34;    stddevNs[1144]=0.24;    stddevEs[1144]=0.25;    stddevUs[1144]=1.07;    corrNEs[1144]=-0.003; 
   ids[1145]='P591';    sts[1145]='CalCityAptCS2005';    lats[1145]=35.1524249101;    lngs[1145]=241.9835256217;    ves[1145]=-9.96;    vns[1145]=12.98;    vus[1145]=-1.48;    stddevNs[1145]=0.18;    stddevEs[1145]=0.15;    stddevUs[1145]=0.56;    corrNEs[1145]=-0.011; 
   ids[1146]='P592';    sts[1146]='GrassVallyCS2007';    lats[1146]=35.2385517321;    lngs[1146]=242.6967710328;    ves[1146]=-8.12;    vns[1146]=9.97;    vus[1146]=-2.28;    stddevNs[1146]=0.2;    stddevEs[1146]=0.2;    stddevUs[1146]=0.82;    corrNEs[1146]=-0.007; 
   ids[1147]='P593';    sts[1147]='pyramid_ptCS2007';    lats[1147]=35.3878700066;    lngs[1147]=242.7949407987;    ves[1147]=-7.15;    vns[1147]=8.65;    vus[1147]=-1.83;    stddevNs[1147]=0.18;    stddevEs[1147]=0.18;    stddevUs[1147]=0.61;    corrNEs[1147]=-0.008; 
   ids[1148]='P594';    sts[1148]='CrowCanyonCS2005';    lats[1148]=35.8967113765;    lngs[1148]=242.6098673565;    ves[1148]=-8.1;    vns[1148]=6.63;    vus[1148]=-2.38;    stddevNs[1148]=0.17;    stddevEs[1148]=0.14;    stddevUs[1148]=0.69;    corrNEs[1148]=-0.011; 
   ids[1149]='P595';    sts[1149]='SearlesValCS2005';    lats[1149]=35.6975598798;    lngs[1149]=242.5971646690;    ves[1149]=-8.28;    vns[1149]=7.91;    vus[1149]=-1.79;    stddevNs[1149]=0.21;    stddevEs[1149]=0.15;    stddevUs[1149]=0.64;    corrNEs[1149]=-0.008; 
   ids[1150]='P596';    sts[1150]='QueenShebaCS2007';    lats[1150]=35.9981777044;    lngs[1150]=243.1104776701;    ves[1150]=-5.48;    vns[1150]=3.62;    vus[1150]=-2.27;    stddevNs[1150]=0.22;    stddevEs[1150]=0.23;    stddevUs[1150]=0.83;    corrNEs[1150]=-0.006; 
   ids[1151]='P597';    sts[1151]='OwlsHeadMtCS2007';    lats[1151]=35.7105997437;    lngs[1151]=243.1116035670;    ves[1151]=-6.15;    vns[1151]=5.02;    vus[1151]=-1.77;    stddevNs[1151]=0.2;    stddevEs[1151]=0.2;    stddevUs[1151]=0.78;    corrNEs[1151]=-0.007; 
   ids[1152]='P597';    sts[1152]='OwlsHeadMtCS2007';    lats[1152]=35.7105997508;    lngs[1152]=243.1116035550;    ves[1152]=-6.15;    vns[1152]=5.02;    vus[1152]=-1.77;    stddevNs[1152]=0.2;    stddevEs[1152]=0.2;    stddevUs[1152]=0.78;    corrNEs[1152]=-0.007; 
   ids[1153]='P598';    sts[1153]='Onyx_Peak_CS2007';    lats[1153]=34.1924619200;    lngs[1153]=243.2897331713;    ves[1153]=-11.8;    vns[1153]=14.61;    vus[1153]=0.32;    stddevNs[1153]=0.21;    stddevEs[1153]=0.24;    stddevUs[1153]=0.8;    corrNEs[1153]=-0.004; 
   ids[1154]='P599';    sts[1154]='GammaGulchCS2006';    lats[1154]=34.2171708725;    lngs[1154]=243.4625361193;    ves[1154]=-8.95;    vns[1154]=13.07;    vus[1154]=0.9;    stddevNs[1154]=0.21;    stddevEs[1154]=0.17;    stddevUs[1154]=0.72;    corrNEs[1154]=-0.007; 
   ids[1155]='P600';    sts[1155]='Pushwalla_CS2005';    lats[1155]=33.8658464176;    lngs[1155]=243.7881243809;    ves[1155]=-9.84;    vns[1155]=9.72;    vus[1155]=-0.54;    stddevNs[1155]=0.17;    stddevEs[1155]=0.16;    stddevUs[1155]=0.73;    corrNEs[1155]=-0.009; 
   ids[1156]='P601';    sts[1156]='GeologyRd_CS2006';    lats[1156]=33.9592969385;    lngs[1156]=243.9197723607;    ves[1156]=-7.04;    vns[1156]=6.17;    vus[1156]=-1.11;    stddevNs[1156]=0.22;    stddevEs[1156]=0.17;    stddevUs[1156]=0.61;    corrNEs[1156]=-0.006; 
   ids[1157]='P602';    sts[1157]='PolonlPassCS2008';    lats[1157]=35.7291648065;    lngs[1157]=239.7721075584;    ves[1157]=-17.92;    vns[1157]=19.73;    vus[1157]=3.15;    stddevNs[1157]=0.37;    stddevEs[1157]=0.45;    stddevUs[1157]=1.04;    corrNEs[1157]=-0.003; 
   ids[1158]='P603';    sts[1158]='I40CorridoCS2007';    lats[1158]=34.7169064681;    lngs[1158]=243.9704970500;    ves[1158]=-4.13;    vns[1158]=1.28;    vus[1158]=0.21;    stddevNs[1158]=0.23;    stddevEs[1158]=0.23;    stddevUs[1158]=0.9;    corrNEs[1158]=-0.002; 
   ids[1159]='P604';    sts[1159]='HarvardHilCS2008';    lats[1159]=34.9368281721;    lngs[1159]=243.3285466186;    ves[1159]=-7.77;    vns[1159]=5.73;    vus[1159]=-0.68;    stddevNs[1159]=0.35;    stddevEs[1159]=1.03;    stddevUs[1159]=1.32;    corrNEs[1159]=-0.001; 
   ids[1160]='P604';    sts[1160]='HarvardHilCS2008';    lats[1160]=34.9368281678;    lngs[1160]=243.3285466013;    ves[1160]=-7.77;    vns[1160]=5.73;    vus[1160]=-0.68;    stddevNs[1160]=0.35;    stddevEs[1160]=1.03;    stddevUs[1160]=1.32;    corrNEs[1160]=-0.001; 
   ids[1161]='P605';    sts[1161]='MtWatermanCS2008';    lats[1161]=34.3411275839;    lngs[1161]=242.0633347186;    ves[1161]=-22.79;    vns[1161]=20.13;    vus[1161]=0.29;    stddevNs[1161]=0.3;    stddevEs[1161]=0.31;    stddevUs[1161]=1.02;    corrNEs[1161]=-0.004; 
   ids[1162]='P606';    sts[1162]='LucerneValCS2005';    lats[1162]=34.4620642296;    lngs[1162]=243.1203763185;    ves[1162]=-12.26;    vns[1162]=11.98;    vus[1162]=-1.01;    stddevNs[1162]=0.21;    stddevEs[1162]=0.15;    stddevUs[1162]=0.67;    corrNEs[1162]=-0.007; 
   ids[1163]='P607';    sts[1163]='CottonSprgCS2006';    lats[1163]=33.7410400686;    lngs[1163]=244.1793476589;    ves[1163]=-6.86;    vns[1163]=3.57;    vus[1163]=-1.55;    stddevNs[1163]=0.23;    stddevEs[1163]=0.16;    stddevUs[1163]=0.5;    corrNEs[1163]=-0.006; 
   ids[1164]='P608';    sts[1164]='DuplexMineCS2008';    lats[1164]=33.9940839902;    lngs[1164]=244.3133971715;    ves[1164]=-4.73;    vns[1164]=1.96;    vus[1164]=-1.53;    stddevNs[1164]=0.3;    stddevEs[1164]=0.31;    stddevUs[1164]=1.17;    corrNEs[1164]=0.000; 
   ids[1165]='P609';    sts[1165]='YucaipaRidCS2006';    lats[1165]=34.0626866347;    lngs[1165]=243.1071827443;    ves[1165]=-12.95;    vns[1165]=16.72;    vus[1165]=-0.98;    stddevNs[1165]=0.47;    stddevEs[1165]=0.32;    stddevUs[1165]=0.94;    corrNEs[1165]=-0.004; 
   ids[1166]='P609';    sts[1166]='YucaipaRidCS2006';    lats[1166]=34.0626866259;    lngs[1166]=243.1071827354;    ves[1166]=-12.95;    vns[1166]=16.72;    vus[1166]=-0.98;    stddevNs[1166]=0.47;    stddevEs[1166]=0.32;    stddevUs[1166]=0.94;    corrNEs[1166]=-0.004; 
   ids[1167]='P610';    sts[1167]='AmericMineCS2006';    lats[1167]=34.4257474025;    lngs[1167]=244.2362754113;    ves[1167]=-3.41;    vns[1167]=1.21;    vus[1167]=-2.05;    stddevNs[1167]=0.19;    stddevEs[1167]=0.16;    stddevUs[1167]=0.84;    corrNEs[1167]=-0.006; 
   ids[1168]='P611';    sts[1168]='RainboWellCS2006';    lats[1168]=35.2047739969;    lngs[1168]=244.3501888881;    ves[1168]=-3.98;    vns[1168]=0.36;    vus[1168]=-0.72;    stddevNs[1168]=0.23;    stddevEs[1168]=0.38;    stddevUs[1168]=0.61;    corrNEs[1168]=-0.002; 
   ids[1169]='P611';    sts[1169]='RainboWellCS2006';    lats[1169]=35.2047739957;    lngs[1169]=244.3501888856;    ves[1169]=-3.98;    vns[1169]=0.36;    vus[1169]=-0.72;    stddevNs[1169]=0.23;    stddevEs[1169]=0.38;    stddevUs[1169]=0.61;    corrNEs[1169]=-0.002; 
   ids[1170]='P612';    sts[1170]='CalStateSBCS2005';    lats[1170]=34.1873821789;    lngs[1170]=242.6844831812;    ves[1170]=-18.54;    vns[1170]=17.59;    vus[1170]=-0.76;    stddevNs[1170]=0.25;    stddevEs[1170]=0.18;    stddevUs[1170]=0.52;    corrNEs[1170]=-0.005; 
   ids[1171]='P613';    sts[1171]='KellerPeakCS2007';    lats[1171]=34.1961896875;    lngs[1171]=242.9500378925;    ves[1171]=-14.69;    vns[1171]=13.94;    vus[1171]=0.11;    stddevNs[1171]=0.24;    stddevEs[1171]=0.25;    stddevUs[1171]=0.74;    corrNEs[1171]=-0.002; 
   ids[1172]='P614';    sts[1172]='Essex_CTY_CS2007';    lats[1172]=34.7317941692;    lngs[1172]=244.7497378977;    ves[1172]=-3.47;    vns[1172]=0.22;    vus[1172]=-1.68;    stddevNs[1172]=0.19;    stddevEs[1172]=0.2;    stddevUs[1172]=0.78;    corrNEs[1172]=-0.004; 
   ids[1173]='P615';    sts[1173]='PaintdRcksCS2006';    lats[1173]=35.2046091643;    lngs[1173]=243.2370910067;    ves[1173]=-6.75;    vns[1173]=6;    vus[1173]=-1.19;    stddevNs[1173]=0.19;    stddevEs[1173]=0.16;    stddevUs[1173]=0.67;    corrNEs[1173]=-0.009; 
   ids[1174]='P616';    sts[1174]='Red_ButtesCS2007';    lats[1174]=35.4245637774;    lngs[1174]=242.1066677490;    ves[1174]=-10.12;    vns[1174]=10.63;    vus[1174]=-1.47;    stddevNs[1174]=0.19;    stddevEs[1174]=0.19;    stddevUs[1174]=0.65;    corrNEs[1174]=-0.008; 
   ids[1175]='P617';    sts[1175]='TiefortMtsCS2006';    lats[1175]=35.3206432246;    lngs[1175]=243.4283556180;    ves[1175]=-5.47;    vns[1175]=3.55;    vus[1175]=-0.88;    stddevNs[1175]=0.18;    stddevEs[1175]=0.16;    stddevUs[1175]=0.52;    corrNEs[1175]=-0.009; 
   ids[1176]='P618';    sts[1176]='CSUDertStdCS2005';    lats[1176]=35.1418898803;    lngs[1176]=243.8960683429;    ves[1176]=-4.86;    vns[1176]=0.57;    vus[1176]=-0.38;    stddevNs[1176]=0.2;    stddevEs[1176]=0.15;    stddevUs[1176]=0.68;    corrNEs[1176]=-0.007; 
   ids[1177]='P619';    sts[1177]='SilurianHiCS2008';    lats[1177]=35.5259483369;    lngs[1177]=243.8781975189;    ves[1177]=-4.02;    vns[1177]=1.32;    vus[1177]=3.51;    stddevNs[1177]=0.47;    stddevEs[1177]=0.52;    stddevUs[1177]=2.16;    corrNEs[1177]=0.003; 
   ids[1178]='P620';    sts[1178]='AlexnderhlCS2008';    lats[1178]=35.7853645472;    lngs[1178]=243.8550803295;    ves[1178]=-4.53;    vns[1178]=1.38;    vus[1178]=-0.74;    stddevNs[1178]=0.29;    stddevEs[1178]=0.31;    stddevUs[1178]=1.15;    corrNEs[1178]=-0.001; 
   ids[1179]='P621';    sts[1179]='MountPass_CS2005';    lats[1179]=35.4727606435;    lngs[1179]=244.4560502978;    ves[1179]=-3.86;    vns[1179]=0.43;    vus[1179]=-0.67;    stddevNs[1179]=0.2;    stddevEs[1179]=0.15;    stddevUs[1179]=0.52;    corrNEs[1179]=-0.006; 
   ids[1180]='P622';    sts[1180]='RoundVallyCS2005';    lats[1180]=35.1629919183;    lngs[1180]=244.6341359028;    ves[1180]=-3.64;    vns[1180]=0.34;    vus[1180]=-1.31;    stddevNs[1180]=0.18;    stddevEs[1180]=0.15;    stddevUs[1180]=0.57;    corrNEs[1180]=-0.008; 
   ids[1181]='P623';    sts[1181]='VidalJunctCS2005';    lats[1181]=34.1889339957;    lngs[1181]=245.4005956526;    ves[1181]=-3.55;    vns[1181]=0.59;    vus[1181]=-1.92;    stddevNs[1181]=0.17;    stddevEs[1181]=0.15;    stddevUs[1181]=0.61;    corrNEs[1181]=-0.008; 
   ids[1182]='P625';    sts[1182]='CaminoSubsCS2007';    lats[1182]=34.8444437322;    lngs[1182]=245.0348567890;    ves[1182]=-3.41;    vns[1182]=0.4;    vus[1182]=-2.28;    stddevNs[1182]=0.21;    stddevEs[1182]=0.22;    stddevUs[1182]=0.71;    corrNEs[1182]=-0.002; 
   ids[1183]='P626';    sts[1183]='NewYorkMtnCS2005';    lats[1183]=35.2911195030;    lngs[1183]=244.7617231910;    ves[1183]=-3.63;    vns[1183]=0.31;    vus[1183]=-0.76;    stddevNs[1183]=0.21;    stddevEs[1183]=0.15;    stddevUs[1183]=0.51;    corrNEs[1183]=-0.006; 
   ids[1184]='P627';    sts[1184]='QueenVallyNV2006';    lats[1184]=37.9731013225;    lngs[1184]=241.6214756774;    ves[1184]=-5.4;    vns[1184]=5.25;    vus[1184]=-1.78;    stddevNs[1184]=0.18;    stddevEs[1184]=0.19;    stddevUs[1184]=0.66;    corrNEs[1184]=-0.008; 
   ids[1185]='P628';    sts[1185]='Tioga_PassCS2007';    lats[1185]=37.9342304823;    lngs[1185]=240.7506478522;    ves[1185]=-10.11;    vns[1185]=10.27;    vus[1185]=-1.07;    stddevNs[1185]=0.58;    stddevEs[1185]=0.35;    stddevUs[1185]=1.55;    corrNEs[1185]=-0.001; 
   ids[1186]='P629';    sts[1186]='MtTom_LookCS2008';    lats[1186]=37.3758667238;    lngs[1186]=240.8206279010;    ves[1186]=-10.93;    vns[1186]=8.57;    vus[1186]=-1.48;    stddevNs[1186]=0.38;    stddevEs[1186]=0.34;    stddevUs[1186]=1.8;    corrNEs[1186]=0.000; 
   ids[1187]='P630';    sts[1187]='OldMammothCS2006';    lats[1187]=37.6130365353;    lngs[1187]=240.9995588489;    ves[1187]=-9.51;    vns[1187]=8.65;    vus[1187]=-0.71;    stddevNs[1187]=0.81;    stddevEs[1187]=0.2;    stddevUs[1187]=0.78;    corrNEs[1187]=-0.002; 
   ids[1188]='P631';    sts[1188]='Laurel_CrkCS2006';    lats[1188]=37.6053252496;    lngs[1188]=241.0840609097;    ves[1188]=-9;    vns[1188]=5.72;    vus[1188]=1.03;    stddevNs[1188]=1.18;    stddevEs[1188]=0.39;    stddevUs[1188]=0.73;    corrNEs[1188]=-0.001; 
   ids[1189]='P632';    sts[1189]='JuneLake__CS2006';    lats[1189]=37.7856649652;    lngs[1189]=240.9139260909;    ves[1189]=-11.32;    vns[1189]=9.65;    vus[1189]=0.65;    stddevNs[1189]=0.2;    stddevEs[1189]=0.37;    stddevUs[1189]=0.8;    corrNEs[1189]=-0.007; 
   ids[1190]='P633';    sts[1190]='MonoDomesNCS2008';    lats[1190]=37.9134602938;    lngs[1190]=240.9670134265;    ves[1190]=-10.02;    vns[1190]=9.57;    vus[1190]=-1.41;    stddevNs[1190]=0.44;    stddevEs[1190]=0.72;    stddevUs[1190]=1.89;    corrNEs[1190]=0.001; 
   ids[1191]='P634';    sts[1191]='Deer_MountCS2007';    lats[1191]=37.6965922696;    lngs[1191]=240.9742206940;    ves[1191]=-9.31;    vns[1191]=9.13;    vus[1191]=-3.35;    stddevNs[1191]=0.22;    stddevEs[1191]=0.28;    stddevUs[1191]=0.96;    corrNEs[1191]=0.000; 
   ids[1192]='P634';    sts[1192]='Deer_MountCS2007';    lats[1192]=37.6965922616;    lngs[1192]=240.9742206875;    ves[1192]=-9.31;    vns[1192]=9.13;    vus[1192]=-3.35;    stddevNs[1192]=0.22;    stddevEs[1192]=0.28;    stddevUs[1192]=0.96;    corrNEs[1192]=0.000; 
   ids[1193]='P635';    sts[1193]='Grant_LakeCS2007';    lats[1193]=37.8393138275;    lngs[1193]=240.8620105671;    ves[1193]=-11.04;    vns[1193]=9.85;    vus[1193]=0.2;    stddevNs[1193]=0.25;    stddevEs[1193]=0.3;    stddevUs[1193]=0.88;    corrNEs[1193]=-0.003; 
   ids[1194]='P636';    sts[1194]='LogCabinRFCS2007';    lats[1194]=37.9627713932;    lngs[1194]=240.8532941589;    ves[1194]=-11.4;    vns[1194]=8.53;    vus[1194]=-0.71;    stddevNs[1194]=0.2;    stddevEs[1194]=0.25;    stddevUs[1194]=0.89;    corrNEs[1194]=-0.006; 
   ids[1195]='P637';    sts[1195]='PumiceVallCS2008';    lats[1195]=37.9141058135;    lngs[1195]=240.9163025836;    ves[1195]=-10.63;    vns[1195]=8.98;    vus[1195]=-1.93;    stddevNs[1195]=0.44;    stddevEs[1195]=0.53;    stddevUs[1195]=1.55;    corrNEs[1195]=-0.001; 
   ids[1196]='P638';    sts[1196]='LookoutMtnCS2008';    lats[1196]=37.7343176061;    lngs[1196]=241.0565222928;    ves[1196]=-10.78;    vns[1196]=9.99;    vus[1196]=1.17;    stddevNs[1196]=0.3;    stddevEs[1196]=0.37;    stddevUs[1196]=1.33;    corrNEs[1196]=-0.002; 
   ids[1197]='P639';    sts[1197]='FumaroleVaCS2006';    lats[1197]=37.6545626234;    lngs[1197]=241.1312917239;    ves[1197]=-7.12;    vns[1197]=7.11;    vus[1197]=3.1;    stddevNs[1197]=0.25;    stddevEs[1197]=0.2;    stddevUs[1197]=0.79;    corrNEs[1197]=-0.006; 
   ids[1198]='P640';    sts[1198]='Mono_MillsCS2006';    lats[1198]=37.8917385474;    lngs[1198]=241.0395930850;    ves[1198]=-8.87;    vns[1198]=9.59;    vus[1198]=1.51;    stddevNs[1198]=0.2;    stddevEs[1198]=0.19;    stddevUs[1198]=0.73;    corrNEs[1198]=-0.008; 
   ids[1199]='P641';    sts[1199]='SagehenMdwCS2008';    lats[1199]=37.8781893484;    lngs[1199]=241.1480808884;    ves[1199]=-7.4;    vns[1199]=9.45;    vus[1199]=-0.25;    stddevNs[1199]=0.34;    stddevEs[1199]=0.36;    stddevUs[1199]=1.51;    corrNEs[1199]=-0.001; 
   ids[1200]='P642';    sts[1200]='McGeeMountCS2006';    lats[1200]=37.5913670252;    lngs[1200]=241.1833302359;    ves[1200]=-9.87;    vns[1200]=5.33;    vus[1200]=0.78;    stddevNs[1200]=1.14;    stddevEs[1200]=0.84;    stddevUs[1200]=0.71;    corrNEs[1200]=-0.000; 
   ids[1201]='P643';    sts[1201]='Toms_PlaceCS2006';    lats[1201]=37.5615530534;    lngs[1201]=241.3018384898;    ves[1201]=-7.91;    vns[1201]=6.41;    vus[1201]=-0.03;    stddevNs[1201]=0.2;    stddevEs[1201]=0.47;    stddevUs[1201]=0.74;    corrNEs[1201]=-0.005; 
   ids[1202]='P643';    sts[1202]='Toms_PlaceCS2006';    lats[1202]=37.5615530492;    lngs[1202]=241.3018384691;    ves[1202]=-7.91;    vns[1202]=6.41;    vus[1202]=-0.03;    stddevNs[1202]=0.2;    stddevEs[1202]=0.47;    stddevUs[1202]=0.74;    corrNEs[1202]=-0.005; 
   ids[1203]='P644';    sts[1203]='WheelrRidgCS2007';    lats[1203]=37.4953923575;    lngs[1203]=241.3155977218;    ves[1203]=-8.6;    vns[1203]=5.78;    vus[1203]=-1.35;    stddevNs[1203]=0.27;    stddevEs[1203]=0.26;    stddevUs[1203]=0.84;    corrNEs[1203]=-0.004; 
   ids[1204]='P645';    sts[1204]='OwensGorgeCS2008';    lats[1204]=37.5412718206;    lngs[1204]=241.4065452254;    ves[1204]=-7.74;    vns[1204]=6.6;    vus[1204]=0.29;    stddevNs[1204]=0.36;    stddevEs[1204]=0.54;    stddevUs[1204]=1.52;    corrNEs[1204]=-0.000; 
   ids[1205]='P646';    sts[1205]='FossilTiltCS2006';    lats[1205]=37.6770347702;    lngs[1205]=241.1800127119;    ves[1205]=-6.43;    vns[1205]=7.66;    vus[1205]=1.34;    stddevNs[1205]=0.2;    stddevEs[1205]=0.24;    stddevUs[1205]=0.79;    corrNEs[1205]=-0.006; 
   ids[1206]='P647';    sts[1206]='OHarrelCynCS2008';    lats[1206]=37.7543617579;    lngs[1206]=241.2322944499;    ves[1206]=-6.41;    vns[1206]=8.46;    vus[1206]=1.82;    stddevNs[1206]=0.43;    stddevEs[1206]=0.31;    stddevUs[1206]=1.58;    corrNEs[1206]=0.002; 
   ids[1207]='P648';    sts[1207]='CraterMarkCS2006';    lats[1207]=37.8000158617;    lngs[1207]=240.9807883542;    ves[1207]=-10.87;    vns[1207]=9.56;    vus[1207]=0.68;    stddevNs[1207]=0.19;    stddevEs[1207]=0.19;    stddevUs[1207]=0.8;    corrNEs[1207]=-0.007; 
   ids[1208]='P649';    sts[1208]='GraniteMtnCS2006';    lats[1208]=37.9034565930;    lngs[1208]=241.2637708666;    ves[1208]=-6.25;    vns[1208]=8.17;    vus[1208]=0.49;    stddevNs[1208]=0.19;    stddevEs[1208]=0.18;    stddevUs[1208]=0.65;    corrNEs[1208]=-0.008; 
   ids[1209]='P650';    sts[1209]='AntelopeMtCS2006';    lats[1209]=37.8912767494;    lngs[1209]=241.4450998858;    ves[1209]=-5.6;    vns[1209]=6.54;    vus[1209]=-0.95;    stddevNs[1209]=0.18;    stddevEs[1209]=0.17;    stddevUs[1209]=0.72;    corrNEs[1209]=-0.008; 
   ids[1210]='P651';    sts[1210]='ChalfantVaCS2006';    lats[1210]=37.5631096767;    lngs[1210]=241.6130177019;    ves[1210]=-6.6;    vns[1210]=5.87;    vus[1210]=-1.35;    stddevNs[1210]=0.22;    stddevEs[1210]=0.17;    stddevUs[1210]=0.76;    corrNEs[1210]=-0.007; 
   ids[1211]='P652';    sts[1211]='BarcroftObCS2007';    lats[1211]=37.5891581871;    lngs[1211]=241.7615487947;    ves[1211]=-6.4;    vns[1211]=3.99;    vus[1211]=-1.39;    stddevNs[1211]=0.25;    stddevEs[1211]=0.31;    stddevUs[1211]=0.89;    corrNEs[1211]=-0.003; 
   ids[1212]='P652';    sts[1212]='BarcroftObCS2007';    lats[1212]=37.5891581964;    lngs[1212]=241.7615487933;    ves[1212]=-6.4;    vns[1212]=3.99;    vus[1212]=-1.39;    stddevNs[1212]=0.25;    stddevEs[1212]=0.31;    stddevUs[1212]=0.89;    corrNEs[1212]=-0.003; 
   ids[1213]='P652';    sts[1213]='BarcroftObCS2007';    lats[1213]=37.5891581861;    lngs[1213]=241.7615487960;    ves[1213]=-6.4;    vns[1213]=3.99;    vus[1213]=-1.39;    stddevNs[1213]=0.25;    stddevEs[1213]=0.31;    stddevUs[1213]=0.89;    corrNEs[1213]=-0.003; 
   ids[1214]='P653';    sts[1214]='BlindSprinCS2006';    lats[1214]=37.7374999298;    lngs[1214]=241.5283403090;    ves[1214]=-5.76;    vns[1214]=6.08;    vus[1214]=-0.82;    stddevNs[1214]=0.18;    stddevEs[1214]=0.18;    stddevUs[1214]=0.71;    corrNEs[1214]=-0.009; 
   ids[1215]='P654';    sts[1215]='ConwayRoadCS2008';    lats[1215]=38.0579349085;    lngs[1215]=240.8498112600;    ves[1215]=-9.85;    vns[1215]=9.36;    vus[1215]=3.86;    stddevNs[1215]=0.32;    stddevEs[1215]=0.36;    stddevUs[1215]=1.34;    corrNEs[1215]=-0.002; 
   ids[1216]='P655';    sts[1216]='EveritHillCN2006';    lats[1216]=41.2944860826;    lngs[1216]=237.7936783884;    ves[1216]=-6.21;    vns[1216]=5.68;    vus[1216]=-0.48;    stddevNs[1216]=0.55;    stddevEs[1216]=0.33;    stddevUs[1216]=1.5;    corrNEs[1216]=-0.002; 
   ids[1217]='P656';    sts[1217]='SHGrayBte_CN2007';    lats[1217]=41.3448392220;    lngs[1217]=237.8041624989;    ves[1217]=-6.01;    vns[1217]=5.4;    vus[1217]=-1.49;    stddevNs[1217]=1.42;    stddevEs[1217]=0.72;    stddevUs[1217]=2.08;    corrNEs[1217]=-0.000; 
   ids[1218]='P657';    sts[1218]='SHWestSideCN2007';    lats[1218]=41.3812394947;    lngs[1218]=237.7061519930;    ves[1218]=-5.27;    vns[1218]=5.84;    vus[1218]=-0.37;    stddevNs[1218]=0.34;    stddevEs[1218]=0.34;    stddevUs[1218]=0.96;    corrNEs[1218]=-0.002; 
   ids[1219]='P658';    sts[1219]='SHNorthGteCN2007';    lats[1219]=41.4791771822;    lngs[1219]=237.8090883442;    ves[1219]=-4.68;    vns[1219]=6.08;    vus[1219]=0.17;    stddevNs[1219]=0.35;    stddevEs[1219]=0.29;    stddevUs[1219]=0.92;    corrNEs[1219]=-0.003; 
   ids[1220]='P659';    sts[1220]='SHGravlCrkCN2007';    lats[1220]=41.4537337961;    lngs[1220]=237.9073367882;    ves[1220]=-5.02;    vns[1220]=5.81;    vus[1220]=0.55;    stddevNs[1220]=0.47;    stddevEs[1220]=0.65;    stddevUs[1220]=1.99;    corrNEs[1220]=-0.003; 
   ids[1221]='P660';    sts[1221]='SHAshCreekCN2007';    lats[1221]=41.4095917127;    lngs[1221]=237.9323225752;    ves[1221]=-5.41;    vns[1221]=5.67;    vus[1221]=-2.01;    stddevNs[1221]=0.56;    stddevEs[1221]=0.55;    stddevUs[1221]=1.8;    corrNEs[1221]=0.003; 
   ids[1222]='P661';    sts[1222]='SHHotlum__CN2007';    lats[1222]=41.4635982355;    lngs[1222]=237.6873298740;    ves[1222]=-5.14;    vns[1222]=5.74;    vus[1222]=-1;    stddevNs[1222]=0.34;    stddevEs[1222]=0.33;    stddevUs[1222]=0.87;    corrNEs[1222]=-0.003; 
   ids[1223]='P663';    sts[1223]='SHWhalebakCN2007';    lats[1223]=41.5319399648;    lngs[1223]=237.8470776457;    ves[1223]=-4;    vns[1223]=5.35;    vus[1223]=-4.12;    stddevNs[1223]=0.43;    stddevEs[1223]=0.47;    stddevUs[1223]=1.35;    corrNEs[1223]=-0.003; 
   ids[1224]='P663';    sts[1224]='SHWhalebakCN2007';    lats[1224]=41.5319399672;    lngs[1224]=237.8470776481;    ves[1224]=-4;    vns[1224]=5.35;    vus[1224]=-4.12;    stddevNs[1224]=0.43;    stddevEs[1224]=0.47;    stddevUs[1224]=1.35;    corrNEs[1224]=-0.003; 
   ids[1225]='P664';    sts[1225]='LVMtHelen_CN2007';    lats[1225]=40.4731962204;    lngs[1225]=238.5040954934;    ves[1225]=-7.63;    vns[1225]=6.95;    vus[1225]=-10.31;    stddevNs[1225]=6.37;    stddevEs[1225]=3.11;    stddevUs[1225]=4.01;    corrNEs[1225]=0.000; 
   ids[1226]='P665';    sts[1226]='LVLoDimondCN2007';    lats[1226]=40.4561282651;    lngs[1226]=238.4742456830;    ves[1226]=-6.35;    vns[1226]=8.87;    vus[1226]=-6.48;    stddevNs[1226]=1.39;    stddevEs[1226]=0.41;    stddevUs[1226]=1.6;    corrNEs[1226]=-0.001; 
   ids[1227]='P666';    sts[1227]='LVBumpass_CN2007';    lats[1227]=40.4653457981;    lngs[1227]=238.4854908730;    ves[1227]=-7.1;    vns[1227]=8.77;    vus[1227]=-7.63;    stddevNs[1227]=0.44;    stddevEs[1227]=0.33;    stddevUs[1227]=1.26;    corrNEs[1227]=-0.002; 
   ids[1228]='P667';    sts[1228]='LVWhite___CN2007';    lats[1228]=40.4658270366;    lngs[1228]=238.5325600485;    ves[1228]=-11.88;    vns[1228]=9.47;    vus[1228]=-8.47;    stddevNs[1228]=19.54;    stddevEs[1228]=5.9;    stddevUs[1228]=6.72;    corrNEs[1228]=0.000; 
   ids[1229]='P668';    sts[1229]='LVHatLake_CN2007';    lats[1229]=40.5089941728;    lngs[1229]=238.5369234292;    ves[1229]=-9.53;    vns[1229]=3.87;    vus[1229]=-4.91;    stddevNs[1229]=0.29;    stddevEs[1229]=0.45;    stddevUs[1229]=1.49;    corrNEs[1229]=-0.001; 
   ids[1230]='P669';    sts[1230]='BluLakeCynCN2008';    lats[1230]=40.4921248478;    lngs[1230]=238.3962138900;    ves[1230]=-10.54;    vns[1230]=6.21;    vus[1230]=4.26;    stddevNs[1230]=0.97;    stddevEs[1230]=1.46;    stddevUs[1230]=3.13;    corrNEs[1230]=0.014; 
   ids[1231]='P670';    sts[1231]='LVManzLakeCN2007';    lats[1231]=40.5399055641;    lngs[1231]=238.4228038730;    ves[1231]=-7.89;    vns[1231]=4.17;    vus[1231]=-3.2;    stddevNs[1231]=0.37;    stddevEs[1231]=0.4;    stddevUs[1231]=1.24;    corrNEs[1231]=-0.003; 
   ids[1232]='P670';    sts[1232]='LVManzLakeCN2007';    lats[1232]=40.5399055671;    lngs[1232]=238.4228038756;    ves[1232]=-7.89;    vns[1232]=4.17;    vus[1232]=-3.2;    stddevNs[1232]=0.37;    stddevEs[1232]=0.4;    stddevUs[1232]=1.24;    corrNEs[1232]=-0.003; 
   ids[1233]='P671';    sts[1233]='SiffordMtnCN2008';    lats[1233]=40.4091413239;    lngs[1233]=238.5717921095;    ves[1233]=-12.64;    vns[1233]=10.15;    vus[1233]=-8.46;    stddevNs[1233]=0.88;    stddevEs[1233]=1.59;    stddevUs[1233]=2.43;    corrNEs[1233]=0.009; 
   ids[1234]='P672';    sts[1234]='LavaBedsNMCN2005';    lats[1234]=41.7115704292;    lngs[1234]=238.4930624050;    ves[1234]=-5.07;    vns[1234]=4.58;    vus[1234]=-1.36;    stddevNs[1234]=0.16;    stddevEs[1234]=0.18;    stddevUs[1234]=0.6;    corrNEs[1234]=-0.009; 
   ids[1235]='P673';    sts[1235]='MedicineLkCN2006';    lats[1235]=41.5858235339;    lngs[1235]=238.3869854416;    ves[1235]=-3.9;    vns[1235]=5.36;    vus[1235]=-4.86;    stddevNs[1235]=0.24;    stddevEs[1235]=0.27;    stddevUs[1235]=1.02;    corrNEs[1235]=-0.006; 
   ids[1236]='P674';    sts[1236]='GlassMtn__CN2006';    lats[1236]=41.6163226755;    lngs[1236]=238.5100144958;    ves[1236]=-6.3;    vns[1236]=4.32;    vus[1236]=-2.17;    stddevNs[1236]=0.18;    stddevEs[1236]=0.22;    stddevUs[1236]=0.67;    corrNEs[1236]=-0.006; 
   ids[1237]='P675';    sts[1237]='Holbrook__ID2006';    lats[1237]=42.2122216900;    lngs[1237]=247.2811784473;    ves[1237]=-3.29;    vns[1237]=-0.97;    vus[1237]=-1.03;    stddevNs[1237]=0.22;    stddevEs[1237]=0.16;    stddevUs[1237]=0.68;    corrNEs[1237]=-0.004; 
   ids[1238]='P676';    sts[1238]='HLFHnrysLkID2005';    lats[1238]=44.6542164671;    lngs[1238]=248.6620494691;    ves[1238]=-1.4;    vns[1238]=-4.18;    vus[1238]=-0.07;    stddevNs[1238]=0.34;    stddevEs[1238]=0.15;    stddevUs[1238]=0.96;    corrNEs[1238]=-0.002; 
   ids[1239]='P677';    sts[1239]='WoodlandFmID2006';    lats[1239]=42.8789193215;    lngs[1239]=246.1316978712;    ves[1239]=-3.16;    vns[1239]=-0.92;    vus[1239]=-1.52;    stddevNs[1239]=0.23;    stddevEs[1239]=0.15;    stddevUs[1239]=0.74;    corrNEs[1239]=-0.004; 
   ids[1240]='P678';    sts[1240]='AtomicCityID2006';    lats[1240]=43.4489639387;    lngs[1240]=247.1954495704;    ves[1240]=-2.59;    vns[1240]=-1.26;    vus[1240]=-1.58;    stddevNs[1240]=0.2;    stddevEs[1240]=0.15;    stddevUs[1240]=0.78;    corrNEs[1240]=-0.004; 
   ids[1241]='P679';    sts[1241]='LittleLostID2008';    lats[1241]=44.0398199483;    lngs[1241]=246.6935195132;    ves[1241]=-1.5;    vns[1241]=-0.98;    vus[1241]=-3.23;    stddevNs[1241]=0.88;    stddevEs[1241]=0.62;    stddevUs[1241]=1.77;    corrNEs[1241]=0.002; 
   ids[1242]='P680';    sts[1242]='YNPWestYelMT2005';    lats[1242]=44.5983573664;    lngs[1242]=248.9012508771;    ves[1242]=-0.42;    vns[1242]=-2.94;    vus[1242]=-1.6;    stddevNs[1242]=0.24;    stddevEs[1242]=0.4;    stddevUs[1242]=1.27;    corrNEs[1242]=-0.001; 
   ids[1243]='P681';    sts[1243]='MedicineLgID2006';    lats[1243]=44.3997761440;    lngs[1243]=247.3635266667;    ves[1243]=-1.2;    vns[1243]=-1.3;    vus[1243]=-3.63;    stddevNs[1243]=0.27;    stddevEs[1243]=0.26;    stddevUs[1243]=0.74;    corrNEs[1243]=-0.002; 
   ids[1244]='P682';    sts[1244]='SaltCreek_WY2008';    lats[1244]=42.5033008854;    lngs[1244]=249.0924737626;    ves[1244]=-0.82;    vns[1244]=-0.6;    vus[1244]=-2.05;    stddevNs[1244]=0.78;    stddevEs[1244]=0.84;    stddevUs[1244]=1.96;    corrNEs[1244]=0.002; 
   ids[1245]='P683';    sts[1245]='ChesterfldID2006';    lats[1245]=42.8266975105;    lngs[1245]=248.2654625007;    ves[1245]=-2.56;    vns[1245]=-2.8;    vus[1245]=-3.88;    stddevNs[1245]=0.59;    stddevEs[1245]=0.29;    stddevUs[1245]=0.78;    corrNEs[1245]=-0.001; 
   ids[1246]='P684';    sts[1246]='ParkinsonFID2005';    lats[1246]=43.9191486867;    lngs[1246]=248.5495137507;    ves[1246]=-1.94;    vns[1246]=-1.84;    vus[1246]=-2.91;    stddevNs[1246]=0.21;    stddevEs[1246]=0.14;    stddevUs[1246]=0.8;    corrNEs[1246]=-0.003; 
   ids[1247]='P685';    sts[1247]='Sand_DunesID2007';    lats[1247]=44.0679476046;    lngs[1247]=248.1698457988;    ves[1247]=-1.82;    vns[1247]=-2.16;    vus[1247]=-3.27;    stddevNs[1247]=0.26;    stddevEs[1247]=0.24;    stddevUs[1247]=1.28;    corrNEs[1247]=0.000; 
   ids[1248]='P686';    sts[1248]='YNPFishCrkID2007';    lats[1248]=44.2520553106;    lngs[1248]=248.8449644109;    ves[1248]=-1.74;    vns[1248]=-2.38;    vus[1248]=-6.6;    stddevNs[1248]=0.42;    stddevEs[1248]=0.27;    stddevUs[1248]=1.04;    corrNEs[1248]=-0.000; 
   ids[1249]='P687';    sts[1249]='MSH_Tube1_WA2004';    lats[1249]=46.1095724667;    lngs[1249]=237.6453614446;    ves[1249]=4.2;    vns[1249]=5.54;    vus[1249]=-1.69;    stddevNs[1249]=0.49;    stddevEs[1249]=0.44;    stddevUs[1249]=0.94;    corrNEs[1249]=-0.003; 
   ids[1250]='P687';    sts[1250]='MSH_Tube1_WA2004';    lats[1250]=46.1095723944;    lngs[1250]=237.6453614187;    ves[1250]=4.2;    vns[1250]=5.54;    vus[1250]=-1.69;    stddevNs[1250]=0.49;    stddevEs[1250]=0.44;    stddevUs[1250]=0.94;    corrNEs[1250]=-0.003; 
   ids[1251]='P688';    sts[1251]='MSH_MitchpWA2008';    lats[1251]=46.0301217541;    lngs[1251]=237.8358396553;    ves[1251]=1.48;    vns[1251]=3.15;    vus[1251]=1.43;    stddevNs[1251]=6.37;    stddevEs[1251]=2.71;    stddevUs[1251]=7.06;    corrNEs[1251]=0.000; 
   ids[1252]='P689';    sts[1252]='MSH_MorgleWA2005';    lats[1252]=46.1895536801;    lngs[1252]=237.6393804881;    ves[1252]=2.03;    vns[1252]=4.63;    vus[1252]=-1.35;    stddevNs[1252]=0.24;    stddevEs[1252]=0.34;    stddevUs[1252]=0.74;    corrNEs[1252]=-0.003; 
   ids[1253]='P690';    sts[1253]='MSH_SRidgeWA2005';    lats[1253]=46.1799891090;    lngs[1253]=237.8101309295;    ves[1253]=1.39;    vns[1253]=6.36;    vus[1253]=-3;    stddevNs[1253]=0.32;    stddevEs[1253]=0.59;    stddevUs[1253]=0.78;    corrNEs[1253]=-0.001; 
   ids[1254]='P691';    sts[1254]='MSH_StdBkrWA2006';    lats[1254]=46.2314839445;    lngs[1254]=237.7730357477;    ves[1254]=2.14;    vns[1254]=6.22;    vus[1254]=-0.58;    stddevNs[1254]=0.31;    stddevEs[1254]=0.31;    stddevUs[1254]=0.75;    corrNEs[1254]=-0.001; 
   ids[1255]='P692';    sts[1255]='MSH_LoowitWA2006';    lats[1255]=46.2244987020;    lngs[1255]=237.8157925025;    ves[1255]=5.88;    vns[1255]=2.4;    vus[1255]=-2.8;    stddevNs[1255]=0.72;    stddevEs[1255]=0.4;    stddevUs[1255]=0.94;    corrNEs[1255]=-0.001; 
   ids[1256]='P693';    sts[1256]='MSH_NWDOMEWA2004';    lats[1256]=46.2103217824;    lngs[1256]=237.7976519693;    ves[1256]=4.6;    vns[1256]=2.53;    vus[1256]=-4.03;    stddevNs[1256]=0.25;    stddevEs[1256]=0.37;    stddevUs[1256]=0.77;    corrNEs[1256]=-0.002; 
   ids[1257]='P694';    sts[1257]='MSH_CldWtrWA2006';    lats[1257]=46.2996297174;    lngs[1257]=237.8180692077;    ves[1257]=3.09;    vns[1257]=7.08;    vus[1257]=-1.61;    stddevNs[1257]=0.38;    stddevEs[1257]=0.73;    stddevUs[1257]=1.05;    corrNEs[1257]=-0.001; 
   ids[1258]='P695';    sts[1258]='MSH_NRIDGEWA2004';    lats[1258]=46.1989875547;    lngs[1258]=237.8357865712;    ves[1258]=-0.43;    vns[1258]=5.05;    vus[1258]=-1.97;    stddevNs[1258]=0.27;    stddevEs[1258]=0.51;    stddevUs[1258]=0.86;    corrNEs[1258]=-0.004; 
   ids[1259]='P695';    sts[1259]='MSH_NRIDGEWA2004';    lats[1259]=46.1989875479;    lngs[1259]=237.8357865493;    ves[1259]=-0.43;    vns[1259]=5.05;    vus[1259]=-1.97;    stddevNs[1259]=0.27;    stddevEs[1259]=0.51;    stddevUs[1259]=0.86;    corrNEs[1259]=-0.004; 
   ids[1260]='P696';    sts[1260]='MSH_EADOMEWA2004';    lats[1260]=46.1968814144;    lngs[1260]=237.8483642996;    ves[1260]=2.31;    vns[1260]=4.36;    vus[1260]=-2.08;    stddevNs[1260]=0.24;    stddevEs[1260]=0.37;    stddevUs[1260]=0.75;    corrNEs[1260]=-0.003; 
   ids[1261]='P697';    sts[1261]='MSH_SERDGEWA2004';    lats[1261]=46.1876428702;    lngs[1261]=237.8233906449;    ves[1261]=-4.66;    vns[1261]=10.14;    vus[1261]=-5.62;    stddevNs[1261]=1.2;    stddevEs[1261]=1.36;    stddevUs[1261]=0.82;    corrNEs[1261]=-0.000; 
   ids[1262]='P698';    sts[1262]='MSH_SESLPEWA2004';    lats[1262]=46.1734658809;    lngs[1262]=237.8393887480;    ves[1262]=2.93;    vns[1262]=4.4;    vus[1262]=-2.24;    stddevNs[1262]=0.38;    stddevEs[1262]=0.65;    stddevUs[1262]=0.93;    corrNEs[1262]=-0.001; 
   ids[1263]='P699';    sts[1263]='MSH_WSTRADWA2005';    lats[1263]=46.1897832398;    lngs[1263]=237.7967740641;    ves[1263]=-3.81;    vns[1263]=-12.76;    vus[1263]=-6.18;    stddevNs[1263]=3.41;    stddevEs[1263]=3.21;    stddevUs[1263]=0.86;    corrNEs[1263]=0.000; 
   ids[1264]='P700';    sts[1264]='MSH_ButteCWA2006';    lats[1264]=46.1781351707;    lngs[1264]=237.7826346690;    ves[1264]=2.35;    vns[1264]=4.06;    vus[1264]=-1.05;    stddevNs[1264]=0.28;    stddevEs[1264]=0.47;    stddevUs[1264]=0.77;    corrNEs[1264]=0.001; 
   ids[1265]='P701';    sts[1265]='MSH_ApeCynWA2006';    lats[1265]=46.1946249524;    lngs[1265]=237.8666484286;    ves[1265]=3.62;    vns[1265]=4.51;    vus[1265]=-1.46;    stddevNs[1265]=0.21;    stddevEs[1265]=0.6;    stddevUs[1265]=0.86;    corrNEs[1265]=-0.002; 
   ids[1266]='P702';    sts[1266]='MSH_ElkRckWA2004';    lats[1266]=46.3001615696;    lngs[1266]=237.6543525374;    ves[1266]=3.9;    vns[1266]=4.14;    vus[1266]=-2.64;    stddevNs[1266]=0.56;    stddevEs[1266]=0.31;    stddevUs[1266]=0.62;    corrNEs[1266]=-0.001; 
   ids[1267]='P703';    sts[1267]='MSH_ClmBivWA2006';    lats[1267]=46.1452583477;    lngs[1267]=237.8038089855;    ves[1267]=3.23;    vns[1267]=0.36;    vus[1267]=0.74;    stddevNs[1267]=0.44;    stddevEs[1267]=0.56;    stddevUs[1267]=1.27;    corrNEs[1267]=-0.000; 
   ids[1268]='P704';    sts[1268]='WindyR202GWA2007';    lats[1268]=46.2446221816;    lngs[1268]=237.8631096777;    ves[1268]=-15.62;    vns[1268]=0.72;    vus[1268]=-2.26;    stddevNs[1268]=2.83;    stddevEs[1268]=2.61;    stddevUs[1268]=5.8;    corrNEs[1268]=-0.002; 
   ids[1269]='P705';    sts[1269]='MSH_WGoat_WA2006';    lats[1269]=46.1729588294;    lngs[1269]=237.6894066563;    ves[1269]=0.96;    vns[1269]=4.68;    vus[1269]=-2.36;    stddevNs[1269]=0.43;    stddevEs[1269]=0.42;    stddevUs[1269]=1.18;    corrNEs[1269]=-0.004; 
   ids[1270]='P705';    sts[1270]='MSH_WGoat_WA2006';    lats[1270]=46.1729588213;    lngs[1270]=237.6894065944;    ves[1270]=0.96;    vns[1270]=4.68;    vus[1270]=-2.36;    stddevNs[1270]=0.43;    stddevEs[1270]=0.42;    stddevUs[1270]=1.18;    corrNEs[1270]=-0.004; 
   ids[1271]='P706';    sts[1271]='MatadorRchMT2006';    lats[1271]=45.0434701713;    lngs[1271]=247.4759109314;    ves[1271]=-0.77;    vns[1271]=-0.92;    vus[1271]=-2.96;    stddevNs[1271]=0.2;    stddevEs[1271]=0.17;    stddevUs[1271]=0.82;    corrNEs[1271]=-0.003; 
   ids[1272]='P707';    sts[1272]='RedRockLakMT2008';    lats[1272]=44.7187704322;    lngs[1272]=248.1628363121;    ves[1272]=-1.66;    vns[1272]=-1.92;    vus[1272]=-1.55;    stddevNs[1272]=0.55;    stddevEs[1272]=0.4;    stddevUs[1272]=1.62;    corrNEs[1272]=0.002; 
   ids[1273]='P708';    sts[1273]='YNPGrandTgWY2007';    lats[1273]=43.7863951836;    lngs[1273]=249.0664069784;    ves[1273]=-2.86;    vns[1273]=-0.47;    vus[1273]=-2.89;    stddevNs[1273]=1.49;    stddevEs[1273]=2.38;    stddevUs[1273]=3.98;    corrNEs[1273]=0.000; 
   ids[1274]='P708';    sts[1274]='YNPGrandTgWY2007';    lats[1274]=43.7863951355;    lngs[1274]=249.0664068864;    ves[1274]=-2.86;    vns[1274]=-0.47;    vus[1274]=-2.89;    stddevNs[1274]=1.49;    stddevEs[1274]=2.38;    stddevUs[1274]=3.98;    corrNEs[1274]=0.000; 
   ids[1275]='P709';    sts[1275]='YNPPromtryWY2005';    lats[1275]=44.3917954078;    lngs[1275]=249.7139935187;    ves[1275]=-0.46;    vns[1275]=-0.48;    vus[1275]=3.75;    stddevNs[1275]=0.25;    stddevEs[1275]=0.19;    stddevUs[1275]=1.15;    corrNEs[1275]=-0.003; 
   ids[1276]='P710';    sts[1276]='GTNPGrassyWY2007';    lats[1276]=44.0957541666;    lngs[1276]=249.2678585268;    ves[1276]=-2.42;    vns[1276]=-0.62;    vus[1276]=-5.06;    stddevNs[1276]=0.44;    stddevEs[1276]=0.5;    stddevUs[1276]=0.9;    corrNEs[1276]=-0.000; 
   ids[1277]='P711';    sts[1277]='YNPMadisonWY2005';    lats[1277]=44.6355651049;    lngs[1277]=249.1389343010;    ves[1277]=6.17;    vns[1277]=1.52;    vus[1277]=-8.52;    stddevNs[1277]=0.25;    stddevEs[1277]=1.57;    stddevUs[1277]=0.75;    corrNEs[1277]=0.000; 
   ids[1278]='P712';    sts[1278]='YNP_Bacon_MT2008';    lats[1278]=44.9574715121;    lngs[1278]=248.9276212060;    ves[1278]=-0.92;    vns[1278]=-0.14;    vus[1278]=1.88;    stddevNs[1278]=0.7;    stddevEs[1278]=0.94;    stddevUs[1278]=2.34;    corrNEs[1278]=0.007; 
   ids[1279]='P713';    sts[1279]='GrantT944GWY2008';    lats[1279]=44.3896710314;    lngs[1279]=249.4562663654;    ves[1279]=0.97;    vns[1279]=-3.62;    vus[1279]=-4.11;    stddevNs[1279]=7.39;    stddevEs[1279]=4.13;    stddevUs[1279]=4.16;    corrNEs[1279]=0.001; 
   ids[1280]='P714';    sts[1280]='YNPPantherWY2008';    lats[1280]=44.8958491890;    lngs[1280]=249.2555776766;    ves[1280]=-0.73;    vns[1280]=4.14;    vus[1280]=6.6;    stddevNs[1280]=0.99;    stddevEs[1280]=0.83;    stddevUs[1280]=3.82;    corrNEs[1280]=-0.021; 
   ids[1281]='P714';    sts[1281]='YNPPantherWY2008';    lats[1281]=44.8958491615;    lngs[1281]=249.2555776633;    ves[1281]=-0.73;    vns[1281]=4.14;    vus[1281]=6.6;    stddevNs[1281]=0.99;    stddevEs[1281]=0.83;    stddevUs[1281]=3.82;    corrNEs[1281]=-0.021; 
   ids[1282]='P715';    sts[1282]='WindyMountWY2006';    lats[1282]=43.5007690071;    lngs[1282]=250.3101485530;    ves[1282]=-0.73;    vns[1282]=-0.16;    vus[1282]=-2.91;    stddevNs[1282]=0.19;    stddevEs[1282]=0.18;    stddevUs[1282]=1.06;    corrNEs[1282]=-0.004; 
   ids[1283]='P716';    sts[1283]='YNPCanyon_WY2005';    lats[1283]=44.7182580499;    lngs[1283]=249.4884873195;    ves[1283]=-7.25;    vns[1283]=1.21;    vus[1283]=-0.88;    stddevNs[1283]=0.32;    stddevEs[1283]=0.86;    stddevUs[1283]=0.8;    corrNEs[1283]=0.001; 
   ids[1284]='P717';    sts[1284]='ShoshoneRvWY2006';    lats[1284]=44.4854062679;    lngs[1284]=250.1027085517;    ves[1284]=-0.58;    vns[1284]=-0.68;    vus[1284]=-3.01;    stddevNs[1284]=0.23;    stddevEs[1284]=0.22;    stddevUs[1284]=0.85;    corrNEs[1284]=-0.001; 
   ids[1285]='P718';    sts[1285]='DeadIndianWY2006';    lats[1285]=44.7531008419;    lngs[1285]=250.6236930168;    ves[1285]=-0.72;    vns[1285]=-0.89;    vus[1285]=-1.53;    stddevNs[1285]=0.19;    stddevEs[1285]=0.23;    stddevUs[1285]=0.81;    corrNEs[1285]=0.000; 
   ids[1286]='P719';    sts[1286]='EnnisFish_MT2006';    lats[1286]=45.2177574667;    lngs[1286]=248.2109762792;    ves[1286]=-0.36;    vns[1286]=-0.82;    vus[1286]=-2.52;    stddevNs[1286]=0.19;    stddevEs[1286]=0.2;    stddevUs[1286]=0.69;    corrNEs[1286]=-0.003; 
   ids[1287]='P720';    sts[1287]='YNPSlough_WY2005';    lats[1287]=44.9431005313;    lngs[1287]=249.6937429922;    ves[1287]=-0.6;    vns[1287]=-0.13;    vus[1287]=-1.93;    stddevNs[1287]=0.31;    stddevEs[1287]=0.17;    stddevUs[1287]=0.93;    corrNEs[1287]=-0.003; 
   ids[1288]='P721';    sts[1288]='YNP_NEEnt_MT2005';    lats[1288]=45.0028755296;    lngs[1288]=249.9979509126;    ves[1288]=-0.3;    vns[1288]=-0.65;    vus[1288]=-1.21;    stddevNs[1288]=0.34;    stddevEs[1288]=0.17;    stddevUs[1288]=1.09;    corrNEs[1288]=-0.001; 
   ids[1289]='P722';    sts[1289]='YNPBassRchMT2005';    lats[1289]=45.4572242579;    lngs[1289]=250.4289898560;    ves[1289]=0.18;    vns[1289]=-0.73;    vus[1289]=-1.82;    stddevNs[1289]=0.25;    stddevEs[1289]=0.18;    stddevUs[1289]=0.97;    corrNEs[1289]=-0.001; 
   ids[1290]='P723';    sts[1290]='PineCkMineCS2007';    lats[1290]=37.3804263129;    lngs[1290]=241.2752239298;    ves[1290]=-12.09;    vns[1290]=6.72;    vus[1290]=-6.89;    stddevNs[1290]=0.38;    stddevEs[1290]=0.7;    stddevUs[1290]=1.84;    corrNEs[1290]=0.007; 
   ids[1291]='P724';    sts[1291]='BirchimCynCS2008';    lats[1291]=37.4394772765;    lngs[1291]=241.4389314459;    ves[1291]=-8.47;    vns[1291]=6.99;    vus[1291]=-1.6;    stddevNs[1291]=0.36;    stddevEs[1291]=0.39;    stddevUs[1291]=1.81;    corrNEs[1291]=0.001; 
   ids[1292]='P725';    sts[1292]='SJExpRangeCN2006';    lats[1292]=37.0888952732;    lngs[1292]=240.2543942441;    ves[1292]=-10.87;    vns[1292]=9.11;    vus[1292]=0.74;    stddevNs[1292]=0.17;    stddevEs[1292]=0.18;    stddevUs[1292]=0.64;    corrNEs[1292]=-0.010; 
   ids[1293]='P726';    sts[1293]='WestgardPSCS2006';    lats[1293]=37.2805291578;    lngs[1293]=241.8533041369;    ves[1293]=-6.91;    vns[1293]=4.78;    vus[1293]=-1.18;    stddevNs[1293]=0.17;    stddevEs[1293]=0.18;    stddevUs[1293]=0.61;    corrNEs[1293]=-0.009; 
   ids[1294]='P727';    sts[1294]='CoyoteWarpCS2007';    lats[1294]=37.2735789432;    lngs[1294]=241.5332276119;    ves[1294]=-9.08;    vns[1294]=7.02;    vus[1294]=-1.36;    stddevNs[1294]=0.22;    stddevEs[1294]=0.21;    stddevUs[1294]=0.85;    corrNEs[1294]=-0.005; 
   ids[1295]='P728';    sts[1295]='Snowmass__CO2008';    lats[1295]=39.1777000621;    lngs[1295]=253.0260196141;    ves[1295]=-1.15;    vns[1295]=-0.56;    vus[1295]=-4.41;    stddevNs[1295]=0.86;    stddevEs[1295]=0.91;    stddevUs[1295]=2.39;    corrNEs[1295]=0.004; 
   ids[1296]='P729';    sts[1296]='Mesa_UnionCS2007';    lats[1296]=34.2630808698;    lngs[1296]=240.9039412602;    ves[1296]=-31.91;    vns[1296]=27.71;    vus[1296]=-6.93;    stddevNs[1296]=0.24;    stddevEs[1296]=0.38;    stddevUs[1296]=0.9;    corrNEs[1296]=-0.005; 
   ids[1297]='P730';    sts[1297]='BallardRdgCN2007';    lats[1297]=41.3591974680;    lngs[1297]=239.1717502804;    ves[1297]=-5.55;    vns[1297]=4.29;    vus[1297]=-0.52;    stddevNs[1297]=0.26;    stddevEs[1297]=0.27;    stddevUs[1297]=0.95;    corrNEs[1297]=-0.005; 
   ids[1298]='P731';    sts[1298]='LilJuniperCN2007';    lats[1298]=41.3325151976;    lngs[1298]=239.5272384411;    ves[1298]=-4.98;    vns[1298]=4.07;    vus[1298]=0.09;    stddevNs[1298]=0.25;    stddevEs[1298]=0.22;    stddevUs[1298]=0.9;    corrNEs[1298]=-0.004; 
   ids[1299]='P732';    sts[1299]='Vaughn_Pt_OR2008';    lats[1299]=43.3924995677;    lngs[1299]=236.1086302612;    ves[1299]=2.55;    vns[1299]=10.52;    vus[1299]=-2.8;    stddevNs[1299]=0.71;    stddevEs[1299]=0.66;    stddevUs[1299]=1.61;    corrNEs[1299]=-0.001; 
   ids[1300]='P733';    sts[1300]='GoldBeach_OR2008';    lats[1300]=42.4420159954;    lngs[1300]=235.5867029714;    ves[1300]=2.38;    vns[1300]=12.92;    vus[1300]=1.63;    stddevNs[1300]=2.95;    stddevEs[1300]=2.39;    stddevUs[1300]=2.91;    corrNEs[1300]=-0.000; 
   ids[1301]='P734';    sts[1301]='Brookings_OR2007';    lats[1301]=42.0766350885;    lngs[1301]=235.7067436629;    ves[1301]=0.67;    vns[1301]=11.34;    vus[1301]=1.15;    stddevNs[1301]=0.37;    stddevEs[1301]=0.26;    stddevUs[1301]=0.99;    corrNEs[1301]=-0.003; 
   ids[1302]='P735';    sts[1302]='King_Mtn__OR2008';    lats[1302]=42.6916101129;    lngs[1302]=236.7690014931;    ves[1302]=-2.82;    vns[1302]=7.42;    vus[1302]=-1.95;    stddevNs[1302]=0.97;    stddevEs[1302]=0.66;    stddevUs[1302]=2.18;    corrNEs[1302]=0.000; 
   ids[1303]='P736';    sts[1303]='Chiloquin_OR2008';    lats[1303]=42.5798303374;    lngs[1303]=238.1198724788;    ves[1303]=-2.43;    vns[1303]=4.92;    vus[1303]=-0.48;    stddevNs[1303]=0.34;    stddevEs[1303]=0.32;    stddevUs[1303]=1.16;    corrNEs[1303]=-0.004; 
   ids[1304]='P737';    sts[1304]='FlounceRocOR2008';    lats[1304]=42.7271422518;    lngs[1304]=237.3903650117;    ves[1304]=-2.14;    vns[1304]=5.66;    vus[1304]=1.2;    stddevNs[1304]=0.52;    stddevEs[1304]=0.72;    stddevUs[1304]=1.61;    corrNEs[1304]=0.005; 
   ids[1305]='P738';    sts[1305]='HartMtnNWROR2008';    lats[1305]=42.5460812687;    lngs[1305]=240.3412989300;    ves[1305]=-3.15;    vns[1305]=2.63;    vus[1305]=-0.82;    stddevNs[1305]=0.36;    stddevEs[1305]=0.33;    stddevUs[1305]=1.15;    corrNEs[1305]=-0.002; 
   ids[1306]='P739';    sts[1306]='McDermitt_OR2008';    lats[1306]=42.0201998849;    lngs[1306]=242.2745705214;    ves[1306]=-3.94;    vns[1306]=0.81;    vus[1306]=-1.6;    stddevNs[1306]=0.31;    stddevEs[1306]=0.28;    stddevUs[1306]=1.18;    corrNEs[1306]=-0.002; 
   ids[1307]='P740';    sts[1307]='PathfinderCS2007';    lats[1307]=33.5981945783;    lngs[1307]=243.4040253143;    ves[1307]=-19.53;    vns[1307]=18.55;    vus[1307]=0.87;    stddevNs[1307]=0.24;    stddevEs[1307]=0.36;    stddevUs[1307]=0.91;    corrNEs[1307]=-0.015; 
   ids[1308]='P741';    sts[1308]='SantaR086gCS2006';    lats[1308]=33.5574732536;    lngs[1308]=243.4689795592;    ves[1308]=-19.1;    vns[1308]=18.55;    vus[1308]=-0.71;    stddevNs[1308]=0.28;    stddevEs[1308]=0.29;    stddevUs[1308]=1.07;    corrNEs[1308]=-0.003; 
   ids[1309]='P742';    sts[1309]='FordRa087gCS2006';    lats[1309]=33.4955427843;    lngs[1309]=243.3974278709;    ves[1309]=-23.29;    vns[1309]=21.56;    vus[1309]=5.92;    stddevNs[1309]=0.81;    stddevEs[1309]=0.63;    stddevUs[1309]=2.4;    corrNEs[1309]=-0.025; 
   ids[1310]='P742';    sts[1310]='FordRa087gCS2006';    lats[1310]=33.4955427702;    lngs[1310]=243.3974278507;    ves[1310]=-23.29;    vns[1310]=21.56;    vus[1310]=5.92;    stddevNs[1310]=0.81;    stddevEs[1310]=0.63;    stddevUs[1310]=2.4;    corrNEs[1310]=-0.025; 
   ids[1311]='P744';    sts[1311]='ImperialVCCS2007';    lats[1311]=32.8293826831;    lngs[1311]=244.4915954105;    ves[1311]=-13.67;    vns[1311]=22.4;    vus[1311]=-0.78;    stddevNs[1311]=0.3;    stddevEs[1311]=0.39;    stddevUs[1311]=0.85;    corrNEs[1311]=-0.002; 
   ids[1312]='P776';    sts[1312]='GunstockMRNH2008';    lats[1312]=43.5432650853;    lngs[1312]=288.6214449777;    ves[1312]=0.98;    vns[1312]=-1.89;    vus[1312]=-3.68;    stddevNs[1312]=1.04;    stddevEs[1312]=0.74;    stddevUs[1312]=1.74;    corrNEs[1312]=-0.002; 
   ids[1313]='P777';    sts[1313]='RockyHillAR_2008';    lats[1313]=35.7026599651;    lngs[1313]=267.4545282495;    ves[1313]=-1.24;    vns[1313]=-1.27;    vus[1313]=-2.04;    stddevNs[1313]=0.48;    stddevEs[1313]=0.59;    stddevUs[1313]=1.75;    corrNEs[1313]=0.007; 
   ids[1314]='P778';    sts[1314]='MonteagleTN_2008';    lats[1314]=35.2403506096;    lngs[1314]=274.1851234545;    ves[1314]=-0.41;    vns[1314]=-0.96;    vus[1314]=-10.01;    stddevNs[1314]=0.67;    stddevEs[1314]=0.69;    stddevUs[1314]=2.08;    corrNEs[1314]=0.002; 
   ids[1315]='P779';    sts[1315]='PARI_Obs_NC_2008';    lats[1315]=35.2019413448;    lngs[1315]=277.1275217332;    ves[1315]=-1.09;    vns[1315]=-1.73;    vus[1315]=-6.65;    stddevNs[1315]=0.62;    stddevEs[1315]=0.44;    stddevUs[1315]=1.76;    corrNEs[1315]=0.002; 
   ids[1316]='P780';    sts[1316]='Cerrillos_PR2008';    lats[1316]=18.0750252136;    lngs[1316]=293.4208654812;    ves[1316]=15.96;    vns[1316]=3.12;    vus[1316]=2.61;    stddevNs[1316]=1.32;    stddevEs[1316]=1.51;    stddevUs[1316]=2.46;    corrNEs[1316]=-0.006; 
   ids[1317]='P781';    sts[1317]='NoModesto_CN2008';    lats[1317]=37.6989821067;    lngs[1317]=238.9284430734;    ves[1317]=-11.58;    vns[1317]=8.07;    vus[1317]=-1.83;    stddevNs[1317]=0.42;    stddevEs[1317]=0.52;    stddevUs[1317]=1.76;    corrNEs[1317]=-0.001; 
   ids[1318]='P782';    sts[1318]='CholameLSMCS2008';    lats[1318]=35.6938193783;    lngs[1318]=239.7838095083;    ves[1318]=-18.07;    vns[1318]=21.68;    vus[1318]=-2.5;    stddevNs[1318]=0.37;    stddevEs[1318]=0.4;    stddevUs[1318]=1.2;    corrNEs[1318]=-0.003; 
   ids[1319]='P783';    sts[1319]='Wanship2__UT2008';    lats[1319]=40.8075735072;    lngs[1319]=248.5852790803;    ves[1319]=-1.3;    vns[1319]=0.11;    vus[1319]=-1.53;    stddevNs[1319]=0.33;    stddevEs[1319]=0.32;    stddevUs[1319]=1.49;    corrNEs[1319]=0.003; 
   ids[1320]='P784';    sts[1320]='YorkMn040gCN2008';    lats[1320]=41.8308160193;    lngs[1320]=237.5795443331;    ves[1320]=-2.63;    vns[1320]=5.88;    vus[1320]=1.17;    stddevNs[1320]=0.81;    stddevEs[1320]=1.03;    stddevUs[1320]=2.46;    corrNEs[1320]=-0.000; 
   ids[1321]='P784';    sts[1321]='YorkMn040gCN2008';    lats[1321]=41.8308160156;    lngs[1321]=237.5795443243;    ves[1321]=-2.63;    vns[1321]=5.88;    vus[1321]=1.17;    stddevNs[1321]=0.81;    stddevEs[1321]=1.03;    stddevUs[1321]=2.46;    corrNEs[1321]=-0.000; 
   ids[1322]='P786';    sts[1322]='GasquetAirCN2008';    lats[1322]=41.8454849233;    lngs[1322]=236.0192106929;    ves[1322]=-1.85;    vns[1322]=10.26;    vus[1322]=3.37;    stddevNs[1322]=0.86;    stddevEs[1322]=1.09;    stddevUs[1322]=2.29;    corrNEs[1322]=0.000; 
   ids[1323]='P786';    sts[1323]='GasquetAirCN2008';    lats[1323]=41.8454849165;    lngs[1323]=236.0192106834;    ves[1323]=-1.85;    vns[1323]=10.26;    vus[1323]=3.37;    stddevNs[1323]=0.86;    stddevEs[1323]=1.09;    stddevUs[1323]=2.29;    corrNEs[1323]=0.000; 
   ids[1324]='P787';    sts[1324]='SJGrad058gCN2008';    lats[1324]=36.7994759336;    lngs[1324]=238.4191690990;    ves[1324]=-27.09;    vns[1324]=33.43;    vus[1324]=-0.24;    stddevNs[1324]=0.6;    stddevEs[1324]=0.87;    stddevUs[1324]=2.51;    corrNEs[1324]=-0.003; 
   ids[1325]='P788';    sts[1325]='Gabiln065gCN2008';    lats[1325]=36.7443351846;    lngs[1325]=238.5212381405;    ves[1325]=-29.07;    vns[1325]=33.7;    vus[1325]=-3.41;    stddevNs[1325]=0.73;    stddevEs[1325]=0.92;    stddevUs[1325]=2.47;    corrNEs[1325]=-0.001; 
   ids[1326]='P789';    sts[1326]='Varian073gCS2008';    lats[1326]=35.9465878179;    lngs[1326]=239.5282718466;    ves[1326]=-11.5;    vns[1326]=15.57;    vus[1326]=-1.47;    stddevNs[1326]=0.75;    stddevEs[1326]=0.81;    stddevUs[1326]=1.78;    corrNEs[1326]=0.001; 
   ids[1327]='P790';    sts[1327]='Flengt075gCS2008';    lats[1327]=35.9291479076;    lngs[1327]=239.4846245691;    ves[1327]=-27.02;    vns[1327]=31.05;    vus[1327]=-2.65;    stddevNs[1327]=0.55;    stddevEs[1327]=0.78;    stddevUs[1327]=2.02;    corrNEs[1327]=-0.002; 
   ids[1328]='P791';    sts[1328]='MtHoodSki_OR2009';    lats[1328]=45.3445309888;    lngs[1328]=238.3273173898;    ves[1328]=74.91;    vns[1328]=-2.22;    vus[1328]=6.55;    stddevNs[1328]=34.54;    stddevEs[1328]=166.43;    stddevUs[1328]=6.46;    corrNEs[1328]=-0.000; 
   ids[1329]='P792';    sts[1329]='WindyR202GWA2008';    lats[1329]=46.2446222475;    lngs[1329]=237.8631099789;    ves[1329]=-15.62;    vns[1329]=0.72;    vus[1329]=-2.26;    stddevNs[1329]=2.83;    stddevEs[1329]=2.61;    stddevUs[1329]=5.8;    corrNEs[1329]=-0.002; 
   ids[1330]='P793';    sts[1330]='Dinsmr935gCN2008';    lats[1330]=40.4787537650;    lngs[1330]=236.4268704231;    ves[1330]=-6.45;    vns[1330]=7.83;    vus[1330]=16.93;    stddevNs[1330]=45.14;    stddevEs[1330]=39.34;    stddevUs[1330]=10.69;    corrNEs[1330]=0.000; 
   ids[1331]='P794';    sts[1331]='AnthonyPk2CN2009';    lats[1331]=39.8456802076;    lngs[1331]=237.0362439811;    ves[1331]=-17.64;    vns[1331]=29.15;    vus[1331]=-25.1;    stddevNs[1331]=4.85;    stddevEs[1331]=4.54;    stddevUs[1331]=13.36;    corrNEs[1331]=-0.001; 
   ids[1332]='PABH';    sts[1332]='PABH_PNGA_WA1997';    lats[1332]=47.2127999148;    lngs[1332]=235.7954187832;    ves[1332]=13.5;    vns[1332]=11.33;    vus[1332]=-0.51;    stddevNs[1332]=0.15;    stddevEs[1332]=0.14;    stddevUs[1332]=0.51;    corrNEs[1332]=-0.010; 
   ids[1333]='PBOC';    sts[1333]='Prudhoe_Bay_2COR';    lats[1333]=70.2564029894;    lngs[1333]=211.6650858778;    ves[1333]=-0.91;    vns[1333]=0.29;    vus[1333]=-5.8;    stddevNs[1333]=0.35;    stddevEs[1333]=0.22;    stddevUs[1333]=1.28;    corrNEs[1333]=0.001; 
   ids[1334]='PBPP';    sts[1334]='PBPP_SCGN_CS2000';    lats[1334]=34.5082271138;    lngs[1334]=242.0774285049;    ves[1334]=-18.57;    vns[1334]=17.78;    vus[1334]=-1.53;    stddevNs[1334]=0.14;    stddevEs[1334]=0.15;    stddevUs[1334]=0.5;    corrNEs[1334]=-0.013; 
   ids[1335]='PCLN';    sts[1335]='Porcelain_BNORIS';    lats[1335]=44.7301065649;    lngs[1335]=249.3006041246;    ves[1335]=-109.79;    vns[1335]=109.89;    vus[1335]=-108.04;    stddevNs[1335]=20.54;    stddevEs[1335]=7.37;    stddevUs[1335]=4.47;    corrNEs[1335]=0.000; 
   ids[1336]='PCOL';    sts[1336]='PCOL_PNGA_WA2003';    lats[1336]=47.1720569718;    lngs[1336]=237.4291944962;    ves[1336]=4.08;    vns[1336]=4.27;    vus[1336]=-0.18;    stddevNs[1336]=0.18;    stddevEs[1336]=0.28;    stddevUs[1336]=0.79;    corrNEs[1336]=-0.004; 
   ids[1337]='PHLB';    sts[1337]='PHLB_SCGN_CS1999';    lats[1337]=34.9254233501;    lngs[1337]=242.3055212831;    ves[1337]=-10.85;    vns[1337]=13.53;    vus[1337]=-2.4;    stddevNs[1337]=0.15;    stddevEs[1337]=0.14;    stddevUs[1337]=0.42;    corrNEs[1337]=-0.014; 
   ids[1338]='PIE1';    sts[1338]='Pietown_VLBA_Sit';    lats[1338]=34.3015058216;    lngs[1338]=251.8810728981;    ves[1338]=-0.85;    vns[1338]=0.39;    vus[1338]=-0.2;    stddevNs[1338]=0.17;    stddevEs[1338]=0.3;    stddevUs[1338]=0.74;    corrNEs[1338]=-0.000; 
   ids[1339]='PIE1';    sts[1339]='Pietown_VLBA_Sit';    lats[1339]=34.3015058196;    lngs[1339]=251.8810728572;    ves[1339]=-0.85;    vns[1339]=0.39;    vus[1339]=-0.2;    stddevNs[1339]=0.17;    stddevEs[1339]=0.3;    stddevUs[1339]=0.74;    corrNEs[1339]=-0.000; 
   ids[1340]='PKRD';    sts[1340]='PKRD_SCGN_CS2000';    lats[1340]=34.0715605325;    lngs[1340]=241.7670961642;    ves[1340]=-27.25;    vns[1340]=23.4;    vus[1340]=-1.21;    stddevNs[1340]=0.2;    stddevEs[1340]=0.19;    stddevUs[1340]=0.76;    corrNEs[1340]=-0.007; 
   ids[1341]='PKRD';    sts[1341]='PKRD_SCGN_CS2000';    lats[1341]=34.0715605439;    lngs[1341]=241.7670961616;    ves[1341]=-27.25;    vns[1341]=23.4;    vus[1341]=-1.21;    stddevNs[1341]=0.2;    stddevEs[1341]=0.19;    stddevUs[1341]=0.76;    corrNEs[1341]=-0.007; 
   ids[1342]='POMM';    sts[1342]='POMM_SCGN_CN1999';    lats[1342]=35.9199110666;    lngs[1342]=239.5215649017;    ves[1342]=-23.49;    vns[1342]=26.64;    vus[1342]=-1.63;    stddevNs[1342]=0.33;    stddevEs[1342]=0.24;    stddevUs[1342]=0.92;    corrNEs[1342]=-0.004; 
   ids[1343]='POMM';    sts[1343]='POMM_SCGN_CN1999';    lats[1343]=35.9199116604;    lngs[1343]=239.5215636709;    ves[1343]=-23.49;    vns[1343]=26.64;    vus[1343]=-1.63;    stddevNs[1343]=0.33;    stddevEs[1343]=0.24;    stddevUs[1343]=0.92;    corrNEs[1343]=-0.004; 
   ids[1344]='PPBF';    sts[1344]='PPBF_SCGN_CS1998';    lats[1344]=33.8357270877;    lngs[1344]=242.8179111875;    ves[1344]=-23.09;    vns[1344]=23.42;    vus[1344]=-0.94;    stddevNs[1344]=0.13;    stddevEs[1344]=0.16;    stddevUs[1344]=0.46;    corrNEs[1344]=-0.013; 
   ids[1345]='PRKC';    sts[1345]='Porkchop_GeNORIS';    lats[1345]=44.7215522486;    lngs[1345]=249.2907750009;    ves[1345]=-51.02;    vns[1345]=-15.86;    vus[1345]=-109;    stddevNs[1345]=11.6;    stddevEs[1345]=7.7;    stddevUs[1345]=3.74;    corrNEs[1345]=0.000; 
   ids[1346]='PTSG';    sts[1346]='PTSG_PNGA_CN1999';    lats[1346]=41.7827415739;    lngs[1346]=235.7448009154;    ves[1346]=2.7;    vns[1346]=12.09;    vus[1346]=3.13;    stddevNs[1346]=0.19;    stddevEs[1346]=0.45;    stddevUs[1346]=0.91;    corrNEs[1346]=-0.003; 
   ids[1347]='PTSG';    sts[1347]='PTSG_PNGA_CN1999';    lats[1347]=41.7827415546;    lngs[1347]=235.7448008333;    ves[1347]=2.7;    vns[1347]=12.09;    vus[1347]=3.13;    stddevNs[1347]=0.19;    stddevEs[1347]=0.45;    stddevUs[1347]=0.91;    corrNEs[1347]=-0.003; 
   ids[1348]='PTSG';    sts[1348]='PTSG_PNGA_CN1999';    lats[1348]=41.7827415544;    lngs[1348]=235.7448008333;    ves[1348]=2.7;    vns[1348]=12.09;    vus[1348]=3.13;    stddevNs[1348]=0.19;    stddevEs[1348]=0.45;    stddevUs[1348]=0.91;    corrNEs[1348]=-0.003; 
   ids[1349]='PTSG';    sts[1349]='PTSG_PNGA_CN1999';    lats[1349]=41.7827415364;    lngs[1349]=235.7448008380;    ves[1349]=2.7;    vns[1349]=12.09;    vus[1349]=3.13;    stddevNs[1349]=0.19;    stddevEs[1349]=0.45;    stddevUs[1349]=0.91;    corrNEs[1349]=-0.003; 
   ids[1350]='PUPU';    sts[1350]='PUPU_PNGA_WA2001';    lats[1350]=47.4995594548;    lngs[1350]=237.9919199473;    ves[1350]=3.5;    vns[1350]=2.85;    vus[1350]=-0.43;    stddevNs[1350]=0.33;    stddevEs[1350]=0.27;    stddevUs[1350]=0.89;    corrNEs[1350]=0.000; 
   ids[1351]='PUPU';    sts[1351]='PUPU_PNGA_WA2001';    lats[1351]=47.4995594338;    lngs[1351]=237.9919199419;    ves[1351]=3.5;    vns[1351]=2.85;    vus[1351]=-0.43;    stddevNs[1351]=0.33;    stddevEs[1351]=0.27;    stddevUs[1351]=0.89;    corrNEs[1351]=0.000; 
   ids[1352]='PUPU';    sts[1352]='PUPU_PNGA_WA2001';    lats[1352]=47.4995594422;    lngs[1352]=237.9919199443;    ves[1352]=3.5;    vns[1352]=2.85;    vus[1352]=-0.43;    stddevNs[1352]=0.33;    stddevEs[1352]=0.27;    stddevUs[1352]=0.89;    corrNEs[1352]=0.000; 
   ids[1353]='PUPU';    sts[1353]='PUPU_PNGA_WA2001';    lats[1353]=47.4995594551;    lngs[1353]=237.9919199265;    ves[1353]=3.5;    vns[1353]=2.85;    vus[1353]=-0.43;    stddevNs[1353]=0.33;    stddevEs[1353]=0.27;    stddevUs[1353]=0.89;    corrNEs[1353]=0.000; 
   ids[1354]='PVRS';    sts[1354]='PVRS_SCGN_CS1998';    lats[1354]=33.7738633185;    lngs[1354]=241.6794115658;    ves[1354]=-28.83;    vns[1354]=28.83;    vus[1354]=-0.64;    stddevNs[1354]=0.15;    stddevEs[1354]=0.21;    stddevUs[1354]=0.57;    corrNEs[1354]=-0.011; 
   ids[1355]='QCY2';    sts[1355]='QCY2_BARD_CN2006';    lats[1355]=36.1610660041;    lngs[1355]=238.8626526961;    ves[1355]=-29.67;    vns[1355]=32.69;    vus[1355]=-0.1;    stddevNs[1355]=0.72;    stddevEs[1355]=0.52;    stddevUs[1355]=1.1;    corrNEs[1355]=-0.001; 
   ids[1356]='QCYN';    sts[1356]='QCYN_BARD_CN2003';    lats[1356]=36.1611580065;    lngs[1356]=238.8625165289;    ves[1356]=-32.25;    vns[1356]=35.03;    vus[1356]=-2.92;    stddevNs[1356]=1.17;    stddevEs[1356]=0.93;    stddevUs[1356]=1.62;    corrNEs[1356]=-0.000; 
   ids[1357]='QCYN';    sts[1357]='QCYN_BARD_CN2003';    lats[1357]=36.1611579680;    lngs[1357]=238.8625165128;    ves[1357]=-32.25;    vns[1357]=35.03;    vus[1357]=-2.92;    stddevNs[1357]=1.17;    stddevEs[1357]=0.93;    stddevUs[1357]=1.62;    corrNEs[1357]=-0.000; 
   ids[1358]='RAMT';    sts[1358]='RAMT_SCGN_CS2000';    lats[1358]=35.3387146528;    lngs[1358]=242.3166503535;    ves[1358]=-8.68;    vns[1358]=11.29;    vus[1358]=-2.12;    stddevNs[1358]=0.3;    stddevEs[1358]=0.47;    stddevUs[1358]=0.56;    corrNEs[1358]=-0.002; 
   ids[1359]='RAMT';    sts[1359]='RAMT_SCGN_CS2000';    lats[1359]=35.3387146436;    lngs[1359]=242.3166503506;    ves[1359]=-8.68;    vns[1359]=11.29;    vus[1359]=-2.12;    stddevNs[1359]=0.3;    stddevEs[1359]=0.47;    stddevUs[1359]=0.56;    corrNEs[1359]=-0.002; 
   ids[1360]='RCA2';    sts[1360]='RCA2_SCGN_CS2000';    lats[1360]=34.4999815421;    lngs[1360]=240.2800033426;    ves[1360]=-31.15;    vns[1360]=28.82;    vus[1360]=-0.47;    stddevNs[1360]=0.26;    stddevEs[1360]=0.2;    stddevUs[1360]=0.62;    corrNEs[1360]=-0.007; 
   ids[1361]='RCA2';    sts[1361]='RCA2_SCGN_CS2000';    lats[1361]=34.4999815275;    lngs[1361]=240.2800033438;    ves[1361]=-31.15;    vns[1361]=28.82;    vus[1361]=-0.47;    stddevNs[1361]=0.26;    stddevEs[1361]=0.2;    stddevUs[1361]=0.62;    corrNEs[1361]=-0.007; 
   ids[1362]='RDMT';    sts[1362]='RDMT_SCGN_CS1999';    lats[1362]=34.6439408693;    lngs[1362]=243.3753134595;    ves[1362]=-10.2;    vns[1362]=8.55;    vus[1362]=-1.26;    stddevNs[1362]=0.14;    stddevEs[1362]=0.19;    stddevUs[1362]=0.46;    corrNEs[1362]=-0.009; 
   ids[1363]='RDMT';    sts[1363]='RDMT_SCGN_CS1999';    lats[1363]=34.6439408690;    lngs[1363]=243.3753134464;    ves[1363]=-10.2;    vns[1363]=8.55;    vus[1363]=-1.26;    stddevNs[1363]=0.14;    stddevEs[1363]=0.19;    stddevUs[1363]=0.46;    corrNEs[1363]=-0.009; 
   ids[1364]='REDM';    sts[1364]='REDM_PNGA_OR1998';    lats[1364]=44.2597674294;    lngs[1364]=238.8521190573;    ves[1364]=-0.43;    vns[1364]=3.31;    vus[1364]=-1.84;    stddevNs[1364]=0.18;    stddevEs[1364]=0.18;    stddevUs[1364]=0.67;    corrNEs[1364]=-0.007; 
   ids[1365]='RESO';    sts[1365]='Resolute';    lats[1365]=-1683119.79543;    lngs[1365]=6129763.28751;    ves[1365]=-452;    vns[1365]=-63;    vus[1365]=-0.77;    stddevNs[1365]=0.15;    stddevEs[1365]=4.44;    stddevUs[1365]=0.45;    corrNEs[1365]=0.00032; 
   ids[1366]='RG17';    sts[1366]='RG17EmpireCO2006';    lats[1366]=39.7617593870;    lngs[1366]=254.3303550941;    ves[1366]=-0.4;    vns[1366]=-0.66;    vus[1366]=-2.41;    stddevNs[1366]=0.29;    stddevEs[1366]=0.36;    stddevUs[1366]=0.91;    corrNEs[1366]=-0.007; 
   ids[1367]='RHCL';    sts[1367]='RHCL_SCGN_CS1998';    lats[1367]=34.0190513424;    lngs[1367]=241.9738297852;    ves[1367]=-27.41;    vns[1367]=24.55;    vus[1367]=-0.82;    stddevNs[1367]=0.24;    stddevEs[1367]=0.16;    stddevUs[1367]=0.65;    corrNEs[1367]=-0.008; 
   ids[1368]='RNCH';    sts[1368]='RNCH_SCGN_CN2001';    lats[1368]=35.8999945861;    lngs[1368]=239.4751741195;    ves[1368]=-24.36;    vns[1368]=31.06;    vus[1368]=-0.36;    stddevNs[1368]=0.31;    stddevEs[1368]=0.34;    stddevUs[1368]=0.86;    corrNEs[1368]=-0.003; 
   ids[1369]='RNCH';    sts[1369]='RNCH_SCGN_CN2001';    lats[1369]=35.8999951230;    lngs[1369]=239.4751733289;    ves[1369]=-24.36;    vns[1369]=31.06;    vus[1369]=-0.36;    stddevNs[1369]=0.31;    stddevEs[1369]=0.34;    stddevUs[1369]=0.86;    corrNEs[1369]=-0.003; 
   ids[1370]='RSTP';    sts[1370]='RSTP_SCGN_CS1999';    lats[1370]=34.8750812105;    lngs[1370]=241.8070746387;    ves[1370]=-12.63;    vns[1370]=15.29;    vus[1370]=-2.04;    stddevNs[1370]=0.14;    stddevEs[1370]=0.13;    stddevUs[1370]=0.48;    corrNEs[1370]=-0.017; 
   ids[1371]='RSVY';    sts[1371]='RSVY_SCGN_CS2000';    lats[1371]=34.5414767476;    lngs[1371]=240.8155656003;    ves[1371]=-28.09;    vns[1371]=23.18;    vus[1371]=-0.97;    stddevNs[1371]=0.15;    stddevEs[1371]=0.14;    stddevUs[1371]=0.62;    corrNEs[1371]=-0.015; 
   ids[1372]='RUBY';    sts[1372]='RUBY_BRGN_NV1996';    lats[1372]=40.6172102124;    lngs[1372]=244.8771655379;    ves[1372]=-4.02;    vns[1372]=-0.26;    vus[1372]=-0.31;    stddevNs[1372]=0.16;    stddevEs[1372]=0.13;    stddevUs[1372]=0.85;    corrNEs[1372]=-0.010; 
   ids[1373]='S300';    sts[1373]='S300_BARD_CN1998';    lats[1373]=37.6665076045;    lngs[1373]=238.4417318224;    ves[1373]=-12.67;    vns[1373]=7.03;    vus[1373]=-0.36;    stddevNs[1373]=0.45;    stddevEs[1373]=0.22;    stddevUs[1373]=0.79;    corrNEs[1373]=0.004; 
   ids[1374]='S300';    sts[1374]='S300_BARD_CN1998';    lats[1374]=37.6665076480;    lngs[1374]=238.4417318379;    ves[1374]=-12.67;    vns[1374]=7.03;    vus[1374]=-0.36;    stddevNs[1374]=0.45;    stddevEs[1374]=0.22;    stddevUs[1374]=0.79;    corrNEs[1374]=0.004; 
   ids[1375]='S300';    sts[1375]='S300_BARD_CN1998';    lats[1375]=37.6665076421;    lngs[1375]=238.4417318340;    ves[1375]=-12.67;    vns[1375]=7.03;    vus[1375]=-0.36;    stddevNs[1375]=0.45;    stddevEs[1375]=0.22;    stddevUs[1375]=0.79;    corrNEs[1375]=0.004; 
   ids[1376]='SBCC';    sts[1376]='SBCC_SCGN_CS1999';    lats[1376]=33.5530005935;    lngs[1376]=242.3385137690;    ves[1376]=-27.51;    vns[1376]=27.46;    vus[1376]=-2.65;    stddevNs[1376]=0.16;    stddevEs[1376]=0.14;    stddevUs[1376]=0.5;    corrNEs[1376]=-0.014; 
   ids[1377]='SBCC';    sts[1377]='SBCC_SCGN_CS1999';    lats[1377]=33.5530005728;    lngs[1377]=242.3385137768;    ves[1377]=-27.51;    vns[1377]=27.46;    vus[1377]=-2.65;    stddevNs[1377]=0.16;    stddevEs[1377]=0.14;    stddevUs[1377]=0.5;    corrNEs[1377]=-0.014; 
   ids[1378]='SBCC';    sts[1378]='SBCC_SCGN_CS1999';    lats[1378]=33.5530005738;    lngs[1378]=242.3385137759;    ves[1378]=-27.51;    vns[1378]=27.46;    vus[1378]=-2.65;    stddevNs[1378]=0.16;    stddevEs[1378]=0.14;    stddevUs[1378]=0.5;    corrNEs[1378]=-0.014; 
   ids[1379]='SC00';    sts[1379]='SC00_PNGA_WA2000';    lats[1379]=46.9509248162;    lngs[1379]=239.2753908236;    ves[1379]=2.81;    vns[1379]=1.79;    vus[1379]=-0.09;    stddevNs[1379]=0.13;    stddevEs[1379]=0.64;    stddevUs[1379]=0.6;    corrNEs[1379]=-0.002; 
   ids[1380]='SC01';    sts[1380]='SC01_NMTU_NM2001';    lats[1380]=34.0679528860;    lngs[1380]=253.0334576059;    ves[1380]=-1.29;    vns[1380]=-1.14;    vus[1380]=-0.72;    stddevNs[1380]=0.24;    stddevEs[1380]=0.17;    stddevUs[1380]=0.53;    corrNEs[1380]=-0.002; 
   ids[1381]='SC02';    sts[1381]='SC02_PNGA_WA2001';    lats[1381]=48.5461949021;    lngs[1381]=236.9923898992;    ves[1381]=3.77;    vns[1381]=3.04;    vus[1381]=-0.2;    stddevNs[1381]=0.18;    stddevEs[1381]=0.25;    stddevUs[1381]=0.59;    corrNEs[1381]=-0.005; 
   ids[1382]='SC03';    sts[1382]='SC03_PNGA_WA2002';    lats[1382]=47.8165872162;    lngs[1382]=236.2942624706;    ves[1382]=6.89;    vns[1382]=6.52;    vus[1382]=3.42;    stddevNs[1382]=0.23;    stddevEs[1382]=0.3;    stddevUs[1382]=0.71;    corrNEs[1382]=-0.003; 
   ids[1383]='SC04';    sts[1383]='SC04_PNGA_WA2002';    lats[1383]=48.9231640750;    lngs[1383]=236.2958710543;    ves[1383]=4.46;    vns[1383]=3.7;    vus[1383]=2.02;    stddevNs[1383]=0.19;    stddevEs[1383]=0.18;    stddevUs[1383]=0.6;    corrNEs[1383]=-0.005; 
   ids[1384]='SCH2';    sts[1384]='SCHEFFERVILLE-2';    lats[1384]=54.8320895963;    lngs[1384]=293.1673838246;    ves[1384]=3.59;    vns[1384]=-0.23;    vus[1384]=12.11;    stddevNs[1384]=0.74;    stddevEs[1384]=0.48;    stddevUs[1384]=1.15;    corrNEs[1384]=0.001; 
   ids[1385]='SCH2';    sts[1385]='SCHEFFERVILLE-2';    lats[1385]=54.8320894936;    lngs[1385]=293.1673840447;    ves[1385]=3.59;    vns[1385]=-0.23;    vus[1385]=12.11;    stddevNs[1385]=0.74;    stddevEs[1385]=0.48;    stddevUs[1385]=1.15;    corrNEs[1385]=0.001; 
   ids[1386]='SCIA';    sts[1386]='SCIA_SCGN_CS1999';    lats[1386]=34.6074470371;    lngs[1386]=242.6116989540;    ves[1386]=-13.18;    vns[1386]=13.82;    vus[1386]=-2.07;    stddevNs[1386]=0.14;    stddevEs[1386]=0.13;    stddevUs[1386]=0.5;    corrNEs[1386]=-0.015; 
   ids[1387]='SDHL';    sts[1387]='SDHL_SCGN_CS2001';    lats[1387]=34.2550113703;    lngs[1387]=243.7211025731;    ves[1387]=-5.56;    vns[1387]=7.62;    vus[1387]=0.03;    stddevNs[1387]=0.13;    stddevEs[1387]=0.14;    stddevUs[1387]=0.59;    corrNEs[1387]=-0.013; 
   ids[1388]='SEAT';    sts[1388]='SEAT_PNGA_WA1995';    lats[1388]=47.6539771676;    lngs[1388]=237.6905243785;    ves[1388]=3.45;    vns[1388]=3.53;    vus[1388]=-0.49;    stddevNs[1388]=0.14;    stddevEs[1388]=0.19;    stddevUs[1388]=0.61;    corrNEs[1388]=-0.007; 
   ids[1389]='SEDR';    sts[1389]='SEDR_PNGA_WA1997';    lats[1389]=48.5215579522;    lngs[1389]=237.7761525071;    ves[1389]=2.65;    vns[1389]=2.49;    vus[1389]=-1.83;    stddevNs[1389]=0.18;    stddevEs[1389]=0.18;    stddevUs[1389]=0.58;    corrNEs[1389]=-0.005; 
   ids[1390]='SELD';    sts[1390]='SELD_AKDA_AK2000';    lats[1390]=59.4457097777;    lngs[1390]=208.2933292173;    ves[1390]=0.8;    vns[1390]=-7.72;    vus[1390]=10.42;    stddevNs[1390]=0.55;    stddevEs[1390]=0.67;    stddevUs[1390]=1.5;    corrNEs[1390]=-0.001; 
   ids[1391]='SELD';    sts[1391]='SELD_AKDA_AK2000';    lats[1391]=59.4457097769;    lngs[1391]=208.2933292166;    ves[1391]=0.8;    vns[1391]=-7.72;    vus[1391]=10.42;    stddevNs[1391]=0.55;    stddevEs[1391]=0.67;    stddevUs[1391]=1.5;    corrNEs[1391]=-0.001; 
   ids[1392]='SELD';    sts[1392]='SELD_AKDA_AK2000';    lats[1392]=59.4457097402;    lngs[1392]=208.2933292462;    ves[1392]=0.8;    vns[1392]=-7.72;    vus[1392]=10.42;    stddevNs[1392]=0.55;    stddevEs[1392]=0.67;    stddevUs[1392]=1.5;    corrNEs[1392]=-0.001; 
   ids[1393]='SFDM';    sts[1393]='SFDM_SCGN_CS1999';    lats[1393]=34.4598057576;    lngs[1393]=241.2454935906;    ves[1393]=-26.55;    vns[1393]=23.34;    vus[1393]=0.67;    stddevNs[1393]=0.16;    stddevEs[1393]=0.15;    stddevUs[1393]=0.59;    corrNEs[1393]=-0.014; 
   ids[1394]='SFDM';    sts[1394]='SFDM_SCGN_CS1999';    lats[1394]=34.4598057467;    lngs[1394]=241.2454935844;    ves[1394]=-26.55;    vns[1394]=23.34;    vus[1394]=0.67;    stddevNs[1394]=0.16;    stddevEs[1394]=0.15;    stddevUs[1394]=0.59;    corrNEs[1394]=-0.014; 
   ids[1395]='SG27';    sts[1395]='SG27_AKDA_AK2002';    lats[1395]=71.3228950954;    lngs[1395]=203.3896685437;    ves[1395]=-0.45;    vns[1395]=-1.35;    vus[1395]=-2.73;    stddevNs[1395]=0.42;    stddevEs[1395]=0.38;    stddevUs[1395]=0.92;    corrNEs[1395]=0.000; 
   ids[1396]='SG27';    sts[1396]='SG27_AKDA_AK2002';    lats[1396]=71.3228950310;    lngs[1396]=203.3896684423;    ves[1396]=-0.45;    vns[1396]=-1.35;    vus[1396]=-2.73;    stddevNs[1396]=0.42;    stddevEs[1396]=0.38;    stddevUs[1396]=0.92;    corrNEs[1396]=0.000; 
   ids[1397]='SHIN';    sts[1397]='SHIN_BRGN_CN1996';    lats[1397]=40.5916789779;    lngs[1397]=239.7749564478;    ves[1397]=-6.5;    vns[1397]=4.09;    vus[1397]=0.32;    stddevNs[1397]=0.13;    stddevEs[1397]=0.16;    stddevUs[1397]=0.78;    corrNEs[1397]=-0.013; 
   ids[1398]='SHLD';    sts[1398]='SHLD_PNGA_NV1999';    lats[1398]=41.8683674141;    lngs[1398]=240.9843155542;    ves[1398]=-4.09;    vns[1398]=2.07;    vus[1398]=-0.16;    stddevNs[1398]=0.36;    stddevEs[1398]=0.52;    stddevUs[1398]=0.86;    corrNEs[1398]=-0.000; 
   ids[1399]='SHLD';    sts[1399]='SHLD_PNGA_NV1999';    lats[1399]=41.8683674122;    lngs[1399]=240.9843155504;    ves[1399]=-4.09;    vns[1399]=2.07;    vus[1399]=-0.16;    stddevNs[1399]=0.36;    stddevEs[1399]=0.52;    stddevUs[1399]=0.86;    corrNEs[1399]=-0.000; 
   ids[1400]='SHLD';    sts[1400]='SHLD_PNGA_NV1999';    lats[1400]=41.8683673888;    lngs[1400]=240.9843155089;    ves[1400]=-4.09;    vns[1400]=2.07;    vus[1400]=-0.16;    stddevNs[1400]=0.36;    stddevEs[1400]=0.52;    stddevUs[1400]=0.86;    corrNEs[1400]=-0.000; 
   ids[1401]='SHOS';    sts[1401]='SHOS_BRGN_CS1999';    lats[1401]=35.9713455582;    lngs[1401]=243.7010154584;    ves[1401]=-4.31;    vns[1401]=1.1;    vus[1401]=-0.58;    stddevNs[1401]=0.17;    stddevEs[1401]=0.14;    stddevUs[1401]=0.75;    corrNEs[1401]=-0.011; 
   ids[1402]='SHOS';    sts[1402]='SHOS_BRGN_CS1999';    lats[1402]=35.9713455608;    lngs[1402]=243.7010154578;    ves[1402]=-4.31;    vns[1402]=1.1;    vus[1402]=-0.58;    stddevNs[1402]=0.17;    stddevEs[1402]=0.14;    stddevUs[1402]=0.75;    corrNEs[1402]=-0.011; 
   ids[1403]='SKYB';    sts[1403]='SKYB_SCGN_CS1999';    lats[1403]=34.4386241984;    lngs[1403]=241.5213707650;    ves[1403]=-23.76;    vns[1403]=20.87;    vus[1403]=-1.07;    stddevNs[1403]=0.22;    stddevEs[1403]=0.5;    stddevUs[1403]=0.64;    corrNEs[1403]=-0.003; 
   ids[1404]='SLAC';    sts[1404]='SLAC_BARD_CN2002';    lats[1404]=37.4165177659;    lngs[1404]=237.7957366634;    ves[1404]=-24.1;    vns[1404]=27.84;    vus[1404]=-2.89;    stddevNs[1404]=0.18;    stddevEs[1404]=0.17;    stddevUs[1404]=0.7;    corrNEs[1404]=-0.012; 
   ids[1405]='SLID';    sts[1405]='SLID_BRGN_NV1996';    lats[1405]=39.3142777298;    lngs[1405]=240.1157122625;    ves[1405]=-8.73;    vns[1405]=5.97;    vus[1405]=0.3;    stddevNs[1405]=0.21;    stddevEs[1405]=0.22;    stddevUs[1405]=0.74;    corrNEs[1405]=-0.006; 
   ids[1406]='SLID';    sts[1406]='SLID_BRGN_NV1996';    lats[1406]=39.3142777484;    lngs[1406]=240.1157122472;    ves[1406]=-8.73;    vns[1406]=5.97;    vus[1406]=0.3;    stddevNs[1406]=0.21;    stddevEs[1406]=0.22;    stddevUs[1406]=0.74;    corrNEs[1406]=-0.006; 
   ids[1407]='SLID';    sts[1407]='SLID_BRGN_NV1996';    lats[1407]=39.3142777216;    lngs[1407]=240.1157122555;    ves[1407]=-8.73;    vns[1407]=5.97;    vus[1407]=0.3;    stddevNs[1407]=0.21;    stddevEs[1407]=0.22;    stddevUs[1407]=0.74;    corrNEs[1407]=-0.006; 
   ids[1408]='SLID';    sts[1408]='SLID_BRGN_NV1996';    lats[1408]=39.3142777251;    lngs[1408]=240.1157122303;    ves[1408]=-8.73;    vns[1408]=5.97;    vus[1408]=0.3;    stddevNs[1408]=0.21;    stddevEs[1408]=0.22;    stddevUs[1408]=0.74;    corrNEs[1408]=-0.006; 
   ids[1409]='SLMS';    sts[1409]='SLMS_SCGN_CS1999';    lats[1409]=33.2922279849;    lngs[1409]=244.0221584630;    ves[1409]=-13.53;    vns[1409]=15.6;    vus[1409]=-1.43;    stddevNs[1409]=0.15;    stddevEs[1409]=0.18;    stddevUs[1409]=0.46;    corrNEs[1409]=-0.009; 
   ids[1410]='SMEL';    sts[1410]='SMEL_BRGN_UT1996';    lats[1410]=39.4256436976;    lngs[1410]=247.1550723391;    ves[1410]=-3.6;    vns[1410]=-0.28;    vus[1410]=-0.35;    stddevNs[1410]=0.16;    stddevEs[1410]=0.13;    stddevUs[1410]=0.88;    corrNEs[1410]=-0.008; 
   ids[1411]='SMM1';    sts[1411]='SummitCMP_GL2009';    lats[1411]=72.5795874694;    lngs[1411]=321.5406475181;    ves[1411]=-1676.34;    vns[1411]=149.57;    vus[1411]=-959.6;    stddevNs[1411]=4.68;    stddevEs[1411]=4.3;    stddevUs[1411]=10.74;    corrNEs[1411]=-0.002; 
   ids[1412]='SNI1';    sts[1412]='SNI1_SCGN_CS1995';    lats[1412]=33.2478756538;    lngs[1412]=240.4756346004;    ves[1412]=-32.4;    vns[1412]=34.22;    vus[1412]=-2.99;    stddevNs[1412]=0.14;    stddevEs[1412]=0.14;    stddevUs[1412]=0.74;    corrNEs[1412]=-0.016; 
   ids[1413]='SOMT';    sts[1413]='SOMT_SCGN_CS2000';    lats[1413]=34.3199397569;    lngs[1413]=240.9356671506;    ves[1413]=-28.75;    vns[1413]=28.49;    vus[1413]=-1.08;    stddevNs[1413]=0.3;    stddevEs[1413]=0.23;    stddevUs[1413]=0.63;    corrNEs[1413]=-0.006; 
   ids[1414]='SOMT';    sts[1414]='SOMT_SCGN_CS2000';    lats[1414]=34.3199397322;    lngs[1414]=240.9356671369;    ves[1414]=-28.75;    vns[1414]=28.49;    vus[1414]=-1.08;    stddevNs[1414]=0.3;    stddevEs[1414]=0.23;    stddevUs[1414]=0.63;    corrNEs[1414]=-0.006; 
   ids[1415]='SPIC';    sts[1415]='SPIC_BRGN_UT2003';    lats[1415]=39.3062144145;    lngs[1415]=247.8725251297;    ves[1415]=-3.17;    vns[1415]=0.19;    vus[1415]=-1.25;    stddevNs[1415]=0.12;    stddevEs[1415]=0.14;    stddevUs[1415]=0.45;    corrNEs[1415]=-0.010; 
   ids[1416]='SPMS';    sts[1416]='SPMS_SCGN_CS1998';    lats[1416]=33.9926546374;    lngs[1416]=242.1512247740;    ves[1416]=-26.11;    vns[1416]=23.81;    vus[1416]=-2.27;    stddevNs[1416]=0.17;    stddevEs[1416]=0.14;    stddevUs[1416]=0.64;    corrNEs[1416]=-0.012; 
   ids[1417]='SRS1';    sts[1417]='SRS1_SCGN_CS2000';    lats[1417]=34.0043363238;    lngs[1417]=239.9347789914;    ves[1417]=-31.58;    vns[1417]=34.89;    vus[1417]=-2.39;    stddevNs[1417]=0.15;    stddevEs[1417]=0.14;    stddevUs[1417]=0.78;    corrNEs[1417]=-0.017; 
   ids[1418]='STJO';    sts[1418]='St._Johns';    lats[1418]=4686757.86408;    lngs[1418]=47.5952397514;    ves[1418]=0.13;    vns[1418]=-808;    vus[1418]=0.44;    stddevNs[1418]=0.19;    stddevEs[1418]=0.2;    stddevUs[1418]=0.25;    corrNEs[1418]=0.00074; 
   ids[1419]='STMB';    sts[1419]='Steamboat_GNORIS';    lats[1419]=44.7241113355;    lngs[1419]=249.2971158876;    ves[1419]=-3.69;    vns[1419]=-4.97;    vus[1419]=-6.93;    stddevNs[1419]=12.28;    stddevEs[1419]=2.58;    stddevUs[1419]=3.26;    corrNEs[1419]=0.000; 
   ids[1420]='SUMM';    sts[1420]='SummitCMP_GL2006';    lats[1420]=72.5791900861;    lngs[1420]=321.5426322172;    ves[1420]=-1686.57;    vns[1420]=102.22;    vus[1420]=-688.74;    stddevNs[1420]=1.12;    stddevEs[1420]=1.05;    stddevUs[1420]=1.93;    corrNEs[1420]=0.000; 
   ids[1421]='SUTB';    sts[1421]='SUTB_BARD_CN1997';    lats[1421]=39.2058364287;    lngs[1421]=238.1794008359;    ves[1421]=-11.31;    vns[1421]=6.95;    vus[1421]=-1.81;    stddevNs[1421]=0.51;    stddevEs[1421]=0.2;    stddevUs[1421]=0.6;    corrNEs[1421]=-0.000; 
   ids[1422]='TBLP';    sts[1422]='TBLP_SCGN_CN2001';    lats[1422]=35.9174110144;    lngs[1422]=239.6396638218;    ves[1422]=-15.17;    vns[1422]=17.78;    vus[1422]=0.26;    stddevNs[1422]=0.23;    stddevEs[1422]=0.25;    stddevUs[1422]=1.14;    corrNEs[1422]=-0.007; 
   ids[1423]='TBLP';    sts[1423]='TBLP_SCGN_CN2001';    lats[1423]=35.9174115158;    lngs[1423]=239.6396630300;    ves[1423]=-15.17;    vns[1423]=17.78;    vus[1423]=0.26;    stddevNs[1423]=0.23;    stddevEs[1423]=0.25;    stddevUs[1423]=1.14;    corrNEs[1423]=-0.007; 
   ids[1424]='THCP';    sts[1424]='THCP_SCGN_CS2000';    lats[1424]=35.1581827574;    lngs[1424]=241.5854287352;    ves[1424]=-12.65;    vns[1424]=12.63;    vus[1424]=-0.82;    stddevNs[1424]=0.14;    stddevEs[1424]=0.13;    stddevUs[1424]=0.5;    corrNEs[1424]=-0.017; 
   ids[1425]='THU3';    sts[1425]='THULE';    lats[1425]=-1389088.05517;    lngs[1425]=6180979.25093;    ves[1425]=-738;    vns[1425]=490;    vus[1425]=-0.88;    stddevNs[1425]=-1.61;    stddevEs[1425]=0.48;    stddevUs[1425]=0.16;    corrNEs[1425]=0.00015; 
   ids[1426]='TJRN';    sts[1426]='TJRN_SCGN_CS2000';    lats[1426]=34.4834745985;    lngs[1426]=239.8674369309;    ves[1426]=-31.52;    vns[1426]=32.72;    vus[1426]=-0.78;    stddevNs[1426]=0.15;    stddevEs[1426]=0.14;    stddevUs[1426]=0.7;    corrNEs[1426]=-0.018; 
   ids[1427]='TOIY';    sts[1427]='TOIY_BRGN_NV2003';    lats[1427]=39.5421730462;    lngs[1427]=242.9505925274;    ves[1427]=-4.2;    vns[1427]=0.68;    vus[1427]=-1.06;    stddevNs[1427]=0.13;    stddevEs[1427]=0.13;    stddevUs[1427]=0.44;    corrNEs[1427]=-0.014; 
   ids[1428]='TONO';    sts[1428]='TONO_BRGN_NV1999';    lats[1428]=38.0971940192;    lngs[1428]=242.8159594811;    ves[1428]=-4.52;    vns[1428]=0.99;    vus[1428]=-0.77;    stddevNs[1428]=0.16;    stddevEs[1428]=0.13;    stddevUs[1428]=0.84;    corrNEs[1428]=-0.012; 
   ids[1429]='TOST';    sts[1429]='TOST_SCGN_CS1999';    lats[1429]=34.2479563789;    lngs[1429]=241.1633352697;    ves[1429]=-28.12;    vns[1429]=29.74;    vus[1429]=-4.62;    stddevNs[1429]=0.24;    stddevEs[1429]=0.14;    stddevUs[1429]=0.63;    corrNEs[1429]=-0.010; 
   ids[1430]='TPW2';    sts[1430]='TPW2_PNGA_OR2000';    lats[1430]=46.2073730150;    lngs[1430]=236.2316368728;    ves[1430]=6.99;    vns[1430]=7.45;    vus[1430]=0.94;    stddevNs[1430]=0.2;    stddevEs[1430]=0.32;    stddevUs[1430]=0.87;    corrNEs[1430]=-0.003; 
   ids[1431]='TRND';    sts[1431]='TRND_PNGA_CN1999';    lats[1431]=41.0538864897;    lngs[1431]=235.8491336246;    ves[1431]=2.77;    vns[1431]=16.38;    vus[1431]=-1.21;    stddevNs[1431]=0.27;    stddevEs[1431]=0.52;    stddevUs[1431]=0.93;    corrNEs[1431]=-0.003; 
   ids[1432]='TRND';    sts[1432]='TRND_PNGA_CN1999';    lats[1432]=41.0538864980;    lngs[1432]=235.8491335935;    ves[1432]=2.77;    vns[1432]=16.38;    vus[1432]=-1.21;    stddevNs[1432]=0.27;    stddevEs[1432]=0.52;    stddevUs[1432]=0.93;    corrNEs[1432]=-0.003; 
   ids[1433]='TSWY';    sts[1433]='TSWY_EBRY_WY2001';    lats[1433]=43.6740899403;    lngs[1433]=249.4025239996;    ves[1433]=-1.97;    vns[1433]=-0.86;    vus[1433]=-1.88;    stddevNs[1433]=0.15;    stddevEs[1433]=0.14;    stddevUs[1433]=1.05;    corrNEs[1433]=-0.006; 
   ids[1434]='TUF1';    sts[1434]='Tuff1______NORIS';    lats[1434]=44.7294253298;    lngs[1434]=249.2942690659;    ves[1434]=-2.56;    vns[1434]=-7.46;    vus[1434]=-0.54;    stddevNs[1434]=3.99;    stddevEs[1434]=2.45;    stddevUs[1434]=3.92;    corrNEs[1434]=0.001; 
   ids[1435]='TUNG';    sts[1435]='TUNG_BRGN_NV1996';    lats[1435]=40.4032012173;    lngs[1435]=241.7424953923;    ves[1435]=-5.9;    vns[1435]=2;    vus[1435]=1.45;    stddevNs[1435]=0.2;    stddevEs[1435]=0.15;    stddevUs[1435]=0.92;    corrNEs[1435]=-0.007; 
   ids[1436]='TUNG';    sts[1436]='TUNG_BRGN_NV1996';    lats[1436]=40.4032012106;    lngs[1436]=241.7424953948;    ves[1436]=-5.9;    vns[1436]=2;    vus[1436]=1.45;    stddevNs[1436]=0.2;    stddevEs[1436]=0.15;    stddevUs[1436]=0.92;    corrNEs[1436]=-0.007; 
   ids[1437]='TWHL';    sts[1437]='TWHL_PNGA_WA2001';    lats[1437]=47.0159056624;    lngs[1437]=237.0771248500;    ves[1437]=4.11;    vns[1437]=5.27;    vus[1437]=-0.32;    stddevNs[1437]=0.16;    stddevEs[1437]=0.21;    stddevUs[1437]=0.57;    corrNEs[1437]=-0.006; 
   ids[1438]='UPSA';    sts[1438]='UPSA_BRGN_NV1996';    lats[1438]=39.6271122884;    lngs[1438]=241.1977203956;    ves[1438]=-6.56;    vns[1438]=2.8;    vus[1438]=-0.06;    stddevNs[1438]=0.13;    stddevEs[1438]=0.13;    stddevUs[1438]=0.63;    corrNEs[1438]=-0.016; 
   ids[1439]='USGC';    sts[1439]='USGC_SCGN_CS1998';    lats[1439]=33.0300640501;    lngs[1439]=243.9146657226;    ves[1439]=-24.57;    vns[1439]=23.72;    vus[1439]=-1.29;    stddevNs[1439]=0.14;    stddevEs[1439]=0.13;    stddevUs[1439]=0.4;    corrNEs[1439]=-0.014; 
   ids[1440]='USLO';    sts[1440]='USLO_SCGN_CS2000';    lats[1440]=35.3118047130;    lngs[1440]=239.3389067382;    ves[1440]=-29.1;    vns[1440]=33.78;    vus[1440]=0.08;    stddevNs[1440]=0.3;    stddevEs[1440]=0.37;    stddevUs[1440]=1.49;    corrNEs[1440]=-0.003; 
   ids[1441]='USLO';    sts[1441]='USLO_SCGN_CS2000';    lats[1441]=35.3118046564;    lngs[1441]=239.3389067040;    ves[1441]=-29.1;    vns[1441]=33.78;    vus[1441]=0.08;    stddevNs[1441]=0.3;    stddevEs[1441]=0.37;    stddevUs[1441]=1.49;    corrNEs[1441]=-0.003; 
   ids[1442]='VDCY';    sts[1442]='VDCY_SCGN_CS2000';    lats[1442]=34.1785658521;    lngs[1442]=241.7799999371;    ves[1442]=-26.1;    vns[1442]=23.63;    vus[1442]=-3.6;    stddevNs[1442]=0.14;    stddevEs[1442]=0.14;    stddevUs[1442]=0.43;    corrNEs[1442]=-0.014; 
   ids[1443]='VIMT';    sts[1443]='VIMT_SCGN_CS2000';    lats[1443]=34.1264481905;    lngs[1443]=241.4855865895;    ves[1443]=-28.06;    vns[1443]=26.85;    vus[1443]=-1.1;    stddevNs[1443]=0.2;    stddevEs[1443]=0.23;    stddevUs[1443]=0.59;    corrNEs[1443]=-0.006; 
   ids[1444]='VIMT';    sts[1444]='VIMT_SCGN_CS2000';    lats[1444]=34.1264481903;    lngs[1444]=241.4855865819;    ves[1444]=-28.06;    vns[1444]=26.85;    vus[1444]=-1.1;    stddevNs[1444]=0.2;    stddevEs[1444]=0.23;    stddevUs[1444]=0.59;    corrNEs[1444]=-0.006; 
   ids[1445]='VNCO';    sts[1445]='VNCO_SCGN_CS2000';    lats[1445]=34.2757655639;    lngs[1445]=240.7623305085;    ves[1445]=-31.94;    vns[1445]=28.14;    vus[1445]=-6.8;    stddevNs[1445]=0.49;    stddevEs[1445]=0.42;    stddevUs[1445]=1.15;    corrNEs[1445]=-0.002; 
   ids[1446]='VNCX';    sts[1446]='VNCX_SCGN_CS1998';    lats[1446]=34.2931934304;    lngs[1446]=241.5154600143;    ves[1446]=-26.36;    vns[1446]=24.08;    vus[1446]=-1.22;    stddevNs[1446]=0.17;    stddevEs[1446]=0.14;    stddevUs[1446]=0.47;    corrNEs[1446]=-0.012; 
   ids[1447]='VNDP';    sts[1447]='VNDP_SCGN_CS1992';    lats[1447]=34.5563121707;    lngs[1447]=239.3835489138;    ves[1447]=-32.46;    vns[1447]=34.19;    vus[1447]=-1.48;    stddevNs[1447]=0.15;    stddevEs[1447]=0.14;    stddevUs[1447]=0.62;    corrNEs[1447]=-0.018; 
   ids[1448]='WEIR';    sts[1448]='Tantalus_WeNORIS';    lats[1448]=44.7337554663;    lngs[1448]=249.2849429884;    ves[1448]=-11.22;    vns[1448]=12.21;    vus[1448]=-81.41;    stddevNs[1448]=22.35;    stddevEs[1448]=28.79;    stddevUs[1448]=4.76;    corrNEs[1448]=0.000; 
   ids[1449]='WES2';    sts[1449]='WESTFORD';    lats[1449]=-4458089.50170;    lngs[1449]=4296046.02123;    ves[1449]=-871;    vns[1449]=574;    vus[1449]=-1.04;    stddevNs[1449]=0.66;    stddevEs[1449]=-0.14;    stddevUs[1449]=0.17;    corrNEs[1449]=0.00021; 
   ids[1450]='WGPP';    sts[1450]='WGPP_SCGN_CS1999';    lats[1450]=35.0108487460;    lngs[1450]=241.0163117723;    ves[1450]=-17.49;    vns[1450]=16.32;    vus[1450]=-3.55;    stddevNs[1450]=0.17;    stddevEs[1450]=0.13;    stddevUs[1450]=0.59;    corrNEs[1450]=-0.015; 
   ids[1451]='WIKR';    sts[1451]='WIKR_GPS';    lats[1451]=-1384526.10674;    lngs[1451]=5688747.79573;    ves[1451]=292;    vns[1451]=653;    vus[1451]=-9.42;    stddevNs[1451]=3.5;    stddevEs[1451]=-4.88;    stddevUs[1451]=106.09;    corrNEs[1451]=0.14681; 
   ids[1452]='WILL';    sts[1452]='Williams_Lake';    lats[1452]=52.2368677850;    lngs[1452]=237.8321879987;    ves[1452]=-0.51;    vns[1452]=0.76;    vus[1452]=1.83;    stddevNs[1452]=0.18;    stddevEs[1452]=0.13;    stddevUs[1452]=0.55;    corrNEs[1452]=-0.004; 
   ids[1453]='WIN2';    sts[1453]='WIN2_BARD_CN2008';    lats[1453]=37.6526516716;    lngs[1453]=237.8593715803;    ves[1453]=-22.1;    vns[1453]=23.23;    vus[1453]=-4.27;    stddevNs[1453]=0.57;    stddevEs[1453]=0.62;    stddevUs[1453]=2.27;    corrNEs[1453]=-0.001; 
   ids[1454]='WIN2';    sts[1454]='WIN2_BARD_CN2008';    lats[1454]=37.6526516707;    lngs[1454]=237.8593715836;    ves[1454]=-22.1;    vns[1454]=23.23;    vus[1454]=-4.27;    stddevNs[1454]=0.57;    stddevEs[1454]=0.62;    stddevUs[1454]=2.27;    corrNEs[1454]=-0.001; 
   ids[1455]='WINT';    sts[1455]='WINT_BARD_CN1991';    lats[1455]=37.6526442293;    lngs[1455]=237.8594315810;    ves[1455]=-20.61;    vns[1455]=23.35;    vus[1455]=0.13;    stddevNs[1455]=0.14;    stddevEs[1455]=0.14;    stddevUs[1455]=0.57;    corrNEs[1455]=-0.019; 
   ids[1456]='WKPK';    sts[1456]='WKPK_SCGN_CS1999';    lats[1456]=34.5684992471;    lngs[1456]=241.2584824484;    ves[1456]=-24.02;    vns[1456]=20.96;    vus[1456]=-1.13;    stddevNs[1456]=0.15;    stddevEs[1456]=0.18;    stddevUs[1456]=0.47;    corrNEs[1456]=-0.009; 
   ids[1457]='WLWY';    sts[1457]='WLWY_EBRY_WY1999';    lats[1457]=44.6395180239;    lngs[1457]=249.7133457600;    ves[1457]=3.85;    vns[1457]=-4.16;    vus[1457]=42.68;    stddevNs[1457]=0.22;    stddevEs[1457]=0.42;    stddevUs[1457]=2.11;    corrNEs[1457]=-0.002; 
   ids[1458]='WMOK';    sts[1458]='WichitaMtnOK2005';    lats[1458]=34.7378935555;    lngs[1458]=261.2194754492;    ves[1458]=-0.69;    vns[1458]=-0.91;    vus[1458]=-4.09;    stddevNs[1458]=0.23;    stddevEs[1458]=0.47;    stddevUs[1458]=0.9;    corrNEs[1458]=0.001; 
   ids[1459]='WMOK';    sts[1459]='WichitaMtnOK2005';    lats[1459]=34.7378935684;    lngs[1459]=261.2194754499;    ves[1459]=-0.69;    vns[1459]=-0.91;    vus[1459]=-4.09;    stddevNs[1459]=0.23;    stddevEs[1459]=0.47;    stddevUs[1459]=0.9;    corrNEs[1459]=0.001; 
   ids[1460]='WOMT';    sts[1460]='WOMT_SCGN_CS1999';    lats[1460]=34.6690144972;    lngs[1460]=243.0683344965;    ves[1460]=-11.6;    vns[1460]=11.17;    vus[1460]=-1.58;    stddevNs[1460]=0.13;    stddevEs[1460]=0.14;    stddevUs[1460]=0.42;    corrNEs[1460]=-0.015; 
   ids[1461]='YELL';    sts[1461]='Yellowknife';    lats[1461]=5633638.29340;    lngs[1461]=62.4808937719;    ves[1461]=-0.35;    vns[1461]=-684;    vus[1461]=-0.23;    stddevNs[1461]=3.56;    stddevEs[1461]=0.24;    stddevUs[1461]=0.17;    corrNEs[1461]=0.00065; 
   ids[1462]='YELL';    sts[1462]='Yellowknife';    lats[1462]=5633638.29246;    lngs[1462]=62.4808937601;    ves[1462]=-0.35;    vns[1462]=-684;    vus[1462]=-0.23;    stddevNs[1462]=3.56;    stddevEs[1462]=0.24;    stddevUs[1462]=0.17;    corrNEs[1462]=0.00065; 

	//Create our style icons
	var baseIcon = new GIcon();
	baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
	baseIcon.iconSize = new GSize(15, 20);
	baseIcon.shadowSize = new GSize(10, 10);
	baseIcon.iconAnchor = new GPoint(1, 10);
	baseIcon.infoWindowAnchor = new GPoint(5, 1);
	baseIcon.infoShadowAnchor = new GPoint(5, 5);
	
	//--------------------------------------------------
	// Done with declarations.
	//--------------------------------------------------

	//This should be called when the body() function is called.
	function initializeUnavcoMap() {
	   //This is associated with the checkbox.
		searcharea = document.getElementById("unavcoobsvGPSMap:unavcoGpsSelectionArea");

		//Create the icons for the search area.
		icon_NE = new GIcon(); 
		icon_NE.image = 'http://maps.google.com/mapfiles/ms/micons/red-pushpin.png';
		icon_NE.shadow = '';
		icon_NE.iconSize = new GSize(32, 32);
		icon_NE.shadowSize = new GSize(22, 20);
		icon_NE.iconAnchor = new GPoint(10, 32);
		icon_NE.dragCrossImage = '';
		icon_SW = icon_NE;

	      redStationsJson=JSON.parse(document.getElementById("unavcoRedJsonStations").value);
	      map = new GMap2(document.getElementById('unavcomap_canvas'));  
			speedToDgr = 0.0024 ; 
			cookieTest();   
			deleteCookie(f3CookieName);
         c_start=document.cookie.indexOf(f3CookieName + '='); 
         if (c_start!=-1) {          
         cookie = readCookie ();  
         decodeCookieValue (); }  			  

			map.setCenter(center, zoomfactor)  
			map.setMapType(G_PHYSICAL_MAP);   
			map.addMapType(G_PHYSICAL_MAP);   
			var mapControl = new GMapTypeControl();  
			map.addControl(mapControl);  
			var cleft = new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(10, 60)); 
			map.addControl(new GLargeMapControl(), cleft);   
			map.addControl(new GScaleControl());   
			map.enableContinuousZoom();   
			
			setIconColors();
			getLimits();    
			drawSymbols(); 
			GEvent.addListener(map, 'moveend', function() {getLimits();drawSymbols();});   
			GEvent.addListener(map, 'zoomend', function() {getLimits();drawSymbols();});   
//			GEvent.addListener(map,'click', function(overlay, latlng) {       
//         if (latlng) {   
//         var lng1 = ''+latlng.lng(); 
//         var lng1 = lng1.substring(0,9); 
//         var lat1 = ''+latlng.lat(); 
//         var lat1 = lat1.substring(0,7); 
//         var myHtml = 'latitude,longitude '+lat1+' '+lng1  ;   
//         map.openInfoWindow(latlng, myHtml);   
//         }   
//			});   
        
      }  
			
	 //Set the icon color array's initial values.  Note we assume
    //this array and others are all the size of ids.  Two loops, so this 
    //may need some improvement.
	 function setIconColors() {
	    for (var j=0;j<ids.length;j++) {
		   iconColors[j]=greenIcon;  
	      for(var key in redStationsJson){
			   if(redStationsJson[key].toString()==ids[j].toString()) {
			     iconColors[j]=redIcon;  
            }
		   }
		 }
	 }

    function getLimits() {
        var bounds =  map.getBounds();    
        center =  map.getCenter();    
        zoomfactor =  map.getZoom();    
        var swlatlng = bounds.getSouthWest();    
        var nelatlng = bounds.getNorthEast();    
        latmax = nelatlng.lat();    
        lngmax = nelatlng.lng();    
        latmin = swlatlng.lat();    
        lngmin = swlatlng.lng();    
        deltalng = Math.abs(lngmax - lngmin) ; 
        deltalat = Math.abs(latmax - latmin) ; 

		  space=" ";
//		alert(latmin+space+latmax+space+lngmin+space+lngmax);
    }  

    function cookieTest () {      
       if (testSessionCookie()) {  
           usecookies=1;  
       } 
      else  { 
		  alert ('Cookies are not enabled. Please accept cookies, or all choices will be lost at each remap.');  }   
    } 

   function setSessionCookie () { 
      f3CookieValue = center.lat() +' '+center.lng()+' '+zoomfactor+' ' +declutter+ ' ' +speedToDgr+ ' ' + vectorpointcolor + ' ' + drawellipses + ' ' + drawvectors  + ' ' + drawverticals + ' ' + drawvertellipses + ' ' + drawmarkers; 
     if (usecookies==1) {  
        var expires = ''; 
        var date = new Date(); 
        date.setTime(date.getTime()+(28800*1000)); 
        var expires=date.toGMTString(); 
        document.cookie = f3CookieName+'='+f3CookieValue+expires+'; path=/'; 
        } 
		  //alert(f3CookieValue);
      } 

   function decodeCookieValue () { 
       var varray = cookie.split(' ');       
       center=new GLatLng(varray[0], varray[1]);       
       zoomfactor=parseInt(varray[2]);       
       declutter=parseInt(varray[3]);       
       speedToDgr=varray[4];       
       vectorpointcolor=varray[5];       
       drawellipses=parseInt(varray[6]);       
       drawvectors=parseInt(varray[7]);       
       drawverticals=parseInt(varray[8]);       
       drawvertellipses=parseInt(varray[9]);       
       drawmarkers=parseInt(varray[10]);       
       } 

   function readCookie () {      
     var nameEQ = f3CookieName + '=';   
     var ca = document.cookie.split(';'); 
     for(var i=0;i < ca.length;i++) { 
       var c = ca[i];   
       while (c.charAt(0)==' ') c = c.substring(1,c.length); 
       if(c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length); 
       } 
     return null; 
    } 

    function drawSymbols() {   
      mapsizer =   speedToDgr * deltalng ; 
		map.clearOverlays(); 
      if (drawvectors==1)    drawVectors(); 
//      if (drawvectors==1)    drawSpeedScale () ;  
//      if (drawverticals==1)  drawVerticalSpeedSymbols(); 
      if (drawmarkers ==1)   drawMarkers(); 
//      if (drawplates ==1)    drawPlates(); 
//      if (drawvolcanoes ==1) drawVolcanoes(); 
//      if (draweqs ==1)       drawEpicenters(); 
      setSessionCookie();   
      } 

   function drawSpeedScale () {      
      var deltalng = Math.abs(lngmax - lngmin) ; 
      var deltalat = Math.abs(latmax - latmin) ; 
      var pointcolor='#FF00FF'; 
      var stemcolor='#FF00FF'; 
      x0 = lngmin + deltalng * 0.25; 
      y0 = latmin + deltalat * 0.029; 
      dx= 25.0 * mapsizer;  
      x1=x0 + dx;  
      x0b=x0 + dx*0.04;  
      var gpsscale = new GPolyline([ new GLatLng(y0,x0b), new GLatLng(y0,x1) ], stemcolor, 1, 1); 
      map.addOverlay(gpsscale);  
      dl = deltalng * 0.013;        
      var  x2=x0+ 1.3 * dl ; 
      var  y3=latmin + deltalat * 0.024;  
      var  y4=latmin + deltalat * 0.034;  
      x1=x0 + dx;  
      p1 = new GLatLng(y0,x0);  
      var speedscale = new GPolygon([ new GLatLng(y4,x2), new GLatLng(y3,x2), p1, ], stemcolor, 1, 0.9, pointcolor, 0.9);  
      map.addOverlay(speedscale);  
      } 

    function drawVerticalSpeedSymbols() {   
      var xe,ye,xprev, yprev; 
      var deltalng = Math.abs(lngmax-lngmin); 
      var hgt = 0.008 * deltalng;  
      var offsetx = 0.0047 * deltalng;  
      var dtr=3.141592/180.0; 
      var vertsym = new GPolygon(); 
      for (var j=0; j<ids.length; j++) {       
        vu= vus[j];  
        if (vu==0.00000) continue; ;      
        y0= lats[j] ;      
        x0= lngs[j] ;      
        if (x0>180.0) x0= lngs[j] - 360.0 ;  
        stddevU = stddevUs[j] ; 
        if ( (y0<latmax && y0>latmin && x0<lngmax && x0>lngmin) ) {;} 
        else { continue; }    
          if ( j % declutter == 0 ) { ; } 
          else { continue; };       
        merccorr=Math.cos(y0*dtr);  
        offsety = hgt * merccorr; 
        vertsymcolor = '#cccccc' ;  
        if (vu >.5 && vu<=1) vertsymcolor = '#f2ff00' ;  
        else if (vu >1 ) vertsymcolor =     '#ffec00' ;  
        if (vu >2 ) vertsymcolor =          '#ffcc00' ;  
        if (vu >3) vertsymcolor =           '#ffac00' ;  
        if (vu >4) vertsymcolor =           '#ff8c00' ;  
        if (vu >5) vertsymcolor =           '#ff6c00' ;  
        if (vu >10) vertsymcolor =          '#ff4c00' ;  
        if (vu >20) vertsymcolor =          '#ff2c00' ;  
        if (vu <-.5 ) vertsymcolor = '#00fff2' ;  
        if (vu <-1 ) vertsymcolor =  '#00ecff' ;  
        if (vu <-2 ) vertsymcolor =  '#00ccff' ;  
        if (vu <-3) vertsymcolor = '#00acff' ;  
        if (vu <-4) vertsymcolor = '#008cff' ;  
        if (vu <-5) vertsymcolor = '#006cff' ;  
        if (vu <-10) vertsymcolor = '#004cff' ;  
        if (vu <-20) vertsymcolor = '#002cff' ;  
        if (vu >0.0) { 
        vertsym = new GPolygon([ new GLatLng(y0+offsety,x0), new GLatLng(y0-offsety,x0-offsetx), new GLatLng(y0-offsety,x0+offsetx) ], '#000000', 1, 0.8, vertsymcolor, 0.8);  
        } else if (vu <=0.0) { 
        vertsym = new GPolygon([ new GLatLng(y0-offsety,x0), new GLatLng(y0+offsety,x0-offsetx), new GLatLng(y0+offsety,x0+offsetx) ], '#000000', 1, 0.8, vertsymcolor, 0.8);  
        }  
        map.addOverlay(vertsym);  
        if (drawvertellipses!=0) { 
          radius =mapsizer * ellipseSizer * stddevU ; 
          xe = radius; 
          ye = 0.0;
          if (xe < 0.007 * deltalng) continue; 
          xnext = x0 +xe;   
          ynext = y0 +ye*merccorr;  
          var ellipse_segment = new GPolyline([  new GLatLng(y0,x0), new GLatLng(ynext,xnext) ], vertsymcolor, 1 , 0.8);  
          map.addOverlay(ellipse_segment);  
          var N=25; 
          var af = (360/N)*dtr; 
          for (var ti=1; ti<=N; ti++) { 
             x5=xnext; y5=ynext;  
             theta = ti * af; 
             xe =radius * Math.cos(theta);  
             ye =radius * Math.sin(theta);  
             xnext = x0 +xe;  
             ynext = y0 +ye*merccorr;  
             ellipse_segment = new GPolyline([  new GLatLng(y5,x5), new GLatLng(ynext,xnext) ], vertsymcolor, 1 , 0.8);  
             map.addOverlay(ellipse_segment);  
             }  
           } 
        } 
      } 

    function createPBOMarker(point, staHtml,sCode) {         
      var Icon; 
      if (sCode.length != 4) { 
         Icon = new GIcon(G_DEFAULT_ICON); 
         Icon.image = 'http://www.google.com/intl/en_us/mapfiles/ms/micons/blue-dot.png'; 
        } 
      else {   
         Icon = new GIcon(); 
         Icon.image = generatedImages + '/4char-icon1-' + sCode + '.png'; 
         Icon.imageMap = [2,6,2,13,3,15,5,17,7,18,9,20,9,22,10,23,11,21,11,19,12,19,45,19,47,17,49,16,50,15,51,13,51,6,48,3,46,2,44,1,9,1,6,2];
         Icon.iconSize = new GSize(53, 31);
         Icon.shadowSize = new GSize(65, 30); 
         Icon.iconAnchor = new GPoint(6, 30);
         Icon.infoWindowAnchor = new GPoint(10, 0);
         Icon.printImage = generatedImages + '/4char-icon3-print-' + sCode + '.gif';
         Icon.mozPrintImage = generatedImages + '/4char-icon3-print-' + sCode + '.gif';
        }
      markerOptions = { icon:Icon }; 
      var marker = new GMarker(point, markerOptions);  
      marker.value = 1;    
      GEvent.addListener(marker, 'click', function() {    
        map.openInfoWindowHtml(point, staHtml);    
      });                                          
      return marker;             
    }    

    function createMarker(index,point,id,x0,y0,staHtml) {  
	   var selectedId;
	   baseIcon.image=iconColors[index];
		var markerOptions={icon:baseIcon};
      var marker = new GMarker(point, markerOptions);  
      marker.value = 1;    
		selectedId=document.getElementById("unavcoobsvGPSMap:unavcostationName");
		visibleSelectedList=document.getElementById("unavcoobsvGPSMap:unavcoYellowStations");
		candidateList=document.getElementById("unavcoobsvGPSMap:unavcoCandidateStations");
		
      GEvent.addListener(marker, 'click', function() {    
		  var space=" ";
//		  alert(ids[index]+space+ves[index]+space+stddevEs[index]);
//		  var iconColor=marker.getIcon().image;
		  var selectedIconColor=iconColors[index];
		  if(selectedIconColor==redIcon) {;}
		  else if (selectedIconColor==greenIcon) {
		  
			 //Make new objects to store the station's metadata.
			 var markerMetadata={};
			 markerMetadata["id"]=ids[index];
          markerMetadata["x0"]=x0;
			 markerMetadata["y0"]=y0;
			 markerMetadata["ve"]=ves[index];
			 markerMetadata["vn"]=vns[index];
			 markerMetadata["vu"]=vus[index];
			 markerMetadata["stddevE"]=stddevEs[index];
			 markerMetadata["stddevN"]=stddevNs[index];
			 markerMetadata["stddevU"]=stddevUs[index];

			 baseIcon.image=yellowIcon;
			 marker.getIcon().image=yellowIcon;
			 selectedStations[id]=markerMetadata;
			 iconColors[index]=yellowIcon;
			 //Update the forms

			 selectedId.value=id;
			 var tmplist="";
			 for(var key in selectedStations) {
			   tmplist+=selectedStations[key].id+" ";
			 }
			 visibleSelectedList.value=tmplist;
			 candidateList.value=JSON.stringify(selectedStations);
          //alert(JSON.stringify(selectedStations));
			 map.removeOverlay(marker);
			 map.addOverlay(marker);
			 }
		   else if(selectedIconColor==yellowIcon){
			   baseIcon.image=greenIcon;
				marker.getIcon().image=greenIcon;
				iconColors[index]=greenIcon;
				delete selectedStations[id];
				//Update the forms
				selectedId.value="";
				var tmplist="";
			   for(var key in selectedStations) {
			     tmplist+=selectedStations[key].id+" ";
			   }
				visibleSelectedList.value=tmplist;
				candidateList.value=JSON.stringify(selectedStations);
				map.removeOverlay(marker);
				map.addOverlay(marker);
			}
			map.openInfoWindowHtml(point, staHtml);    
		  });
      return marker;
    }    

    function drawMarkers() {   
      var deltalng = Math.abs(lngmax - lngmin) ; 
      var lt,dxn,dyn,x1,x2,x3,x4,y1,y2,y3,y4,stddevN,stddevE,corrNE,term1,term2,semimajoraxis,semiminoraxis,alpha;
      var xe,ye,xprev, yprev; 
      var dc=0;       

      var dtr=3.141592/180.0; 

      for (var j=0; j<ids.length; j++) {       
        y0=lats[j];      
        x0=lngs[j];      
								 
        space=" ";
        if (x0>180.0) x0= lngs[j] - 360.0 ;  						

		  //Obsolete conditions from original code
//        if ( (y0<latmax && y0>latmin && x0<lngmax && x0>lngmin) )  {  ; } 
//        else { continue; }    
        //Ignore decluttering for now.
//        if ( j % declutter == 0 ) { ; } 
//        else { continue; };       

        ve= ves[j];  
        vn= vns[j];  
        vu= vus[j];  
        id= ids[j];  
        staname = sts[j]; 
        var markerpt = new GLatLng(y0,x0);  
        stddevN = stddevNs[j] ; 
        stddevE = stddevEs[j] ; 
        stddevU = stddevUs[j] ; 
        corrNE = corrNEs[j] ; 
        speed =  Math.sqrt( ve*ve + vn*vn); 
        aspeed = (speed+'').substring(0,4); 
        bearing=  360+ Math.atan2( ve,vn) / dtr; 
        if (bearing> 360) bearing -=360 
        abearing=  (bearing+'').substring(0,6);
        url1='http://pboweb.unavco.org/products/timeseries/'+id+'.raw.png' ; 
        url2='http://pboweb.unavco.org/products/timeseries/'+id+'.detrend.png' ; 
        url3='http://pboweb.unavco.org/products/timeseries/'+id+'.clean.png' ; 
        url4='ftp://data-out.unavco.org/pub/products/position/'+id+'/'+id+'.pbo.snf01.csv' ; 
        url5='http://pboweb.unavco.org/products/timeseries/'+id+'.thumb.png' ; 
        ts = ' time series'; 
        var id_link=''; 
        if (id.length ==4 ) { id_link= '<a href=http://pboweb.unavco.org/shared/scripts/stations/?checkkey='+id+' target='+id+'>'+id+'</a>' ;} 
        else { id_link= id;} 

        ts = '<BR><a href='+url1+' target='+id+'>Time plot</a> (<a href='+url2+' target='+id+'>detrended</a>; <a href='+url3+' target='+id+'>clean</a>); &nbsp; &nbsp; &nbsp; <a href='+url4+'>download data file</a>';   
          smallimage = '<P><img src='+url5+' width=320 height=106>'; 

          var lat8char= y0+''; 
          lat8char= lat8char.substr(0,7); 
          var lng9char= x0+''; 
          lng9char= lng9char.substr(0,9); 
          staHtml = 'Station  '+id_link+'   '+staname+'<font size=-1><BR>horizontal speed: '+aspeed+' mm/yr &nbsp; direction: '+abearing+'<br>Latitude '+lat8char+' &nbsp; Longitude '+lng9char+'<BR>Speed components: East '+ve+' North '+vn+' Up '+vu+' mm/yr ' +' <BR>Std deviations:  &nbsp; &nbsp; &nbsp; &nbsp;  East '+stddevE+'; North '+stddevN+' Up '+stddevU+' mm/yr '+ts +smallimage + '</p>';   

          gpsStationMarker[j]=createMarker(j,markerpt,id,x0,y0,staHtml);
          map.addOverlay(gpsStationMarker[j]);
      }
    }

    function drawVectors() {   
	   var iconColor;
      var deltalng = Math.abs(lngmax - lngmin) ; 
      var lt,dxn,dyn,x1,x2,x3,x4,y1,y2,y3,y4,stddevN,stddevE,corrNE,term1,term2,semimajoraxis,semiminoraxis,alpha;
      var xe,ye,xprev, yprev; 
      var dc=0;       
      var dtr=3.141592/180.0; 
      for (var j=0; j<ids.length; j++) {       
        y0= lats[j] ;      
        x0= lngs[j] ;      
        if(x0>180.0) x0= lngs[j] - 360.0 ;  
        if ( (y0<latmax && y0>latmin && x0<lngmax && x0>lngmin) )  { ; } 
        else { continue; }    
            if ( j % declutter == 0 ) { ; } 
            else { continue; };       
        ve= ves[j];  
        vn= vns[j];  
        dx= ve * mapsizer;  
        x1=x0+ dx;  
        lat0 = y0 *3.141592/180.0 ;      
        var merccorr= Math.cos(lat0); 
        dy= vn * mapsizer * merccorr; 
        y1=y0+ dy;  
        var p1 = new GLatLng(y0,x0);  
        x1b=x0+ dx*0.96;  
        y1b=y0+ dy*0.96;  
        var p2 = new GLatLng(y1b,x1b);  
        GPS_vel_stem = new GPolyline([p1,p2], vectorstemcolor, 1 , 0.95);  
        map.addOverlay(GPS_vel_stem);  
        dc +=1;       
        h= Math.sqrt(dx*dx + dy*dy);  
        dl = deltalng * 0.013;        
        if(dl>0.8 * h) dl=0.8*h;      
        fracx=dx/h;       
        fracy=dy/h;       
        x3 =(x1- dl*fracx)-(dl*fracy / 4.0);       
        y3 =(y1- dl*fracy)+( (dl*fracx / 4.0) *merccorr);       
        x4 =(x1- dl*fracx)+(dl*fracy / 4.0);       
        y4 =(y1- dl*fracy)-( (dl*fracx / 4.0) *merccorr);       
        var GPS_vel_point = new GPolygon([new GLatLng(y4,x4), new GLatLng(y3,x3), new GLatLng(y1,x1) ], vectorstemcolor, 1, 0.9, vectorpointcolor, 0.95);  
        map.addOverlay(GPS_vel_point);  
        if (drawellipses!=0) { 
          sigy = stddevNs[j] ; 
          sigx = stddevEs[j] ; 
          rho = corrNEs[j] ; 
          conrad=1.0; 
          a = (sigy*sigy - sigx*sigx) * (sigy*sigy - sigx*sigx); 
          b = 4. * (rho*sigx*sigy) * (rho*sigx*sigy); 
          c = sigx*sigx + sigy*sigy;  
          eigen1 = conrad * Math.sqrt((c - Math.sqrt(a + b))/2.); 
          eigen2 = conrad * Math.sqrt((c + Math.sqrt(a + b))/2.); 
          d = 2. * rho * sigx * sigy; 
          e =  sigx*sigx - sigy*sigy; 
          ang = Math.atan2(d,e)/2.;   
          semimajoraxis = eigen2 * ellipseSizer;  
          semiminoraxis = eigen1 * ellipseSizer;  
          bearing =  ang ; 
          xe =mapsizer * semimajoraxis; ; 
          ye = 0.0;
          if (xe < 0.00333 * deltalng) continue; 
          xoffset= (xe * Math.cos(bearing) - ye * Math.sin(bearing) );  
          yoffset= (ye * Math.cos(bearing) + xe * Math.sin(bearing) ); 
          xnext= x1 +xoffset;   
          ynext = y1+yoffset*merccorr;  
          xradius = mapsizer * semimajoraxis 
          yradius = mapsizer * semiminoraxis   
          var N=21;  
          var points= new Array(21); 
          for (var ti=0; ti<21; ti++) {  
             x5=xnext; y5=ynext;  
             theta = ti * 0.349;
             xe = xradius * Math.cos(theta);
             ye = yradius * Math.sin(theta);
             xoffset=  (xe * Math.cos(bearing) - ye * Math.sin(bearing) );   
             yoffset=  (ye * Math.cos(bearing) + xe * Math.sin(bearing) );   
             xnext = x1+xoffset; 
             ynext = y1+yoffset*merccorr;
             points[ti]= new GLatLng(y5,x5);
             }
          var ellipse= new GPolyline(points, vectorstemcolor, 1 , 0.8);  
          map.addOverlay(ellipse);  
        } 
      }   
    }  

    function drawPlates() {   
      var pbpoints = new Array();  
      for (var j=0; j<=-1; j++) {       
        pbpoints = multiplate_verticesARRAY[j]; 
        var plate_outline = new GPolyline( pbpoints, '#ffff00', 2, 0.9);  
        map.addOverlay(plate_outline);  
      }   
    }  

    function drawEpicenters() {   
      var dx;       
      var dy;       
      dx= (lngmax - lngmin)/250;       
      dy= (latmax - latmin)/160;       
      var declutter_quakes=1;  
        if (declutter>1) {    declutter_quakes= declutter * 2 }   
      for (var j=0; j<0; j++) {       
        y0= eq_lats[j] ;      
        x0= eq_lngs[j] ;      
        if(x0>180.0) x0= eq_lngs[j] - 360.0 ;  
        if ( (y0<latmax && y0>latmin && x0<lngmax && x0>lngmin) )  { ; } 
        else { continue; }    
        if ( j % declutter_quakes == 0 ) { ; } 
        else { continue; };       
        x1=x0;  
        y1=y0+ dy;  
        x2=x0+ dx;  
        y2=y0;  
        x3=x0;  
        y3=y0- dy;  
        x4=x0- dx;  
        y4=y0;  
        eqdepth= eq_deps[j] ;      
        if (eqdepth <=10)                     {  earthquakecolor3 ='#f433e4';  } 
        else if (eqdepth >10 && eqdepth <=20) {earthquakecolor3 =  '#c334dd';  } 
        else if (eqdepth >20 && eqdepth <=33) {earthquakecolor3 =  '#a235d0';  } 
        else if (eqdepth >33 && eqdepth <=70) {  earthquakecolor3 ='#0000ff';  } 
        else if (eqdepth >70 && eqdepth<=110) {  earthquakecolor3 ='#bbffbb';  } 
        else if (eqdepth >110&& eqdepth<=150) {  earthquakecolor3 ='#00ff00';  } 
        else if (eqdepth >150 &&eqdepth<=300) {  earthquakecolor3 ='#ffff00';  } 
        else if (eqdepth >300 &&eqdepth<=500) {  earthquakecolor3 ='#ff6600';  } 
        else if (eqdepth >500)                {  earthquakecolor3 ='#ff0000';  } 
        var eq_point = new GPolygon( [new GLatLng(y3,x3), new GLatLng(y4,x4), new GLatLng(y1,x1), new GLatLng(y2,x2) ], earthquakecolor, 1, 0.9, earthquakecolor3, 0.9);  
        map.addOverlay(eq_point);  
      }   
    }  
    function drawVolcanoes() {   
      var dx;
      var dy;
      dx= (lngmax - lngmin)/120;       
      dy= (latmax - latmin)/67;       
      for (var j=0; j<0; j++) {       
        y0= vol_lats[j] ;      
        x0= vol_lngs[j] ;      
        if(x0>180.0) x0= vol_lngs[j] - 360.0 ;  
        if ( (y0<latmax && y0>latmin && x0<lngmax && x0>lngmin) )  { ; } 
        else { continue; }    
        x1=x0+dx;  
        y1=y0;  
        x2=x0- dx;  
        y2=y0;  
        x3=x0;  
        y3=y0+ dy;  
        var vol_point = new GPolygon([ new GLatLng(y1,x1), new GLatLng(y2,x2), new GLatLng(y3,x3) ], volcanocolor,1,0.9,volcanocolor2,0.9);
        map.addOverlay(vol_point);  
        var markerpt = new GLatLng(y0,x0);  
        var volname,volloc,voltype,voltime; 
        var volname = vol_names[j]; 
        var volloc  = vol_locs[j]; 
        var vt  = vol_types[j]; 
        var slat = y0+''; 
        var slon = x0+''; 
        staHtml = '<font size=-1>'+volname+' ('+vt+')<br>'+volloc+' (Lat '+slat+' Lon'+slon+')</font></p>';   
        map.addOverlay(createMarker(markerpt, staHtml));  
      }   
    }   
//--------------------------------------------------
//Here is the code for mananging the selection box.
//--------------------------------------------------
//This plots the selection area box. 
function initialPosition() {
// map.clearOverlays();
	var bounds = map.getBounds();
	var span = bounds.toSpan();
	var newSW = new GLatLng(bounds.getSouthWest().lat() + span.lat()/3, 
			bounds.getSouthWest().lng() + span.lng()/3);
	var newNE = new GLatLng(bounds.getNorthEast().lat() - span.lat()/3, 
			bounds.getNorthEast().lng() - span.lng()/3);

	var newBounds = new GLatLngBounds(newSW, newNE);

	marker_NE = new GMarker(newBounds.getNorthEast(), {draggable: true, icon: icon_NE});
	GEvent.addListener(marker_NE, 'dragend', function() {
		updatePolyline();
//		updateGPSinthebox();
	});

	marker_SW = new GMarker(newBounds.getSouthWest(), {draggable: true, icon: icon_SW});
	GEvent.addListener(marker_SW, 'dragend', function() {
		updatePolyline();
//		updateGPSinthebox();
	});  

	map.addOverlay(marker_NE);
	map.addOverlay(marker_SW);
// map.addOverlay(marker_move);

	updatePolyline();
}

function updateGPSinthebox() {

	var minlat = document.getElementById("unavcoobsvGPSMap:unavcominlat");	
	var minlon = document.getElementById("unavcoobsvGPSMap:unavcominlon");     
	var maxlat = document.getElementById("unavcoobsvGPSMap:unavcomaxlat");
	var maxlon = document.getElementById("unavcoobsvGPSMap:unavcomaxlon");

	minlat.value = marker_SW.getPoint().lat();
	minlon.value = marker_SW.getPoint().lng();
	maxlat.value = marker_NE.getPoint().lat();
	maxlon.value = marker_NE.getPoint().lng();

	if (marker_SW.getPoint().lat() >= marker_NE.getPoint().lat()) {
		maxlat.value = marker_SW.getPoint().lat();
		minlat.value = marker_NE.getPoint().lat();
	}

	if (marker_SW.getPoint().lng() >= marker_NE.getPoint().lng()) {
		maxlon.value = marker_SW.getPoint().lng();
		minlon.value = marker_NE.getPoint().lng();
	}

   for(var j=0;j<gpsStationMarker.length;j++) {
        //Could also use lats[j], lngs[j] for this.
	     var latLng=gpsStationMarker[j].getLatLng();
	     stationLat=latLng.lat();
	     stationLng=latLng.lng();
	     if((stationLng<=maxlon.value && stationLng>=minlon.value)
           &&(stationLat<=maxlat.value && stationLat>=minlat.value)) {
          var id=ids[j]
          var x0=lngs[j];
          var y0=lats[j];
			 if(x0>180.0) x0=lngs[j]-360.0;
			 var markerMetadata={};
			 markerMetadata["id"]=ids[j];
          markerMetadata["x0"]=x0;
			 markerMetadata["y0"]=y0;
			 markerMetadata["ve"]=ves[j];
			 markerMetadata["vn"]=vns[j];
			 markerMetadata["vu"]=vus[j];
			 markerMetadata["stddevE"]=stddevEs[j];
			 markerMetadata["stddevN"]=stddevNs[j];
			 markerMetadata["stddevU"]=stddevUs[j];

			 baseIcon.image=yellowIcon;
			 gpsStationMarker[j].getIcon().image=yellowIcon;
			 selectedStations[id]=markerMetadata;
			 iconColors[j]=yellowIcon;

			 var tmplist="";
			 for(var key in selectedStations) {
			   tmplist+=selectedStations[key].id+" ";
			 }
			 visibleSelectedList.value=tmplist;
			 candidateList.value=JSON.stringify(selectedStations);
          //alert(JSON.stringify(selectedStations));
			 map.removeOverlay(gpsStationMarker[j]);
			 map.addOverlay(gpsStationMarker[j]);
        }
	}
}

function updatePolyline() {
	if (border) {
		map.removeOverlay(border);
	}

	var points = [
		 marker_NE.getPoint(),
		 new GLatLng(marker_SW.getPoint().lat(), marker_NE.getPoint().lng()),
		 marker_SW.getPoint(),
		 new GLatLng(marker_NE.getPoint().lat(), marker_SW.getPoint().lng()),
		 marker_NE.getPoint()];
	border = new GPolyline(points, "#FF0000");

	map.addOverlay(border);
}

function toggleBorder() {
	if (searcharea.checked == false) {  

		map.removeOverlay(border);
		map.removeOverlay(marker_NE);
		map.removeOverlay(marker_SW);  
	}

	else {    

		initialPosition();
	}
}
//]]>
    </script> 	
  </f:verbatim>

  <h:panelGroup id="simplexUnavcoPGHolder">
  <f:verbatim><fieldset><legend><b>UNAVCO Station Map</b></legend></f:verbatim>
	 <h:form id="unavcoobsvGPSMap">
	 <f:verbatim>
		  Select the stations that you want to use as observation points. Then, fetch the values and add them to your project observation list. This map interface and data are derived from the
		  <a href="http://geon.unavco.org/unavco/GPSVelocityViewer.php" target="_blank">
			 UNAVCO GPS Velocity Viewer
			 </a>.
		</f:verbatim>
		<h:panelGrid id="unavcomapsAndCrap" columns="2" columnClasses="alignTop,alignTop">
		  <h:panelGroup id="unavcomapncrap1">
			 <f:verbatim> 
				<div id="unavcomap_canvas" style="width: 600px; height: 400px"></div>
			 </f:verbatim>
		  </h:panelGroup>
		  <h:panelGroup id="unavcomapncrap2">
			 <h:panelGrid id="unavcomanncraplayoutgrid"
							  border="1"
							  columns="1">
				<h:panelGroup id="unavcomapncrapLayoutGroup1">
				  <h:outputText id="unavcoSimplexGPSInstructions1"
									 value="First, select stations to import into your project by clicking. 
											Selected stations are yellow."/>
				<h:panelGrid id="unavcodfjdlkj" 
								 border="0"
								 columns="2">
				  
				  <h:outputText id="unavcodkljrabd2" value="Current Station:"/> 
				  <h:inputText id="unavcostationName" 
									value="#{SimplexBean.gpsStationName}" 
									style="text-align:center;width:45px" 
									readonly="true"/>
				  
				  <h:outputText id="unavcodkljr3dssraea" value="Selected GPS list:"/> 
				  <h:inputText id="unavcoYellowStations" readonly="true" value=""/> 
				</h:panelGrid>
			 </h:panelGroup>
			 
			 <h:panelGroup id="unavcomapncrapLayoutGroup2">
				<h:outputText id="unavcoSimplexGPSInstructions2" 
								  value="Instead of individual stations, you can grab all 
											the stations in a selection area."/>
				<h:panelGrid id="unavcosimplexGPSStationSelectionArea" 
								 columns="2">
				  <h:outputText id="unavcodkljr3dssra" value="Use Search Area:"/>
				  <h:selectBooleanCheckbox id="unavcoGpsSelectionArea" 
													onclick="toggleBorder()" 
													value="#{SimplexBean.searcharea}"/>
				  <h:outputText id="unavcoSimplexGetGPSSelectionArea" 
									 value="Select Stations in Box:"/>
				  <h:commandButton id="unavcoSimplexFetchGPSStations"
										 type="button" 
										 onclick="updateGPSinthebox()"
										 value="Select Stations in Box"/>
				  <h:inputHidden id="unavcominlon" value="#{SimplexBean.selectedminlon}"/>
				  <h:inputHidden id="unavcominlat" value="#{SimplexBean.selectedminlat}"/>
				  <h:inputHidden id="unavcomaxlon" value="#{SimplexBean.selectedmaxlon}"/>
				  <h:inputHidden id="unavcomaxlat" value="#{SimplexBean.selectedmaxlat}"/>
				</h:panelGrid>
			 </h:panelGroup>
			 
			 <h:panelGroup id="unavcomapncrapLayoutGroup5">
				<h:outputText id="unavcosimplexStationSelection3"
								  value="Finally, import the selected stations into your project."/>
				<h:inputHidden id="unavcoCandidateStations"
									value="#{SimplexBean.gpsJSONValues}"/>
				<h:panelGrid id="unavconploebba" columns="2">
				  <h:commandButton id="unavcoaddGPSObsv" value="Add Station(s)"
										 actionListener="#{SimplexBean.toggleAddJSONGPSObsvForProject}"/>
				  <h:commandButton id="unavcocloseMap" value="Close Map"
										 actionListener="#{SimplexBean.toggleCloseMap}"/>
				</h:panelGrid>
			 </h:panelGroup>
		  </h:panelGrid>
		</h:panelGroup>
	 </h:panelGrid>
	 <h:panelGrid id="unavcosimplexKeyGrid" 
					  columns="7">
		<h:outputText id="unavcosimplexMapKey" 
						  escape="false"
						  value="<b>Map Key</b><br/>"/>
		<h:outputText id="unavcosimplexMapKeyGreen" 
						  escape="false"
						  value="Unselected Station:"/>
		<h:graphicImage id="unavcosimplexKeyGreenPin"
							 url="images/mm_20_green.png"/>
		<h:outputText id="unavcosimplexMapKeyYellow" 
						  escape="false"
						  value="Selected Station:"/>
		<h:graphicImage id="unavcosimplexKeyYellowPin"
							 url="images/mm_20_yellow.png"/>
		<h:outputText id="unavcosimplexMapKeyRed" 
						  escape="false"
						  value="Imported Station:"/>
		<h:graphicImage id="unavcosimplexKeyRedPin"
							 url="images/mm_20_red.png"/>
	 </h:panelGrid>
  </h:form>
<f:verbatim></fieldset></f:verbatim>
</h:panelGroup>
</h:panelGrid>