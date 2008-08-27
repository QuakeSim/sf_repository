<?xml version="1.0" encoding="ISO-8859-1"?>

<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:defineObjects/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
  <meta name="keywords"           content="electronic commerce, ecommerce, ebusiness, e-business, e-commerce, enterprise software, net economy, Sun Microsystems, Sun Open Network Environment, Sun ONE, internet software" />
  <meta name="Description"        content="Sun Microsystems, Inc. is delivering Sun ONE e-commerce software and enterprise solutions that enable companies to compete successfully in the Net Economy." />
  <meta name="TemplateVersion"    content="Sun ONE Online Help 2.3" />
  <meta name="LASTUPDATED"        content="Thu Jun 29 14:49:39 2006" />
  <title>Sun Java System Portal Server Desktop Online Help</title>
  <style>
  b.cBold  { font-weight: bold }
  span.cBookTitle  { color: #FFFFFF;font-family: Helvetica, sans-serif;font-size: 13px;font-weight: bold }
  span.cMastHead  { color: #594fbf;font-family: Helvetica, sans-serif;font-size: 14pt;font-weight: bold }
  span.cCode  { font-family: Courier New, Courier, mono;font-size: 100% }
  span.cCodeEmphasis  { font-family: Courier New, Courier, mono;font-size: 100%;font-style: italic }
  span.cCodeStrong  { font-family: Courier New, Courier, mono;font-size: 100%;font-weight: bold }
  span.cContents  { font-family: Arial, Helvetica, ;font-size: 10pt }
  em.cEmphasis  { font-style: italic }
  span.cGlossaryTerm  { font-size: 13px;font-weight: bold }
  span.cHeadRunIn  { font-size: 10pt;font-style: italic;font-weight: bold }
  b.cLeadIn2para  { font-weight: bold }
  span.cManRef  { font-family: Courier New, Courier, mono;font-size: 90% }
  b.cStrong  { font-weight: bold }
  b.cStrongItalic  { font-style: italic;font-weight: bold }
  sub.cSubscript  { font-size: 80%;vertical-align: sub }
  sup.cSuperscript  { font-size: 80%;vertical-align: super }
  span.cTitle  { font-style: italic }
  u.cUnderline  { text-decoration: underline }
  i.cVariable  { font-style: italic }
  p.pAnchor  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pBody  { font-family: Arial, Helvetica, ;font-size: 10pt }
  p.pBodyRelative  { font-family: Arial, Helvetica, ;font-size: 10pt }
  p.pBookTitle  { color: #FFFFFF;font-family: Helvetica, sans-serif;font-size: 10pt;font-weight: bold;text-align: right }
  p.pCaption  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold }
  p.pCaptionIndent  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold }
  p.pCaution  { font-family: Arial, Helvetica, ;font-size: 10pt;font-weight: bold }
  div.pCellBody  { font-family: Arial, Helvetica, ;font-size: 10pt }
  div.pCellHeading  { font-family: Arial, Helvetica, ;font-size: 10pt;font-weight: bold;text-align: center }
  p.pCodeline  { font-family: Courier New, Courier, mono;font-size: 13px }
  p.pCodelineIndent  { font-family: Courier New, Courier, mono;font-size: 13px }
  p.pFooter  { font-family: Helvetica, sans-serif;font-size: 9pt }
  span.pGlossaryTerm  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold }
  h2.pGroupTitlesIX  { color: #003366;font-family: Arial, Helvetica, ;font-size: 16px;font-weight: bold }
  h1.pHeading1  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 14pt;font-weight: bold }
  h2.pHeading2  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 12pt;font-weight: bold }
  h3.pHeading3  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 11pt;font-weight: bold }
  h4.pHeading4  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-style: italic;font-weight: bold;margin-bottom: -4pt }
  p.pIndented1  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 2.5em }
  p.pIndented2  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 5em }
  p.pIndented3  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 7.5em }
  p.pIndented4  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 10em }
  p.pIndented5  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 12.5em }
  p.pIndentedRelative  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 2.5em }
  p.pLevel1IX  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 2.5em }
  p.pLevel2IX  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 5em }
  p.pLevel3IX  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 7.5em }
  p.pLevel4IX  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 10em }
  p.pLevel5IX  { font-family: Arial, Helvetica, ;font-size: 10pt;margin-left: 12.5em }
  p.pNavigation  { font-family: Arial, Helvetica, ;font-size: 10pt }
  p.pNote  { font-family: Arial, Helvetica, ;font-size: 10pt;font-weight: bold }
  h2.pNumTOC  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold }
  p.pParagraph  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pParaIndent1  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pParaIndent2  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pParaIndent3  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pPartNumText  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  div.pPreformatted  { font-family: Courier New, Courier, mono;font-size: 9pt }
  div.pPreformattedRelative  { font-family: Courier New, Courier, mono;font-size: 10pt }
  blockquote.pQuotation  { font-family: Arial, Helvetica, ;font-size: 10pt;font-style: italic;margin-left: 2.5em }
  h2.pRefHead1  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 18px;font-weight: bold }
  h3.pRefHead2  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 16px;font-weight: bold }
  h4.pRefHeader  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold;margin-bottom: -4pt }
  p.pRunInHead  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pRunInText  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pScreenText  { font-family: Courier New, Courier, mono;font-size: 9pt }
  div.pSmartList1  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  div.pSmartList2  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  div.pSmartList3  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  div.pSmartList4  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  div.pSmartList5  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pStepParaI1  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pStepParaI2  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pStepParaI3  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pTableCaption  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bolder }
  p.pTableCaptionA  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bolder }
  p.pTableCaptionP  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bolder }
  p.pTableCaptionWide  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bolder }
  p.pTableCaptionWideA  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bolder }
  p.pTableCaptionWideP  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bolder }
  p.pTableFootnote  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 9pt }
  p.pTableHead  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold }
  p.pTableText  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt }
  p.pTableTextRight  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;text-align: right }
  div.pTableTitle  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-style: italic }
  h2.pTitle  { color: #003366;font-family: Arial, Helvetica, ;font-size: 10pt;font-weight: bold }
  h2.pTOC1  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold }
  p.pTOC2  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;margin-left: 2.5em;margin-top: -6pt }
  p.pTOC3  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;margin-left: 5em;margin-top: -6pt }
  p.pTOC4  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;margin-left: 7.5em;margin-top: -6pt }
  p.pTOC5  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;margin-left: 10em;margin-top: -6pt }
  h2.pTOCChapTitle  { font-family: Arial, Helvetica, Verdana, sans-serif;font-size: 10pt;font-weight: bold }
</style>
</head>
<body text="#000000" link="#594FBF" vlink="#9966cc" alink="#333366" bgcolor="#FFFFFF"
      marginheight="0" marginwidth="0" leftmargin="0" rightmargin="0" topmargin="0">
<a name="top"> </a>

<!-- Mast Head -->
<table border="0" cellpadding="0" width="100%" bgcolor="#e5e5e5">
<tr><td>

<table border="0" cellspacing="10" width="100%" bgcolor="#e5e5e5">
<tr>
<td bgcolor="#ffffff">
<font face="helvetica, Arial" color="#594fbf">&#160;
<b>Sun Java System Portal Server Desktop Online Help</b></font></td>
<td bgcolor="#594fbf" width="130"><img src="<%=renderResponse.encodeURL(renderRequest.getContextPath()+"/sunlogo.gif")%>" width="130" height="30"/></td>
</tr>
</table>

</td></tr>
</table>
<!-- End Mast Head -->

<!-- navigation -->
  <table width="100%" border="0" cellspacing="0" summary="Header navigation table">
    <tr><td>&#160;<span class="cContents">     
    </span>
    <hr size="1" noshade="-1" /> 
    </td></tr>
    
  </table>

<!-- end navigation -->
<!-- chapter content -->
<blockquote>

<a name="wp30050"> </a><h1 class="pHeading1">
RSS Portlet Channel
</h1>
<a name="wp30051"> </a><p class="pParagraph">
RSS stands for Really Simple Syndication. It is used to easily distribute a list of headlines, update notices and blogs. RSS feeds notify new and changed content. Most news channels and blogs use the RSS feed feature.
</p>
<a name="wp30059"> </a><p class="pParagraph">
The RSS Portlet allows users to manage a set of RSS feeds and switch between the feeds. The following options are available to customize the RSS Portlet:
</p>
<div class="pSmartList1"><ul class="pSmartList1">
<a name="wp30064"> </a><div class="pSmartList1"><li>Select an RSS feed to view from a set of available feeds</li></div>
<a name="wp30072"> </a><div class="pSmartList1"><li>Add and remove RSS feeds</li></div>
<a name="wp30073"> </a><div class="pSmartList1"><li>Limit the time window of displayed feeds</li></div>
<a name="wp30077"> </a><div class="pSmartList1"><li>Limit the number of feeds to display</li></div>
<a name="wp30078"> </a><div class="pSmartList1"><li>Select if articles are displayed in the current or new browser window</li></div>
</ul></div>
<a name="wp30090"> </a><h2 class="pHeading2">
To Display a RSS Feed
</h2>
<a name="wp30091"> </a><p class="pParagraph">
You can display a specific RSS feed from a set of available RSS feeds. Select the RSS feed from the RSS Portlet drop-down menu. The selected RSS feed is displayed below the drop-down list.
</p>
<a name="wp30097"> </a><p class="pParagraph">
If configured with a single RSS feed, the RSS Portlet will not show the drop-down list.
</p>
<a name="wp30111"> </a><h2 class="pHeading2">
To Customize the RSS Portlet
</h2>
<a name="wp30122"> </a><p class="pParagraph">
The RSS Portlet can be customized to suit your needs. 
</p>
<a name="wp30152"> </a><p class="pParagraph">
To Customize the RSS Portlet, use the following steps:
</p>
<div class="pSmartList1"><ol type="1" class="pSmartList1">
<a name="wp30153"> </a><div class="pSmartList1"><li>Select Help.</li></div>
<a name="wp30154"> </a><p class="pStepParaI1">
The Customize RSS Feeds window appears.
</p>
<a name="wp30155"> </a><div class="pSmartList1"><li>Make a selection or type the values you want.</li></div>
<a name="wp30388"> </a><div class="pSmartList1"><li>Click Finished to save changes and return to the portal desktop. Click Cancel to return the portal desktop without changing preferences.</li></div>
<a name="wp30225"> </a><p class="pStepParaI1">
The following options are available to customize the RSS Portlet:
</p>
<div class="pSmartList2"><ul class="pSmartList2">
<a name="wp30250"> </a><div class="pSmartList2"><li>Start Feed &#8211; Allows you to choose which RSS feed to display at the top of the portlet from the drop-down list.</li></div>
<a name="wp30141"> </a><div class="pSmartList2"><li>Additional RSS Feed &#8211; Allows you to add a new RSS feed to the list of available feeds. Type the URL of the RSS feed in the text box and click Add.</li></div>
<a name="wp30144"> </a><div class="pSmartList2"><li>Maximum Feed Age (Hours) &#8211; Limits the time the RSS feed is displayed in the portlet.</li></div>
<a name="wp30149"> </a><div class="pSmartList2"><li>Disable &#8211; Disables the maximum feed age restriction.</li></div>
<a name="wp30150"> </a><div class="pSmartList2"><li>Maximum Number of Entries &#8211; Allows you to specify the maximum number of feeds to be displayed in the portlet.</li></div>
<a name="wp30151"> </a><div class="pSmartList2"><li>New Windows For Entries &#8211; Allows you to specify if the articles are displayed in a new or the current browser window.</li></div>
</ul></div>
</ol></div>
<a name="wp30263"> </a><h2 class="pHeading2">
To Remove a RSS feed
</h2>
<a name="wp30264"> </a><p class="pParagraph">
You can remove one or more RSS feeds from the list of available feeds.
</p>
<a name="wp30265"> </a><p class="pParagraph">
To remove RSS feeds, use the following steps:
</p>
<div class="pSmartList1"><ol type="1" class="pSmartList1">
<a name="wp30267"> </a><div class="pSmartList1"><li>Click the Edit button.</li></div>
<a name="wp30268"> </a><div class="pSmartList1"><li>Uncheck the RSS feeds you want removed from the list of available RSS feeds.</li></div>
<a name="wp30271"> </a><div class="pSmartList1"><li>Click Finished to save changes and return to the portal desktop. Click Cancel to return the portal desktop without changing preferences.</li></div>
</ol></div>

</blockquote>
<!-- end chapter content -->
<!-- footer -->
<!-- navigation -->

  <table width="100%" border="0" cellspacing="0" summary="Footer navigation table">
    <tr><td>
    <hr size="1" noshade="-1" />
    <span class="cContents">&#160;  
    </span>
    </td></tr>
  </table>
<!-- end navigation -->
<!-- end footer -->
</body>
</html>

