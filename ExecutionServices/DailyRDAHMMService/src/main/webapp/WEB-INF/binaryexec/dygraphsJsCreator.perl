#!/usr/bin/perl
use strict; use warnings;
open FILE,"$ARGV[0]" or die $!;

my @allArray=<FILE>;

#Print the east values
#print the header
print "function data_east() {\n";
print "return \"\" +";
print "\"Data, State 1, State 2, State 3, State 4, State 5\\n\" +\n";
print "\"";
my $iter=0;
foreach my $val1 (@allArray) {
	 $iter++;
	 my @lineArray=split(' ',$val1);

	 if($lineArray[0]=="1") {
		  print $lineArray[1] . "," . $lineArray[2] . ",,,,\\n ";
	 }
	 elsif($lineArray[0]=="2") {
		  print $lineArray[1] . ",," . $lineArray[2] . ",,,\\n ";
	 }
	 elsif($lineArray[0]=="3") {
		  print $lineArray[1] . ",,," . $lineArray[2] . ",,\\n ";
	 }
	 elsif($lineArray[0]=="4") {
		  print $lineArray[1] . ",,,," . $lineArray[2] . ",\\n ";
	 }
	 elsif($lineArray[0]=="5") {
		  print $lineArray[1] . ",,,,," . $lineArray[2] ."\\n ";
	 }
#	 if($iter le $#allArray) { print ","; }
}
print "\"}\n";

#Print the north values
#print the header
print "function data_north() {\n";
print "return \"\" +";
print "\"Data, State 1, State 2, State 3, State 4, State 5\\n\" +\n";
print "\"";
$iter=0;
foreach my $val1 (@allArray) {
	 $iter++;
	 my @lineArray=split(' ',$val1);

	 if($lineArray[0]=="1") {
		  print $lineArray[1] . "," . $lineArray[3] . ",,,,\\n ";
	 }
	 elsif($lineArray[0]=="2") {
		  print $lineArray[1] . ",," . $lineArray[3] . ",,,\\n ";
	 }
	 elsif($lineArray[0]=="3") {
		  print $lineArray[1] . ",,," . $lineArray[3] . ",,\\n ";
	 }
	 elsif($lineArray[0]=="4") {
		  print $lineArray[1] . ",,,," . $lineArray[3] . ",\\n ";
	 }
	 elsif($lineArray[0]=="5") {
		  print $lineArray[1] . ",,,,," . $lineArray[3] ."\\n ";
	 }
#	 if($iter le $#allArray) { print ","; }
}
print "\"}\n";

#Print the up values
#print the header
print "function data_up() {\n";
print "return \"\" +";
print "\"Data, State 1, State 2, State 3, State 4, State 5\\n\" +\n";
print "\"";
$iter=0;
foreach my $val1 (@allArray) {
	 $iter++;
	 my @lineArray=split(' ',$val1);

	 if($lineArray[0]=="1") {
		  print $lineArray[1] . "," . $lineArray[4] . ",,,,\\n ";
	 }
	 elsif($lineArray[0]=="2") {
		  print $lineArray[1] . ",," . $lineArray[4] . ",,,\\n ";
	 }
	 elsif($lineArray[0]=="3") {
		  print $lineArray[1] . ",,," . $lineArray[4] . ",,\\n ";
	 }
	 elsif($lineArray[0]=="4") {
		  print $lineArray[1] . ",,,," . $lineArray[4] . ",\\n ";
	 }
	 elsif($lineArray[0]=="5") {
		  print $lineArray[1] . ",,,,," . $lineArray[4] ."\\n ";
	 }
#	 if($iter le $#allArray) { print ","; }
}
print "\"}\n";
close(FILE);
