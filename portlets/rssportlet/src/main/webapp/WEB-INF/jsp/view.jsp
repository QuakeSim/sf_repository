<%--
  The contents of this file are subject to the terms
  of the Common Development and Distribution License
  (the License).  You may not use this file except in
  compliance with the License.
 
  You can obtain a copy of the license at
  http://www.sun.com/cddl/cddl.html or
  at portlet-repository/CDDLv1.0.txt.
  See the License for the specific language governing
  permissions and limitations under the License.
 
  When distributing Covered Code, include this CDDL
  Header Notice in each file and include the License file
  at portlet-repository/CDDLv1.0.txt.
  If applicable, add the following below the CDDL Header,
  with the fields enclosed by brackets [] replaced by
  you own identifying information:
  "Portions Copyrighted [year] [name of copyright owner]"
 
  Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 --%>

<%@ page import="javax.portlet.PortletSession" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.sun.portal.rssportlet.FormNames" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<portlet:defineObjects/>
<portlet:actionURL var="actionURL"/>
<fmt:setBundle basename="view"/>    

<!-- This div is meant to compensate for a layout bug in GridSphere -->
<div style="margin-left: 12px">
<c:catch var="exception">    
    <jsp:useBean id="alertHandler" class="com.sun.portal.rssportlet.AlertHandler" scope="session"/>           
    <jsp:useBean id="settingsBean" class="com.sun.portal.rssportlet.SettingsBean"/>    
    <jsp:useBean id="settingsHandler" class="com.sun.portal.rssportlet.SettingsHandler">        
        <jsp:setProperty name="settingsHandler" property="portletRequest" value="<%=renderRequest%>"/>
        <jsp:setProperty name="settingsHandler" property="portletConfig" value="<%=portletConfig%>"/>  
    </jsp:useBean>
    <jsp:useBean id="feedBean" class="com.sun.portal.rssportlet.FeedBean"/>        
    <jsp:useBean id="feedHandler" class="com.sun.portal.rssportlet.FeedHandler"/>            

    <% 
        // jsp doesn't seem to like me doing this in a jsp:setProperty
        settingsHandler.setSettingsBean(settingsBean);
        // so we can get them from a portlet action
        renderRequest.getPortletSession().setAttribute("alertHandler", alertHandler, PortletSession.PORTLET_SCOPE);
    %>    
    
    <!-- don't show feed selector if there is only one feed configured -->
    <c:if test="${settingsBean.feedsSize > 1}">    
        <!-- BEGIN select feed drop down list -->
        <form name="goForm" method="POST" action="<%=actionURL.toString()%>" target="_self">                    
            <table>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td style="vertical-align: top">                    
                        <select name="<%=FormNames.INPUT_SELECT_FEED%>" onChange="goForm.submit()">
                            <c:forEach var="feed" items="${settingsBean.feeds}">
                                <c:choose>
                                    <c:when test="${feed == settingsBean.selectedFeed}">
                                        <option selected value="<c:out value="${feed}"/>">
                                            <div class="portlet-font"><c:out value="${feed}"/></div>
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="<c:out value="${feed}"/>">
                                            <div class="portlet-font"><c:out value="${feed}"/></div>
                                        </option>                    
                                    </c:otherwise>                    
                                </c:choose>
                            </c:forEach>
                        </select>
                    </td>
                    <td style="vertical-align: top">
                        <input class="portlet-form-button" name="<%=FormNames.SUBMIT_GO%>" type="submit" value="<fmt:message key="go"/>">
                    </td>
                </tr>
            </table>
        </form>    
        <!-- END select feed drop down list -->
        <br/>        
    </c:if>

    <c:choose> 
        <c:when test="${settingsBean.selectedFeed != null}">    
            <% 
                // prepare feed bean
                // jsp doesn't seem to like me doing this in a jsp:setProperty
                feedHandler.setSettingsBean(settingsBean);
                feedHandler.setFeedBean(feedBean);
            %>

            <!-- BEGIN feed render -->
            <!-- 
            <table style="border-style: none; padding: 4; width: 100%">
             -->
                <!-- draw feed header -->
                <!-- 
                <table style="border-style: solid; border-width: thin; padding: 8px">
                    <tr><td>
                        <div class="portlet-font">
                            <c:if test="${feedBean.feed.image != null}">
                                <a href="<c:out value="${feedBean.feed.image.link}"/>" target="<c:out value="${settingsBean.windowTarget}"/>"><img border="0" alt="<c:out value="${feedBean.feed.image.link}"/>" src="<c:out value="${feedBean.feed.image.url}"/>"/></a>    
                                <br/>
                            </c:if>
                            -->
                            <c:choose>
                              <c:when test="${settingsBean.overrideTitle != null}">
                                <c:set var="blogTitle" value="${settingsBean.overrideTitle}" />
                              </c:when>
                              <c:otherwise>
                                <c:set var="blogTitle" value="${feedBean.feed.title}" />
                              </c:otherwise>
                            </c:choose>
                            <h1>
                            <c:if test="${! settingsBean.noBlogLinks}">
                              <a href="<c:out value="${feedBean.feed.link}"/>" style="font-weight: bolder" target="<c:out value="${settingsBean.windowTarget}"/>"><c:out value="${blogTitle}"/></a>
                            </c:if>
                            <c:if test="${settingsBean.noBlogLinks}">
                              <span style="font-weight: bolder"><c:out value="${blogTitle}"/></span>
                            </c:if>
                            </h1>
                            <!-- 
                            <c:if test="${feedBean.feed.publishedDate != null}">                                
                                - <span style="font-style: italic"><fmt:formatDate value="${feedBean.feed.publishedDate}"/></span>
                            </c:if>
                            <c:if test="${feedBean.feed.description != null}">    
                                <br/>
                                <c:out value="${feedBean.feedDescription}" escapeXml="false"/>                                        
                            </c:if>
                        </div>
                    </td></tr>
                </table>
                -->
                
                <!-- 
                <tr><td>&nbsp;</td></tr>    
                 -->

            <table width="647" border="0" cellspacing="0" cellpadding="0" height="108">                 
                <c:set var="entries" value="${feedBean.feedEntries}"/>
                <c:choose>
                  <c:when test="${feedBean.feedEntriesSize == 3 && ! settingsBean.showDescription }">
                    <tr><td class="boxNews">
                      <!-- START draw feed entries -->   
                      <c:forEach var="entry" items="${entries}">
                              <div class="newsItem">
                                  <c:choose>
                                    <c:when test="${settingsBean.overrideLink != null}">
                                      <span style="font-weight: bolder;"><a href="<c:out value="${settingsBean.overrideLink}"/>" target="<c:out value="${settingsBean.windowTarget}"/>"><c:out value="${entry.title}"/></a></span>
                                    </c:when>
                                    <c:otherwise>
                                      <c:if test="${! settingsBean.noBlogLinks}">
                                        <span style="font-weight: bolder;"><a href="<c:out value="${entry.link}"/>" target="<c:out value="${settingsBean.windowTarget}"/>"><c:out value="${entry.title}"/></a></span>
                                      </c:if>
                                      <c:if test="${settingsBean.noBlogLinks}">
                                        <span style="font-weight: bolder;"><c:out value="${entry.title}"/></span>
                                      </c:if>
                                    </c:otherwise>
                                  </c:choose>
                                  <c:if test="${entry.publishedDate != null}">
                                      - <span style="font-style: italic"><fmt:formatDate value="${entry.publishedDate}"/></span>      
                                  </c:if>
                                  <c:if test="${settingsBean.showDescription}">
                                    <br>
                                    <!-- div style="font-size: smaller"-->
                                    <c:out value="${entry.description.value}" escapeXml="false"/>
                                    <!-- /div -->
                                  </c:if>
                              </div>
                      </c:forEach>
                      <!-- END draw feed entries -->
                    </td></tr>
                  </c:when>
                  <c:otherwise>
                    <tr><td><ul>
                      <!-- START draw feed entries -->   
                      <c:forEach var="entry" items="${entries}">
                              <li class="listAnchor">
                                  <c:choose>
                                    <c:when test="${settingsBean.overrideLink != null}">
                                      <span style="font-weight: bolder;"><a href="<c:out value="${settingsBean.overrideLink}"/>" target="<c:out value="${settingsBean.windowTarget}"/>"><c:out value="${entry.title}"/></a></span>
                                    </c:when>
                                    <c:otherwise>
                                      <c:if test="${! settingsBean.noBlogLinks}">
                                        <span style="font-weight: bolder;"><a href="<c:out value="${entry.link}"/>" target="<c:out value="${settingsBean.windowTarget}"/>"><c:out value="${entry.title}"/></a></span>
                                      </c:if>
                                      <c:if test="${settingsBean.noBlogLinks}">
                                        <span style="font-weight: bolder;"><c:out value="${entry.title}"/></span>
                                      </c:if>
                                    </c:otherwise>
                                  </c:choose>
                                  <c:if test="${entry.publishedDate != null}">
                                      - <span style="font-style: italic"><fmt:formatDate value="${entry.publishedDate}"/></span>      
                                  </c:if>
                                  <c:if test="${settingsBean.showDescription}">
                                    <br>
                                    <!-- div style="font-size: smaller"-->
                                    <c:out value="${entry.description.value}" escapeXml="false"/>
                                    <!-- /div -->
                                  </c:if>
                              </li>
                      </c:forEach>
                      <!-- END draw feed entries -->
                    </ul></td></tr>
                  </c:otherwise>
                </c:choose>
 
                <c:if test="${feedBean.feedEntriesSize == 0}">
                    <tr><td>
                        <div class="portlet-msg-alert">
                            <c:choose>
                                <c:when test="${settingsBean.disableMaxAge}">
                                    <fmt:message key="no_entries"/>                        
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="no_entries_since" ><fmt:param value="${settingsBean.maxAge}"/></fmt:message>                        
                                </c:otherwise>
                            </c:choose>                    
                        </div>
                    </td></tr>
                </c:if>
            </table>
        </c:when>
        <c:otherwise>
            <fmt:message key="no_feeds"/>	
        </c:otherwise>
    </c:choose>
    <!-- END feed render -->
</c:catch>

<c:if test="${exception != null}">
    <% 
        Throwable t = (Throwable)pageContext.getAttribute("exception");
        StringWriter st = new StringWriter();
        PrintWriter pw = new PrintWriter(st);
        t.printStackTrace(pw);
        portletConfig.getPortletContext().log("exception occured in view.jsp", t);
    %>
    <div class="portlet-msg-error" style="color: red">
        <c:choose>
            <c:when test="${settingsBean.selectedFeed != null}">
                <fmt:message key="exception_url"><fmt:param value="${settingsBean.selectedFeed}"/></fmt:message>
            </c:when>
            <c:otherwise>
                <fmt:message key="exception"/>
            </c:otherwise>
        </c:choose>
        <div style="font-size: smaller">            
            <pre><%= t.getMessage() %></pre>
        </div>
    </div>
   <!--
   <%= st %>
   -->       
</c:if>

</div>
